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

import com.wudsn.tools.thecartstudio.Texts;

/**
 * Supported export formats.
 * 
 * @author Peter Dell
 * 
 */
public final class ExportFormat {
	/**
	 * Creation is private.
	 */
	private ExportFormat() {

	}

	public static final int BIN_IMAGE = 1;
	public static final int CAR_IMAGE = 2;
	public static final int ATR_IMAGE = 3;
	public static final int ATR_IMAGES = 4;

	public static final class FileExtensions {
		public static final String BIN_IMAGE = ".bin";
		public static final String CAR_IMAGE = ".car";
		public static final String ATR_IMAGE = ".atr";
		public static final String ATR_IMAGES = ".atr";
	}

	public static String getFileExtension(int exportFormat) {
		switch (exportFormat) {
		case BIN_IMAGE:
			return FileExtensions.BIN_IMAGE;
		case CAR_IMAGE:
			return FileExtensions.CAR_IMAGE;
		case ATR_IMAGE:
			return FileExtensions.ATR_IMAGE;
		case ATR_IMAGES:
			return FileExtensions.ATR_IMAGE;
		default:
			throw new IllegalArgumentException("Unsupported export format "
					+ exportFormat + ".");
		}
	}

	public static String getFileFilterDescription(int exportFormat) {
		switch (exportFormat) {
		case ExportFormat.BIN_IMAGE:
			return Texts.ExportFormat_BIN_IMAGE_FileFilterDescription;
		case ExportFormat.CAR_IMAGE:
			return Texts.ExportFormat_CAR_IMAGE_FileFilterDescription;
		case ExportFormat.ATR_IMAGE:
			return Texts.ExportFormat_ATR_IMAGE_FileFilterDescription;
		case ExportFormat.ATR_IMAGES:
			return Texts.ExportFormat_ATR_IMAGES_FileFilterDescription;

		default:
			throw new RuntimeException("Unknown export format " + exportFormat
					+ ".");
		}

	}
}
