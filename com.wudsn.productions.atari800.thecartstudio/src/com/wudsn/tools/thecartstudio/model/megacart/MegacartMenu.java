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

package com.wudsn.tools.thecartstudio.model.megacart;

import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.thecartstudio.model.ImportableMenu;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry;

public final class MegacartMenu extends ImportableMenu {
	private static final int MENU_CONTENT_LENGTH;
	private static final byte[] MENU_TEXT_BYTES;

	static {
		MENU_CONTENT_LENGTH = 0x4000;
		MENU_TEXT_BYTES = ASCIIString.getBytes("BY BERND HERALE - VERSION");
	}

	/**
	 * Creation is public.
	 * 
	 * @param content
	 *            The file content, not <code>null</code>.
	 */
	public MegacartMenu(byte[] content) {
		super(content, "MegaCart Studio");
	}

	@Override
	public boolean hasMenuEntries() {
		return ByteArrayUtility.getIndexOf(content, 0x0000,
				MENU_CONTENT_LENGTH, MENU_TEXT_BYTES) >= 0;
	}

	@Override
	public int collectMenuEntries(Object owner, Collector collector) {
		if (owner == null) {
			throw new IllegalArgumentException(
					"Parameter 'owner' must not be null.");
		}
		if (collector == null) {
			throw new IllegalArgumentException(
					"Parameter 'collector' must not be null.");
		}

		if (content.length > MENU_CONTENT_LENGTH) {

			// Find signature.
			byte[] signature = MENU_TEXT_BYTES;
			int signatureIndex = ByteArrayUtility.getIndexOf(content, 0,
					MENU_CONTENT_LENGTH, signature);

			if (signatureIndex == -1) {
				return Result.NOT_SUPPORTED_MENU_VERSION_FOUND;
			}

			int menuEntriesCount = 0;
			int menuVersion;
			// stx $7c20, lda $7c20, then the start is at signatureIndex+3
			signature = new byte[] { (byte) 0xAE, (byte) 0x20, (byte) 0x7C,
					(byte) 0x8E, (byte) 0x20, (byte) 0x7C };
			signatureIndex = ByteArrayUtility.getIndexOf(content, 0,
					MENU_CONTENT_LENGTH, signature);
			if (signatureIndex == 0x37ea) {
				menuVersion = Version.MEGACART;
			} else {
				menuVersion = 0;
			}
			int offset = 0x7;

			collector.collectMenu(owner, "MegacartMenu: signatureIndex="
					+ Integer.toHexString(signatureIndex));

			for (int j = 0; j < 254; j++) {
				menuEntriesCount = j;

				if (getByte(offset) == 0xff) {
					break; // break for loop
				}
				StringBuilder titleBuilder = new StringBuilder(
						WorkbookEntry.TITLE_LENGTH);
				for (int i = 0; i < 33; i++) {
					char c = ATASCII[getByte(offset + i)];
					titleBuilder.append(c);
				}
				collector.collectMenuEntry(owner, menuVersion, j,
						titleBuilder.toString());
				offset += 40;
			}

			if (menuEntriesCount > 0) {
				if (menuVersion != 0) {
					return Result.MENU_ENTRIES_FOUND_AND_STARTABLE;
				}
				return Result.MENU_ENTRIES_FOUND_BUT_NOT_STARTABLE;
			}
			return Result.NO_MENU_ENTRIES_FOUND;
		}

		return Result.NO_MENU_FOUND;
	}

}
