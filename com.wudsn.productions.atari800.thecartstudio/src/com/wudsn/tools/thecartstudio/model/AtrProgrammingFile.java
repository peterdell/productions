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

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.wudsn.tools.base.atari.AtrFile;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;

/**
 * Data container for reading and parsing flash programming images in ATR
 * format. A disk image consists of an ATR header, 2 bytes in the first boot
 * sector, the actual boot code and the remaining input content.
 */
public final class AtrProgrammingFile {

	/**
	 * Definition of an Atarimax Maxflash build.
	 */
	public static final class BuildInfo {
		public final String signature;
		public final int signatureOffset;
		public final int mode;
		public final int modeOffset;

		public BuildInfo(String signature, int signatureOffset, int mode,
				int modeOffset) {

			// The offsets in the definition are absolute file offsets.
			this.signature = signature;
			this.signatureOffset = signatureOffset;

			this.mode = mode;
			this.modeOffset = modeOffset;
		}

		@Override
		public String toString() {
			return "signature=" + signature + ", signatureOffset=0x"
					+ Integer.toHexString(signatureOffset) + ",mode=" + mode
					+ ", modeOffset=0x" + Integer.toHexString(modeOffset);
		}
	}

	// Loaded information.
	private File sourceFile;
	private long sourceFileSize;
	private InputStream inputStream;
	private byte[] sourceFileContent;

	private byte[] atrHeader;
	private int atrHeaderSize;
	private byte[] bootSectorHeader;
	private int bootSectorHeaderSize;
	private int bootSectors;
	private byte[] bootCode;

	// Derived information.
	private CartridgeType cartridgeType;
	private BuildInfo buildInfo;

	public AtrProgrammingFile(File sourceFile) {
		if (sourceFile == null) {
			throw new IllegalArgumentException(
					"Parameter 'sourceFile' must not be null.");
		}
		this.sourceFile = sourceFile;
		this.sourceFileSize = sourceFile.length();
		atrHeader = new byte[AtrFile.HEADER_SIZE];
		bootSectorHeader = new byte[2];
	}

	public String getSourceFilePath() {
		return sourceFile.getAbsolutePath();
	}

	public int getSourceFileContentOffset() {
		return atrHeaderSize + bootCode.length;
	}

	public int getSourceFileContentSize() {
		return (int) (sourceFileSize - getSourceFileContentOffset());
	}

	public byte[] getSourceFileContent() {
		return sourceFileContent;
	}

	public byte[] getBootCode() {
		return bootCode;
	}

	public CartridgeType getCartridgeType() {
		return cartridgeType;
	}

	public BuildInfo getBuildInfo() {
		return buildInfo;
	}

	public boolean loadHeadersAndBootCode() throws CoreException {
		inputStream = FileUtility.openInputStream(sourceFile);

		// Load ATR header plus boot sector count
		try {
			atrHeaderSize = inputStream.read(atrHeader);
			bootSectorHeaderSize = inputStream.read(bootSectorHeader);
		} catch (IOException ex) {
			// ERROR: Cannot read content of file '{0}'.
			// Original error message: {1}
			throw new CoreException(com.wudsn.tools.base.Messages.E206,
					sourceFile.getAbsolutePath(), ex.getLocalizedMessage());
		}

		// If the file is valid ATR and has boot sectors...
		if (atrHeaderSize == atrHeader.length && AtrFile.isHeader(atrHeader)
				&& bootSectorHeaderSize == bootSectorHeader.length) {

			// Only low byte of boot sectors count is relevant.
			bootSectors = 0xff & bootSectorHeader[1];
			bootCode = new byte[bootSectors * AtrFile.SECTOR_SIZE_SD];
			try {
				inputStream.read(bootCode, bootSectorHeaderSize,
						bootCode.length - bootSectorHeaderSize);
			} catch (IOException ex) {
				// ERROR: Cannot read content of file '{0}'.
				// Original error message: {1}
				throw new CoreException(com.wudsn.tools.base.Messages.E206,
						sourceFile.getAbsolutePath(), ex.getLocalizedMessage());
			}
			return true;
		}
		return false;
	}

	/**
	 * Determine if a byte array contains an ASCII string is at a given index.
	 * 
	 * @param bytes
	 *            The byte array to search in, may be empty, not
	 *            <code>null</code>.
	 * @param string
	 *            The ASCII string to search for, may be empty, not
	 *            <code>null</code> .
	 * @param index
	 *            The index where the string is expected, a non-negative
	 *            integer.
	 * @return <code>true</code> if the byte array contains that ASCII string at
	 *         the given index, <code>false</code> otherwise.
	 */
	public static boolean containsStringAt(byte[] bytes, String string,
			int index) {
		if (bytes == null) {
			throw new IllegalArgumentException(
					"Parameter 'bytes' must not be null.");
		}
		if (string == null) {
			throw new IllegalArgumentException(
					"Parameter 'string' must not be null.");
		}
		if (index < 0) {
			throw new IllegalArgumentException(
					"Parameter 'index' must not be negative.");
		}
		int endIndex = index + string.length();
		if (endIndex > bytes.length - 1) {
			return false;
		}
		byte[] stringBytes = ASCIIString.getBytes(string);
		int j = 0;
		for (int i = index; i < endIndex; i++) {
			if (bytes[i] != stringBytes[j++]) {
				return false;
			}
		}
		return true;
	}

	public void setCartridgeTypeAndBuildInfo(CartridgeType cartridgeType,
			BuildInfo buildInfo) {
		this.cartridgeType = cartridgeType;
		this.buildInfo = buildInfo;

	}

	public byte[] createCartridgeContent() throws CoreException {
		byte[] cartridgeContent = new byte[cartridgeType.getSizeInKB() * KB];
		sourceFileContent = FileUtility.readBytes(getSourceFilePath(),
				inputStream, cartridgeContent.length, false);
		return cartridgeContent;

	}

	public void close() {
		if (inputStream != null) {
			try {
				FileUtility.closeInputStream(sourceFile, inputStream);
			} catch (CoreException ignore) {

			}
		}
	}

}