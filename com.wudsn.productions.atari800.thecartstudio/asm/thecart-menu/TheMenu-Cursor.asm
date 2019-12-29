;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

;===============================================================

	.proc cursor

cursor_line 	 .word		;Public
last_cursor_line .word		;Private

	.local selected_genre
number			.byte	;Public
	.endl

	.local selected_entry	;Public
number			.word	;Public
the_cart_mode		.byte	;Public
content_size		.dword	;Public
start_bank_number	.word	;Public
initial_bank_number	.word	;Public
loader_base_address	.word	;Public
source_type		.byte	;Public
item_menu_version	.byte	;Public
item_number		.byte	;Public
genre_number		.byte	;Public
	.endl

	.if * >= $1000
	.error "Selected entry is in the memory area the is use by the simple menu start routines ", *
	.endif

;===============================================================

	.proc init
	jsr clear_cursor
	lda #0
	sta selected_genre.number
	rts	
	.endp
	
;===============================================================

	.proc clear_cursor
	lda #0
	sta cursor_line
	sta cursor_line+1
	lda #$ff
	sta last_cursor_line
	sta last_cursor_line+1
	rts
	.endp

;===============================================================

	.proc move_cursor

	.var stick 		 .byte	;Working copy for shifting
	.var stick_step	 	 .word	;Make byte a word
	.var maximum_cursor_line .word

	lda input.input_help_mode
	bne help_mode_active

	jsr controls.read
	mva controls.stick stick		;Create working copy for shifting
	mva controls.stick_step+1 stick_step	;Create word based on hight byte

	jsr handle_up_down
	jsr handle_left_right
help_mode_active
	rts

;===============================================================

	.proc handle_up_down		 ;IN: <stick>, <stick_step>
	sbw result.found_entry_index #1 maximum_cursor_line

	lsr stick
	bcs not_up
	sbw cursor_line stick_step
	bcs up_ok
	lda #0				;Underflow, set to zero
	sta cursor_line
	sta cursor_line+1
up_ok
not_up	lsr stick
	bcs not_down
	adw cursor_line stick_step
	cpw cursor_line maximum_cursor_line
	bcc down_ok			;Overflow, set to last line
	mwa maximum_cursor_line cursor_line
down_ok
not_down
	cpw cursor_line last_cursor_line
	bne different_line
	rts

different_line
	mwa cursor_line last_cursor_line
	jsr cursor.get_selected_entry
	rts
	.endp				;End of handle_up_down

;===============================================================

	.proc handle_left_right		 ;IN: <stick>, <stick_step>
	ldx selected_genre.number

	lsr stick
	bcs not_left
	cpx #0
	beq not_left_limit
	dex
not_left_limit
not_left
	lsr stick
	bcs not_right
	inx
	cpx menu_mcb.menu_genres_count
	bcs not_right_limit
	inx
not_right_limit
	dex
not_right
	cpx selected_genre.number
	stx selected_genre.number
	bne different_genre
	rts

different_genre
	jsr visualization.menu.print_selected_genre
	jsr result.start_search_for_input
	rts
	.endp				;End of handle_up_down

;===============================================================

	.endp				;End of move_cursor

;===============================================================

	.proc get_selected_entry
	
	selected_entry_pointer	= result.top_entry_pointer	;Reuse result VBI variables
	menu_entry_number	= result.menu_entry_number
	bank_number		= result.bank_number
	bank_address		= result.bank_address

	.macro m_get_byte
 	ldy #menu_entry.:1
	mva (bank_address),y selected_entry.:1
	.endm

	.macro m_get_word
 	ldy #menu_entry.:1
	mva (bank_address),y selected_entry.:1
	iny
	mva (bank_address),y selected_entry.:1+1
	.endm

	.macro m_get_long
 	ldy #menu_entry.:1
	mva (bank_address),y selected_entry.:1
	iny
	mva (bank_address),y selected_entry.:1+1
	iny
	mva (bank_address),y selected_entry.:1+2
	iny
	mva (bank_address),y selected_entry.:1+3
	.endm

	lda result.found_entry_index
	ora result.found_entry_index+1
	bne some_entries_found

	sta selected_entry_pointer	;Nothing found, so nothing can be selected.
	sta selected_entry_pointer+1
	sta menu_entry_number
	sta menu_entry_number+1
	sta bank_number
	sta bank_number+1
	sta bank_address
	sta bank_address+1
	jmp print_selected_entry

some_entries_found
	m_convert_found_entry_index cursor.cursor_line selected_entry_pointer

	ldy #0
	lda (selected_entry_pointer),y
	sta menu_entry_number
	iny
	lda (selected_entry_pointer),y
	sta menu_entry_number+1

	m_convert_menu_entry_number 0

	ldx bank_number	
	ldy bank_number+1
	jsr menu_core.set_bank

	m_get_word number
	m_get_byte the_cart_mode
 	m_get_long content_size
 	m_get_word start_bank_number
 	m_get_word initial_bank_number
 	m_get_word loader_base_address
 	m_get_byte source_type
 	m_get_byte item_menu_version
 	m_get_byte item_number
 	m_get_byte genre_number

print_selected_entry
	jsr visualization.menu.print_selected_entry
	rts

	.endp				;End of get_selected_entry

	.endp				;End of cursor

