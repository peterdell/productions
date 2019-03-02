package com.wudsn.tools.thecartstudio.directory;

import com.wudsn.tools.base.gui.HTMLWriter;

/**
 * Copyright (C) 2013 - 2019 <a href="http://www.wudsn.com" target="_top">Peter
 * Dell</a>
 * 
 * This file is part of The!Cart Studio distribution.
 * 
 * The!Cart Studio is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 2 of the License, or (at your option) any
 * later version.
 * 
 * The!Cart Studio distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * The!Cart Studio. If not, see <http://www.gnu.org/licenses/>.
 */

abstract class DirectoryEntry {

	private int id;
	private String idString;

	private int bank;
	private int offset;
	protected int paddingBytes;

	protected DirectoryEntry() {
		id = -1;
		idString = "";
	}

	final void setId(int id) {
		this.id = id;
		idString = Integer.toString(id);
	}

	public final int getId() {
		return id;
	}

	public final String getIdString() {
		return idString;
	}

	public abstract int getRawSize();

	final void setBankAndOffset(int bank, int offset) {
		this.bank = bank;
		this.offset = offset;
	}

	public final int getBank() {
		return bank;
	}

	public final int getOffset() {
		return offset;
	}

	final void setPaddingBytes(int number) {
		this.paddingBytes = number;
	}

	abstract void writeAsBinary(DirectoryWriter directoryWriter);

	void writeAsHTML(HTMLWriter htmlWriter) {
		if (htmlWriter == null) {
			throw new IllegalArgumentException(
					"Parameter 'htmlWriter' must not be null.");
		}
		htmlWriter.writeTableCell(idString);
		htmlWriter.writeTableCell("$" + Integer.toHexString(bank));
		htmlWriter.writeTableCell("$" + Integer.toHexString(offset));
		htmlWriter.writeTableCell("$" + Integer.toHexString(getRawSize()));
		htmlWriter.writeTableCell("$" + Integer.toHexString(paddingBytes));

	}

}
