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

import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.repository.ValueSet;
import com.wudsn.tools.thecartstudio.ValueSets;

/**
 * Value set representing the header type of a file.
 */
public final class FileHeaderType extends ValueSet {

	public static final FileHeaderType NONE;
	public static final FileHeaderType CART;

	// Instances
	private static final Map<String, FileHeaderType> values;

	static {

		values = new TreeMap<String, FileHeaderType>();

		NONE = add("NONE");
		CART = add("CART");

		initializeClass(FileHeaderType.class, ValueSets.class);
	}

	private FileHeaderType(String id) {
		super(id, 0);

	}

	private static FileHeaderType add(String id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}

		FileHeaderType result = new FileHeaderType(id);
		values.put(id, result);
		return result;
	}

	/**
	 * Gets the unmodifiable list of all values
	 * 
	 * @return The unmodifiable list of all values, not <code>null</code>.
	 */
	public static List<FileHeaderType> getValues() {
		return Collections.unmodifiableList(new ArrayList<FileHeaderType>(
				values.values()));
	}

	/**
	 * Gets a value set instance by its id.
	 * 
	 * @param id
	 *            The id, not <code>null</code>.
	 * @return The value set instance or <code>null</code>.
	 */
	public static FileHeaderType getInstance(String id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}
		return values.get(id);
	}

	/**
	 * Gets the size of the header for this file type.
	 * 
	 * @return The size of the header, a non-negative integer.
	 */
	public int getHeaderSize() {
		return this == FileHeaderType.CART ? CartridgeFileUtility.CART_HEADER_SIZE
				: 0;
	}
}
