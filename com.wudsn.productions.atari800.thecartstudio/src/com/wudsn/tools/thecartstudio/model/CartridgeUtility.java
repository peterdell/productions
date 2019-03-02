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

/**
 * Offset are given as negative offsets, relative to the end of the start bank.
 * 
 * @author Peter Dell
 */
public final class CartridgeUtility {

	/**
	 * Creation is private,
	 */
	private CartridgeUtility() {

	}

	// Start address vector, used if CARTFG has CARTFG_START_CART bit set.
	public static final int CARTCS_OFFSET = 0x1ffa - 0x2000;

	// Flag, must be zero for modules
	public static final int CART_OFFSET = 0x1ffc - 0x2000;

	// Flags, indicating how to start the module.
	public static final int CARTFG_OFFSET = 0x1ffd - 0x2000;

	// Flag value: Directly jump via CARTAD during RESET.
	public static final byte CARTFG_DIAGNOSTIC_CART = (byte) 0x80;

	// Flag value: Jump via CARTAD and then via CARTCS.
	public static final byte CARTFG_START_CART = (byte) 0x04;

	// Flag value: Boot peripherals, then start the module.
	public static final byte CARTFG_BOOT = (byte) 0x01;

	// Initialization address vector.
	public static final int CARTAD_OFFSET = 0x1ffe - 0x2000;

	/**
	 * Sets the vectors and flags in a module.
	 * 
	 * @param content
	 *            The content to be updated, not <code>null</code>.
	 * @param startBankOffset
	 *            The offset in the content to the byte at address $a000.
	 * @param startBankSize
	 *            The size of the bank that starts at startBankOffset.
	 * @param flags
	 *            The flags, see {@link #CARTFG_DIAGNOSTIC_CART},
	 *            {@link #CARTFG_START_CART}, {@link #CARTFG_BOOT}.
	 * @param initAddress
	 *            The init address of the module, must between $8000-$bfff.
	 * @param startAddress
	 *            The start address of the module, must between $8000-$bfff or
	 *            0x0000.
	 */
	public static void setCartridgeVectors(byte[] content, int startBankOffset,
			int startBankSize, int flags, int initAddress, int startAddress) {
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		if (startBankOffset < 0) {
			throw new IllegalArgumentException(
					"Parameter 'startBankOffset' must not be negative. Specified value is "
							+ startBankOffset + ".");
		}
		if (startBankSize < 0) {
			throw new IllegalArgumentException(
					"Parameter 'startBankSize' must not be negative. Specified value is "
							+ startBankSize + ".");
		}

		// For modules with less than 8k, the address space is mirrored.
		// In this case vectors are located at the end of the first mirror.
		int startBankEndOffset = startBankOffset + startBankSize;

		if (initAddress < 0x4000 || initAddress > 0xbfff) {
			throw new IllegalArgumentException(
					"Parameter 'initAddress' must be between 0x8000 and 0xbfff. Specified value is "
							+ Integer.toHexString(initAddress) + ".");
		}
		if (startAddress != 0x0000
				&& (startAddress < 0x4000 || startAddress > 0xbfff)) {
			throw new IllegalArgumentException(
					"Parameter 'startAddress' must be between 0x8000 and 0xbfff. Specified value is "
							+ Integer.toHexString(startAddress) + ".");
		}

		// Align to end of start bank.
		try {
			content[startBankEndOffset + CARTCS_OFFSET] = (byte) (startAddress & 0xff);
			content[startBankEndOffset + CARTCS_OFFSET + 1] = (byte) (startAddress >> 8 & 0xff);

			content[startBankEndOffset + CART_OFFSET] = 0x00;
			content[startBankEndOffset + CARTFG_OFFSET] = (byte) (flags & 0xff);

			content[startBankEndOffset + CARTAD_OFFSET] = (byte) (initAddress & 0xff);
			content[startBankEndOffset + CARTAD_OFFSET + 1] = (byte) (initAddress >> 8 & 0xff);
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new RuntimeException(ex);
		}
	}
}
