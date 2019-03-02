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

package com.wudsn.tools.thecartstudio.ui;

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;

import java.util.ArrayList;
import java.util.List;

import com.wudsn.tools.base.common.JDK.ComboBoxModel;
import com.wudsn.tools.base.common.JDK.DefaultComboBoxModel;
import com.wudsn.tools.base.repository.ValueSet;
import com.wudsn.tools.thecartstudio.model.ContentType;
import com.wudsn.tools.thecartstudio.model.FlashTargetType;

/**
 * Factory for content type lists, filtered by file content size.
 * 
 * @author Peter Dell
 */
public final class ContentTypeComboxBoxModelFactory {

	/**
	 * Gets the model for all content types that match the content size and are
	 * supported by the flash target type.
	 * 
	 * @param filterByFileContentSize
	 *            <code>true</code> to filter by fileContentSize,
	 *            <code>false</code> to filter only by flash target type.
	 * @param fileContentSize
	 *            The size of the file content in bytes.
	 * @param flashTargetType
	 *            The flash target type used as filter for the supported content
	 *            types.
	 * @param currentContentType
	 *            The current content type or <code>null</code>.
	 * @return The combo box model, not <code>null</code>.
	 */
	public static ComboBoxModel<ContentType> getModel(
			boolean filterByFileContentSize, int fileContentSize,
			FlashTargetType flashTargetType, ContentType currentContentType) {
		if (flashTargetType == null) {
			throw new IllegalArgumentException(
					"Parameter 'flashTargetType' must not be null.");
		}
		int key;
		if (fileContentSize <= 0) {
			key = 0;
		} else {
			if (fileContentSize % KB == 0) {
				key = fileContentSize / KB;
			} else {
				key = Integer.MAX_VALUE;
			}

		}

		List<ContentType> contentTypes = ContentType.getValues();
		List<ContentType> relevantContentTypes = new ArrayList<ContentType>();
		if (filterByFileContentSize) {
			switch (key) {
			// File empty no nor file at all.
			case 0:
				break;

			// File size is no multiple of 1K, so only binary and executable are
			// an option.
			case Integer.MAX_VALUE:
				relevantContentTypes.add(ContentType.FILE_BINARY);
				relevantContentTypes.add(ContentType.FILE_EXECUTABLE);
				break;

			default:
				// Filter all supported content types by the requested size.
				for (ContentType contentType : contentTypes) {
					int sizeInKB = contentType.getCartridgeType().getSizeInKB();
					// If size matches or size does not matter for the content
					// type..
					if (sizeInKB == key || sizeInKB == 0) {
						// ... and it is supported by the flash target type,
						// then add.
						if (flashTargetType.isContentTypeSupported(contentType)) {
							relevantContentTypes.add(contentType);
						}
					}
				}
				break;
			}
		} else {
			// Add all content types support for the flash target type.
			for (ContentType contentType : contentTypes) {
				if (flashTargetType.isContentTypeSupported(contentType)) {
					relevantContentTypes.add(contentType);
				}

			}
		}

		// The Unknown type and the current type are always allowed.
		if (!relevantContentTypes.contains(ContentType.UNKNOWN)) {
			relevantContentTypes.add(ContentType.UNKNOWN);
		}

		if (currentContentType != null
				&& !relevantContentTypes.contains(currentContentType)) {
			relevantContentTypes.add(currentContentType);
		}
		ValueSet.sort(relevantContentTypes);
		ComboBoxModel<ContentType> result = new DefaultComboBoxModel<ContentType>(
				relevantContentTypes
						.toArray(new ContentType[relevantContentTypes.size()]));

		return result;
	}
}
