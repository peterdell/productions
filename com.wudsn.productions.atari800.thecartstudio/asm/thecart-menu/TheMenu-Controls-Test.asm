
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;

	org $2000

cnt	= $14
x1	= $80

	.proc test
	jsr controls.init

loop	lda cnt
wait	cmp cnt
	beq wait

	jsr controls.read
	lda controls.stick
	sta x1
	ldy #0
:8	jsr print_bit

	ldy #40
	ldx #0
loop2	lda controls,x
	sta (88),y
	iny
	inx
	cpx #20
	bne loop2

	jmp loop

	.proc print_bit
	lda x1
	and #$80
	sta (88),y	
	asl x1
	iny
	rts
	.endp

	icl "CartMenu-Kernel-Equates.asm"
	icl "CartMenu-ExtendedMenu-Controls.asm"
	.endp
	
	
	run test