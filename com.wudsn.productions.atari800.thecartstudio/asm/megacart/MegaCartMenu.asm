;	@com.wudsn.ide.asm.outputfileextension=.rom

	opt h-
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
ICHID       = $0000
ICDNO       = $0001
ICCOM       = $0002
ICSTA       = $0003
ICBAL       = $0004
ICBAH       = $0005
ICPTL       = $0006
ICPTH       = $0007
ICBLL       = $0008
ICBLH       = $0009
ICAX1       = $000A
ICAX2       = $000B
ICAX3       = $000C
ICAX4       = $000D
ICAX5       = $000E
ICAX6       = $000F
; 
; DISPLAY LIST EQUATES
; 
ADLI        = $0080
AVB         = $0040
ALMS        = $0040
AVSCR       = $0020
AHSCR       = $0010
AJMP        = $0001
AEMPTY1     = $0000
AEMPTY2     = $0010
AEMPTY3     = $0020
AEMPTY4     = $0030
AEMPTY5     = $0040
AEMPTY6     = $0050
AEMPTY7     = $0060
AEMPTY8     = $0070
; 
; OS VARIABLES FOR XL/XE
; 
; PAGE 0
; 
NGFLAG      = $0001
CASINI      = $0002
RAMLO       = $0004
TRAMSZ      = $0006
CMCMD       = $0007
WARMST      = $0008
BOOT        = $0009
DOSVEC      = $000A
DOSINI      = $000C
APPMHI      = $000E
POKMSK      = $0010
BRKKEY      = $0011
RTCLOK      = $0012
BUFADR      = $0015
ICCOMT      = $0017
DSKFMS      = $0018
DSKUTL      = $001A
ABUFPT      = $001C
ICHIDZ      = $0020
ICDNOZ      = $0021
ICCOMZ      = $0022
ICSTAZ      = $0023
ICBALZ      = $0024
ICBAHZ      = $0025
ICPTLZ      = $0026
ICPTHZ      = $0027
ICBLLZ      = $0028
ICBLHZ      = $0029
ICAX1Z      = $002A
ICAX2Z      = $002B
ICAX3Z      = $002C
ICAX4Z      = $002D
ICAX5Z      = $002E
ICAX6Z      = $002F
STATUS      = $0030
CHKSUM      = $0031
BUFRLO      = $0032
BUFRHI      = $0033
BFENLO      = $0034
BFENHI      = $0035
LTEMP       = $0036
BUFRFL      = $0038
RECVDN      = $0039
XMTDON      = $003A
CHKSNT      = $003B
NOCKSM      = $003C
BPTR        = $003D
FTYPE       = $003E
FEOF        = $003F
FREQ        = $0040
SOUNDR      = $0041
CRITIC      = $0042
FMSZPG      = $0043
ZCHAIN      = $004A
DSTAT       = $004C
ATRACT      = $004D
DRKMSK      = $004E
COLRSH      = $004F
TEMP        = $0050
HOLD1       = $0051
LMARGN      = $0052
RMARGN      = $0053
ROWCRS      = $0054
COLCRS      = $0055
DINDEX      = $0057
SAVMSC      = $0058
OLDROW      = $005A
OLDCOL      = $005B
OLDCHR      = $005D
OLDADR      = $005E
FKDEF       = $0060
PALNTS      = $0062
LOGCOL      = $0063
ADRESS      = $0064
MLTTMP      = $0066
SAVADR      = $0068
RAMTOP      = $006A
BUFCNT      = $006B
BUFSTR      = $006C
BITMSK      = $006E
SHFAMT      = $006F
ROWAC       = $0070
COLAC       = $0072
ENDPT       = $0074
DELTAR      = $0076
DELTAC      = $0077
KEYDEF      = $0079
SWPFLG      = $007B
HOLDCH      = $007C
INSDAT      = $007D
COUNTR      = $007E
LOMEM       = $0080
; 
; PAGE 2
; 
VDSLST      = $0200
VPRCED      = $0202
VINTER      = $0204
VBREAK      = $0206
VKEYBD      = $0208
VSERIN      = $020A
VSEROR      = $020C
VSEROC      = $020E
VTIMR1      = $0210
VTIMR2      = $0212
VTIMR4      = $0214
VIMIRQ      = $0216
CDTMV1      = $0218
CDTMV2      = $021A
CDTMV3      = $021C
CDTMV4      = $021E
CDTMV5      = $0220
VVBLKI      = $0222
VVBLKD      = $0224
CDTMA1      = $0226
CDTMA2      = $0228
CDTMF3      = $022A
SRTIMR      = $022B
CDTMF4      = $022C
INTEMP      = $022D
CDTMF5      = $022E
SDMCTL      = $022F
SDLSTL      = $0230
SDLSTH      = $0231
SSKCTL      = $0232
SPARE       = $0233
LPENH       = $0234
LPENV       = $0235
BRKKY       = $0236
VPIRQ       = $0238
CDEVIC      = $023A
CCOMND      = $023B
CAUX1       = $023C
CAUX2       = $023D
TMPSIO      = $023E
ERRFLG      = $023F
DFLAGS      = $0240
DBSECT      = $0241
BOOTAD      = $0242
COLDST      = $0244
RECLEN      = $0245
DSKTIM      = $0246
PDVMSK      = $0247
SHPDVS      = $0248
PDMSK       = $0249
RELADR      = $024A
PPTMPA      = $024C
PPTMPX      = $024D
CHSALT      = $026B
VSFLAG      = $026C
KEYDIS      = $026D
FINE        = $026E
GPRIOR      = $026F
PADDL0      = $0270
PADDL1      = $0271
PADDL2      = $0272
PADDL3      = $0273
PADDL4      = $0274
PADDL5      = $0275
PADDL6      = $0276
PADDL7      = $0277
STICK0      = $0278
STICK1      = $0279
STICK2      = $027A
STICK3      = $027B
PTRIG0      = $027C
PTRIG1      = $027D
PTRIG2      = $027E
PTRIG3      = $027F
PTRIG4      = $0280
PTRIG5      = $0281
PTRIG6      = $0282
PTRIG7      = $0283
STRIG0      = $0284
STRIG1      = $0285
STRIG2      = $0286
STRIG3      = $0287
HIBYTE      = $0288
WMODE       = $0289
BLIM        = $028A
IMASK       = $028B
JVECK       = $028C
NEWADR      = $028E
TXTROW      = $0290
TXTCOL      = $0291
TINDEX      = $0293
TXTMSC      = $0294
TXTOLD      = $0296
CRETRY      = $029C
HOLD3       = $029D
SUBTMP      = $029E
HOLD2       = $029F
DMASK       = $02A0
TMPLBT      = $02A1
ESCFLG      = $02A2
TABMAP      = $02A3
LOGMAP      = $02B2
INVFLG      = $02B6
FILFLG      = $02B7
TMPROW      = $02B8
TMPCOL      = $02B9
SCRFLG      = $02BB
HOLD4       = $02BC
DRETRY      = $02BD
SHFLOC      = $02BE
BOTSCR      = $02BF
PCOLR0      = $02C0
PCOLR1      = $02C1
PCOLR2      = $02C2
PCOLR3      = $02C3
COLOR0      = $02C4
COLOR1      = $02C5
COLOR2      = $02C6
COLOR3      = $02C7
COLOR4      = $02C8
RUNADR      = $02C9
HIUSED      = $02CB
ZHIUSE      = $02CD
GBYTEA      = $02CF
LOADAD      = $02D1
ZLOADA      = $02D3
DSCTLN      = $02D5
ACMISR      = $02D7
KRPDER      = $02D9
KEYREP      = $02DA
NOCLIK      = $02DB
HELPFG      = $02DC
DMASAV      = $02DD
PBPNT       = $02DE
PBUFSZ      = $02DF
RUNAD       = $02E0
INITAD      = $02E2
RAMSIZ      = $02E4
MEMTOP      = $02E5
MEMLO       = $02E7
HNDLOD      = $02E9
DVSTAT      = $02EA
CBAUDL      = $02EE
CBAUDH      = $02EF
CRSINH      = $02F0
KEYDEL      = $02F1
CH1         = $02F2
CHACT       = $02F3
CHBAS       = $02F4
NEWROW      = $02F5
NEWCOL      = $02F6
ROWINC      = $02F8
COLINC      = $02F9
CHAR        = $02FA
ATACHR      = $02FB
CH          = $02FC
FILDAT      = $02FD
DSPFLG      = $02FE
SSFLAG      = $02FF
; 
; PAGE 3
; 
DDEVIC      = $0300
DUNIT       = $0301
DCOMND      = $0302
DSTATS      = $0303
DBUFLO      = $0304
DBUFHI      = $0305
DTIMLO      = $0306
DUNUSE      = $0307
DBYTLO      = $0308
DBYTHI      = $0309
DAUX1       = $030A
DAUX2       = $030B
TIMER1      = $030C
ADDCOR      = $030E
CASFLG      = $030F
TIMER2      = $0310
TEMP1       = $0312
TEMP2       = $0314
TEMP3       = $0315
SAVIO       = $0316
TIMFLG      = $0317
STACKP      = $0318
TSTAT       = $0319
HATABS      = $031A
PUPBT1      = $033D
PUPBT2      = $033E
PUPBT3      = $033F
IOCB0       = $0340
IOCB1       = $0350
IOCB2       = $0360
IOCB3       = $0370
IOCB4       = $0380
IOCB5       = $0390
IOCB6       = $03A0
IOCB7       = $03B0
PRNBUF      = $03C0
SUPERF      = $03E8
CKEY        = $03E9
CASSBT      = $03EA
CARTCK      = $03EB
DERRF       = $03EC
ACMVAR      = $03ED
BASICF      = $03F8
MINTLK      = $03F9
GINTLK      = $03FA
CHLINK      = $03FB
CASBUF      = $03FD
; 
; HARDWARE REGISTERS
; 
; GTIA
; 
M0PF        = $D000
HPOSP0      = $D000
M1PF        = $D001
HPOSP1      = $D001
M2PF        = $D002
HPOSP2      = $D002
M3PF        = $D003
HPOSP3      = $D003
P0PF        = $D004
HPOSM0      = $D004
P1PF        = $D005
HPOSM1      = $D005
P2PF        = $D006
HPOSM2      = $D006
P3PF        = $D007
HPOSM3      = $D007
M0PL        = $D008
SIZEP0      = $D008
M1PL        = $D009
SIZEP1      = $D009
M2PL        = $D00A
SIZEP2      = $D00A
M3PL        = $D00B
SIZEP3      = $D00B
P0PL        = $D00C
SIZEM       = $D00C
P1PL        = $D00D
GRAFP0      = $D00D
P2PL        = $D00E
GRAFP1      = $D00E
P3PL        = $D00F
GRAFP2      = $D00F
TRIG0       = $D010
GRAFP3      = $D010
TRIG1       = $D011
GRAFM       = $D011
TRIG2       = $D012
COLPM0      = $D012
TRIG3       = $D013
COLPM1      = $D013
PAL         = $D014
COLPM2      = $D014
COLPM3      = $D015
COLPF0      = $D016
COLPF1      = $D017
COLPF2      = $D018
COLPF3      = $D019
COLBK       = $D01A
PRIOR       = $D01B
VDELAY      = $D01C
GRACTL      = $D01D
HITCLR      = $D01E
CONSOL      = $D01F
; 
; POKEY
; 
POT0        = $D200
AUDF1       = $D200
POT1        = $D201
AUDC1       = $D201
POT2        = $D202
AUDF2       = $D202
POT3        = $D203
AUDC2       = $D203
POT4        = $D204
AUDF3       = $D204
POT5        = $D205
AUDC3       = $D205
POT6        = $D206
AUDF4       = $D206
POT7        = $D207
AUDC4       = $D207
ALLPOT      = $D208
AUDCTL      = $D208
KBCODE      = $D209
STIMER      = $D209
RANDOM      = $D20A
SKREST      = $D20A
POTGO       = $D20B
SERIN       = $D20D
SEROUT      = $D20D
IRQST       = $D20E
IRQEN       = $D20E
SKSTAT      = $D20F
SKCTL       = $D20F
; 
; PIA
; 
PORTA       = $D300
PORTB       = $D301
PACTL       = $D302
PBCTL       = $D303
; 
; ANTIC
; 
DMACLT      = $D400
CHACTL      = $D401
DLISTL      = $D402
DLISTH      = $D403
HSCROL      = $D404
VSCROL      = $D405
PMBASE      = $D407
CHBASE      = $D409
WSYNC       = $D40A
VCOUNT      = $D40B
PENH        = $D40C
PENV        = $D40D
NMIEN       = $D40E
NMIST       = $D40F
NMIRES      = $D40F
; 
; FLOATING POINT ROUTINES
; 
AFP         = $D800
FASC        = $D8E6
IFP         = $D9AA
FPI         = $D9D2
ZFR0        = $DA44
ZF1         = $DA46
FSUB        = $DA60
FADD        = $DA66
FMUL        = $DADB
FDIV        = $DB28
PLYEVL      = $DD40
FLD0R       = $DD89
FLD0P       = $DD8D
FLD1R       = $DD98
FLD1P       = $DD9C
FSTOR       = $DDA7
FSTOP       = $DDAB
FMOVE       = $DDB6
EXP         = $DDC0
EXP10       = $DDCC
LOG         = $DECD
LOG10       = $DED1
; 
; ROM VECTORS
; 
DSKINV      = $E453
CIOV        = $E456
SIOV        = $E459
SETVBV      = $E45C
SYSVBV      = $E45F
XITVBV      = $E462
SIOINV      = $E465
SENDEV      = $E468
INTINV      = $E46B
CIOINV      = $E46E
SELFSV      = $E471
WARMSV      = $E474
COLDSV      = $E477
RBLOKV      = $E47A
CSOPIV      = $E47D
PUPDIV      = $E480
SELFTSV     = $E483
PENTV       = $E486
PHUNLV      = $E489
PHINIV      = $E48C
GPDVV       = $E48F
;
; Code equates
;
L0400       = $0400
L0401       = $0401
L0402       = $0402
L0404       = $0404
L0405       = $0405
L0406       = $0406
L0407       = $0407
L0408       = $0408
L040A       = $040A
L042C       = $042C
L043C       = $043C
L045C       = $045C
L04A3       = $04A3
L04DE       = $04DE
L04E8       = $04E8
L04F5       = $04F5
L0500       = $0500
L0545       = $0545
L0548       = $0548
L055B       = $055B
L0566       = $0566
L057B       = $057B
L057C       = $057C
L057D       = $057D
L057E       = $057E
L0600       = $0600
L060B       = $060B
L060C       = $060C
L0645       = $0645
L0688       = $0688
L06A9       = $06A9
L06B5       = $06B5
L0700       = $0700
L0701       = $0701
L0702       = $0702
L0703       = $0703
L0704       = $0704
L0705       = $0705
L0706       = $0706
L0707       = $0707
L0708       = $0708
L070A       = $070A
L072C       = $072C
L0736       = $0736
L0756       = $0756
L079D       = $079D
L07C6       = $07C6
L07D0       = $07D0
L07DD       = $07DD
L0800       = $0800
L082D       = $082D
L0830       = $0830
L0843       = $0843
L084E       = $084E
L0863       = $0863
L0864       = $0864
L0865       = $0865
L0866       = $0866
L7C20       = $7C20
L7C21       = $7C21
L7C22       = $7C22
L7C23       = $7C23
L7C24       = $7C24
old_dli_L7C25       = $7C25
old_dma_L7C27       = $7C27
L7C40       = $7C40
L7EC0       = $7EC0
L7ED4       = $7ED4
L7ED5       = $7ED5
L7ED7       = $7ED7
L7ED8       = $7ED8
L8003       = $8003
LB200       = $B200
LB400       = $B400
LB580       = $B580
LB680       = $B680
LC2F6       = $C2F6
LC2F7       = $C2F7
LC2F8       = $C2F8
LC2F9       = $C2F9
LC2FA       = $C2FA
LC30B       = $C30B
LC30C       = $C30C
LC410       = $C410
LC49F       = $C49F
LC4C0       = $C4C0
LC4C1       = $C4C1
LC4C2       = $C4C2
LC4C3       = $C4C3
LC4C4       = $C4C4
LC4C5       = $C4C5
LC4DD       = $C4DD
LC4ED       = $C4ED
LC4F9       = $C4F9
LC933       = $C933
LC934       = $C934
LC935       = $C935
LC938       = $C938
LCC00       = $CC00
LCC01       = $CC01
LCC8C       = $CC8C
LCC92       = $CC92
LCCA6       = $CCA6
LCCB1       = $CCB1
LCCC3       = $CCC3
LCCC4       = $CCC4
LCCF5       = $CCF5
LCCF6       = $CCF6
LCD00       = $CD00
LCD25       = $CD25
LCDC0       = $CDC0
LCDCD       = $CDCD
LCE00       = $CE00
LCE11       = $CE11
LCE29       = $CE29
LCE31       = $CE31
LCE86       = $CE86
LCE87       = $CE87
LCEF1       = $CEF1
LCEF2       = $CEF2
LCEF3       = $CEF3
LCEF4       = $CEF4
LCEF5       = $CEF5
LCEF6       = $CEF6
LCEF7       = $CEF7
LCEF8       = $CEF8
LCEF9       = $CEF9
LCF00       = $CF00
LD500       = $D500
LD5A1       = $D5A1
LD5A2       = $D5A2
LD5A3       = $D5A3
LD5A5       = $D5A5
LD5A6       = $D5A6
LD5A7       = $D5A7
LFC67       = $FC67
LFC68       = $FC68
LFC69       = $FC69
LFC6A       = $FC6A
;
; Start of code
;
            org $B000
;
LB000
	.proc LB000_0600
	       lda SDMCTL
            sta old_dma_L7C27
            lda #$00
            sta SDMCTL
            sta DMACLT
            inc CRITIC
            sei
            lda #$FF
            sta PORTB
            sta LD500
            lda TRIG3
            sta GINTLK
            lda TRIG3
            sta GINTLK
            dec CRITIC
            lda old_dma_L7C27
            sta SDMCTL
            sta DMACLT
            cli
            lda #$C0
            sta RAMSIZ
            sta RAMTOP
            jsr L06A9
            jsr L0688
            ldx #$40
LB040       ldy #$00
            tya
LB043       sta L7C20,Y
            iny
            bne LB043
            inc L0645
            dex
            bne LB040
            lda #$00
            sta SDMCTL
            lda RTCLOK+2
LB056       cmp RTCLOK+2
            beq LB056
            lda SDMCTL
            sta old_dma_L7C27
            lda #$00
            sta SDMCTL
            sta DMACLT
            inc CRITIC
            sei
            lda #$00
            sta LD500
            sta FMSZPG+6
            lda #$00
            sta COLDST
            lda #$01
            sta BOOT
            lda #>COLDSV
            sta DOSINI+1
            sta DOSVEC+1
            lda #<COLDSV
            sta DOSVEC
            sta DOSINI
            rts
            ldx #$10
            lda #$03
            sta IOCB0+ICCOM,X
            lda #$A6
            sta IOCB0+ICBAL,X
            lda #$06
            sta IOCB0+ICBAH,X
            lda #$0C
            sta IOCB0+ICAX1,X
            lda #$00
            sta IOCB0+ICAX2,X
            jmp CIOV

            .byte 'E:'
            .byte $9B

            ldx #$10
            jsr L06B5
            ldx #$20
            jsr L06B5
            ldx #$30
            lda #$0C
            sta IOCB0+ICCOM,X
            jmp CIOV
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00
            .endp

LB100       .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            lda #$22
            sta SDMCTL
            lda RTCLOK+2
LB111       cmp RTCLOK+2
            beq LB111
            lda #$22
            sta L0708
            lda VVBLKI
            sta L0863
            sta L0865
            lda VVBLKI+1
            sta L0864
            sta L0866
            lda L0702
            sta LD500
            ldy #$00
            lda (FMSZPG+2),Y
            cmp #$FF
            bne LB148
            tax
            jsr L079D
            cmp #$FF
            bne LB152
            jsr L079D
            jmp L0736
LB148       sta FMSZPG+4
            jsr L079D
            sta FMSZPG+5
            jmp L0756
LB152       sta FMSZPG+5
            stx FMSZPG+4
            jsr L079D
            sta L0700
            jsr L079D
            sta L0701
            lda L0707
            bne LB174
            lda FMSZPG+4
            sta RUNAD
            lda FMSZPG+5
            sta RUNAD+1
            inc L0707
LB174       jsr L079D
            sta (FMSZPG+4),Y
            jsr L07D0
            beq LB186
LB17E       jsr L079D
            jsr L07C6
            bne LB17E
LB186       jsr L07DD
            jsr L084E
            bne LB197
            jsr L0830
            jsr L0843
            jmp (RUNAD)
LB197       jsr L079D
            jmp L072C
            lda L0702
            sta LD500
            inc FMSZPG+2
            bne LB1BC
            inc FMSZPG+3
            lda FMSZPG+3
            cmp #$C0
            bne LB1BC
            lda #$80
            sta FMSZPG+3
            inc L0702
            lda L0702
            sta LD500
LB1BC       lda (FMSZPG+2),Y
            pha
            lda #$FF
            sta LD500
            pla
            rts
            pha
            inc FMSZPG+4
            bne LB1CD
            inc FMSZPG+5
LB1CD       pla
            sta (FMSZPG+4),Y
            lda FMSZPG+5
            cmp L0701
            bne LB1DC
            lda FMSZPG+4
            cmp L0700
LB1DC       rts
            lda FMSZPG+5
            cmp #$02
            bne LB22C
            lda FMSZPG+4
            cmp #$E3
            bne LB22C
            jsr L0830
            ldx L0866
            cpx L0864
            beq LB1FC
            lda #$06
            ldy L0865
            jsr SETVBV
LB1FC       jsr L082D
            lda SDMCTL
            sta L0708
            lda VVBLKI+1
            cmp L0864
            beq LB221
            sta L0866
            lda VVBLKI
            sta L0865
            lda #$06
            ldy L0863
            ldx L0864
            jsr SETVBV
LB221       lda #$00
            sta SDMCTL
            sta DMACLT
            inc CRITIC
            sei
LB22C       rts
            jmp (INITAD)
            lda TRIG3
            sta GINTLK
            dec CRITIC
            lda L0708
            sta SDMCTL
            sta DMACLT
            cli
            rts
            ldx #$80
            ldy #$00
LB247       dey
            bne LB247
            dex
            bne LB247
            rts
            lda L0702
            cmp L0706
            bne LB262
            lda FMSZPG+3
            cmp L0705
            bne LB262
            lda FMSZPG+2
            cmp L0704
LB262       rts
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
LB300       .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            lda #$22
            sta SDMCTL
            lda RTCLOK+2
LB311       cmp RTCLOK+2
            beq LB311
            lda #$22
            sta L0408
            lda VVBLKI
            sta L057B
            sta L057D
            lda VVBLKI+1
            sta L057C
            sta L057E
            lda L0402
            sta LD5A6
            sta LD5A1
            sta LD500
            ldy #$00
            lda (FMSZPG+2),Y
            cmp #$FF
            bne LB34E
            tax
            jsr L04A3
            cmp #$FF
            bne LB358
            jsr L04A3
            jmp L043C
LB34E       sta FMSZPG+4
            jsr L04A3
            sta FMSZPG+5
            jmp L045C
LB358       sta FMSZPG+5
            stx FMSZPG+4
            jsr L04A3
            sta L0400
            jsr L04A3
            sta L0401
            lda L0407
            bne LB37A
            lda FMSZPG+4
            sta RUNAD
            lda FMSZPG+5
            sta RUNAD+1
            inc L0407
LB37A       jsr L04A3
            sta (FMSZPG+4),Y
            jsr L04E8
            beq LB38C
LB384       jsr L04A3
            jsr L04DE
            bne LB384
LB38C       jsr L04F5
            jsr L0566
            bne LB39D
            jsr L0548
            jsr L055B
            jmp (RUNAD)
LB39D       jsr L04A3
            jmp L042C
            lda L0402
            sta LD500
            sta LD5A6
            sta LD5A1
            inc FMSZPG+2
            bne LB3CE
            inc FMSZPG+3
            lda FMSZPG+3
            cmp #$C0
            bne LB3CE
            lda #$80
            sta FMSZPG+3
            inc L0402
            lda L0402
            sta LD500
            sta LD5A3
            sta LD5A7
LB3CE       lda (FMSZPG+2),Y
            pha
            lda #$FF
            sta LD500
            sta LD5A5
            sta LD5A2
            pla
            rts
            pha
            inc FMSZPG+4
            bne LB3E5
            inc FMSZPG+5
LB3E5       pla
            sta (FMSZPG+4),Y
            lda FMSZPG+5
            cmp L0401
            bne LB3F4
            lda FMSZPG+4
            cmp L0400
LB3F4       rts
            lda FMSZPG+5
            cmp #$02
            bne LB444
            lda FMSZPG+4
            cmp #$E3
            bne LB444
            jsr L0548
            ldx L057E
            cpx L057C
            beq LB414
            lda #$06
            ldy L057D
            jsr SETVBV
LB414       jsr L0545
            lda SDMCTL
            sta L0408
            lda VVBLKI+1
            cmp L057C
            beq LB439
            sta L057E
            lda VVBLKI
            sta L057D
            lda #$06
            ldy L057B
            ldx L057C
            jsr SETVBV
LB439       lda #$00
            sta SDMCTL
            sta DMACLT
            inc CRITIC
            sei
LB444       rts
            jmp (INITAD)
            lda TRIG3
            sta GINTLK
            dec CRITIC
            lda L0408
            sta SDMCTL
            sta DMACLT
            cli
            rts
            ldx #$80
            ldy #$00
LB45F       dey
            bne LB45F
            dex
            bne LB45F
            rts
            lda L0402
            cmp L0406
            bne LB47A
            lda FMSZPG+3
            cmp L0405
            bne LB47A
            lda FMSZPG+2
            cmp L0404
LB47A       rts
            .byte $00,$00,$00,$00,$00
LB480       lda DDEVIC
            cmp #$31
            bne LB48E
            lda DUNIT
            cmp #$01
            beq LB493
LB48E       inc CRITIC
            jmp LC938
LB493       lda LCE31
            bne LB48E
            lda #$80
            sta BUFRLO
            lda #$CE
            sta BUFRHI
            ldx LCEF8
            beq LB4AF
            lda BUFRLO
            clc
LB4A8       adc #$09
            sta BUFRLO
            dex
            bne LB4A8
LB4AF       lda DCOMND
            cmp #$53
            bne LB4B9
            jmp LCDCD
LB4B9       cmp #$50
            bne LB4C0
LB4BD       jmp LCC92
LB4C0       cmp #$57
            beq LB4BD
            cmp #$52
            beq LB4CE
            ldy #$F0
            sty DSTATS
            rts
LB4CE       lda DAUX1
            ora DAUX2
            bne LB4E1
LB4D6       jmp LCCA6
LB4D9       lda LCE86
            cmp DAUX1
            bcs LB4EB
LB4E1       lda LCE87
            cmp DAUX2
            beq LB4D9
            bcc LB4D6
LB4EB       lda DAUX2
            bne LB51D
            lda DAUX1
            cmp #$04
            bcs LB51D
            ldy #$00
            jsr LCDC0
            lda DAUX1
            sta LCEF1
            lda DAUX2
            sta LCEF2
            dec LCEF1
            clc
            jsr LCD25
            jsr LCCB1
            ldx #$FE
            lda #$00
            ldy #$01
            sty DSTATS
            sec
            rts
LB51D       ldy #$03
            jsr LCDC0
            sec
            jmp LCC8C
            ldx #$FE
            lda #$00
            ldy #$8B
            sty DSTATS
            sec
            rts
            inc CRITIC
            ldy #$00
            ldx BFENLO
LB537       lda VCOUNT
            cmp LCEF9
            bne LB537
            stx LD500
LB542       lda LCF00,Y
            sta LCF00,Y
            iny
            bpl LB542
            lda #$FF
            sta LD500
            lda DBYTHI
            beq LB584
            lda LCCF6
            cmp #$BF
            bne LB569
            lda LCCF5
            cmp #$80
            bne LB569
            lda #$7F
            sta LCCF6
            inx
LB569       lda VCOUNT
            cmp LCEF9
            bne LB569
            stx LD500
LB574       lda LCF00,Y
            sta LCF00,Y
            iny
            bmi LB574
            lda #$FF
            sta LD500
            lda #$00
LB584       sta CRITIC
            lda DBUFLO
            sta BUFRLO
            lda DBUFHI
            sta BUFRHI
            ldy #$00
            ldx #$80
            lda DBYTHI
            beq LB59B
            ldx #$00
LB59B       lda LCF00,Y
            sta (BUFRLO),Y
            iny
            dex
            bne LB59B
            rts
            bcc LB5C3
            lda DAUX1
            sbc #$04
            sta LCEF1
            lda DAUX2
            sbc #$00
            sta LCEF2
            lda #$00
            sta LCEF4
            ldy #$08
            lda (BUFRLO),Y
            lsr
            bcs LB5D5
LB5C3       lda #$00
            sta LCEF4
            lsr LCEF2
            ror LCEF1
            bcc LB5D5
            lda #$80
            sta LCEF4
LB5D5       lda #$00
            sta LCEF3
            ldy #$06
LB5DC       lsr LCEF2
            ror LCEF1
            ror LCEF3
            dey
            bne LB5DC
            lsr LCEF3
            lsr LCEF3
            ldx LCEF1
            ldy #$01
            lda BFENLO,Y
            and #$7F
            sta LCEF5
            dey
            lda BFENLO,Y
            clc
            adc LCEF4
            sta LCEF1
            lda LCEF5
            adc LCEF3
            sta LCEF2
            and #$40
            beq LB614
            inx
LB614       lda LCEF2
            and #$3F
            ora #$80
            sta LCEF2
            txa
            ldy #$02
            clc
            adc BFENLO,Y
            sta LCEF3
            lda LCEF1
            sta LCCC3
            sta LCCF5
            lda LCEF2
            sta LCCC4
            sta LCCF6
            lda LCEF3
            sta BFENLO
            rts
            ldx #$00
LB642       lda (BUFRLO),Y
            sta BFENLO,X
            iny
            inx
            cpx #$03
            bne LB642
            rts
            ldy #$08
            lda (BUFRLO),Y
            and #$FE
            sta DVSTAT
            lda #$FF
            sta DVSTAT+1
            lda #$01
            sta DVSTAT+2
            lda #$00
            sta DVSTAT+3
            sta DSTATS
            tay
            rts
            stx LCEF6
            txa
            ldx LCEF7
LB671       cmp LCE29,X
            beq LB687
            dex
            bpl LB671
            cmp #$F0
            beq LB697
            cmp #$F4
            beq LB69E
            ldx LCEF6
            jmp LCE11
LB687       stx LCEF8
            lda #$00
            sta LCE31
LB68F       ldx #$FF
            stx CH
            jmp LFC6A
LB697       lda #$0B
            sta LCE31
            bne LB68F
LB69E       lda #$00
            sta LCE31
            sta LCEF8
            jmp COLDSV
            .byte $DF,$DE,$DA,$D8,$DD,$DB,$F3,$F5,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00
LB700       lda RAMTOP
            cmp #$80
            beq LB713
            lda #$36
            sta SDLSTL
            lda #$BE
            sta SDLSTH
LB710       jmp LB710
LB713       lda CONSOL
            cmp #$03
            bne LB73E
            lda #$00
            sta SDMCTL
            sta DMACLT
            inc CRITIC
            sei
            ldy #$40
LB727       lda LB738,Y
            sta L0600,Y
            dey
            bpl LB727
            lda #$FF
            sta COLDST
            jmp L0600
LB738       sta LD500
            jmp COLDSV
LB73E       lda #$00
            sta NMIEN
            jsr LBD65
            lda #$01
            sta BASICF
            lda VDSLST
            sta old_dli_L7C25
            lda VDSLST+1
            sta old_dli_L7C25+1
            lda #$70
            sta VDSLST
            lda #$BD
            sta VDSLST+1
            lda #$C5
            sta SDLSTL
            sta DLISTL
            lda #$BE
            sta SDLSTH
            sta DLISTH
            lda #$C0
            sta NMIEN
            lda #$00
            sta L0708
            sta L7C20
            sta L7C21
            sta L7C22
            sta L7C23
            sta COLOR2
            sta L7C24
            lda #$3E
            sta COLOR1
            jsr LBD19
            jsr LBD04
            jsr LBCC8
            jsr LBCEC
            jsr LBC89
            jsr LBC53
            ldx #$00
            jsr LBCF9
            jsr LBC3B
            jsr LBB57
            bmi LB7ED
LB7B1       lda #$FF
            sta CH
LB7B6       lda CH
            ldx #$0F
LB7BB       cmp LBDDE,X
            beq LB7ED
            dex
            bpl LB7BB
            ldx #$03
LB7C5       cmp LBDEE,X
            beq LB7CF
            dex
            bpl LB7C5
            bmi LB7DC
LB7CF       lda LBDF2,X
            sta L0708
            lda #$FF
            sta CH
            bmi LB7E0
LB7DC       cmp #$0C
            beq LB7EA
LB7E0       jsr LBB8B
            lda TRIG0
            eor #$01
            beq LB7B6
LB7EA       ldx L7C20
LB7ED       stx L7C20
            jsr LBB61
test        jsr LBCF5
            ldx L7C21
            cpx #$00
            beq LB800
            jsr LBC19
LB800       ldx L7C20
            cpx #$00
            beq LB81D
            jsr LBC2A
            ldy #$07
            lda (FMSZPG+4),Y
            cmp #$FF
            beq LB81A
            ldy #$03
            lda (FMSZPG+4),Y
            and #$20
            beq LB81D
LB81A       jmp LB7B1
LB81D       ldy #$01
LB81F       lda (FMSZPG+4),Y
            sta FMSZPG+2,Y
            dey
            bpl LB81F
            ldy #$02
            lda (FMSZPG+4),Y
            sta L0702
            iny
            lda (FMSZPG+4),Y
            sta L0703
            iny
            lda (FMSZPG+4),Y
            sta L0704
            iny
            lda (FMSZPG+4),Y
            sta L0705
            iny
            lda (FMSZPG+4),Y
            sta L0706
            lda #$FF
            sta CH
            lda #$00
            sta ATRACT
            sta NMIEN
            jsr LBD65
            lda old_dli_L7C25
            sta VDSLST
            lda old_dli_L7c25+1
            sta VDSLST+1
            lda #$C0
            sta NMIEN
            ldy #$00
LB868       lda LB000,Y
            sta L0600,Y
            dey
            bne LB868
            jsr L0600
            ldy #$00
            tya
LB877       sta L0600,Y
            dey
            bne LB877
            ldy #$0A
            ldx L0703
            txa
            and #$10
            bne LB8EC
            txa
            and #$08
            bne LB8D0
            txa
            and #$0F
            bne LB8A6
LB891       lda LB100,Y
            sta L0700,Y
            iny
            bne LB891
LB89A       lda LB200,Y
            sta L0800,Y
            iny
            bne LB89A
            jmp L070A
LB8A6       lda LB300,Y
            sta L0400,Y
            iny
            bne LB8A6
LB8AF       lda LB400,Y
            sta L0500,Y
            iny
            bne LB8AF
            ldy #$09
            ldx #$09
LB8BC       lda L0700,Y
            sta L0400,Y
            dey
            bpl LB8BC
            lda #$00
LB8C7       sta L0700,X
            dex
            bpl LB8C7
            jmp L040A
LB8D0       ldy #$40
            ldx L0702
LB8D5       lda LB738,Y
            sta L0400,Y
            dey
            bpl LB8D5
            ldy #$14
            lda #$00
LB8E2       sta L0700,Y
            dey
            bpl LB8E2
            txa
            jmp L0400
LB8EC       ldy #$00
            lda LCC00
            clc
LB8F2       adc LCC01,Y
            iny
            bne LB8F2
LB8F8       adc LC933,Y
            iny
            cpy #$20
            bne LB8F8
            cmp #$E6
            beq LB942
            ldy #$00
LB906       lda LBE92,Y
            sta L0600,Y
            lda LB91B,Y
            sta L0700,Y
            iny
            bne LB906
            jsr LBC77
            jmp L0700
LB91B       lda #$00
            sta SDLSTL
            sta DLISTL
            lda #$06
            sta SDLSTH
            sta DLISTH
            lda #$FF
            sta LD500
            lda #$22
            sta SDMCTL
            sta DMACLT
            lda #$40
            sta NMIEN
            cli
            lda #$00
LB940       beq LB940
LB942       jsr LBABB
            jsr LBA57
            lda L0703
            and #$07
            sta L0703
            sta LCEF7
            lda #$80
            sta BUFRLO
            lda #$CE
            sta BUFRHI
LB95B       jsr LBA7A
            jsr LB997
            ldx #$01
            jsr LBC2A
            clc
            lda BUFRLO
            adc #$09
            sta BUFRLO
            dec L0703
            bpl LB95B
            lda #$4C
            sta LC933
            lda #$00
            sta LC934
            lda #$CC
            sta LC935
            ldy #$30
LB983       lda LB98F,Y
            sta L0600,Y
            dey
            bpl LB983
            jmp L0600
LB98F       lda #$FF
            sta LD500
            jmp COLDSV
LB997       ldy #$00
LB999       lda LB9C3,Y
            sta L0600,Y
            iny
            bne LB999
            lda PALNTS
            beq LB9AD
            lda #$91
            sta LCEF9
            bne LB9B2
LB9AD       lda #$7D
            sta LCEF9
LB9B2       ldy #$00
            lda (FMSZPG+4),Y
            sta FMSZPG+2
            iny
            lda (FMSZPG+4),Y
            sta FMSZPG+3
            iny
            lda (FMSZPG+4),Y
            jmp L0600
LB9C3       sta LD500
            ldy #$05
            lda (FMSZPG+2),Y
            tax
            ldy #$08
            sta (BUFRLO),Y
            dey
            lda (FMSZPG+2),Y
            sta L0702
            ldy #$03
            lda (FMSZPG+2),Y
            sta L0701
            dey
            lda (FMSZPG+2),Y
            sta L0700
            lda FMSZPG+6
            sta LD500
            sec
            lda L0700
            sbc #$18
            sta L0700
            lda L0701
            sbc #$00
            sta L0701
            lda L0702
            sbc #$00
            sta L0702
            ldy #$03
LBA02       lsr L0702
            ror L0701
            ror L0700
            dey
            bne LBA02
            cpx #$01
            bne LBA1B
            lsr L0702
            ror L0701
            ror L0700
LBA1B       ldy #$06
            clc
            lda L0700
            adc #$03
            sta (BUFRLO),Y
            lda L0701
            adc #$00
            iny
            sta (BUFRLO),Y
            ldy #$08
            lda (BUFRLO),Y
            bne LBA4A
            tax
            ldy #$06
            lda (BUFRLO),Y
            cmp #$D0
            bcc LBA4F
            iny
            lda (BUFRLO),Y
            cmp #$02
            bcc LBA4F
            iny
            txa
            ora #$10
            sta (BUFRLO),Y
            rts
LBA4A       ora #$30
            sta (BUFRLO),Y
            rts
LBA4F       ldy #$08
            txa
            ora #$90
            sta (BUFRLO),Y
            rts
LBA57       lda LB480,Y
            sta LCC00,Y
            lda LB580,Y
            sta LCD00,Y
            lda #$00
            sta LCE00,Y
            sta LCF00,Y
            iny
            bne LBA57
            ldy #$7F
LBA70       lda LB680,Y
            sta LCE00,Y
            dey
            bpl LBA70
            rts
LBA7A       ldy #$02
            lda (FMSZPG+4),Y
            tax
            ldy #$00
            lda (FMSZPG+4),Y
            clc
            adc #$10
            sta (BUFRLO),Y
            iny
            lda (FMSZPG+4),Y
            adc #$00
            cmp #$C0
            bne LBA94
            inx
            lda #$80
LBA94       sta (BUFRLO),Y
            txa
            iny
            sta (BUFRLO),Y
            ldy #$00
            clc
            lda (BUFRLO),Y
            adc #$80
            ldy #$03
            sta (BUFRLO),Y
            ldy #$01
            lda (BUFRLO),Y
            adc #$01
            ldy #$04
            cmp #$C0
            bne LBAB4
            inx
            lda #$80
LBAB4       sta (BUFRLO),Y
            txa
            iny
            sta (BUFRLO),Y
            rts
LBABB       sei
            ldy #$00
            sty NMIEN
            sty DMACLT
            sty BUFRLO
            lda #$FF
            sta BUFRHI
LBACA       jsr LBB49
            dec BUFRHI
            lda BUFRHI
            cmp #$D7
            bne LBACA
            lda #$CF
            sta BUFRHI
LBAD9       jsr LBB49
            dec BUFRHI
            lda BUFRHI
            cmp #$BF
            bne LBAD9
            dec PORTB
            lda #$D0
            sta LC30B
            sta LC2F9
            sta LC49F
            lda #$1A
            sta LC30C
            lda #$AC
            sta LC4DD
            sta LC4ED
            lda #$FE
            sta LC4F9
            lda #$E6
            sta LC4C0
            lda #$06
            sta LC4C1
            sta LC4C3
            lda #$A5
            sta LC4C2
            lda #$C9
            sta LC4C4
            lda #$C0
            sta LC4C5
            lda #$91
            sta LC2F6
            lda #$04
            sta LC2F7
            lda #$C8
            sta LC2F8
            lda #$FB
            sta LC2FA
            lda #$AD
            sta LC410
            lda #$4C
            sta LFC67
            lda #$EA
            sta LFC68
            lda #$CD
            sta LFC69
            rts
LBB49       lda (BUFRLO),Y
            dec PORTB
            sta (BUFRLO),Y
            inc PORTB
            dey
            bne LBB49
LBB56       rts
LBB57       lda L8003
            bpl LBB60
            ldx #$00
            lda #$FF
LBB60       rts
LBB61       jsr LBC77
            jsr LBC47
            jsr LBCF9
            ldx #$02
LBB6C       jsr LBC3B
            jsr LBB81
            jsr LBC47
            jsr LBB81
            jsr LBB81
            dex
            bpl LBB6C
            jmp LBC3B
LBB81       clc
            lda RTCLOK+2
            adc #$02
LBB86       cmp RTCLOK+2
            bne LBB86
            rts
LBB8B       lda L0708
            cmp #$01
            beq LBB9F
            cmp #$02
            beq LBBBD
            cmp #$04
            beq LBBC7
            cmp #$08
            beq LBBD3
            rts
LBB9F       jsr LBC47
            ldx L7C20
            dex
LBBA6       txa
            and #$0F
            tax
            stx L7C20
            jsr LBCF9
            jsr LBC3B
            lda #$00
            sta L0708
            sta ATRACT
            jmp LB7B1
LBBBD       jsr LBC47
            ldx L7C20
            inx
            jmp LBBA6
LBBC7       ldy L7C21
            dey
            bpl LBBDE
            ldy L7C22
            jmp LBBDE
LBBD3       ldy L7C21
            iny
            cpy L7C23
            bne LBBDE
            ldy #$00
LBBDE       sty L7C21
            ldx #$00
            jsr LBCF9
            jsr LBD04
            jsr LBCC8
            jsr LBCEC
            ldx L7C21
            beq LBBF7
            jsr LBC19
LBBF7       jsr LBC89
            jsr LBC53
            ldx L7C20
            jsr LBCF9
            jsr LBC3B
            jsr LBB81
            jsr LBB81
            jsr LBB81
            lda #$00
            sta L0708
            sta ATRACT
            jmp LBB81
LBC19       clc
            lda FMSZPG+4
            adc #$80
            sta FMSZPG+4
            lda FMSZPG+5
            adc #$02
            sta FMSZPG+5
            dex
            bne LBC19
            rts
LBC2A       clc
            lda FMSZPG+4
            adc #$28
            sta FMSZPG+4
            lda FMSZPG+5
            adc #$00
            sta FMSZPG+5
            dex
            bne LBC2A
            rts
LBC3B       ldy #$27
LBC3D       lda (FMSZPG+2),Y
            ora #$80
            sta (FMSZPG+2),Y
            dey
            bpl LBC3D
            rts
LBC47       ldy #$27
LBC49       lda (FMSZPG+2),Y
            and #$7F
            sta (FMSZPG+2),Y
            dey
            bpl LBC49
            rts
LBC53       ldy #$28
LBC55       lda LBF4C,Y
            sta L7EC0,Y
            dey
            bpl LBC55
            ldy L7C22
            jsr LBD46
            stx L7ED7
            sta L7ED8
            ldy L7C21
            jsr LBD46
            stx L7ED4
            sta L7ED5
            rts
LBC77       ldy #$7E
LBC79       sty CONSOL
            lda VCOUNT
LBC7F       cmp VCOUNT
            beq LBC7F
            dey
            dey
            bpl LBC79
            rts
LBC89       ldx #$00
            jsr LBCF9
            lda FMSZPG+2
            clc
            adc #$04
            sta FMSZPG+2
            ldx #$10
LBC97       ldy #$00
LBC99       lda (FMSZPG+4),Y
            cmp #$FF
            beq LBCAC
            sta (FMSZPG+2),Y
            iny
            cpy #$21
            bne LBC99
            jsr LBCAD
            dex
            bne LBC97
LBCAC       rts
LBCAD       clc
            lda FMSZPG+2
            adc #$28
            sta FMSZPG+2
            lda FMSZPG+3
            adc #$00
            sta FMSZPG+3
            clc
            lda FMSZPG+4
            adc #$28
            sta FMSZPG+4
            lda FMSZPG+5
            adc #$00
            sta FMSZPG+5
            rts
LBCC8       ldx #$00
            jsr LBCF9
LBCCD       ldy #$00
LBCCF       lda LBDAE,X
            sta (FMSZPG+2),Y
            inx
            iny
            cpy #$03
            bne LBCCF
            clc
            lda FMSZPG+2
            adc #$28
            sta FMSZPG+2
            lda FMSZPG+3
            adc #$00
            sta FMSZPG+3
            cpx #$30
            bne LBCCD
            rts
LBCEC       lda #$07
LBCEE       sta FMSZPG+4
            lda #$80
            sta FMSZPG+5
            rts
LBCF5       lda #$00
            beq LBCEE
LBCF9       lda LBDF6,X
            sta FMSZPG+2
            lda LBE06,X
            sta FMSZPG+3
            rts
LBD04       ldx #$00
            jsr LBCF9
            ldx #$02
            ldy #$00
            tya
LBD0E       sta (FMSZPG+2),Y
            iny
            bne LBD0E
            inc FMSZPG+3
            dex
            bpl LBD0E
            rts
LBD19       jsr LBCEC
LBD1C       clc
            lda FMSZPG+4
            adc #$80
            sta FMSZPG+4
            lda FMSZPG+5
            adc #$02
            sta FMSZPG+5
            ldy #$00
            lda (FMSZPG+4),Y
            cmp #$FF
            beq LBD3E
            inc L7C22
            lda L7C22
            cmp #$12
            beq LBD3E
            jmp LBD1C
LBD3E       ldy L7C22
            iny
            sty L7C23
            rts
LBD46       iny
            tya
            ldx #$00
            ldy #$01
LBD4C       sec
            sbc #$01
            beq LBD5B
            iny
            cpy #$0A
            bne LBD4C
            inx
            ldy #$00
            beq LBD4C
LBD5B       txa
            clc
            adc #$10
            tax
            tya
            clc
            adc #$10
            rts
LBD65       ldx #$14
            ldy #$00
LBD69       dey
            bne LBD69
            dex
            bne LBD69
            rts
            pha
            tya
            pha
            ldy #$10
LBD75       lda LBD9E,Y
            sta WSYNC
            sta COLPF0
            dey
            bpl LBD75
            lda L7C24
            cmp #$05
            bne LBD97
            lda #$00
            sta L7C24
            lda PORTA
            eor #$FF
            beq LBD97
            sta L0708
LBD97       inc L7C24
            pla
            tay
            pla
            rti
LBD9E       .byte $10,$12,$14,$16,$18,$1A,$1C,$1F,$1F,$3C,$3A,$38,$36,$34,$32,$30
LBDAE       .byte $1C,$21,$1E,$1C,$22,$1E,$1C,$23,$1E,$1C,$24,$1E,$1C,$25,$1E,$1C
            .byte $26,$1E,$1C,$27,$1E,$1C,$28,$1E,$1C,$29,$1E,$1C,$2A,$1E,$1C,$2B
            .byte $1E,$1C,$2C,$1E,$1C,$2D,$1E,$1C,$2E,$1E,$1C,$2F,$1E,$1C,$30,$1E
LBDDE       .byte $3F,$15,$12,$3A,$2A,$38,$3D,$39,$0D,$01,$05,$00,$25,$23,$08,$0A
LBDEE       .byte $06,$07,$0E,$0F
LBDF2       .byte $04,$08,$01,$02
LBDF6       .byte $40,$68,$90,$B8,$E0,$08,$30,$58,$80,$A8,$D0,$F8,$20,$48,$70,$98
LBE06       .byte $7C,$7C,$7C,$7C,$7C,$7D,$7D,$7D,$7D,$7D,$7D,$7D,$7E,$7E,$7E,$7E
            .byte $04,$68,$90,$B8,$E0,$08,$30,$58,$80,$A8,$D0,$F8,$20,$48,$70,$98
            .byte $80,$7C,$7C,$7C,$7C,$7D,$7D,$7D,$7D,$7D,$7D,$7D,$7E,$7E,$7E,$7E
            .byte AEMPTY8
            .byte AEMPTY8
            .byte AEMPTY8
            .byte AEMPTY8
            .byte AEMPTY8
            .byte ALMS+$02
            .word L060C
            .byte $02
            .byte AVB+AJMP
            .word L0600
            .byte $00,$00,$00,$00,$34,$68,$65,$00,$70,$72,$6F,$67,$72,$61,$6D,$00
            .byte $6E,$65,$65,$64,$00,$14,$18,$6B,$00,$32,$21,$2D,$00,$6D,$65,$6D
            .byte $6F,$72,$79,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$35,$73,$65
            .byte $00,$6D,$6F,$72,$65,$00,$32,$21,$2D,$00,$61,$6E,$64,$00,$73,$74
            .byte $61,$72,$74,$00,$61,$67,$61,$69,$6E,$00,$00,$00,$00,$00,$00,$00
LBE92       .byte AEMPTY8
            .byte AEMPTY8
            .byte AEMPTY8
            .byte AEMPTY8
            .byte AEMPTY8
            .byte ALMS+$02
            .word L060B
            .byte AVB+AJMP
            .word L0600
            .byte $21,$34,$32,$0D,$2C,$6F,$61,$64,$65,$72,$00,$6E,$65,$65,$64,$00
            .byte $6F,$72,$69,$67,$69,$6E,$61,$6C,$00,$21,$74,$61,$72,$69,$00,$38
            .byte $2C,$0F,$38,$25,$00,$2F,$33,$00
LBEC5       .byte AEMPTY8
            .byte AEMPTY8
            .byte ADLI+AEMPTY1
            .byte ALMS+$07
            .word LBEE8
            .byte AEMPTY8
            .byte ALMS+$02
            .word L7C40
            .byte $02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02,$02
            .byte AEMPTY6
            .byte $02
            .byte AEMPTY7
            .byte ALMS+$02
            .word LBEFC
            .byte $02
            .byte AVB+AJMP
            .word LBEC5
LBEE8       .byte $2D,$35,$2C,$34,$29,$00,$26,$2C,$21,$33,$28,$00,$2D,$25,$27,$21
            .byte $23,$21,$32,$34
LBEFC       .byte $80,$A6,$A9,$AC,$A5,$80,$AC,$AF,$A1,$A4,$A5,$B2,$80,$B6,$94,$8E
            .byte $94,$80,$88,$E3,$89,$80,$92,$EB,$91,$93,$80,$E2,$F9,$80,$A2,$8E
            .byte $80,$A8,$E5,$F2,$E1,$EC,$E5,$80,$80,$B0,$A3,$80,$B0,$F2,$EF,$E7
            .byte $F2,$E1,$ED,$80,$88,$E3,$89,$80,$92,$EB,$91,$93,$80,$E2,$F9,$80
            .byte $AD,$E1,$F2,$E3,$E9,$EE,$80,$B3,$EF,$E3,$E8,$E1,$E3,$EB,$E9,$80
LBF4C       .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$30
            .byte $61,$67,$65,$00,$10,$10,$0F,$10,$10,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
            .byte $00,$00,$00,$00
            .byte '(C)2013 BY BERND HERALE - VERSION 4.3   -11.12.2013'
            .byte $00,$00,$00,$00,$00,$00,$00
            .word LB700
            .byte $00,$04
            .word LBB56
;
         
