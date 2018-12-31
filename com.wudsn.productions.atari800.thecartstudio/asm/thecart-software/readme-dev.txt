The!Cart programming information
(c) 2013 Matthias Reichl

1. General information

The!Cart is equipped with 128MB flash (Spansion S29GL01 chip),
512k RAM and a 256-byte SPI EEPROM (Microchip 25AA020A).
The memory is addressed using 16384 8k banks (64 8k banks when
using RAM instead of flash).

2. Register definition

The cartridge configuration registers are located at $D5A0-$D5A8.
All registers are read/write unless noted otherwise.
Unused bits shall be written as '0' and always read back as '0'.

Powerup configuration is 8k mode ($A000-$BFFF) using flash bank 0,
writes to flash are disabled.

Depending on the selected cartridge mode additional registers
are enabled at $D5xx.

The primary bank register also serves as a base bank register
for the various sub-modes.

The secondary bank register is only used in "flexi mode".

$D5A0: primary bank register low byte (0-255, default: 0)
$D5A1: primary bank register high byte (0-63, default: 0)
$D5A2: primary bank enable (1=enable, 0=disable, default: 1)

$D5A3: secondary bank register low byte (0-255, default: 0)
$D5A4: secondary bank register high byte (0-63, default: 0)
$D5A5: secondary bank enable (1=enable, 0=disable, default: 0)

$D5A6: cart mode select (see section on cartridge modes, default: 1 / 8k)

$D5A7: flash/ram selection and write enable control (0-15, default: 0)
bit 0: primary bank write enable (0 = write protect, 1 = write enable)
bit 1: primary bank source (0 = flash, 1 = RAM)
bit 2: secondary bank write enable (0 = write protect, 1 = write enable)
bit 3: secondary bank source (0 = flash, 1 = RAM)

$D5A8: SPI interface to EEPROM
bit 0: SPI CLK
bit 1: SPI CS
bit 7: SPI data in (on reads), SPI data out (on writes)

$D5AF: configuration lock
Writing to this register disables "The!Cart" registers at $D5Ax.

3. Supported cartridge modes

Cartridge mode is selected with bits 0-5 of $D5A6, values other
than the ones listed here are reserved (and result in "cartridge off").

$00: off, cartridge disabled
$01: 8k banks at $A000
$02: AtariMax 1MBit / 128k
$03: Atarimax 8MBit / 1MB
$04: OSS M091
$08: SDX 64k cart, $D5Ex banking
$09: Diamond GOS 64k cart, $D5Dx banking
$0A: Express 64k cart, $D57x banking
$0C: Atrax 128k cart
$0D: Williams 64k cart
$20: flexi mode (separate 8k banks at $A000 and $8000)
$21: standard 16k cart at $8000-$BFFF
$22: MegaMax 16k mode (up to 2MB), AtariMax 8Mbit banking
$23: Blizzard 16k
$24: Sic!Cart 512k
$28: 16k Mega cart
$29: 32k Mega cart
$2A: 64k Mega cart
$2B: 128k Mega cart
$2C: 256k Mega cart
$2D: 512k Mega cart
$2E: 1024k Mega cart
$2F: 2048k Mega cart
$30: 32k XEGS cart
$31: 64k XEGS cart
$32: 128k XEGS cart
$33: 256k XEGS cart
$34: 512k XEGS cart
$35: 1024k XEGS cart
$38: 32k SWXEGS cart
$39: 64k SWXEGS cart
$3A: 128k SWXEGS cart
$3B: 256k SWXEGS cart
$3C: 512k SWXEGS cart
$3D: 1024k SWXEGS cart

4. Details on cartridge modes

In addition to the main "8k" mode The!Cart introduces a very flexible
"16k" mode, called "flexi mode". The primary bank register controls
the 8k bank at $A000-$BFFF, the secondary bank register controls the 8k bank
at $8000-$9FFF.

Using the flash/ram select register it's possible to map in either RAM
or flash, and the mapped-in RAM can be configured to be read/write or
read-only. These settings can be configured independently for the $8000 and
$A000 8k banks.

Flexi mode can also be used to emulate "right" cartridges (by disabling
the primary $A000 bank and enabling only the secondary $8000) bank or
to emulate both a non-switched 8k "left" and "right" cart.

The!Cart can also emulate a wide variety of other popular cartridges,
like AtariMax, (SW)XEGS and SIC! carts. When one of these "emulated"
modes is selected, the corresponding configuration registers are
enabled in the $D5xx area.

After configuring an emulated mode the "configuration lock" register
at $D5AF should be written to disable the main configuration registers
at $D5Ax. Re-enabling the main configration registers is then
only possible by pressing the button on The!Cart or by powercycling
the Atari.

If the main configuration registers aren't disabled, the corresponding
memory adresses ($D5A0-$D5A8 and $D5AF) aren't available to the
emulated modes. For example, writing $D5A0 doesn't disable am
emulated Atarimax 8MBit cart but will change the bank register.

5. Using the bank registers

The primary bank register always addresses the memory in 8k banks.
When The!Cart is configured to 16k mode, the least significant bit
of the bank register is simply ignored.

The emulated bank registers modify the low-order bits of the primary bank
register. As a consequence, cartridge data needs to be aligned at memory blocks
of the cartridge size (for example: AtariMax 8MBit carts have to start at an
1MB boundary, i.e.  8k bank 128, 256, 384, ...)

Emulated registers of switchable carts also modify the bank enable register.

For example: Accessing $D567 in AtariMax mode sets the 7 low-order
bits of the primary bank register to $67 and the primary bank enable
register to $01. Accessing $D580 in AtariMax mode sets the primary bank
enable register to $00.

Note: in native (8k and flexi) mode writing to the primary/secondary
bank registers automatically sets the primary/secondary enable register
to $01.

