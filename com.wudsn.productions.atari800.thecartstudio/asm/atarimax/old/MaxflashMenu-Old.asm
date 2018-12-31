;	@com.wudsn.ide.asm.outputfileextension=.rom

	opt h-

l3ff7	= lbff7-$8000

	ins "Maxflash-Example.rom",+0,127*$2000

;	.word $ffff, $a000, $bfff
	icl "MaxflashMenu-SystemEquates.asm"

;
; code equates
;
l0084	= $0084
l0085	= $0085
l0086	= $0086
l0087	= $0087
l0088	= $0088
l0089	= $0089
l008a	= $008a
l008b	= $008b
l008c	= $008c
l008d	= $008d
l00d4	= $00d4
l00d5	= $00d5
l00d6	= $00d6
l00d9	= $00d9
l00da	= $00da
l00df	= $00df
l00e0	= $00e0
l00e1	= $00e1
l00e2	= $00e2
l00e6	= $00e6
l00ec	= $00ec
l00ed	= $00ed
l00ee	= $00ee
l00ef	= $00ef
l00f0	= $00f0
l00f1	= $00f1
l00f2	= $00f2
l00f3	= $00f3
l00f4	= $00f4
l00f5	= $00f5
l00f6	= $00f6
l00f7	= $00f7
l00f8	= $00f8
l00f9	= $00f9
l00fa	= $00fa
l00fb	= $00fb
l00fc	= $00fc
l00fd	= $00fd
l00fe	= $00fe
l00ff	= $00ff
l0100	= $0100
l0118	= $0118
l0119	= $0119
l011a	= $011a
l0159	= $0159
l015b	= $015b
l015c	= $015c
l0180	= $0180
l0194	= $0194
l01e7	= $01e7
l057f	= $057f
l0580	= $0580
l0581	= $0581
l0582	= $0582
l1000	= $1000
l1028	= $1028
l1050	= $1050
l1078	= $1078
l10a0	= $10a0
l10c8	= $10c8
l10f0	= $10f0
l1118	= $1118
l1140	= $1140
l1168	= $1168
l1190	= $1190
l11b8	= $11b8
l11e0	= $11e0
l1208	= $1208
l1230	= $1230
l1258	= $1258
l1280	= $1280
l12a8	= $12a8
l12d0	= $12d0
l12f8	= $12f8
l1320	= $1320
l1370	= $1370
l1398	= $1398
l1399	= $1399
l139a	= $139a
l139b	= $139b
l139c	= $139c
l139d	= $139d
l139e	= $139e
selected_entry_l139f	= $139f
menu_ptr_l13a0	= $13a0
l13a2	= $13a2
l13a3	= $13a3
l13a4	= $13a4
l13a5	= $13a5
l13a6	= $13a6
l13a7	= $13a7
l13a8	= $13a8
selected_max_line_l13a9	= $13a9

l3ff8	= $3ff8

la000	= $a000
lbff0	= $bff0

	.enum os
lc180	= $c180
lc1c1	= $c1c1
lc1c2	= $c1c2
lc1c6	= $c1c6
lc1ca	= $c1ca
lc313	= $c313
lc314	= $c314
lc315	= $c315
lc319	= $c319
lc31a	= $c31a
lc322	= $c322
lc323	= $c323
lc327	= $c327
lc328	= $c328
lc410	= $c410
lc411	= $c411
lc412	= $c412
lc413	= $c413
lc414	= $c414
lc41a	= $c41a
lc41b	= $c41b
lc41e	= $c41e
lc41f	= $c41f
lc420	= $c420
lc487	= $c487
lc488	= $c488
lc489	= $c489
lc48a	= $c48a
lc48b	= $c48b
lc48c	= $c48c
lc48d	= $c48d
lc48e	= $c48e
lc495	= $c495
lc496	= $c496
lc497	= $c497
lc498	= $c498
lc499	= $c499
lc49a	= $c49a
lc49b	= $c49b
lc49c	= $c49c
lc4a1	= $c4a1
lc4a2	= $c4a2
lc4a3	= $c4a3
lc4a4	= $c4a4
lc4a5	= $c4a5
lc4a6	= $c4a6
lc4a7	= $c4a7
lc4a8	= $c4a8
lc4af	= $c4af
lc4b0	= $c4b0
lc4b1	= $c4b1
lc4b2	= $c4b2
lc4b3	= $c4b3
lc4b4	= $c4b4
lc4b5	= $c4b5
lc4b6	= $c4b6
lc4bd	= $c4bd
lc4c0	= $c4c0
lc4ed	= $c4ed
lc4ee	= $c4ee
lc4ef	= $c4ef
lc4f9	= $c4f9
lc4fa	= $c4fa
lc4fb	= $c4fb
lc4fc	= $c4fc
lc4fd	= $c4fd
lc507	= $c507
lc508	= $c508
lc509	= $c509
lc50a	= $c50a
lc901	= $c901
lc902	= $c902
lc903	= $c903
lc904	= $c904
lc905	= $c905
lc90f	= $c90f
lc910	= $c910
lc911	= $c911
lc912	= $c912
lc913	= $c913
lcc80	= $cc80
lccc1	= $ccc1
lccc2	= $ccc2
lccc6	= $ccc6
lccca	= $ccca
le959	= $e959
lf104	= $f104
lf114	= $f114
lf115	= $f115
lf116	= $f116
lf117	= $f117
lf257	= $f257
lf258	= $f258
lf259	= $f259
lf25a	= $f25a
lf25b	= $f25b
	.ende
	
ld500	= $d500
ld510	= $d510
ld580	= $d580
ld818	= $d818
ld988	= $d988
ld99c	= $d99c
lda48	= $da48
lda51	= $da51
lda5a	= $da5a
ldaf7	= $daf7
ldb09	= $db09
ldb1a	= $db1a
ldb4e	= $db4e
ldb94	= $db94
ldb9d	= $db9d
ldba1	= $dba1
ldbaf	= $dbaf
ldbbb	= $dbbb
ldbe7	= $dbe7
ldbeb	= $dbeb
ldc00	= $dc00
ldc04	= $dc04
ldc3a	= $dc3a
ldc3e	= $dc3e
ldc62	= $dc62
ldc70	= $dc70
ldc93	= $dc93
ldc9d	= $dc9d
ldc9f	= $dc9f
ldca4	= $dca4
ldcb9	= $dcb9
ldcc1	= $dcc1
ldccf	= $dccf
ldce0	= $dce0
ldd01	= $dd01
ldd05	= $dd05
ldd09	= $dd09
ldd0f	= $dd0f
ldd28	= $dd28
ldd34	= $dd34
lde95	= $de95

;
; start of code
;
	org $2000
;
	.proc l2000
	jsr ldba1
	jsr ldbbb
	bcs l2041
	ldx #$ed
	ldy #$04
	jsr lda48
	ldx #$ff
	stx l00f1
	jsr zfr0
	beq l201c
l2018	lda #$ff
	sta l00f0
l201c	jsr ldb94
	bcs l2042
	pha
	ldx l00d5
	bne l2037
	jsr ldbeb
	pla
	ora l00d9
	sta l00d9
	ldx l00f1
	bmi l2018
	inx
	stx l00f1
	bne l2018
l2037	pla
	ldx l00f1
	bpl l203e
	inc l00ed
l203e	jmp ld818
l2041	rts
	.endp

	.proc l2042
	cmp #$2e
	beq l205a
	cmp #$45
	beq l2063
	ldx l00f0
	bne l20b6
	cmp #$2b
	beq l2000.l2018
	cmp #$2d
	beq l2056
l2056	sta l00ee
	beq l2000.l2018
l205a	ldx l00f1
	bpl l20b6
	inx
	stx l00f1
	beq l2000.l2018
l2063	lda l00f2
	sta l00ec
	jsr ldb94
	bcs l20a3
l206c	tax
	lda l00ed
	pha
	stx l00ed
	jsr ldb94
	bcs l208e
	pha
	lda l00ed
	asl
	sta l00ed
	asl
	asl
	adc l00ed
	sta l00ed
	pla
	clc
	adc l00ed
	sta l00ed
	ldy l00f2
	jsr ldb9d
l208e	lda l00ef
	beq l209b
	lda l00ed
	eor #$ff
	clc
	adc #$01
	sta l00ed
l209b	pla
	clc
	adc l00ed
	sta l00ed
	bne l20b6
l20a3	cmp #$2b
	beq l20ad
	cmp #$2d
	bne l20b2
	sta l00ef
l20ad	jsr ldb94
	bcc l206c
l20b2	lda l00ec
	sta l00f2
l20b6	dec l00f2
	lda l00ed
	ldx l00f1
	bmi l20c3
	beq l20c3
	sec
	sbc l00f1
l20c3	pha
	rol
	pla
	ror
	sta l00ed
	bcc l20ce
	jsr ldbeb
l20ce	lda l00ed
	clc
	adc #$44
	sta l00d4
	jsr ldc00
	bcs l20e5
	ldx l00ee
	beq l20e4
	lda l00d4
	ora #$80
	sta l00d4
l20e4	clc
l20e5	rts
	.endp

	.proc unknown_20e6
	jsr lda51
	lda #$30
	sta l057f
	lda l00d4
	beq l211a
	and #$7f
	cmp #$3f
	bcc l2120
	cmp #$45
	bcs l2120
	sec
	sbc #$3f
	jsr ldc70
	jsr ldca4
	ora #$80
	sta l0580,x
	lda l0580
	cmp #$2e
	beq l2114
	jmp ld988
l2114	jsr ldcc1
	jmp ld99c
l211a	lda #$b0
	sta l0580
	rts
	.endp

	.proc l2120
	lda #$01
	jsr ldc70
	jsr ldca4
	inx
	stx l00f2
	lda l00d4
	asl
	sec
	sbc #$80
	ldx l0580
	cpx #$30
	beq l214f
	ldx l0581
	ldy l0582
	stx l0582
	sty l0581
	ldx l00f2
	cpx #$02
	bne l214c
	inc l00f2
l214c	clc
	adc #$01
l214f	sta l00ed
	lda #$45
	ldy l00f2
	jsr ldc9f
	sty l00f2
	lda l00ed
	bpl l2169
	lda #$00
	sec
	sbc l00ed
	sta l00ed
	lda #$2d
	bne l216b
l2169	lda #$2b
l216b	jsr ldc9f
	ldx #$00
	lda l00ed
l2172	sec
	sbc #$0a
	bcc l217a
	inx
	bne l2172
l217a	clc
	adc #$0a
	pha
	txa
	jsr ldc9d
	pla
	ora #$80
	jsr ldc9d
	lda l0580
	cmp #$30
	bne l219c
	clc
	lda l00f3
	adc #$01
	sta l00f3
	lda l00f4
	adc #$00
	sta l00f4
l219c	lda l00d4
	bpl l21a9
	jsr ldcc1
	ldy #$00
	lda #$2d
	sta (l00f3),y
l21a9	rts
	.endp

	.proc unknown_l21aa
	lda l00d4
	sta l00f8
	lda l00d5
	sta l00f7
	jsr zfr0
	sed
	ldy #$10
l21b8	asl l00f8
	rol l00f7
	ldx #$03
l21be	lda l00d4,x
	adc l00d4,x
	sta l00d4,x
	dex
	bne l21be
	dey
	bne l21b8
	cld
	lda #$42
	sta l00d4
	jmp ldc00
	lda #$00
	sta l00f7
	sta l00f8
	lda l00d4
	bmi l2242
	cmp #$43
	bcs l2242
	sec
	sbc #$40
	bcc l2224
	adc #$00
	asl
	sta l00f5
l21ea	jsr lda5a
	bcs l2242
	lda l00f7
	sta l00f9
	lda l00f8
	sta l00fa
	jsr lda5a
	bcs l2242
	jsr lda5a
	bcs l2242
	clc
	lda l00f8
	adc l00fa
	sta l00f8
	lda l00f7
	adc l00f9
	sta l00f7
	bcs l2242
	jsr ldcb9
	clc
	adc l00f8
	sta l00f8
	lda l00f7
	adc #$00
	bcs l2242
	sta l00f7
	dec l00f5
	bne l21ea
l2224	jsr ldcb9
	cmp #$05
	bcc l2238
	clc
	lda l00f8
	adc #$01
	sta l00f8
	lda l00f7
	adc #$00
	sta l00f7
l2238	lda l00f8
	sta l00d4
	lda l00f7
	sta l00d5
	clc
	rts
l2242	sec
	rts
	ldx #$d4
	ldy #$06
	lda #$00
l224a	sta $00,x
	inx
	dey
	bne l224a
	rts
	lda #$05
	sta l00f4
	lda #$80
	sta l00f3
	rts
	clc
	rol l00f8
	rol l00f7
	rts
	lda l00e0
	eor #$80
	sta l00e0
l2266	lda l00e0
	and #$7f
	sta l00f7
	lda l00d4
	and #$7f
	sec
	sbc l00f7
	bpl l2285
	ldx #$05
l2277	lda l00d4,x
	ldy l00e0,x
	sta l00e0,x
	tya
	sta l00d4,x
	dex
	bpl l2277
	bmi l2266
l2285	beq l228e
	cmp #$05
	bcs l22a4
	jsr ldc3e
l228e	sed
	lda l00d4
	eor l00e0
	bmi l22b3
	ldx #$04
	clc
l2298	lda l00d5,x
	adc l00e1,x
	sta l00d5,x
	dex
	bpl l2298
	cld
	bcs l22a7
l22a4	jmp ldc00
l22a7	lda #$01
	jsr ldc3a
	lda #$01
	sta l00d5
	jmp ldc00
l22b3	ldx #$04
	sec
l22b6	lda l00d5,x
	sbc l00e1,x
	sta l00d5,x
	dex
	bpl l22b6
	bcc l22c5
	cld
	jmp ldc00
l22c5	lda l00d4
	eor #$80
	sta l00d4
	sec
	ldx #$04
l22ce	lda #$00
	sbc l00d5,x
	sta l00d5,x
	dex
	bpl l22ce
	cld
	jmp ldc00
	lda l00d4
	beq l2324
	lda l00e0
	beq l2321
	jsr ldccf
	sec
	sbc #$40
	sec
	adc l00e0
	bmi l2326
	jsr ldce0
l22f1	lda l00df
	and #$0f
	sta l00f6
	dec l00f6
	bmi l2301
	jsr ldd01
	jmp ldaf7
l2301	lda l00df
	lsr
	lsr
	lsr
	lsr
	sta l00f6
	dec l00f6
	bmi l2313
	jsr ldd05
	jmp ldb09
l2313	jsr ldc62
	dec l00f5
	bne l22f1
	lda l00ed
	sta l00d4
	jmp ldc04
l2321	jsr zfr0
l2324	clc
	rts
l2326	sec
	rts

	lda l00e0
	beq l2326
	lda l00d4
	beq l2324
	jsr ldccf
	sec
	sbc l00e0
	clc
	adc #$40
	bmi l2326
	jsr ldce0
	inc l00f5
	jmp ldb4e
	.endp

	.proc l2343
	ldx #$00
l2345	lda l00d5,x
	sta l00d4,x
	inx
	cpx #$0c
	bne l2345
l234e	ldy #$05
	sec
	sed
l2352	lda l00da,y
	sbc l00e6,y
	sta l00da,y
	dey
	bpl l2352
	cld
	bcc l2365
	inc l00d9
	bne l234e
l2365	jsr ldd0f
	asl l00d9
	asl l00d9
	asl l00d9
	asl l00d9
l2370	ldy #$05
	sec
	sed
l2374	lda l00da,y
	sbc l00e0,y
	sta l00da,y
	dey
	bpl l2374
	cld
	bcc l2387
	inc l00d9
	bne l2370
l2387	jsr ldd09
	dec l00f5
	bne l2343
	jsr ldc62
	jmp ldb1a
	
	jsr ldbaf
	ldy l00f2
	bcc l239d
	lda (l00f3),y
l239d	iny
	sty l00f2
	rts
	ldy l00f2
	lda #$20
l23a5	cmp (l00f3),y
	bne l23ac
	iny
	bne l23a5
l23ac	sty l00f2
	rts
	.endp

	.proc unknown_l23af
	ldy l00f2
	lda (l00f3),y
	sec
	sbc #$30
	bcc l23d0
	cmp #$0a
	rts
	lda l00f2
	pha
	jsr ldb94
	bcc l23e2
	cmp #$2e
	beq l23db
	cmp #$2b
	beq l23d2
	cmp #$2d
	beq l23d2
l23cf	pla
l23d0	sec
	rts
l23d2	jsr ldb94
	bcc l23e2
	cmp #$2e
	bne l23cf
l23db	jsr ldb94
	bcc l23e2
	bcs l23cf
l23e2	pla
	sta l00f2
	clc
	rts
	ldx #$e7
	bne l23ed
	ldx #$d5
l23ed	ldy #$04
l23ef	clc
	rol ramlo,x
	rol casini+1,x
	rol casini,x
	rol ngflag,x
	rol $00,x
	rol l00ec
	dey
	bne l23ef
	rts
	ldx #$00
	stx l00da
	ldx #$04
	lda l00d4
	beq l2438
l240a	lda l00d5
	bne l2428
	ldy #$00
l2410	lda l00d6,y
	sta l00d5,y
	iny
	cpy #$05
	bcc l2410
	dec l00d4
	dex
	bne l240a
	lda l00d5
	bne l2428
	sta l00d4
	clc
	rts
l2428	lda l00d4
	and #$7f
	cmp #$71
	bcc l2431
	rts
l2431	cmp #$0f
	bcs l2438
	jsr zfr0
l2438	clc
	rts
	ldx #$d4
	bne l2440
	ldx #$e0
l2440	stx l00f9
	sta l00f7
	sta l00f8
l2446	ldy #$04
l2448	lda ramlo,x
	sta ramlo+1,x
	dex
	dey
	bne l2448
	lda #$00
	sta ramlo+1,x
	ldx l00f9
	dec l00f7
	bne l2446
	lda $00,x
	clc
	adc l00f8
	sta $00,x
	rts
	ldx #$0a
l2464	lda l00d4,x
	sta l00d5,x
	dex
	bpl l2464
	lda #$00
	sta l00d4
	rts
	sta l00f7
	ldx #$00
	ldy #$00
l2476	jsr ldc93
	sec
	sbc #$01
	sta l00f7
	lda l00d5,x
	lsr
	lsr
	lsr
	lsr
	jsr ldc9d
	lda l00d5,x
	and #$0f
	jsr ldc9d
	inx
	cpx #$05
	bcc l2476
	lda l00f7
	bne l249c
	lda #$2e
	jsr ldc9f
l249c	rts
	ora #$30
	sta l0580,y
	iny
	rts
	ldx #$0a
l24a6	lda l0580,x
	cmp #$2e
	beq l24b4
	cmp #$30
	bne l24b8
	dex
	bne l24a6
l24b4	dex
	lda l0580,x
l24b8	rts
	jsr ldbeb
	lda l00ec
	and #$0f
	rts
	sec
	lda l00f3
	sbc #$01
	sta l00f3
	lda l00f4
	sbc #$00
	sta l00f4
	rts
	lda l00d4
	eor l00e0
	and #$80
	sta l00ee
	asl l00e0
	lsr l00e0
	lda l00d4
	and #$7f
	rts
	ora l00ee
	sta l00ed
	lda #$00
	sta l00d4
	sta l00e0
	jsr ldd28
	jsr ldbe7
	lda l00ec
	and #$0f
	sta l00e6
	lda #$05
	sta l00f5
	jsr ldd34
	jsr zfr0
	rts
	ldx #$d9
	bne l250b
	ldx #$d9
	bne l2511
	ldx #$df
l250b	ldy #$e5
	bne l2513
	ldx #$df
l2511	ldy #$eb
l2513	lda #$05
	sta l00f7
	clc
	sed
l2519	lda $00,x
	adc $0000,y
	sta $00,x
	dex
	dey
	dec l00f7
	bpl l2519
	cld
	rts
	ldy #$05
l252a	lda l00e0,y
	sta l00e6,y
	dey
	bpl l252a
	rts
	ldy #$05
l2536	lda l00d4,y
	sta l00da,y
	dey
	bpl l2536
	rts
	stx l00fe
	sty l00ff
	sta l00ef
	ldx #$e0
	ldy #$05
	jsr fstor
	jsr fmove
	ldx l00fe
	ldy l00ff
	jsr fld0r
	dec l00ef
	beq l2588
l255b	jsr fmul
	bcs l2588
	clc
	lda l00fe
	adc #$06
	sta l00fe
	bcc l256f
	lda l00ff
	adc #$00
	sta l00ff
l256f	ldx l00fe
	ldy l00ff
	jsr fld1r
	jsr fadd
	bcs l2588
	dec l00ef
	beq l2588
	ldx #$e0
	ldy #$05
	jsr fld1r
	bmi l255b
l2588	rts
	stx l00fc
	sty l00fd
	ldy #$05
l258f	lda (l00fc),y
	sta l00d4,y
	dey
	bpl l258f
	rts
	stx l00fc
	sty l00fd
	ldy #$05
l259e	lda (l00fc),y
	sta l00e0,y
	dey
	bpl l259e
	rts
	stx l00fc
	sty l00fd
	ldy #$05
l25ad	lda l00d4,y
	sta (l00fc),y
	dey
	bpl l25ad
	rts
	ldx #$05
l25b8	lda l00d4,x
	sta l00e0,x
	dex
	bpl l25b8
	rts
	ldx #$89
	ldy #$de
	jsr fld1r
	jsr fmul
	bcs l264b
	lda #$00
	sta l00f1
	lda l00d4
	sta l00f0
	and #$7f
	sta l00d4
	sec
	sbc #$40
	bmi l2603
	cmp #$04
	bpl l264b
	ldx #$e6
	ldy #$05
	jsr fstor
	jsr fpi
	lda l00d4
	sta l00f1
	lda l00d5
	bne l264b
	jsr ifp
	jsr fmove
	ldx #$e6
	ldy #$05
	jsr fld0r
	jsr fsub
l2603	lda #$0a
	ldx #$4d
	ldy #$de
	jsr plyevl
	jsr fmove
	jsr fmul
	lda l00f1
	beq l2639
	clc
	ror
	sta l00e0
	lda #$01
	bcc l2620
	lda #$10
l2620	sta l00e1
	ldx #$04
	lda #$00
l2626	sta l00e2,x
	dex
	bpl l2626
	lda l00e0
	clc
	adc #$40
	bcs l264b
	bmi l264b
	sta l00e0
	jsr fmul
l2639	lda l00f0
	bpl l264a
	jsr fmove
	ldx #$8f
	ldy #$de
	jsr fld0r
	jsr fdiv
l264a	rts
l264b	sec
	rts
	.endp

	.byte $3d,$17,$94,$19,$00,$00,$3d,$57,$33,$05,$00,$00,$3e,$05,$54,$76
	.byte $62,$00,$3e,$32,$19,$62,$27,$00,$3f,$01,$68,$60,$30,$36,$3f,$07
	.byte $32,$03,$27,$41,$3f,$25,$43,$34,$56,$75,$3f,$66,$27,$37,$30,$50
	.byte $40,$01,$15,$12,$92,$55,$3f,$99,$99,$99,$99,$99,$3f,$43,$42,$94
	.byte $48,$19,$40,$01,$00,$00,$00,$00
	stx l00fe
	sty l00ff
	ldx #$e0
	ldy #$05
	jsr fstor
	ldx l00fe
	ldy l00ff
	jsr fld1r
	jsr fadd
	ldx #$e6
	ldy #$05
	jsr fstor
	ldx #$e0
	ldy #$05
	jsr fld0r
	ldx l00fe
	ldy l00ff
	jsr fld1r
	jsr fsub
	ldx #$e6
	ldy #$05
	jsr fld1r
	jsr fdiv
	rts
	lda #$01
	bne l26d3
	lda #$00
l26d3	sta l00f0
	lda l00d4
	bpl l26db
	sec
	rts
l26db	lda l00d4
	sta l00e0
	sec
	sbc #$40
	asl
	sta l00f1
	lda l00d5
	and #$f0
	bne l26ef
	lda #$01
	bne l26f3
l26ef	inc l00f1
	lda #$10
l26f3	sta l00e1
	ldx #$04
	lda #$00
l26f9	sta l00e2,x
	dex
	bpl l26f9
	jsr fdiv
	ldx #$66
	ldy #$df
	jsr lde95
	ldx #$e6
	ldy #$05
	jsr fstor
	jsr fmove
	jsr fmul
	lda #$0a
	ldx #$72
	ldy #$df
	jsr plyevl
	ldx #$e6
	ldy #$05
	jsr fld1r
	jsr fmul
	ldx #$6c
	ldy #$df
	jsr fld1r
	jsr fadd
	jsr fmove
	lda #$00
	sta l00d5
	lda l00f1
	sta l00d4
	bpl l2746
	eor #$ff
	clc
	adc #$01
	sta l00d4
l2746	jsr ifp
	bit l00f1
	bpl l2753
	lda #$80
	ora l00d4
	sta l00d4
l2753	jsr fadd
	lda l00f0
	beq l2764
	ldx #$89
	ldy #$de
	jsr fld1r
	jsr fdiv
l2764	clc
	rts
	.byte $40,$03,$16,$22,$77,$66,$3f,$50,$00,$00,$00,$00,$3f,$49,$15,$57
	.byte $11,$08,$bf,$51,$70,$49,$47,$08,$3f,$39,$20,$57,$61,$95,$bf,$04
	.byte $39,$63,$03,$55,$3f,$10,$09,$30,$12,$64,$3f,$09,$39,$08,$04,$60
	.byte $3f,$12,$42,$58,$47,$42,$3f,$17,$37,$12,$06,$08,$3f,$28,$95,$29
	.byte $71,$17,$3f,$86,$85,$88,$96,$44,$3e,$16,$05,$44,$49,$00,$be,$95
	.byte $68,$38,$45,$00,$3f,$02,$68,$79,$94,$16,$bf,$04,$92,$78,$90,$80
	.byte $3f,$07,$03,$15,$20,$00,$bf,$08,$92,$29,$12,$44,$3f,$11,$08,$40
	.byte $09,$11,$bf,$14,$28,$31,$56,$04,$3f,$19,$99,$98,$77,$44,$bf,$33
	.byte $33,$33,$31,$13,$3f,$99,$99,$99,$99,$99,$3f,$78,$53,$98,$16,$34
	.byte $98,$16,$34,$fc,$e0,$32,$50,$d9,$68,$11

	icl "MaxflashMenu-Old-$2800.asm"

	.proc unknown_atr
	clc
	lda ddevic
	adc dunit
	adc #$ff
	sta cdevic
	cmp #$31
	beq l2aa9
l2aa6	jmp os.le959
l2aa9	lda consol
	and #$07
	cmp #$01
	beq l2aa6
	inc critic
	lda dbuflo
	sta bufrlo
	lda dbufhi
	sta bufrhi
	lda daux1
	sta caux1
	lda daux2
	sta caux2
	lda dcomnd
	cmp #$52
	beq l2af7
	cmp #$53
	beq l2ad7
	bne l2aec
l2ad7	ldy #$03
	lda #$00
	sta (bufrlo),y
	dey
	lda #$20
	sta (bufrlo),y
	dey
	lda #$ff
	sta (bufrlo),y
	dey
	lda #$10
	sta (bufrlo),y
l2aec	ldy #$01
	lda #$00
	sty dstats
	sta critic
	sec
	rts
l2af7	sec
	lda caux1
	sbc #$01
	sta caux1
	lda caux2
	sbc #$00
	sta caux2
	asl caux1
	rol caux2
	asl caux1
	rol caux2
	clc
	lda #$00
	sta dstats
	adc caux2
	tax
	lda #$00
	sta bfenlo
	lsr caux1
	lsr caux1
	lsr caux1
	ror bfenlo
	clc
	lda caux1
	adc #$a0
	sta bfenhi
	bne l2b39
l2b37	bcc l2aec
l2b39	lda #$a0
	sta bufrhi
	lda #$00
	sta bufrlo
	sta ld500,x
l2b44	ldy #$00
l2b46	eor (bufrlo),y
	iny
	bne l2b46
	inc bufrhi
	ldy bufrhi
	cpy #$c0
	bne l2b44
	ldy caux2
	cmp l2bb5,y
	bne l2b39
	lda #$c5
	sta bufrlo
	lda #$2b
	sta bufrhi
	ldy #$00
l2b65	lda (bfenlo),y
	sta (bufrlo),y
	iny
	bpl l2b65
l2b6c	sta ld510
L2B6f	lda #$00
	beq l2b83
	lda la000+1
	cmp #$ca
	bne l2b6c
	lda la000+2
	cmp #$d0
	bne l2b6c
	beq l2b91
l2b83	lda la000
	inc la000
	cmp la000
	beq l2b6c
	sta la000
l2b91	lda dbuflo
	sta bfenlo
	lda dbufhi
	sta bfenhi
	ldy #$00
l2b9d	lda (bufrlo),y
	sta (bfenlo),y
	iny
	bpl l2b9d
	lda #$e0
	sta audc1
	sta audc2
	sta audc3
	sta audc4
	clc
	bcc l2b37
	.endp
	
	.proc l2bb5
	ldy #$01
	lda (l0084),y
	sei
	tax
	jsr eor_l3758
	ldy #$00
	tya
l2bc1	sta $0000,y
	iny
	bne l2bc1
	jmp coldsv
	.endp


	.proc init_l2bca
	ldx #$ff
	txs
	lda #$80
	sta ramsiz
	sta memtop+1
	sta ramtop
	sta ramlo+1
	lda #$00
	jsr l2f1f
	lda sdlstl
	sta l0118
	lda sdlsth
	sta l0119
	lda #$00
	sta sdlstl
	sta sdlsth
	ldy #$00
l2bf4	lda ichidz,y
	sta l0100,y
	lda #$00
	sta ichidz,y
	iny
	cpy #$18
	bne l2bf4
	ldy #$00
	tya
l2c07	sta hposp0,y
	sta audf1,y
	sta dmaclt,y
	iny
	bne l2c07
	sei
	lda #$00
	sta pokmsk
	sta irqen
	sta nmien
	ldy #$01
	lda (l0084),y
	pha
	tax
	jsr eor_l3758
	lda #$a0
	sta l0085
	lda #$80
	sta l0087
	lda #$00
	sta l0084
	sta l0086
	tay
l2c36	lda (l0084),y
	sta (l0086),y
	iny
	bne l2c36
	inc l0085
	inc l0087
	lda l0085
	cmp #$c0
	bne l2c36
	pla
	tax
	inx
	jsr eor_l3758
	lda trig3
	sta gintlk
	lda #$00
	sta boot
	lda #$01
	sta basicf
	sta critic
	tax
	clc
l2c60	adc lbff0,x
	inx
	bne l2c60
	sta cartck
	ldy #$00
l2c6b	lda l2c79,y
	sta l011a,y
	iny
	cpy #$d1
	bne l2c6b
	jmp l011a
	.endp

	.proc l2c79
	lda #$00
	sta l0084
	tay
	lda lbffd
	and #$80
	bne l2c89
	lda #$10
	bne l2c8b
l2c89	lda #$02
l2c8b	sta l0085
	lda #$00
l2c8f	sta (l0084),y
	iny
	bne l2c8f
	inc l0085
	ldx l0085
	cpx #$70
	bne l2c8f
	lda trig3
	sta gintlk
	lda lbffc
	bne l2cc9
	lda lbffd
	bpl l2cc9
	lda #$01
	sta coldst
	ldy #$00
	tya
l2cb4	sta $0000,y
	iny
	bne l2cb4
	lda #$00
	sta rtclok
	sta rtclok+1
	sta rtclok+2
	lda lbffd
	cli
	jmp (lbffe)

l2cc9	lda #$ff
	sta coldst
	lda #$40
	sta nmien
	ldy #$82
	lda #$00
	sta critic
l2cd9	sta $0000,y
	iny
	bne l2cd9
	jsr l015b
	sei
	lda #$00
	sta coldst
	lda l0118
	sta sdlstl
	sta dlistl
	lda l0119
	sta sdlsth
	sta dlisth
	ldy #$00
l2cfc	lda l0100,y
	sta ichidz,y
	iny
	cpy #$18
	bne l2cfc
	ldx #$22
	stx sdmctl
	stx dmaclt
	inx
	stx sskctl
	stx skctl
	lda #$02
	sta chact
	sta chactl
	lda #$e0
	sta chbas
	sta chbase
	lda #$28
	sta audctl
	ldx #$00
l2d2d	lda l01e7,x
	sta color0,x
	sta colpf0,x
	inx
	cpx #$04
	bne l2d2d
	lda #$c0
	sta pokmsk
	sta irqen
	cli
	jmp (lbffa)
	plp
	dex
	sty fmszpg+3,x
	.endp

	.proc init_l2d4a
	ldx #$ff
	txs
	sei
	stx nmien
	lda #$a0
	sta ramsiz
	sta memtop+1
	sta ramtop
	sta ramlo+1
	ldx #$00
	stx coldst
	stx critic
	lda l139b
	sta l2e68+1
	txa
	jsr l2e68
	txa
	jsr l2f1f
	lda #$c0
	sta ramsiz
	sta memtop+1
	sta ramtop
	sta ramlo+1
	ldy #$00
	lda (l0084),y
	and #$01
	sta chksnt
	ldy #$02
	lda (l0084),y
	sta bufrlo
	iny
	lda (l0084),y
	clc
	adc #$a0
	sta bufrhi
	ldy #$01
	lda (l0084),y
	tay
	sta bufrfl
	lda #$00
	sta recvdn
	ldx #$00
	txa
l2da2	sta lomem,x
	sta l0180,x
	inx
	cpx #$80
	bne l2da2
	lda #$10
	jsr l2ddf
	jsr l2df0
	jsr l2dfe
	ldx bufrfl
	jsr eor_l3758
	lda trig3
	sta gintlk
	ldx #$00
l2dc4	lda la000,x
	sta prnbuf,x
	lda #$00
	inx
	cpx #$10
	bne l2dc4
	stx status
	lda #$10
	sta bfenhi
	lda #$00
	sta bfenlo
	tay
	jmp l0100
	.endp

	.proc l2ddf
	sta bfenlo
	ldx #$00
l2de3	lda l36d8,y
	sta prnbuf+16,x
	iny
	inx
	cpx bfenlo
	bne l2de3
	rts
	.endp

	.proc l2df0
	ldx #$00
l2df2	lda l2eef,x
	sta hibyte,x
	inx
	cpx #$30
	bne l2df2
	rts
	.endp

	.proc l2dfe
	ldx #$00
l2e00	lda l2e0c,x
	sta l0100,x
	inx
	cpx #$e3
	bne l2e00
	rts
	.endp

	.proc l2e0c
	sta (bfenlo),y
	iny
	bne l2e0c
	inc bfenhi
	ldx bfenhi
	cpx #$50
	bne l2e0c
l2e19	jsr l0194
	sta bfenlo
	jsr l0194
	sta bfenhi
	jsr l0194
	sta ltemp
	jsr l0194
	sta ltemp+1
	ora ltemp
	beq l2e86
	lda #$00
	sta initad
	sta initad+1
l2e39	jsr l0194
	jsr hibyte
	dec ltemp
	lda ltemp
	cmp #$ff
	bne l2e49
	dec ltemp+1
l2e49	lda ltemp
	ora ltemp+1
	bne l2e39
	lda initad
	ora initad+1
	beq l2e19
	jsr l015c
	cli
	jsr l0159
	sei
	jsr txtold+5
	clc
	bcc l2e19
	jmp (initad)
	.endp

	.proc l2e68
	sta ld580
	lda trig3
	bne l2e68
	sta gintlk
	lda la000
	inc la000
	cmp la000
	beq l2e68
	sta la000
	lda #$00
	sta status
	rts
	.endp
	
	.proc l2e86
	jsr l015c
	cli
	ldy #$00
	lda chksnt
	bne l2e9d
	lda runad
	sta dosvec
	lda runad+1
	sta dosvec+1
	jmp warmsv
	.endp

l2e9d	jmp (runad)

	.proc l2ea0
	ldx #$00
	lda (bufrlo,x)
	tax
	inc bufrlo
	bne l2eed
	inc bufrhi
	lda bufrhi
	cmp #$c0
	bne l2eed
	inc bufrfl
	inc recvdn
	stx xmtdon
	ldx bufrfl
l2eb9	sta ld500,x
	lda #$a0
	sta bufrhi
	lda #$00
	sta bufrlo
l2ec4	ldy #$00
l2ec6	eor (bufrlo),y
	iny
	bne l2ec6
	inc bufrhi
	ldy bufrhi
	cpy #$c0
	bne l2ec4
	ldy recvdn
	cmp prnbuf+16,y
	bne l2eb9
	lda #$a0
	sta bufrhi
	ldx #$00
l2ee0	lda la000,x
	sta prnbuf,x
	inx
	cpx #$10
	bne l2ee0
	ldx xmtdon
l2eed	txa
	rts
	.endp

l2eef	ldx bfenhi
	cpx #$a0
	bcc l2efa
	tax
	jsr l015c
	txa
l2efa	ldx #$00
	sta (bfenlo,x)
	ldx status
	bne l2f18
l2f02	ldx bufrfl
	sta ld500,x
	ldx #$00
l2f09	lda la000,x
	cmp prnbuf,x
	bne l2f02
	inx
	cpx #$10
	bne l2f09
	stx status
l2f18	inc bfenlo
	bne l2f1e
	inc bfenhi
l2f1e	rts

	.proc l2f1f
	pha
	ldx #$60
	lda #$0c
	sta iocb0+iccom,x
	jsr ciov
	ldx #$00
	lda #$0c
	sta iocb0+iccom,x
	jsr ciov
	ldx #$00
	lda #$03
	sta iocb0+iccom,x
	lda #$77
	sta iocb0+icbal,x
	lda #$2f
	sta iocb0+icbah,x
	lda #$0c
	sta iocb0+icax1,x
	lda #$00
	sta iocb0+icax2,x
	jsr ciov
	ldx #$60
	lda #$03
	sta iocb0+iccom,x
	lda #$73
	sta iocb0+icbal,x
	lda #$2f
	sta iocb0+icbah,x
	pla
	sta iocb0+icax2,x
	and #$f0
	eor #$10
	ora #$0c
	sta iocb0+icax1,x
	jmp ciov
	.byte 'S:'
	.byte $9b,$00
	.byte 'E:'
	.byte $9b,$00
	.endp

	.proc get_stick_l2f7b
	lda trig0
	bne l2f9a
l2f80	lda trig0
	beq l2f80
	jsr get_byte_l35c4
	cmp #$ff
	beq l2f9a
	cmp #$00
	bne l2f95
	jsr l34e5
	clc
	rts

l2f95	jsr l3598
	sec
	rts

l2f9a	lda porta
	and #$04
	bne l2fa6
	jsr l350f
	clc
	rts

l2fa6	lda porta
	and #$08
	bne l2fbd
	jsr get_byte_l35c4
	cmp #$ff
	beq l2fbb
	cmp #$00
	bne l2fbb
	jsr l34e5
l2fbb	clc
	rts

l2fbd	lda porta
	and #$01
	bne l2fe4
	jsr cursor_up_l3538
	ldy #$80
	jsr l358f
	lda porta
	and #$01
	bne l2fbb
l2fd3	jsr cursor_up_l3538
	ldy #$d0
	jsr l358f
	lda porta
	and #$01
	beq l2fd3
	bne l2fbb
l2fe4	lda porta
	and #$02
	bne l3009
	jsr cursor_down_l3563
	ldy #$80
	jsr l358f
	lda porta
	and #$02
	bne l3009
l2ffa	jsr cursor_down_l3563
	ldy #$d0
	jsr l358f
	lda porta
	and #$02
	beq l2ffa
l3009	clc
	rts
	.endp

	.proc get_key_l300b
	lda ch
	cmp #$ff
	bne l3014
	clc
	rts
l3014	ldy #$ff
	sty ch

	cmp #$1c	;ESC
	bne l3022
	jsr l350f
l3020	clc
	rts

l3022	cmp #$0c	;RETURN
	bne l303b
	jsr get_byte_l35c4
	cmp #$ff
	beq l3020
	cmp #$00
	bne l3036
	jsr l34e5
	clc
	rts

l3036	jsr l3598
	sec
	rts

l303b	cmp #$0e
	bne l3044
	jsr cursor_up_l3538
	clc
	rts

l3044	cmp #$0f
	bne l304b
	jsr cursor_down_l3563
l304b	clc
	rts
	.endp

	.proc init_screen_l304d
	lda #$c0
	sta ramsiz
	inc basicf
	lda #$00
	sta warmst
	sta coldst
	sta porta
	lda os.lf104
	cmp #$4d
	beq l306b
	lda #$ff
	sta portb

l306b	lda #$3c
	sta pactl
	sta pbctl
	lda portb
	lda porta
	lda #$23
	sta skctl
	lda #$a0
	sta audc3
	sta audc4
	lda #$28
	sta audctl
	lda #$ff
	sta serout
	lda #$c0
	sta pokmsk
	sta irqen
	lda #$7f
	sta nmien
	lda #$e0
	sta chbas
	sta chbase
	lda #$02
	sta chact
	sta chactl
	lda #$5c
	sta pupbt1
	lda #$93
	sta pupbt2
	lda #$25
	sta pupbt3
	lda #$ff
	sta ch
	rts
	.endp

	.proc init_colors_l30c1
	lda #$ff
	sta pcolr0
	sta pcolr1
	sta pcolr2
	sta pcolr3
	lda #$0c
	sta color0
	sta color1
	sta colpf0
	sta colpf1
	lda #$00
	sta color2
	sta color3
	sta color4
	sta colpf2
	sta colpf3
	sta colbk
	rts
	.endp

	.proc clear_screen_l30f2
	lda #$00
	sta l00fe
	lda #$10
	sta l00ff
	lda #$00
	tay
	ldx #$03
	beq l310b
l3101	sta (l00fe),y
	iny
	bne l3101
	inc l00ff
	dex
	bne l3101
l310b	cpy #$aa
	beq l3114
	sta (l00fe),y
	iny
	bne l310b
l3114	rts
	.endp

	.proc main_loop_l3115
	jsr init_screen_l304d
	jsr init_colors_l30c1
	jsr clear_screen_l30f2
	jsr init_menu_l31c0	;Autorun?
	bcs start
	jsr set_menu_ptr_l33a88
loop	jsr get_stick_l2f7b
	bcs start		;Trigger pressed?
	jsr get_key_l300b
	bcs start		;Enter pressed?
	jmp loop

start	jsr start_entry_l35de	;Pressed
	jmp main_loop_l3115
	.endp

	.local dl_l3139
	.byte aempty8
	.byte aempty8
	.byte aempty8
	.byte alms+$07
	.word l1000
	.byte aempty4
l3140	.byte alms+$02
	.word l1028
	.byte alms+$02
	.word l1050
	.byte alms+$02
	.word l1078
	.byte alms+$02
	.word l10a0
	.byte alms+$02
	.word l10c8
	.byte alms+$02
	.word l10f0
	.byte alms+$02
	.word l1118
	.byte alms+$02
	.word l1140
	.byte alms+$02
	.word l1168
	.byte alms+$02
	.word l1190
	.byte alms+$02
	.word l11b8
	.byte alms+$02
	.word l11e0
	.byte alms+$02
	.word l1208
	.byte alms+$02
	.word l1230
	.byte alms+$02
	.word l1258
	.byte alms+$02
	.word l1280
	.byte alms+$02
	.word l12a8
	.byte alms+$02
	.word l12d0
	.byte alms+$02
	.word l12f8
	.byte alms+$02
	.word l1320
	.byte aempty4
	.byte alms+$02
	.word l1370
	.byte avb+ajmp
	.word dl_l3139
	.endl

text_l3183	.byte $80,$80,$b3,$e3,$f2,$ef,$ec,$ec,$80,$e4,$ef,$f7,$ee,$80,$e6,$ef
	.byte $f2,$80,$ed,$ef,$f2,$e5,$80,$f4,$e9,$f4,$ec,$e5,$f3,$80,$80,$80
	.byte $80,$80,$80,$80,$80,$80,$80,$80
l31ab	.byte $00,$03,$06,$09,$0c,$0f,$12,$15,$18,$1b,$1e,$21,$24,$27,$2a,$2d
	.byte $30,$33,$36,$39,$3c

	.proc init_menu_l31c0		;OUT: <C>=0/1 for autorun
	lda l3ff7
	sta l0088
	lda l3ff7+1
	sta l0089

	ldy #$00
	sty l1398
	sty selected_entry_l139f
	ldx #$00
	lda (l0088),y
	sta l139a
	and #$c0
	beq l31e3
	lda #$7f
	ldx #$80
	bne l31f2
l31e3	lda l139a
	and #$20
	bne l31ee
	ldx #$10
	bne l31f2
l31ee	lda #$24
	ldx #$30
l31f2	sta l1399
	stx l139b

	clc
	lda l0088
	adc #$01
	sta l0088
	lda l0089
	adc #$00
	sta l0089

	lda os.lf104
	cmp #$4d		;"M"
	bne return_clc
	lda os.lf114
	cmp #$52		;"R"
	bne return_clc
	lda os.lf115
	cmp #$9b		;RETURN
	bne return_clc

	lda os.lf116
	sta menu_ptr_l13a0
	lda os.lf117
	sta menu_ptr_l13a0+1
	lda #$4f		;"O"
	sta os.lf115
	lda #$52		;"R"
	sta os.lf116
	lda #$9b		;RETURN
	sta os.lf117
	sec
	rts

return_clc
	clc
	rts
	.endp

	.proc l3239
	ldy l139c
	lda l31ab,y
	tay
	txa
	sta dl_l3139.l3140,y
	rts
	.endp

	.proc add40_l3245
	clc
	lda l0086
	adc #$28
	sta l0086
	lda l0087
	adc #$00
	sta l0087
	rts
	.endp

	.proc l3253
	lda l13a4
	beq l3271
	ldy #$00
	tya
l325b	sta (l0086),y
	iny
	cpy l13a4
	bne l325b
	clc
	lda l0086
	adc l13a4
	sta l0086
	lda l0087
	adc #$00
	sta l0087

l3271	ldy #$00
	lda l13a3
	sta (l0086),y
	iny
l3279	lda (l0084),y
	cmp #$9b
	beq l3289
	ora l13a2
	sta (l0086),y
	iny
	cpy #$28
	bne l3279
l3289	iny
	tya
	dey
	clc
	adc l0084
	sta l0084
	lda #$00
	adc l0085
	sta l0085
	lda l13a2
l329a	sta (l0086),y
	iny
	cpy #$28
	bne l329a
	jsr add40_l3245
	sec
	lda l0086
	sbc l13a4
	sta l0086
	lda l0087
	sbc #$00
	sta l0087
	inc l139c
	rts
	.endp

	.proc l32b6
	clc
	lda l008a
	adc #$02
	sta l008a
	lda l008b
	adc #$00
	sta l008b
	rts
	.endp

l32c4	lda l008a
	sta l0084
	lda l008b
	sta l0085
	ldy #$00
l32ce	lda (l0084),y
	iny
	cmp #$9b
	bne l32ce
	tya
	clc
	adc l0084
	sta l0084
	lda #$00
	adc l0085
	sta l0085
l32e1	ldy #$00
	lda (l0084),y
	sta l008c
	iny
	lda (l0084),y
	ora l008c
	beq l32fe
	clc
	lda l0084
	adc #$02
	sta l0084
	lda l0085
	adc #$00
	sta l0085
	jmp l32e1
l32fe	clc
	lda l0084
	adc #$02
	sta l008a
	lda l0085
	adc #$00
	sta l008b
	rts
l330c	clc
	lda l008c
	adc #$03
	sta l0084
	lda l008d
	adc #$00
	sta l0085
	ldx #$42
	jsr l3239
	lda l139d
	cmp selected_entry_l139f
	bne l3336
	lda l008c
	sta menu_ptr_l13a0
	lda l008d
	sta menu_ptr_l13a0+1
	lda #$1e
	ldy #$80
	bne l3339
l3336	lda #$00
	tay
l3339	sta l13a3
	sty l13a2
	lda #$01
	sta l13a4
	jsr l3253
	rts
l3348	lda l008a
	sta l0084
	lda l008b
	sta l0085
	ldx #$46
	jsr l3239
	lda l139d
	cmp selected_entry_l139f
	bne l336b
	lda l008a
	sta menu_ptr_l13a0
	lda l008b
	sta menu_ptr_l13a0+1
	lda #$1e
	bne l336d
l336b	lda #$00
l336d	sta l13a3
	lda #$00
	sta l13a2
	sta l13a4
	jsr l3253
l337b	ldy #$00
	lda (l0084),y
	sta l008c
	iny
	lda (l0084),y
	sta l008d
	ora l008c
	beq l339a
	clc
	lda l0084
	adc #$02
	sta l0084
	lda l0085
	adc #$00
	sta l0085
	jmp l337b
l339a	clc
	lda l0084
	adc #$02
	sta l008a
	lda l0085
	adc #$00
	sta l008b
	rts

	.proc set_menu_ptr_l33a88
	lda #$00
	sta selected_max_line_l13a9
	sta menu_ptr_l13a0
	sta menu_ptr_l13a0+1
	lda l0088
	sta l0084
	lda l0089
	sta l0085
	lda #$00
	sta l0086
	lda #$10
	sta l0087
	lda #$00
	sta l13a3
	sta l13a2
	sta l13a4
	jsr l3253
	lda #$00
	sta l139c
	sta l139d
	lda l0084
	sta l008a
	lda l0085
	sta l008b
l33e1	ldy #$00
	lda (l008a),y
	sta l008c
	iny
	lda (l008a),y
	sta l008d
	ora l008c
	beq l3419
	lda l139c
	cmp #$14
	beq l3419
	lda l139d
	cmp l139e
	bcc l340d
	lda l139e
	clc
	adc #$14
	cmp l139d
	bcc l340d
	jsr l330c
l340d	jsr l32b6
	inc l139d
	inc selected_max_line_l13a9
	jmp l33e1

l3419	ldy #$00
	lda (l008a),y
	sta l008c
	iny
	lda (l008a),y
	ora l008c
	beq l342f
	jsr l32b6
	inc selected_max_line_l13a9
	jmp l3419
l342f	lda l1398
	bne l347c
	jsr l32b6
l3437	ldy #$00
	lda (l008a),y
	cmp #$00
	bne l347c
	lda l139c
	cmp #$14
	beq l346b
	lda l139d
	cmp l139e
	bcc l345f
	lda l139e
	clc
	adc #$14
	cmp l139d
	bcc l345f
	jsr l3348
	jmp l3462
l345f	jsr l32c4
l3462	inc l139d
	inc selected_max_line_l13a9
	jmp l3437
l346b	ldy #$00
	lda (l008a),y
	cmp #$00
	bne l347c
	jsr l32c4
	inc selected_max_line_l13a9
	jmp l346b
l347c	lda l139c
	cmp #$14
	beq l3496
	ldy #$00
	tya
l3486	sta (l0086),y
	iny
	cpy #$28
	bne l3486
	inc l139c
	jsr add40_l3245
	jmp l347c
l3496	ldx #$00
	lda selected_max_line_l13a9
	cmp #$15
	bcs l34aa
	txa
l34a0	sta l1370,x
	inx
	cpx #$28
	bne l34a0
	beq l34b5
l34aa	lda text_l3183,x
	sta l1370,x
	inx
	cpx #$28
	bne l34aa

l34b5	lda vcount
	cmp #$80
	bne l34b5
	lda #$22
	sta dmaclt
	sta sdmctl
	lda #$39
	sta sdlstl
	sta dlistl
	lda #$31
	sta sdlsth
	sta dlisth
	lda #$e0
	sta chbase
	sta chbas
	lda #$02
	sta chact
	sta chactl
	rts
	.endp

	.proc l34e5
	lda l1398
	bne l350e
	inc l1398
	lda selected_entry_l139f
	sta l13a6
	lda l139e
	sta l13a5
	lda #$00
	sta selected_entry_l139f
	sta l139e
	lda menu_ptr_l13a0
	sta l0088
	lda menu_ptr_l13a0+1
	sta l0089
	jsr set_menu_ptr_l33a88
l350e	rts
	.endp

	.proc l350f
	lda l1398
	beq l3537
	lda #$00
	sta l1398
	lda l13a6
	sta selected_entry_l139f
	lda l13a5
	sta l139e
	clc
	lda l3ff7
	adc #$01
	sta l0088
	lda l3ff8
	adc #$00
	sta l0089
	jsr set_menu_ptr_l33a88
l3537	rts
	.endp

	.proc cursor_up_l3538
	lda selected_entry_l139f
	beq l354e
	cmp l139e
	beq l3544
	bcs l3547
l3544	dec l139e
l3547	dec selected_entry_l139f
	jsr set_menu_ptr_l33a88
	rts

l354e	lda selected_max_line_l13a9
	sta selected_entry_l139f
	sec
	sbc #$14
	sta l139e
	bpl l3547
	lda #$00
	sta l139e
	beq l3547
	.endp

	.proc cursor_down_l3563
	ldx selected_entry_l139f
	inx
	cpx selected_max_line_l13a9
	bcs scroll_down_l3585
	lda selected_entry_l139f
	cmp #$13
	bcc l357e
	sec
	sbc l139e
	cmp #$13
	bcc l357e
	inc l139e
l357e	inc selected_entry_l139f
l3581	jsr set_menu_ptr_l33a88
	rts
	.endp
	
	.proc scroll_down_l3585
	lda #$00
	sta selected_entry_l139f
	sta l139e
	beq cursor_down_l3563.l3581
	.endp

	.proc l358f
	ldx #$00
l3591	inx
	bne l3591
	iny
	bne l3591
	rts
	.endp
	
	.proc l3598
	lda #$03
	sta l13a8
l359d	lda selected_entry_l139f
	sta l13a7
	lda #$ff
	sta selected_entry_l139f
	jsr set_menu_ptr_l33a88
	ldy #$e0
	jsr l358f
	lda l13a7
	sta selected_entry_l139f
	jsr set_menu_ptr_l33a88
	ldy #$e0
	jsr l358f
	dec l13a8
	bne l359d
	rts
	.endp

	.proc get_byte_l35c4
	lda menu_ptr_l13a0
	ora menu_ptr_l13a0+1
	bne l35cf
	lda #$ff
	rts

l35cf	lda menu_ptr_l13a0
	sta l0084
	lda menu_ptr_l13a0+1
	sta l0085
	ldy #$00
	lda (l0084),y
	rts
	.endp

	.proc start_entry_l35de
	jsr get_byte_l35c4		;Get type of entry
	tay
	and #$80
	beq l360b
	lda os.lf104
	cmp #$4d
	beq l360b
	lda portb
	and #$01
	beq l360b
	jsr l363b
	lda #$9b
	sta os.lf115
	lda menu_ptr_l13a0
	sta os.lf116
	lda menu_ptr_l13a0+1
	sta os.lf117
	jmp coldsv
	.endp

	.proc l360b		;Start entry
	tya
	and #$7f
	cmp #$02
	bne l3615
l3612	jmp l2bb5
l3615	cmp #$01
	beq l3612
	cmp #$03
	bne l3620
	jmp init_l2bca
l3620	cmp #$04
	bne l3627
	jmp init_l2d4a
l3627	cmp #$05
	bne l362e
	jmp init_l2d4a
l362e	cmp #$06
	bne l3635
l3632	jmp l2800
l3635	cmp #$07
	beq l3632
	clc
	rts
	.endp

	.proc l363b
	lda portb
	ora #$02
	sta portb
	sei
	lda #$00
	sta nmien
	lda portb
	and #$fe
	sta portb
	lda #$00
	sta l0084
	lda #$20
	sta l0085
	lda #$00
	sta l0086
	lda #$d8
	sta l0087
	ldx #$08
	ldy #$00
l3665	lda (l0084),y
	sta (l0086),y
	iny
	bne l3665
	inc l0085
	inc l0087
	dex
	bne l3665
	ldx l139a
	jsr eor_l3758
	lda #$00
	sta l0086
	sta l0084
	lda #$a0
	sta l0085
	lda #$e0
	sta l0087
	ldx #$20
	ldy #$00
l368b	lda (l0084),y
	sta (l0086),y
	iny
	bne l368b
	inc l0085
	inc l0087
	dex
	bne l368b
l3699	ldy #$00
	ldx l1399
	sta ld500,x
l36a1	lda la000,y
	cmp l2000,y
	bne l3699
	iny
	bne l36a1
	rts
	.endp

	org $b6ad

	.proc main_b6ad
	lda consol
	and #$04
	beq l36d7
	lda #$a0
	sta l00fd
	lda #$20
	sta l00ff
	lda #$00
	sta l00fc
	sta l00fe
	tax
	tay
l36c4	lda (l00fc),y
	sta (l00fe),y
	iny
	bne l36c4
	inc l00fd
	inc l00ff
	inx
	cpx #$20
	bne l36c4
	jmp main_loop_l3115
l36d7	rts
	.endp

	org $36d8
l36d8	.byte $42,$ba,$08,$8f,$5e,$c6,$4a,$a3,$e4,$64,$f3,$e6,$63,$f8,$3f,$39
	.byte $ef,$ce,$6d,$72,$bd,$5a,$d9,$42,$96,$18,$26,$77,$29,$12,$57,$4c
	.byte $5d,$c7,$07,$e6,$59,$af,$44,$ed,$a5,$9c,$be,$17,$eb,$41,$32,$78
	.byte $9b,$48,$13,$f6,$92,$dd,$4e,$29,$93,$2d,$7a,$73,$0c,$2d,$d9,$78
	.byte $d0,$95,$f9,$24,$4c,$eb,$d9,$a0,$41,$d1,$63,$57,$af,$24,$a3,$76
	.byte $7c,$93,$55,$e4,$83,$f7,$64,$01,$13,$7d,$d0,$74,$e8,$da,$cd,$11
	.byte $23,$2f,$4d,$cd,$6c,$60,$10,$10,$fc,$8e,$6f,$1a,$6a,$0c,$08,$39
	.byte $eb,$74,$84,$5f,$0a,$d3,$a9,$d1,$5f,$e0,$19,$ec,$e9,$79,$c0,$77

	.proc eor_l3758			;IN: <X>
	lda #$a0
	sta l008d
	lda #$00
	sta l008c
	sta ld500,x
l3763	ldy #$00
l3765	eor (l008c),y
	iny
	bne l3765
	inc l008d
	ldy l008d
	cpy #$c0
	bne l3763
	cmp l36d8,x
	bne eor_l3758
	rts
	.endp
	jmp main_b6ad

	org $b77b

	.byte $00,$01,$ad,$b6,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
	.byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
lbc77
	icl "MaxflashMenu-Old-Entries.asm"

lbff7	.word lbc77

lbff9	jmp main_b6ad
lbffa	= lbff9+1
lbffc	.byte $00
lbffd	.byte $01
lbffe	.word main_b6ad

;	.word $2e0,$2e1,lbff9