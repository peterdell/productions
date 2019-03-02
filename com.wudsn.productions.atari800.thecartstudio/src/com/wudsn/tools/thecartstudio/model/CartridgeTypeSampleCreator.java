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

import java.io.File;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.atari.Platform;
import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.HexUtility;
import com.wudsn.tools.base.common.Main;
import com.wudsn.tools.base.common.ResourceUtility;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.common.TextUtility;

/**
 * Creates valid and invalid files of different types and sizes.
 * 
 * @author Peter Dell
 */
public final class CartridgeTypeSampleCreator extends Main {

	private final static class LinePrinter {
		private byte[] content;
		private int startOffset;
		private int screenWidth;

		public LinePrinter(byte[] content, int startoffset, int screenWidth) {
			this.content = content;
			this.startOffset = startoffset;
			this.screenWidth = screenWidth;
		}

		public void printfln(String text, String... parameters) {
			if (text == null) {
				throw new IllegalArgumentException(
						"Parameter text must not be null.");
			}
			text = TextUtility.format(text, parameters);
			byte[] bytes = ASCIIString.getBytesCentered(text, screenWidth);
			System.arraycopy(bytes, 0, content, startOffset, bytes.length);
			startOffset += screenWidth;
		}
	}

	private File folder;
	private Map<Platform, byte[]> binaries;
	private int fileCount;

	private void writeFile(String subFolderName, String fileName,
			CartridgeType cartridgeType, String extension, byte[] header,
			byte[] content) throws CoreException {
		if (subFolderName == null) {
			throw new IllegalArgumentException(
					"Parameter 'subFolderName' must not be null.");
		}
		if (cartridgeType == null) {
			throw new IllegalArgumentException(
					"Parameter 'fileType' must not be null.");
		}
		if (extension == null) {
			throw new IllegalArgumentException(
					"Parameter 'extension' must not be null.");
		}
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		File subFolder = new File(folder, subFolderName);
		if (!subFolder.exists()) {
			if (!subFolder.mkdir()) {
				// ERROR: Cannot create folder '{0}'.
				throw new CoreException(com.wudsn.tools.base.Messages.E202,
						subFolder.getAbsolutePath());
			}
		}
		File file;
		if (fileName == null) {
			file = new File(subFolder, cartridgeType.getText() + " ("
					+ cartridgeType.getNumericId() + ")" + extension);
		} else {
			file = new File(subFolder, fileName + extension);
		}
		OutputStream outputStream = FileUtility.openOutputStream(file);
		try {
			if (header != null) {
				FileUtility.writeBytes(file, outputStream, header, 0,
						header.length);
			}
			FileUtility.writeBytes(file, outputStream, content, 0,
					content.length);
		} finally {
			FileUtility.closeOutputStream(file, outputStream);
		}

		fileCount++;
	}

	public void run(File folder) throws CoreException {
		if (folder == null) {
			throw new IllegalArgumentException(
					"Parameter 'folder' must not be null.");
		}
		folder = folder.getAbsoluteFile();
		if (!folder.exists()) {
			// ERROR: Folder '{0} does not exist.
			throw new CoreException(com.wudsn.tools.base.Messages.E200,
					folder.getAbsolutePath());
		}

		this.folder = folder;
		this.fileCount = 0;

		// Load all platform binaries.
		binaries = new TreeMap<Platform, byte[]>();
		for (Platform platform : Platform.getValues()) {
			// These are actually BIN image without header.
			String resourcePath = platform.getCartridgeTypeSampleCreatorPath();
			if (StringUtility.isEmpty(resourcePath)) {
				continue;
			}
			byte[] binary = ResourceUtility
					.loadResourceAsByteArray(resourcePath);
			if (binary == null) {
				// ERROR: Cannot open resource '{0}' for reading. Check
				// class path and its contents.
				throw new CoreException(com.wudsn.tools.base.Messages.E216,
						resourcePath);
			}
			binaries.put(platform, binary);
		}

		println("Generating sample files in folder '"
				+ folder.getAbsolutePath() + "'.");

		// Optionally set this filter for debugging.
		CartridgeType singleCartridgeType = CartridgeType.UNKNOWN; // CartridgeType.CARTRIDGE_MEGA_2048;

		// Loop over all cartridge types.
		for (CartridgeType cartridgeType : CartridgeType.getValues()) {
			byte[] header;
			byte[] content;

			// Ignore all types with 0 or undefined fixed file size.
			int sizeInBytes = cartridgeType.getSizeInKB() * ByteArrayUtility.KB;
			if (sizeInBytes == 0) {
				continue;
			}

			// Ignore others than "singleCartridgeType" if specified.
			if (!singleCartridgeType.equals(CartridgeType.UNKNOWN)) {
				if (!cartridgeType.equals(singleCartridgeType)) {
					continue;
				}
			}

			println("Generating content sample files for cartridge type '"
					+ cartridgeType.getText() + " ("
					+ cartridgeType.getNumericId() + ")'.");

			// For The!Cart only a correct .CAR and .ROM is created, because of
			// the file size.
			if (cartridgeType != CartridgeType.CARTRIDGE_THECART_128M) {

				String context = "ROM-Correct";
				content = createContent(cartridgeType, FileHeaderType.NONE,
						sizeInBytes, true, context);
				header = null;
				writeFile(context, null, cartridgeType, ".rom", header, content);

				context = "ROM-Size-Too-Small";
				content = createContent(cartridgeType, FileHeaderType.NONE,
						sizeInBytes - 1, false, context);
				writeFile(context, null, cartridgeType, ".rom", header, content);

				context = "ROM-Size-Too-Large";
				content = createContent(cartridgeType, FileHeaderType.NONE,
						sizeInBytes + 1, true, context);
				writeFile(context, null, cartridgeType, ".rom", header, content);

				context = "CAR-Correct";
				int cartridgeTypeNumericId = cartridgeType.getNumericId();
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes, true, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeTypeNumericId, content);
				writeFile(context, null, cartridgeType, ".car", header, content);

				// Create special sub-folder for all types that are supported by
				// The!Cart.
				ContentType contentType = ContentType.getInstance(cartridgeType
						.getId());
				if (contentType == null) {
					throw new RuntimeException(
							"No content type defined for cartridge type '"
									+ cartridgeType.getId() + "'.");
				}
				if (contentType.getTheCartMode() != TheCartMode.TC_MODE_NOT_SUPPORTED) {
					context = "CAR-Correct-Supported";
					content = createContent(cartridgeType, FileHeaderType.CART,
							sizeInBytes, true, context);
					header = CartridgeFileUtility
							.createCartridgeHeaderWithCheckSum(
									cartridgeTypeNumericId, content);
					writeFile(context, null, cartridgeType, ".car", header,
							content);
				}

				context = "CAR-Size-Too-Small";
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes - 1, false, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeTypeNumericId, content);
				writeFile(context, null, cartridgeType, ".car", header, content);

				context = "CAR-Size-Too-Large";
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes + 1, true, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeTypeNumericId, content);
				writeFile(context, null, cartridgeType, ".car", header, content);

				context = "CAR-Wrong-Header";
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes, true, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeTypeNumericId, content);
				content[3] = 'Z';
				writeFile(context, null, cartridgeType, ".car", header, content);

				context = "CAR-CartridgeType-Too-Small";
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes, true, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(-1, content);
				writeFile(context, null, cartridgeType, ".car", header, content);

				context = "CAR-CartridgeType-Too-Large";
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes, true, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(Integer.MAX_VALUE,
								content);
				writeFile(context, null, cartridgeType, ".car", header, content);

				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes, true, "Wrong");
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeTypeNumericId, content);
				content[8]++;
				writeFile("CAR-Wrong-Checksum", null, cartridgeType, ".car",
						header, content);

			} else {
				int cartridgeTypeNumericId = cartridgeType.getNumericId();

				String context = "ROM-Correct";
				content = createContent(cartridgeType, FileHeaderType.NONE,
						sizeInBytes, true, context);
				header = null;
				writeFile(context, null, cartridgeType, ".rom", header, content);

				context = "CAR-Correct";
				content = createContent(cartridgeType, FileHeaderType.CART,
						sizeInBytes, true, context);
				header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeTypeNumericId, content);
				writeFile(context, null, cartridgeType, ".car", header, content);
			}
		}

		if (singleCartridgeType.equals(CartridgeType.UNKNOWN)) {
			// General sample for testing The!Cart Studio itself.
			writeNumberSamples("CAR-Correct-Small",
					CartridgeType.CARTRIDGE_STD_8, FileHeaderType.CART, "ROM",
					16385);
			writeNumberSamples("CAR-Correct-Large",
					CartridgeType.CARTRIDGE_ATMAX_1024, FileHeaderType.CART,
					"ROM", 129);

			// Special samples for creating specific workbooks.
			writeNumberSamples("ROM-Correct-Small-Atarimax-Short-Titles",
					CartridgeType.CARTRIDGE_STD_8, FileHeaderType.NONE,
					"ROM", 127);
			writeNumberSamples("ROM-Correct-Small-Atarimax-Long-Titles",
					CartridgeType.CARTRIDGE_STD_8, FileHeaderType.NONE,
					"Atarimax ROM", 127);
			writeNumberSamples("ROM-Correct-Small-MegaCart",
					CartridgeType.CARTRIDGE_STD_16, FileHeaderType.NONE,
					"MegaCart ROM", 127);
		}

		println(fileCount + " sample files created in the sub-folders of '"
				+ folder.getAbsolutePath() + "'.");
	}

	private void writeNumberSamples(String subFolderName,
			CartridgeType cartridgeType, FileHeaderType fileHeaderType,
			String prefix, int totalNumber) throws CoreException {
		if (subFolderName == null) {
			throw new IllegalArgumentException(
					"Parameter 'subFolderName' must not be null.");
		}
		if (cartridgeType == null) {
			throw new IllegalArgumentException(
					"Parameter 'cartridgeType' must not be null.");
		}
		if (fileHeaderType == null) {
			throw new IllegalArgumentException(
					"Parameter 'fileHeaderType' must not be null.");
		}
		println("Generating " + totalNumber
				+ " number sample files for cartridge type '"
				+ cartridgeType.getText() + " (" + cartridgeType.getNumericId()
				+ ")' with file header type '" + fileHeaderType.getText()
				+ "'.");

		String totalNumberString = Integer.toString(totalNumber);
		int digits = totalNumberString.length();
		for (int i = 0; i < totalNumber; i++) {
			StringBuilder numberStringBuilder = new StringBuilder(
					Integer.toString(i + 1));
			while (numberStringBuilder.length() < digits) {
				numberStringBuilder.insert(0, '0');
			}
			String numberString = numberStringBuilder.toString();
			String fileName = prefix + "-" + numberString;
			String title = fileName + "/" + totalNumberString;
			byte[] content = createContent(cartridgeType, fileHeaderType,
					cartridgeType.getSizeInKB() * ByteArrayUtility.KB, true,
					title);
			if (fileHeaderType.equals(FileHeaderType.CART)) {
				byte[] header = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeType.getNumericId(), content);
				writeFile(subFolderName, fileName, cartridgeType, ".car",
						header, content);
			} else {
				writeFile(subFolderName, fileName, cartridgeType, ".rom", null,
						content);

			}
		}
	}

	private byte[] createContent(CartridgeType cartridgeType,
			FileHeaderType fileHeaderType, int sizeInBytes,
			boolean withVectors, String title) {
		if (cartridgeType == null) {
			throw new IllegalArgumentException(
					"Parameter 'cartridgeType' must not be null.");
		}
		if (fileHeaderType == null) {
			throw new IllegalArgumentException(
					"Parameter 'fileHeaderType' must not be null.");
		}
		if (title == null) {
			throw new IllegalArgumentException(
					"Parameter 'title' must not be null.");
		}
		int startBankOffset = cartridgeType.getInitialBankOffset();
		int bankSize = cartridgeType.getBankSize();
		int startAddress = cartridgeType.getInitialBankAddress();

		byte[] content = new byte[sizeInBytes];
		byte[] binary = binaries.get(cartridgeType.getPlatform());
		if (binary == null) {
			throw new RuntimeException("No binary for platform '"
					+ cartridgeType + "'.");
		}
		int atariMaxFix = 6;
		startAddress = startAddress + atariMaxFix;
		int highByteOffset = startBankOffset + atariMaxFix + 1;
		int textOffset = startBankOffset + (binary[atariMaxFix + 3] & 0xff)
				+ 256 * (binary[atariMaxFix + 4] & 0xff);

		System.arraycopy(binary, 0, content, startBankOffset, binary.length);

		int numericId = cartridgeType.getNumericId();
		ContentType contentType = ContentType
				.getInstanceByCartridgeType(numericId);
		String theCartMode = contentType.getTheCartMode() == 0 ? "NONE"
				: TextUtility.formatAsDecimal(contentType.getTheCartMode())
						+ "/$"
						+ HexUtility.getByteValueHexString(contentType
								.getTheCartMode());
		LinePrinter linePrinter = new LinePrinter(content, textOffset, 40);
		linePrinter.printfln("{0}", title);
		linePrinter.printfln("{0} ({1})", cartridgeType.getText(),
				TextUtility.formatAsDecimal(numericId));
		linePrinter.printfln("{0} / {1}", fileHeaderType.getText(),
				TextUtility.formatAsMemorySize(sizeInBytes));
		linePrinter.printfln(
				"Banks:{0} Size:{1}",
				TextUtility.formatAsDecimal(cartridgeType.getSizeInKB()
						* ByteArrayUtility.KB / bankSize),
				TextUtility.formatAsMemorySize(bankSize));
		linePrinter.printfln("Initial Bank:{0} Address:${1}", TextUtility
				.formatAsDecimal(cartridgeType.getInitialBankNumber()),
				HexUtility.getLongValueHexString(cartridgeType
						.getInitialBankAddress()));
		linePrinter.printfln("The!Cart Mode: {0}", theCartMode);

		// Set high byte the actual start address at offset 1.
		content[highByteOffset] = (byte) (startAddress / 256);

		if (withVectors) {
			CartridgeUtility.setCartridgeVectors(content, startBankOffset,
					bankSize, 0, startAddress, 0x0000);
		}

		if (cartridgeType == CartridgeType.CARTRIDGE_ATRAX_SDX_64
				|| cartridgeType == CartridgeType.CARTRIDGE_ATRAX_SDX_128) {
			content = createInterleavedAtraxSDXContent(content);
		}
		if (cartridgeType == CartridgeType.CARTRIDGE_ATRAX_128) {
			content = createInterleavedAtrax128Content(content);
		}
		return content;
	}

	/**
	 * Encoded Atrax SDX ROMs use an interleaved address and data bus.
	 * 
	 * Addresses: A0 - A6, A1 - A7, A2 - A12, A3 - A15, A4 - A14, A5 - A13, A6 -
	 * A8, A7 - A5, A8 - A4, A9 - A3, A10 - A0, A11 - A1, A12 - A2, A13 - A9
	 * (bank select), A14 - A11 (bank select), A15 - A10 (bank select), A16 -
	 * A16 (bank select)
	 * 
	 * Data: D0 - Q4, D1 - Q0, D2 - Q5, D3 - Q1, D4 - Q7, D5 - Q6, D6 - Q3, D7 -
	 * Q2
	 * 
	 * @param content
	 *            The original content, not <code>null</code>.
	 * @return The interleaved content, not <code>null</code>.
	 */
	private static byte[] createInterleavedAtraxSDXContent(byte[] content) {
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		byte[] result = new byte[content.length];
		for (int a0 = 0; a0 < content.length; a0++) {
			byte b0 = content[a0];

			int a0_6 = (a0 >>> 0) & 0x1;
			int a0_7 = (a0 >>> 1) & 0x1;
			int a0_12 = (a0 >>> 2) & 0x1;
			int a0_15 = (a0 >>> 3) & 0x1;
			int a0_14 = (a0 >>> 4) & 0x1;
			int a0_13 = (a0 >>> 5) & 0x1;
			int a0_8 = (a0 >>> 6) & 0x1;
			int a0_5 = (a0 >>> 7) & 0x1;
			int a0_4 = (a0 >>> 8) & 0x1;
			int a0_3 = (a0 >>> 9) & 0x1;
			int a0_0 = (a0 >>> 10) & 0x1;
			int a0_1 = (a0 >>> 11) & 0x1;
			int a0_2 = (a0 >>> 12) & 0x1;
			int a0_9 = (a0 >>> 13) & 0x1;
			int a0_11 = (a0 >>> 14) & 0x1;
			int a0_10 = (a0 >>> 15) & 0x1;
			int a0_16 = (a0 >>> 16) & 0x1;

			int b0_4 = (b0 >>> 0) & 0x1;
			int b0_0 = (b0 >>> 1) & 0x1;
			int b0_5 = (b0 >>> 2) & 0x1;
			int b0_1 = (b0 >>> 3) & 0x1;
			int b0_7 = (b0 >>> 4) & 0x1;
			int b0_6 = (b0 >>> 5) & 0x1;
			int b0_3 = (b0 >>> 6) & 0x1;
			int b0_2 = (b0 >>> 7) & 0x1;

			int a1 = 0;
			a1 |= a0_0 * 0x00001;
			a1 |= a0_1 * 0x00002;
			a1 |= a0_2 * 0x00004;
			a1 |= a0_3 * 0x00008;
			a1 |= a0_4 * 0x00010;
			a1 |= a0_5 * 0x00020;
			a1 |= a0_6 * 0x00040;
			a1 |= a0_7 * 0x00080;
			a1 |= a0_8 * 0x00100;
			a1 |= a0_9 * 0x00200;
			a1 |= a0_10 * 0x00400;
			a1 |= a0_11 * 0x00800;
			a1 |= a0_12 * 0x01000;
			a1 |= a0_13 * 0x02000;
			a1 |= a0_14 * 0x04000;
			a1 |= a0_15 * 0x08000;
			a1 |= a0_16 * 0x10000;
			a1 = a1 & 0x1ffff;

			int b1 = 0;
			b1 |= b0_0 * 0x00001;
			b1 |= b0_1 * 0x00002;
			b1 |= b0_2 * 0x00004;
			b1 |= b0_3 * 0x00008;
			b1 |= b0_4 * 0x00010;
			b1 |= b0_5 * 0x00020;
			b1 |= b0_6 * 0x00040;
			b1 |= b0_7 * 0x00080;
			b1 = b1 & 0xff;

			result[a1] = (byte) b1;
		}
		return result;
	}

	/**
	 * Encoded Atrax 128 ROMs use an interleaved address and data bus.
	 * 
	 * Addresses: A0 - A5, A1 - A6, A2 - A7, A3 - A12, A4 - A0, A5 - A1, A6 -
	 * A2, A7 - A3, A8 - A4, A9 - A8, A10 - A10, A11 - A11, A12 - A9, A13 - A13
	 * (bank select), A14 - A14 (bank select), A15 - A15 (bank select), A16 -
	 * A16 (bank select)
	 * 
	 * Data: D0 - Q5, D1 - Q6, D2 - Q2, D3 - Q4, D4 - Q0, D5 - Q1, D6 - Q7, D7 -
	 * Q3
	 * 
	 * 
	 * @param content
	 *            The original content, not <code>null</code>.
	 * @return The interleaved content, not <code>null</code>.
	 */
	private static byte[] createInterleavedAtrax128Content(byte[] content) {
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		byte[] result = new byte[content.length];
		for (int a0 = 0; a0 < content.length; a0++) {
			byte b0 = content[a0];

			int a0_5 = (a0 >>> 0) & 0x1;
			int a0_6 = (a0 >>> 1) & 0x1;
			int a0_7 = (a0 >>> 2) & 0x1;
			int a0_12 = (a0 >>> 3) & 0x1;
			int a0_0 = (a0 >>> 4) & 0x1;
			int a0_1 = (a0 >>> 5) & 0x1;
			int a0_2 = (a0 >>> 6) & 0x1;
			int a0_3 = (a0 >>> 7) & 0x1;
			int a0_4 = (a0 >>> 8) & 0x1;
			int a0_8 = (a0 >>> 9) & 0x1;
			int a0_10 = (a0 >>> 10) & 0x1;
			int a0_11 = (a0 >>> 11) & 0x1;
			int a0_9 = (a0 >>> 12) & 0x1;
			int a0_13 = (a0 >>> 13) & 0x1;
			int a0_14 = (a0 >>> 14) & 0x1;
			int a0_15 = (a0 >>> 15) & 0x1;
			int a0_16 = (a0 >>> 16) & 0x1;

			int b0_5 = (b0 >>> 0) & 0x1;
			int b0_6 = (b0 >>> 1) & 0x1;
			int b0_2 = (b0 >>> 2) & 0x1;
			int b0_4 = (b0 >>> 3) & 0x1;
			int b0_0 = (b0 >>> 4) & 0x1;
			int b0_1 = (b0 >>> 5) & 0x1;
			int b0_7 = (b0 >>> 6) & 0x1;
			int b0_3 = (b0 >>> 7) & 0x1;

			int a1 = 0;
			a1 |= a0_0 * 0x00001;
			a1 |= a0_1 * 0x00002;
			a1 |= a0_2 * 0x00004;
			a1 |= a0_3 * 0x00008;
			a1 |= a0_4 * 0x00010;
			a1 |= a0_5 * 0x00020;
			a1 |= a0_6 * 0x00040;
			a1 |= a0_7 * 0x00080;
			a1 |= a0_8 * 0x00100;
			a1 |= a0_9 * 0x00200;
			a1 |= a0_10 * 0x00400;
			a1 |= a0_11 * 0x00800;
			a1 |= a0_12 * 0x01000;
			a1 |= a0_13 * 0x02000;
			a1 |= a0_14 * 0x04000;
			a1 |= a0_15 * 0x08000;
			a1 |= a0_16 * 0x10000;
			a1 = a1 & 0x1ffff;

			int b1 = 0;
			b1 |= b0_0 * 0x00001;
			b1 |= b0_1 * 0x00002;
			b1 |= b0_2 * 0x00004;
			b1 |= b0_3 * 0x00008;
			b1 |= b0_4 * 0x00010;
			b1 |= b0_5 * 0x00020;
			b1 |= b0_6 * 0x00040;
			b1 |= b0_7 * 0x00080;
			b1 = b1 & 0xff;

			result[a1] = (byte) b1;
		}
		return result;
	}
}
