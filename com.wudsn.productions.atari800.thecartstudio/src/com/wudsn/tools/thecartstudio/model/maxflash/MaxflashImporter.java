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

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;
import static com.wudsn.tools.base.common.ByteArrayUtility.MB;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.atari.AtrFile;
import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.Log;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.AtrProgrammingFile;
import com.wudsn.tools.thecartstudio.model.AtrProgrammingFile.BuildInfo;
import com.wudsn.tools.thecartstudio.model.ContentType;
import com.wudsn.tools.thecartstudio.model.ExportFormat.FileExtensions;
import com.wudsn.tools.thecartstudio.model.FlashTargetType;
import com.wudsn.tools.thecartstudio.model.Importer;
import com.wudsn.tools.thecartstudio.model.Workbook;

public final class MaxflashImporter extends Importer {

    // Atarimax Maxflash Programming ATR parameters.
    // Minimum size is ATR header plus the standard boot code (0x13 sectors).
    private final static int BANK_SIZE = 0x2000;
    private final static int ATR_MAX_MIN_FILE_SIZE = AtrFile.HEADER_SIZE + 0x13
	    * AtrFile.SECTOR_SIZE_SD + BANK_SIZE;
    private final static int ATR_MAX_MAX_FILE_SIZE = 2 * MB;

    private static final String BUILD_PREFIX = "ATARIMAX Flash Utility Build ";
    private static final byte[] BUILD_PREFIX_BYTES = ASCIIString
	    .getBytes(BUILD_PREFIX);

    // Mode for a module which is of the correct cartridge type but has an
    // unknown signature.
    public static final int MODE_UNKNOWN_SIGNATURE = 1;
    // Mode which copies the complete data from the mode offset until the
    // end of the programming file into the content.
    private static final int MODE_FULL_IMAGE = 2;
    // Mode which copies the data defined by the usage map at the mode
    // offset into the content.
    private static final int MODE_USAGE_MAP = 3;

    private List<BuildInfo> buildInfos;

    public MaxflashImporter() {

	// Create list of supported builds and their properties.
	buildInfos = new ArrayList<BuildInfo>();
	addBuildInfo(BUILD_PREFIX + "11032003", 0x51b, MODE_FULL_IMAGE, 0x810);
	addBuildInfo(BUILD_PREFIX + "11032003", 0x520, MODE_FULL_IMAGE, 0x810);
	addBuildInfo(BUILD_PREFIX + "05292003", 0x50e, MODE_FULL_IMAGE, 0x810);
	addBuildInfo(BUILD_PREFIX + "08252004", 0x610, MODE_USAGE_MAP, 0x58e);
	addBuildInfo(BUILD_PREFIX + "04092005", 0x610, MODE_USAGE_MAP, 0x58e);
	addBuildInfo(BUILD_PREFIX + "04092005", 0x613, MODE_USAGE_MAP, 0x591);
	addBuildInfo(BUILD_PREFIX + "08152009", 0x61b, MODE_USAGE_MAP, 0x599);
	addBuildInfo(BUILD_PREFIX + "10032011", 0x623, MODE_USAGE_MAP, 0x5a1);
	addBuildInfo(BUILD_PREFIX + "04072012", 0x62b, MODE_USAGE_MAP, 0x5a9);
	buildInfos = Collections.unmodifiableList(buildInfos);
    }

    /**
     * Adds a new build info.
     * 
     * @param signature
     *            The signature string, not <code>null</code>.
     * @param signatureOffset
     *            The signature offset in the ATR file.
     * @param mode
     *            The conversion mode.
     * @param modeOffset
     *            The conversion mode offset in the ATR file.
     */
    private void addBuildInfo(String signature, int signatureOffset, int mode,
	    int modeOffset) {
	if (signature == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'signature' must not be null.");
	}

	if (signatureOffset < 0) {
	    throw new IllegalArgumentException(
		    "Parameter 'signatureOffset' must not be negative. Specified value was "
			    + signatureOffset + ".");
	}
	buildInfos.add(new BuildInfo(signature, signatureOffset, mode,
		modeOffset));
    }

    private void determineCartridgeTypeAndBuild(
	    AtrProgrammingFile atrProgrammingFile) {
	if (atrProgrammingFile == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'programmingImage' must not be null.");
	}

	// Round up to the next suitable AtariMax cartridge
	// size and determine cartridge type and use map offset.
	// The usage map offset might be version specific.
	CartridgeType cartridgeType = null;
	BuildInfo buildInfo = null;

	if (atrProgrammingFile.getSourceFileContentSize() <= 128 * KB) {
	    cartridgeType = CartridgeType.CARTRIDGE_ATMAX_128;

	} else if (atrProgrammingFile.getSourceFileContentSize() <= 1 * MB) {
	    cartridgeType = CartridgeType.CARTRIDGE_ATMAX_1024;
	} else {
	    return;
	}

	for (int i = 0; i < buildInfos.size() && buildInfo == null; i++) {
	    BuildInfo currentBuildInfo = buildInfos.get(i);
	    if (AtrProgrammingFile.containsStringAt(
		    atrProgrammingFile.getBootCode(),
		    currentBuildInfo.signature,
		    currentBuildInfo.signatureOffset - AtrFile.HEADER_SIZE)) {
		buildInfo = currentBuildInfo;
	    }
	}

	if (buildInfo == null) {
	    // Try to find the standard signature prefix.
	    int signatureOffset = ByteArrayUtility.getIndexOf(
		    atrProgrammingFile.getBootCode(), 0, BANK_SIZE - 1,
		    BUILD_PREFIX_BYTES);
	    // Try to find the unknown build number.
	    if (signatureOffset >= 0) {
		String signature = new String(atrProgrammingFile.getBootCode(),
			signatureOffset + BUILD_PREFIX_BYTES.length, 8);
		buildInfo = new BuildInfo(signature, signatureOffset,
			MODE_UNKNOWN_SIGNATURE, 0);
	    }
	}
	atrProgrammingFile.setCartridgeTypeAndBuildInfo(cartridgeType,
		buildInfo);

    }

    @SuppressWarnings("static-method")
    private byte[] createCartridgeContent(AtrProgrammingFile atrProgrammingFile)
	    throws CoreException {
	if (atrProgrammingFile == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'programmingImage' must not be null.");
	}
	byte[] cartridgeContent = atrProgrammingFile.createCartridgeContent();
	BuildInfo buildInfo = atrProgrammingFile.getBuildInfo();
	Log.logInfo("Processing {0}: {1}",
		new Object[] { atrProgrammingFile.getSourceFilePath(),
			buildInfo.toString() });

	switch (buildInfo.mode) {
	case MODE_FULL_IMAGE:
	    int sourceFileOffset = atrProgrammingFile
		    .getSourceFileContentOffset() - buildInfo.modeOffset;
	    System.arraycopy(atrProgrammingFile.getSourceFileContent(),
		    sourceFileOffset, cartridgeContent, 0,
		    atrProgrammingFile.getSourceFileContentSize());
	    break;

	case MODE_USAGE_MAP:
	    // There is a usage map with 128 bytes that represent either
	    // used ($00) or unused ($ff) banks.
	    int bootCodeUsageMapOffset = buildInfo.modeOffset
		    - AtrFile.HEADER_SIZE;
	    int sourceFileContentOffset = 0;
	    int cartridgeContentOffset = 0;

	    int banksCount = cartridgeContent.length / BANK_SIZE;
	    for (int i = 0; i < banksCount; i++) {
		byte usage = (atrProgrammingFile.getBootCode()[bootCodeUsageMapOffset
			+ i]);
		// Check if bank is used.
		if (usage == 0) {
		    // Yes, copy used bank.
		    System.arraycopy(atrProgrammingFile.getSourceFileContent(),
			    sourceFileContentOffset, cartridgeContent,
			    cartridgeContentOffset, BANK_SIZE);
		    sourceFileContentOffset += BANK_SIZE;
		} else if (usage == (byte) 0xff) {
		    // No, fill unused banks with 0xff.
		    Arrays.fill(cartridgeContent, cartridgeContentOffset,
			    cartridgeContentOffset + BANK_SIZE, (byte) 0xff);
		} else {
		    // ERROR: File '{0}' cannot be converted because it
		    // contains the invalid usage tab entry {1} at offset
		    // {2}.
		    throw new CoreException(Messages.E503,
			    TextUtility.formatAsHexaDecimal(usage, 256),
			    TextUtility.formatAsHexaDecimal(
				    bootCodeUsageMapOffset,
				    bootCodeUsageMapOffset),
			    atrProgrammingFile.getSourceFilePath());
		}
		cartridgeContentOffset += BANK_SIZE;
	    }
	    break;

	default:
	    throw new IllegalStateException("Content creation for mode "
		    + buildInfo.mode + " is not supported.");
	}

	return cartridgeContent;
    }

    @Override
    public void importFile(Workbook workbook, File file, ImportResult result) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter workbook must not be null.");
	}
	if (file == null) {
	    throw new IllegalArgumentException("file must not be null.");
	}
	if (result == null) {
	    throw new IllegalArgumentException(
		    "Parameter result must not be null.");
	}
	// Check if target content type is supported.
	FlashTargetType flashTargetType = workbook.getRoot()
		.getFlashTargetType();
	if (!flashTargetType
		.isContentTypeSupported(ContentType.CARTRIDGE_ATMAX_128)
		&& !flashTargetType
			.isContentTypeSupported(ContentType.CARTRIDGE_ATMAX_1024)) {
	    return;
	}

	String fileName = file.getName();
	long fileLength = file.length();
	boolean relevant = (fileName.toLowerCase().endsWith(
		FileExtensions.ATR_IMAGE) && (fileLength > MaxflashImporter.ATR_MAX_MIN_FILE_SIZE && fileLength < MaxflashImporter.ATR_MAX_MAX_FILE_SIZE));
	if (!relevant) {
	    return;
	}

	// The source file (".atr") will be converted to the target file
	// (".car").
	File sourceFile = file;
	File targetFile = new File(sourceFile.getParentFile(),
		fileName.substring(0, fileName.length()
			- FileExtensions.ATR_IMAGE.length())
			+ FileExtensions.CAR_IMAGE);

	try {
	    AtrProgrammingFile atrProgrammingFile = new AtrProgrammingFile(file);
	    try {
		if (!atrProgrammingFile.loadHeadersAndBootCode()) {
		    // INFO: File '{0}' skipped, because it is not a valid
		    // programming ATR file.
		    throw new CoreException(Messages.I500,
			    atrProgrammingFile.getSourceFilePath());
		}

		determineCartridgeTypeAndBuild(atrProgrammingFile);

		// AtariMax cartridge type detected? If not, return
		// silently.
		if (atrProgrammingFile.getCartridgeType() == null) {
		    return;
		}

		BuildInfo buildInfo = atrProgrammingFile.getBuildInfo();

		if (buildInfo == null) {
		    // INFO: File '{0}' skipped, because no programming ATR
		    // build signature was found in the file.
		    throw new CoreException(Messages.I501,
			    atrProgrammingFile.getSourceFilePath());
		}

		if (buildInfo.mode == MaxflashImporter.MODE_UNKNOWN_SIGNATURE) {
		    // ERROR: File '{0}' cannot be converted because it
		    // contains an unknown programming ATR build signature
		    // {0}.
		    throw new CoreException(Messages.E502, buildInfo.signature,
			    atrProgrammingFile.getSourceFilePath());
		}

		byte[] cartridgeContent = createCartridgeContent(atrProgrammingFile);
		byte[] cartridgeHeader = CartridgeFileUtility
			.createCartridgeHeaderWithCheckSum(atrProgrammingFile
				.getCartridgeType().getNumericId(),
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
		    result.convertedFile = targetFile;
		} finally {
		    try {
			if (outputStream != null) {
			    FileUtility.closeOutputStream(targetFile,
				    outputStream);
			}
		    } catch (CoreException ignore) {

		    }
		}

	    } finally {
		atrProgrammingFile.close();
	    }

	} catch (CoreException ex) {
	    result.convertedFileException = ex;
	}
    }

}