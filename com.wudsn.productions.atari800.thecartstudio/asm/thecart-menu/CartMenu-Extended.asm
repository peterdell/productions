
;	>>> The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.outputfile=cartmenu-extended.rom
;

	.def alignment_mode	;Enforce alignment

p1		= $80		;General purpose ZP pointer (not in interrupts)
p2		= $82		;General purpose ZP pointer (not in interrupts)
search_pointer	= $84		;Special purpose ZP pointer (see TheMenu.asm)
x1		= $86		;General purpose ZP variable (not in interrupts)
x2		= $87		;General purpose ZP variable (not in interrupts)

menu_mcb_zp	= $a0		;The menu control block in zero page ($a0-$df)
carmenu_zp	= $e0		;Cartmenu zero page ($e0-$e1)
vbi_zp		= $e2		;Extended menu zero page ($e2-$ff)

data_start	= $2400		;Code must end before this address
chr		= data_start	;Charset in RAM below module (1K)
pm		= chr+$400 	;Player Missle graphics in RAM below module (2K)
sm		= pm+$800	;Screen in RAM below module (1K)
menu_data_start = $3400		;The menu uses this are as buffer
menu_data_end	= $9fff		;until the end of the RAM

module_a000	= $a000		;Standard ROM base address
bank_size	= $2000		;In native mode, all banks are always 8k


menu_start_ram	= $0700		;Start of the menu ROM copy and start routine in RAM 
menu_main_ram	= $0800		;Start of the menu main souring in RAM 
menu_atr_rom 	= $b800		;Start of the menu SIO ROM routine used by the ATR starter
menu_start_rom  = $be00		;Start of the menu ROM copy and start routine in ROM 
menu_mcb_rom	= $bf00		;The menu control block in ROM 
menu_mcb_size	= $0c+$28	;Size of the menu control block, $0c bytes header plus $28 bytes title
menu_signature	= $bfe0		;16 bytes like "The!Cart99991231"

menu_mcb.cartridge_type			= menu_mcb_zp + $00; ZP address of the cartridge type (byte)
menu_mcb.bank_count			= menu_mcb_zp + $02; ZP address of the bank count (word)
menu_mcb.menu_genres_count		= menu_mcb_zp + $04; ZP address of the genres count (word)
menu_mcb.menu_genres_start_bank_number	= menu_mcb_zp + $06; ZP address of the start bank (word)
menu_mcb.menu_entries_count		= menu_mcb_zp + $08; ZP address of the entries count (word)
menu_mcb.menu_entries_start_bank_number	= menu_mcb_zp + $0a; ZP address of the start bank (word)
menu_mcb.menu_title			= menu_mcb_zp + $0c; ZP address of the title ($28 bytes)

menu_start_bank	= 8
menu_entry_size	= 64			;Must be a divider of $2000!

;===============================================================

	.enum menu_genre
	flags			= 0	;Byte
	text_length		= 1	;Byte
	text_offset		= 2	;Word
	.ende

	.enum menu_entry
	number			= 0	; Word
	the_cart_mode 		= 2	; Byte
	content_size 		= 4	; 4 Bytes
	start_bank_number	= 8	; Word
	initial_bank_number	= 10	; Word
	loader_base_address	= 12	; Word
	source_type		= 14	; Byte
	item_menu_version	= 15	; Byte
	item_number		= 16	; Byte
	title_length		= 17	; Byte
	title			= 18	; 40 Bytes
	genre_number		= 58	; Byte, 0 means no genre assigned
	favorite_indicator	= 59	; Byte
	.ende
	
	.enum menu_entry_source_type
	menu_entry 		= 0	; Constant
	menu_entry_item		= 1	; Constant
	.ende

;===============================================================

	.enum cartridge_type
	CARTRIDGE_ATMAX_1024	= 42	;Type 42: Atarimax 1 MB Flash cartridge  
	CARTRIDGE_THECART_32M	= 65	;Type 65: The!Cart 32 MB Flash cartridge
	CARTRIDGE_THECART_64M	= 66	;Type 64: The!Cart 64 MB Flash cartridge
	CARTRIDGE_THECART_128M	= 62	;Type 61: The!Cart 128 MB Flash cartridge
	.ende

	icl "CartMenu-Kernel-Equates.asm"
	icl "CartMenu-Definitions.asm"

;===============================================================

	opt h-f+

	org module_a000			;Cartmenu part 1, simple menu
	.local cartmenu_rom_part1 	;Always use latest "cartmenu.rom" which is also used in the studio
	ins "..\..\src\data\cartmenu.rom",+0,$1800
	.endl

;===============================================================

	org menu_atr_rom		;Resident callbacks for the ATR simulation

	icl "atr/TheMenu-ATR-Starter-ROM.asm"

;===============================================================

	org menu_start_rom		;First entry vector on power up, called from simple menu startup sequence

	.proc menu_start

	jsr check_reset_button

;	Init normal screen etc. while bank 0 is active
	cld
	jsr cartmenu.entry_initos

	lda #$ff			;Disable BASIC ROM in MMU on XL machines
	sta portb			;On an Atari 800 this does not work, but also does not harm as it is a write to a read registere
	sta coldst			;RESET triggers coldstart by default
	mva #$01 basicf			;Ensure BASIC is not re-enabled during warmstart
	lda #$c0			;Use RAM in cartridge area in XL machines
	sta ramtop			;Change the values used by the graphics screen
	sta ramsiz			;The memtop for the physical RAM must not be changed

;	Copy menu control block to zeropage RAM
;	Copy menu loader to RAM
	ldx #0
copy_menu_mcb
	cpx #menu_mcb_size
	bcs copy_menu
	lda menu_mcb_rom,x
	sta menu_mcb_zp,x
copy_menu
	lda menu_start_rom,x
	sta menu_start_ram,x
	inx
	bne copy_menu_mcb

;	Setup bank switching routine based on flash target class by setting the low byte of the JMP vector
	lda menu_mcb.cartridge_type
	ldx #<menu_core.set_bank.set_bank_atmax_1024
	cmp #cartridge_type.CARTRIDGE_ATMAX_1024
	beq setup_cartridge_type

;	All The!Cart versions are mapped to the same routine which checks for the maximum high bank 64
;	The check could be more precise, but there's no enough space in this first page for it
	ldx #<menu_core.set_bank.set_bank_thecart_128m
	cmp #cartridge_type.CARTRIDGE_THECART_32M
	beq setup_cartridge_type
	cmp #cartridge_type.CARTRIDGE_THECART_64M
	beq setup_cartridge_type
	cmp #cartridge_type.CARTRIDGE_THECART_128M
	beq setup_cartridge_type

	lda #$44
	jam

setup_cartridge_type			;IN: <X>=low byte of set_bank_routine, <Y>=(maximum high byte of bank number)+1
	stx menu_core.set_bank.jump_adr

	jmp menu_core.copy_menu_init

;===============================================================

	.proc check_reset_button	;Cartridge RESET button still pressed?
	sei				;Disable VBI stage 2
	lda #1
	sta the_cart.secondary_bank_enable ;Try to enable secondary bank window
	lda the_cart.secondary_bank_enable ;Forced to zero by RESET button?
	bne can_start
cannot_restart
	mva vcount colbk
	lda #1				;Cartridge RESET button released?
	sta the_cart.secondary_bank_enable
	lda the_cart.secondary_bank_enable
	beq cannot_restart
	lda #0
	sta the_cart.secondary_bank_enable	
can_start
	lda #0
	sta the_cart.secondary_bank_enable
	cli				;Enable VBI stage 2
	rts
	.endp				;End of check_reset_button

	.endp				;menu_start

;===============================================================

;	Relocate copy routine to RAM area
	opt f-
	org *-menu_start_rom+menu_start_ram
	opt f+

	.proc menu_core

;===============================================================

	.proc copy_menu_init	;Copy complete menu from cartridge to RAM

	sei			;Disable all interrupts
	lda #0
	sta irqen
	sta nmien

	mwa #menu_start_bank copy_banks.bank_number
	mva #[.len extended_menu+bank_size-1]/bank_size copy_banks.bank_count
	mva #>menu_main_ram p2+1
	jsr copy_banks

	lda #$40		;Enable all interrupts again
	sta nmien
	lda #$c0
	sta irqen
	cli
	jmp menu_main_ram


;===============================================================

	.proc copy_banks	;Copy full banks from $a000 to (p2)
	.var .word bank_number, .byte bank_count

copy_bank
	ldx bank_number
	ldy bank_number+1
	jsr set_bank

	ldx #>bank_size
	ldy #0
	sty p1
	sty p2
	mva #>module_a000 p1+1
copy_page
	lda (p1),y
	sta (p2),y
	iny
	bne copy_page
	inc p1+1
	inc p2+1
	dex
	bne copy_page
	inw bank_number
	dec bank_count
	bne copy_bank
	rts
	.endp				;End of copy_banks

	.endp				;End copy_menu_init

;===============================================================

	.proc set_bank			;IN: <X>=Bank lo, <Y>=Bank hi
jump_adr = *+1
	jmp set_bank			;Lo byte is adapted by initialization

	.proc set_bank_atmax_1024
	sta $d500,x
	cpy #1
	bcs bank_error
	rts
	.endp				;End of set_bank_maxflash

	.proc set_bank_thecart_128m
	stx the_cart.primary_bank_lo	;Set primary bank register low byte (0-255, default: 0)
	sty the_cart.primary_bank_hi	;Set primary bank register high byte (0-63, default: 0)
;	n/a the_cart.primary_bank_enable;Set primary bank enable (1=enable, 0=disable, default: 1)
	cpy #64				;Max bank hard coded for 128 MB
	bcs bank_error
	rts
	.endp				;End of set_bank_thecart

	.proc bank_error		;Invalid bank value used
	lda #$34
	sta color4
	sta colbk
	.byte 2
	.endp				;End of bank_error

	.endp				;End of set_bank


	.proc get_bank			;OUT: <X>=Bank lo, <Y>=Bank hi

 	.proc get_bank_thecart_128m
	ldx the_cart.primary_bank_lo
	ldy the_cart.primary_bank_hi
	rts
	.endp

	.endp

;===============================================================

	org menu_start_ram+$ff		;Align to full page
	.byte 0

	.endp				;End of menu_core
;===============================================================

	opt f-
	org menu_mcb_rom		;Filled by The!Cart Studio during export
	opt f+
	.word cartridge_type.CARTRIDGE_ATMAX_1024	;flash_target_class
	.word 1				;bank_count
	.word 1				;menu_entries_count
	.word 11			;menu_entries_start_bank_number
	.byte '0123456789012345678901234567890123456789'

;===============================================================

	org menu_signature		;16 reserved bytes

;===============================================================

	org $bff0			;Entered from other banks during startup

	.proc menu_foreign_init	
	lda #0
	sta $d500			;Activate bank 0 of AtariMax carts
	jmp ($bffe)
	nop
	nop
	.endp

	org $bffa			;Cartridge control block
	.byte a(menu_start_rom),$00,$04,a(menu_start_rom)

;===============================================================

	opt f-
	org module_a000			;Cartmenu part 2, Flasher and MyPicoDos
	.local cartmenu_rom_part2
	ins "..\..\src\data\cartmenu.rom",+$2000,$8000

;	24k of dummy data reserved for future use by HIAS
	opt f-
	org $0000
	opt f+
	org $5fff
	.byte $ff
	.endl

;===============================================================

	opt f-
	org menu_main_ram		;Extended Menu Code, 24k, $6000 bytes
	opt f+

	icl "TheMenu.asm"

	m_info extended_menu

	org menu_main_ram+$5fff
	.byte 0

;===============================================================

	opt f-
	org $0000			;Extended Menu Data, 40k, bank 11
	opt f+

	.align $9fff
	.byte 0

;===============================================================


