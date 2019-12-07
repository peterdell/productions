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

import org.w3c.dom.Element;

import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.thecartstudio.DataTypes;

/**
 * Container for a single genre.
 * 
 * @author Peter Dell
 */
public final class WorkbookGenre {

	public static final String ALL = "All";

	public static final class Attributes {
		private Attributes() {
		}

		public static final String ELEMENT_NAME = "genre";

		public static final Attribute NAME = new Attribute("name",
				DataTypes.WorkbookGenre_Name);

	}

	private String name;

	public WorkbookGenre() {
		name = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException(
					"Parameter 'name' must not be null.");
		}
		this.name = name.trim();
	}

	final void serialize(Element element) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Parameter 'element' must not be null.");
		}
		Attributes.NAME.serializeString(element, getName());
	}

	final void deserialize(org.xml.sax.Attributes attributes) {
		if (attributes == null) {
			throw new IllegalArgumentException(
					"Parameter 'attributes' must not be null.");
		}

		String value;

		value = Attributes.NAME.deserializeString(attributes);
		if (value != null) {
			setName(value);
		}
	}

	@Override
	public String toString() {
		return "name=" + name;
	}

	public WorkbookGenre createCopy() {
		WorkbookGenre result = new WorkbookGenre();
		result.name = name;
		return result;
	}

	public boolean equals(WorkbookGenre other) {
		if (other == null) {
			throw new IllegalArgumentException(
					"Parameter 'other' must not be null.");
		}
		return other.name.equals(name);
	}
}
