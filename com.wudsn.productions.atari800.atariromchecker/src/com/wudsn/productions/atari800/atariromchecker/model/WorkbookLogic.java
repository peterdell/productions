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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wudsn.productions.atari800.atariromchecker.Messages;
import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabase;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabaseEntry;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.HexUtility;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;

/**
 * <pre>
 * 
 * FROM: http://atariage.com/forums/topic/201133-os-source-code-all-revisions/#entry2667627
 * Recreated source codes of all known official revisions of the Atari OS by Kr0tki (Tomasz Krasuski).
 * 
 * FROM: http://mixinc.net/atari/books/XL-OS_Full_Searchable.pdf
 * 
 * REV�LEVEL DETERMINATION
 * 
 * To allow program products to determine which Atari Home Computer and Operating System Revision level it is operating with,
 * the follOwing tests are recommended,
 * If location $FCD8 = $A2, then product is an A400/ A800 wherein:
 *   If location $FFF8 = $DD and $FFF9 = $57 then OS is NTSC rev A.
 *   If location $FFF8 = $D6 and $FFF9 = $57 then OS is PAL  rev A.
 *   If location $FFF8 = $F3 and $FFF9 = $E6 then OS is NTSC rev B.
 *   If location $FFF8 = $22 and $FFF9 = $58 then OS is PAL  rev B.
 *   Otherwise, it is some future A400/ A800 OS.
 * If location $FCD8 not $A2, then product is a 1200XL or other future home computer product wherein:
 *   If location SFFFl = $Ol. then OS is 1200XL and location $FFF7 will be the internal rev number for the 1200XL OS.
 *   Otherwise, location $FFFl = product code for future Atari Home Computer product and location SFFF7 contains OS rev level for this product.
 * 
 * The check sums are screwed  up with the NTSC rev. B ROM as Atari didn't write the checksums into the ROM before manufacturing.
 * The production ROMs are therefore left with zeroes at $E40F/$E41F, and $FFF8-$FFF9 contains copy of the PIRQ vector, normally stored at $FFFE-$FFFF.
 * The latter is apparently a leftover from debugging - search for "SET UP RAM VECTORS FOR LINBUG VERSION" in the rev. B sources for some interesting piece of code.
 * 
 * 
 * FROM: http://www.ataripreservation.org/websites/freddy.offenga/osroms.txt
 * FROM: http://www.ataripreservation.org/websites/freddy.offenga/osromv36.zip
 * 
 * Rev. TV    Date        CRC-32      Part Nr(s)
 * ~~~~ ~~~~~ ~~~~~~~~~~~ ~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * A    NTSC  1979        0xc1b3bb02  CO12499A, CO14599A, CO12399B
 * A    PAL   1979        0x72b3fed4  CO15199,  CO15299,  CO12399B
 * B    NTSC  1981        0x0e86d61d  CO12499B, CO14599B, CO12399B
 * B    PAL   (*)         (*)         (*)
 * </pre>
 **/

public final class WorkbookLogic {

    private ROMVersionDatabase romVersionDatabase;
    private CartridgeDatabase cartridgeDatabase;

    public WorkbookLogic(ROMVersionDatabase romVersionDatabase,
	    CartridgeDatabase cartridgeDatabase) {
	if (romVersionDatabase == null) {
	    throw new IllegalArgumentException(
		    "Parameter romVersionDatabase must not be null.");
	}
	if (cartridgeDatabase == null) {
	    throw new IllegalArgumentException(
		    "Parameter cartridgeDatabase must not be null.");
	}
	this.romVersionDatabase = romVersionDatabase;
	this.cartridgeDatabase = cartridgeDatabase;
    }

    /**
     * Analyze a list of files or folders.
     * 
     * @param workbook
     *            The workbook to contain the result of the analysis.
     * @param filesList
     *            The list of file or folders to be analyzed. May be empty, not
     *            <code>null</code>.
     */
    public void checkFiles(Workbook workbook, List<File> filesList) {
	if (filesList == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'filesList' must not be null.");
	}

	List<File> resolvedFilesList = new ArrayList<File>();
	for (File file : filesList) {
	    resolveFiles(resolvedFilesList, file);
	}

	Collections.sort(resolvedFilesList);
	workbook.clear();
	workbook.setResolvedFilesCount(resolvedFilesList.size());

	for (File file : resolvedFilesList) {

	    checkFile(workbook, file);
	}
    }

    /**
     * Resolve a file or folder into the list of files. Only files with
     * extension ".rom", ".bin" or ".car" are returned.
     * 
     * @param file
     *            The current file or folder to be resolved, not
     *            <code>null</code>.
     * @param filesList
     *            The modifiable list of files that have been resolved so far,
     *            not <code>null</code>.
     */
    private void resolveFiles(List<File> filesList, File file) {

	if (filesList == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'filesList' must not be null.");
	}
	if (filesList.contains(file)) {
	    return;
	}
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	if (file.isDirectory()) {
	    File[] innerFiles = file.listFiles();
	    if (innerFiles != null) {
		for (File innerFile : innerFiles) {
		    resolveFiles(filesList, innerFile);
		}
	    }
	} else {
	    String fileName = file.getName().toLowerCase();
	    if (fileName.endsWith(".bin") || fileName.endsWith(".rom")
		    || fileName.endsWith(".car")) {
		filesList.add(file.getAbsoluteFile());
	    }
	}
    }

    /**
     * Read current and compute actual checksums of an Atari 400/800 OS ROM
     * image
     * 
     * @param workbook
     *            The workbook, not <code>null</code>.
     * @param file
     *            The file, not <code>null</code>.
     */
    private void checkFile(Workbook workbook, File file) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	WorkbookEntry entry = new WorkbookEntry(file);
	workbook.addEntry(entry);
	int headerSize = CartridgeFileUtility.CART_HEADER_SIZE;
	byte[] content;
	try {
	    content = FileUtility.readBytes(file, ROM.MAX_SIZE + headerSize,
		    true);
	} catch (CoreException ex) {
	    entry.setMessage(ex.getMessage());
	    return;
	}

	// Guess type
	int type;
	switch (content.length) {
	case 0x2800:
	    type = ROM.OS_AB;
	    break;
	case 0x4000:
	    type = ROM.OS_XL;
	    break;
	default:
	    type = ROM.UNKNOWN;
	}

	int cartridgeTypeNumericID = 0;
	// Check if there is a ".car" file inside
	if (content.length % ByteArrayUtility.KB == headerSize) {
	    cartridgeTypeNumericID = CartridgeFileUtility
		    .getCartridgeTypeNumericId(content);
	    if (cartridgeTypeNumericID > 0) {

		content = CartridgeFileUtility.getCartridgeContent(content);
		type = ROM.UNKNOWN; // Not an OS

	    }
	}

	// Set the content
	ROM rom = new ROM(new ByteArray(content), type);
	entry.setROM(rom);

	// Try to find the ROM version
	ROMVersion romVersion = romVersionDatabase.getByCRC32(rom.getSize(),
		rom.getCRC32HexString());
	if (romVersion != ROMVersion.UNKNOWN) {
	    entry.setROMVersion(romVersion);
	} else {
	    List<CartridgeDatabaseEntry> cartridgeDatabaseEntries;
	    int sizeInKB = rom.getSize() / ByteArrayUtility.KB;
	    cartridgeDatabaseEntries = cartridgeDatabase
		    .getEntriesBySizeAndCRC32(sizeInKB, rom.getCRC32());
	    if (!cartridgeDatabaseEntries.isEmpty()) {
		for (CartridgeDatabaseEntry cartridgeDatabaseEntry : cartridgeDatabaseEntries) {
		    romVersion = ROMVersionFactory
			    .createCartridgeDatabaseEntryROMVersion(
				    cartridgeDatabaseEntry,
				    rom.getMD5HexString());
		    entry.setROMVersion(romVersion);
		}
		return;

	    } else if (cartridgeTypeNumericID > 0) {
		CartridgeType carridgeType = CartridgeType
			.getInstance(cartridgeTypeNumericID);
		romVersion = ROMVersionFactory.createCartridgeTypeROMVersion(
			rom, carridgeType, "", "", "");
		entry.setROMVersion(romVersion);
	    }
	}

	ROM fixture = rom.createFixture(); // TODO Support CAR CheckSums
	if (!fixture.contentEquals(rom)) {
	    entry.setMessage("Incorrect checksums");

	    entry = new WorkbookEntry(file);
	    entry.setFileName(entry.getFileName() + " (fixed)");
	    workbook.addEntry(entry);
	    entry.setROM(fixture);
	    romVersion = romVersionDatabase.getByCRC32(fixture.getSize(),
		    fixture.getCRC32HexString());
	    entry.setROMVersion(romVersion);
	    entry.setMessage("With corrected checksums");

	}
    }

    @SuppressWarnings("static-method")
    public Comparison compareEntries(Workbook workbook,
	    List<WorkbookEntry> selectedEntries, MessageQueue messageQueue) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (selectedEntries == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'selectedEntries' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	if (selectedEntries.size() < Comparison.MIN) {
	    // ERROR: Select at least {0} entries for comparison.
	    messageQueue.sendMessage(workbook, null, Messages.E103,
		    TextUtility.formatAsDecimal(Comparison.MIN));
	    return null;
	}

	List<WorkbookEntry> validEntries = new ArrayList<WorkbookEntry>();
	int fileSize = -1;
	for (int i = 0; i < selectedEntries.size(); i++) {
	    WorkbookEntry entry = selectedEntries.get(i);
	    ROM rom = entry.getROM();
	    if (rom != null) {
		if (fileSize == -1) {
		    fileSize = rom.getSize();
		}
		if (rom.getSize() == fileSize) {
		    validEntries.add(entry);
		}
	    }
	}

	if (validEntries.size() < Comparison.MIN) {
	    // ERROR: Select at least {0} entries with equal file size for
	    // comparison.
	    messageQueue.sendMessage(workbook, null, Messages.E104,
		    TextUtility.formatAsDecimal(Comparison.MIN));
	    return null;
	}
	if (validEntries.size() > Comparison.MAX) {
	    // ERROR: Select at most {0} entries with equal file size for
	    // comparison.
	    messageQueue.sendMessage(workbook, null, Messages.E105,
		    TextUtility.formatAsDecimal(Comparison.MAX));
	    return null;
	}
	Comparison result = new Comparison(validEntries);

	Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
	for (int offset = 0; offset < fileSize; offset++) {
	    boolean same = true;
	    int i = 0;
	    int colorIndex = 0;
	    ROM rom = validEntries.get(i).getROM();
	    int b0 = rom.getByte(offset);
	    colorMap.clear();
	    colorMap.put(Integer.valueOf(b0), Integer.valueOf(colorIndex));
	    for (i = 1; i < validEntries.size(); i++) {
		int b1 = validEntries.get(i).getROM().getByte(offset);
		if (b1 != b0) {
		    same = false;
		}
		Integer key = Integer.valueOf(b1);
		Integer colorValue = colorMap.get(key);
		if (colorValue == null) {
		    colorIndex++;
		    colorMap.put(key, Integer.valueOf(colorIndex));
		}
	    }

	    // Create entry if there is only one file, or if there is a
	    // difference between multiple files.
	    if (validEntries.size() == 1 || !same) {

		ComparisonEntry entry = result.createEntry(offset,
			rom.toAddress(offset));
		for (i = 0; i < validEntries.size(); i++) {
		    int value = validEntries.get(i).getROM().getByte(offset);
		    entry.setValue(i,
			    "$" + HexUtility.getByteValueHexString(value),
			    colorMap.get(Integer.valueOf(value)).intValue());
		}
		result.addEntry(entry);
	    }
	}
	return result;
    }
}
