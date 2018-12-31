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

import java.util.Arrays;

/**
 * Wrapper class for byte array to simplify access without casting.
 * 
 * @author Peter Dell
 */
public final class ByteArray {
    private final byte[] content;

    public ByteArray(byte[] content) {
	if (content == null) {
	    throw new IllegalArgumentException("Parameter 'content' must not be null.");
	}
	this.content = content;
    }

    public byte[] getBytes() {
	return content;

    }

    public int getSize() {
	return content.length;
    }

    public boolean equals(ByteArray other) {
	return Arrays.equals(this.content, other.content);
    }

    public int getByte(int offset) {
	return content[offset] & 0xff;
    }

    public void setByte(int offset, int value) {
	content[offset] = (byte) (value & 0xff);
    }

}
