;
;	Sample BIN ROM image for creating sample ".ROM" and ".CAR" files for Atari 5200 consoles.
;
;	(c) 2013-06-07 JAC!
;
;	@com.wudsn.ide.asm.outputfile=..\..\src\data\CartridgeTypeSampleCreator-Atari5200.rom

vbiv	= $202
xitvbv	= $fcb7	;RTI

antic	= $d400
pokey	= $e800
gtia	= $c000
font	= $f800

	icl "CartridgeTypeSampleCreator.asm"