/*
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
package com.wudsn.tools.thecartstudio.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Bank color utility.
 * 
 * @author Peter Dell
 * 
 */
public final class BankColorUtility {

	/**
	 * Creation is private.
	 */
	private BankColorUtility() {

	}

	private static final Map<Color, Color> lineColorMap;

	static {
		lineColorMap = new HashMap<Color, Color>();
	}

	private static Color makeBrighter(Color color, float factor) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		r = (int) (r * factor);
		g = (int) (g * factor);
		b = (int) (b * factor);

		if (r > 255) {
			r = 255;
		}
		if (g > 255) {
			g = 255;
		}
		if (b > 255) {
			b = 255;
		}
		return new Color(r, g, b);
	}

	public static Color getLineColor(Color color) {
		if (color == null) {
			return color;
		}
		Color lineColor = lineColorMap.get(color);
		if (lineColor == null) {
			lineColor = makeBrighter(color, 0.8f);
			lineColorMap.put(color, lineColor);
		}
		return lineColor;
	}
}
