;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

	.enum joystick
down	= 1
up	= 2
left	= 4
right	= 8
fire	= 16
any	= 31
	.ende

	.proc controls		;OUT: <A>=joystick, bits with 0 are active

;===============================================================

stick		   .byte 0	;Public
stick_step	   .word 0	;Public, use high byte as step
stick_old	   .byte 0	;Private
start_delay_cnt	   .byte 0	;Private
repeat_delay_new   .byte 0	;Private
repeat_delay_cnt   .byte 0	;Private

	.local bits
left	.byte joystick.left
right	.byte joystick.right
fire	.byte joystick.fire
	.endl

;===============================================================

	.proc init
	lda #joystick.any
	sta stick
	sta stick_old
	rts
	.endp

;===============================================================

	.proc read
	start_delay = 10
	repeat_delay = 8

	lda stick0
	and #joystick.left|joystick.right|joystick.up|joystick.down
	ldx trig0
	seq
	ora #joystick.fire
	sta stick

	jsr map_keys

	lda stick
	cmp stick_old
	beq is_same
	mva #start_delay start_delay_cnt
	mva #repeat_delay repeat_delay_new
	mwa #$100 stick_step
	jmp return_with_key

is_same	
	cmp #joystick.any
	beq return_without_key
	lda start_delay_cnt
	beq no_more_start_delay
	dec start_delay_cnt
	jmp return_without_key

no_more_start_delay
	lda repeat_delay_cnt
	beq no_more_repeat_delay
	dec repeat_delay_cnt
	jmp return_without_key

no_more_repeat_delay
	lda repeat_delay_new
	lsr
	sta repeat_delay_cnt
	lda repeat_delay_new
	beq repeat_delay_new_zero
	dec repeat_delay_new
	jmp return_with_key

repeat_delay_new_zero
	lda stick_step+1
	cmp #$ff
	beq stick_step_is_max
	adw stick_step #5
stick_step_is_max

return_with_key
	mva #7 consol
	lda stick
	sta stick_old
	rts

return_without_key
	lda #joystick.any
	sta stick
	rts

;===============================================================

	.proc map_keys		;Map keyboard to joystick
	lda skstat
	and #4
	bne no_key

	lda kbcode
	and #63
	tax
	lda stick

	cpx #$0c		;Map "RETURN" to "FIRE" 
	bne no_return
	and #~joystick.fire
	jmp set_key
no_return
	cpx #$06		;Map "CURSOR LEFT" to "LEFT"
	bne no_left
	and #~joystick.left
	jmp set_key
no_left
	cpx #$07		;Map "CURSOR RIGHT" to "ROGHT"
	bne no_right
is_right
	and #~joystick.right
	jmp set_key
no_right
	cpx #$0f		;Map "CURSOR UP" to "UP"
	bne no_cursor_up
	and #~joystick.up
	jmp set_key
no_cursor_up
	cpx #$0e		;Map "CURSOR DOWN" to "DOWN"
	bne no_cursor_down
	and #~joystick.down
	jmp set_key
no_cursor_down

set_key	sta stick
no_key	rts
	.endp			;End of map_keys
	.endp			;End of read

	.endp			;End of controls
