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

/**
 * Set Kr0tki's "setchecksum-800.c" and "setchecksum-xl.c" for the algorithm and
 * locations of the checksums.
 * 
 * @author Peter Dell
 */
final class ROMCheckSums {
    public static final CheckSum OS_AS_FPP = new CheckSum("FPP", 0x07fe, 0x07ff, new CheckSumEvaluator() {

	@Override
	public int evaluate(ByteArray content) {
	    if (content == null) {
		throw new IllegalArgumentException("Parameter 'content' must not be null.");
	    }
	    // Check sum of FPP ROM
	    int result = 0;
	    for (int i = 0x0000; i < 0x07fe; i++) {
		result += content.getByte(i);
	    }
	    result &= 0xffff;
	    return result;
	}
    });

    public static final CheckSum OS_AS_ROM1 = new CheckSum("OS1", 0x0c0f, 0x0c1f, new CheckSumEvaluator() {

	@Override
	public int evaluate(ByteArray content) {
	    // Checksum of first 4K ROM.
	    int result = 0;
	    for (int i = 0x0800; i < 0x0c0f; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    for (int i = 0x0c10; i < 0x0c1f; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    for (int i = 0x0c20; i < 0x1800; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    result &= 0xffff;
	    return result;
	}
    });
    public static final CheckSum OS_AS_ROM2 = new CheckSum("OS2", 0x27f8, 0x27f9, new CheckSumEvaluator() {

	@Override
	public int evaluate(ByteArray content) {
	    // Checksum of second 4K ROM.
	    int result = 0;
	    for (int i = 0x1800; i < 0x27f8; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    for (int i = 0x27fa; i < 0x2800; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    result &= 0xffff;
	    return result;
	}
    });

    public static final CheckSum OS_XL_ROM1 = new CheckSum("OS1", 0x0000, 0x0001, new CheckSumEvaluator() {
	@Override
	public int evaluate(ByteArray content) {
	    // Checksum of first 8K ROM.
	    int result = 0;
	    for (int i = 0x0002; i < 0x2000; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    result &= 0xffff;
	    return result;
	}
    });

    public static final CheckSum OS_XL_ROM2 = new CheckSum("OS2", 0x3ff8, 0x3ff9, new CheckSumEvaluator() {

	@Override
	public int evaluate(ByteArray content) {
	    // Checksum of second 8K ROM.
	    int result = 0;

	    for (int i = 0x2000; i < 0x3ff8; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    for (int i = 0x3ffa; i < 0x4000; i++) {
		result += content.getByte(i) & 0xff;
	    }
	    result &= 0xffff;
	    return result;
	}
    });
}