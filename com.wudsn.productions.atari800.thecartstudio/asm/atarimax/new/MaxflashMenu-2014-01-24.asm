;
; System equates
;
; OS EQUATES
; ----------
; 
; Syntax:
; Use '=' for addresses
; and '<' for read addresses (ex: KBCODE)
; and '>' for write addresses (ex: STIMER)
; and '#' for values
; 
; IO EQUATES
; 
iccom	= $0002
icbal	= $0004
icbah	= $0005
icax1	= $000a

; 
; DISPLAY LIST EQUATES
; 
adli	 = $0080
avb	  = $0040
alms	 = $0040
ajmp	 = $0001
aempty1     = $0000
aempty8     = $0070
; 
; OS VARIABLES FOR XL/XE
; 
; PAGE 0
; 
ngflag	= $0001
casini	= $0002
ramlo	= $0004
tramsz	= $0006
cmcmd	= $0007
warmst	= $0008
boot	= $0009

dosini      = $000c
appmhi      = $000e
pokmsk      = $0010

bufadr      = $0015
iccomt      = $0017
dskfms      = $0018
dskutl      = $001a
abufpt      = $001c
ichidz      = $0020
icdnoz      = $0021
iccomz      = $0022
icstaz      = $0023
icbalz      = $0024
icbahz      = $0025

icax4z      = $002d
icax5z      = $002e
icax6z      = $002f

bufrfl      = $0038
recvdn      = $0039
xmtdon      = $003a
chksnt      = $003b

critic      = $0042

; 
; PAGE 2
; 
vdslst      = $0200

sdmctl      = $022f
sdlstl      = $0230
sdlsth      = $0231

coldst      = $0244

pcolr0      = $02c0
pcolr1      = $02c1
pcolr2      = $02c2
pcolr3      = $02c3
color0      = $02c4
color1      = $02c5
color2      = $02c6
color3      = $02c7
runad	= $02e0
initad      = $02e2
ramsiz      = $02e4
chact	= $02f3
chbas	= $02f4
ch	   = $02fc
; 
; PAGE 3
; 
iocb0	= $0340
basicf      = $03f8
gintlk      = $03fa


; 
; HARDWARE REGISTERS
; 
; GTIA
; 
trig0	= $d010
trig1	= $d011
trig3	= $d013
colpf1      = $d017
colpf2      = $d018
colbk	= $d01a
consol      = $d01f

; 
; POKEY
; 
pot0	 = $d200
pot1	 = $d201
pot5	 = $d205
pot7	 = $d207
audctl	= $d208

skrest      = $d20a
serout      = $d20d
irqen	= $d20e
skstat      = $d20f
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
dmaclt      = $d400
chactl      = $d401
dlistl      = $d402
dlisth      = $d403
chbase      = $d409
wsync	= $d40a
vcount      = $d40b
nmien	= $d40e

; 
; ROM VECTORS
; 
ciov	 = $e456
warmsv      = $e474
coldsv      = $e477

;
; Code equates
;
L015F	= $015F
L0162	= $0162
L019D	= $019D
L01D9	= $01D9
L01E1	= $01E1
L1000	= $1000
L1C00	= $1C00
L1C22	= $1C22
L75A2	= $75A2
LAA61	= $AA61
LAE63	= $AE63
LB152	= $B152

LC000	= $C000
LC2D1	= $C2D1
LC2D2	= $C2D2
LC2D3	= $C2D3
LC2D4	= $C2D4
LC2D6	= $C2D6
LC2D7	= $C2D7
LC2D8	= $C2D8
LC2D9	= $C2D9
LC2E0	= $C2E0
LC2E1	= $C2E1
LC2E6	= $C2E6
LC2EA	= $C2EA
LC2F6	= $C2F6
LC30B	= $C30B
LC30C	= $C30C
LC30D	= $C30D
LC30E	= $C30E
LC30F	= $C30F
LC310	= $C310
LC311	= $C311
LC312	= $C312
LC313	= $C313
LC314	= $C314
LC315	= $C315
LC316	= $C316
LC317	= $C317
LC318	= $C318
LC319	= $C319
LC31A	= $C31A
LC31B	= $C31B
LC31C	= $C31C
LC31D	= $C31D
LC31E	= $C31E
LC31F	= $C31F
LC320	= $C320
LC321	= $C321
LC322	= $C322
LC323	= $C323
LC324	= $C324
LC325	= $C325
LC326	= $C326
LC410	= $C410
LC411	= $C411
LC412	= $C412
LC485	= $C485
LC486	= $C486
LC487	= $C487
LC488	= $C488
LC489	= $C489
LC48A	= $C48A
LC48B	= $C48B
LC48C	= $C48C
LC48D	= $C48D
LC48E	= $C48E
LC4A1	= $C4A1
LC4A2	= $C4A2
LC4A3	= $C4A3
LC4A4	= $C4A4
LC4A5	= $C4A5
LC4A6	= $C4A6
LC4A7	= $C4A7
LC4A8	= $C4A8
LC4AF	= $C4AF
LC4B2	= $C4B2
LD500	= $D500
LE000	= $E000
LE45A	= $E45A
LE45B	= $E45B
LF104	= $F104
LF257	= $F257
LF258	= $F258
LF259	= $F259
LF25A	= $F25A
LF25B	= $F25B
LFFDC	= $FFDC
LFFDD	= $FFDD
LFFDE	= $FFDE
;
; Start of code
;
	opt h-
	org $A000
;
	.local font
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$18,$18,$18,$18,$00,$18,$00
	.byte $00,$66,$66,$66,$00,$00,$00,$00,$00,$66,$FF,$66,$66,$FF,$66,$00
	.byte $18,$3E,$60,$3C,$06,$7C,$18,$00,$00,$66,$6C,$18,$30,$66,$46,$00
	.byte $1C,$36,$1C,$38,$6F,$66,$3B,$00,$00,$18,$18,$18,$00,$00,$00,$00
	.byte $00,$0E,$1C,$18,$18,$1C,$0E,$00,$00,$70,$38,$18,$18,$38,$70,$00
	.byte $00,$66,$3C,$FF,$3C,$66,$00,$00,$00,$18,$18,$7E,$18,$18,$00,$00
	.byte $00,$00,$00,$00,$00,$18,$18,$30,$00,$00,$00,$7E,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$18,$18,$00,$00,$06,$0C,$18,$30,$60,$40,$00
	.byte $00,$3C,$66,$6E,$76,$66,$3C,$00,$00,$18,$38,$18,$18,$18,$7E,$00
	.byte $00,$3C,$66,$0C,$18,$30,$7E,$00,$00,$7E,$0C,$18,$0C,$66,$3C,$00
	.byte $00,$0C,$1C,$3C,$6C,$7E,$0C,$00,$00,$7E,$60,$7C,$06,$66,$3C,$00,$00,$3C,$60,$7C,$66,$66,$3C,$00,$00
	.byte $7E,$06,$0C,$18,$30,$30,$00,$00,$3C,$66,$3C,$66,$66,$3C,$00,$00
	.byte $3C,$66,$3E,$06,$0C,$38,$00,$00,$00,$18,$18,$00,$18,$18,$00,$00
	.byte $00,$18,$18,$00,$18,$18,$30,$06,$0C,$18,$30,$18,$0C,$06,$00,$00
	.byte $00,$7E,$00,$00,$7E,$00,$00,$60,$30,$18,$0C,$18,$30,$60,$00,$00
	.byte $3C,$66,$0C,$18,$00,$18,$00,$00,$3C,$66,$6E,$6E,$60,$3E,$00,$00
	.byte $18,$3C,$66,$66,$7E,$66,$00,$00,$7C,$66,$7C,$66,$66,$7C,$00,$00
	.byte $3C,$66,$60,$60,$66,$3C,$00,$00,$78,$6C,$66,$66,$6C,$78,$00,$00
	.byte $7E,$60,$7C,$60,$60,$7E,$00,$00,$7E,$60,$7C,$60,$60,$60,$00,$00
	.byte $3E,$60,$60,$6E,$66,$3E,$00,$00,$66,$66,$7E,$66,$66,$66,$00,$00
	.byte $7E,$18,$18,$18,$18,$7E,$00,$00,$06,$06,$06,$06,$66,$3C,$00,$00
	.byte $66,$6C,$78,$78,$6C,$66,$00,$00,$60,$60,$60,$60,$60,$7E,$00,$00
	.byte $63,$77,$7F,$6B,$63,$63,$00,$00,$66,$76,$7E,$7E,$6E,$66,$00,$00
	.byte $3C,$66,$66,$66,$66,$3C,$00,$00,$7C,$66,$66,$7C,$60,$60,$00,$00
	.byte $3C,$66,$66,$66,$6C,$36,$00,$00,$7C,$66,$66,$7C,$6C,$66,$00,$00
	.byte $3C,$60,$3C,$06,$06,$3C,$00,$00,$7E,$18,$18,$18,$18,$18,$00,$00
	.byte $66,$66,$66,$66,$66,$7E,$00,$00,$66,$66,$66,$66,$3C,$18,$00,$00
	.byte $63,$63,$6B,$7F,$77,$63,$00,$00,$66,$66,$3C,$3C,$66,$66,$00,$00
	.byte $66,$66,$3C,$18,$18,$18,$00,$00,$7E,$0C,$18,$30,$60,$7E,$00,$00
	.byte $1E,$18,$18,$18,$18,$1E,$00,$00,$40,$60,$30,$18,$0C,$06,$00,$00
	.byte $78,$18,$18,$18,$18,$78,$00,$00,$08,$1C,$36,$63,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$FF,$00,$00,$36,$7F,$7F,$3E,$1C,$08,$00,$18
	.byte $18,$18,$1F,$1F,$18,$18,$18,$03,$03,$03,$03,$03,$03,$03,$03,$18
	.byte $18,$18,$F8,$F8,$00,$00,$00,$18,$18,$18,$F8,$F8,$18,$18,$18,$00
	.byte $00,$00,$F8,$F8,$18,$18,$18,$03,$07,$0E,$1C,$38,$70,$E0,$C0,$C0
	.byte $E0,$70,$38,$1C,$0E,$07,$03,$01,$03,$07,$0F,$1F,$3F,$7F,$FF,$00
	.byte $00,$00,$00,$0F,$0F,$0F,$0F,$80,$C0,$E0,$F0,$F8,$FC,$FE,$FF,$0F
	.byte $0F,$0F,$0F,$00,$00,$00,$00,$F0,$F0,$F0,$F0,$00,$00,$00,$00,$FF
	.byte $FF,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$FF,$FF,$00
	.byte $00,$00,$00,$F0,$F0,$F0,$F0,$00,$1C,$1C,$77,$77,$08,$1C,$00,$00
	.byte $00,$00,$1F,$1F,$18,$18,$18,$00,$00,$00,$FF,$FF,$00,$00,$00,$18
	.byte $18,$18,$FF,$FF,$18,$18,$18,$00,$00,$3C,$7E,$7E,$7E,$3C,$00,$00
	.byte $00,$00,$00,$FF,$FF,$FF,$FF,$C0,$C0,$C0,$C0,$C0,$C0,$C0,$C0,$00
	.byte $00,$00,$FF,$FF,$18,$18,$18,$18,$18,$18,$FF,$FF,$00,$00,$00,$F0
	.byte $F0,$F0,$F0,$F0,$F0,$F0,$F0,$18,$18,$18,$1F,$1F,$00,$00,$00,$78
	.byte $60,$78,$60,$7E,$18,$1E,$00,$00,$18,$3C,$7E,$18,$18,$18,$00,$00
	.byte $18,$18,$18,$7E,$3C,$18,$00,$00,$18,$30,$7E,$30,$18,$00,$00,$00
	.byte $18,$0C,$7E,$0C,$18,$00,$00,$00,$18,$3C,$7E,$7E,$3C,$18,$00,$00
	.byte $00,$3C,$06,$3E,$66,$3E,$00,$00,$60,$60,$7C,$66,$66,$7C,$00,$00
	.byte $00,$3C,$60,$60,$60,$3C,$00,$00,$06,$06,$3E,$66,$66,$3E,$00,$00
	.byte $00,$3C,$66,$7E,$60,$3C,$00,$00,$0E,$18,$3E,$18,$18,$18,$00,$00
	.byte $00,$3E,$66,$66,$3E,$06,$7C,$00,$60,$60,$7C,$66,$66,$66,$00,$00
	.byte $18,$00,$38,$18,$18,$3C,$00,$00,$06,$00,$06,$06,$06,$06,$3C,$00
	.byte $60,$60,$6C,$78,$6C,$66,$00,$00,$38,$18,$18,$18,$18,$3C,$00,$00
	.byte $00,$66,$7F,$7F,$6B,$63,$00,$00,$00,$7C,$66,$66,$66,$66,$00,$00
	.byte $00,$3C,$66,$66,$66,$3C,$00,$00,$00,$7C,$66,$66,$7C,$60,$60,$00
	.byte $00,$3E,$66,$66,$3E,$06,$06,$00,$00,$7C,$66,$60,$60,$60,$00,$00
	.byte $00,$3E,$60,$3C,$06,$7C,$00,$00,$18,$7E,$18,$18,$18,$0E,$00,$00
	.byte $00,$66,$66,$66,$66,$3E,$00,$00,$00,$66,$66,$66,$3C,$18,$00,$00
	.byte $00,$63,$6B,$7F,$3E,$36,$00,$00,$00,$66,$3C,$18,$3C,$66,$00,$00
	.byte $00,$66,$66,$66,$3E,$0C,$78,$00,$00,$7E,$0C,$18,$30,$7E,$00,$00
	.byte $18,$3C,$7E,$7E,$18,$3C,$00,$18,$18,$18,$18,$18,$18,$18,$18,$00
	.byte $7E,$78,$7C,$6E,$66,$06,$00,$08,$18,$38,$78,$38,$18,$08,$00,$10
	.byte $18,$1C,$1E,$1C,$18,$10,$00
	.endl

	.local config
LA400	.byte $00
LA401	.byte $80
LA402	.byte $00
LA403	.byte $00
LA404	.byte $00
LA405	.byte $00
LA406	.byte $80
LA407	.byte $60,$91,$6F
LA40A	.byte $00
LA40B	.byte $00
LA40C	.byte $00
LA40D	.byte $A0
LA40E	.byte $08
LA40F	.byte $A8
LA410	.byte $02
LA411	.byte $00
LA412	.byte $00
	.endl

	.proc START
	lda PORTB
	ora #$42
	sta PORTB
	and #$01
	bne LA436
	jsr LAA85
	jsr LAEF1
	lda LE000
	sta ICHIDZ
	ldx #$00
	stx LE000
	cmp #$FF
	beq LA436
	jmp LA45D
LA436	jsr LA6D6
	beq LA460
	jsr LAA85
	lda config.LA403
	bpl LA44C
	and #$7F
	jsr LA461
	sta ICHIDZ
	bne LA45D
LA44C	jsr LAEF1
	jsr LAFC1
	jsr LAC20
	sta ICHIDZ
	jsr LA467
	jsr LAFA9
LA45D	jmp LA74B
LA460	rts
	.endp

	.proc LA461
	tax
	dex
	lda LBD3F,X
	rts
	.endp

	.proc LA467
	bit config.LA401
	bpl LA475
	lda DSKFMS+1
	cmp #$05
	beq LA475
	jsr LADA1.LAE08
LA475	rts
	.endp

	.proc LA476
	stx BUFRFL
	sty RECVDN
	lda #$00
	sta XMTDON
	lda #$10
	sta CHKSNT
	ldy #$00
LA484	lda (BUFRFL),Y
	sta (XMTDON),Y
	iny
	bne LA484
	jmp L1000
	.endp

	.proc LA48E
	ldx #<la495
	ldy #>la495
	jmp LA476

la495	lda #$00
	sta NMIEN
	sta irqen

	sei
	sta WSYNC
	sta LD500
	jmp COLDSV
	.endp

	.proc unknown2
	lda PORTB
	ora #$02
	sta PORTB
	sei
	lda #$00
	sta NMIEN
	lda PORTB
	and #$FE
	sta PORTB
	sta WSYNC
	sta LD500
	ldx config.LA40B
	sta LD500,X
	lda #$00
	sta BUFRFL
	lda #$A0
	sta RECVDN
	lda #$00
	sta XMTDON
	lda #$C0
	sta CHKSNT
	ldx #$40
LA4DB	ldy #$00
LA4DD	lda (BUFRFL),Y
	sta (XMTDON),Y
	iny
	bne LA4DD
	inc RECVDN
	inc CHKSNT
	lda CHKSNT
	cmp #$D0
	bne LA501
	clc
	lda RECVDN
	adc #$08
	sta RECVDN
	sec
	txa
	sbc #$08
	tax
	clc
	lda CHKSNT
	adc #$08
	sta CHKSNT
LA501	cmp #$E0
	bne LA517
	sta LD500
	ldy config.LA40B
	iny
	sta LD500,Y
	lda #$00
	sta BUFRFL
	lda #$A0
	sta RECVDN
LA517	dex
	bne LA4DB
	sta LD500
	rts
	.endp

	.proc LA51E
	lda #$A0
	sta LC4AF
	rts
	.endp

	.proc LA524
	lda #$EA
	sta LC30B
	sta LC30C
	sta LC30D
	sta LC30E
	sta LC30F
	sta LC310
	sta LC311
	sta LC312
	sta LC313
	sta LC314
	sta LC315
	sta LC316
	sta LC317
	sta LC318
	sta LC319
	sta LC31A
	sta LC31B
	sta LC31C
	sta LC31D
	sta LC31E
	sta LC31F
	sta LC320
	sta LC321
	sta LC322
	sta LC323
	sta LC324
	sta LC325
	sta LC326
	sta LC487
	sta LC488
	sta LC489
	sta LC48A
	sta LC48B
	sta LC48C
	sta LC48D
	sta LC48E
	sta LC4A1
	sta LC4A2
	sta LC4A3
	sta LC4A4
	sta LC4A5
	sta LC4A6
	sta LC4A7
	sta LC4A8
	lda #$1D
	sta LC485
	lda #$C5
	sta LC486
	lda #$C0
	sta LC4AF
	lda #$60
	sta LC4B2
	lda #$EA
	sta LC410
	sta LC411
	sta LC412
	rts
	.endp

	.proc unknown1
	sei
	lda #$00
	sta NMIEN
	lda PORTB
	and #$FE
	ora #$02
	sta PORTB
	sta LD500
	lda #$00
	sta BUFRFL
	lda config.LA40D
	sta RECVDN
	lda #$00
	sta XMTDON
	lda #$D8
	sta CHKSNT
	ldx config.LA40E
	ldy config.LA40C
	sta LD500,Y
	ldy #$00
LA5F9	lda (BUFRFL),Y
	sta (XMTDON),Y
	iny
	bne LA5F9
	inc RECVDN
	inc CHKSNT
	dex
	bne LA5F9
	sta LD500
	lda #$00
	sta BUFRFL
	lda config.LA40F
	sta RECVDN
	lda #$00
	sta XMTDON
	lda #$C1
	sta CHKSNT
	ldx config.LA410
	ldy config.LA40C
	sta LD500,Y
	ldy #$00
LA626	lda (BUFRFL),Y
	sta (XMTDON),Y
	iny
	bne LA626
	inc RECVDN
	inc CHKSNT
	dex
	bne LA626
	sta LD500
	ldx config.LA40A
	sta LD500,X
	lda #$00
	sta BUFRFL
	lda #$A0
	sta RECVDN
	lda #$00
	sta XMTDON
	lda #$E0
	sta CHKSNT
	ldx #$20
	ldy #$00
LA651	lda (BUFRFL),Y
	sta (XMTDON),Y
	iny
	bne LA651
	inc RECVDN
	inc CHKSNT
	dex
	bne LA651
	sta LD500
	rts
	.endp

	.proc LA663
	lda PORTB
	and #$01
	bne LA66B
	rts
LA66B	sei
	lda #$00
	sta DSKFMS
	sta NMIEN
	sta WSYNC
	lda PORTB
	pha
	and #$FE
	sta PORTB
	lda LC000
	inc LC000
	cmp LC000
	beq LA68F
	dec LC000
	inc DSKFMS
LA68F	pla
	sta PORTB
	lda #$40
	sta NMIEN
	cli
	lda DSKFMS
	beq LA69E
	rts

LA69E	jsr LAEF1
	jsr LB034
	ldx #$00
	ldy #$02
	jsr LB0A4
	lda #<message
	ldy #>message
	jsr LB002
stop	jmp stop

	.local message
	.byte 'This selection requires 64k RAM!',$9B
	.endl

	.endp

	.proc LA6D6
	lda CONSOL
	bit config.LA402
	bpl LA6E0
	eor #$04
LA6E0	and #$04
	rts
	.endp

	.proc LA6E3
	ldy #$00
	ldx ICHIDZ
	dex
	txa
	sta XMTDON
	sty CHKSNT
	asl XMTDON
	rol CHKSNT
	asl XMTDON
	rol CHKSNT
	asl XMTDON
	rol CHKSNT
	clc
	lda #$3F
	adc XMTDON
	sta BUFRFL
	lda #$B9
	adc CHKSNT
	sta RECVDN
	lda (BUFRFL),Y
	rts
	.endp

	.proc LA709
	iny
	lda (BUFRFL),Y
	rts
	.endp

	.proc LA70D
	jsr LAAE0
	lda #$FF
	sta CH
	lda #$41
	sta ICAX4Z
	lda #$2D
	sta ICAX5Z
	sta SDLSTL
	sta DLISTL
	lda #$00
	sta ICAX6Z
	sta SDLSTH
	sta DLISTH
	lda #$E0
	sta CHBAS
	sta CHBASE
	sta WSYNC
loop	lda VCOUNT
	bne loop
	ldy #$00
	bit config.LA405
	bpl skip_blank
	sty SDMCTL
	sty DMACLT
skip_blank
	rts
	.endp

	.proc LA74B
	jsr LA70D
	jsr LA6E3
	sta ICDNOZ
	jsr LA709
	and #$F0
	sta BUFADR
	lda #$00
	sta BUFADR+1
	jsr LA709
	sta CHKSNT
	lda #$00
	sta XMTDON
	jsr LA709
	pha
	jsr LA709
	pha
	jsr LA709
	pha
	sta ICCOMZ
	jsr LA709
	sta ICSTAZ
	jsr LA709
	sta ICBALZ
	lda PORTB
	and #$01
	beq LA7BF
	bit ICSTAZ
	bpl LA7A3
	lda LF104
	cmp #$4D
	beq LA7A3
	jsr LA663
	ldx #$CA
	ldy #$A5
	jsr LA476
	lda ICHIDZ
	sta LE000
	jmp WARMSV

LA7A3	bit ICSTAZ
	bvc LA7BF
	jsr LA663
	ldx #$A7
	ldy #$A4
	jsr LA476
	lda ICHIDZ
	sta LE000
	jsr LA524
	jsr LA51E
	jmp LA48E
	.endp
	
	.proc LA7BF
	lda ICDNOZ
	cmp #$01
	beq LA7F8
	cmp #$02
	beq LA7E9
	cmp #$03
	beq LA7E6
	cmp #$04
	beq LA7EC
	cmp #$05
	beq LA7F2
	cmp #$06
	beq LA7EF
	cmp #$07
	beq LA7F5
LA7DD	lda SKREST
	sta COLBK
	jmp LA7DD
	.endp

LA7E6	jmp LA99C
LA7E9	jmp LA967
LA7EC	jmp LA893
LA7EF	jmp LA97E
LA7F2	jmp LA988
LA7F5	jmp LA992

	.proc LA7F8
	ldy #$06
	pla
	sta (BUFADR),Y
	ldy #$02
	pla
	sta (BUFADR),Y
	ldy #$03
	pla
	sta (BUFADR),Y
	ldy #$07
	lda #$00
	sta (BUFADR),Y
	ldy #$00
	lda ICBALZ
	and #$80
	lsr
	ora config.LA400
	sta (BUFADR),Y
	ldy #$01
	lda config.LA406
	sta (BUFADR),Y
	ldx #$BF
	ldy #$BD
	stx BUFRFL
	sty RECVDN
	ldy #$00
LA82A	lda (BUFRFL),Y
	bit config.LA400
	bpl LA834
	sta COLBK
LA834	sta (XMTDON),Y
	iny
	bne LA82A
	ldx #$BF
	ldy #$BE
	stx BUFRFL
	sty RECVDN
	ldx #$00
LA843	txa
	tay
	lda (BUFRFL),Y
	beq LA855
	tay
	lda (XMTDON),Y
	and #$0F
	ora BUFADR
	sta (XMTDON),Y
	inx
	bne LA843
LA855	ldx #$E9
	ldy #$BE
	stx BUFRFL
	sty RECVDN
	ldx #$00
LA85F	txa
	tay
	lda (BUFRFL),Y
	beq LA86D
	tay
	lda CHKSNT
	sta (XMTDON),Y
	inx
	bne LA85F
LA86D	lda #$01
	sta BOOT
	lda #$FF
	sta NMIEN
	tax
	txs
	cli
	lda ICSTAZ
	and #$20
	beq LA882
	jsr LB133
LA882	lda ICSTAZ
	and #$10
	beq LA890
	lda #$00
	sta SDMCTL
	sta DMACLT
LA890	jmp (XMTDON)
	.endp

	.proc LA893
	jsr LAEF1
	jsr LB034
	ldx #$00
	ldy #$02
	jsr LB0A4
	lda #$D4
	ldy #$A8
	jsr LB002
	ldx #$00
	ldy #$03
	jsr LB0A4
	lda #$F5
	ldy #$A8
	jsr LB002
	ldx #$00
	ldy #$05
	jsr LB0A4
	lda #$1B
	ldy #$A9
	jsr LB002
	ldx #$00
	ldy #$06
	jsr LB0A4
	lda #$42
	ldy #$A9
	jsr LB002
LA8D1	jmp LA8D1
	.endp
	
	.local unkown3
	.byte '16kB ROM dumps are not supported'
	.byte $9B
	.byte 'on Maxflash G1 cartridges or preview.'
	.byte $9B
	.byte 'Please try the cracked .XEX version of'
	.byte $9B
	.byte 'this title in your workbook instead.'
	.byte $9B
	.endl

	.proc LA967
	lda #$FF
	sta NMIEN
	tax
	txs
	cli
	ldx #$76
	ldy #$A9
	jmp LA476

	ldx ICCOMZ
	sta LD500,X
	jmp COLDSV
	.endp

	.proc LA97E
	lda ICCOMZ
	clc
	adc #$03
	sta ICCOMZ
	jmp LA967
	.endp

	.proc LA988
	lda ICCOMZ
	clc
	adc #$07
	sta ICCOMZ
	jmp LA967
	.endp

	.proc LA992
	lda ICCOMZ
	clc
	adc #$0F
	sta ICCOMZ
	jmp LA967
	.endp

	.proc LA99C
	jsr LA663
	lda #$00
	sta LE45A
	lda #$C1
	sta LE45B
	lda #$20
	sta LFFDC
	lda #$56
	sta LFFDD
	lda #$C2
	sta LFFDE
	lda config.LA406
	sta LC2D6
	lda config.LA400
	sta LC2D7
	pla
	sta LC2D1
	pla
	sta LC2D2
	pla
	sta LC2D3
	jsr LA6E3
	jsr LA709
	sta LC2D4
	cmp #$FF
	bne LA9EF
	lda LC2E6
	ora #$20
	sta LC2E6
	lda #$01
	sta LC2E0
	lda #$00
	sta LC2E1
LA9EF	jsr LA709
	tax
	and #$80
	sta LC2D8
	txa
	asl
	and #$80
	sta LC2D9
	lda #$00
	sta ABUFPT+2
	sta ABUFPT+3
LAA05	jsr LA6E3
	cmp #$03
	beq LAA12
LAA0C	inc ICHIDZ
	bpl LAA05
	bmi LAA3C
LAA12	ldx ABUFPT+2
	jsr LA709
	sta LC2F6,X
	inc ABUFPT+2
	ldx ABUFPT+3
	jsr LA709
	jsr LA709
	sta LC2EA,X
	inx
	jsr LA709
	sta LC2EA,X
	inx
	jsr LA709
	sta LC2EA,X
	inx
	stx ABUFPT+3
	cpx #$0C
	bne LAA0C
LAA3C	lda #$FF
	sta NMIEN
	tax
	txs
	cli
	lda config.LA406
	pha
	ldx #$4F
	ldy #$AA
	jmp LA476
	
	pla
	tax
	sta LD500,X
	lda #$A9
	sta LF257
	bit LC2D9
	bmi LAA61
	lda #$C0
	.byte $2c
	lda #$a0
	sta LF258
	lda #$85
	sta LF259
	lda #$06
	sta LF25A
	lda #$60
	sta LF25B
	bit LC2D9
	bpl LAA82
	lda PORTB
	and #$FD
	sta PORTB
LAA82	jmp COLDSV
	.endp

	.proc LAA85
	lda #$00
	sta COLDST
	sta WARMST
	lda #$C0
	sta RAMSIZ
	inc BASICF
	lda #$00
	sta PORTA
	lda #$3C
	sta PACTL
	sta PBCTL
	lda PORTA
	lda #$23
	sta SKSTAT
	lda #$A0
	sta POT5
	sta POT7
	lda #$28
	sta AUDCTL
	lda #$FF
	sta SEROUT
	lda #$C0
	sta POKMSK
	sta irqen
	lda #$7F
	sta NMIEN
	lda #$00
	sta AUDCTL
	lda #$A0
	sta CHBASE
	sta CHBAS
	lda #$02
	sta CHACTL
	sta CHACT
	jsr LAAE0
	rts
	.endp

	.proc LAAE0
	lda #$FF
	sta PCOLR0
	sta PCOLR1
	sta PCOLR2
	sta PCOLR3
	lda #$28
	sta COLOR0
	lda #$CA
	sta COLOR1
	lda #$94
	sta COLOR2
	lda #$46
	sta COLOR3
	rts
	.endp

LAB03	ldx #$00
	ldy #$17
	jsr LB0A4
	jsr LB074
	ldx #$00
	ldy #$17
	jsr LB0A4
	lda #$C7
	ldy #$AB
	jsr LB002
	lda #$00
	sta ICHIDZ
	lda ABUFPT+1
	jmp LAB27
LAB24	jsr get_key_LAE54
LAB27	lda DSKFMS+1
	cmp #$00
	beq LAB79
	cmp #$05
	bne LAB24
	lda ABUFPT+1
	cmp #$34
	beq LAB5E
	cmp #$1C
	beq LABB7
	ldx #$00
LAB3D	cmp LABEE,X
	beq LAB49
	inx
	cpx #$0A
	bne LAB3D
	beq LAB24
LAB49	ldy ICHIDZ
	cpy #$03
	beq LAB24
	lda LABF8,X
	ldx ICHIDZ
	sta ICSTAZ,X
	jsr LB0F8
	inc ICHIDZ
	jmp LAB24
LAB5E	ldy ICHIDZ
	beq LAB24
	dec ICHIDZ
	ldx BUFADR
	dex
	jsr LB0DA
	lda LAC02
	jsr LB0F8
	ldx BUFADR
	dex
	jsr LB0DA
	jmp LAB24
LAB79	ldx ICHIDZ
	beq LABB7
LAB7D	cpx #$03
	beq LAB87
	jsr LABB9
	inx
	bpl LAB7D
LAB87	lda #$00
	sta ICHIDZ
	lda ICSTAZ
	and #$0F
	beq LAB99
	cmp #$02
	bcs LABB7
	lda #$64
	sta ICHIDZ
LAB99	lda ICBALZ
	and #$0F
	tax
	lda LABE4,X
	clc
	adc ICHIDZ
	sta ICHIDZ
	lda ICBAHZ
	and #$0F
	clc
	adc ICHIDZ
	sta ICHIDZ
	beq LABB7
	cmp #$80
	bcs LABB7
	clc
	rts
LABB7	sec
	rts
LABB9	lda ICBALZ
	sta ICBAHZ
	lda ICSTAZ
	sta ICBALZ
	lda LABF8
	sta ICSTAZ
	rts

	.byte 'Enter Choice (ESC to Abort):'
	.byte $20
labe4	.byte $00,$0a,$14,$1e,$28,$32,$3c,$46,$50,$5a
labee	.byte $32,$1f,$1e,$1a,$18,$1d,$1b,$33,$35,$30
labf8	.byte '0123456789'
lac02	.byte ' '

	.proc lac03
	ldx #$00
	ldy #$00
	jsr lb0a4
	lda #$f5
	ldy #$be
	jsr lb002
	ldx #$00
	ldy #$17
	jsr lb0a4
	lda #$1e
	ldy #$bf
	jsr lb002
	rts
	.endp

	.proc lac20
	jsr lb034
	lda #$01
	sta dskutl
	lda #$00
	sta dskutl+1
	jsr lac03
lac2e	jsr lb054
	lda #$00
	sta dskfms
	lda #$70
	sta bufrfl
	lda #$b1
	sta recvdn
lac3d	lda dskutl+1
	cmp dskfms
	beq lac4a
	jsr lafe0
	inc dskfms
	bpl lac3d
lac4a	lda lb16f
	sec
	sbc dskutl+1
	cmp #$17
	bcc lac56
	lda #$16
lac56	sta abufpt
lac58	jsr lb002.lb006
	dec abufpt
	bne lac58
lac5f	ldy dskutl
	jsr lb0e0
	jsr lb085
	jsr get_key_lae54
	jsr lb098
	lda dskfms+1
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
	bne lac5f
	jsr lab03
	bcs lac94
	jsr la461
	cmp #$ff
	beq lac94
	rts

lac94	jsr lac03
	jmp lac5f
lac9a	beq lad0d
lac9c	lda dskutl+1
	beq lac5f
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
lacba	bne lac5f
lacbc	lda dskutl
	cmp #$01
	beq lacc6
	dec dskutl
	bpl lac5f
lacc6	lda dskutl+1
lacc8	beq lac5f
	dec dskutl+1
	jsr lada1
	jmp lac5f
lacd2	lda dskutl
	cmp #$16
	beq lad1a
	lda #$16
	cmp lb16f
	bcc lace2
	lda lb16f
lace2	sta dskutl
	bne lacba
lace6	lda dskutl
	cmp #$16
	beq lacfb
	clc
	lda dskutl+1
	adc dskutl
	cmp lb16f
lacf4	bcs lacf8
	inc dskutl
lacf8	jmp lac5f
lacfb	lda dskutl+1
	clc
	adc #$16
	cmp lb16f
	bcs lacf4
	inc dskutl+1
	jsr lad39
	jmp lac5f
lad0d	clc
	lda dskutl+1
	adc dskutl
	jsr la461
	cmp #$ff
	beq lacc8
	rts
lad1a	lda dskutl+1
	clc
	adc #$2c
	cmp lb16f
	bcs lad2d
	lda dskutl+1
	adc #$16
	sta dskutl+1
	jmp lac2e
lad2d	sec
	lda lb16f
	sbc #$16
	sta dskutl+1
	jmp lac2e
	rts
	.endp

	.proc lad39
	clc
	lda dl.lms_lb151
	adc #$28
	sta xmtdon
	lda lb152
	adc #$00
	sta chksnt
	clc
	lda dl.lms_lb151
	adc #$50
	sta bufrfl
	lda lb152
	adc #$00
	sta recvdn
	ldx #$14
lad59	ldy #$27
lad5b	lda (bufrfl),y
	sta (xmtdon),y
	dey
	bpl lad5b
	clc
	lda bufrfl
	adc #$28
	sta bufrfl
	lda recvdn
	adc #$00
	sta recvdn
	clc
	lda xmtdon
	adc #$28
	sta xmtdon
	lda chksnt
	adc #$00
	sta chksnt
	dex
	bpl lad59
	lda #$70
	sta bufrfl
	lda #$b1
	sta recvdn
	clc
	lda #$14
	adc dskutl+1
	sta dskfms
lad8e	jsr lafe0
	dec dskfms
	bpl lad8e
	ldy dskutl
	jsr lb0e0
	jsr lb074
	jsr lb002.lb006
	rts
	.endp

	.proc lada1
	clc
	lda dl.lms_lb151
	adc #$70
	sta xmtdon
	lda lb152
	adc #$03
	sta chksnt
	clc
	lda dl.lms_lb151
	adc #$48
	sta bufrfl
	lda lb152
	adc #$03
	sta recvdn
	ldx #$14
ladc1	ldy #$27
ladc3	lda (bufrfl),y
	sta (xmtdon),y
	dey
	bpl ladc3
	sec
	lda bufrfl
	sbc #$28
	sta bufrfl
	lda recvdn
	sbc #$00
	sta recvdn
	sec
	lda xmtdon
	sbc #$28
	sta xmtdon
	lda chksnt
	sbc #$00
	sta chksnt
	dex
	bpl ladc1
	lda #$70
	sta bufrfl
	lda #$b1
	sta recvdn
	lda dskutl+1
	beq ladfc
	sta dskfms
ladf5	jsr lafe0
	dec dskfms
	bne ladf5
ladfc	ldy dskutl
	jsr lb0e0
	jsr lb074
	jsr lb002.lb006
	rts
	
	.proc lae08
	lda #$04
	sta dskfms
lae0c	jsr lb085
	jsr delay_lae39
	jsr lb098
	jsr delay_lae39
	dec dskfms
	bne lae0c
	rts
	.endp

	.proc lae1d
	sta pot0
	ldx #$e0
lae22	stx pot1
	jsr delay_lae44
	inx
	cpx #$e9
	bne lae22
lae2d	stx pot1
	jsr delay_lae44
	dex
	cpx #$df
	bne lae2d
	rts
	.endp

	.proc delay_lae39
	ldx #$0f
lae3b	ldy #$ff
lae3d	dey
	bne lae3d
	dex
	bne lae3b
	rts
	.endp
	
	.proc delay_lae44
	pha
	txa
	pha
	tya
	pha
	ldx #$01
	jsr delay_lae39.lae3b
	pla
	tay
	pla
	tax
	pla
	rts
	.endp
	.endp

	.proc get_key_lae54
	lda #$ff
	sta dskfms+1
	sta ch
	bit config.la404
	bpl lae63
	ldx #$45
	bit l75a2
	tay
lae66	dey
	bne lae66
	dex
	bne lae66
lae6c	lda #$01
	bit config.la412
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
	bit config.la412
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
	sta dskfms+1
	bit config.la401
	bpl laef0
	lda #$08
	jsr lada1.lae1d
laef0	rts
	.endp

laef1	lda #$00
	sta xmtdon
	lda #$1c
	sta chksnt
	lda #$4d
	sta bufrfl
	lda #$b1
	sta recvdn
	ldy #$21
laf03	lda (bufrfl),y
	sta (xmtdon),y
	dey
	bpl laf03
	lda dl.lb16d
	sta sdlstl
	sta dlistl
	lda dl.lb16d+1
	sta sdlsth
	sta dlisth
	lda #$22
	sta sdmctl
	sta dmaclt
	lda #$a0
	sta chbas
	sta chbase
	jsr laae0
	lda #$02
	sta chact
	sta chactl
	rts

	.proc dli_afxx
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
	ldy config.la407,x
	inc abufpt+2
	lda abufpt+2
	cmp #$01
	beq laf73
	cmp #$02
	beq laf9d
	bit config.la411
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
laf73	bit config.la411
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

	.proc lafa9
	bit config.la401
	bpl lafc1.lafdf
	lda #$7f
	sta nmien
	sta wsync
	lda dosini
	sta vdslst
	lda dosini+1
	sta vdslst+1
	rts
	.endp

	.proc lafc1
	bit config.la401
	bpl lafdf
	lda vdslst
	sta dosini
	lda vdslst+1
	sta dosini+1
	ldx #$38
	ldy #$af
	stx vdslst
	sty vdslst+1
	lda #$ff
	sta nmien
lafdf	rts
	.endp

lafe0	lda #$00
	sta iccomt
lafe4	ldy #$00
	lda (bufrfl),y
	beq lafee
	cmp #$9b
	bne laff0
lafee	dec iccomt
laff0	clc
	lda bufrfl
	adc #$01
	sta bufrfl
	lda recvdn
	adc #$00
	sta recvdn
	bit iccomt
	bpl lafe4
	rts

	.proc lb002
	sta bufrfl
	sty recvdn
lb006	lda #$00
	sta iccomt
lb00a	ldy #$00
	lda (bufrfl),y
	beq lb01b
	cmp #$9b
	bne lb01f
	ldx #$00
	ldy bufadr+1
	jsr lb128
lb01b	dec iccomt
	bmi lb022
lb01f	jsr lb0f8
lb022	clc
	lda bufrfl
	adc #$01
	sta bufrfl
	lda recvdn
	adc #$00
	sta recvdn
	bit iccomt
	bpl lb00a
	rts
	.endp

	.proc lb034
	lda #$00
	sta dskfms
lb038	ldx #$00
	ldy dskfms
	jsr lb0a4
	jsr lb074
	lda dskfms
	cmp #$17
	beq lb04c
	inc dskfms
	bpl lb038
lb04c	ldx #$00
	ldy #$00
	jsr lb0a4
	rts
	.endp

	.proc lb054
	lda #$01
	sta dskfms
lb058	ldx #$00
	ldy dskfms
	jsr lb0a4
	jsr lb074
	lda dskfms
	cmp #$16
	beq lb06c
	inc dskfms
	bpl lb058
lb06c	ldx #$00
	ldy #$01
	jsr lb0a4
	rts
	.endp
	
	.proc lb074
	ldx #$00
	ldy bufadr+1
	jsr lb0a4
	lda #$00
	ldy #$27
lb07f	sta (appmhi),y
	dey
	bpl lb07f
	rts
	.endp

	.proc lb085
	ldx #$00
	ldy bufadr+1
	jsr lb0a4
	ldy #$27
lb08e	lda (appmhi),y
	ora #$80
	sta (appmhi),y
	dey
	bpl lb08e
	rts
	.endp

	.proc lb098
	ldy #$27
loop	lda (appmhi),y
	and #$7f
	sta (appmhi),y
	dey
	bpl loop
	rts
	.endp

	.proc lb0a4
	stx bufadr
	sty bufadr+1
	jsr lb0ef
	lda dl.lms_lb151
	sta appmhi
	lda lb152
	sta appmhi+1
	ldy bufadr+1
	beq lb0c9
lb0b9	clc
	lda appmhi
	adc #$28
	sta appmhi
	lda appmhi+1
	adc #$00
	sta appmhi+1
	dey
	bne lb0b9
lb0c9	clc
	lda appmhi
	adc bufadr
	sta appmhi
	lda appmhi+1
	adc #$00
	sta appmhi+1
	jsr lb0e6
	rts
	.endp

	.proc lb0da
	ldy bufadr+1
	jsr lb0a4
	rts
	.endp

	.proc lb0e0
	ldx bufadr
	jsr lb0a4
	rts
	.endp

	.proc lb0e6
	ldy #$00
	lda (appmhi),y
	ora #$80
	sta (appmhi),y
	rts
	.endp

	.proc lb0ef
	ldy #$00
	lda (appmhi),y
	and #$7f
	sta (appmhi),y
	rts
	.endp

	.proc lb0f8
	pha
	jsr lb0ef
	pla
	jsr lb10b
	ldy #$00
	sta (appmhi),y
	jsr lb11d
	jsr lb0e6
	rts

lb10b	cmp #$20
	bcs lb115
	sec
	sbc #$40
	jmp lb11c
lb115	cmp #$5f
	bcs lb11c
	sec
	sbc #$20
lb11c	rts
	.endp

lb11d	ldy bufadr+1
	ldx bufadr
	inx
	cpx #$28
	bne lb12f
	ldx #$00
lb128	iny
	cpy #$17
	bcc lb12f
	ldy #$01
lb12f	jsr lb0a4
	rts
lb133	lda #$03
	ldx #$00
	sta iocb0+iccom,x
	lda #$48
	sta iocb0+icbal,x
	lda #$c4
	sta iocb0+icbah,x
	lda #$0c
	sta iocb0+icax1,x
	jsr ciov
	rts
	
	.local dl
	.byte aempty8
	.byte aempty8
	.byte adli+aempty8
	.byte alms+$02
lms_lb151	.word l1c22
	.byte adli+aempty1
	.byte $02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02
	.byte $02,$02,$02,$02,$02,$02
	.byte ADLI+AEMPTY1
	.byte $02
	.byte AVB+AJMP
lb16d	.word l1c00
	.endl

lb16f	.byte '<  1> A'
	.byte $9B
	.byte '<  2> B'
	.byte $9B
	.byte '<  3> C'
	.byte $9B
	.byte '<  4> '
	.byte $9B
	.byte '<  5> '
	.byte $9B
	.byte '<  6> '
	.byte $9B
	.byte '<  7> '
	.byte $9B
	.byte '<  8> '
	.byte $9B
	.byte '<  9> '
	.byte $9B
	.byte '< 10> '
	.byte $9B
	.byte '< 11> '
	.byte $9B
	.byte '< 12> '
	.byte $9B
	.byte '< 13> '
	.byte $9B
	.byte '< 14> '
	.byte $9B
	.byte '< 15> '
	.byte $9B
	.byte '< 16> '
	.byte $9B
	.byte '< 17> '
	.byte $9B
	.byte '< 18> '
	.byte $9B
	.byte '< 19> '
	.byte $9B
	.byte '< 20> '
	.byte $9B
	.byte '< 21> '
	.byte $9B
	.byte '< 22> '
	.byte $9B
	.byte '< 23> '
	.byte $9B
	.byte '< 24> '
	.byte $9B
	.byte '< 25> '
	.byte $9B
	.byte '< 26> '
	.byte $9B
	.byte '< 27> '
	.byte $9B
	.byte '< 28> '
	.byte $9B
	.byte '< 29> '
	.byte $9B
	.byte '< 30> '
	.byte $9B
	.byte '< 31> '
	.byte $9B
	.byte '< 32> '
	.byte $9B
	.byte '< 33> '
	.byte $9B
	.byte '< 34> '
	.byte $9B
	.byte '< 35> '
	.byte $9B
	.byte '< 36> '
	.byte $9B
	.byte '< 37> '
	.byte $9B
	.byte '< 38> '
	.byte $9B
	.byte '< 39> '
	.byte $9B
	.byte '< 40> '
	.byte $9B
	.byte '< 41> '
	.byte $9B
	.byte '< 42> '
	.byte $9B
	.byte '< 43> '
	.byte $9B
	.byte '< 44> '
	.byte $9B
	.byte '< 45> '
	.byte $9B
	.byte '< 46> '
	.byte $9B
	.byte '< 47> '
	.byte $9B
	.byte '< 48> '
	.byte $9B
	.byte '< 49> '
	.byte $9B
	.byte '< 50> '
	.byte $9B
	.byte '< 51> '
	.byte $9B
	.byte '< 52> '
	.byte $9B
	.byte '< 53> '
	.byte $9B
	.byte '< 54> '
	.byte $9B
	.byte '< 55> '
	.byte $9B
	.byte '< 56> '
	.byte $9B
	.byte '< 57> '
	.byte $9B
	.byte '< 58> '
	.byte $9B
	.byte '< 59> '
	.byte $9B
	.byte '< 60> '
	.byte $9B
	.byte '< 61> '
	.byte $9B
	.byte '< 62> '
	.byte $9B
	.byte '< 63> '
	.byte $9B
	.byte '< 64> '
	.byte $9B
	.byte '< 65> '
	.byte $9B
	.byte '< 66> '
	.byte $9B
	.byte '< 67> '
	.byte $9B
	.byte '< 68> '
	.byte $9B
	.byte '< 69> '
	.byte $9B
	.byte '< 70> '
	.byte $9B
	.byte '< 71> '
	.byte $9B
	.byte '< 72> '
	.byte $9B
	.byte '< 73> '
	.byte $9B
	.byte '< 74> '
	.byte $9B
	.byte '< 75> '
	.byte $9B
	.byte '< 76> '
	.byte $9B
	.byte '< 77> '
	.byte $9B
	.byte '< 78> '
	.byte $9B
	.byte '< 79> '
	.byte $9B
	.byte '< 80> '
	.byte $9B
	.byte '< 81> '
	.byte $9B
	.byte '< 82> '
	.byte $9B
	.byte '< 83> '
	.byte $9B
	.byte '< 84> '
	.byte $9B
	.byte '< 85> '
	.byte $9B
	.byte '< 86> '
	.byte $9B
	.byte '< 87> '
	.byte $9B
	.byte '< 88> '
	.byte $9B
	.byte '< 89> '
	.byte $9B
	.byte '< 90> '
	.byte $9B
	.byte '< 91> '
	.byte $9B
	.byte '< 92> '
	.byte $9B
	.byte '< 93> '
	.byte $9B
	.byte '< 94> '
	.byte $9B
	.byte '< 95> '
	.byte $9B
	.byte '< 96> '
	.byte $9B
	.byte '< 97> '
	.byte $9B
	.byte '< 98> '
	.byte $9B
	.byte '< 99> '
	.byte $9B
	.byte '<100> '
	.byte $9B
	.byte '<101> '
	.byte $9B
	.byte '<102> '
	.byte $9B
	.byte '<103> '
	.byte $9B
	.byte '<104> '
	.byte $9B
	.byte '<105> '
	.byte $9B
	.byte '<106> '
	.byte $9B
	.byte '<107> '
	.byte $9B
	.byte '<108> '
	.byte $9B
	.byte '<109> '
	.byte $9B
	.byte '<110> '
	.byte $9B
	.byte '<111> '
	.byte $9B
	.byte '<112> '
	.byte $9B
	.byte '<113> '
	.byte $9B
	.byte '<114> '
	.byte $9B
	.byte '<115> '
	.byte $9B
	.byte '<116> '
	.byte $9B
	.byte '<117> '
	.byte $9B
	.byte '<118> '
	.byte $9B
	.byte '<119> '
	.byte $9B
	.byte '<120> '
	.byte $9B
	.byte '<121> '
	.byte $9B
	.byte '<122> '
	.byte $9B
	.byte '<123> '
	.byte $9B
	.byte '<124> '
	.byte $9B
	.byte '<125> '
	.byte $9B
	.byte '<126> '
	.byte $9B
	.byte '<127> 127 Last Entry'
	.byte $9B
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF

	.local LBD3F
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.byte $FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FF
	.endl

	.proc LBDBF
	dec CMCMD
	jsr L019D
	sta RAMLO
	jsr L019D
	sta RAMLO+1
	jsr L019D
	sta BOOT
	jsr L019D
	sta WARMST
	inc CMCMD
	lda #$00
	ora RAMLO
	ora RAMLO+1
	ora BOOT
	ora WARMST
	beq LBE03
	lda #$00
	sta INITAD
	sta INITAD+1
LBDEB	jsr L0162
	lda BOOT
	ora WARMST
	bne LBDEB
	lda INITAD
	ora INITAD+1
	beq LBDFF
	jsr L015F
LBDFF	bne LBDBF
	beq LBDBF
LBE03	bit $00
	bvc LBE1B
	lda RUNAD
	sta.w DOSINI
	lda RUNAD+1
	sta.w DOSINI+1
	lda #$01
	sta.w BOOT
	jmp WARMSV
LBE1B	jmp (RUNAD)
	jmp (INITAD)
	lda RAMLO+1
	cmp #$9F
	bcs LBE47
	lda CASINI+1
	cmp #$BF
	bcs LBE47
	lda WARMST
	beq LBE47
	jsr L01D9
	ldy #$00
LBE36	lda (CASINI),Y
	sta (RAMLO),Y
	iny
	bne LBE36
	inc CASINI+1
	inc RAMLO+1
	dec WARMST
	jsr L01E1
	rts
	.endp

	.proc LBE47
	jsr L019D
	ldy #$00
	sta (RAMLO),Y
	clc
	lda RAMLO
	adc #$01
	sta RAMLO
	lda RAMLO+1
	adc #$00
	sta RAMLO+1
	rts
	jsr L01D9
	ldy #$00
	lda (CASINI),Y
	pha
	clc
	lda CASINI
	adc #$01
	sta CASINI
	lda CASINI+1
	adc #$00
	sta CASINI+1
	cmp #$C0
	bcc LBE7B
	inc TRAMSZ
	lda #$A0
	sta CASINI+1
LBE7B	bit CMCMD
	bmi LBE8C
	sec
	lda BOOT
	sbc #$01
	sta BOOT
	lda WARMST
	sbc #$00
	sta WARMST
LBE8C	jsr L01E1
	pla
	bit $00
	bpl LBE97
	sta COLBK
LBE97	rts
	ldx TRAMSZ
	inc CRITIC
	sta LD500,X
	rts
	ldx NGFLAG
	sta LD500,X
	lda TRIG3
	sta GINTLK
	dec CRITIC
	rts
	.endp

	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $FF,$01,$06,$0B,$10,$15,$17,$1B,$1D,$1F,$21,$30,$32,$45,$63,$69
	.byte $6F,$78,$7A,$7F,$81,$83,$8E,$91,$95,$97,$9B,$A3,$A7,$AB,$AD,$B1
	.byte $B7,$BB,$BD,$C2,$C6,$C8,$CC,$D2,$DA,$E2,$00,$04,$09,$0E,$13,$2E
	.byte $3F,$74,$86,$8A,$9F,$CF,$00,$3E
	.byte '   Atarimax Maxflash MultiCart Menu   <'
	.byte $00
	.byte '>  Use Keyboard or Joystick to Select  <'
	.byte $00
	.byte 'Maxflash Menu Software, Copyright 2009 Steven J Tucker'
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
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
  
