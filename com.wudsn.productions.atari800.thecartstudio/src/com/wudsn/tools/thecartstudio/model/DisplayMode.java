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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wudsn.tools.base.repository.ValueSet;
import com.wudsn.tools.thecartstudio.ValueSets;

public final class DisplayMode extends ValueSet {

	public final static DisplayMode SINGLE_ENTRY;
	public final static DisplayMode MULTIPLE_ENTRIES;

	// Instances
	private static final Map<String, DisplayMode> values;
	static {
		SINGLE_ENTRY = new DisplayMode("SINGLE_ENTRY");
		MULTIPLE_ENTRIES = new DisplayMode("MULTIPLE_ENTRIES");

		values = new TreeMap<String, DisplayMode>();
		values.put(SINGLE_ENTRY.id, SINGLE_ENTRY);
		values.put(MULTIPLE_ENTRIES.id, MULTIPLE_ENTRIES);

		initializeClass(DisplayMode.class, ValueSets.class);
	}

	private DisplayMode(String id) {
		super(id, 0);
	}

	/**
	 * Gets the unmodifiable list of all values
	 * 
	 * @return The unmodifiable list of all values, not <code>null</code>.
	 */
	public static List<DisplayMode> getValues() {
		return Collections.unmodifiableList(new ArrayList<DisplayMode>(values
				.values()));
	}

	/**
	 * Gets a value set instance by its id.
	 * 
	 * @param id
	 *            The id, not <code>null</code>.
	 * @return The value set instance or <code>null</code>.
	 */
	public static DisplayMode getInstance(String id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}
		return values.get(id);
	}
}
