package com.wudsn.tools.thecartstudio.directory;

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

import java.io.ByteArrayOutputStream;

import com.wudsn.tools.base.common.ASCIIString;

public class DirectoryWriter {
	private ByteArrayOutputStream outputStream;

	public DirectoryWriter() {

		this.outputStream = new ByteArrayOutputStream();
	}

	public int getSize() {
		return outputStream.size();
	}

	public void writeByte(int value) {
		outputStream.write(value);
	}

	public void writeWord(int value) {
		outputStream.write(value);
		outputStream.write(value >>> 8);
	}

	public void writeString(String value) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Parameter 'value' must not be null.");
		}
		byte[] bytes = ASCIIString.getBytes(value);
		for (int i = 0; i < bytes.length; i++) {
			outputStream.write(bytes[i]);
		}
	}

	public void writePaddingBytes(int number) {
		for (int i = 0; i < number; i++) {
			outputStream.write(0);
		}
	}

	public byte[] toByteArray() {

		return outputStream.toByteArray();
	}

}
