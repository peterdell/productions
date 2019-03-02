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

import java.util.List;

import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.repository.RepositoryValidation;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot.Attributes;

/**
 * Validation class.
 * 
 * @author Peter Dell
 */
public final class WorkbookRootValidation {

	private WorkbookGenreValidation genreValidation;
	private WorkbookEntryValidation entryValidation;

	/**
	 * Creation is private.
	 */
	private WorkbookRootValidation() {
		genreValidation = WorkbookGenreValidation.createInstance();
		entryValidation = WorkbookEntryValidation.createInstance();

	}

	public static final WorkbookRootValidation createInstance() {
		return new WorkbookRootValidation();
	}

	public void validateSave(WorkbookRoot root, MessageQueue messageQueue,
			boolean withEntries) {
		if (root == null) {
			throw new IllegalArgumentException(
					"Parameter 'root' must not be null.");
		}
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}

		// Adapt some values automatically. User space must be a multiple of the
		// alignment.
		int userSpaceAlignmentSize = root.getUserSpaceAlignmentSize();
		if (userSpaceAlignmentSize > 0) {
			// Round up to next multiple of userSpaceAlignmentSize.
			root.setUserSpaceSize(userSpaceAlignmentSize
					* ((root.getUserSpaceSize() + userSpaceAlignmentSize - 1) / userSpaceAlignmentSize));

		}
		// Now start actual validation.
		RepositoryValidation rv = RepositoryValidation
				.createInstance(messageQueue);
		rv.isStringValid(root, WorkbookRoot.Attributes.TITLE, root.getTitle());
		boolean sizeValid = rv.isLongValid(root,
				WorkbookRoot.Attributes.BANK_COUNT, 1,
				WorkbookRoot.MAX_BANK_COUNT, root.getBankCount());
		sizeValid &= rv.isLongValid(root, WorkbookRoot.Attributes.BANK_SIZE,
				WorkbookRoot.MIN_BANK_SIZE, WorkbookRoot.MAX_BANK_SIZE,
				root.getBankSize());
		if (sizeValid) {
			// User space must be between 0 and the complete image size.
			rv.isMemorySizeValid(root, WorkbookRoot.Attributes.BANK_SIZE, 0,
					root.getImageSize(), root.getUserSpaceSize());
		}

		// In "User Defined" mode, this check is not executed here as all
		// content types are supported there.
		// This may lead to entries the can be flashed but not started from the
		// extended menu.
		// Adding more complex checks would be too bear much effort and bring a
		// too close dependency to the extended menu implementation.
		if (!root.getFlashTargetType().getSupportedCartridgeMenuTypes()
				.contains(root.getCartridgeMenuType())) {
			// ERROR: {0} '{1}' is not supported by the flash module '{2}'
			messageQueue.sendMessage(root, Attributes.CARTRDIGE_MENU_TYPE,
					Messages.E412, Attributes.CARTRDIGE_MENU_TYPE.getDataType()
							.getLabelWithoutMnemonics(), root
							.getCartridgeMenuType().getText(), root
							.getFlashTargetType().getText());
		}

		// Check the genres
		genreValidation.validateSave(root, messageQueue);

		// Check the entries if requested
		if (withEntries) {
			for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
				entryValidation.validateSave(root, entry, messageQueue);
			}
		}
	}

	public void validateExport(WorkbookRoot root, MessageQueue messageQueue) {

		// Export is only possible, the save is also possible.
		validateSave(root, messageQueue, true);
		if (messageQueue.containsError()) {
			return;
		}

		for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
			entryValidation.validateExport(root, entry, messageQueue);
		}
		if (messageQueue.containsError()) {
			return;
		}

		for (WorkbookBank bank : root.getBanksList()) {
			List<WorkbookEntry> entries = bank.getEntries();
			if (bank.isReserved() && entries.size() > 0) {
				// ERROR: Bank {0} is reserved but has entry '{1}' assigned.
				// Perform "Edit/Reassign Banks" again to assign all entries to
				// unreserved banks.
				messageQueue.sendMessage(bank, WorkbookBank.Attributes.NUMBER,
						Messages.E401, TextUtility.formatAsDecimal(bank
								.getNumber()), entries.get(0).getTitle());
				continue;
			}
			if (entries.size() > 1) {
				// ERROR: Bank {0} has multiple entries assigned. Perform
				// "Edit/Reassign Banks" again to assign all entries to unique
				// banks.
				messageQueue.sendMessage(bank, WorkbookBank.Attributes.NUMBER,
						Messages.E402,
						TextUtility.formatAsDecimal(bank.getNumber()));
				continue;
			}
		}
	}
}
