#ifndef CARTDEF_H
#define CARTDEF_H

/*
   CartDef.h - The!Cart definitions

   Copyright (C) 2013 Matthias Reichl <hias@horus.com>

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/

#define	BANKSIZE		8192
#define BANKS_PER_BLOCK		16

// checksum blocksize
#define BLOCKSIZE		(BANKSIZE*BANKS_PER_BLOCK)

#define TOTALBANKS_64MB		8192
#define TOTALBANKS_128MB	16384

#define BLOCKS_PER_DD_ATR	(16*1024*1024 / BLOCKSIZE - 1)

#define HASH_BLOCK_START	1
#define HASH_BLOCK_COUNT	1

#define HASH_LEN		64

#define FIRST_USER_BANK		((CKSUM_BLOCK + CKSUM_BLOCK_COUNT) * BANKS_PER_BLOCK)

// maximum length of image name (end with EOL if shorter)
#define IMG_NAME_LEN		40

// fixed length of timestamp, including EOL
// format is "YYYY-DD-MM HH:MM:SS\233"
#define IMG_TIMESTAMP_LEN	20

// cart info is stored in the HASH area
#define CARTINFO_OFS		(HASH_BLOCK_START * BLOCKSIZE + HASH_BLOCK_START * HASH_LEN)

// signature
#define SIGNATURE_OFS		CARTINFO_OFS
#define SIGNATURE_LEN		16
#define SIGNATURE_DATA		"The!CartCSUM0100"

// number of allocated blocks
#define BLOCKUSE_OFS		(CARTINFO_OFS + 16)
#define BLOCKUSE_LEN		2

// image name
#define IMGNAME_OFS		(CARTINFO_OFS + 24)
#define IMGNAME_LEN		IMG_NAME_LEN

// programming header

// signature identifying programming file
#define HDR_SIGNATURE_OFS	0
#define HDR_SIGNATURE_LEN	16
#define HDR_SIGNATURE_DATA	"The!CartPROG0100"

// total 64k blocks in this programming (multi-) file
#define HDR_BLOCKS_OFS		16
#define HDR_BLOCKS_LEN		2

// start of checksum area (in 64k blocks, 0 = no checksum)
#define HDR_CS_START_OFS	18
#define HDR_CS_START_LEN	2

// number of 64k checksum blocks (0 = no checksums)
#define HDR_CS_COUNT_OFS	20

// total images/parts
#define HDR_TOTAL_PARTS_OFS	22
#define HDR_TOTAL_PARTS_LEN	1

// current image/part number (starting with 0)
#define HDR_PARTNO_OFS		23
#define HDR_PARTNO_LEN		1

// image name
#define HDR_NAME_OFS		32
#define HDR_NAME_LEN		IMG_NAME_LEN

// image timestamp
#define HDR_TIMESTAMP_OFS	72
#define HDR_TIMESTAMP_LEN	IMG_TIMESTAMP_LEN

// hash of hashblock, used to identify multi-images
#define HDR_HASH_OFS		128
#define HDR_HASH_LEN		64

// usage bitmap
#define HDR_BITMAP_OFS		4096
#define HDR_BITMAP_LEN		2048


#endif
