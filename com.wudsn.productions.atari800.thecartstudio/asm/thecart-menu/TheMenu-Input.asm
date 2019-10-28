
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

	.proc input			;Handle keyboard input

input_sm	= sm+screen_width
input_max_length = screen_width

input_help_mode	.byte 0
input_started	.byte 0
input_changed	.byte 0
input_length	.byte 0
input_blink	.byte 0
input_buffer	.ds screen_width

;===============================================================

	.proc init

	ldy #0
	sty input_help_mode
	sty input_started
	sty input_changed
	sty input_length
	sty input_blink

;	Set keyboard delay and repeat delay based on the original values
	lda pal
	and #$0e
	bne is_ntsc
	ldx #$05/2
	ldy #$28/2
	bne store
is_ntsc	ldx #$06/2
	ldy #$30/2
store	stx keyrep			;Twice the speed to repeat
	sty krpdel			;Half the time to wait for repeat
	rts
	.endp

;===============================================================


	.proc handle_keys		;Called in VBI

	.proc blink_cursor
	inc input_blink			;Increment blinking phase
	lda input_blink			;Compute <C>=slow blinking
	lsr
	lsr
	lsr
	lsr
	lsr
	lda #$80+' '			;Default cursor is inverse
	scc
	eor #$80			;Blinking cursor is not
	ldy input_length
	cpy #input_max_length		;Do not print cursor if line is full
	scs
	sta input_sm,y			;Write to screen
	.endp

	ldy ch				;Any key pressed?
	lda #$ff			;Clear key code
	sta ch

	cpy #$27			;ATARI key also works as HELP, because Atari 400/800 have no help key.
	beq help_key_pressed
	cpy #$ff
	bne normal_key_pressed
	ldy helpfg
	bne help_key_pressed
	rts

	.proc help_key_pressed
	ldx #$00			;Clear HELP key flag
	stx helpfg

	lda input_help_mode		;Toggle help mode
	eor #$ff
	sta input_help_mode

	jsr visualization.menu.toggle_help_mode
	rts
	.endp

	.proc normal_key_pressed
	lda input_help_mode		;If help mode is active, any key first deactivates help mode
	bne help_key_pressed

	lda key_definitions,y		;Convert key code to ASCII
	cmp #$9b			;Do not convert RETURN
	seq
	and #127			;Mask out inverse bit

	.proc check_ignored_keys
	ldx #(.len ignored_keys)-1	;Check if the ASCII value is on the list of keys to ignore
loop	cmp ignored_keys,x
	sne
	rts				;Return if ignored key is found

	dex
	bpl loop			;Check next ignored key
	.endp

	.proc click			;Identical code from OS, but shorter
	ldx #$6e
loop	stx consol
	ldy vcount
wait	cpy vcount
	beq wait
	dex
	dex
	bpl loop
	.endp

	.proc check_first_key		;Clear the welcome message on first key
	pha
	lda input_started
	bne skip
	inc input_started
	jsr visualization.menu.clear_welcome
skip
	pla
	.endp
	
	cmp #$7f			;"TAB"
	bne no_tab
	jsr visualization.menu.toggle_details_mode
	rts

no_tab
	cmp #27				;"ESC"
	bne no_escape
	mva #search.status.start_simple_menu search.search_status
	rts

no_escape
	cmp #126			;"BACKSPACE"
	bne no_backspace
	ldy input_length
	beq backspace_done
	dec input_length		;Move left
	mvx #0 input_blink		;Keep cursor visible
	mvx #1 input_changed		;Indicate new search required

	lda #$80+' '
	sta input_sm-1,y		;Print cursor
	cpy #input_max_length
	beq backspace_done
	lda #' '			;Clear previous cursor position
	sta input_sm,y

backspace_done
	rts

no_backspace
	ldy input_length
	cpy #input_max_length
	beq text_input_done
	inc input_length		;Move right
	mvx #0 input_blink
	mvx #1 input_changed

	cmp #$20			;Convert to uppercase
	bcs no_control
	adc #$40
no_control
	cmp #'a'
	bcc no_lower_case
	cmp #'z'+1
	bcs no_lower_case
	sbc #$20-1
no_lower_case
	sta input_buffer,y		;Write to buffer
	sta input_sm,y			;Print character
	cpy #input_max_length-1		;Enough space for cursor?
	beq text_input_done
	lda #$80+' '			;Print new cursor position
	sta input_sm+1,y

text_input_done
	rts

;===============================================================

	.local key_definitions		;Directly included, so the menu also works with the OldRunner OS
	ins "TheMenu-Input-Key-Definitions.bin"
	.end
	
	.local ignored_keys		;ASCII values of keys that should be ignored
	.byte 28			;Ignore "CURSOR UP" with control
	.byte '-'			;Ignore "CURSOR UP" without control
	.byte 29			;Ignore "CURSOR DOWN" with control
	.byte '='			;Ignore "CURSOR DOWN" without control

	.byte 30			;Ignore "CURSOR UP" with control
	.byte '+'			;Ignore "CURSOR UP" without control
	.byte 31			;Ignore "CURSOR DOWN" with control
	.byte '*'			;Ignore "CURSOR DOWN" without control

	.byte $9b			;Ignore "RETURN"
	.endl

;===============================================================

	.endp				;End of normal_key_pressed
	.endp				;End of handle_keys

	.endp		;End of input