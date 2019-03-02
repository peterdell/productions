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

import static com.wudsn.tools.thecartstudio.model.CartDef.BANKSIZE;
import static com.wudsn.tools.thecartstudio.model.CartDef.BANKS_PER_BLOCK;
import static com.wudsn.tools.thecartstudio.model.CartDef.BLOCKSIZE;
import static com.wudsn.tools.thecartstudio.model.CartDef.BLOCKS_PER_DD_ATR;
import static com.wudsn.tools.thecartstudio.model.CartDef.BLOCKUSE_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HASH_BLOCK_COUNT;
import static com.wudsn.tools.thecartstudio.model.CartDef.HASH_BLOCK_START;
import static com.wudsn.tools.thecartstudio.model.CartDef.HASH_LEN;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_BITMAP_LEN;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_BITMAP_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_BLOCKS_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_CS_COUNT_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_CS_START_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_HASH_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_NAME_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_PARTNO_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_SIGNATURE_DATA;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_SIGNATURE_LEN;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_SIGNATURE_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_TIMESTAMP_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.HDR_TOTAL_PARTS_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.IMGNAME_OFS;
import static com.wudsn.tools.thecartstudio.model.CartDef.IMG_NAME_LEN;
import static com.wudsn.tools.thecartstudio.model.CartDef.IMG_TIMESTAMP_LEN;
import static com.wudsn.tools.thecartstudio.model.CartDef.SIGNATURE_DATA;
import static com.wudsn.tools.thecartstudio.model.CartDef.SIGNATURE_LEN;
import static com.wudsn.tools.thecartstudio.model.CartDef.SIGNATURE_OFS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.atari.AtrFile;
import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.DateUtility;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.ExportFormat.FileExtensions;

/**
 * Logic for supported export formats.
 * 
 * @author Peter Dell
 * 
 */
public final class Exporter {

	private WorkbookExport workbookExport;
	private byte[] data;
	private MessageQueue messageQueue;
	private List<File> exportedFiles;
	private long exportedFileSize;

	private static final class RomTool {
		public static final int IMAGE_HEADER_SIZE = BANKSIZE;

		private byte[] data;
		private MessageDigest sha512;

		private int maxBlock;

		private static byte EOL = (byte) 155;
		private byte[] imageName = new byte[IMG_NAME_LEN];
		private byte[] imageTimeStamp = new byte[IMG_TIMESTAMP_LEN];
		private byte[] imageHeader = new byte[IMAGE_HEADER_SIZE];

		public RomTool(WorkbookExport workbookExport) {
			if (workbookExport == null) {
				throw new IllegalArgumentException(
						"Parameter 'workbookExport' must not be null.");
			}

			this.data = workbookExport.getData();
			try {
				sha512 = MessageDigest.getInstance("SHA-512");
			} catch (NoSuchAlgorithmException ex) {
				throw new RuntimeException(ex);
			}
			// Prepare programming images.
			imageName = new byte[IMG_NAME_LEN];
			imageTimeStamp = new byte[IMG_TIMESTAMP_LEN];
			imageHeader = new byte[IMAGE_HEADER_SIZE];

			// Get the global workbook information.
			WorkbookRoot root = workbookExport.getWorkbook().getRoot();

			// Compute highest used bank and block.
			int maxBank = 0;
			for (WorkbookBank bank : root.getBanksList()) {
				ReservedContentProvider reservedContentProvider = bank
						.getReservedContentProvider();
				boolean relevant = (reservedContentProvider != null && reservedContentProvider
						.isReservedForSystem()) || !bank.getEntries().isEmpty();
				if (relevant) {
					maxBank = bank.getNumber() + 1;
				}

			}
			maxBlock = (maxBank + BANKS_PER_BLOCK - 1) / BANKS_PER_BLOCK;
			if (maxBlock < HASH_BLOCK_START + HASH_BLOCK_COUNT) {
				maxBlock = HASH_BLOCK_START + HASH_BLOCK_COUNT;
			}

			setImageName(root.getTitle());
			setImageTimeStamp();
			calculateHashes();
			addTheCartSignature();
			setupBaseHeader();
		}

		public int getMaxBlock() {
			return maxBlock;
		}

		private void setImageName(String name) {
			if (name == null) {
				throw new IllegalArgumentException(
						"Parameter 'name' must not be null.");
			}
			int index = 0;
			int dst = 0;
			while (index < name.length() && dst < imageName.length - 1) {
				char c = name.charAt(index++);
				if (c < 32 || c == 96 || c == 123 || c >= 125) {
					continue;
				}
				imageName[dst++] = (byte) c;
			}
			imageName[dst++] = EOL;
			while (dst < imageName.length) {
				imageName[dst++] = (byte) 0xff;
			}
		}

		private void setImageTimeStamp() {
			byte[] dateTime = ASCIIString.getBytes(DateUtility
					.getCurrentDateTimeString());
			System.arraycopy(dateTime, 0, imageTimeStamp, 0, dateTime.length);
			imageTimeStamp[imageTimeStamp.length - 1] = EOL;
		}

		private void calculateHashes() {
			for (int i = 0; i < maxBlock; i++) {
				// No hash value for hash blocks.
				if (i >= HASH_BLOCK_START
						&& i < HASH_BLOCK_START + HASH_BLOCK_COUNT) {
					continue;
				}
				int hashOffset = HASH_BLOCK_START * BLOCKSIZE + i * HASH_LEN;
				computeSHA512(data, i * BLOCKSIZE, BLOCKSIZE, data, hashOffset);
			}
		}

		private void computeSHA512(byte[] source, int sourceOffset, int size,
				byte[] dest, int destOffset) {
			sha512.reset();
			sha512.update(source, sourceOffset, size);
			byte[] hash = sha512.digest();
			System.arraycopy(hash, 0, dest, destOffset, hash.length);

		}

		private void addTheCartSignature() {
			System.arraycopy(SIGNATURE_DATA, 0, data, SIGNATURE_OFS,
					SIGNATURE_LEN);
			data[BLOCKUSE_OFS] = (byte) (maxBlock & 0xff);
			data[BLOCKUSE_OFS + 1] = (byte) (maxBlock >> 8);
			System.arraycopy(imageName, 0, data, IMGNAME_OFS, IMG_NAME_LEN);
		}

		private void calculateUsageMap() {
			for (int i = 0; i < HDR_BITMAP_LEN; i++) {
				imageHeader[HDR_BITMAP_OFS + i] = 0;
			}
			for (int block = 0; block < maxBlock * 2; block++) {
				int blockOffset = block * BANKSIZE * 8;
				int usedBits = 0;
				for (int i = 0; i < 8; i++) {
					int bankOffset = blockOffset + i * BANKSIZE;
					boolean bankUnused = true;
					for (int j = 0; bankUnused && j < BANKSIZE; j++) {
						bankUnused = (data[bankOffset + j] == WorkbookBank.UNUSED_BYTE);
					}
					if (!bankUnused) {
						usedBits |= (1 << i);
					}
				}
				imageHeader[HDR_BITMAP_OFS + block] = (byte) usedBits;
			}
		}

		private void setupBaseHeader() {
			computeSHA512(data, HASH_BLOCK_START * BLOCKSIZE, HASH_BLOCK_COUNT
					* BLOCKSIZE, imageHeader, HDR_HASH_OFS);
			System.arraycopy(HDR_SIGNATURE_DATA, 0, imageHeader,
					HDR_SIGNATURE_OFS, HDR_SIGNATURE_LEN);
			imageHeader[HDR_BLOCKS_OFS] = (byte) (maxBlock & 0xff);
			imageHeader[HDR_BLOCKS_OFS + 1] = (byte) (maxBlock >> 8);
			imageHeader[HDR_TOTAL_PARTS_OFS] = 1;
			imageHeader[HDR_PARTNO_OFS] = 1;
			imageHeader[HDR_CS_START_OFS] = HASH_BLOCK_START & 0xff;
			imageHeader[HDR_CS_START_OFS + 1] = HASH_BLOCK_START >> 8;
			imageHeader[HDR_CS_COUNT_OFS] = HASH_BLOCK_COUNT;
			System.arraycopy(imageName, 0, imageHeader, HDR_NAME_OFS,
					IMG_NAME_LEN);
			System.arraycopy(imageTimeStamp, 0, imageHeader, HDR_TIMESTAMP_OFS,
					IMG_TIMESTAMP_LEN);

			calculateUsageMap();
		}

		public byte[] createImageHeader() {
			return imageHeader;
		}

	}

	public Exporter(WorkbookExport workbookExport, MessageQueue messageQueue) {
		if (workbookExport == null) {
			throw new IllegalArgumentException(
					"Parameter 'workbookExport' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
		this.workbookExport = workbookExport;
		this.data = workbookExport.getData();
		this.messageQueue = messageQueue;

		exportedFiles = new ArrayList<File>();
		exportedFileSize = 0;

	}

	public void exportAsBinImage(File file) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}

		try {
			FileUtility.writeBytes(file, data);
			addExportedFile(file);
			exportIndexFile(file);
		} catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(file, null));
			return;
		}
		// INFO: Workbook with {0} exported as BIN cartridge image {1}.
		messageQueue.sendMessage(file, null, Messages.I110,
				TextUtility.formatAsMemorySize(exportedFileSize),
				file.getAbsolutePath());
	}

	private void addExportedFile(File file) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}
		exportedFiles.add(file);
		exportedFileSize += file.length();
	}

	public void exportAsCarImage(File file) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}
		CartridgeType cartridgeType = workbookExport.getWorkbook().getRoot()
				.getCartridgeType();
		if (cartridgeType == CartridgeType.UNKNOWN) {
			throw new RuntimeException(
					"Cannot export to unknown cartrdige type.");
		}

		try {
			OutputStream outputStream = FileUtility.openOutputStream(file);
			try {
				byte[] cartridgeHeader = CartridgeFileUtility
						.createCartridgeHeaderWithCheckSum(
								cartridgeType.getNumericId(), data);
				FileUtility.writeBytes(file, outputStream, cartridgeHeader, 0,
						cartridgeHeader.length);
				FileUtility
						.writeBytes(file, outputStream, data, 0, data.length);
			} finally {
				FileUtility.closeOutputStream(file, outputStream);
			}
			addExportedFile(file);
			exportIndexFile(file);
		} catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(file, null));
			return;
		}
		// INFO: Workbook with {0} exported as CAR cartridge image {1}.
		messageQueue.sendMessage(file, null, Messages.I111,
				TextUtility.formatAsMemorySize(exportedFileSize),
				file.getAbsolutePath());
	}

	public void exportAsAtrImage(File file) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}

		RomTool romTool = new RomTool(workbookExport);
		int romSize = romTool.getMaxBlock() * BLOCKSIZE;
		int dataSize = RomTool.IMAGE_HEADER_SIZE + romSize;
		try {
			OutputStream outputStream = FileUtility.openOutputStream(file);
			try {
				byte[] atrHeader = AtrFile.createHeader(dataSize,
						AtrFile.SECTOR_SIZE_8K);
				FileUtility.writeBytes(file, outputStream, atrHeader, 0,
						atrHeader.length);

				byte[] imageHeader = romTool.createImageHeader();
				FileUtility.writeBytes(file, outputStream, imageHeader, 0,
						imageHeader.length);

				FileUtility.writeBytes(file, outputStream, data, 0, romSize);
			} finally {
				FileUtility.closeOutputStream(file, outputStream);
			}
			addExportedFile(file);
			exportIndexFile(file);

		} catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(file, null));
			return;
		}
		// INFO: Workbook with {0} exported as ATR programming image {1}.
		messageQueue.sendMessage(file, null, Messages.I112,
				TextUtility.formatAsMemorySize(exportedFileSize),
				file.getAbsolutePath());
	}

	public void exportAsAtrImages(File file) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}
		String fileName = file.getName();
		String extension = FileExtensions.ATR_IMAGES;
		if (!fileName.endsWith(extension)) {
			throw new IllegalArgumentException(
					"Parameter 'file' be a file that ends with '" + extension
							+ "'. Specified value was '" + fileName + "'.");
		}
		int extensionIndex = fileName.length() - extension.length();

		String baseFileName = fileName.substring(0, extensionIndex);

		// Find and strip last "_<nnn>" where "<nnn>" is a sequence of digits.
		int numberIndex = fileName.lastIndexOf("_");
		if (numberIndex >= 0) {
			int i = numberIndex + 1;
			char c = fileName.charAt(i++);
			boolean digits = (c >= '0' && c <= '9');
			while (digits && i < extensionIndex) {
				c = fileName.charAt(i++);
				digits = (c >= '0' && c <= '9');
			}
			if (digits) {
				baseFileName = fileName.substring(0, numberIndex);
			}
		}

		RomTool romTool = new RomTool(workbookExport);
		int maxBlock = romTool.getMaxBlock();
		byte[] imageHeader = romTool.createImageHeader();

		int numberOfAtrs = (maxBlock + BLOCKS_PER_DD_ATR - 1)
				/ BLOCKS_PER_DD_ATR;

		imageHeader[HDR_TOTAL_PARTS_OFS] = (byte) numberOfAtrs;
		for (int currentAtr = 1; currentAtr <= numberOfAtrs; currentAtr++) {
			imageHeader[HDR_PARTNO_OFS] = (byte) currentAtr;
			File partFile = new File(file.getParentFile(), baseFileName + "_"
					+ currentAtr + extension).getAbsoluteFile();

			int startBlock = (currentAtr - 1) * BLOCKS_PER_DD_ATR;
			int numberOfBlocks = maxBlock - startBlock;
			if (numberOfBlocks > BLOCKS_PER_DD_ATR) {
				numberOfBlocks = BLOCKS_PER_DD_ATR;
			}
			exportAsAtrImagesPart(partFile, imageHeader, startBlock,
					numberOfBlocks);
			if (messageQueue.containsError()) {
				return;
			}
			addExportedFile(partFile);
		}
		exportIndexFile(file);

		String fileNames = exportedFiles.get(0).getAbsolutePath();
		int count = exportedFiles.size();
		if (count > 1) {
			fileNames += " ... " + exportedFiles.get(count - 1).getName();
		}
		// INFO: Workbook with {0} exported as ATR programming image
		// {1}.
		messageQueue.sendMessage(file, null, Messages.I113,
				TextUtility.formatAsMemorySize(exportedFileSize), fileNames);
	}

	private void exportAsAtrImagesPart(File file, byte[] imageHeader,
			int startBlock, int numberOfBlocks) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}
		if (imageHeader == null) {
			throw new IllegalArgumentException(
					"Parameter 'imageHeader' must not be null.");
		}

		// 3 unused SD sectors + 8k image header + data for part
		int romSize = numberOfBlocks * BLOCKSIZE;
		int dataSize = AtrFile.BOOT_SECTORS_SIZE_SD + RomTool.IMAGE_HEADER_SIZE
				+ romSize;
		try {
			OutputStream outputStream = FileUtility.openOutputStream(file);
			try {
				byte[] atrHeader = AtrFile.createHeader(dataSize,
						AtrFile.SECTOR_SIZE_DD);
				FileUtility.writeBytes(file, outputStream, atrHeader, 0,
						atrHeader.length);

				byte[] bootSectors = AtrFile.createBootSectors();
				FileUtility.writeBytes(file, outputStream, bootSectors, 0,
						bootSectors.length);

				FileUtility.writeBytes(file, outputStream, imageHeader, 0,
						imageHeader.length);

				FileUtility.writeBytes(file, outputStream, data, startBlock
						* BLOCKSIZE, romSize);
			} finally {
				FileUtility.closeOutputStream(file, outputStream);
			}
		} catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(file, null));
		}
	}

	/**
	 * Exports the text index file for the export file.
	 * 
	 * @param file
	 *            The export file used as base file name.
	 */
	private void exportIndexFile(File file) {
		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(bos);
		workbookExport.print(writer);
		writer.close();
		file = FileUtility.normalizeFileExtension(file, ".txt");
		try {
			FileUtility.writeBytes(file, bos.toByteArray());
		} catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(null, null));
		}
	}

	/**
	 * Gets the unmodifiable list of file that were exported.
	 * 
	 * @return The unmodifiable list of file that were exported, may be empty,
	 *         not <code>null</code>.
	 */
	public List<File> getExportedFiles() {
		return Collections.unmodifiableList(exportedFiles);
	}
}
