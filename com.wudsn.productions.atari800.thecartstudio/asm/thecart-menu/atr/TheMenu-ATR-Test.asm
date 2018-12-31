;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;

	icl "CartMenu-Kernel-Equates.asm"

	org $2000

	.proc test

;bootini    = $04	;Word, same as in OS boot process
;dosini	   = $0c	;Word, same as in OS boot process
buffer_ptr = $15	;Word, BUFADR, normally used by DSKINV
cart_ptr   = $32	;Word, BUFRLO/HI, normally used by SIOV
cart_bank  = $34	;Byte, BFENLO, normally used by SIOV
;buffer_len = $35	;Byte, BFENHI, normally used by SIOV

sector_number = buffer_ptr

daux_tab	= $4000
sector_number_tab = $4100
cart_ptr_tab	= $4200
cart_bank_tab	= $4300


	mwa #$100 daux1
	mwa #$100 dbytlo
loop
	jsr compute_cart_adr
	jsr compute_start_bank_number
	lda daux1
	asl
	tax
	
	mwa daux1 daux_tab,x
	mwa sector_number sector_number_tab,x

	mwa cart_ptr cart_ptr_tab,x
	mwa cart_bank cart_bank_tab,x

	inc daux1
	bpl loop
	
wait	lda $d40b
	sta $d01a
	jmp wait

	
	.proc compute_cart_adr		;Compute cart_adr and sector_number (as 128 byte sector with linear layout)
	lda daux1
	ldy daux2
	sta sector_number
	sty sector_number+1

	ldx dbythi			;Double density?
	beq no_dd			;No, high byte of number of bytes to transfer is 0
	cpy #0				;Sectors 256, ... have 256 bytes in DD
	bne is_dd
	cmp #4				;Sectors 1,2,3 have 128 bytes even in DD
	bcc no_dd
is_dd	sbc #4				;<C>==1, sector_number=sector_number-4
	scs
	dec sector_number+1
	asl				;sector_number=sector_number*2
	rol sector_number+1
	adc #4				;<C>==0, sector_number=sector_number+4
	scc
	inc sector_number+1
	sta sector_number
no_dd
	lda sector_number+1		;Set cart_ptr = sector_number*$100/2 = (daux1,2)*$80
	lsr
	lda sector_number
	ror
	and #$1f			;Mask high byte to to $a000-$bfff
	ora #$a0
	sta cart_ptr+1			;Store address, hi

	lda #0
	ror
	sta cart_ptr			;Store address, lo
	rts
	.endp
	
 	.proc compute_start_bank_number
	lda sector_number+1		;Set cart_bank = sector_number/(8192/128) = sector_number/64
	sta cart_bank+1
	lda sector_number
	ldy #6
shift_loop
	lsr cart_bank+1
	ror
	dey
	bne shift_loop
	sta cart_bank
	rts
	.endp
	
	.endp