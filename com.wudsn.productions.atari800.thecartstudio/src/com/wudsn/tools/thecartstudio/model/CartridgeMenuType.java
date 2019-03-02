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

public final class CartridgeMenuType extends ValueSet {

	public final static CartridgeMenuType NONE;
	public final static CartridgeMenuType SIMPLE;
	public final static CartridgeMenuType EXTENDED;

	// Instances
	private static List<CartridgeMenuType> values;
	private static final Map<String, CartridgeMenuType> map;

	static {
		values = new ArrayList<CartridgeMenuType>();
		map = new TreeMap<String, CartridgeMenuType>();

		NONE = add("NONE", 0);
		SIMPLE = add("SIMPLE", 1);
		EXTENDED = add("EXTENDED", 2);

		values = Collections.unmodifiableList(values);
		initializeClass(CartridgeMenuType.class, ValueSets.class);

	}

	private CartridgeMenuType(String id, int sortKey) {
		super(id, sortKey);

	}

	private static CartridgeMenuType add(String id, int sortKey) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}

		CartridgeMenuType result = new CartridgeMenuType(id, sortKey);
		values.add(result);
		map.put(id, result);
		return result;
	}

	/**
	 * Gets the unmodifiable list of all values.
	 * 
	 * @return The unmodifiable list of all values, not <code>null</code>.
	 */
	public static List<CartridgeMenuType> getValues() {
		return values;
	}

	/**
	 * Gets a value set instance by its id.
	 * 
	 * @param id
	 *            The id, not <code>null</code>.
	 * @return The value set instance or <code>null</code>.
	 */
	public static CartridgeMenuType getInstance(String id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}
		return map.get(id);
	}

}
