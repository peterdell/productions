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

package com.wudsn.tools.thecartstudio.model.williams;

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabaseEntry;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.thecartstudio.model.ContentType;
import com.wudsn.tools.thecartstudio.model.ExportFormat.FileExtensions;
import com.wudsn.tools.thecartstudio.model.FlashTargetType;
import com.wudsn.tools.thecartstudio.model.Importer;
import com.wudsn.tools.thecartstudio.model.Workbook;

public final class WilliamsImporter extends Importer {

    private static final int CARTRIDGE_WILL_32_SIZE = CartridgeType.CARTRIDGE_WILL_32
	    .getSizeInKB() * KB;
    private static final int CARTRIDGE_WILL_32_CAR_SIZE = CartridgeFileUtility.CART_HEADER_SIZE
	    + CARTRIDGE_WILL_32_SIZE;
    private static final int CARTRIDGE_WILL_64_SIZE = CartridgeType.CARTRIDGE_WILL_64
	    .getSizeInKB() * KB;

    public WilliamsImporter() {

    }

    @Override
    public void importFile(Workbook workbook, File file, ImportResult result) {

	if (workbook == null) {
	    throw new IllegalArgumentException("workbook must not be null.");
	}
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter file must not be null.");
	}
	if (result == null) {
	    throw new IllegalArgumentException(
		    "Parameter result must not be null.");
	}

	// Check if conversion is required and target content type is supported.
	FlashTargetType flashTargetType = workbook.getRoot()
		.getFlashTargetType();

	if (flashTargetType
		.isContentTypeSupported(ContentType.CARTRIDGE_WILL_32)) {
	    return;
	}
	if (!flashTargetType
		.isContentTypeSupported(ContentType.CARTRIDGE_WILL_64)) {
	    return;
	}

	String fileName = file.getName();
	long fileLength = file.length();
	boolean relevant = (fileName.toLowerCase().endsWith(
		FileExtensions.CAR_IMAGE) && fileLength == CARTRIDGE_WILL_32_CAR_SIZE)
		|| fileLength == CARTRIDGE_WILL_32_SIZE;
	if (!relevant) {
	    return;
	}
	// The source file (".bin/.rom/.car") will be converted to the target
	// file
	// ("-Converted.car").
	File sourceFile = file;
	int index = fileName.lastIndexOf('.');
	if (index == -1) {
	    index = fileName.length();
	}
	File targetFile = new File(sourceFile.getParentFile(),
		fileName.substring(0, index) + "-Converted"
			+ FileExtensions.CAR_IMAGE);

	CartridgeDatabaseEntry cartridgeDatabaseEntry = null;
	try {
	    int numericId = 0;
	    int startOffset = -1;
	    byte[] content = FileUtility.readBytes(sourceFile,
		    CARTRIDGE_WILL_32_CAR_SIZE, true);

	    // Convert ROM file to CAR file.
	    if (content.length == CARTRIDGE_WILL_32_SIZE) {
		int crc32 = ByteArrayUtility.getCRC32(content);
		List<CartridgeDatabaseEntry> cartridgeDatabaseEntries = workbook
			.getCartridgeDatabase().getEntriesBySizeAndCRC32(
				content.length / KB, crc32);
		if (cartridgeDatabaseEntries.size() == 1) {

		    cartridgeDatabaseEntry = cartridgeDatabaseEntries.get(0);
		    numericId = cartridgeDatabaseEntry.getCartridgeType()
			    .getNumericId();
		    startOffset = 0;
		}

	    } // Convert CAR file to CAR file
	    else if (content.length == CARTRIDGE_WILL_32_CAR_SIZE) {
		numericId = CartridgeFileUtility
			.getCartridgeTypeNumericId(content);
		startOffset = CartridgeFileUtility.CART_HEADER_SIZE;
	    }

	    // Result must match a Williams 32 KB cartridge.
	    if (numericId != CartridgeType.CARTRIDGE_WILL_32.getNumericId()) {
		return;
	    }

	    byte[] cartridgeContent = new byte[CARTRIDGE_WILL_64_SIZE];
	    System.arraycopy(content, startOffset, cartridgeContent, 0,
		    CARTRIDGE_WILL_32_SIZE);
	    byte[] cartridgeHeader = CartridgeFileUtility
		    .createCartridgeHeaderWithCheckSum(
			    CartridgeType.CARTRIDGE_WILL_64.getNumericId(),
			    cartridgeContent);

	    OutputStream outputStream = null;
	    try {
		// Open output file, write header, write content
		// and use a new file.
		outputStream = FileUtility.openOutputStream(targetFile);
		FileUtility.writeBytes(targetFile, outputStream,
			cartridgeHeader, 0, cartridgeHeader.length);
		FileUtility.writeBytes(targetFile, outputStream,
			cartridgeContent, 0, cartridgeContent.length);
		result.cartridgeDatabaseEntry = cartridgeDatabaseEntry;
		result.convertedFile = targetFile;
	    } finally {
		try {
		    if (outputStream != null) {
			FileUtility.closeOutputStream(targetFile, outputStream);
		    }
		} catch (CoreException ignore) {

		}
	    }

	} catch (CoreException ex) {
	    result.convertedFileException = ex;
	}
    }
}