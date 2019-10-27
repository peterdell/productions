;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=../CartMenu-Extended.asm
;
;	The definition in this file are used by both the RAM and the ROM part of the ATR started

	.proc start_atr_file_rom	;Starter ROM part, must be completely within the first bank

	icl "..\TheMenu-Starter-Globals.asm"

;===============================================================

	.proc special_sio		;IN: <A>=disk status (dvstat), <X>=start bank low, <Y>=start bank hi
					;OUT: cart_bank (word), cart_ptr(word), buffer_ptr (word), buffer_len (word), <C>=0 to perform normal sector loading, <C>=1 to return SIO status returned in in <Y>

	dvstat_temp   = buffer_len	;Byte, reuse buffer_len it will only be used later
	sector_number = buffer_ptr	;Word, reuse buffer_ptr because it will only be used later

	sta dvstat_temp			;Bit 7=1 means disk is ED, bit 5=1 means disk is DD
	stx cart_start_bank
	sty cart_start_bank+1
	
	lda dunit			;Is device number "D1:"?
	cmp #1
	beq device_d1
	ldy #130			;No, return error 130 - Nonexistent Device
	sec
	rts

device_d1
	lda dcomnd			;Is command "R"read sector?
	cmp #'R'
	jeq command_read		;Yes
	cmp #'S'			;Is command "S"tatus?
	jeq command_status
	ldy #132			;No, return error 132 - Unknown command
	sec
	rts

;===============================================================

	.proc command_read

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
is_dd	sbc #4				;<C>==1
	scs
	dec sector_number+1
	asl
	rol sector_number+1
	adc #4				;<C>==0
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
	.endp

	.proc compute_start_bank_number
	lda sector_number+1		;Set cart_bank = sector_number/(8192/128) =  sector_number/64
	sta cart_bank+1
	lda sector_number
	ldy #6
shift_loop
	lsr cart_bank+1
	ror
	dey
	bne shift_loop
	sta cart_bank

	adw cart_start_bank cart_bank cart_bank
	.endp				;End of compute_start_bank_number

	mwa dbuflo buffer_ptr		;Set buffer_ptr to target address
	mva dbytlo buffer_len		;Copy number of bytes to ZP
	clc				;OUT: cart_bank (word), cart_ptr (word), buffer_ptr (word), buffer_len (word), <C>=0 to perform normal sector loading
	rts
	
	.endp				;End of command_read

;===============================================================

	.proc command_status		;Process "S"tatus command
	mva dvstat_temp dvstat
	lda #0
	sta dvstat+1			;Clear remaining status bytes.
	sta dvstat+2
	sta dvstat+3
	ldy #1
	sec
	rts
	.endp				;End of command_status

;===============================================================

	.endp				;End of special_sio

	.endp				;End of start_atr_file_rom
	
	m_assert_align start_atr_file_rom bank_size
	