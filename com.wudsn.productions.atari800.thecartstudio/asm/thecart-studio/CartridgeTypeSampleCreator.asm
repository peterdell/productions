;
;	Sample BIN ROM image for creating sample ".ROM" and ".CAR" files.
;	System equates are defined in the main program depending on architecture.
;	Compile using "Makefile.bat".
;	Note that for types with very small banks like "AST 32 KB (47)" not all texts is displayed.
;
;	(c) 2013-06-07 JAC!	
;	(r) 2018-05-30 JAC!	
;
;	@com.wudsn.ide.asm.mainsourcefile=CartridgeTypeSampleCreator-Atari800.asm

p1	= $80
p2	= $82

	opt h-f+

	org $0600
	.proc main			;Assume CLD is set

	.byte $ff,$ff,$00,$00,$00,$00	;Maxflash Cartridge Studio fix

	lda #>main			;High byte of PC, updated by external program at startOffset+1
	.byte $2c,a(ram.text-main)	;BIT abs to store text offset read by external program at startOffset+3/4
	sta p1+1
	lda #>main
	sta p2+1
	ldy #0
	sty antic+$0e
	sty p1
	sty p2+2
	ldx #>[.len main + $ff]
copy_loop
	lda (p1),y
	sta (p2),y
	iny
	bne copy_loop
	inc p1+1
	inc p2+1
	dex
	bne copy_loop
	jmp ram
	
	.proc ram
	mwa #xitvbv vbiv
	mva #$40 antic+$0e
	mva #14 gtia+$17	;White text foreground
	mwa #dl antic+$02
	mva #$22 antic+$00
	mva #>font antic+$09

	ldx #[.len text]
text_loop
	lda text-1,x		;Convert ASCII to screen code
	cmp #96
	scs
	sbc #31
	sta text-1,x
	dex
	bne text_loop
	stx gtia+$18		;Black text background

forver_loop
	lda antic+$0b
	sta gtia+$1a
	jmp forver_loop

	.local dl
:9	.byte $70
	.byte $42,a(text)
:5	.byte $70,$02
	.byte $41,a(dl)
	.endl

	.local text		;6 lines of text, keept in ASCII in the ROM, so it canm be read in the hex editor
:240	.byte 0
	.endl

	.endp			;End of ram
  	
  	.endp			;End of main

  	.print .len main