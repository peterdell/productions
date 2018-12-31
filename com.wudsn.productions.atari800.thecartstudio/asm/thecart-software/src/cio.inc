CIOV	= $E456

ICCOM	= $342
ICSTA	= $343
ICBAL	= $344
ICBAH	= $345
ICBLL	= $348
ICBLH	= $349
ICAX1	= $34A
ICAX2	= $34B

; CIO-Befehle

COPEN	=   3
CCLSE	=  12
CGTXT	=   5
CPTXT	=   9
CGBIN	=   7
CPBIN	=  11

	.MACRO KANNUM ;KANAL
	LDX #(%1)*16
	.ENDM

	.MACRO OPEN ; KANAL,AUX1,AUX2,FILENAME
	KANNUM %1
	LDA #%2
	STA ICAX1,X
	LDA #%3
	STA ICAX2,X
	LDA #COPEN
	STA ICCOM,X
	LDA #<[%4]
	STA ICBAL,X
	LDA #>[%4]
	STA ICBAH,X
	JSR CIOV
	.ENDM

	.MACRO CLOSE ; KANAL
	KANNUM %1
	LDA #CCLSE
	STA ICCOM,X
	JSR CIOV
	.ENDM

	.MACRO NOTE ; KANAL,SECTOR,BYTE
	KANNUM %1
	LDA #$26
	STA ICCOM,X
	JSR CIOV
	LDA $34C,X
	STA %2
	LDA $34D,X
	STA %2+1
	LDA $34E,X
	STA %3
	.ENDM

	.MACRO POINT ; KANAL,SECTOR,BYTE
	KANNUM %1
	LDA #$25
	STA ICCOM,X
	LDA %2
	STA $34C,X
	LDA %2+1
	STA $34D,X
	LDA %3
	STA $34E,X
	JSR CIOV
	.ENDM

	.MACRO PRINT ; KANAL,LABEL
	KANNUM %1
	LDA #CPTXT
	STA ICCOM,X
	LDA #<[%2]
	STA ICBAL,X
	LDA #>[%2]
	STA ICBAH,X
	LDA #127		; max. Laenge
	STA ICBLL,X
	LDA #0
	STA ICBLH,X
	JSR CIOV
	.ENDM

	.MACRO PRINT2 ; KANAL,VECTOR
	KANNUM %1
	LDA #CPTXT
	STA ICCOM,X
	LDA %2
	STA ICBAL,X
	LDA %2+1
	STA ICBAH,X
	LDA #127		; max. Laenge
	STA ICBLL,X
	LDA #0
	STA ICBLH,X
	JSR CIOV
	.ENDM

	.MACRO BPUT ; KANAL,LAENGE,BUFFER
	KANNUM %1
	LDA #CPBIN
	STA ICCOM,X
	LDA #<[%2]
	STA ICBLL,X
	LDA #>[%2]
	STA ICBLH,X
	LDA #<[%3]
	STA ICBAL,X
	LDA #>[%3]
	STA ICBAH,X
	JSR CIOV
	.ENDM

	.MACRO BPUT2 ; KANAL,LEN-VECTOR,BUFFER
	KANNUM %1
	LDA #CPBIN
	STA ICCOM,X
	LDA %2
	STA ICBLL,X
	LDA %2+1
	STA ICBLH,X
	LDA #<[%3]
	STA ICBAL,X
	LDA #>[%3]
	STA ICBAH,X
	JSR CIOV
	.ENDM

	.MACRO BPUT3 ; KANAL,LEN-VECTOR,BUFFER-VEC
	KANNUM %1
	LDA #CPBIN
	STA ICCOM,X
	LDA %2
	STA ICBLL,X
	LDA %2+1
	STA ICBLH,X
	LDA %3
	STA ICBAL,X
	LDA %3+1
	STA ICBAH,X
	JSR CIOV
	.ENDM

	.MACRO BGET ; KANAL,LAENGE,BUFFER
	KANNUM %1
	LDA #CGBIN
	STA ICCOM,X
	LDA #<[%2]
	STA ICBLL,X
	LDA #>[%2]
	STA ICBLH,X
	LDA #<[%3]
	STA ICBAL,X
	LDA #>[%3]
	STA ICBAH,X
	JSR CIOV
	.ENDM

	.MACRO BGET2 ; KANAL,LEN-VECTOR,BUFFER
	KANNUM %1
	LDA #CGBIN
	STA ICCOM,X
	LDA %2
	STA ICBLL,X
	LDA %2+1
	STA ICBLH,X
	LDA #<[%3]
	STA ICBAL,X
	LDA #>[%3]
	STA ICBAH,X
	JSR CIOV
	.ENDM

	.MACRO INPUT ; KANAL,BUFFER
	KANNUM %1
	LDA #CGTXT
       	STA ICCOM,X
       	LDA #<[%2]
       	STA ICBAL,X
       	LDA #>[%2]
       	STA ICBAH,X
       	LDA #127                ; max. Laenge
       	STA ICBLL,X
       	LDA #0
       	STA ICBLH,X
        JSR CIOV
	.ENDM

	.MACRO INPUT2 ; KANAL,BUFFER,LEN
	KANNUM %1
	LDA #CGTXT
       	STA ICCOM,X
       	LDA #<[%2]
       	STA ICBAL,X
       	LDA #>[%2]
       	STA ICBAH,X
       	LDA #[%3]                ; max. Laenge
       	STA ICBLL,X
       	LDA #0
       	STA ICBLH,X
        JSR CIOV
	.ENDM

	; new, shorter macros, also include "cio.src"

; "bput" text to #0, parameter: label TEXT (label TEXTL must be set to length of text)
	.macro BPUT0 ; LABEL
	.if %0 <> 1
	.error "BPUT0 needs 1 parameter"
	.endif
	LDX #<%1
	LDY #>%1
	LDA #%$1L
	JSR BPUT0COD
	.endm

	.macro BPUT0L ; label, length
	.if %0 <> 2
	.error "BPUT0L needs 2 parameter"
	.endif
	LDX #<[%1]
	LDY #>[%1]
	LDA #%2
	JSR BPUT0COD
	.endm

	.macro PRINT0	; address
	.if %0 <> 1
	.error "PRINT0 needs 1 parameter"
	.endif
	LDX #<[%1]
	LDY #>[%1]
	JSR PRINT0COD
	.endm

	.macro INPUT0	; address, 127 bytes
	.if %0 <> 1
	.error "INPUT0 needs 1 parameter"
	.endif
	LDA #127
	LDX #<[%1]
	LDY #>[%1]
	JSR INPUT0COD
	.endm

	.macro INPUT0L	; address, length from addressL
	.if %0 <> 1
	.error "INPUT0L needs 1 parameter"
	.endif
	LDA #%$1L
	LDX #<[%1]
	LDY #>[%1]
	JSR INPUT0COD
	.endm

