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

import java.util.ArrayList;
import java.util.List;

import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.thecartstudio.DataTypes;

/**
 * A bank in the workbook.
 * 
 * @author Peter Dell
 * 
 */
public final class WorkbookBank {

	/**
	 * Unused bytes in flash ROMs have all bits set.
	 */
	public static final byte UNUSED_BYTE = (byte) 0xff;

	public static final class Attributes {
		private Attributes() {
		}

		public static final String ELEMENT_NAME = "bank";

		public static final Attribute NUMBER = new Attribute("number",
				DataTypes.WorkbookBank_Number);
	}

	private int number;
	private ReservedContentProvider reservedContentProvider;
	private List<WorkbookEntry> entries;

	WorkbookBank(int number) {
		if (number < 0) {
			throw new IllegalArgumentException(
					"Bank number must not be negative. Specified value is "
							+ number + ".");
		}
		this.number = number;
		this.reservedContentProvider = null;

		entries = new ArrayList<WorkbookEntry>();
	}

	/**
	 * Gets the number of the bank.
	 * 
	 * @return The number of the bank, a non-negative integer.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Determines if the bank is reserved and must not be occupied by entries.
	 * 
	 * @return <code>true</code> if the bank is reserved and must not be
	 *         occupied by entries.
	 */
	public boolean isReserved() {
		return reservedContentProvider != null;
	}

	/**
	 * Package local access to set reserved content provider of a bank.
	 * 
	 * @param reservedContentProvider
	 */
	final void setReservedContentProvider(
			ReservedContentProvider reservedContentProvider) {
		this.reservedContentProvider = reservedContentProvider;
	}

	/**
	 * Gets the reserved content provider bank.
	 * 
	 * @return The reserved content provider or <code>null</code>.
	 */
	public ReservedContentProvider getReservedContentProvider() {
		return reservedContentProvider;
	}

	/**
	 * Gets the modifiable list of workbook entries assigned to the bank.
	 * 
	 * @return The modifiable list of workbook entries assigned to the bank, may
	 *         be empty, not <code>null</code>.
	 */
	public List<WorkbookEntry> getEntries() {
		return entries;
	}

	/**
	 * Determines if the bank is used, i.e. is reserved or has at least one
	 * workbook entry assigned.
	 * 
	 * @return <code>true</code> if the bank is used.
	 */
	public boolean isUsed() {
		return isReserved() || !entries.isEmpty();
	}

	@Override
	public String toString() {
		return "number=" + number + " reservedContentProvider=["
				+ reservedContentProvider + "] entries=" + entries.toString();
	}
}
