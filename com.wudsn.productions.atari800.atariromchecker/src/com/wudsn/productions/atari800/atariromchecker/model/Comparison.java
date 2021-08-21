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

public final class Comparison {

    public static final int MIN = 1;
    public static final int MAX = 5;
    public static final int MAXIMUM_ADRESS=0xffff;
	
    private List<WorkbookEntry> workbookEntries;
    private List<ComparisonEntry> entriesList;
    private List<ComparisonEntry> unmodifiableEntriesList;

    private AttributeTableModel entriesModel;

    public Comparison(List<WorkbookEntry> workbookEntries) {
	if (workbookEntries == null) {
	    throw new IllegalArgumentException("Parameter 'workbookEntries' must not be null.");
	}
	this.workbookEntries=workbookEntries;
	entriesList = new ArrayList<ComparisonEntry>();
	unmodifiableEntriesList = Collections.unmodifiableList(entriesList);
    }

    public int getWorkbookEntryCount() {
	return workbookEntries.size();
    }

    public WorkbookEntry getWorkbookEntry(int index) {
	return workbookEntries.get(index);
    }

    public void setEntriesTableModel(AttributeTableModel entriesModel) {
	this.entriesModel = entriesModel;
    }

    public void clear() {
	entriesList.clear();
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    public ComparisonEntry createEntry(int offset, int address) {
	ComparisonEntry entry = new ComparisonEntry(getWorkbookEntryCount());
	entry.setOffset(offset);
	entry.setAddress(address);
	return entry;
    }

    public void addEntry(ComparisonEntry entry) {
	if (entry == null) {
	    throw new IllegalArgumentException("Parameter 'entry' must not be null.");
	}
	entriesList.add(entry);
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    public ComparisonEntry getEntry(int row) {
	return entriesList.get(row);
    }

    public int getEntryCount() {
	return entriesList.size();
    }

    public List<ComparisonEntry> getUnmodifiableEntriesList() {
	return unmodifiableEntriesList;
    }

    @SuppressWarnings("static-method")
    public long getMaximumOffset() {
	return 0xffff;
    }

    @SuppressWarnings("static-method")
    public long getMaximumAddress() {
	return MAXIMUM_ADRESS;
    }

    @Override
    public String toString() {
	return "workbookEntryCount=" + getWorkbookEntryCount() + "/entries=" + entriesList.toString();
    }
}
