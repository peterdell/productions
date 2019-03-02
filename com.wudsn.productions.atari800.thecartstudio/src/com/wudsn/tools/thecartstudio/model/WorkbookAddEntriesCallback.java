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

/**
 * Callback for user confirmation and results. Instance must only be used once.
 * 
 * @author Peter Dell
 */
public abstract class WorkbookAddEntriesCallback {
	public final static class AddResult {
		/**
		 * Creation is private
		 */
		private AddResult() {
		}

		public static final int CANCEL = -1;
		public static final int OVERWRITE = 1;
		public static final int RENAME = 2;
		public static final int SKIP = 3;
	}

	public final static class UseTitleResult {
		/**
		 * Creation is private
		 */
		private UseTitleResult() {
		}

		public static final int CANCEL = -1;
		public static final int YES = 1;
		public static final int NO = 2;
	}

	protected int lastAddResult;
	protected boolean addAll;

	protected int lastUseTitleResult;
	protected boolean useTitleAll;

	int addedEntriesCount;
	int updatedEntriesCount;
	int skippedEntriesCount;

	/**
	 * Creation is protected.
	 */
	protected WorkbookAddEntriesCallback() {

	}

	public abstract int confirmAdd(String existingEntryTitle,
			String existingFileName, String newFilePath, String renamedFileName);

	public abstract int confirmUseTitleFromCartridgeDatabase(
			String entryFileName, String cartridgeDatabaseEntryTitle);

	public final int getAddedEntriesCount() {
		return addedEntriesCount;
	}

	public final int getUpdatesEntriesCount() {
		return updatedEntriesCount;
	}

	public final int getSkippedEntriesCount() {
		return skippedEntriesCount;
	}

	public boolean isCancelled() {
		return lastAddResult == AddResult.CANCEL
				|| lastUseTitleResult == UseTitleResult.CANCEL;
	}
}
