;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=../CartMenu-Extended.asm


	.proc xex_starter	;The!Cart is OFF

content_size_length = 4
content_size	= dskfms	;4 bytes
segment_start	= fmszpg	;Word
segment_end	= fmszpg+2	;Word

starter_marker	= $700	;"T" to differentiate $00,$03" for DOS 2x, "M" for MyDos, "S" for SpartaDOS
starter_version	= $701	;Version of the control block and entry structure
starter_entry	= $702	;Copy of the complete entry
starter_code	= starter_entry+.len cursor.selected_entry

start_xex_entry
	jsr copy_starter
	lda #>(starter_template+$ff)	;Clear everything starting at the relocatable parts
	m_clear_main_ram_and_zp
	mva #1 boot?			;Indicate that disk boot was successful
	mva #0 coldst			;Indicate that next RESET is a warmstart
	jmp starter_code		;Jump to starter code

;===============================================================

	.proc copy_starter		;Copy marker, selected entry and start routine to low RAM

	mva #'T' starter_marker
	mva #1 starter_version

	.proc copy_entry
	ldx #.len cursor.selected_entry-1
loop	lda cursor.selected_entry,x
	sta starter_entry,x
	dex
	bpl loop
	.endp

	.proc copy_content_size
	ldx #content_size_length-1
	ldy #cursor.selected_entry.content_size-cursor.selected_entry+content_size_length-1
loop	lda starter_entry,y
	sta content_size,x
	dey
	dex
	bpl loop
	.endp

	.if .len starter > $200
	.error "Starter too large. Adapt copy code."
	.endif

	ldy #0
loop	lda starter_template,y		
	sta starter_code,y
	lda starter_template+$100,y		
	sta starter_code+$100,y
	iny
	bne loop

	rts

	.endp				;End of copy_starter

;===============================================================

starter_template
	.proc starter, starter_code	;Starter RAM part, does not have to be relocatable
	
	.proc clear_ram
	lda #0				;Clear main memory from starter_end to start of DL page
	sta buffer_ptr
	ldy #<starter_end
	ldx #>starter_end
	stx buffer_ptr+1
loop	sta (buffer_ptr),y
	iny
	bne loop
	inc buffer_ptr+1
	ldx buffer_ptr+1		;Page of DL reached?
	cpx sdlsth
	bne loop
	.endp

	ldy #cursor.selected_entry.start_bank_number-cursor.selected_entry
	mwa starter_entry,y cart_bank	;Copy start bank to counter

;===============================================================

	sei
	lda cart_bank
	sta the_cart.primary_bank_lo	;Set primary bank register low byte (0-255, default: 0), also enables the cart (!)
	lda cart_bank+1
	sta the_cart.primary_bank_hi	;Set primary bank register high byte (0-63, default: 0), also enables the cart (!)

;===============================================================

	mwa #module_a000 cart_ptr	;Start a begin of first file bank

	ldy #cursor.selected_entry.the_cart_mode-cursor.selected_entry
	lda starter_entry,y
	cmp #the_cart_mode.tc_mode_sap_file
	beq start_sap

	.proc start_xex
	jsr load_xex
	jmp (runad)
	.endp

	.proc start_sap // TODO: SAP handling is untested
	mwa #0 buffer_len

loop	lda #1
	sta the_cart.primary_bank_enable;Enable The!Cart
	ldy #0
	lda (cart_ptr),y
	iny
	and (cart_ptr),y
	cmp #$ff
	beq end_sap

	ldy #0
	lda (cart_ptr),y
	cmp #$0a
	bne no_cr
	lda #$9b
	bne put_char
no_cr	cmp #$0d
	beq skip_char
put_char
	sty the_cart.primary_bank_enable;Disable The!Cart
	jsr putchr
skip_char
	inw cart_ptr
	inw buffer_len
	jmp loop
end_sap

	jsr adjust_content_size
	jsr load_xex
	.byte 2
	.endp

	.proc putchr
	tax
	lda $e407
	pha
	lda $e406
	pha
	txa
	rts
	.endp

;===============================================================

	.proc load_xex
	
	mwa #0 runad			;Mark RUNAD as not yet set

	.proc segment_loop
	mwa #return initad		;Set INITAD to RTS

	mwa #segment_start buffer_ptr	;Load 2 byte header
	mwa #2 buffer_len		;EOF?

	jsr simulate_bget
	beq eof_error

	lda runad			;Default RUNAD to first segment
	ora runad+1
	bne runad_set
	mwa segment_start runad
runad_set

	lda segment_start		;Skip header?
	and segment_start+1
	cmp #$ff
	beq segment_loop

	mwa #segment_end buffer_ptr	;Load end address
	mwa #2 buffer_len
	jsr simulate_bget
	beq eof_error			;EOF?

	mwa segment_start buffer_ptr	;Set buffer and compute length
	sbw segment_end buffer_ptr buffer_len
	inw buffer_len
	
	jsr simulate_bget		;Load segment
	pha
	jsr jmp_initad			;Handle INITAD first
	pla
	beq return			;EOF?
	jmp segment_loop

return	rts
	.endp				;end of segment_loop

jmp_initad
	jmp (initad)

eof_error				;structure error
	mva #$38 $d01a
	jmp eof_error
	.endp

;===============================================================

	.proc simulate_bget		;IN: .word buffer_ptr, .word buffer_len, OUT: <Z>=1 if EOF
	sei				;Disable TRIG3 check for setting The!Cart registers and shadow register copying

	jsr adjust_content_size		;content_size=content_size-buffer_len

	.proc disable_screen		;Create empty DL and set hardware pointers to it.
	lda sdmctl			;Screen is already off
	beq dl_not_in_module
	lda sdlsth
	cmp #>module_a000
	bcc dl_not_in_module		;Do not disable the screen, if the DL is not in the module area.

	lda #$41			;Create empty DL in zero page
	ldx #<empty_dl
	ldy #>empty_dl
	sta empty_dl
	stx empty_dl+1
	sty empty_dl+2

	sta wsync			;Change ANTIC pointer at the begin of the next scanline and before the module is enabled
	stx dlptr
	sty dlptr+1

dl_not_in_module
	.endp				;End of disable_screen


;===============================================================

	ldx #1
	ldy #0

loop	stx the_cart.primary_bank_enable;Enable The!Cart
	lda (cart_ptr),y
	sty the_cart.primary_bank_enable;Disable The!Cart
	sta (buffer_ptr),y

	inw cart_ptr
	lda cart_ptr+1
	cmp #>[module_a000+bank_size]
	bne skip
	mva #>module_a000 cart_ptr+1
	inw cart_bank
	lda cart_bank
	sta the_cart.primary_bank_lo	;Set primary bank register low byte (0-255, default: 0), also enables the cart (!)
	lda cart_bank+1
	sta the_cart.primary_bank_hi	;Set primary bank register high byte (0-63, default: 0), also enables the cart (!)
skip

	inw buffer_ptr

	dew buffer_len
	lda buffer_len
	ora buffer_len+1
	bne loop

	lda content_size
	ora content_size+1
	ora content_size+2
	ora content_size+3

	cli
	rts

	.endp				;End of simulate_bget

	.proc adjust_content_size	;content_size=content_size-buffer_len
	sec
	lda content_size
	sbc buffer_len
	sta content_size
	lda content_size+1
	sbc buffer_len+1
	sta content_size+1
	lda content_size+2
	sbc #0
	sta content_size+2
	lda content_size+3
	sbc #0
	sta content_size+3
	rts
	.endp

starter_end
	.endp				;End of starter

	m_info xex_starter.starter

	.endp				;End of xex_starter