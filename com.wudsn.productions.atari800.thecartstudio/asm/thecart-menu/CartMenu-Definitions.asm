;
;	>>> The!Cart Menu - Cartmenu definitions by hias <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=CartMenu-Extended.asm

; The following jump vectors ar available in bank 0 of the "cartmenu.rom".
; See "cartmenu.src" of the "http://www.horus.com/~hias/thecart/software/".
;
;$A000: Start CartMenu - ruft "mein" CartMenu auf
;
;$A003: Start Cartridge - einfach A mit dem Mode, X mit dem
;Low-Byte der Banknummer und Y mit dem High-Byte der Banknummer
;laden und dort hin springen und die Cart wird gestartet.
;
;$A006: Read SPI EEPROM
;$A009: Write SPI EEPROM
;
;Der ZP Vektor in $E0/$E1 muß auf die zu lesenden/schreibenden Daten
;zeigen, X den Byte-Count und Y die Adresse im EEPROM enthalten.
;Bei einem Fehler ist nach dem Return das Carry-Flag gesetzt
;(cleared bei OK).
;
;Beim Schreiben von Daten musst Du folgendes beachten: Das EEPROM
;ist intern in Blöcken zu 16-Bytes organisiert und der interne
;Adress-Zähler kann diese Grenzen nicht überschreiten (er macht
;dann einen wrap-around).
;
;Schreibst Du zB 3 Bytes ab Adresse 14, so landen die in den
;Adressen 14, 15, 0.
;
;Also am besten immer max. 16 Bytes schreiben und aufpassen, daß
;Du keine 16-Byte Grenze überschreitest.
;Wie's beim Lesen aussieht hab' ich noch nicht getestet, aber
;lieber aufpassen und kleine Häppchen schreiben :)
;Achja: Das CartMenu verwendet die EEPROM Adressen ab $F0, der Rest
;darunter ist frei.
;
;$A00C: Init OS Variables
;
;Das CartMenu wird per Cart-Init gestartet, zu diesem Zeitpunkt
;ist noch kein GR.0 Bildschirm geöffnet und einige Variablen im
;OS Bereich sind noch nicht wie gewohnt initialisiert.
;
;Wenn Du die Cart Start/Init Adressen verbiegst, mach am besten
;einen JSR dorthin, damit der Zeichensatz richtig gesetzt ist
;und Keyboard/Break IRQ und NMI aktiviert sind. Danach sollte
;alles wie üblicherweise gewohnt laufen.
;
; $A00F: default cartridge start address
;
; Einsprungpunkt zum default The!Cart Menü Start bei $A00F,
; zeigt per Default auf mein expert Menü kann von Peters Menü
; verbogen werden. Die Cart Init/Start Vektoren bei $BFFx zeigen
; nun auf diesen Einsprungpunkt.
;
; $A012: Start cartridge at specific address
;
; A=mode, X=bank lo, Y=bank hi, C=lockflag, E0/E1: start address
; Start is performed with NMI and IRQs off.

	.enum cartmenu		;HIAS Simple Menu (cartmenu.rom)
runadr			= $e0	;Run address for entry_runcart_adr
spivec			= $e2	;SPI EEPROM zero page vector
entry_cartmenu		= $a000	;Start cartmenu
entry_runcart		= $a003	;Start cartridge, IN: <A>=mode, <X>=bank lo, <Y>=bank hi, <C>=lockflag
entry_read_eeprom	= $a006 ;Read SPI EEPROM, IN: (SPIVEC)=data, <X>=byte count, <Y>=EEPROM address
entry_write_eeprom	= $a009 ;Write SPI EEPROM, IN: (SPIVEC)=data, <X>=byte count, <Y>=EEPROM address
entry_initos		= $a00c	;Init OS Variables
entry_default_cartstart	= $a00f	;Default cartride start vector used by the flasher, set to cartmenu or extended menu, depending on which is available
entry_runcart_adr	= $a012	;Start cartridge, IN: <A>=mode, <X>=bank lo, <Y>=bank hi, <C>=lockflag, <runadr>=run address
	.ende
	
	.enum the_cart_mode	;Special The!Cart mode value used by the studio, see the_cart.mode
tc_mode_off		= $00
tc_mode_8k		= $01	;Configuration lock is ignored in this mode
tc_mode_flexi		= $20	;Configuration lock is ignored in this mode, The!Cart 
tc_mode_atr_file	= $e0
tc_mode_binary_file	= $e1
tc_mode_executable_file = $e2	
tc_mode_sap_file	= $e3	
	.ende
	
	.enum the_cart
primary_bank_lo		= $d5a0
primary_bank_hi		= $d5a1
primary_bank_enable	= $d5a2
secondary_bank_lo	= $d5a3
secondary_bank_hi	= $d5a4
secondary_bank_enable	= $d5a5
mode			= $d5a6	;See the_cart_mode above
configuration_lock	= $d5af
	.ende
