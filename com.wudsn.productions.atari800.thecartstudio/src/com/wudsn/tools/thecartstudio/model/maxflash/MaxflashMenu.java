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

package com.wudsn.tools.thecartstudio.model.maxflash;

import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.thecartstudio.model.ImportableMenu;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry;

public final class MaxflashMenu extends ImportableMenu {
	private static final int MENU_LINE_LENGTH = 40;
	private static final int MENU_CONTENT_LENGTH;
	private static final byte[] MENU_TEXT_BYTES;
	private static final byte[] MENU_ENTRY_TEXT_BYTES1;
	private static final byte[] MENU_ENTRY_TEXT_BYTES2;

	static {
		MENU_CONTENT_LENGTH = 0x2000;
		MENU_TEXT_BYTES = ASCIIString.getBytes("Maxflash Menu");
		// Original version
		MENU_ENTRY_TEXT_BYTES1 = ASCIIString.getBytes("<A>");
		// Manually modified versions
		MENU_ENTRY_TEXT_BYTES2 = ASCIIString.getBytes(" A. ");

	}

	/**
	 * Creation is public.
	 * 
	 * @param content
	 *            The file content, not <code>null</code>.
	 */
	public MaxflashMenu(byte[] content) {
		super(content, "Maxflash Cartridge Studio");
	}

	/**
	 * Determine the index where the menu entries start.
	 * 
	 * @param content
	 *            The ROM content,not <code>null</code>.
	 * @return The index where the menu entries start or <code>-1</code>.
	 */
	private static int getMenuEntryIndex(byte[] content) {
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		int result = ByteArrayUtility.getIndexOf(content, 0x0000,
				MENU_CONTENT_LENGTH, MENU_ENTRY_TEXT_BYTES1);
		if (result == -1) {
			result = ByteArrayUtility.getIndexOf(content, 0x0000,
					MENU_CONTENT_LENGTH, MENU_ENTRY_TEXT_BYTES2);
		}
		return result;
	}

	@Override
	public boolean hasMenuEntries() {

		int menuTextIndex = ByteArrayUtility.getIndexOf(content, 0x0000,
				MENU_CONTENT_LENGTH, MENU_TEXT_BYTES);
		int menuEntryIndex = getMenuEntryIndex(content);
		return menuTextIndex >= 0
				|| (menuEntryIndex == 0x524 || menuEntryIndex == 0x53c
						|| menuEntryIndex == 0x5e5 || menuEntryIndex == 0x604
						|| menuEntryIndex == 0x60d || menuEntryIndex == 0x67b
						|| menuEntryIndex == 0x6a3 || menuEntryIndex == 0x6ed
						|| menuEntryIndex == 0x783 || menuEntryIndex == 0x8db);
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
			byte[] signature = ASCIIString
					.getBytes("Maxflash Menu Software, Copyright 2009 Steven J Tucker");
			int signatureIndex = ByteArrayUtility.getIndexOf(content, 0,
					MENU_CONTENT_LENGTH, signature);
			int menuEntryIndex = getMenuEntryIndex(content);

			byte[] oldMenuVectors = new byte[] { (byte) 0x4c, (byte) 0xad,
					(byte) 0xb6, (byte) 0x00, (byte) 0x01, (byte) 0xad,
					(byte) 0xb6 };
			int oldMenuVectorsIndex = content.length - 7;
			int oldMenuPointerIndex = oldMenuVectorsIndex - 2;
			boolean oldMenu = ByteArrayUtility.getIndexOf(content,
					oldMenuVectorsIndex, 7, oldMenuVectors) == oldMenuVectorsIndex;

			int menuVersion = -1;
			int menuEntriesOffset = -1;
			int menuEntriesSkip = 6;
			switch (signatureIndex) {

			case 0x1e1c:
				menuVersion = 0;
				menuEntriesOffset = 0x1045;
				break;

			case 0x1e35:
				menuVersion = 0;
				menuEntriesOffset = 0x105e;
				break;

			case 0x1f47: // Version 2.4 (Build 11/25/2013)
				menuVersion = Version.ATARIMAX_NEW;
				menuEntriesOffset = 0x1170;
				break;

			case 0x1f36:
				menuVersion = 0;
				menuEntriesOffset = 0x115e;
				break;
			default:
				switch (menuEntryIndex) {
				case 0x524:
				case 0x53c:
				case 0x5e5:
				case 0x604:
				case 0x60d:
				case 0x67b:
				case 0x6a3:
				case 0x6ed:
				case 0x783:
				case 0x8db:
					menuVersion = 0;
					menuEntriesOffset = menuEntryIndex;
					menuEntriesSkip = 4;
					break;
				default:
					if (oldMenu) {
						menuVersion = Version.ATARIMAX_OLD;
						menuEntriesOffset = content.length - 0x02000
								+ getWord(oldMenuPointerIndex) - 0xa000;

					} else {
						return Result.NOT_SUPPORTED_MENU_VERSION_FOUND;
					}
				}
			}

			int menuEntriesCount;
			if (oldMenu) {
				collector.collectMenu(owner, "MaxflashMenu-old");

				menuEntriesCount = getByte(menuEntriesOffset++);
				while (menuEntriesOffset < content.length
						&& getByte(menuEntriesOffset) != 0x9b) {
					menuEntriesOffset++;
				}
				menuEntriesOffset++; // Skip $9b

				int entryPointer = getWord(menuEntriesOffset);
				int j = 0;
				while (entryPointer != 0) {
					entryPointer += content.length - 0x02000 - 0x2000 + 4;
					StringBuilder titleBuilder = new StringBuilder(
							WorkbookEntry.TITLE_LENGTH);
					char c;
					while (entryPointer < content.length
							&& (c = (char) getByte(entryPointer)) != 0x9b) {
						titleBuilder.append(ATASCII[c]);
						entryPointer++;
					}
					collector.collectMenuEntry(owner, menuVersion, j,
							titleBuilder.toString());
					menuEntriesOffset += 2;
					entryPointer = getWord(menuEntriesOffset);
					j++;
				}

				// Old Menu
				menuEntriesCount = 1;
				if (menuVersion != 0) {
					return Result.MENU_ENTRIES_FOUND_AND_STARTABLE;
				}
				return Result.MENU_ENTRIES_FOUND_BUT_NOT_STARTABLE;

			}
			// New Menu
			collector.collectMenu(owner,
					"MaxflashMenu-$" + Integer.toHexString(signatureIndex)
							+ "-$" + Integer.toHexString(menuEntryIndex));
			menuEntriesCount = getByte(menuEntriesOffset - 1);

			if (menuEntriesCount >= 0) {

				// For a 1 MB Atarimax module, the maximum number of entries is
				// 126 because the bank 0 and 127 are reserved for the menu and
				// powerup code.
				// Maxflash Studio only reserves about 2000 bytes for the title
				// texts. Title texts are store are variable length strings.
				// Typically the entry space on the cartridge runs out for the
				// entry data, before the title space runs out for the titles.
				// But due to a missing warning in Maxflash studio, the title
				// space can overwrite the entry space if many long titles are
				// used. See
				// http://www.atarimax.com/flashcart/forum/viewtopic.php?f=3&t=1473
				menuEntriesCount = Math.min(menuEntriesCount, 127);
				boolean corruptedEndFound = false;
				for (int j = 0; j < menuEntriesCount && !corruptedEndFound; j++) {

					menuEntriesOffset += menuEntriesSkip;
					StringBuffer titleBuilder = new StringBuffer(
							WorkbookEntry.TITLE_LENGTH);
					while (menuEntriesOffset < MENU_CONTENT_LENGTH
							&& getByte(menuEntriesOffset) != 0x9b) {
						char c = (char) (getByte(menuEntriesOffset));
						titleBuilder.append(c);
						menuEntriesOffset++;
					}
					menuEntriesOffset++; // Skip 0x9b
					if (titleBuilder.length() <= MENU_LINE_LENGTH) {
						collector.collectMenuEntry(owner, menuVersion, j,
								titleBuilder.toString());
					} else {
						corruptedEndFound = true;
					}
				}

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
