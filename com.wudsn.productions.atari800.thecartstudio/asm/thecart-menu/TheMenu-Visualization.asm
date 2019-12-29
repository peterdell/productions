
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

;===============================================================

	.proc visualization

result_dli	= 3

cnt		= $14	;OS frame counter

	.proc screen

	.var vdslst_old .word

	.proc init
	mwa #dl sdlstl			;Setup display
	mwa vdslst vdslst_old		;Save old DLI vector
	mwa #nmi.dli vdslst		;Activate DLI

	lda #$00
	sta color4

	lda #14
	sta pcolor0			;White border (PM)
	sta pcolor1			;White border (PM)
	sta color0			;White border (SM)

	lda #12
	sta color3			;Header
	sta screen.colors.colpf1	;First line
	sta screen.colors.colpf1+1	;Second line
	lda #14
	sta screen.colors.colpf1+2	;Third line
	lda #12
	sta screen.colors.colpf1+23	;First status line
	sta screen.colors.colpf1+24	;Second status line

	lda #$72
	sta screen.colors.colpf2
	adc #$02
	sta screen.colors.colpf2+1
	adc #$02
	sta screen.colors.colpf2+2
	lda #$72
	sta screen.colors.colpf2+23
	sta screen.colors.colpf2+24

	pm_offset = $1e			;Setup PM border lines
	pm_lines = 206
	mva #>pm pmbase
	mva #3 gractl
	mva #$2f hposp0
	mva #$d0 hposp1
	lda #0
	tax
clear_pm
	sta pm+$400,x
	sta pm+$500,x
	inx
	bne clear_pm
	lda #$80
fill_pm
	sta pm+$400+pm_offset,x
	sta pm+$500+pm_offset,x
	inx
	cpx #pm_lines
	bne fill_pm

	ldx #0
copy_chr				;Create ASCII charset layout
	lda $e000,x
	sta chr+$100,x
	lda $e100,x
	sta chr+$200,x
	lda $e200,x
	sta chr+$000,x
	lda $e300,x
	sta chr+$300,x
	inx
	bne copy_chr

	lda #' '
clear_sm
	sta sm,x			;Clear screen
	sta sm+$100,x
	sta sm+$200,x
	sta sm+$300,x
	inx
	bne clear_sm
	rts
	.endp

;===============================================================

	.proc activate
	mva #$3e sdmctl			;Activate screen in next frame
	rts
	.endp

;===============================================================

	.proc exit
	mva #$40 nmien			;Disable DLIs
	mwa vdslst_old vdslst		;Restore original DLI vector

	lda #0				;Hide PMs
	sta hposp0
	sta hposp1
	sta pcolor0			;Reset PM colors
	sta pcolor1
	sta gractl			;Reset PM graphics
	sta grafp0
	sta grafp1	
	rts

	.endp

;===============================================================

	.proc nmi

	.proc vbi
	mva #0 dli.count
	lda #0
	sta dli.cursor_color1
	lda cnt
	lsr
	lsr
	and #15
	tax
	cpx #.len colors
	scc
	ldx #0
	lda colors,x
	sta dli.cursor_color2

	lda $13
	lsr
	lda cnt
	ror	
	and #$f0
	ora #12
	sta color3
	rts
	
	.local colors
	.byte 12,12,12,10,8,6,8,10
	.endl
	.endp

;===============================================================

	.proc dli
	pha
	txa
	pha
	sta wsync
count = *+1
	ldx #0
cursor_line = *+1
	cpx #$ff
	beq set_cursor
colpf1_adr = *+1
	lda screen.colors.colpf1,x
	sta colpf1
colpf2_adr = *+1
	lda screen.colors.colpf2,x
	sta colpf2
	lda #>chr		;Activate ASCII charset
	sta chbase
dli_end	inc count
	pla
	tax
	pla
	rti

set_cursor

cursor_color1 = *+1
	lda #0
	sta colpf1
cursor_color2 = *+1
	lda #0
	sta colpf2
	jmp dli_end

	.endp			;End of dli

	.endp			;End of nmi
 
;===============================================================

	.align $400

	.local header	
	.byte "      ";
	.by +$a0 "THE!CART"
	.byte "      "
	.endl

	.local line
:40	.byte $55
	.endl

	.local welcome
	.byte '  Welcome! Press HELP for instructions. '
	.endl

	.local dl
	.byte $60,$46,a(header),$80+$60
	.byte $4e,a(line)
	.byte $80+$42,a(sm)
	.byte $4e,a(line)
	.byte $80+$42,a(sm+screen_width*1)
	.byte $4e,a(line)
	.byte $80+$42,a(genre_sm)
	.byte $4e,a(line)
	.byte $80+$42,a(sm+screen_width*2)
:19	.byte $80+$02
	.byte $4e,a(line)
	.byte $80+$42,a(sm+screen_width*22)
welcome_lms = *+1
	.byte $00+$42,a(welcome)
	.byte $4e,a(line)
	.byte $41,a(dl)
	.endl

	m_assert_same_1k dl

	.local colors
	.local colpf1
:screen_lines	.byte 0
	.endl
	m_assert_same_page colpf1

	.local colpf2
:screen_lines	.byte 0
	.endl
	.endl

	m_assert_same_page colpf2

	.local help_dl			;Help screen display list

	.macro m_empty_4_lines
	.byte $4f,a(help_sm.empty_line)
	.byte $4f,a(help_sm.empty_line)
	.byte $4f,a(help_sm.empty_line)
	.byte $80+$4f,a(help_sm.empty_line)
	.endm

	.byte $60,$46,a(header),$80+$60
	.byte $4e,a(line)
	.byte $80+$42,a(help_sm)
	.byte $4e,a(line)
	.byte $80+$42,a(help_sm+screen_width*1)
	.byte $4e,a(line)
	.byte $80+$42,a(help_sm+screen_width*2)
	.byte $4e,a(line)
	m_empty_4_lines
	.byte $80+$42,a(help_sm+screen_width*3)
	.byte $80+$42,a(help_sm+screen_width*4)
	m_empty_4_lines
	m_empty_4_lines
	.byte $80+$42,a(help_sm+screen_width*5)
	.byte $80+$42,a(help_sm+screen_width*6)
	m_empty_4_lines
	m_empty_4_lines
	.byte $80+$42,a(help_sm+screen_width*7)
	.byte $80+$42,a(help_sm+screen_width*8)
	.byte $80+$42,a(help_sm+screen_width*9)
	m_empty_4_lines
	m_empty_4_lines
	.byte $80+$42,a(help_sm+screen_width*10)
	.byte $80+$42,a(help_sm+screen_width*11)
	.byte $80+$42,a(help_sm+screen_width*12)
	.byte $80+$42,a(help_sm+screen_width*13)
	.byte $80+$42,a(help_sm+screen_width*14)
	.byte $80+$42,a(help_sm+screen_width*15)
	.byte $80+$42,a(help_sm+screen_width*16)
	.byte $80+$42,a(help_sm+screen_width*17)
	.byte $80+$42,a(help_sm+screen_width*18)
	m_empty_4_lines
	.byte $4e,a(line)
	.byte $80+$42,a(help_sm+screen_width*19)
	.byte $00+$42,a(help_sm+screen_width*20)
	.byte $4e,a(line)
	.byte $41,a(help_dl)
	.endl

	m_assert_same_1k help_dl

	.local help_sm			;Help screen content
	.byte ' This line contains the workbook title. '
	.byte '  This line contains the search term.   '
	.byte '  This line contains the genre names.   '
	.byte '   Welcome to The!Cart extended menu.   '
	.byte '  You can use the following controls.   '
	.byte ' Keyboard to type the search term:      '
	.byte '  DELETE          - Delete last input   '
	.byte ' Keyboard or joystick to select genre:  '
	.byte '  LEFT  or +      - Previous genre      '
 	.byte '  RIGHT or *      - Next genre          '
	.byte ' Keyboard or joystick to select entry:  '
	.byte '  UP    or -      - Cursor line up      '
	.byte '  DOWN  or =      - Cursor line down    '
	.byte '  FIRE  or RETURN - Start entry         '
	.byte '      with CONTROL- Start entry''s menu  '
	.byte '      with SHIFT  - Don''t lock The!Cart '
	.byte '                                        '
	.byte '                                        '
	.byte '                                        '
	.byte '  TAB             - Toggle details mode '
	.byte '   The!Cart Studio by Peter Dell/JAC!   '

	.local empty_line
:40	.byte 0
	.endl	
	.endl

	.local help_colors		;Help screen text colors
	
	.local colpf1
:screen_lines+4	.byte 14
	.endl
	m_assert_same_page colpf1

	.local colpf2
	.byte $72,$74,$76
	.byte $34,$34,$34,$34
	.byte $44,$44,$44,$44,$54
	.byte $54,$54,$54,$54
	.byte $64,$64,$64,$64,$64,$64,$64,$64,$64,$64,$64
	.byte $72,$72
	.endl
	m_assert_same_page colpf2

	.endl


	.endp				;End of screen

	.proc print

	print_sm = sm+screen_width*22;

	.macro m_print_text
	jmp skip
text	.byte ':1'
	.byte 0
skip
	lda print.print_text.text_adr	;Push for recursive call, low byte
	pha
	lda #<text
	ldy #>text
	jsr print.print_text
	pla
	sta print.print_text.text_adr	;Pull for recurive call, low byte
	.endm

	.macro m_print_space
	jsr print.print_space
	.endm

	.macro m_print_byte
	lda :1
	jsr print.print_hex
	.endm

	.macro m_print_word
	lda :1+1
	jsr print.print_hex
	lda :1
	jsr print.print_hex
	.endm

	.macro m_print_long
	lda :1+3
	jsr print.print_hex
	lda :1+2
	jsr print.print_hex
	lda :1+1
	jsr print.print_hex
	lda :1
	jsr print.print_hex
	.endm

	.proc print_text		;Low byte of text_adr must have been pushed before
	sta text_adr
	lda text_adr+1			;Push for recursive call, high byte
	pha
	sty text_adr+1
	ldy #0
text_adr = *+1
loop	lda $ffff,y
	beq return
	sta print.print_sm,x
	inx
	cpx #81
	scc
	jam

	iny
	bne loop	
return	pla				;Pull for recursive call, high byte
	sta text_adr+1
	rts
	.endp

	.proc print_space
	lda #' '
	sta print.print_sm,x
	inx
	rts
	.endp

	.proc print_hex
	
	sty save_y+1
	pha
	lsr
	lsr
	lsr
	lsr
	tay
	lda hex_digits,y
	sta print_sm,x
	inx
	pla
	and #15
	tay
	lda hex_digits,y
	sta print_sm,x
	inx
save_y	ldy #0
	rts
	
hex_digits	.byte '0123456789ABCDEF'
	.endp
	
	.endp				;End of print

;===============================================================

	.proc menu

	.var help_mode .byte
	.var details_mode .byte

	displayed_entries.color1 = screen.colors.colpf1+result_dli
	displayed_entries.color2 = screen.colors.colpf2+result_dli

;===============================================================

	.proc init
	lda #0
	sta help_mode
	sta details_mode
	rts
	.endp

;===============================================================

	.proc print_menu_title
	ldy #screen_width-1
loop	lda menu_mcb.menu_title,y
	sta sm,y
	dey
	bpl loop
	rts
	.endp

;===============================================================

	.proc toggle_help_mode
	.var old_cursor_line .byte

	lda input.input_help_mode
	bne help_active
	mwa #screen.dl sdlstl		;Setup normal display
	mwa #screen.colors.colpf1 screen.nmi.dli.colpf1_adr
	mwa #screen.colors.colpf2 screen.nmi.dli.colpf2_adr
	mva old_cursor_line screen.nmi.dli.cursor_line 
	rts

help_active
	mwa #screen.help_dl sdlstl	;Setup help display
	mwa #screen.help_colors.colpf1 screen.nmi.dli.colpf1_adr
	mwa #screen.help_colors.colpf2 screen.nmi.dli.colpf2_adr
	mva screen.nmi.dli.cursor_line old_cursor_line
	mva #$ff screen.nmi.dli.cursor_line
	rts
	.endp

;===============================================================

	.proc print_welcome
	rts
	.endp

	.proc clear_welcome
	mwa #(sm+screen_width*23) screen.dl.welcome_lms
	rts
	.endp

	.proc print_menu_entries_count
	ldx #40
	print.m_print_text "Found:None/"
	print.m_print_word menu_mcb.menu_entries_count
	rts
	.endp

	.proc set_entry_colors		;IN: <X>=line
	lda #14	
	sta displayed_entries.color1,x

	lda result.displayed_entries.lo,x
	asl
	asl
	asl
	asl
	ora #$04
	sta displayed_entries.color2,x
	rts
	.endp

	.proc clear_entry_colors	;IN: <X>=line
;	lda #0
;	sta displayed_entries.color1,x
;	sta displayed_entries.color2,x
	lda result.displayed_entries.lo,x
	asl
	asl
	asl
	asl
	ora #$04
	sta displayed_entries.color1,x
	sta displayed_entries.color2,x
	rts
	.endp

;===============================================================

	.proc set_cursor_line		;IN: <A>=cursor line, $ff is not visible
	cmp #$ff
	beq not_visible 
	clc
	adc #result_dli
not_visible
	sta screen.nmi.dli.cursor_line
	rts
	.endp

;===============================================================

	.proc toggle_details_mode
	lda details_mode
	clc
	adc #1
	cmp #3
	sne
	lda #0
	sta details_mode
	jsr print_selected_entry
	rts
	.endp

;===============================================================

	.proc print_selected_genre	;Print select genre as inverse tab title and the rest before/after the selected tab

	.var genre_number .byte
	.var text_start_adr .word
	.var text_end_adr .word
	.var text_end_offset .word
	.var inverse .byte

	lda #0
	jsr genres.compute_genre_text_start_adr
	mwa p1 text_start_adr

	lda cursor.selected_genre.number
	jsr genres.compute_genre_text_end_adr
	mwa p1 text_end_adr

	sbw text_end_adr text_start_adr text_end_offset

	lda text_end_offset+1
	bne print_right_to_left
	lda text_end_offset
	cmp #screen_width
	bcs print_right_to_left

	.proc print_left_to_right
	mva #0 genre_number		;Start gerne on the left
	ldx #0				;Current print x offset

genre_loop
	lda genre_number
	jsr genres.compute_genre_text_start_adr

	jsr compute_inverse
 
	ldy #0
text_loop
	lda (p1),y
	beq end_of_text
	ora inverse
	sta genre_sm,x
	inw p1
	inx
	cpx #screen_width
	bcs all_done
	jmp text_loop

end_of_text
	lda #' '
	sta genre_sm,x
	inx
	cpx #screen_width
	bcs all_done

	inc genre_number
	lda genre_number
	cmp menu_mcb.menu_genres_count
	bne genre_loop

	lda #' '
clear_rest_of_line
	cpx #screen_width
	bcs all_done
	sta genre_sm,x
	inx
	bne clear_rest_of_line

all_done
	rts
	.endp				;end of print_left_to_right

	.proc print_right_to_left
	mva cursor.selected_genre.number genre_number		;Start gerne on the right
	ldx #39				;Current print x offset

genre_loop
	lda genre_number
	jsr genres.compute_genre_text_end_adr

	jsr compute_inverse
 
	ldy #0
text_loop
	lda (p1),y
	beq end_of_text
	ora inverse
	sta genre_sm,x
	dew p1
	dex
	cpx #$ff
	beq all_done
	jmp text_loop

end_of_text
	lda #' '
	sta genre_sm,x
	dex
	cpx #$ff
	beq all_done

	dec genre_number
	lda genre_number
	cmp #$ff
	bne genre_loop

all_done
	rts
	.endp

	.proc compute_inverse		;Selected gerne shall be inverse
	ldy #$00
	lda genre_number
 	cmp cursor.selected_genre.number
 	sne
 	ldy #$80
 	sty inverse
 	rts
 	.endp
 
	.endp

;===============================================================

	.proc print_selected_entry
	ldx #screen_width		;Clear line
	lda #' '
loop	sta print.print_sm-1,x
	dex
	bne loop

	print.m_print_text "Entry:"
	print.m_print_word cursor.selected_entry.number
	inx
	
	lda details_mode
	bne technical_details

;===============================================================

	.proc readable_details		;IN: <X>=print cursor X position
	print.m_print_text "Genre:"

	.proc copy_genre
	lda cursor.selected_entry.genre_number
	jsr genres.compute_genre_text_start_adr
	ldy #0
loop	lda (p1),y
	beq exit
	sta print.print_sm,x
	iny
	inx
	cpx #screen_width
	bcc loop
exit
	.endp 

	rts
	.endp

;===============================================================

	.proc technical_details		;IN: <A>=1/2
	cmp #1
	jne part2

	.proc part1
	print.m_print_text "Mode:"
	print.m_print_byte cursor.selected_entry.the_cart_mode
	print.m_print_space
	print.m_print_text "Bank:"
	print.m_print_word cursor.selected_entry.start_bank_number
	print.m_print_space
	mva #'(' print.print_sm,x+
	print.m_print_byte cursor.selected_entry.initial_bank_number
	mva #')' print.print_sm,x+
	print.print_space

	lda cursor.selected_entry.source_type
	bne menu_entry_item
	lda #' '
	sta print.print_sm,x+
	sta print.print_sm,x+
	sta print.print_sm,x+
	sta print.print_sm,x+
	jmp no_menu_entry_item
menu_entry_item
	mva #'(' print.print_sm,x+
	print.m_print_byte cursor.selected_entry.item_number
	mva #')' print.print_sm,x+
no_menu_entry_item	
	rts
	.endp			;End of part1

	.proc part2
	print.m_print_text "Size:"
	print.m_print_long cursor.selected_entry.content_size
	rts
	.endp			;End of part2

	.endp			;End of technical_details

	.endp			;End of print_selected_entry

	.endp			;End of menu

;===============================================================

	.proc result_status

text_position = 56

	.proc print_searching
 	ldx #text_position
	print.m_print_text "Searching ....          "
	rts
	.endp

	.proc print_search_finished_complete
	ldx #text_position
	print.m_print_text "Search completed in "
	print.m_print_word search.search_frame_counter
	rts
	.endp

	.proc print_search_finished_incomplete
	ldx #text_position
	print.m_print_text "Too many results in "
	print.m_print_word search.search_frame_counter
	rts
	.endp

	.proc print_search_finished_user_break
	ldx #text_position
	print.m_print_text "User break.             "
	rts
	.endp

;===============================================================

	.proc print_found_entry_index	;Called during VBI
	ldx #46			
	lda result.found_entry_index
	ora result.found_entry_index+1
	bne print_found
	print.m_print_text "None"
	jmp print_done
print_found
	print.m_print_word result.found_entry_index
print_done

	ldy result.result_status
	cpy #result.status.searching
	bne not_searching
	lda cnt
	lsr
	lsr
	and #3
	tay
	lda animation,y
	sta print.print_sm,x
	ldx #66
	print.m_print_word search.search_entry_index
	rts

not_searching
	lda #'/'
	sta print.print_sm,x
	rts

	.local animation
	.byte 6,18,7,124
	.endl

	.endp			;End of print_found_entry_index

;===============================================================

	.endp			;print_result_status

;===============================================================

	.endp			;End of visualization
