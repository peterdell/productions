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

import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.repository.RepositoryValidation;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.WorkbookGenre.Attributes;

/**
 * Validation class.
 * 
 * @author Peter Dell
 */
public final class WorkbookGenreValidation {

	public static final WorkbookGenreValidation createInstance() {
		return new WorkbookGenreValidation();
	}

	public void validateSave(WorkbookRoot root, MessageQueue messageQueue) {
		if (root == null) {
			throw new IllegalArgumentException(
					"Parameter 'root' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
		// Check the genres
		int size = root.getUnmodifiableGenresList().size();
		if (size > WorkbookRoot.MAX_GENRE_COUNT) {
			messageQueue.sendMessage(root, null, Messages.E427,
					TextUtility.formatAsDecimal(size),
					TextUtility.formatAsDecimal(WorkbookRoot.MAX_GENRE_COUNT));
		}
		for (WorkbookGenre genre : root.getUnmodifiableGenresList()) {
			validateSave(root, genre, messageQueue);
		}
	}

	@SuppressWarnings("static-method")
	public void validateSave(WorkbookRoot root, WorkbookGenre genre,
			MessageQueue messageQueue) {
		if (root == null) {
			throw new IllegalArgumentException(
					"Parameter 'root' must not be null.");
		}
		if (genre == null) {
			throw new IllegalArgumentException(
					"Parameter 'genre' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
		RepositoryValidation rv = RepositoryValidation
				.createInstance(messageQueue);
		String name = genre.getName();
		if (rv.isStringSpecified(genre, Attributes.NAME, name)) {
			if (name.equalsIgnoreCase(WorkbookGenre.ALL)) {
				// ERROR: Name '{0}' of the genre defined in the workbook is
				// reserved. Choose another name for the genre.
				messageQueue.sendMessage(genre, Attributes.NAME, Messages.E426,
						WorkbookGenre.ALL);
			} else {
				rv.isStringValid(genre, Attributes.NAME, genre.getName());
			}
		}
	}

	@SuppressWarnings("static-method")
	public void validateExport(WorkbookRoot root, WorkbookGenre genre,
			MessageQueue messageQueue) {
		if (root == null) {
			throw new IllegalArgumentException(
					"Parameter 'root' must not be null.");
		}
		if (genre == null) {
			throw new IllegalArgumentException(
					"Parameter 'genre' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
	}
}
