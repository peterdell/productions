
;	>>>  The!Cart - ExtendedMenu by JAC! <<<
;
;	@com.wudsn.ide.asm.mainsourcefile=../CartMenu-Extended.asm


	.proc atr_starter	;The!Cart is OFF

	.enum atr_header	;Offset in the ATR header

	sector_size = 4		;Word

	paragraph_0 = 2		;Byte 1 of 4
	paragraph_1 = 3		;Byte 2 of 4
	paragraph_2 = 6		;Byte 3 of 4
	paragraph_3 = 7		;Byte 4 of 4

	sd_paragraphs = $1680	;Value of paragraph_0/1/2/3 for single sided single density disk with 720 sectors
	.ende

	.enum sector_size
	sd	= $80
	dd	= $100
	.ende

starter_ptr		= fmszpg	;Used during initialization only, make sure it is not in the user ZP $80-$ff
jsr_ptr			= fmszpg+2	;Used during initialization only

start_atr_entry
	mva cursor.selected_entry.item_number selected_item_number
	jsr copy_starter
	lda #>(starter_template+$ff)	;Clear everything starting at the relocatable parts
	m_clear_main_ram_and_zp
	jsr prepare_boot
	jmp (starter_ptr)		;Jump to jump_boot

;===============================================================

	.proc copy_starter		;Copy start routine to low RAM, OUT: <starter_ptr>=loader_base_address

;	Relocate JSR calls in to JSR JUMP_...
	.macro m_reloc jsr_label jump_label
	adw starter_ptr #(:jump_label-starter) jsr_ptr
 
 	ldy #:jsr_label+1-starter
	mva jsr_ptr (starter_ptr),y
	iny
	mva jsr_ptr+1 (starter_ptr),y
	.endm

	mwa cursor.selected_entry.loader_base_address starter_ptr

	ldy #0
loop	lda starter_template,y		
	sta (starter_ptr),y
	iny
	cpy #.len starter
	bne loop

;	Compute disk status value based on ATR size and sector size
	jsr set_disk_status_code

;	Copy start bank into starter
	ldy #starter.simulate_siov.start_bank_number_lo-starter
	mva cursor.selected_entry.start_bank_number (starter_ptr),y
	ldy #starter.simulate_siov.start_bank_number_hi-starter
	mva cursor.selected_entry.start_bank_number+1 (starter_ptr),y

;	Relocate jump table in starter
	m_reloc starter.jmp_boot   starter.simulate_boot
	m_reloc starter.jmp_dskinv starter.simulate_dskinv
	m_reloc starter.jmp_siov   starter.simulate_siov

;	Relocate initialization calls in simulate_boot
	m_reloc starter.simulate_boot.jsr_bootini starter.simulate_boot.jump_bootini
	m_reloc starter.simulate_boot.jsr_dosini  starter.simulate_boot.jump_dosini

;	Relocate JSR SIOV calls in simulate_boot to JSR JMP_SIOV
;	Make sure this is the last relocation, because the starter uses jump (jsr_ptr) for SIOV
	m_reloc starter.simulate_boot.jsr_siov starter.jmp_siov
	rts

;===============================================================
;	Compute disk status value based on ATR size and sector size.
;	The ATR header is contained in the first 16 bytes of the first bak of the ATR entry.
;	Any software that really cares about supporting big disks is going to be using PERCOM block commands.
;	So the best model for DVSTAT is to follow here is the XF551.
;	Short disks are handled as if they were the next largest full disk.
;	- SS/SD: %000xxxxx for 128 byte/sec <= 720 sectors
;	- SS/ED: %100xxxxx for 128 byte/sec >  720 sectors
;	- SS/DD: %001xxxxx for 256 byte/sec <= 720 sectors
;	- DS/DD: %011xxxxx for 256 byte/sec >  720 sectors

	.proc set_disk_status_code

	sector_size_mode = x1
	disk_size_mode = x2

	sei
;	Activate the original The!Cart Mode
;	Do not set the configuration lock, we need to acces the full The!Cart registers
	mva #the_cart_mode.tc_mode_flexi the_cart.mode
	ldx cursor.selected_entry.start_bank_number
	ldy cursor.selected_entry.start_bank_number+1 
	jsr menu_core.set_bank

;	Check the sector size.
	mva #$00 sector_size_mode
	cpw module_a000+atr_header.sector_size #sector_size.sd
	beq is_sd
	cpw module_a000+atr_header.sector_size #sector_size.dd
	beq is_dd
	jmp coldsv
is_dd	mva #$02 sector_size_mode
is_sd

;	Check the disk size.
	mva #$01 disk_size_mode		;Assume >720
	lda module_a000+atr_header.paragraph_2
	ora module_a000+atr_header.paragraph_3
	bne is_large
	sec
	lda module_a000+atr_header.paragraph_0
	sbc #<atr_header.sd_paragraphs
	lda module_a000+atr_header.paragraph_1
	sbc #>atr_header.sd_paragraphs
	bcs is_large
	mva #$00 disk_size_mode		;Is actually <=720
is_large
	lda sector_size_mode
	ora disk_size_mode
	tax
	lda dvstat_table,x 
	ldy #starter.simulate_siov.disk_status_code-starter
	sta (starter_ptr),y
	mva #0 the_cart.primary_bank_enable
	cli
	rts
	
	.local dvstat_table
	.byte %00000000 ;for 128 byte/sec <= 720 sectors
	.byte %10000000 ;for 128 byte/sec >  720 sectors
	.byte %00100000 ;for 256 byte/sec <= 720 sectors
	.byte %01100000 ;for 256 byte/sec >  720 sectors
	.endl

	.endp				;End of set_disk_status

	.endp				;End of copy_starter

;===============================================================

	.proc prepare_boot		;Setup boot parameters and vectors
	
	mva #$31 ddevic
	mva #1   dunit			;Unit 1
	mva #'R' dcomnd
	mva #31  dtimlo
	mwa #1   daux1			;First sector number
	mwa #boot_buffer dbuflo
	mwa #sector_size.sd dbytlo
	jsr jmp_siov_ptr		;Load first sector

	mva boot_buffer+1 dbsect	;Use sector count from boot header

	lda boot_buffer+2 		;Use load address from boot header
	sta bootad
	sta dbuflo
	lda boot_buffer+3 
	sta bootad+1
	sta dbuflo+1
	
	mwa boot_buffer+4 dosini	;Use DOSINI address from boot header

	adw bootad #6 bootini		;Preprepare initialization jump
	rts

	.proc jmp_siov_ptr		;JSR wrapper procedure
	jmp (jsr_ptr)
	.endp

	.endp

;===============================================================

starter_template
	.proc starter			;Starter RAM part, must be relocatable

jmp_boot				;Fixed start address offset +0
	jmp simulate_boot
jmp_dskinv				;Fixed start address offset +3
	jmp simulate_dskinv
jmp_siov				;Fixed start address offset +6
	jmp simulate_siov

;===============================================================
;
;	Sector offsets overview
;
;	Offset	SD	DD
;	$000	HEADER	HEADER
;	$080	1	1
;	$100	2	2
;	$180	3	3
;	$200	4	4
;	$280	5
;	$300	6	5
;	$380	7
;	$400	8	6

	.proc simulate_dskinv		;Minimum setup only
	lda #$31
	sta ddevic
	lda #1
	sta dunit
	.endp				;Fall through

;===============================================================

	.proc simulate_siov
	lda ddevic			;Is device "Dx:"?
	cmp #$31
	jne siov			;No, resort to normal SIO, should never be the case...

	sei				;Disable TRIG3 check for setting The!Cart registers and shadow register copying

	.proc disable_screen		;Create empty DL and set hardware pointers to it.
	lda sdmctl			;Screen is already off
	beq dl_not_in_module
	lda sdlsth
	cmp #>module_a000
	bcc dl_not_in_module		;Do not disable the screen, if the DL is not in the module area.

	lda #$41			;Create empty DL in zero page
	ldx #<empty_dl
	ldy #>empty_dl
	sta empty_dl
	stx empty_dl+1
	sty empty_dl+2

	sta wsync			;Change ANTIC pointer at the begin of the next scanline and before the module is enabled
	stx dlptr
	sty dlptr+1

dl_not_in_module
	.endp				;End of disable_screen

;===============================================================

	lda #$00			;The ROM part of the starter is in bank 0 at menu_atr_rom
	sta the_cart.primary_bank_lo	;Set primary bank register low byte (0-255, default: 0), also enables the cart (!)
	sta the_cart.primary_bank_hi	;Set primary bank register high byte (0-63, default: 0), also enables the cart (!)

disk_status_code = *+1
	lda #$20			;Set during initialization based on ATR header
start_bank_number_lo = *+1		;Set during initialization based on menu entry
	ldx #$ff
start_bank_number_hi =*+1		;Set during initialization based on menu entry
	ldy #$ff
	jsr start_atr_file_rom.special_sio 	;Call the ROM part of the starter routine
	mva #0 the_cart.primary_bank_enable	;Disable The!Cart again, so it is off in case we just return 
	bcs return_with_status			;If <C>=1, then <Y>=SIO status code to be returned
	
	mva cart_bank the_cart.primary_bank_lo		;Set primary bank register low byte (0-255, default: 0), also enables the cart (!)
	mva cart_bank+1 the_cart.primary_bank_hi	;Set primary bank register high byte (0-63, default: 0), also enables the cart (!)

;===============================================================

	.proc copy_sector

	ldy #0				;Index counter
	ldx #0				;Constant zero to disable the The!Cart
	lda buffer_ptr+1		;Will the buffer and the ROM overlap potentially?
	cmp #>[module_a000-$100]	;Must consider the last page especially
	bcs switch_per_byte		;Yes, so switch per byte
	
	.proc switch_per_sector
	lda #1
	sta the_cart.primary_bank_enable;Enable The!Cart

loop	lda (cart_ptr),y
	sta (buffer_ptr),y
	iny
	cpy buffer_len			;Compare with length in ZP
	bne loop
	stx the_cart.primary_bank_enable;Disable The!Cart
	beq done			;<Z>=1
	.endp

	.proc switch_per_byte
loop	lda #1
	sta the_cart.primary_bank_enable;Enable The!Cart
	lda (cart_ptr),y
	stx the_cart.primary_bank_enable;Disable The!Cart
	sta (buffer_ptr),y
	iny
	cpy buffer_len			;Compare with length in ZP
	bne loop
	.endp
done
	.endp

;===============================================================

	ldy #1				;Return with DSTATS=1, <Y>=1; <Z>=1; <N>=0
return_with_status
	sty dstats
	cpy #1
	cli				;Enable VBI stage 2 again
	rts
	.endp				;End of simulate_sio

;===============================================================
;	The boot simulation is located as last part of the starter, so it can be overwritten after the boot process.
;	This increases the compatibility because only real SIO parts have to remain resident.

	.proc simulate_boot		;Must be called immediately after VBI.
	ram_start = $0a00

	.proc clear_ram			;Clear RAM up to the DL
	mva #>ram_start buffer_ptr+1
	lda #0
	sta buffer_ptr
	tay
loop	sta (buffer_ptr),y
	iny
	bne loop
	inc buffer_ptr+1
	ldx buffer_ptr+1		;Page of DL reached?
	cpx sdlsth
	bne loop
	.endp

sector_loop

jsr_siov
	jsr jmp_siov			;Must be relocated
	adw dbuflo dbytlo
	inw daux1
	dec dbsect
	bne sector_loop

jsr_bootini
	jsr jump_bootini		;Must be relocated
jsr_dosini
	jsr jump_dosini			;Must be relocated
	lda boot?			;Set bit 0 to indicate DOSINI is set
	ora #1
	sta boot?
	mva #0 coldst
	jmp warmsv

	.proc jump_bootini		;JSR wrapper procedure
	jmp (bootini)
	.endp

	.proc jump_dosini		;JSR wrapper procedure
	jmp (dosini)
	.endp

	.endp				;End of simulate_boot

	.endp				;End of starter

	m_info atr_starter.starter
	.echo "Critical SIO simulation section has  ", [starter.simulate_boot-starter]+1, " bytes."

	.endp				;End of atr_starter