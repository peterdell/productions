;	@com.wudsn.ide.asm.mainsourcefile=MaxflashMenu-Old.asm

	.proc L2800
       sei
            lda L139B
            sta L2E68+1
            sta unknown_atr.L2B6c+1
            jsr L2E68
            jsr L2822
            jsr L283B
            jsr L28BB
            jsr L29FE
            jsr L2A3D
            jsr L2A89
            jmp COLDSV
L2822       ldy #$00
            lda (L0084),Y
            and #$80
            sta L00FA
            ldy #$00
            lda (L0084),Y
            and #$7F
            cmp #$07
            bne L2835
            dey
L2835       sty L00FB
            sty unknown_atr.L2B6f+1
            rts
L283B       lda os.LF104
            cmp #$4D
            beq L28BA
            lda PORTB
            ora #$02
            sta PORTB
            lda #$00
            sta L00FC
            lda #$C0
            sta L00FD
            lda #$00
            sta L00FE
            lda #$40
            sta L00FF
            ldy #$00
L285C       lda (L00FC),Y
            sta (L00FE),Y
            iny
            bne L285C
            inc L00FF
            inc L00FD
            beq L2879
            lda L00FF
            cmp #$50
            bne L285C
            lda #$58
            sta L00FF
            lda #$D8
            sta L00FD
            bne L285C
L2879       lda #$00
            sta NMIEN
            lda PORTB
            and #$FE
            sta PORTB
            lda #$00
            sta L00FC
            lda #$40
            sta L00FD
            lda #$00
            sta L00FE
            lda #$C0
            sta L00FF
            ldy #$00
L2898       lda (L00FC),Y
            sta (L00FE),Y
            iny
            bne L2898
            inc L00FD
            inc L00FF
            beq L28B5
            lda L00FF
            cmp #$D0
            bne L2898
            lda #$D8
            sta L00FF
            lda #$58
            sta L00FD
            bne L2898
L28B5       lda #$40
            sta NMIEN
L28BA       rts
L28BB       lda os.lf104
            cmp #$4D
            bne L28DC
            lda #$A9
            sta os.lf257
            lda #$C0
            sta os.lf258
            lda #$85
            sta os.lf259
            lda #$06
            sta os.lf25A
            lda #$60
            sta os.lf25B
            rts
L28DC       lda os.lc313
            cmp #$85
            beq L28E6
            jmp L2972
L28E6       lda #$89
            sta os.lc327
            sta os.lc322
            lda #$FF
            sta os.lc328
            sta os.lc323
            lda #$FE
            sta os.lc507
            lda #$EA
            sta os.lc41E
            sta os.lc41F
            sta os.lc420
            sta os.lc495
            sta os.lc496
            sta os.lc497
            sta os.lc498
            sta os.lc499
            sta os.lc49A
            sta os.lc49B
            sta os.lc49C
            sta os.lc4AF
            sta os.lc4B0
            sta os.lc4B1
            sta os.lc4B2
            sta os.lc4B3
            sta os.lc4B4
            sta os.lc4B5
            sta os.lc4B6
            sta os.lc4FB
            sta os.lc4FC
            sta os.lc4FD
            sta os.lc508
            sta os.lc509
            sta os.lc50A
            lda #$A9
            sta os.LC90F
            lda #$7F
            sta os.LC910
            lda #$EA
            sta os.LC911
            sta os.LC912
            sta os.LC913
            ldy L00FB
            beq L2967
            sta os.lc413
            sta os.lc414
L2967       lda #$C0
            sta os.lc4BD
            lda #$60
            sta os.lc4C0
            rts
L2972       lda #$8E
            sta os.lc314
            sta os.lc319
            lda #$FF
            sta os.lc315
            sta os.lc31A
            lda #$FE
            sta os.lc4F9
            lda #$EA
            sta os.lc410
            sta os.lc411
            sta os.lc412
            sta os.lc487
            sta os.lc488
            sta os.lc489
            sta os.lc48A
            sta os.lc48B
            sta os.lc48C
            sta os.lc48D
            sta os.lc48E
            sta os.lc4A1
            sta os.lc4A2
            sta os.lc4A3
            sta os.lc4A4
            sta os.lc4A5
            sta os.lc4A6
            sta os.lc4A7
            sta os.lc4A8
            sta os.lc4ED
            sta os.lc4EE
            sta os.lc4EF
            sta os.lc4FA
            sta os.lc4FB
            sta os.lc4FC
            lda #$A9
            sta os.LC901
            lda #$7F
            sta os.LC902
            lda #$EA
            sta os.LC903
            sta os.LC904
            sta os.LC905
            ldy L00FB
            beq L29F3
            sta os.lc41A
            sta os.lc41B
L29F3       lda #$C0
            sta os.lc4AF
            lda #$60
            sta os.lc4B2
            rts
L29FE       ldx siov+1
            lda siov+2
            stx unknown_atr.l2aa6+1
            sta unknown_atr.l2aa6+2
            ldy L00FA
            bne L2A14
            ldx #$00
            lda #$CC
            bne L2A18
L2A14       ldx #$00
            lda #$C1
L2A18       stx L00FE
            sta L00FF
            stx siov+1
            sta siov+2
            lda #$96
            sta L00FC
            lda #$2A
            sta L00FD
            ldx #$02
            ldy #$00
L2A2E       lda (L00FC),Y
            sta (L00FE),Y
            iny
            bne L2A2E
            inc L00FD
            inc L00FF
            dex
            bne L2A2E
            rts
L2A3D       ldy #$01
            lda (L0084),Y
            tax
            ldy L00FA
            bne L2A60
            stx os.LCC80
            lda #$2F
            sta os.LCCC6
            lda #$CD
            sta os.LCCCA
            ldy #$1F
            sty os.LCCC1
            lda #$CD
            sta os.LCCC2
            jmp L2A77
L2A60       stx os.lc180
            lda #$2F
            sta os.lc1C6
            lda #$C2
            sta os.lc1CA
            ldy #$1F
            sty os.lc1C1
            lda #$C2
            sta os.lc1C2
L2A77       sty L00FE
            sta L00FF
            ldy #$00
L2A7D       lda L36D8,X
            sta (L00FE),Y
            inx
            iny
            cpy #$10
            bne L2A7D
            rts
L2A89       lda L00FB
            beq L2A95
            lda PORTB
            and #$FD
            sta PORTB
L2A95       rts
	.endp
