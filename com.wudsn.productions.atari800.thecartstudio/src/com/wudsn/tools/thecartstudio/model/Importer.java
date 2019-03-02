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
import java.util.ArrayList;
import java.util.List;

import com.wudsn.tools.base.atari.cartridge.CartridgeDatabaseEntry;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.thecartstudio.model.maxflash.MaxflashImporter;
import com.wudsn.tools.thecartstudio.model.williams.WilliamsImporter;

/**
 * Logic for supported import formats.
 * 
 * @author Peter Dell
 * 
 */
public abstract class Importer {

    public static final class ImportResult {
	public CartridgeDatabaseEntry cartridgeDatabaseEntry;
	public File convertedFile;
	public CoreException convertedFileException;
    }

    protected Importer() {

    }

    public abstract void importFile(Workbook workbook, File file,
	    ImportResult result);

    /**
     * Automatically convert a file before it is added. The new file is created
     * in the source file folder. Existing target files are overwritten
     * silently. If conversion is required but the new file cannot be created,
     * an error is reported.
     * 
     * @param workbook
     *            The workbook, not <code>null</code>,
     * @param file
     *            The file, not <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>. Will contain error
     *            messages if the file does not exist or cannot be read.
     * 
     * @return The import result. If old file, the new file or <code>null</code>
     *         if the new file could not be created.
     */
    public static ImportResult autoConvertFile(Workbook workbook, File file,
	    MessageQueue messageQueue) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}

	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	List<Importer> importers = new ArrayList<Importer>();
	importers.add(new MaxflashImporter());
	importers.add(new WilliamsImporter());
	ImportResult result = new ImportResult();

	for (Importer importer : importers) {
	    importer.importFile(workbook, file, result);
	    if (result.convertedFile != null
		    || result.convertedFileException != null) {
		return result;
	    }
	}
	return result;
    }

}
