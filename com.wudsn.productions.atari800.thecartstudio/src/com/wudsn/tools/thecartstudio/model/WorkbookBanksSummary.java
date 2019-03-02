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

public final class WorkbookBanksSummary {
	int definedBanksCount;
	int reservedBanksCount;
	int requiredBanksCount;

	/**
	 * Number of banks defined in the workbook.
	 * 
	 * @return The number of banks, a non-negative integer.
	 */
	public int getDefinedBanks() {
		return definedBanksCount;
	}

	/**
	 * Number of banks reserved by reserved content providers.
	 * 
	 * @return The number of banks, a non-negative integer.
	 */
	public int getReservedBanks() {
		return reservedBanksCount;
	}

	/**
	 * Number of banks require by all entries.
	 * 
	 * @return The number of banks, a non-negative integer.
	 */
	public int getRequiredBanks() {
		return requiredBanksCount;
	}

	/**
	 * Number of banks exceeding the defined number of banks.
	 * 
	 * @return The number of banks, a non-negative integer.
	 */
	public int getExceededBanks() {
		return getTotalBanks() - definedBanksCount;
	}

	/**
	 * The total number of banks.
	 * 
	 * @return The number of banks, a non-negative integer.
	 */
	public int getTotalBanks() {
		return Math.max(definedBanksCount, reservedBanksCount
				+ requiredBanksCount);
	}
}
