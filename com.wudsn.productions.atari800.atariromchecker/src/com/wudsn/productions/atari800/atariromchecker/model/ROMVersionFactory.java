/**
 * Copyright (C) 2014 <a href="https://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of the Atari ROM Checker.
 * 
 * ROM Checker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * ROM Checker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Atari ROM Checker  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wudsn.productions.atari800.atariromchecker.model;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabaseEntry;
import com.wudsn.tools.base.common.ByteArrayUtility;

/**
 * 
 * 
 * @author Peter Dell
 * 
 */
public final class ROMVersionFactory {

    public static ROMVersion createCartridgeDatabaseEntryROMVersion(
	    CartridgeDatabaseEntry carridgeDatabaseEntry, String md5HexString) {
	if (carridgeDatabaseEntry == null) {
	    throw new IllegalArgumentException(
		    "Parameter carridgeDatabaseEntry must not be null.");
	}
	return createCartridgeTypeROMVersion(
		carridgeDatabaseEntry.getSizeInKB() * ByteArrayUtility.KB,
		carridgeDatabaseEntry.getCRC32HexString(), md5HexString,
		carridgeDatabaseEntry.getCartridgeType(),
		carridgeDatabaseEntry.getTitle(),
		carridgeDatabaseEntry.getPublisher(),
		carridgeDatabaseEntry.getDate());

    }

    public static ROMVersion createCartridgeTypeROMVersion(ROM rom,
	    CartridgeType carridgeType, String title, String publisher,
	    String date) {
	return createCartridgeTypeROMVersion(rom.getSize(),
		rom.getCRC32HexString(), rom.getMD5HexString(), carridgeType,
		title, publisher, date);

    }

    public static ROMVersion createCartridgeTypeROMVersion(int size,
	    String crc32HexString, String md5HexString,
	    CartridgeType carridgeType, String title, String publisher,
	    String date) {
	if (crc32HexString == null) {
	    throw new IllegalArgumentException(
		    "Parameter crc32HexString must not be null.");
	}
	if (md5HexString == null) {
	    throw new IllegalArgumentException(
		    "Parameter md5HexString must not be null.");
	}
	if (carridgeType == null) {
	    throw new IllegalArgumentException(
		    "Parameter carridgeType must not be null.");
	}
	if (title == null) {
	    throw new IllegalArgumentException(
		    "Parameter title must not be null.");
	}
	String type = carridgeType.toString();
	if (carridgeType != CartridgeType.UNKNOWN) {
	    type = type + " (" + carridgeType.getNumericId() + ")";
	}
	ROMVersion romVersion;
	romVersion = ROMVersion.createInstance(size, crc32HexString,
		md5HexString, type, title, publisher, date, "", "", "", "");
	return romVersion;
    }
}