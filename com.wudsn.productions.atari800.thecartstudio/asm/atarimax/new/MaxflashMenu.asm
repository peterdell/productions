
screen_width	= $28
screen_lines	= $18

status_line	= $17

banksize	= $2000

; System equates
; 
; DISPLAY LIST EQUATES
; 
adli	= $80
alms	= $40
aempty1	= $00
aempty8	= $70
; 
; OS VARIABLES FOR XL/XE
warmst	= $08
boot	= $09
pokmsk	= $10
critic	= $42

old_vdslst	= $0c	;Backup for DLI handler
cursor_ptr	= $0e
bufadr		= $15
iccomt		= $17
dskfms		= $18
menu_key	= $19
dskutl		= $1a
abufpt		= $1c
ichidz		= $20
entry_type	= $21
iccomz		= $22
icstaz		= $23
icbalz		= $24
icbahz		= $25

icax4z		= $2d		;Used for empty display list, $41
icax5z		= $2e		;Used for empty display list, $2d
icax6z		= $2f		;Used for empty display list, $00

p1	= $38
p2	= $3a


; 
; PAGE 2
; 
vdslst	= $0200

sdmctl	= $022f
sdlstl	= $0230
sdlsth	= $0231

coldst	= $0244

pcolr0	= $02c0
pcolr1	= $02c1
pcolr2	= $02c2
pcolr3	= $02c3
color0	= $02c4
color1	= $02c5
color2	= $02c6
color3	= $02c7
runad	= $02e0
initad	= $02e2
ramsiz	= $02e4
chact	= $02f3
chbas	= $02f4
ch	= $02fc
; 
; PAGE 3
; 
iocb0	= $0340
basicf	= $03f8
gintlk	= $03fa


; 
; HARDWARE REGISTERS
; 
; GTIA
; 
trig0	= $d010
trig1	= $d011
trig3	= $d013
colpf1	= $d017
colpf2	= $d018
colbk	= $d01a
consol	= $d01f

; 
; POKEY
; 
audf0	= $d200
audc0	= $d201
audc2	= $d205
audc3	= $d207
audctl	= $d208

random	= $d20a
serout	= $d20d
irqen	= $d20e
skstat	= $d20f
; 
; PIA
; 
porta	= $d300
portb	= $d301
pactl	= $d302
pbctl	= $d303
; 
; ANTIC
; 
dmaclt	= $d400
chactl	= $d401
dlistl	= $d402
dlisth	= $d403
chbase	= $d409
wsync	= $d40a
vcount	= $d40b
nmien	= $d40e

; 
; ROM VECTORS
; 
ciov	= $e456
warmsv	= $e474
coldsv	= $e477

;
; Code equates
;
code_ram = $1000
dl_rbam	 = $1c00
sm_ram	 = $1c22

la000	= $a000
lc000	= $c000
ld000	= $d000
ld500	= $d500
le000	= $e000
siov	= $e459	;SIO jump address

	.local os
lc100	= $c100
lc256	= $c256
lc2d1	= $c2d1
lc2d2	= $c2d2
lc2d3	= $c2d3
lc2d4	= $c2d4
lc2d6	= $c2d6
lc2d7	= $c2d7
lc2d8	= $c2d8
lc2d9	= $c2d9
lc2e0	= $c2e0
lc2e1	= $c2e1
lc2e6	= $c2e6
lc2ea	= $c2ea
lc2f6	= $c2f6
lc30b	= $c30b
lc410	= $c410
lc448	= $c448		;"E:",$9b
lc485	= $c485
lc486	= $c486
lc487	= $c487
lc4a1	= $c4a1
lc4af	= $c4af
lc4b2	= $c4b2
lf104	= $f104
lf257	= $f257		;LDA #nn
lf259	= $f259		;STA $06/unpacker.bank_number
lf25b	= $f25b		;RTS
lffdc	= $ffdc		;JSR lc256
	.endl		;End of OS


;
; Start of code
;
	opt h-r-

	org $a000
;
	.local chr
	ins "MaxflashMenu.chr"
	.endl

	.local options
colors		.byte $00
cheese		.byte $80
reverse		.byte $00
runfirst	.byte $00	;$00 or $80+number of autostart entry number
fastkeys	.byte $00
anticoff	.byte $00
cartoff		.byte $80
textcolors	.byte $60,$91,$6f
osbank		.byte $00
xlosbank	.byte $00
mathpackbnk	.byte $00
mathpackpag	.byte $a0
mathpacksiz	.byte $08
osbdiskpag	.byte $a8
osbdisksiz	.byte $02
cheeseopts	.byte $00
stick2off	.byte $00
	.endl

	.proc start
	lda portb
	ora #$42
	sta portb
	and #$01
	bne os_not_in_ram
	jsr initialize_system
	jsr copy_dl_to_ram

	lda le000
	sta ichidz
	ldx #$00
	stx le000
	cmp #$ff
	beq no_autostart_active
	jmp autostart_entry

os_not_in_ram
no_autostart_active
	jsr check_option_pressed
	beq no_menu

	jsr initialize_system
	lda options.runfirst
	bpl no_autostart_defined
	and #$7f

the_cart_start_address		;IN: <A>=entry number (1..127)
	jsr get_from_menumap
	sta ichidz
	bne autostart_entry

no_autostart_defined
	jsr copy_dl_to_ram
	jsr dli.setup_dli
	jsr select_entry_lac20
	sta ichidz
	jsr flash_line_la467
	jsr dli.shutdown_dli

autostart_entry
	jmp start_menu_entry

no_menu	rts
	.endp

	.proc get_from_menumap	;IN: <A>=entry number (1..127), OUT: <A>=entry bank
	tax
	dex
	lda menu_entries.menumap,x
	rts
	.endp

	.proc flash_line_la467
	bit options.cheese
	bpl skip
	lda menu_key
	cmp #$05
	beq skip
	jsr select_entry_lac20.lada1.flash_line
skip	rts
	.endp

	.proc copy_page_to_l1000_and_run	;IN: <X>=lo, <Y>=hi
	stx p1
	sty p1+1
	lda #<code_ram
	sta p2
	lda #>code_ram
	sta p2+1
	ldy #$00
la484	lda (p1),y
	sta (p2),y
	iny
	bne la484
	jmp code_ram
	.endp

	.proc run_coldstart
	ldx #<run_coldstart_from_ram
	ldy #>run_coldstart_from_ram
	jmp copy_page_to_l1000_and_run

	.proc run_coldstart_from_ram
	lda #$00
	sta nmien
	sta irqen

	sei
	sta wsync
	sta ld500
	jmp coldsv
	.endp

	.endp

	.proc os_handler
	.proc copy_os_to_ram
	lda portb
	ora #$02
	sta portb
	sei
	lda #$00
	sta nmien
	lda portb
	and #$fe
	sta portb
	sta wsync
	sta ld500
	ldx options.xlosbank
	sta ld500,x
	mwa #la000 p1
	mwa #lc000 p2
	ldx #$40
page_loop
	ldy #$00
byte_loop
	lda (p1),y
	sta (p2),y
	iny
	bne byte_loop
	inc p1+1
	inc p2+1
	lda p2+1
	cmp #>ld000
	bne skip_d0
	clc
	lda p1+1
	adc #$08
	sta p1+1
	sec
	txa
	sbc #$08
	tax
	clc
	lda p2+1
	adc #$08
	sta p2+1
skip_d0	cmp #>le000
	bne skip_e0
	sta ld500
	ldy options.xlosbank
	iny
	sta ld500,y
	mwa #la000 p1
skip_e0	dex
	bne page_loop
	sta ld500
	rts
	.endp

	.proc set_os_c4af
	lda #>la000
	sta os.lc4af
	rts
	.endp

	.proc patch_os
	lda #$ea
:28	sta os.lc30b+#
:8	sta os.lc487+#
:8	sta os.lc4a1+#

	lda #$1d
	sta os.lc485
	lda #$c5
	sta os.lc486
	lda #$c0
	sta os.lc4af
	lda #$60
	sta os.lc4b2
	lda #$ea
:3	sta os.lc410+#
	rts
	.endp

	.proc la5ca
	sei
	lda #$00
	sta nmien
	lda portb
	and #$fe
	ora #$02
	sta portb
	sta ld500
	lda #$00
	sta p1
	lda options.mathpackpag
	sta p1+1
	lda #$00
	sta p2
	lda #$d8
	sta p2+1
	ldx options.mathpacksiz
	ldy options.mathpackbnk
	sta ld500,y
	ldy #$00
loop1	lda (p1),y
	sta (p2),y
	iny
	bne loop1
	inc p1+1
	inc p2+1
	dex
	bne loop1
	sta ld500

	lda #$00
	sta p1
	lda options.osbdiskpag
	sta p1+1

	lda #<os.lc100
	sta p2
	lda #>os.lc100
	sta p2+1
	ldx options.osbdisksiz
	ldy options.mathpackbnk
	sta ld500,y
	ldy #$00
loop2	lda (p1),y
	sta (p2),y
	iny
	bne loop2
	inc p1+1
	inc p2+1
	dex
	bne loop2
	
	.proc copy_os
	sta ld500
	ldx options.osbank
	sta ld500,x
	lda #<la000
	sta p1
	lda #>la000
	sta p1+1
	lda #<le000
	sta p2
	lda #>le000
	sta p2+1
	ldx #$20
	ldy #$00
loop	lda (p1),y
	sta (p2),y
	iny
	bne loop
	inc p1+1
	inc p2+1
	dex
	bne loop
	.endp

	sta ld500
	rts
	.endp

	.proc disable_os_rom
	os_ram_flag = dskfms

	lda portb
	and #$01
	bne la66b
	rts

la66b	sei
	lda #$00
	sta os_ram_flag
	sta nmien
	sta wsync
	lda portb
	pha
	and #$fe
	sta portb
	lda lc000
	inc lc000
	cmp lc000
	beq os_not_ram
	dec lc000
	inc os_ram_flag
os_not_ram
	pla
	sta portb
	lda #$40
	sta nmien
	cli
	lda os_ram_flag
	beq print_64k_required
	rts

	.proc print_64k_required
	jsr copy_dl_to_ram
	jsr screen.clear_screen
	ldx #$00
	ldy #$02
	jsr screen.set_cursor
	lda #<message
	ldy #>message
	jsr screen.print_line
stop	jmp stop

	.local message
	.byte 'This selection requires 64k RAM!',$9B
	.endl
	.endp


	.endp

	.endp
	
	.proc check_option_pressed	;OUT: <Z>=0/1
	lda consol
	bit options.reverse
	bpl skip
	eor #$04
skip	and #$04
	rts
	.endp

	.proc compute_index_on_bindata	;IN: ichidz = entry number, OUT: <A> = entry type
	ldy #$00
	ldx ichidz
	dex
	txa
	sta p2
	sty p2+1
	asl p2
	rol p2+1
	asl p2
	rol p2+1
	asl p2
	rol p2+1
	clc
	lda #<menu_entries.bindata
	adc p2
	sta p1
	lda #>menu_entries.bindata
	adc p2+1
	sta p1+1
	lda (p1),y
	rts
	.endp

	.proc get_next_byte_from_p1
	iny
	lda (p1),y
	rts
	.endp

	.proc init_blank_screen
	dl_zp = icax4z

	jsr set_colors_laae0
	lda #$ff
	sta ch
	lda #$41
	sta dl_zp
	lda #<dl_zp
	sta dl_zp+1
	sta sdlstl
	sta dlistl
	lda #>dl_zp
	sta dl_zp+2
	sta sdlsth
	sta dlisth
	lda #$e0
	sta chbas
	sta chbase
	sta wsync
loop	lda vcount
	bne loop
	ldy #$00
	bit options.anticoff
	bpl skip_blank
	sty sdmctl
	sty dmaclt
skip_blank
	rts
	.endp

	.proc start_menu_entry		;IN: ichidz = entry number
	jsr init_blank_screen
	jsr compute_index_on_bindata
	sta entry_type
	jsr get_next_byte_from_p1
	and #$f0
	sta bufadr
	lda #$00
	sta bufadr+1
	jsr get_next_byte_from_p1
	sta p2+1
	lda #$00
	sta p2
	jsr get_next_byte_from_p1
	pha
	jsr get_next_byte_from_p1
	pha
	jsr get_next_byte_from_p1
	pha
	sta iccomz
	jsr get_next_byte_from_p1
	sta icstaz
	jsr get_next_byte_from_p1
	sta icbalz
	lda portb
	and #$01
	beq handle_entry_type_lae7bf

	bit icstaz
	bpl la7a3
	lda os.lf104
	cmp #$4d
	beq la7a3

	jsr os_handler.disable_os_rom
	ldx #<os_handler.la5ca
	ldy #>os_handler.la5ca
	jsr copy_page_to_l1000_and_run
	lda ichidz
	sta le000
	jmp warmsv

la7a3	bit icstaz
	bvc handle_entry_type_lae7bf
	jsr os_handler.disable_os_rom
	ldx #<os_handler.copy_os_to_ram
	ldy #>os_handler.copy_os_to_ram
	jsr copy_page_to_l1000_and_run
	lda ichidz
	sta le000
	jsr os_handler.patch_os
	jsr os_handler.set_os_c4af
	jmp run_coldstart
	
	.proc handle_entry_type_lae7bf	;entry_type = type of entry/loader
	lda entry_type
	cmp #$01
	beq la7f8
	cmp #$02
	beq jump_la967
	cmp #$03
	beq jump_patch_sio_la99c
	cmp #$04
	beq jump_la893
	cmp #$05
	beq jump_la988
	cmp #$06
	beq jump_la97e
	cmp #$07
	beq jump_la992
loop	lda random		;Error loop
	sta colbk
	jmp loop

jump_patch_sio_la99c	jmp patch_sio_la99c
jump_la967	jmp la967
jump_la893	jmp la893
jump_la97e	jmp la97e
jump_la988	jmp la988
jump_la992	jmp la992


	.proc la7f8
	ldy #$06
	pla
	sta (bufadr),y
	ldy #$02
	pla
	sta (bufadr),y
	ldy #$03
	pla
	sta (bufadr),y
	ldy #$07
	lda #$00
	sta (bufadr),y
	ldy #$00
	lda icbalz
	and #$80
	lsr
	ora options.colors
	sta (bufadr),y
	ldy #$01
	lda options.cartoff
	sta (bufadr),y
	ldx #<unpacker_rom
	ldy #>unpacker_rom
	stx p1
	sty p1+1
	ldy #$00
la82a	lda (p1),y
	bit options.colors
	bpl la834
	sta colbk
la834	sta (p2),y
	iny
	bne la82a
	ldx #<lbebf
	ldy #>lbebf
	stx p1
	sty p1+1
	ldx #$00
la843	txa
	tay
	lda (p1),y
	beq la855
	tay
	lda (p2),y
	and #$0f
	ora bufadr
	sta (p2),y
	inx
	bne la843
la855
	ldx #<lbee9
	ldy #>lbee9
	stx p1
	sty p1+1

	ldx #$00
la85f	txa
	tay
	lda (p1),y
	beq la86d
	tay
	lda p2+1
	sta (p2),y
	inx
	bne la85f
la86d	lda #$01
	sta boot
	lda #$ff
	sta nmien
	tax
	txs
	cli
	lda icstaz
	and #$20
	beq la882
	jsr screen.open_editor
la882	lda icstaz
	and #$10
	beq la890
	lda #$00
	sta sdmctl
	sta dmaclt
la890	jmp (p2)
	.endp

	.proc la893
	jsr copy_dl_to_ram
	jsr screen.clear_screen
	ldx #$00
	ldy #$02
	jsr screen.set_cursor
	lda #<messages.la8d4
	ldy #>messages.la8d4
	jsr screen.print_line
	ldx #$00
	ldy #$03
	jsr screen.set_cursor
	lda #<messages.la8f5
	ldy #>messages.la8f5
	jsr screen.print_line
	ldx #$00
	ldy #$05
	jsr screen.set_cursor
	lda #<messages.la91b
	ldy #>messages.la91b
	jsr screen.print_line
	ldx #$00
	ldy #$06
	jsr screen.set_cursor
	lda #<messages.la942
	ldy #>messages.la942
	jsr screen.print_line
stop	jmp stop


	.local messages
la8d4	.byte '16kB ROM dumps are not supported',$9b
la8f5	.byte 'on Maxflash G1 cartridges or preview.',$9b
la91b	.byte 'Please try the cracked .XEX version of',$9b
la942	.byte 'this title in your workbook instead.',$9b
	.endl

	.endp

	.proc la967
	lda #$ff
	sta nmien
	tax
	txs
	cli
	ldx #<la976
	ldy #>la976
	jmp copy_page_to_l1000_and_run

	.proc la976
	ldx iccomz
	sta ld500,x
	jmp coldsv
	.endp

	.endp

	.proc la97e
	lda iccomz
	clc
	adc #$03
	sta iccomz
	jmp la967
	.endp

	.proc la988
	lda iccomz
	clc
	adc #$07
	sta iccomz
	jmp la967
	.endp

	.proc la992
	lda iccomz
	clc
	adc #$0f
	sta iccomz
	jmp la967
	.endp

	.proc patch_sio_la99c
	jsr os_handler.disable_os_rom
	lda #<os.lc100
	sta siov+1
	lda #>os.lc100
	sta siov+2
	lda #$20
	sta os.lffdc
	lda #<os.lc256
	sta os.lffdc+1
	lda #>os.lc256
	sta os.lffdc+2
	lda options.cartoff
	sta os.lc2d6
	lda options.colors
	sta os.lc2d7
	pla
	sta os.lc2d1
	pla
	sta os.lc2d2
	pla
	sta os.lc2d3
	jsr compute_index_on_bindata
	jsr get_next_byte_from_p1
	sta os.lc2d4
	cmp #$ff
	bne la9ef
	lda os.lc2e6
	ora #$20
	sta os.lc2e6
	lda #$01
	sta os.lc2e0
	lda #$00
	sta os.lc2e1
la9ef	jsr get_next_byte_from_p1
	tax
	and #$80
	sta os.lc2d8
	txa
	asl
	and #$80
	sta os.lc2d9
	lda #$00
	sta abufpt+2
	sta abufpt+3
laa05	jsr compute_index_on_bindata
	cmp #$03
	beq laa12
laa0c	inc ichidz
	bpl laa05
	bmi laa3c
laa12	ldx abufpt+2
	jsr get_next_byte_from_p1
	sta os.lc2f6,x
	inc abufpt+2
	ldx abufpt+3
	jsr get_next_byte_from_p1
	jsr get_next_byte_from_p1
	sta os.lc2ea,x
	inx
	jsr get_next_byte_from_p1
	sta os.lc2ea,x
	inx
	jsr get_next_byte_from_p1
	sta os.lc2ea,x
	inx
	stx abufpt+3
	cpx #$0c
	bne laa0c
laa3c	lda #$ff
	sta nmien
	tax
	txs
	cli
	lda options.cartoff
	pha
	ldx #<laa4f_at_1000
	ldy #>laa4f_at_1000
	jmp copy_page_to_l1000_and_run
	
	.proc laa4f_at_1000
	pla
	tax
	sta ld500,x
	lda #$a9		;LDA #
	sta os.lf257
	bit os.lc2d9
	bmi laa61
	lda #$c0		;LDA #$C0
	.byte $2c
laa61	lda #$a0		;LDA #$A0
	sta os.lf257+1
	lda #$85		;STA $06/unpacker.bank_number
	sta os.lf259
	lda #<unpacker.bank_number
	sta os.lf259+1
	lda #$60		;RTS
	sta os.lf25b

	bit os.lc2d9
	bpl laa82
	lda portb
	and #$fd
	sta portb
laa82	jmp coldsv
	.endp

	.endp

	.endp

	.endp			;End of start_menu_entry
 
	.proc initialize_system	;Initialize reset state, ports, POKEY, ANTIC, IRQs and NMIs
	lda #$00
	sta coldst
	sta warmst
	lda #$c0
	sta ramsiz
	inc basicf
	lda #$00
	sta porta
	lda #$3c
	sta pactl
	sta pbctl
	lda porta
	lda #$23
	sta skstat
	lda #$a0
	sta audc2
	sta audc3
	lda #$28
	sta audctl
	lda #$ff
	sta serout
	lda #$c0
	sta pokmsk
	sta irqen
	lda #$7f
	sta nmien
	lda #$00
	sta audctl
	lda #>chr
	sta chbase
	sta chbas
	lda #$02
	sta chactl
	sta chact
	jsr set_colors_laae0
	rts
	.endp

	.proc set_colors_laae0
	lda #$ff
	sta pcolr0
	sta pcolr1
	sta pcolr2
	sta pcolr3
	lda #$28
	sta color0
	lda #$ca
	sta color1
	lda #$94
	sta color2
	lda #$46
	sta color3
	rts
	.endp

	.proc lab03
	ldx #$00
	ldy #status_line
	jsr screen.set_cursor
	jsr screen.clear_line
	ldx #$00
	ldy #status_line
	jsr screen.set_cursor
	lda #<message_enter_choice
	ldy #>message_enter_choice
	jsr screen.print_line
	lda #$00
	sta ichidz
	lda abufpt+1
	jmp lab24.lab27

	.proc lab24
	jsr get_key_lae54
lab27	lda menu_key
	cmp #$00
	beq lab79
	cmp #$05
	bne lab24
	lda abufpt+1
	cmp #$34
	beq lab5e
	cmp #$1c
	beq labb7
	ldx #$00
lab3d	cmp keycodes,x
	beq lab49
	inx
	cpx #$0a
	bne lab3d
	beq lab24
lab49	ldy ichidz
	cpy #$03
	beq lab24
	lda numbers_labf8,x
	ldx ichidz
	sta icstaz,x
	jsr screen.print_ascii_char
	inc ichidz
	jmp lab24
lab5e	ldy ichidz
	beq lab24
	dec ichidz
	ldx bufadr
	dex
	jsr screen.set_cursor_x
	lda space_lac02
	jsr screen.print_ascii_char
	ldx bufadr
	dex
	jsr screen.set_cursor_x
	jmp lab24
	.endp

lab79	ldx ichidz
	beq labb7
lab7d	cpx #$03
	beq lab87
	jsr labb9
	inx
	bpl lab7d
lab87	lda #$00
	sta ichidz
	lda icstaz
	and #$0f
	beq lab99
	cmp #$02
	bcs labb7
	lda #$64
	sta ichidz
lab99	lda icbalz
	and #$0f
	tax
	lda page_offsets,x
	clc
	adc ichidz
	sta ichidz
	lda icbahz
	and #$0f
	clc
	adc ichidz
	sta ichidz
	beq labb7
	cmp #$80
	bcs labb7
	clc
	rts

labb7	sec
	rts

	.proc labb9
	lda icbalz
	sta icbahz
	lda icstaz
	sta icbalz
	lda numbers_labf8
	sta icstaz
	rts
	.endp

	.local message_enter_choice
	.byte 'Enter Choice (ESC to Abort):'
	.byte $20
	.endl


page_offsets	.byte $00,$0a,$14,$1e,$28,$32,$3c,$46,$50,$5a
keycodes	.byte $32,$1f,$1e,$1a,$18,$1d,$1b,$33,$35,$30
numbers_labf8	.byte '0123456789'
space_lac02	.byte ' '

	.endp

	.proc init_title_footer_and_status
	ldx #$00
	ldy #$00
	jsr screen.set_cursor
	lda #<title
	ldy #>title
	jsr screen.print_line
	ldx #$00
	ldy #status_line
	jsr screen.set_cursor
	lda #<footer
	ldy #>footer
	jsr screen.print_line
	rts
	.endp

	.proc select_entry_lac20
	jsr screen.clear_screen
	lda #$01
	sta dskutl
	lda #$00
	sta dskutl+1
	jsr init_title_footer_and_status
lac2e	jsr screen.clear_screen2
	lda #$00
	sta dskfms
	lda #<menu_entries.choices
	sta p1
	lda #>menu_entries.choices
	sta p1+1
lac3d	lda dskutl+1
	cmp dskfms
	beq lac4a
	jsr lafe0
	inc dskfms
	bpl lac3d
lac4a	lda menu_entries.totalchoices
	sec
	sbc dskutl+1
	cmp #$17
	bcc lac56
	lda #$16
lac56	sta abufpt
lac58	jsr screen.print_line.with_p1
	dec abufpt
	bne lac58

loop_lac5f
	ldy dskutl
	jsr screen.set_cursor_y
	jsr screen.invert_line
	jsr get_key_lae54
	jsr screen.normalize_inverted_line
	lda menu_key
	cmp #$01
	beq lacbc
	cmp #$02
	beq lace6
	cmp #$03
	beq lacd2
	cmp #$04
	beq lacb0
	cmp #$00
	beq lac9a
	cmp #$05
	bne loop_lac5f
	jsr lab03
	bcs lac94
	jsr get_from_menumap
	cmp #$ff
	beq lac94
	rts

lac94	jsr init_title_footer_and_status
	jmp loop_lac5f

lac9a	beq lad0d
lac9c	lda dskutl+1
	beq loop_lac5f
	sec
	sbc #$16
	bcc laca9
	sta dskutl+1
	bcs lac2e
laca9	lda #$00
	sta dskutl+1
	jmp lac2e
lacb0	lda dskutl
	cmp #$01
	beq lac9c
	lda #$01
	sta dskutl
lacba	bne loop_lac5f
lacbc	lda dskutl
	cmp #$01
	beq lacc6
	dec dskutl
	bpl loop_lac5f
lacc6	lda dskutl+1
lacc8	beq loop_lac5f
	dec dskutl+1
	jsr lada1
	jmp loop_lac5f
lacd2	lda dskutl
	cmp #$16
	beq lad1a
	lda #$16
	cmp menu_entries.totalchoices
	bcc lace2
	lda menu_entries.totalchoices
lace2	sta dskutl
	bne lacba
lace6	lda dskutl
	cmp #$16
	beq lacfb
	clc
	lda dskutl+1
	adc dskutl
	cmp menu_entries.totalchoices
lacf4	bcs lacf8
	inc dskutl
lacf8	jmp loop_lac5f
lacfb	lda dskutl+1
	clc
	adc #$16
	cmp menu_entries.totalchoices
	bcs lacf4
	inc dskutl+1
	jsr lad39
	jmp loop_lac5f

lad0d	clc
	lda dskutl+1
	adc dskutl
	jsr get_from_menumap
	cmp #$ff
	beq lacc8
	rts
lad1a	lda dskutl+1
	clc
	adc #$2c
	cmp menu_entries.totalchoices
	bcs lad2d
	lda dskutl+1
	adc #$16
	sta dskutl+1
	jmp lac2e
lad2d	sec
	lda menu_entries.totalchoices
	sbc #$16
	sta dskutl+1
	jmp lac2e
	rts

	.proc lad39
	clc
	lda screen.dl.lms_adr
	adc #screen_width
	sta p2
	lda screen.dl.lms_adr+1
	adc #$00
	sta p2+1

	clc
	lda screen.dl.lms_adr
	adc #screen_width*2
	sta p1
	lda screen.dl.lms_adr+1
	adc #$00
	sta p1+1

	ldx #$14
line_loop
	ldy #screen_width-1
char_loop
	lda (p1),y
	sta (p2),y
	dey
	bpl char_loop
	clc
	lda p1
	adc #screen_width
	sta p1
	lda p1+1
	adc #$00
	sta p1+1
	clc
	lda p2
	adc #screen_width
	sta p2
	lda p2+1
	adc #$00
	sta p2+1
	dex
	bpl line_loop
	lda #<menu_entries.choices
	sta p1
	lda #>menu_entries.choices
	sta p1+1
	clc
	lda #$14
	adc dskutl+1
	sta dskfms

loop	jsr lafe0
	dec dskfms
	bpl loop

	ldy dskutl
	jsr screen.set_cursor_y
	jsr screen.clear_line
	jsr screen.print_line.with_p1
	rts
	.endp



	.proc lada1
	clc
	lda screen.dl.lms_adr
	adc #$70
	sta p2
	lda screen.dl.lms_adr+1
	adc #$03
	sta p2+1
	clc
	lda screen.dl.lms_adr
	adc #$48
	sta p1
	lda screen.dl.lms_adr+1
	adc #$03
	sta p1+1
	ldx #$14
line_loop
	ldy #screen_width-1
char_loop
	lda (p1),y
	sta (p2),y
	dey
	bpl char_loop
	sec
	lda p1
	sbc #screen_width
	sta p1
	lda p1+1
	sbc #$00
	sta p1+1
	sec
	lda p2
	sbc #screen_width
	sta p2
	lda p2+1
	sbc #$00
	sta p2+1
	dex
	bpl line_loop
	lda #$70
	sta p1
	lda #$b1
	sta p1+1
	lda dskutl+1
	beq ladfc
	sta dskfms
ladf5	jsr lafe0
	dec dskfms
	bne ladf5
ladfc	ldy dskutl
	jsr screen.set_cursor_y
	jsr screen.clear_line
	jsr screen.print_line.with_p1
	rts
	
	.proc flash_line
	lda #$04
	sta dskfms
loop	jsr screen.invert_line
	jsr delay_lae39
	jsr screen.normalize_inverted_line
	jsr delay_lae39
	dec dskfms
	bne loop
	rts
	.endp

	.proc play_note		;IN: <A>=audf0
	sta audf0
	ldx #$e0
loop1	stx audc0
	jsr delay_lae44
	inx
	cpx #$e9
	bne loop1
loop2	stx audc0
	jsr delay_lae44
	dex
	cpx #$df
	bne loop2
	rts
	.endp

	.proc delay_lae39
	ldx #$0f
outer_loop
	ldy #$ff
inner_loop
	dey
	bne inner_loop
	dex
	bne outer_loop
	rts
	.endp
	
	.proc delay_lae44
	pha
	txa
	pha
	tya
	pha
	ldx #$01
	jsr delay_lae39.outer_loop
	pla
	tay
	pla
	tax
	pla
	rts
	.endp
	.endp

	.endp
	
	.proc get_key_lae54
	lda #$ff
	sta menu_key
	sta ch
	bit options.fastkeys
	bpl lae63
	ldx #$45
	.byte $2c
lae63	ldx #$75
	tay
lae66	dey
	bne lae66
	dex
	bne lae66
lae6c	lda #$01
	bit options.stick2off
	bmi lae75
	lda #$11
lae75	sta dskfms
	lda porta
	and dskfms
	cmp dskfms
	bne laed9
	asl dskfms
	lda porta
	and dskfms
	cmp dskfms
	bne laedc
	asl dskfms
	lda porta
	and dskfms
	cmp dskfms
	bne laed3
	asl dskfms
	lda porta
	and dskfms
	cmp dskfms
	bne laed6
	lda trig0
	beq laee2
	bit options.stick2off
	bmi key_loop
	lda trig1
	beq laee2

key_loop
	lda ch
	cmp #$ff
	beq lae6c
	cmp #$0f
	beq laedc
	cmp #$0e
	beq laed9
	cmp #$0c
	beq laee2
	cmp #$21
	beq laee2
	cmp #$07
	beq laed6
	cmp #$06
	beq laed3
	sta abufpt+1
	bne laedf
laed3	lda #$04
	.byte $2c
laed6	lda #$03
	.byte $2c
laed9	lda #$01
	.byte $2c
laedc	lda #$02
	.byte $2c
laedf	lda #$05
	.byte $2c
laee2	lda #$00
	sta menu_key
	bit options.cheese
	bpl laef0
	lda #$08
	jsr select_entry_lac20.lada1.play_note
laef0	rts
	.endp

	.proc copy_dl_to_ram
	mwa #dl_rbam p2
	mwa #screen.dl p1
	ldy #[.len screen.dl]-1
loop	lda (p1),y
	sta (p2),y
	dey
	bpl loop

	lda screen.dl.vbl_adr
	sta sdlstl
	sta dlistl
	lda screen.dl.vbl_adr+1
	sta sdlsth
	sta dlisth

	lda #$22
	sta sdmctl
	sta dmaclt
	lda #>chr
	sta chbas
	sta chbase
	jsr set_colors_laae0
	lda #2
	sta chact
	sta chactl
	rts
	.endp

;===============================================================

	.proc dli

	.proc dli_handler
	pha
	txa
	pha
	tya
	pha
	lda vcount
	cmp #$10
	bcs laf48
	lda #$00
	sta abufpt+2
laf48	ldx abufpt+2
	ldy options.textcolors,x
	inc abufpt+2
	lda abufpt+2
	cmp #$01
	beq laf73
	cmp #$02
	beq laf9d
	bit options.cheeseopts
	bmi laf8d
	ldx #$07
laf60	sty wsync
	sty colpf2
	tya
	sbc #$03
	sta colpf1
	dey
	dey
	dex
	bne laf60
	beq lafa3
laf73	bit options.cheeseopts
	bmi laf8d
	ldx #$07
laf7a	sty wsync
	sty colpf2
	tya
	adc #$03
	sta colpf1
	iny
	iny
	dex
	bne laf7a
	beq lafa3
laf8d	tya
	sta wsync
	and #$f0
	sta colpf2
	lda #$ff
	sta colpf1
	bne lafa3
laf9d	sty wsync
	sty colpf2
lafa3	pla
	tay
	pla
	tax
	pla
	rti
	.endp

	.proc shutdown_dli
	bit options.cheese
	bpl setup_dli.return
	lda #$7f
	sta nmien
	sta wsync
	lda old_vdslst
	sta vdslst
	lda old_vdslst+1
	sta vdslst+1
	rts
	.endp

	.proc setup_dli
	bit options.cheese
	bpl return
	lda vdslst
	sta old_vdslst
	lda vdslst+1
	sta old_vdslst+1
	ldx #<dli_handler
	ldy #>dli_handler
	stx vdslst
	sty vdslst+1
	lda #$ff
	sta nmien
return	rts
	.endp
	
	.endp		;End of dli

;===============================================================

	.proc lafe0
	lda #$00
	sta iccomt
lafe4	ldy #$00
	lda (p1),y
	beq lafee
	cmp #$9b
	bne laff0
lafee	dec iccomt
laff0	clc
	lda p1
	adc #$01
	sta p1
	lda p1+1
	adc #$00
	sta p1+1
	bit iccomt
	bpl lafe4
	rts
	.endp
	
	

;===============================================================

	.proc screen

	.proc print_line		;IN: <A>=lo, <Y>=hi
	sta p1
	sty p1+1
with_p1	lda #$00			;IN: p1 = source address
	sta iccomt
lb00a	ldy #$00
	lda (p1),y
	beq lb01b
	cmp #$9b
	bne lb01f
	ldx #$00
	ldy bufadr+1
	jsr advance_cursor.lb128
lb01b	dec iccomt
	bmi lb022
lb01f	jsr print_ascii_char
lb022	clc
	lda p1
	adc #$01
	sta p1
	lda p1+1
	adc #$00
	sta p1+1
	bit iccomt
	bpl lb00a
	rts
	.endp

	.proc clear_screen
	line = dskfms
	lda #$00
	sta line
loop	ldx #$00
	ldy line
	jsr set_cursor
	jsr clear_line
	lda line
	cmp #status_line
	beq last_line
	inc line
	bpl loop

last_line
	ldx #$00
	ldy #$00
	jsr set_cursor
	rts
	.endp

	.proc clear_screen2
	line = dskfms

	lda #$01
	sta line
loop	ldx #$00
	ldy line
	jsr set_cursor
	jsr clear_line
	lda line
	cmp #$16
	beq last_line
	inc line
	bpl loop
last_line
	ldx #$00
	ldy #$01
	jsr set_cursor
	rts
	.endp
	
	.proc clear_line		;IN:  bufadr+1 = line
	ldx #$00
	ldy bufadr+1
	jsr set_cursor
	lda #$00
	ldy #$27
loop	sta (cursor_ptr),y
	dey
	bpl loop
	rts
	.endp

	.proc invert_line		;IN:  bufadr+1 = line
	ldx #$00
	ldy bufadr+1
	jsr set_cursor
	ldy #$27
loop	lda (cursor_ptr),y
	ora #$80
	sta (cursor_ptr),y
	dey
	bpl loop
	rts
	.endp

	.proc normalize_inverted_line
	ldy #$27
loop	lda (cursor_ptr),y
	and #$7f
	sta (cursor_ptr),y
	dey
	bpl loop
	rts
	.endp

	.proc set_cursor
	stx bufadr
	sty bufadr+1
	jsr clear_cursor
	lda dl.lms_adr
	sta cursor_ptr
	lda dl.lms_adr+1
	sta cursor_ptr+1
	ldy bufadr+1
	beq lb0c9
lb0b9	clc
	lda cursor_ptr
	adc #screen_width
	sta cursor_ptr
	lda cursor_ptr+1
	adc #$00
	sta cursor_ptr+1
	dey
	bne lb0b9
lb0c9	clc
	lda cursor_ptr
	adc bufadr
	sta cursor_ptr
	lda cursor_ptr+1
	adc #$00
	sta cursor_ptr+1
	jsr print_cursor
	rts
	.endp

	.proc set_cursor_x		;Set cursor to column <X> keeping the current line
	ldy bufadr+1
	jsr set_cursor
	rts
	.endp

	.proc set_cursor_y		;Set cursor to line <Y> keeping the current column
	ldx bufadr
	jsr set_cursor
	rts
	.endp

	.proc print_cursor
	ldy #$00
	lda (cursor_ptr),y
	ora #$80
	sta (cursor_ptr),y
	rts
	.endp

	.proc clear_cursor
	ldy #$00
	lda (cursor_ptr),y
	and #$7f
	sta (cursor_ptr),y
	rts
	.endp

	.proc print_ascii_char		;Print ASCII char, in <A>=ASCII char
	pha
	jsr clear_cursor
	pla
	jsr convert_to_atascii
	ldy #$00
	sta (cursor_ptr),y
	jsr advance_cursor
	jsr print_cursor
	rts
	
	.proc convert_to_atascii
	cmp #$20
	bcs no_control
	sec
	sbc #$40
	jmp return
no_control
	cmp #$5f
	bcs return
	sec
	sbc #$20
return	rts
	.endp

	.endp

	.proc advance_cursor
	ldy bufadr+1
	ldx bufadr
	inx
	cpx #screen_width
	bne lb12f
	ldx #$00
lb128	iny
	cpy #status_line
	bcc lb12f
	ldy #$01
lb12f	jsr set_cursor
	rts
	.endp

	.proc open_editor	;Open #0,12,0,"E:"

	.enum iocb
iccom	= $02
icbal	= $04
icbah	= $05
icax1	= $0a
	.ende

	lda #$03
	ldx #$00
	sta iocb0+iocb.iccom,x
	lda #<os.lc448	;Contains "E:",$9b
	sta iocb0+iocb.icbal,x
	lda #>os.lc448
	sta iocb0+iocb.icbah,x
	lda #$0c
	sta iocb0+iocb.icax1,x
	jsr ciov
	rts
	.endp

	.local dl
	.byte aempty8
	.byte aempty8
	.byte adli+aempty8
	.byte alms+$02
lms_adr	.word sm_ram
	.byte adli+aempty1
	.byte $02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02
	.byte $02,$02,$02,$02,$02,$02
	.byte adli+aempty1
	.byte $02
	.byte $41
vbl_adr	.word dl_rbam
	.endl
	
	.endp

	.local menu_entries

totalchoices	.byte ''

	.local choices
	.byte '<  1> A'
	.byte $9B
	.byte '<  2> B'
	.byte $9B
	.byte '<  3> C'
	.byte $9b
	.byte '<  4> '
	.byte $9b
	.byte '<  5> '
	.byte $9b
	.byte '<  6> '
	.byte $9b
	.byte '<  7> '
	.byte $9b
	.byte '<  8> '
	.byte $9b
	.byte '<  9> '
	.byte $9b
	.byte '< 10> '
	.byte $9b
	.byte '< 11> '
	.byte $9b
	.byte '< 12> '
	.byte $9b
	.byte '< 13> '
	.byte $9b
	.byte '< 14> '
	.byte $9b
	.byte '< 15> '
	.byte $9b
	.byte '< 16> '
	.byte $9b
	.byte '< 17> '
	.byte $9b
	.byte '< 18> '
	.byte $9b
	.byte '< 19> '
	.byte $9b
	.byte '< 20> '
	.byte $9b
	.byte '< 21> '
	.byte $9b
	.byte '< 22> '
	.byte $9b
	.byte '< 23> '
	.byte $9b
	.byte '< 24> '
	.byte $9b
	.byte '< 25> '
	.byte $9b
	.byte '< 26> '
	.byte $9b
	.byte '< 27> '
	.byte $9b
	.byte '< 28> '
	.byte $9b
	.byte '< 29> '
	.byte $9b
	.byte '< 30> '
	.byte $9b
	.byte '< 31> '
	.byte $9b
	.byte '< 32> '
	.byte $9b
	.byte '< 33> '
	.byte $9b
	.byte '< 34> '
	.byte $9b
	.byte '< 35> '
	.byte $9b
	.byte '< 36> '
	.byte $9b
	.byte '< 37> '
	.byte $9b
	.byte '< 38> '
	.byte $9b
	.byte '< 39> '
	.byte $9b
	.byte '< 40> '
	.byte $9b
	.byte '< 41> '
	.byte $9b
	.byte '< 42> '
	.byte $9b
	.byte '< 43> '
	.byte $9b
	.byte '< 44> '
	.byte $9b
	.byte '< 45> '
	.byte $9b
	.byte '< 46> '
	.byte $9b
	.byte '< 47> '
	.byte $9b
	.byte '< 48> '
	.byte $9b
	.byte '< 49> '
	.byte $9b
	.byte '< 50> '
	.byte $9b
	.byte '< 51> '
	.byte $9b
	.byte '< 52> '
	.byte $9b
	.byte '< 53> '
	.byte $9b
	.byte '< 54> '
	.byte $9b
	.byte '< 55> '
	.byte $9b
	.byte '< 56> '
	.byte $9b
	.byte '< 57> '
	.byte $9b
	.byte '< 58> '
	.byte $9b
	.byte '< 59> '
	.byte $9b
	.byte '< 60> '
	.byte $9b
	.byte '< 61> '
	.byte $9b
	.byte '< 62> '
	.byte $9b
	.byte '< 63> '
	.byte $9b
	.byte '< 64> '
	.byte $9b
	.byte '< 65> '
	.byte $9b
	.byte '< 66> '
	.byte $9b
	.byte '< 67> '
	.byte $9b
	.byte '< 68> '
	.byte $9b
	.byte '< 69> '
	.byte $9b
	.byte '< 70> '
	.byte $9b
	.byte '< 71> '
	.byte $9b
	.byte '< 72> '
	.byte $9b
	.byte '< 73> '
	.byte $9b
	.byte '< 74> '
	.byte $9b
	.byte '< 75> '
	.byte $9b
	.byte '< 76> '
	.byte $9b
	.byte '< 77> '
	.byte $9b
	.byte '< 78> '
	.byte $9b
	.byte '< 79> '
	.byte $9b
	.byte '< 80> '
	.byte $9b
	.byte '< 81> '
	.byte $9b
	.byte '< 82> '
	.byte $9b
	.byte '< 83> '
	.byte $9b
	.byte '< 84> '
	.byte $9b
	.byte '< 85> '
	.byte $9b
	.byte '< 86> '
	.byte $9b
	.byte '< 87> '
	.byte $9b
	.byte '< 88> '
	.byte $9b
	.byte '< 89> '
	.byte $9b
	.byte '< 90> '
	.byte $9b
	.byte '< 91> '
	.byte $9b
	.byte '< 92> '
	.byte $9b
	.byte '< 93> '
	.byte $9b
	.byte '< 94> '
	.byte $9b
	.byte '< 95> '
	.byte $9b
	.byte '< 96> '
	.byte $9b
	.byte '< 97> '
	.byte $9b
	.byte '< 98> '
	.byte $9b
	.byte '< 99> '
	.byte $9b
	.byte '<100> '
	.byte $9b
	.byte '<101> '
	.byte $9b
	.byte '<102> '
	.byte $9b
	.byte '<103> '
	.byte $9b
	.byte '<104> '
	.byte $9b
	.byte '<105> '
	.byte $9b
	.byte '<106> '
	.byte $9b
	.byte '<107> '
	.byte $9b
	.byte '<108> '
	.byte $9b
	.byte '<109> '
	.byte $9b
	.byte '<110> '
	.byte $9b
	.byte '<111> '
	.byte $9b
	.byte '<112> '
	.byte $9b
	.byte '<113> '
	.byte $9b
	.byte '<114> '
	.byte $9b
	.byte '<115> '
	.byte $9b
	.byte '<116> '
	.byte $9b
	.byte '<117> '
	.byte $9b
	.byte '<118> '
	.byte $9b
	.byte '<119> '
	.byte $9b
	.byte '<120> '
	.byte $9b
	.byte '<121> '
	.byte $9b
	.byte '<122> '
	.byte $9b
	.byte '<123> '
	.byte $9b
	.byte '<124> '
	.byte $9b
	.byte '<125> '
	.byte $9b
	.byte '<126> '
	.byte $9b
	.byte '<127> 127 Last Entry'
	.byte $9b
	.endl

	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff

	.local bindata
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff

	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff

	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff
	.endl

	.local menumap
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.byte $ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	.endl

	.endl		;End of menu_entries

unpacker_rom
	icl "MaxflashMenu-Unpacker.asm"

	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $ff
lbebf	.byte $01,$06,$0b,$10,$15,$17,$1b,$1d,$1f,$21,$30,$32,$45,$63,$69
	.byte $6f,$78,$7a,$7f,$81,$83,$8e,$91,$95,$97,$9b,$a3,$a7,$ab,$ad,$b1
	.byte $b7,$bb,$bd,$c2,$c6,$c8,$cc,$d2,$da,$e2,$00
lbee9	.byte $04,$09,$0e,$13,$2e,$3f,$74,$86,$8a,$9f,$cf,0
title	.byte '>   Atarimax Maxflash MultiCart Menu   <',0
footer	.byte '>  Use Keyboard or Joystick to Select  <',0
signature
	.byte 'Maxflash Menu Software, Copyright 2009 Steven J Tucker',0

	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00

	jmp start

	.word start
	.byte $00,$01
	.word start
  
