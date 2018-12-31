/**
 * Copyright (C) 2014 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.HexUtility;
import com.wudsn.tools.base.common.TextUtility;

public final class ROM {

    public static final int MAX_SIZE = 128 * ByteArrayUtility.MB;

    public static final int UNKNOWN = 0;
    public static final int OS_AB = 1;
    public static final int OS_XL = 2;

    private ByteArray content;
    private int type;

    // Returns long to ensure unsigned handling
    private long crc32;

    public ROM(ByteArray content, int type) {
	if (content == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'content' must not be null.");
	}
	this.content = content;
	this.type = type;

	CRC32 crc32 = new CRC32();
	crc32.update(content.getBytes());
	this.crc32 = crc32.getValue();
    }

    public CheckSum[] getCheckSums() {
	switch (type) {
	case OS_AB:
	    return new CheckSum[] { ROMCheckSums.OS_AS_FPP,
		    ROMCheckSums.OS_AS_ROM1, ROMCheckSums.OS_AS_ROM2 };
	case OS_XL:
	    return new CheckSum[] { ROMCheckSums.OS_XL_ROM1,
		    ROMCheckSums.OS_XL_ROM2 };
	default:
	    return new CheckSum[0];
	}
    }

    public int getSize() {
	return content.getSize();
    }

    public int getByte(int offset) {
	return content.getByte(offset);
    }

    public long getCRC32() {
	return crc32;
    }

    /**
     * Gets the CRC32 value as hex string with leading "0x" prefix and upper
     * case characters.
     * 
     * @return The hex string, not empty and not <code>null</code>.
     */
    public String getCRC32HexString() {
	long crc32 = getCRC32();
	return "0x" + HexUtility.getLongValueHexString(crc32, 8);
    }

    /**
     * Gets the CRC32 value as hex string with leading "0x" prefix and upper
     * case characters.
     * 
     * @return The hex string, not empty and not <code>null</code>.
     */
    public String getMD5HexString() {
	MessageDigest md5;
	try {
	    md5 = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException ex) {
	    throw new RuntimeException(ex);
	}

	md5.update(content.getBytes());
	return "0x" + ByteArrayUtility.toHexString(md5.digest());
    }

    public boolean contentEquals(ROM other) {
	return content.equals(other.content);
    }

    @Override
    public String toString() {
	return "MD5=" + getMD5HexString() + " CRC-32=" + getCRC32HexString();
    }

    /**
     * Converts a file offset into a ROM address.
     * 
     * @param offset
     *            The file offset or <code>-1</code> if the offset is not valid.
     * @return The ROM address or or <code>-1</code> if the offset is not valid.
     */
    public int toAddress(int offset) {
	switch (type) {
	case UNKNOWN:
	    return -1;
	case OS_AB:
	    if (offset >= 0 && offset < 0x2800) {
		return 0xd800 + offset;
	    }
	    break;
	case OS_XL:
	    if (offset >= 0 && offset < 0x4000) {
		if (offset >= 0x1000 && offset < 0x1800) {
		    return 0x4000 + offset;
		}
		return 0xc000 + offset;
	    }
	    break;

	default:
	    throw new RuntimeException();
	}

	return -1;
    }

    public static String toHexBytes(int value) {
	if (value < 0) {
	    return "";
	}
	int low = value & 0xff;
	int high = (value >>> 8) & 0xff;
	return toHexByte(low) + "/" + toHexByte(high);
    }

    private static String toHexByte(int value) {
	value = value & 0xff;
	if (value < 16) {
	    return "$0" + Integer.toHexString(value);
	}
	return "$" + Integer.toHexString(value);
    }

    public ROM createFixture() {
	if (type == UNKNOWN) {
	    return this;
	}

	byte[] newContentArray;
	newContentArray = new byte[content.getSize()];
	System.arraycopy(content.getBytes(), 0, newContentArray, 0,
		newContentArray.length);
	ByteArray newContent = new ByteArray(newContentArray);

	// Update check sums
	for (CheckSum checkum : getCheckSums()) {
	    checkum.setEvaluatedValue(newContent);
	}
	return new ROM(newContent, this.type);
    }

    public String getCheckSumValues() {
	// Update check sums
	StringBuilder result = new StringBuilder();
	for (CheckSum checkum : getCheckSums()) {
	    if (result.length() > 0) {
		result.append(", ");
	    }
	    int value = checkum.getCurrentValue(content);
	    result.append(TextUtility.formatAsHexaDecimal(
		    toAddress(checkum.getOffsetLowByte()),
		    Comparison.MAXIMUM_ADRESS));
	    result.append("/");
	    result.append(TextUtility.formatAsHexaDecimal(
		    toAddress(checkum.getOffsetHighByte()),
		    Comparison.MAXIMUM_ADRESS));
	    result.append("=");
	    result.append(toHexBytes(value));
	}

	return result.toString();
    }
}