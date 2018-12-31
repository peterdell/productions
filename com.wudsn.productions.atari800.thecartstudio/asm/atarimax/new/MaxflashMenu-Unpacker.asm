;	@com.wudsn.ide.asm.mainsourcefile=MaxflashMenu.asm
	.proc unpacker, $100
; 
; PAGE 0
; 
zp_00		= $00
ngflag		= $01
src_ptr		= $02
dest_ptr	= $04
bank_number	= $06
cmcmd		= $07
byte_counter_hi	= $08
byte_counter_lo	= $09
dosini		= $0c

	.proc load_segment
	dec cmcmd
	jsr get_next_byte
	sta dest_ptr
	jsr get_next_byte
	sta dest_ptr+1
	jsr get_next_byte
	sta byte_counter_lo
	jsr get_next_byte
	sta byte_counter_hi
	inc cmcmd

	lda #$00
	ora dest_ptr
	ora dest_ptr+1
	ora byte_counter_lo
	ora byte_counter_hi
	beq segment_complete

	lda #$00
	sta initad
	sta initad+1
loop	jsr copy_next_page
	lda byte_counter_lo
	ora byte_counter_hi
	bne loop

	lda initad
	ora initad+1
	beq no_initadr
	jsr jump_initadr
no_initadr
	bne load_segment
	beq load_segment
	.endp

	.proc segment_complete
	bit zp_00			;Use warmstart or runad?
	bvc jump_runadr
	lda runad
	sta.w dosini
	lda runad+1
	sta.w dosini+1
	lda #$01
	sta.w byte_counter_lo
	jmp warmsv
	.endp

	.proc jump_runadr
	jmp (runad)
	.endp

	.proc jump_initadr
	jmp (initad)
	.endp


	.proc copy_next_page
	lda dest_ptr+1
	cmp #>$9fff
	bcs copy_next_byte

	lda src_ptr+1
	cmp #>$bfff
	bcs copy_next_byte

	lda byte_counter_hi
	beq copy_next_byte

	jsr activate_bank
	ldy #$00
loop	lda (src_ptr),y
	sta (dest_ptr),y
	iny
	bne loop
	inc src_ptr+1
	inc dest_ptr+1
	dec byte_counter_hi
	jsr deactivate_bank
	rts
	.endp

	.proc copy_next_byte
	jsr get_next_byte
	ldy #$00
	sta (dest_ptr),y
	clc
	lda dest_ptr
	adc #$01
	sta dest_ptr
	lda dest_ptr+1
	adc #$00
	sta dest_ptr+1
	rts
	.endp

	.proc get_next_byte
	jsr activate_bank
	ldy #$00
	lda (src_ptr),y
	pha

	clc
	lda src_ptr
	adc #$01
	sta src_ptr
	lda src_ptr+1
	adc #$00
	sta src_ptr+1

	cmp #>lc000
	bcc not_end_of_bank
	inc bank_number
	lda #>la000
	sta src_ptr+1
not_end_of_bank
	bit cmcmd
	bmi skip
	sec
	lda byte_counter_lo
	sbc #$01
	sta byte_counter_lo
	lda byte_counter_hi
	sbc #$00
	sta byte_counter_hi
skip	jsr deactivate_bank

	pla
	bit zp_00
	bpl return
	sta colbk
return	rts
	.endp
	
	.proc activate_bank	;IN: <bank_number>=bank number
	ldx bank_number
	inc critic
	sta ld500,x
	rts
	.endp

	.proc deactivate_bank
	ldx ngflag
	sta ld500,x
	lda trig3
	sta gintlk
	dec critic
	rts
	.endp

	.endp		;End of unpacker