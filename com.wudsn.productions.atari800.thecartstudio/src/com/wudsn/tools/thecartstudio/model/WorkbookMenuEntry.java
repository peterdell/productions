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

import com.wudsn.tools.base.common.ASCIIString;

/**
 * Representation of one or more menu entries that result from a workbook entry.
 */
public final class WorkbookMenuEntry implements Comparable<WorkbookMenuEntry> {

    public static final int SOURCE_TYPE_MENU_ENTRY = 0;
    public static final int SOURCE_TYPE_MENU_ENTRY_ITEM = 1;

    private WorkbookEntry workbookEntry;
    private boolean titleSet;
    private String title;
    private int sourceType;
    private int itemMenuVersion;
    private int itemNumber;

    public WorkbookMenuEntry(WorkbookEntry workbookEntry) {
	if (workbookEntry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbookEntry' must not be null.");
	}
	this.workbookEntry = workbookEntry;
	this.titleSet = false;
	this.title = "";
	this.sourceType = SOURCE_TYPE_MENU_ENTRY;
	this.itemNumber = 0;
    }

    public WorkbookEntry getWorkbookEntry() {
	return workbookEntry;
    }

    public void setTitle(String title) {
	if (title == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'title' must not be null.");
	}
	this.titleSet = true;
	this.title = title;
    }

    public String getTitle() {
	if (titleSet) {
	    return title;
	}
	return workbookEntry.getTitle();
    }

    public void setSourceType(int sourceType) {
	this.sourceType = sourceType;
    }

    public int getSourceType() {
	return sourceType;
    }

    public void setItemMenuVersion(int itemMenuVersion) {
	this.itemMenuVersion = itemMenuVersion;
    }

    public int getItemMenuVersion() {
	return itemMenuVersion;
    }

    public void setItemNumber(int itemNumber) {
	this.itemNumber = itemNumber;
    }

    public int getItemNumber() {
	return itemNumber;
    }

    @Override
    public int compareTo(WorkbookMenuEntry other) {
	// Compare ignoring case.
	return ASCIIString.CASE_INSENSITIVE_COMPARATOR.compare(getTitle(),
		other.getTitle());
    }

}