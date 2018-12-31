;
;	Sample BIN ROM image for creating sample ".ROM" and ".CAR" files for Atari XL/XE computers.
;
;	(c) 2013-06-07 JAC!	
;
;	@com.wudsn.ide.asm.outputfile=..\..\src\data\CartridgeTypeSampleCreator-Atari800.rom

vbiv	= $222
xitvbv	= $e462

antic 	= $d400
pokey	= $d200
gtia	= $d000
font	= $e000

	icl "CartridgeTypeSampleCreator.asm"