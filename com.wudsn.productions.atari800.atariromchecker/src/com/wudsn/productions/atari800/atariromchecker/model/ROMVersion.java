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

import com.wudsn.productions.atari800.atariromchecker.DataTypes;
import com.wudsn.tools.base.repository.Attribute;

/**
 * Constants are named according to "sysrom.h" and "sysrom.c".
 * 
 * @author Peter Dell
 * 
 */
public final class ROMVersion implements Comparable<ROMVersion> {

    public static final ROMVersion UNKNOWN;

    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute FILE_SIZE = new Attribute("fileSize",
		DataTypes.ROMVersion_FileSize);
	public static final Attribute CRC32 = new Attribute("crc32",
		DataTypes.ROMVersion_CRC32);
	public static final Attribute MD5 = new Attribute("crc32",
		DataTypes.ROMVersion_MD5);

	public static final Attribute TYPE = new Attribute("type",
		DataTypes.ROMVersion_Type);
	public static final Attribute ID = new Attribute("id",
		DataTypes.ROMVersion_Id);
	public static final Attribute PUBLISHER = new Attribute("publisher",
		DataTypes.ROMVersion_Publisher);
	public static final Attribute REVISION = new Attribute("revision",
		DataTypes.ROMVersion_Revision);
	public static final Attribute NORM = new Attribute("norm",
		DataTypes.ROMVersion_Norm);
	public static final Attribute DATE = new Attribute("date",
		DataTypes.ROMVersion_Date);
	public static final Attribute COMMENT = new Attribute("comment",
		DataTypes.ROMVersion_Comment);
	public static final Attribute PARTS = new Attribute("parts",
		DataTypes.ROMVersion_Parts);
    }

    private final int fileSize;
    private final String crc32; // Hex String
    private final String md5; // Hex String
    private final String type;
    private final String id;
    private final String publisher;
    private final String date;
    private final String revision;
    private final String norm;
    private final String parts;

    private final String comment;

    static {
	UNKNOWN = new ROMVersion(0, "", "", "Unknown", "Unknown", "Unknown",
		"", "", "", "", "");
    }

    static ROMVersion createInstance(int fileSize, String crc32, String md5,
	    String type, String id, String publisher, String date,
	    String revision, String norm, String parts, String comment) {
	if (fileSize <= 0) {
	    throw new IllegalArgumentException(
		    "Parameter 'fileSize' must be positive. Specified value was "
			    + fileSize + ".");
	}
	if (crc32 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'crc32' must not be null.");
	}
	if (md5 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'md5' must not be null.");
	}
	if (type == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'type' must not be null.");
	}
	if (id == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'id' must not be null.");
	}
	if (publisher == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'publisher' must not be null.");
	}
	if (date == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'date' must not be null.");
	}
	if (revision == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'revision' must not be null.");
	}

	if (norm == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'norm' must not be null.");
	}

	if (parts == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'parts' must not be null.");
	}
	if (comment == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'comment' must not be null.");
	}
	ROMVersion result = new ROMVersion(fileSize, crc32, md5, type, id,
		publisher, date, revision, norm, parts, comment);
	return result;
    }

    private ROMVersion(int fileSize, String crc32, String md5, String type,
	    String id, String publisher, String date, String revision,
	    String norm, String parts, String comment) {
	if (fileSize < 0) {
	    throw new IllegalArgumentException(
		    "Parameter 'fileSize' must not be negative. Specified value was "
			    + fileSize + ".");
	}
	if (crc32 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'crc32' must not be null.");
	}
	if (md5 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'md5' must not be null.");
	}
	if (type == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'type' must not be null.");
	}
	if (id == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'id' must not be null.");
	}
	if (publisher == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'publisher' must not be null.");
	}
	if (date == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'date' must not be null.");
	}
	if (revision == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'revision' must not be null.");
	}
	if (norm == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'norm' must not be null.");
	}
	if (parts == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'parts' must not be null.");
	}
	if (comment == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'comment' must not be null.");
	}

	this.fileSize = fileSize;
	this.crc32 = crc32;
	this.md5 = md5;
	this.type = type;
	this.id = id;
	this.publisher = publisher;
	this.date = date;
	this.revision = revision;
	this.norm = norm;
	this.parts = parts;
	this.comment = comment;
    }

    public int getFileSize() {
	return fileSize;
    }

    public String getCRC32() {
	return crc32;
    }

    public String getMD5() {
	return md5;
    }

    public String getType() {
	return type;
    }

    public String getId() {
	return id;
    }

    public String getPublisher() {
	return publisher;
    }

    public String getDate() {
	return date;
    }

    public String getRevision() {
	return revision;
    }

    public String getNorm() {
	return norm;
    }

    public String getParts() {
	return parts;
    }

    public String getComment() {
	return comment;
    }

    @Override
    public String toString() {
	return type + "/" + id + "/" + revision + "/" + date + "/" + norm + "/"
		+ comment + "/" + parts;
    }

    @Override
    public int compareTo(ROMVersion o) {
	int result = type.compareTo(o.type);

	if (result == 0) {
	    result = date.compareTo(o.date);
	}
	if (result == 0) {
	    result = id.compareTo(o.id);
	}
	if (result == 0) {
	    result = revision.compareTo(o.revision);
	}
	return result;
    }

}