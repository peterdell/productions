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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.gui.AttributeTableModel;

public final class Workbook {

    private List<WorkbookEntry> entriesList;
    private List<WorkbookEntry> unmodifiableEntriesList;

    private int resolvedFilesCount;

    private AttributeTableModel entriesModel;

    public Workbook() {
	entriesList = new ArrayList<WorkbookEntry>();
	unmodifiableEntriesList = Collections.unmodifiableList(entriesList);
    }

    public void setEntriesTableModel(AttributeTableModel entriesModel) {
	this.entriesModel = entriesModel;
    }
    
    public void clear() {
	entriesList.clear();
	resolvedFilesCount = 0;
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    public void addEntry(WorkbookEntry entry) {
	if (entry == null) {
	    throw new IllegalArgumentException("Parameter 'entry' must not be null.");
	}
	entriesList.add(entry);
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    public WorkbookEntry getEntry(int row) {
	return entriesList.get(row);
    }

    public int getEntryCount() {
	return entriesList.size();
    }

    public List<WorkbookEntry> getUnmodifiableEntriesList() {
	return unmodifiableEntriesList;
    }

    public void setResolvedFilesCount(int resolvedFilesCount) {
	this.resolvedFilesCount = resolvedFilesCount;
    }

    public int getResolvedFilesCount() {
	return resolvedFilesCount;
    }
}
