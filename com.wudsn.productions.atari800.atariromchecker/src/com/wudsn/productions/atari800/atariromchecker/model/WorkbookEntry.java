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

import com.wudsn.productions.atari800.atariromchecker.DataTypes;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.repository.Attribute;

public final class WorkbookEntry {
    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute ID = new Attribute("id",
		DataTypes.WorkbookEntry_Id);
	public static final Attribute FOLDER_PATH = new Attribute("folderPath",
		DataTypes.WorkbookEntry_FolderPath);
	public static final Attribute FILE_NAME = new Attribute("fileName",
		DataTypes.WorkbookEntry_FileName);
	public static final Attribute FILE_SIZE = new Attribute("fileSize",
		DataTypes.WorkbookEntry_FileSize);
	public static final Attribute SIZE = new Attribute("size",
		DataTypes.WorkbookEntry_Size);
	public static final Attribute MD5 = new Attribute("md5",
		DataTypes.WorkbookEntry_MD5);
	public static final Attribute CRC32 = new Attribute("crc32",
		DataTypes.WorkbookEntry_CRC32);
	public static final Attribute CHECK_SUMS = new Attribute("crc32",
		DataTypes.WorkbookEntry_CheckSums);
	public static final Attribute MESSAGE = new Attribute("message",
		DataTypes.WorkbookEntry_Message);

    }

    private File file;
    private String folderPath;
    private String fileName;
    private long fileSize;
    private String md5HexString;
    private String crc32HexString;
    private ROM rom;
    private ROMVersion romVersion;
    private String message;

    public WorkbookEntry(File file) {
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	this.file = file;
	this.folderPath = file.getParentFile().getPath();
	this.fileName = file.getName();
	this.fileSize=file.length();

	md5HexString = "";
	crc32HexString = "";
	rom = null;
	romVersion = null;
	message = "";
    }

    public File getFile() {
	return file;
    }

    public String getFolderPath() {
	return folderPath;
    }

    public void setFileName(String fileName) {
	if (fileName == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'fileName' must not be null.");
	}
	this.fileName = fileName;
    }

    public String getFileName() {
	return fileName;
    }

    public long getFileSize(){
	return fileSize;
    }
    
    public String getMD5() {
	return md5HexString;
    }

    public void setMD5(String md5) {
	if (md5 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'md5' must not be null.");
	}
	this.md5HexString = md5;
    }

    public boolean isMD5forCRC32() {
	if (StringUtility.isEmpty(md5HexString) || romVersion == null
		|| StringUtility.isEmpty(romVersion.getCRC32())) {
	    return true;
	}
	return md5HexString.equals(romVersion.getMD5());
    }

    public String getCRC32() {
	return crc32HexString;
    }

    public void setCRC32(String crc32) {
	if (crc32 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'crc32' must not be null.");
	}
	this.crc32HexString = crc32;
    }

    public void setROM(ROM rom) {
	if (rom == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'rom' must not be null.");
	}
	this.rom = rom;

	setMD5(rom.getMD5HexString());
	setCRC32(rom.getCRC32HexString());

    }

    public ROM getROM() {
	return rom;
    }

    public void setROMVersion(ROMVersion romVersion) {
	if (romVersion == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'romVersion' must not be null.");
	}
	this.romVersion = romVersion;
    }

    public ROMVersion getROMVersion() {
	return romVersion;
    }

    public void setMessage(String message) {
	if (message == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'message' must not be null.");
	}
	this.message = message;
    }

    public String getMessage() {
	return message;
    }

}
