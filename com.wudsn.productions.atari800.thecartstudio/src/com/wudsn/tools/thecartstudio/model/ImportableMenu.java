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

public abstract class ImportableMenu {

    /**
     * Results for {@link #collectMenuEntries}.
     */
    public static final class Result {
	private Result() {
	}

	public static final int NO_MENU_FOUND = 1;
	public static final int NOT_SUPPORTED_MENU_VERSION_FOUND = 2;
	public static final int NO_MENU_ENTRIES_FOUND = 3;
	public static final int MENU_ENTRIES_FOUND_BUT_NOT_STARTABLE = 4;
	public static final int MENU_ENTRIES_FOUND_AND_STARTABLE = 5;
    }

    public static interface Collector {
	/**
	 * Collect a single entry.
	 * 
	 * @param owner
	 *            The object representing the owner of the cartridge with an
	 *            importable menu or <code>null</code>.
	 * @param info
	 *            The information describing the menu.
	 */
	public void collectMenu(Object owner, String info);

	/**
	 * Collect a single entry.
	 * 
	 * @param owner
	 *            The object representing the owner of the cartridge with an
	 *            importable menu or <code>null</code>.
	 * @param itemMenuVersion
	 *            The menu version of the item.
	 * @param itemNumber
	 *            The zero-based number of the item.
	 * @param title
	 *            The title, may be empty, not <code>null</code>.
	 */
	public void collectMenuEntry(Object owner, int itemMenuVersion,
		int itemNumber, String title);
    }

    /**
     * Menu Item Version numbers used by the starter code.
     */
    public final class Version {
	/**
	 * Creation is private
	 */
	Version() {
	}

	public static final int ATARIMAX_OLD = 1;
	public static final int ATARIMAX_NEW = 2;
	public static final int MEGACART = 3;
    }

    protected static final char[] ATASCII;

    static {
	ATASCII = new char[256];
	for (int i = 0; i < 256; i++) {
	    int c = i & 127;
	    if (c < 96) {
		c = c + 32;
	    }
	    ATASCII[i] = (char) c;
	}
    }

    protected final byte[] content;
    private String creatingToolName;

    protected ImportableMenu(byte[] content, String creatingToolName) {
	if (content == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'content' must not be null.");
	}
	if (creatingToolName == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'creatingToolName' must not be null.");
	}
	this.content = content;
	this.creatingToolName = creatingToolName;
    }

    public final String getCreatingToolName() {
	return creatingToolName;
    }

    /**
     * Determines if the cartridge content (possibly with header) contains an
     * importable menu.
     * 
     * @return <code>true</code> if the cartridge contains a importable menu,
     *         <code>false</code> otherwise.
     */
    public abstract boolean hasMenuEntries();

    /**
     * Collect the menu entries from an importable menu.
     * 
     * @param owner
     *            The object representing the owner of the cartridge with an
     *            importable menu or <code>null</code>.
     * @param collector
     *            The collector callback.
     * 
     * @return The result code, see {@link Result}.
     */
    public abstract int collectMenuEntries(Object owner, Collector collector);

    protected final int getByte(int offset) {
	return content[offset] & 0xff;
    }

    protected final int getWord(int offset) {
	return (content[offset] & 0xff) + 256 * (content[offset + 1] & 0xff);
    }
}