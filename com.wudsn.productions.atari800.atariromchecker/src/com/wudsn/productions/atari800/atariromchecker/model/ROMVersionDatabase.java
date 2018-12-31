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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.common.ResourceUtility;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.io.CSVReader;

/**
 * Database of all known ROM versions.
 * 
 * @author Peter Dell
 * 
 */
public final class ROMVersionDatabase {

    private List<ROMVersion> entries;

    public ROMVersionDatabase() {
	entries = new ArrayList<ROMVersion>();
    }

    public void load() {
	CSVReader csvReader = new CSVReader();
	byte[] fileContent = ResourceUtility
		.loadResourceAsByteArray("ROMVersions.csv");
	InputStream headerInputStream = new ByteArrayInputStream(fileContent);
	InputStream bodyInputStream = new ByteArrayInputStream(fileContent);
	csvReader.open(headerInputStream, bodyInputStream, ';', "UTF-8");
	while (csvReader.readNextRow()) {
	    String fileSize = csvReader.getColumnValue("File Size");
	    String crc32 = csvReader.getColumnValue("CRC32");
	    if (StringUtility.isSpecified(fileSize)) {
		String md5 = csvReader.getColumnValue("MD5");
		String type = csvReader.getColumnValue("Type");
		String id = csvReader.getColumnValue("Id");
		String publisher = csvReader.getColumnValue("Publisher");

		String year = csvReader.getColumnValue("Year");
		String month = csvReader.getColumnValue("Month");
		String day = csvReader.getColumnValue("Day");
		String revision = csvReader.getColumnValue("Revision");

		String date;
		if (year.length() == 4) {
		    date = year.substring(0, 4);
		    if (month.length() >= 0 && month.length() <= 2) {
			if (month.length() == 1) {
			    month = "0" + month;
			}
			date += "-" + month;
			if (day.length() >= 0 && day.length() <= 2) {
			    if (day.length() == 1) {
				day = "0" + day;
			    }
			    date += "-" + day;
			}
		    }
		} else {
		    date = year;
		}
		String norm = csvReader.getColumnValue("Norm");
		String parts = csvReader.getColumnValue("Parts");
		String comment = csvReader.getColumnValue("Comment");
		ROMVersion romVersion = ROMVersion.createInstance(
			Integer.parseInt(fileSize), crc32, md5, type, id,
			publisher, date, revision, norm, parts, comment);
		entries.add(romVersion);
	    }
	}
	Collections.sort(entries);
    }

    public List<ROMVersion> getEntries() {
	return Collections.unmodifiableList(entries);
    }

    public ROMVersion getByCRC32(int fileSize, String crc32) {
	if (crc32 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'crc32' must not be null.");
	}
	for (int i = 0; i < entries.size(); i++) {
	    ROMVersion romVersion = entries.get(i);
	    if (romVersion.getFileSize() == fileSize
		    && romVersion.getCRC32().equals(crc32)) {
		return romVersion;
	    }
	}
	return ROMVersion.UNKNOWN;
    }

}