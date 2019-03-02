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

import com.wudsn.tools.base.common.MessageQueue;

/**
 * Provides reserved content for a continuous sequence of banks.
 * 
 * @author Peter Dell
 * 
 */
public abstract class ReservedContentProvider {

	private String title;
	private int requiredBankSize;
	private int startBankNumber;
	protected int requiredBanksCount;
	private boolean reservedForSystem;

	ReservedContentProvider() {
		title = "";
		startBankNumber = 0;
		requiredBanksCount = 0;
		reservedForSystem = false;
	}

	protected void init(String title, int requiredBankSize,
			int startBankNumber, int requiredBanksCount,
			boolean reservedForSystem) {
		if (title == null) {
			throw new IllegalArgumentException(
					"Parameter 'title' must not be null.");
		}
		if (requiredBankSize < 0) {
			throw new IllegalArgumentException(
					"Parameter 'requiredBankSize' must not be negative. Specifed value is "
							+ requiredBankSize + ".");
		}
		if (startBankNumber < 0) {
			throw new IllegalArgumentException(
					"Parameter 'startBankNumber' must not be negative. Specifed value is "
							+ startBankNumber + ".");
		}
		if (requiredBanksCount < 0) {
			throw new IllegalArgumentException(
					"Parameter 'requiredBanksCount' must not be negative. Specifed value is "
							+ requiredBanksCount + ".");
		}
		this.title = title;
		this.requiredBankSize = requiredBankSize;
		this.startBankNumber = startBankNumber;
		this.requiredBanksCount = requiredBanksCount;
		this.reservedForSystem = reservedForSystem;
		// Log.logError("Adding reserved content provider {0} with start bank number {1} and required banks count {2}.",
		// new Object[] { title, Integer.valueOf(startBankNumber),
		// Integer.valueOf(requiredBanksCount) }, null);
	}

	/**
	 * Initializes based on a given workbook root.
	 * 
	 * @param workbookRoot
	 *            The workbook root, not <code>null</code>.
	 */
	public abstract void init(WorkbookRoot workbookRoot);

	public String getTitle() {
		return title;
	}

	/**
	 * Gets the required bank size or <code>0</code> if the bank size does not
	 * matter.
	 * 
	 * @return The required bank size or <code>0</code> if the bank size does
	 *         not matter.
	 */
	public int getRequiredBankSize() {
		return requiredBankSize;
	}

	public int getStartBankNumber() {
		return startBankNumber;
	}

	public int getRequiredBanksCount() {
		return requiredBanksCount;
	}

	/**
	 * Determines if the banks of this provide are reserved for the system or
	 * for the user.
	 * 
	 * @return <code>true</code> if the banks of this provide are reserved for
	 *         the system, <code>false</code> f the banks of this provide are
	 *         reserved for the user (and must not be flashed).
	 */
	public boolean isReservedForSystem() {
		return reservedForSystem;
	}

	/**
	 * Creates the content to be inserted in the export data.
	 * 
	 * @param workbook
	 *            The workbook, not <code>null</code>.
	 * @param workbookExport
	 *            The contains for the complete workbook export, not
	 *            <code>null</code>.
	 * @param messageQueue
	 *            The message queue, not <code>null</code>.
	 * @return The content or <code>null</code>.
	 */
	public abstract byte[] createContent(Workbook workbook,
			WorkbookExport workbookExport, MessageQueue messageQueue);

	@Override
	public String toString() {
		return "title=" + title + " startBankNumber=" + startBankNumber
				+ " requiredBanksCount=" + requiredBanksCount
				+ "reservedForSystem=" + reservedForSystem;
	}

}
