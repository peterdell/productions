;	@com.wudsn.ide.asm.mainsourcefile=MaxflashMenu-Old.asm
;
; system equates
;
; os equates
; ----------
; 
; syntax:
; use '=' for addresses
; and '<' for read addresses (ex: kbcode)
; and '>' for write addresses (ex: stimer)
; and '#' for values
; 
; io equates
; 
ichid	= $0000
icdno	= $0001
iccom	= $0002
icsta	= $0003
icbal	= $0004
icbah	= $0005
icptl	= $0006
icpth	= $0007
icbll	= $0008
icblh	= $0009
icax1	= $000a
icax2	= $000b
icax3	= $000c
icax4	= $000d
icax5	= $000e
icax6	= $000f
; 
; display list equates
; 
adli	= $0080
avb	= $0040
alms	= $0040
avscr	= $0020
ahscr	= $0010
ajmp	= $0001
aempty1	= $0000
aempty2	= $0010
aempty3	= $0020
aempty4	= $0030
aempty5	= $0040
aempty6	= $0050
aempty7	= $0060
aempty8	= $0070
; 
; os variables for xl/xe
; 
; page 0
; 
ngflag	= $0001
casini	= $0002
ramlo	= $0004
tramsz	= $0006
cmcmd	= $0007
warmst	= $0008
boot	= $0009
dosvec	= $000a
dosini	= $000c
appmhi	= $000e
pokmsk	= $0010
brkkey	= $0011
rtclok	= $0012
bufadr	= $0015
iccomt	= $0017
dskfms	= $0018
dskutl	= $001a
abufpt	= $001c
ichidz	= $0020
icdnoz	= $0021
iccomz	= $0022
icstaz	= $0023
icbalz	= $0024
icbahz	= $0025
icptlz	= $0026
icpthz	= $0027
icbllz	= $0028
icblhz	= $0029
icax1z	= $002a
icax2z	= $002b
icax3z	= $002c
icax4z	= $002d
icax5z	= $002e
icax6z	= $002f
status	= $0030
chksum	= $0031
bufrlo	= $0032
bufrhi	= $0033
bfenlo	= $0034
bfenhi	= $0035
ltemp	= $0036
bufrfl	= $0038
recvdn	= $0039
xmtdon	= $003a
chksnt	= $003b
nocksm	= $003c
bptr	= $003d
ftype	= $003e
feof	= $003f
freq	= $0040
soundr	= $0041
critic	= $0042
fmszpg	= $0043
zchain	= $004a
dstat	= $004c
atract	= $004d
drkmsk	= $004e
colrsh	= $004f
temp	= $0050
hold1	= $0051
lmargn	= $0052
rmargn	= $0053
rowcrs	= $0054
colcrs	= $0055
dindex	= $0057
savmsc	= $0058
oldrow	= $005a
oldcol	= $005b
oldchr	= $005d
oldadr	= $005e
fkdef	= $0060
palnts	= $0062
logcol	= $0063
adress	= $0064
mlttmp	= $0066
savadr	= $0068
ramtop	= $006a
bufcnt	= $006b
bufstr	= $006c
bitmsk	= $006e
shfamt	= $006f
rowac	= $0070
colac	= $0072
endpt	= $0074
deltar	= $0076
deltac	= $0077
keydef	= $0079
swpflg	= $007b
holdch	= $007c
insdat	= $007d
countr	= $007e
lomem	= $0080
; 
; page 2
; 
vdslst	= $0200
vprced	= $0202
vinter	= $0204
vbreak	= $0206
vkeybd	= $0208
vserin	= $020a
vseror	= $020c
vseroc	= $020e
vtimr1	= $0210
vtimr2	= $0212
vtimr4	= $0214
vimirq	= $0216
cdtmv1	= $0218
cdtmv2	= $021a
cdtmv3	= $021c
cdtmv4	= $021e
cdtmv5	= $0220
vvblki	= $0222
vvblkd	= $0224
cdtma1	= $0226
cdtma2	= $0228
cdtmf3	= $022a
srtimr	= $022b
cdtmf4	= $022c
intemp	= $022d
cdtmf5	= $022e
sdmctl	= $022f
sdlstl	= $0230
sdlsth	= $0231
sskctl	= $0232
spare	= $0233
lpenh	= $0234
lpenv	= $0235
brkky	= $0236
vpirq	= $0238
cdevic	= $023a
ccomnd	= $023b
caux1	= $023c
caux2	= $023d
tmpsio	= $023e
errflg	= $023f
dflags	= $0240
dbsect	= $0241
bootad	= $0242
coldst	= $0244
reclen	= $0245
dsktim	= $0246
pdvmsk	= $0247
shpdvs	= $0248
pdmsk	= $0249
reladr	= $024a
pptmpa	= $024c
pptmpx	= $024d
chsalt	= $026b
vsflag	= $026c
keydis	= $026d
fine	= $026e
gprior	= $026f
paddl0	= $0270
paddl1	= $0271
paddl2	= $0272
paddl3	= $0273
paddl4	= $0274
paddl5	= $0275
paddl6	= $0276
paddl7	= $0277
stick0	= $0278
stick1	= $0279
stick2	= $027a
stick3	= $027b
ptrig0	= $027c
ptrig1	= $027d
ptrig2	= $027e
ptrig3	= $027f
ptrig4	= $0280
ptrig5	= $0281
ptrig6	= $0282
ptrig7	= $0283
strig0	= $0284
strig1	= $0285
strig2	= $0286
strig3	= $0287
hibyte	= $0288
wmode	= $0289
blim	= $028a
imask	= $028b
jveck	= $028c
newadr	= $028e
txtrow	= $0290
txtcol	= $0291
tindex	= $0293
txtmsc	= $0294
txtold	= $0296
cretry	= $029c
hold3	= $029d
subtmp	= $029e
hold2	= $029f
dmask	= $02a0
tmplbt	= $02a1
escflg	= $02a2
tabmap	= $02a3
logmap	= $02b2
invflg	= $02b6
filflg	= $02b7
tmprow	= $02b8
tmpcol	= $02b9
scrflg	= $02bb
hold4	= $02bc
dretry	= $02bd
shfloc	= $02be
botscr	= $02bf
pcolr0	= $02c0
pcolr1	= $02c1
pcolr2	= $02c2
pcolr3	= $02c3
color0	= $02c4
color1	= $02c5
color2	= $02c6
color3	= $02c7
color4	= $02c8
runadr	= $02c9
hiused	= $02cb
zhiuse	= $02cd
gbytea	= $02cf
loadad	= $02d1
zloada	= $02d3
dsctln	= $02d5
acmisr	= $02d7
krpder	= $02d9
keyrep	= $02da
noclik	= $02db
helpfg	= $02dc
dmasav	= $02dd
pbpnt	= $02de
pbufsz	= $02df
runad	= $02e0
initad	= $02e2
ramsiz	= $02e4
memtop	= $02e5
memlo	= $02e7
hndlod	= $02e9
dvstat	= $02ea
cbaudl	= $02ee
cbaudh	= $02ef
crsinh	= $02f0
keydel	= $02f1
ch1	= $02f2
chact	= $02f3
chbas	= $02f4
newrow	= $02f5
newcol	= $02f6
rowinc	= $02f8
colinc	= $02f9
char	= $02fa
atachr	= $02fb
ch	= $02fc
fildat	= $02fd
dspflg	= $02fe
ssflag	= $02ff
; 
; page 3
; 
ddevic	= $0300
dunit	= $0301
dcomnd	= $0302
dstats	= $0303
dbuflo	= $0304
dbufhi	= $0305
dtimlo	= $0306
dunuse	= $0307
dbytlo	= $0308
dbythi	= $0309
daux1	= $030a
daux2	= $030b
timer1	= $030c
addcor	= $030e
casflg	= $030f
timer2	= $0310
temp1	= $0312
temp2	= $0314
temp3	= $0315
savio	= $0316
timflg	= $0317
stackp	= $0318
tstat	= $0319
hatabs	= $031a
pupbt1	= $033d
pupbt2	= $033e
pupbt3	= $033f
iocb0	= $0340
iocb1	= $0350
iocb2	= $0360
iocb3	= $0370
iocb4	= $0380
iocb5	= $0390
iocb6	= $03a0
iocb7	= $03b0
prnbuf	= $03c0
superf	= $03e8
ckey	= $03e9
cassbt	= $03ea
cartck	= $03eb
derrf	= $03ec
acmvar	= $03ed
basicf	= $03f8
mintlk	= $03f9
gintlk	= $03fa
chlink	= $03fb
casbuf	= $03fd
; 
; hardware registers
; 
; gtia
; 
m0pf	= $d000
hposp0	= $d000
m1pf	= $d001
hposp1	= $d001
m2pf	= $d002
hposp2	= $d002
m3pf	= $d003
hposp3	= $d003
p0pf	= $d004
hposm0	= $d004
p1pf	= $d005
hposm1	= $d005
p2pf	= $d006
hposm2	= $d006
p3pf	= $d007
hposm3	= $d007
m0pl	= $d008
sizep0	= $d008
m1pl	= $d009
sizep1	= $d009
m2pl	= $d00a
sizep2	= $d00a
m3pl	= $d00b
sizep3	= $d00b
p0pl	= $d00c
sizem	= $d00c
p1pl	= $d00d
grafp0	= $d00d
p2pl	= $d00e
grafp1	= $d00e
p3pl	= $d00f
grafp2	= $d00f
trig0	= $d010
grafp3	= $d010
trig1	= $d011
grafm	= $d011
trig2	= $d012
colpm0	= $d012
trig3	= $d013
colpm1	= $d013
pal	= $d014
colpm2	= $d014
colpm3	= $d015
colpf0	= $d016
colpf1	= $d017
colpf2	= $d018
colpf3	= $d019
colbk	= $d01a
prior	= $d01b
vdelay	= $d01c
gractl	= $d01d
hitclr	= $d01e
consol	= $d01f
; 
; pokey
; 
pot0	= $d200
audf1	= $d200
pot1	= $d201
audc1	= $d201
pot2	= $d202
audf2	= $d202
pot3	= $d203
audc2	= $d203
pot4	= $d204
audf3	= $d204
pot5	= $d205
audc3	= $d205
pot6	= $d206
audf4	= $d206
pot7	= $d207
audc4	= $d207
allpot	= $d208
audctl	= $d208
kbcode	= $d209
stimer	= $d209
random	= $d20a
skrest	= $d20a
potgo	= $d20b
serin	= $d20d
serout	= $d20d
irqst	= $d20e
irqen	= $d20e
skstat	= $d20f
skctl	= $d20f
; 
; pia
; 
porta	= $d300
portb	= $d301
pactl	= $d302
pbctl	= $d303
; 
; antic
; 
dmaclt	= $d400
chactl	= $d401
dlistl	= $d402
dlisth	= $d403
hscrol	= $d404
vscrol	= $d405
pmbase	= $d407
chbase	= $d409
wsync	= $d40a
vcount	= $d40b
penh	= $d40c
penv	= $d40d
nmien	= $d40e
nmist	= $d40f
nmires	= $d40f
; 
; floating point routines
; 
afp	= $d800
fasc	= $d8e6
ifp	= $d9aa
fpi	= $d9d2
zfr0	= $da44
zf1	= $da46
fsub	= $da60
fadd	= $da66
fmul	= $dadb
fdiv	= $db28
plyevl	= $dd40
fld0r	= $dd89
fld0p	= $dd8d
fld1r	= $dd98
fld1p	= $dd9c
fstor	= $dda7
fstop	= $ddab
fmove	= $ddb6
exp	= $ddc0
exp10	= $ddcc
log	= $decd
log10	= $ded1
; 
; rom vectors
; 
dskinv	= $e453
ciov	= $e456
siov	= $e459
setvbv	= $e45c
sysvbv	= $e45f
xitvbv	= $e462
sioinv	= $e465
sendev	= $e468
intinv	= $e46b
cioinv	= $e46e
selfsv	= $e471
warmsv	= $e474
coldsv	= $e477
rblokv	= $e47a
csopiv	= $e47d
pupdiv	= $e480
selftsv	= $e483
pentv	= $e486
phunlv	= $e489
phiniv	= $e48c
gpdvv	= $e48f