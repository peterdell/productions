The!Cart software
(c) 2013-2017 Matthias Reichl <hias@horus.com>

1. What's in this package

This package contains the Atari software and source code for The!Cart:

- flasher
- simple configuration menu (cartmenu.rom)

Software updates are available here:
http://www.horus.com/~hias/thecart/


2. Version history:

2017-09-24
==========

* Update MyPicoDos to version 4.06

* Correct wrong URL in readme.txt

* Fix assembly on ATasm 1.07

2014-04-07
==========

Fixed loading of OSS carts in MyPicoDos CAR loader

2014-01-15
==========

* Fix several flasher issues:
  - only ask for a disk when data will actually be read from it
  - add retry option on I/O errors
  - don't print empty bank numbers
  - in non-incremental mode program checksum block after data
    has been programmed (like in incremental mode)

* Display software version in the!cart config menu

* Check SHIFT key (to keep main config registers enabled) when
  disabling the!cart with ESC from the config menu and when
  OPTION is pressed during boot

2013-12-01
==========

* support configuration locking option of latest CPLD logic (131201):
  The!Cart configuration registers at $D5Ax are now disabled by
  default when choosing a mode other than the native The!Cart mode.
  Starting a cartridge with SHIFT+RETURN restores the previous
  behaviour (i.e. The!Cart configuration registers are still
  available at $D5Ax)

* Add Autorun option:
  If enabled the selected mode will be automatically started when
  powering on the Atari. Autorun can be turned off by pressing
  SELECT (either at powerup or when the Autorun message appears)

* Add Cart Disable option:
  Pressing "OPTION" during powerup disables The!Cart. This is
  identical to selecting "Mode: off" in the configuration menu
  or pressing ESC.

* Add CAR loaded to builtin MyPicoDos:
  MyPicoDos can now load CAR files directly into the The!Cart SRAM,
  providing a quick and convenient way to test cartridge images.
  All cartridge types up to 512k are supported. Atarimax 8MBit CAR
  files are also supported, but a warning will be shown and only
  the first 512k will be loaded.

2013-06-05:
===========
Initial public release


3. Usage information

thecart.atr contains the flasher (FLASH.COM) and the simple cartridge
menu (CARTMENU.ROM).

It's recommended to use The!Cart studio on your PC and then use
the "Program cartridge" option of the flasher to perform incremental
updates. The!Cart Studio can be downloaded from here:

http://www.wudsn.com/productions/atari800/thecartstudio/help/TheCartStudio.html

If you completely erased The!Cart, load the flasher from thecart.atr
and then flash the Studio programming file as usual. This will
bring back the menu.

If you don't want to use The!Cart Studio at all, use the
"Program from RAW file" option to program the CARTMENU.ROM
starting from bank 0.

In case you want to use both The!Cart Studio on your PC and also
do manual flashing from your Atari be careful:

* Reserve some user space at the end of The!Cart in the Studio.
* Only use the reserved user space area for manual programming.

If you accidentally overwrite (parts of) the space used by
The!Cart studio, use the full update option to reprogram the
Studio programming file (answer "no" when asked if an incremental
update should be performed)

