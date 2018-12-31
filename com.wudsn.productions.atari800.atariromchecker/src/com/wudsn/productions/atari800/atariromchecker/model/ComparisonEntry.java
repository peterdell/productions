/**
 * Copyright (C) 2014 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of the Atari ROM Checker.
 * 
 * ROM Checker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * ROM Checker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Atari ROM Checker  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wudsn.productions.atari800.atariromchecker.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.wudsn.productions.atari800.atariromchecker.DataTypes;
import com.wudsn.tools.base.repository.Attribute;

public final class ComparisonEntry {
    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute ID = new Attribute("id", DataTypes.ComparisonEntry_Id);
	public static final Attribute OFFSET = new Attribute("offset", DataTypes.ComparisonEntry_Offset);
	public static final Attribute ADDRESS = new Attribute("address", DataTypes.ComparisonEntry_Address);
	public static final Attribute VALUE = new Attribute("value", DataTypes.ComparisonEntry_Value);
    }

    private int offset;
    private int address;
    private List<String> values;
    private List<Integer> colors;

    ComparisonEntry(int columnCount) {
	values = new ArrayList<String>(columnCount);
	colors = new ArrayList<Integer>(columnCount);
	for (int i = 0; i < columnCount; i++) {
	    values.add("");
	    colors.add(Integer.valueOf(0));
	}
    }

    void setOffset(int offset) {
	this.offset = offset;
    }

    public int getOffset() {
	return offset;
    }

    void setAddress(int address) {
	this.address = address;
    }

    public int getAddress() {
	return address;
    }

    void setValue(int column, String value, int color) {
	values.set(column, value);
	colors.set(column, Integer.valueOf(color));
    }

    public String getValue(int column) {
	return values.get(column);
    }

    public Color getColor(int column) {
	Integer colorKey = colors.get(column);
	switch (colorKey.intValue()) {
	case 0:
	    return Color.GREEN;
	case 1:
	    return Color.RED;
	case 2:
	    return Color.YELLOW;
	case 3:
	    return Color.ORANGE;
	case 4:
	    return Color.PINK;
	}
	return Color.WHITE;
    }

    @Override
    public String toString() {
	return "offset=" + offset + "/address=" + address + "/values=" + values.toString();
    }

}
