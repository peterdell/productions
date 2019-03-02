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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wudsn.tools.base.gui.HTMLWriter;

public final class DirectoryLevel extends DirectoryEntry {

	private String prefix;
	private char character;
	private List<String> words;
	private Map<String, DirectoryLevel> children;
	private List<DirectoryRecord> records;

	public DirectoryLevel() {

		prefix = "";
		character = 0;
		words = new ArrayList<String>();
		children = new TreeMap<String, DirectoryLevel>();
		records = new ArrayList<DirectoryRecord>();
	}

	void setPrefix(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException(
					"Parameter 'prefix' must not be null.");
		}
		this.prefix = prefix;
		if (prefix.length() > 0) {
			this.character = prefix.charAt(prefix.length() - 1);
		}
	}

	public String getPrefix() {
		return prefix;
	}

	public char getCharacter() {
		return character;
	}

	public List<DirectoryRecord> getRecords() {
		return records;
	}

	public List<String> getWords() {
		return words;
	}

	@Override
	public int getRawSize() {
		return 1 + children.size() * 3 + records.size() * 2 + 2;
	}

	void createChildren(Directory directory, int prefixLength) {
		if (directory == null) {
			throw new IllegalArgumentException(
					"Parameter 'directory' must not be null.");
		}
		if (words.isEmpty()) {
			return;
		}
		String currentWord = null;
		String currentPrefix = null;
		DirectoryLevel child = null;
		String lastPrefix = null;

		for (int i = 1; i < words.size(); i++) {
			currentWord = words.get(i);
			currentPrefix = currentWord.substring(0, prefixLength);
			if (i == 1 || !currentPrefix.equals(lastPrefix)) {
				child = directory.createLevel();
				children.put(currentPrefix.substring(prefixLength - 1), child);
				child.setPrefix(currentPrefix);
				child.words.add(currentWord);
				lastPrefix = currentPrefix;
			} else {
				child.words.add(currentWord);

			}
			for (DirectoryRecord record : records) {
				if (record.getWords().contains(currentWord)
						&& !child.getRecords().contains(record)) {
					child.getRecords().add(record);
				}
			}
			Collections.sort(child.getRecords(), new RecordIdComparator());

		}
		for (DirectoryLevel child2 : children.values()) {
			child2.createChildren(directory, prefixLength + 1);

		}
	}

	@Override
	void writeAsBinary(DirectoryWriter directoryWriter) {
		if (directoryWriter == null) {
			throw new IllegalArgumentException(
					"Parameter 'directoryWriter' must not be null.");
		}
		directoryWriter.writeByte(children.size());

		for (DirectoryLevel child : children.values()) {
			directoryWriter.writeByte(child.character);
			directoryWriter.writeWord(child.getId());
		}

		for (DirectoryRecord record : records) {
			directoryWriter.writeWord(record.getId());
		}
		// Record list is 0 terminated
		directoryWriter.writeWord(0);

		directoryWriter.writePaddingBytes(paddingBytes);
	}

	@Override
	void writeAsHTML(HTMLWriter htmlWriter) {
		if (htmlWriter == null) {
			throw new IllegalArgumentException(
					"Parameter 'htmlWriter' must not be null.");
		}

		super.writeAsHTML(htmlWriter);

		htmlWriter.writeTableCell(prefix);

		StringBuilder cellBuilder = new StringBuilder();

		for (Map.Entry<String, DirectoryLevel> entry : children.entrySet()) {
			cellBuilder.append("<a href=\"#level_"
					+ entry.getValue().getIdString() + "\">");
			cellBuilder.append(entry.getKey());
			cellBuilder.append("</a>");
			cellBuilder.append(" ");
		}
		htmlWriter.writeTableCell(cellBuilder.toString());

		htmlWriter.writeTableCell(Integer.toString(records.size()));

		cellBuilder.setLength(0);
		for (DirectoryRecord record : records) {
			cellBuilder.append("<a href=\"#record_" + record.getIdString()
					+ "\">");
			cellBuilder.append(record.getIdString());
			cellBuilder.append("</a>");
			cellBuilder.append(" ");

		}
		htmlWriter.writeTableCell(cellBuilder.toString());

	}

	@Override
	public String toString() {
		return "id=" + getIdString() + "prefix=" + prefix + "/words="
				+ words.size() + " " + words + " records=" + records
				+ "/\nchildren=" + children + "\n";
	}
}
