/**
 * Copyright (C) 2013 - 2019 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of The!Cart Studio distribution.
 * 
 * The!Cart Studio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The!Cart Studio distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with The!Cart Studio. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wudsn.tools.thecartstudio.model;

import com.wudsn.tools.base.common.ASCIIString;

/**
 * Based on "CartDef.h" by Matthias Reichl <hias@horus.com>, version from
 * 2013-06-03, transferred to Java by Peter Dell.
 * 
 * Bank 0-15: cartmenu.rom (128k) Bank 16-31: check sums (128k) Bank 32-..:
 * cartridge data
 * 
 * @author Matthias Reichl
 * @author Peter Dell
 */

public final class CartDef {

	public final static int BANKSIZE = 8192;
	public final static int BANKS_PER_BLOCK = 16;

	// checksum blocksize
	public final static int BLOCKSIZE = (BANKSIZE * BANKS_PER_BLOCK);

	public final static int BLOCKS_PER_DD_ATR = (16 * 1024 * 1024 / BLOCKSIZE - 1);

	public final static int HASH_BLOCK_START = 1;
	public final static int HASH_BLOCK_COUNT = 1;

	public final static int HASH_LEN = 64;

	// maximum length of image name (end with EOL if shorter)
	public final static int IMG_NAME_LEN = 40;

	// fixed length of timestamp, including EOL
	// format is "YYYY-DD-MM HH:MM:SS\233"
	public final static int IMG_TIMESTAMP_LEN = 20;

	// cart info is stored in the HASH area
	public final static int CARTINFO_OFS = (HASH_BLOCK_START * BLOCKSIZE + HASH_BLOCK_START
			* HASH_LEN);

	// signature
	public final static int SIGNATURE_OFS = CARTINFO_OFS;
	public final static int SIGNATURE_LEN = 16;
	public final static byte[] SIGNATURE_DATA = ASCIIString
			.getBytes("The!CartCSUM0100");

	// number of allocated blocks
	public final static int BLOCKUSE_OFS = (CARTINFO_OFS + 16);
	public final static int BLOCKUSE_LEN = 2;

	// image name
	public final static int IMGNAME_OFS = (CARTINFO_OFS + 24);
	public final static int IMGNAME_LEN = IMG_NAME_LEN;

	// programming header

	// signature identifying programming file
	public final static int HDR_SIGNATURE_OFS = 0;
	public final static int HDR_SIGNATURE_LEN = 16;
	public final static byte[] HDR_SIGNATURE_DATA = ASCIIString
			.getBytes("The!CartPROG0100");

	// total 64k blocks in this programming (multi-) file
	public final static int HDR_BLOCKS_OFS = 16;
	public final static int HDR_BLOCKS_LEN = 2;

	// start of checksum area (in 64k blocks, 0 = no checksum)
	public final static int HDR_CS_START_OFS = 18;
	public final static int HDR_CS_START_LEN = 2;

	// number of 64k checksum blocks (0 = no checksums)
	public final static int HDR_CS_COUNT_OFS = 20;

	// total images/parts
	public final static int HDR_TOTAL_PARTS_OFS = 22;
	public final static int HDR_TOTAL_PARTS_LEN = 1;

	// current image/part number (starting with 0)
	public final static int HDR_PARTNO_OFS = 23;
	public final static int HDR_PARTNO_LEN = 1;

	// image name
	public final static int HDR_NAME_OFS = 32;
	public final static int HDR_NAME_LEN = IMG_NAME_LEN;

	// image timestamp
	public final static int HDR_TIMESTAMP_OFS = 72;
	public final static int HDR_TIMESTAMP_LEN = IMG_TIMESTAMP_LEN;

	// hash of hashblock, used to identify multi-images
	public final static int HDR_HASH_OFS = 128;
	public final static int HDR_HASH_LEN = 64;

	// usage bitmap
	public final static int HDR_BITMAP_OFS = 4096;
	public final static int HDR_BITMAP_LEN = 2048;
}