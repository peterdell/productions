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

import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.repository.RepositoryValidation;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.UserSpaceContentProvider;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry.Attributes;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry.Parameter;

/**
 * Validation class.
 * 
 * @author Peter Dell
 */
public final class WorkbookEntryValidation {

	public static final WorkbookEntryValidation createInstance() {
		return new WorkbookEntryValidation();
	}

	@SuppressWarnings("static-method")
	public void validateSave(WorkbookRoot root, WorkbookEntry entry,
			MessageQueue messageQueue) {
		if (root == null) {
			throw new IllegalArgumentException(
					"Parameter 'root' must not be null.");
		}
		if (entry == null) {
			throw new IllegalArgumentException(
					"Parameter 'entry' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
		RepositoryValidation rv = RepositoryValidation
				.createInstance(messageQueue);
		rv.isStringValid(entry, Attributes.TITLE, entry.getTitle());

		rv.isStringValid(entry, Attributes.FILE_NAME, entry.getFileName());

		String genreName = entry.getGenreName();
		if (StringUtility.isSpecified(genreName)) {
			boolean defined = false;
			for (WorkbookGenre genre : root.getUnmodifiableGenresList()) {
				if (genre.getName().equalsIgnoreCase(genreName)) {
					// If equals ignoring case, adapt case.
					entry.setGenreName(genre.getName());
					defined = true;
				}
			}
			// ERROR: Genre name '{0}' is not defined.
			rv.isStringDefined(entry, Attributes.GENRE_NAME, genreName, defined);
		}
	}

	@SuppressWarnings("static-method")
	public void validateExport(WorkbookRoot root, WorkbookEntry entry,
			MessageQueue messageQueue) {
		if (root == null) {
			throw new IllegalArgumentException(
					"Parameter 'root' must not be null.");
		}
		if (entry == null) {
			throw new IllegalArgumentException(
					"Parameter 'entry' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
		if (entry.getContentType() == ContentType.UNKNOWN) {
			// ERROR: Content type is unknown. Select a
			// valid content type for the entry.
			messageQueue.sendMessage(entry, Attributes.CONTENT_TYPE,
					Messages.E405);
			return;
		}
		FlashTargetType flashTargetType = root.getFlashTargetType();
		if (!flashTargetType.isContentTypeSupported(entry.getContentType())) {
			// ERROR: Content type '{0}' is not
			// supported by the flash module type '{1}'. Select a supported
			// content type for the entry.
			messageQueue.sendMessage(entry, Attributes.CONTENT_TYPE,
					Messages.E406, entry.getContentType().getText(),
					flashTargetType.getText());
			return;
		}

		if (entry.getType().equals(WorkbookEntryType.FILE_ENTRY)) {
			// If banks are assigned, we are also sure that the start bank
			// is valid.
			if (!entry.areBanksAssigned()) {
				// ERROR: Entry has no banks assigned. Perform
				// "Edit/Reassign Banks" first to assign all entries to a start
				// bank where enough free banks are available.
				messageQueue.sendMessage(entry,
						Attributes.BANKS_ASSIGNED_INDICATOR, Messages.E400);
				return;
			}

			if (entry.getStartBankNumber() % entry.getAlignmentBanksCount() != 0) {
				if (entry.isStartBankFixed()) {
					// ERROR: Start bank {0} is not aligned to a multiple of
					// {1}. Specify a valid start bank.
					messageQueue.sendMessage(entry, Attributes.START_BANK,
							Messages.E414, TextUtility.formatAsDecimal(entry
									.getStartBankNumber()), TextUtility
									.formatAsDecimal(entry
											.getAlignmentBanksCount()));
				} else {
					// ERROR: Start bank {0} is not aligned to a multiple of
					// {1}.
					// Perform "Edit/Reassign Banks" first to assign all entries
					// to a properly aligned start bank.
					messageQueue.sendMessage(entry, Attributes.START_BANK,
							Messages.E415, TextUtility.formatAsDecimal(entry
									.getStartBankNumber()), TextUtility
									.formatAsDecimal(entry
											.getAlignmentBanksCount()));
				}
				return;
			}
		} else {
			RepositoryValidation rv = RepositoryValidation
					.createInstance(messageQueue);

			// User space entries require a user space.
			if (root.getUserSpaceBanksCount() == 0) {
				// ERROR: User space entries are not possible because no user
				// space defined in the workbook options.
				messageQueue.sendMessage(entry, Attributes.TYPE, Messages.E421);
				return;
			}

			// The start bank for user space entries must be in the user space.
			UserSpaceContentProvider userSpaceContentProvider = new UserSpaceContentProvider();
			userSpaceContentProvider.init(root);
			int startBankNumber = userSpaceContentProvider.getStartBankNumber();
			if (!rv.isLongValid(entry, Attributes.START_BANK, startBankNumber,
					startBankNumber + root.getUserSpaceBanksCount() - 1,
					entry.getStartBankNumber())) {
				return;
			}
		}

		try {
			entry.setParametersList(Parameter.getParametersList(entry
					.getParameters()));
		} catch (CoreException ex) {
			entry.setParametersList(null);
			messageQueue.sendMessage(ex.createMessageQueueEntry(entry,
					Attributes.PARAMETERS));
		}
	}
}
