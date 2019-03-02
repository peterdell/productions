package com.wudsn.tools.thecartstudio.directory;

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

import java.util.ArrayList;
import java.util.List;

import com.wudsn.tools.base.gui.HTMLWriter;

public class DirectoryRecord extends DirectoryEntry {

	private String title;
	private List<String> words;

	public DirectoryRecord() {
		words = new ArrayList<String>();
	}

	public String getTitle() {
		return title;
	}

	void setTitle(String title) {
		if (title == null) {
			throw new IllegalArgumentException(
					"Parameter 'title' must not be null.");
		}
		this.title = title;
	}

	public List<String> getWords() {
		return words;
	}

	@Override
	public int getRawSize() {
		return 1 + title.length();
	}

	@Override
	void writeAsBinary(DirectoryWriter directoryWriter) {
		if (directoryWriter == null) {
			throw new IllegalArgumentException(
					"Parameter 'directoryWriter' must not be null.");
		}
		// directoryWriter.writeWord(id);
		directoryWriter.writeByte(title.length());
		directoryWriter.writeString(title);
		directoryWriter.writePaddingBytes(paddingBytes);

	}

	@Override
	void writeAsHTML(HTMLWriter htmlWriter) {
		if (htmlWriter == null) {
			throw new IllegalArgumentException(
					"Parameter 'htmlWriter' must not be null.");
		}
		super.writeAsHTML(htmlWriter);
		htmlWriter.writeTableCell(title);
		htmlWriter.writeTableCell(HTMLWriter.getString(words));
	}

	@Override
	public String toString() {
		return "id=" + getId() + "/title=" + title + "/words=" + words;
	}

}
