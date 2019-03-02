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

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;

/**
 * Result of an {@link Workbook#export(int, MessageQueue)} call.
 * 
 * @author Peter Dell
 * 
 */
public final class WorkbookExport {

	private Workbook workbook;
	private byte[] data;
	private List<WorkbookMenuEntry> menuEntries;

	public WorkbookExport(Workbook workbook, byte[] data) {
		if (workbook == null) {
			throw new IllegalArgumentException(
					"Parameter 'workbook' must not be null.");
		}
		if (data == null) {
			throw new IllegalArgumentException(
					"Parameter 'data' must not be null.");
		}
		this.workbook = workbook;
		this.data = data;
		this.menuEntries = Collections.emptyList();
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public byte[] getData() {
		return data;
	}

	public void print(PrintWriter writer) {
		if (writer == null) {
			throw new IllegalArgumentException(
					"Parameter 'writer' must not be null.");
		}
		WorkbookRoot root = workbook.getRoot();
		writer.println(root.getTitle());
		writer.println();
		int menuEntryNumber = 1;
		for (WorkbookMenuEntry menuEntry : menuEntries) {
			writer.print(TextUtility.formatAsDecimal(menuEntryNumber));
			writer.print(": \"");
			writer.print(menuEntry.getTitle());
			writer.print("\" from \"");
			writer.print(menuEntry.getWorkbookEntry().getTitle());
			writer.print("\" in \"");
			writer.print(menuEntry.getWorkbookEntry().getFileName());
			writer.println("\"");
			menuEntryNumber++;
		}
	}

	void setMenuEntries(List<WorkbookMenuEntry> menuEntries) {
		if (menuEntries == null) {
			throw new IllegalArgumentException(
					"Parameter 'menuEntries' must not be null.");
		}
		this.menuEntries = Collections.unmodifiableList(menuEntries);
	}

}
