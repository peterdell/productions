;
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=../CartMenu-Extended.asm
;
;	The definition in this file are used by both the RAM and the ROM part of the ATR started.
;	Only zero page locations used by DISKIN/SIOV must be used.
;	Using zero page locations used by CIO will break DOS like access.

selected_item_number	= $00	;Byte, used in patched ATR file menus, see AtrLoader.java
bootini    		= $04	;Word, same as in OS boot process
dosini	   		= $0c	;Word, same as in OS boot process
buffer_ptr		= $15	;Word, BUFADR, normally used by DSKINV
dskfms			= $18	;word, internal use in dos/dos
dskutl			= $1a	;word, internal use in dos/dos
cart_start_bank		= $32	;Word, BFENLO, normally used by SIOV
cart_bank  		= $34	;Word, BFENLO, normally used by SIOV
cart_ptr  		= $36	;Word, LTEMP, normally used by SIOV
buffer_len		= $38	;Word, BUFRFL, normally used by SIOV
empty_dl   		= $3a	;3 bytes, RECVDN/XMTDON/CHKSNT, normally used by SIOV
fmszpg			= $43	;7 bytes reserved for the File Managing System (FMS)
boot_buffer 		= $700
