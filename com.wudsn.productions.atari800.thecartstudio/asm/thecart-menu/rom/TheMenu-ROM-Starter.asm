;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=../CartMenu-Extended.asm

	.proc rom_starter

	.proc start_rom_entry		;Start selected entry, The!Cart is ON
	jsr check_lock
	lda cursor.selected_entry.the_cart_mode
	ldx cursor.selected_entry.start_bank_number
	ldy cursor.selected_entry.start_bank_number+1
	jmp cartmenu.entry_runcart	;Start cartridge
	.endp

;===============================================================

	.proc start_rom_entry_item	;Start item of selected entry, The!Cart is ON

starter_ram = $100

	jsr copy_starter

	mwa #starter_ram cartmenu.runadr ;Set callback run address

	jsr check_lock
	lda cursor.selected_entry.the_cart_mode
	ldx cursor.selected_entry.start_bank_number
	ldy cursor.selected_entry.start_bank_number+1
	jmp cartmenu.entry_runcart_adr	;Start cartridge via run address

;===============================================================

	.proc copy_starter		;Copy entry start routine to low RAM
	ldy #0
loop	lda starter_template,y
	sta starter_ram,y
	iny
	cpy #.len starter
	bne loop
	mva cursor.selected_entry.item_menu_version starter.item_menu_version
	mva cursor.selected_entry.item_number starter.item_number
	rts
	.endp

;===============================================================

starter_template			;Start address of template
	.proc starter,starter_ram
	item_menu_version = $00
	item_number = $01

	lda #>($0400)
	m_clear_main_ram_and_zp

	jsr open_editor

	lda $14
wait_vbi
	cmp $14
	beq wait_vbi

	lda item_menu_version
	cmp #1
	beq atarimax.old
	cmp #2
	beq atarimax.new
	cmp #3
	beq megacart
	jam

;===============================================================

	.proc atarimax
	
	.proc old
	ldx #$20
	ldy #0
loop
source	lda $a000,y
target	sta $2000,y
	iny
	bne loop
	inc source+2
	inc target+2
	dex
	bne loop
	stx $34bd			;Black screen
	mwa #callback $3127		;Turn "JSR get_stick_l2f7b" into "JSR callback"
	jmp $3115

callback				;Simulate sequence of cursor down
	lda item_number
	beq start
	jsr $3563			;Cursor down
	dec item_number
	jmp callback
start	sec				;C=1 indicates start
	rts
	.endp

;===============================================================

	.proc new
the_cart_start_address =$a445		;IN: <A>=entry number (1..127), this is the same entry point that is used by the autostart feature of the AtariMax itself.

	mva #0 $d500			;Activate bank 0 of the AtariMax cart manually because we do not go through the boot vector
	lda item_number
	clc
	adc #1				;Convert base 0 to base 1
	jmp the_cart_start_address
	.endp

;===============================================================

	.endp				;End of Atarimax

;===============================================================

	.proc megacart
	lda #0				;Activate bank 0 of the MegaCart 512k, 2MB cart manually because we do not go through the boot vector.
	sta $d500			;MegaCart 4MB would have start bank 254, but that's not supported anyway.
	lda item_number
	pha
	lsr
	lsr
	lsr
	lsr
	sta $7c21			;Item number DIV 16
	pla
	and #15
	sta $7c20			;Item number MOD 16
	mwa vdslst $7c25		;Preset "old" DLI vector
	jmp $b7f3
	.endp

	.proc open_editor
	mva #$40 nmien
	cli
	lda $e401			;Open E:
	pha
	lda $e400
	pha
	rts
	.endp

	.endp				;End of starter
	
	.endp				;End of start_rom_entry_item

	.proc check_lock		;Check of The!Cart register should be locked, OUT: <C>=1 to lock, <C>=0 to keep unlocked
	sec				;Default, lock The!Cart registers
	lda skstat			;If SHIFT is pressed, don't lock the The!Cart configuration
	and #8				;Bit 3 = 0 means SHIFT is pressed
	bne not_pressed			;Skip next if not 0
	clc				;Don't lock The!Cart registers
not_pressed
	rts
	.endp

	.endp				;End of rom_starter