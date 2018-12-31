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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wudsn.tools.base.atari.cartridge.CartridgeDatabase;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabaseEntry;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.HTMLWriter;
import com.wudsn.tools.base.repository.Attribute;

/**
 * The ROMVersionWriter exports the ROM definition as HTML. It is called by the
 * online help in the program itself. It is also called by the ANT script to
 * create a static version of the HTML help page including the ROM version
 * information.
 * 
 * @author Peter Dell
 */
public final class ROMVersionWriter {

    public static void main(String args[]) throws CoreException {
	if (args.length != 1) {
	    throw new IllegalArgumentException(
		    "Output file name must be specified");
	}
	String content = getHTML();
	FileUtility.writeString(new File(args[0]), content);
    }

    public static String getHTML() {
	HTMLWriter writer = new HTMLWriter();
	writer.beginTable();
	ROMVersionDatabase romVersionDatabase = new ROMVersionDatabase();
	romVersionDatabase.load();

	CartridgeDatabase cartridgeDatabase = new CartridgeDatabase();
	cartridgeDatabase.load();

	// Create combined list.
	List<ROMVersion> romVersions = new ArrayList<ROMVersion>(
		romVersionDatabase.getEntries());
	List<CartridgeDatabaseEntry> cartridgeDatabaseEntries = cartridgeDatabase
		.getEntries();
	for (CartridgeDatabaseEntry cartridgeDatabaseEntry : cartridgeDatabaseEntries) {
	    romVersions.add(ROMVersionFactory
		    .createCartridgeDatabaseEntryROMVersion(
			    cartridgeDatabaseEntry, ""));
	}

	// Create header
	writerTableHeader(writer, ROMVersion.Attributes.FILE_SIZE);
	writerTableHeader(writer, ROMVersion.Attributes.CRC32);
	writerTableHeader(writer, ROMVersion.Attributes.MD5);
	writerTableHeader(writer, ROMVersion.Attributes.TYPE);
	writerTableHeader(writer, ROMVersion.Attributes.ID);
	writerTableHeader(writer, ROMVersion.Attributes.PUBLISHER);
	writerTableHeader(writer, ROMVersion.Attributes.DATE);
	writerTableHeader(writer, ROMVersion.Attributes.REVISION);
	writerTableHeader(writer, ROMVersion.Attributes.NORM);
	writerTableHeader(writer, ROMVersion.Attributes.PARTS);
	writerTableHeader(writer, ROMVersion.Attributes.COMMENT);

	for (ROMVersion romVersion : romVersions) {
	    writer.beginTableRow();
	    writer.writeTableCell(TextUtility.formatAsMemorySize(romVersion
		    .getFileSize()));
	    writer.writeTableCell(romVersion.getCRC32());
	    writer.writeTableCell(romVersion.getMD5());
	    writer.writeTableCell(romVersion.getType());
	    writer.writeTableCell(romVersion.getId());
	    writer.writeTableCell(romVersion.getRevision());
	    writer.writeTableCell(romVersion.getDate());
	    writer.writeTableCell(romVersion.getNorm());
	    writer.writeTableCell(romVersion.getComment());
	    writer.writeTableCell(romVersion.getParts());
	    writer.end();
	}
	writer.end();
	return writer.toHTML();

    }

    private static void writerTableHeader(HTMLWriter writer, Attribute attribute) {
	if (writer == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'writer' must not be null.");
	}
	if (attribute == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'attribute' must not be null.");
	}
	writer.writeTableHeader(attribute.getDataType()
		.getLabelWithoutMnemonics());

    }
}
