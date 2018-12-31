;  flashdef.inc - The!Cart flasher definitions
;
;  Copyright (C) 2013-2014 Matthias Reichl <hias@horus.com>
;
;  This program is free software; you can redistribute it and/or modify
;  it under the terms of the GNU General Public License as published by
;  the Free Software Foundation; either version 2 of the License, or
;  (at your option) any later version.
;
;  This program is distributed in the hope that it will be useful,
;  but WITHOUT ANY WARRANTY; without even the implied warranty of
;  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;  GNU General Public License for more details.
;
;  You should have received a copy of the GNU General Public License
;  along with this program; if not, write to the Free Software
;  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

; ------------------------------------------------------------
; program locations
; ------------------------------------------------------------
; program start
START		= $4000

; 8k data buffer
DATABUF		= $2000

; ZP vectors used in libflash, can be reused
;FLASHVEC	= $E0 ; ,$E1: absolute flash address
;DATAVEC	= $E2 ; ,$E3: data buffer vector
;FLASHTMP	= $E4 ; ,$E5: temporary vector

; checksum cache (1 page each) - just before display list/screen
CSCACHE		= $9A00
FCSCACHE	= $9B00

; copies of header data
; programming file id, 64-bytes
IMAGEID		= $AF00
IMAGEIDLEN	= 64

; 8k banks per 128k block
BANKSPERBLOCK	= 16

; image use bitmap, 2k
IMAGEMAP	= $B000
IMAGEMAPLEN	= $0800

; type of RAM on flash cart
FTYPE_RAM	= TRAM512

; ------------------------------------------------------------
; flash memory definitions
; ------------------------------------------------------------
; hash area start (128k block)
HASHSTART	= 1
; number of 128k blocks in hash map
HASHCOUNT	= 1

; length of single hash in bytes
HASHLEN		= 64

; byte offsets in hash area
; info block start/length
CINFOSTART	= 64
CINFOLEN	= 64

; offsets within info block

; signature (12 bytes fixed + 2 byte major + 2 byte minor version)
CSIGNATURE	= 0
CSIGLEN		= 14
CSIGLENF	= 16

; 2 byte block count
CBLOCKCOUNT	= 16

; image name
CNAME		= 24
CNAMELEN	= 40

; ------------------------------------------------------------
; programming image definitions
; ------------------------------------------------------------

; sector of programming header
PHSECTOR_8K	= 1
PHSECTOR_DD	= 4

; first sector of image data
PISECTOR_8K	= 2
PISECTOR_DD	= 36

; blocks per DD image
PIBLOCKS_DD	= 127

; header signature (12 bytes fixed + 2 byte major + 2 byte minor version)
PHSIGNATURE	= 0
PHSIGLEN	= 14
PHSIGLENF	= 16

; 2 byte block count
PHBLOCKCOUNT	= 16

; 2 byte checksum area start (in 128k blocks, 0 = no checksum)
PHCSSTART	= 18

; number of 128k checksum blocks (0 = no checksum)
PHCSCOUNT	= 20

; total parts
PHPARTS		= 22
; current part number
PHPARTNO	= 23

; image name
PHNAME		= 32
PHNAMELEN	= 40

; image timestamp
PHTIMESTAMP	= 72
PHTIMESTAMPLEN	= 20

; image id
PHIMGID		= 128
PHIMGIDLEN	= 64

; usage bitmap
PHBITMAP	= 4096
PHBITMAPLEN	= 2048

; ------------------------------------------------------------
; other miscellaneous stuff
; ------------------------------------------------------------

; minimum drive number
MINDRIVENO	= 1
; maximum drive number
MAXDRIVENO	= 15
