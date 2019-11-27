;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

;	When this procedure is called, the following conditions hold true
;	<I>     = $00
;	portb 	= $ff
;	basicf	= $01
;	ramtop  = $c0
;	ramsiz  = $c0
;	coldst  = $ff


	.proc starter	

	icl "TheMenu-Starter-Globals.asm"

	lda search.search_status
	cmp #search.status.start_simple_menu
	beq start_simple_menu
	cmp #search.status.start_selected_entry
	beq start_selected_entry
	jmp *

;===============================================================

	.proc cold_start
	sei				;Prevent VBI stage 2
	lda #the_cart_mode.tc_mode_off	;Disable and lock the cartridge
	sta the_cart.mode
	sta the_cart.configuration_lock
	mva trig3 gintlk		;Make sure cartridge status is up to date
	cli
	jmp coldsv
	.endp

	.proc activate_simple_menu	;Activate "simple menu" bank
	sei				;Disable VBI stage 2
	ldx #<0
	ldy #>0
	jsr menu_core.set_bank
	mva trig3 gintlk
	cli				;Enabled VBI stage 2
	rts
	.endp

;===============================================================

	.proc start_simple_menu
	jsr activate_simple_menu
	jmp cartmenu.entry_cartmenu
	.endp

;===============================================================

	.proc start_selected_entry	;Start selected entry

	lda cursor.selected_entry.the_cart_mode
	cmp #the_cart_mode.tc_mode_atr_file
	jeq atr_starter.start_atr_entry ;ATR files get their own handling
	cmp #the_cart_mode.tc_mode_executable_file
	jeq xex_starter.start_xex_entry ;Executable files get their own handling
	cmp #the_cart_mode.tc_mode_sap_file
	jeq xex_starter.start_xex_entry ;SAP files get their own handling, they are basically executable file

	lda #0				;Initialize boot and RESET relevant state
	sta warmst
	sta coldst
	sta boot?

	jsr activate_simple_menu	;All following parts require the simple menu

;	Add initial bank offset
	adw cursor.selected_entry.start_bank_number cursor.selected_entry.initial_bank_number

	jsr check_start_entry_item
	jcc rom_starter.start_rom_entry
	jmp rom_starter.start_rom_entry_item

;===============================================================

	.proc check_start_entry_item	;Determine source and menu version of selected entry, OUT: <C>=0 to start the entry, <C>=1 to start the entry item

	lda cursor.selected_entry.source_type
	cmp #menu_entry_source_type.menu_entry_item
	bne entry			;Not an item that can be started but a regular entry

	lda cursor.selected_entry.item_menu_version
	beq entry			;Not a supported version for direct start.

	lda skctl			;If any key with CONTROL is pressed, start the menu, not the item
	and #4				;Bit 2 = 0 indicates last key is still pressed
	bne entry_item			;No key pressed
	lda kbcode			;Bit 7 = 1 set means CONTROL pressed
	bmi entry

entry_item
	sec
	rts

entry	clc
	rts
	.endp

;;===============================================================

	.macro m_clear_main_ram_and_zp	;Clear all the RAM starting at page <A>, assume screen DMA is off
	sta clear+2
	eor #$ff
	clc
	adc #>$a000
	tay
	lda #0
	tax
clear	sta $ff00,x
	inx
	bne clear
	inc clear+2
	dey
	bne clear
clear_zp				;Clear user ZP $80-$ff
	sta $80,x
	inx
	bpl clear_zp
	.endm

;===============================================================

	icl "rom/TheMenu-ROM-Starter.asm"

;===============================================================

	icl "atr/TheMenu-ATR-Starter.asm"

;===============================================================

	icl "xex/TheMenu-XEX-Starter.asm"

	.endp				;End of start_selected_entry

	.endp				;End of starter