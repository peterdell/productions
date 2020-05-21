
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;	Make sure the latest version of "cartmenu.rom" is in the "/src/data" folder.
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

	.proc extended_menu

screen_width	= 40
screen_lines	= 25

result_lines	= 20
result_sm	= sm+screen_width*2


genres_list 	  = menu_data_start	;Up to menu_mcb.menu_genres_count genre records, followed by the genre names 
genres_list_size  = $400
genre_sm	  = genres_list+genres_list_size ;Screen memory for the genre line, $400 bytes
found_entries	  = genre_sm+$400	;Numbers (lo/hi) of entries that have been found, 16384*2 = 32768 bytes
found_entries_end = menu_data_end

;===============================================================

	.macro m_convert_menu_entry_number	;Convert menu_entry_number to menu bank_number and bank_address, parameter 1 is the offset

;	Capital letters are variable E=entry, B=bank, A=address, lower case letters are bit number "f..0".
;	00000000000000Ef EeEdEcEbEaE9E8E7 000000E6E5E4E3E2 E1E0000000000000
;	BfBeBdBcBbBaB9B8 B7B6B5B4B3B2B1B0 AfAeAdAcAbAaA9A8 A7A6A5A4A3A2A1A0

	lda menu_entry_number
	asl
	lda menu_entry_number+1
	rol
	sta bank_number
	lda #0
	sta bank_address
	rol
	sta bank_number+1

	adw bank_number menu_mcb.menu_entries_start_bank_number 

	lda menu_entry_number
	and #$7f
	lsr
	ror bank_address
	lsr
	ror bank_address
	clc
	adc #>module_a000
	sta bank_address+1

	adw bank_address #:1
	.endm

	.macro m_convert_found_entry_index	;:1 found_entry_index, :2 found_entry_pointer
	mwa :1 :2
	asl :2
	rol :2+1
	lda :2+1
	adc #>found_entries
	sta :2+1
	.endm
;===============================================================

	.proc main			;This is the first part executed in RAM

	jmp skip_signature

	.byte 'The!Cart Extended Menu',0

skip_signature

	lda consol			;Holding down SELECT disables the cartrigde and performs coldstart
	and #2
	jeq starter.cold_start

	lda consol			;Holding down OPTION starts simple menu
	and #4
	jeq starter.start_simple_menu	;This only works for "The!Cart", not yet for Atarimax

	jsr init			;Init extended menu scree

	jsr search.loop
	jsr exit
	jmp starter

	.endp				;End of main

;===============================================================

	.proc init
	lda #0				;Deactivate screen at first
	sta sdmctl
	sta dmactl

	sei				;Disable VBI stage 2
	mva #1 result.semaphore		;Lock VBI semaphore

	mwa vvblki exit.saved_vvblki
	mwa vvblkd exit.saved_vvblkd

	lda #6				;Setup immediate VBI
	ldx #>nmi.vbi.immediate
	ldy #<nmi.vbi.immediate
	jsr setvbv

	lda #7				;Setup deferred VBI
	ldx #>nmi.vbi.deferred
	ldy #<nmi.vbi.deferred
	jsr setvbv

	jsr genres.init
	jsr input.init
	jsr cursor.init

	jsr visualization.screen.init
	jsr visualization.menu.init
	jsr visualization.menu.print_menu_title
	jsr visualization.menu.print_selected_genre
	jsr visualization.menu.print_menu_entries_count
	jsr visualization.menu.print_welcome

	jsr result.clear_result_screen
	jsr result.clear_result_lines

	jsr visualization.screen.activate

	mva #1 input.input_changed	;Trigger search

	mva #0 result.semaphore		;Unlock VBI semaphore
	cli				;Enable VBI stage 2
	rts

	.endp				;End of init

;===============================================================

	.proc exit
	.var saved_vvblki .word
	.var saved_vvblkd .word

	mva #1 result.semaphore

	jsr visualization.screen.exit

	sei			;Prevent VBI stage 2 before disabling The!Cart
	mva #0 the_cart.primary_bank_enable
	mva trig3 gintlk	;Make sure cartridge status is up to date
	cli			;Enable VBI state 2 again

	lda #7			;Restore old deferred VBI
	ldx saved_vvblkd+1
	ldy saved_vvblkd
	jsr setvbv

	lda #6			;Restore old immeditate VBI
	ldx saved_vvblki+1
	ldy saved_vvblki
	jsr setvbv

	ldx #$00		;Open E: via CIO to ensure ICBOs are setup correctly
	mva #cio_command.close iccom,x
	jsr ciov

	mva #cio_command.open iccom,x
	mva #12 icax1,x
	mwa #editor icbal,x
	jsr ciov
	rts

	.local editor
	.byte 'E:',$9b
	.endl

	.endp			;End of exit


;===============================================================

	.proc nmi

	.proc vbi

	.proc immediate
	mva trig3 gintlk	;Prevent lockup when module is switched on/off. 
	mva sdmctl dmactl	;Ensure DMA control is ready to fetch actual DL
	mwx chbas  chbase	;Ensure font is ready, too
	and #$20		;Check if DL fetching is active
	beq no_dli
	mva #$c0 nmien		;Enable DLIs only when DL fetching is active	
no_dli
	jsr visualization.screen.nmi.vbi
	jsr search.increment_frame_counter

	jmp sysvbv 		;Perform rest of standard VBI
	.endp			;End of immediate

;===============================================================
	
	.proc deferred		;Perform actual menu handling 

	cli			;Don't block shadow register updates

	jsr visualization.result_status.print_found_entry_index

	jsr input.handle_keys	;Handle keyboard input for search terms

	lda input.input_help_mode
	bne semaphore_locked	;Do nothing in help mode
	lda result.semaphore
	bne semaphore_locked	;Do nothing if semaphore is locked

	mva #1 result.semaphore
	
	jsr menu_core.get_bank
	stx old_bank_lo
	sty old_bank_hi

	.proc logic
	lda input.input_changed	;Any pending input?
	beq input_unchanged
	mva #0 input.input_changed
	jsr result.clear_result_lines
	jsr result.start_search_for_input
input_unchanged

	jsr cursor.move_cursor	;Handle joystick and cursor keys

	lda controls.stick
	and #joystick.fire
	bne no_fire_or_return
	mva #search.status.start_selected_entry search.search_status
no_fire_or_return

	jsr result.update_top_line	;Compute visible scroll area
	jsr result.update_result_lines

	.endp

old_bank_lo = *+1
	ldx #$00
old_bank_hi = *+1
	ldy #$00
	jsr menu_core.set_bank

	mva #0 result.semaphore
semaphore_locked

	jmp xitvbv
	.endp			;End of deferred

;===============================================================

	.proc wait_next_vbi
	lda $14
loop	cmp $14
	beq loop
	rts
	.endp

;===============================================================

	.endp		;End of vbi

	.endp		;End of nmi

;===============================================================

	.proc genres
	.proc init

	ldx menu_mcb.menu_genres_start_bank_number
	ldy menu_mcb.menu_genres_start_bank_number+1
	jsr menu_core.set_bank

	mwa #module_a000 p1
	mwa #genres_list p2
	ldx #>genres_list_size
	ldy #0
loop	lda (p1),y
	sta (p2),y
	iny
	bne loop
	inc p1+1
	inc p2+1
	dex
	bne loop
	rts
	.endp
	
;===============================================================

	.proc compute_adr		; IN: <A>=genre number, OUT: p1=genre address, does not change <X>
	sta p1
	lda #0
	asl p1
	rol
	asl p1
	rol
	sta p1+1
	adw p1 #genres_list
	rts
	.endp

;===============================================================

	.proc compute_genre_text_start_adr	; IN: <A>=genre number, OUT: p1=text start address, <A>=text_length, does not change <X>
	jsr compute_adr
	ldy #menu_genre.text_length
	lda (p1),y
	pha
	ldy #menu_genre.text_offset
	lda (p1),y
	clc
	adc #<genres_list
	pha
	iny
	lda (p1),y
	adc #>genres_list
	sta p1+1
	pla
	sta p1
	pla
	rts
	.endp

	.proc compute_genre_text_end_adr	; IN: <A>=genre number, OUT: p1=text end address, does not change <X>
	jsr compute_genre_text_start_adr
	clc
	adc p1
	sta p1
	lda p1+1
	adc #0
	sta p1+1
	dew p1
	rts
	.endp
	
	.endp

;===============================================================

	.proc result

	.enum status
	searching		= $00
	not_searching		= $01
	finished_complete	= $81
	finished_incomplete	= $82
	finished_user_break	= $83
	.ende

semaphore		= vbi_zp	;Byte
result_status		= vbi_zp+1	;Byte, synchronized via vbi.semaphore
found_entry_index	= vbi_zp+2	;Word, synchronized via vbi.semaphore
found_entry_pointer	= vbi_zp+4	;Word, synchronized via vbi.semaphore
top_entry_index		= vbi_zp+6	;Word
top_entry_pointer	= vbi_zp+8	;Word
menu_entry_number	= vbi_zp+10	;Word
bank_number		= vbi_zp+12	;Word
bank_address		= vbi_zp+14	;Word
screen_address		= vbi_zp+16	;Word

;===============================================================

	.proc clear_result_screen		;Clear result screen
	lda #' '
	ldx #screen_width-1
loop
	.rept result_lines
	sta result_sm+#*screen_width,x
	.endr

	dex
	bpl loop
	rts
	.endp

;===============================================================

	.proc clear_result_lines	;Clear result list
	ldx #result_lines-1
clear_result_lines
	lda #$ff
	sta displayed_entries.lo,x
	sta displayed_entries.hi,x
	jsr visualization.menu.clear_entry_colors
	dex
	bpl clear_result_lines

	mva #status.not_searching result_status
	rts
	.endp

;===============================================================
	.proc start_search_for_input	;Mark result as incomplete and start searching first entry with current input

	mwa #0 found_entry_index	;Start with first entry
	mwa #found_entries found_entry_pointer
	mva #status.searching result.result_status
	jsr visualization.result_status.print_searching

	jsr cursor.clear_cursor
	mva #search.status.restart search.search_status

	.proc copy_input_buffer		;Copy input buffer to search
	ldx input.input_length		;Copy length
	stx search.input_length
loop	dex				;Copy characters
	bmi done
	mva input.input_buffer,x search.input_buffer,x
	jmp loop
done
	.endp
	rts

	.endp

;===============================================================

	.proc update_top_line
	middle_line = result_lines/2	;Middle line of the screen

	lda found_entry_index
	ora found_entry_index+1
	bne found_something
	lda #$ff 			;Nothing found, clear cursor line
	bne set_cursor_line

found_something
	sbw cursor.cursor_line #middle_line top_entry_index
	bcs more

	clc
	lda top_entry_index
	adc #middle_line
	mwx #0 top_entry_index
	jmp set_cursor_line

more	lda #middle_line

set_cursor_line
	jsr visualization.menu.set_cursor_line	;IN: <A>=cursor line, $ff for cursor off
	rts
	.endp

;===============================================================

	.proc update_result_lines	;Update the result list
	.var line_number .byte		;Line number of the currently processed line
	.var entry_counter .word	;Number of visible entries that can be displayed from the result

	m_convert_found_entry_index top_entry_index top_entry_pointer

	mwa found_entry_index $0002
	mwa top_entry_index $0004
	
	sbw found_entry_index top_entry_index entry_counter
	bcs no_underflow
	mwa #0 entry_counter		;Make negative value to 0
no_underflow
	lda entry_counter+1
	beq no_high_byte
	mva #$ff entry_counter		;Limit entry_counter to byte value
no_high_byte

	mwa entry_counter $0006
	
	ldx #0
display_entry_loop
	cpx entry_counter		;Any more entries to display? (only low byte is relevant)
	bcs clear_entry			;No more entries found, clear remaining entries on screen

	ldy #0				;Check if number from the result and currently displayed number are different
	lda (top_entry_pointer),y
	cmp displayed_entries.lo,x
	bne display_entry		;Yes, display entry
	iny
	lda (top_entry_pointer),y
	cmp displayed_entries.hi,x
	bne display_entry		;Yes display entry

display_entry_next
	adw top_entry_pointer #2	;Next entry in the result
	inx
	cpx #result_lines
	bne display_entry_loop
	rts

;===============================================================

	.proc clear_entry
	lda displayed_entries.hi,x	;Already cleared?
	cmp #$ff
	beq display_entry_next

	lda #$ff			;Mark entry as cleared
	sta displayed_entries.lo,x
	sta displayed_entries.hi,x

	jsr visualization.menu.clear_entry_colors;IN: <X>=line
	jmp display_entry_next
	.endp

;===============================================================

	.proc display_entry
	stx line_number			;Remember current line

	ldy #0				;Copy next found entry number to displayed entries and menu_entry_number
	lda (top_entry_pointer),y
	sta displayed_entries.lo,x
	sta menu_entry_number
	iny
	lda (top_entry_pointer),y
	sta displayed_entries.hi,x
	sta menu_entry_number+1

	m_convert_menu_entry_number menu_entry.title

	lda screen_lines.lo,x
	sta screen_address
	lda screen_lines.hi,x
	sta screen_address+1

	ldx bank_number			;This changes <X>
	ldy bank_number+1
	jsr menu_core.set_bank

	ldy #screen_width-1		;Copy entry title
loop
	.rept 4
	lda (bank_address),y
	sta (screen_address),y
	dey
	.endr
	bpl loop

	ldx line_number			;Set entry colors
	jsr visualization.menu.set_entry_colors	;IN: <X>=line

	jmp display_entry_next
	.endp

;===============================================================


	.endp			;End of update_result_lines

;===============================================================

	.local screen_lines	;Screen line addresses
	.local lo
	.rept result_lines
	.byte <[result_sm+#*screen_width]
	.endr
	.endl			;End of lo
	.local hi
	.rept result_lines
	.byte >[result_sm+#*screen_width]
	.endr
	.endl			;End of hi
	.endl

	.local displayed_entries ;Numbers of visible entries that are already displayed	
	.local lo
	.ds result_lines
	.endl
	.local hi
	.ds result_lines
	.endl
	.endl
	
	.endp		;End of result

;===============================================================

	.proc search

	.enum status
	searching		= $00
	start_simple_menu 	= $01
	start_selected_entry	= $02
	restart			= $03
	.ende

	.var search_status .byte		;Public, write access
	.var input_length .byte			;Public, write access
input_buffer .ds input.input_max_length		;Public, write access
	.var search_genre_number .byte		;Private, filled when search is started
	.var search_genre_favorite_flag .byte	;Private, filled when search is started
	.var search_entry_max_index .word	;Private, maximum number of entries that fit into found_entries
	.var search_entry_index	.word		;Private, counting up, read access by visualization
	.var search_entry_counter .word		;Private, counting down, read access by visualization
	.var search_bank_number	.word		;Private, read access by visualization
	.var search_frame_counter .word		;Private, see increment_frame_counter, read access by visualization

;===============================================================

	.proc loop				;Main search loop

search_restart
	jsr nmi.vbi.wait_next_vbi		;Wait for VBI processing to complete search_frame_counter handling
	jsr init_search_entries

search_loop
	jsr search_next_entry
	lda search_status			;Cancelled by user selection?
	beq search_loop
	cmp #status.restart
	beq search_restart

	rts

;===============================================================

	.proc init_search_entries
	mva #status.searching search_status	;Start searching	

	mva cursor.selected_genre.number search_genre_number
	jsr genres.compute_adr
	ldy #menu_genre.flags
	lda (p1),y
	and #$80
	sta search_genre_favorite_flag

;	Compute number of entries that will fit into the found_entries
;	The end of the physical ram is store in memtop.
;	While ramtop ($6a) and ramsiz ($2e4) are changed to $c0 during
;	startup, the value of memtop ($2e5/$2e6) is unchanged here.
	lda memtop+1
	sec
	sbc #>found_entries
	lsr
	sta search_entry_max_index+1

	mwa #0 search_entry_index
	mwa menu_mcb.menu_entries_count search_entry_counter
	mwa menu_mcb.menu_entries_start_bank_number search_bank_number
	mwa #module_a000 search_pointer
	
	mwa #0 search_frame_counter

	mva #$ff brkkey

	ldx search_bank_number			;Activate first bank to search in
	ldy search_bank_number+1
	jsr menu_core.set_bank
	rts

	.endp

;===============================================================

	.proc search_next_entry

	lda result.result_status		;Dirty read first
	jne not_searching_or_finished		;Not searching or finished

	mva #1 result.semaphore			;Synchronized read to make sure
	lda result.result_status		;Check is result is already complete?
	jne not_searching_or_finished

	lda search.search_status		;Check if search shall still be continued?
	cmp #status.searching
	jne not_searching_or_finished

	lda search_entry_counter		;Check if there are any more entries in the menu?
	ora search_entry_counter+1
	bne not_finished_complete		;Mark result as complete?
	mva #result.status.finished_complete result.result_status
	jsr visualization.result_status.print_search_finished_complete
	jmp finished				;Done with complete result.

not_finished_complete
	lda brkkey
	bne not_finished_user_break
	mva #result.status.finished_user_break result.result_status
	jsr visualization.result_status.print_search_finished_user_break
	jmp finished				;Done with incomplete result.

not_finished_user_break
	ldy #menu_entry.genre_number
	lda search_genre_number
	beq not_specific_genre			;If the search genre is not specified, continue with comparing the title
	cmp (search_pointer),y			;If the search genre is specific...
	jne entry_not_matching			;... but it's different from the entry genre, then it's no match

not_specific_genre
	ldx input_length			;Empty string matches everything
	beq find_all_or_favorites		;--- or favorites
	stx search_substring.compare_input_length ;Self-modifying code to save cycles

;	ldx #0					;Find first substring
;	.proc find_substring_start
;loop	lda input_buffer,x
;	cmp #' '
;	bne found
;	inx
;	cpx input_length
;	bne loop
;	jmp search_substring.substring_not_matching
;found
;	.endp

;	stx substring_start
;find_substring_loop
;	lda input_buffer,x
;	cmp #' '
;	beq substring_end_found
;	inx
;	cpx input_length
;	bne find_substring_loop
;substring_end_found

	.proc search_substring
	title_position = x1			;ZP variables

	mva #menu_entry.title title_position	;Compare entry with input buffer
	ldy #menu_entry.title_length		;end_position = title_position+<title_length>
	adc (search_pointer),y
	sta compare_title_end_position		;Self-modifying code to save cycles

compare_next_position
	ldx #0					;Start position in the user input
	ldy title_position			;Start position in the entry title

compare_next_char

compare_title_end_position = *+1		;End position of the entry title
	cpy #$ff
	bcs entry_not_matching
	lda (search_pointer),y			;Normalize characters to upper-case
	cmp #'a'
	bcc no_lower_case
	cmp #'z'+1
	bcs no_lower_case
	sbc #$20-1
no_lower_case
	cmp input_buffer,x			;Compute with upper-case input buffer
	bne no_char_match_found			;Restart compare for substring at next title position
	iny
	inx
compare_input_length = *+1			;End position of the user input
	cpx #$ff
	bcc compare_next_char
	bcs matching_subtring_found		;All characters compared

no_char_match_found				;Step one character to the right in the title and search again
	inc title_position
	jmp compare_next_position

substring_not_matching
	jmp entry_not_matching

matching_subtring_found
	.endp					;End of search_substring

find_all_or_favorites
	lda search_genre_favorite_flag		;Restrict to favorites?
	beq matching_entry_found		;... if not, we ave a match
	lda input_length			;User input available?
	bne matching_entry_found		;... then inore favorites filter
	ldy #menu_entry.favorite_indicator	;Restricted but no user input, so check...
	lda (search_pointer),y			;... if favorite flag is set
	bpl entry_not_matching			;... and skip if not set

;TODO TODO TODO
	.proc matching_entry_found
	lda result.found_entry_index+1 		;Check if there is space for more found entries in the list
	cmp search_entry_max_index+1
	bcc not_finished_incomplete		;Mark result as incomplete?
	mva #result.status.finished_incomplete result.result_status
	jsr visualization.result_status.print_search_finished_incomplete
	jmp finished				;Done with incomplete result.
not_finished_incomplete

	ldy #0					;Enter current search_entry_index in the found_entries
	lda search_entry_index
	sta (result.found_entry_pointer),y
	iny
	lda search_entry_index+1
	sta (result.found_entry_pointer),y

	lda result.found_entry_index		;If the first entry is found, the cursor becomes releveant
	ora result.found_entry_index+1		;and the data for the entry must be selected
	bne not_first_rentry
	jsr cursor.get_selected_entry.some_entries_found	;Cursor line is zero at this point
not_first_rentry

	inw result.found_entry_index		;Step to next free found_entry index
	adw result.found_entry_pointer #2	;Step to next free found_entry address
	.endp					;End of match_found

entry_not_matching

next_entry
	inw search_entry_index			;Increment entries index
	dew search_entry_counter		;Decrement remaining entries counter

	clc					;Increment pointer and bank number
	lda search_pointer
	adc #menu_entry_size
	sta search_pointer
	lda search_pointer+1
	adc #0
	sta search_pointer+1
	cmp #>[module_a000+bank_size]
	bcc not_next_bank
	lda #>module_a000
	sta search_pointer+1
	inw search_bank_number
	ldx search_bank_number			;Activate bank of the entry to search in
	ldy search_bank_number+1
	jsr menu_core.set_bank
not_next_bank

finished

not_searching_or_finished
	mva #0 result.semaphore

	rts
	.endp			;End of next

	.endp			;End of loop

;===============================================================

	.proc increment_frame_counter
	lda search_status		;Check if search shall still be continued?
	cmp #status.searching
	bne not_searching
	inw search_frame_counter
not_searching
	rts
	.endp

;===============================================================

	.endp				;End of search

;===============================================================

	icl "TheMenu-Controls.asm"

	icl "TheMenu-Cursor.asm"

	icl "TheMenu-Input.asm"

	icl "TheMenu-Visualization.asm"

	icl "TheMenu-Starter.asm"

;===============================================================
end_of_code

	m_assert_end_of_code data_start

	.endp				;End of extended_menu
