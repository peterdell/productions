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

import static com.wudsn.tools.thecartstudio.model.CartDef.BANKS_PER_BLOCK;
import static com.wudsn.tools.thecartstudio.model.CartDef.HASH_BLOCK_COUNT;
import static com.wudsn.tools.thecartstudio.model.CartDef.HASH_BLOCK_START;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.Texts;

/**
 * Creates all available reserved content providers.
 * 
 * @author Peter Dell
 * 
 */
public final class ReservedContentProviderFactory {
	/**
	 * CheckSum area for incremental flashing if incremental flashing is
	 * supported.
	 */
	public static final class CheckSumContentProvider extends
			ReservedContentProvider {
		@Override
		public void init(WorkbookRoot root) {
			if (root == null) {
				throw new IllegalArgumentException(
						"Parameter 'root' must not be null.");
			}
			int requiredBanksCount = 0;
			if (root.getCartridgeType().isIncrementalFlashingSupported()) {
				requiredBanksCount = HASH_BLOCK_COUNT * BANKS_PER_BLOCK;
			}

			init(Texts.ReservedContentProvider_CheckSums, CartDef.BANKSIZE,
					HASH_BLOCK_START * BANKS_PER_BLOCK, requiredBanksCount,
					true);

		}

		@Override
		public byte[] createContent(Workbook workbook,
				WorkbookExport workbookExport, MessageQueue messageQueue) {
			return null;
		}
	}

	public static final class MenuContentProvider extends
			ReservedContentProvider {
		private CartridgeMenu cartridgeMenu;

		@Override
		public void init(WorkbookRoot root) {
			if (root == null) {
				throw new IllegalArgumentException(
						"Parameter 'root' must not be null.");
			}
			int bankSize;
			int requiredBanksCount;
			CartridgeMenuType cartridgeMenuType = root.getCartridgeMenuType();
			cartridgeMenu = CartridgeMenu.createInstance(cartridgeMenuType);

			if (cartridgeMenuType == CartridgeMenuType.NONE) {
				bankSize = root.getBankSize();
				requiredBanksCount = 0;
			} else if (cartridgeMenuType == CartridgeMenuType.SIMPLE
					|| cartridgeMenuType == CartridgeMenuType.EXTENDED) {
				bankSize = CartDef.BANKSIZE;
				requiredBanksCount = CartDef.HASH_BLOCK_START
						* CartDef.BANKS_PER_BLOCK;

			} else {
				throw new RuntimeException("Unknown cartridge menu type '"
						+ cartridgeMenuType + "'.");
			}

			init(Texts.ReservedContentProvider_Menu, bankSize, 0,
					requiredBanksCount, true);
		}

		@Override
		public byte[] createContent(Workbook workbook,
				WorkbookExport workbookExport, MessageQueue messageQueue) {
			if (workbook == null) {
				throw new IllegalArgumentException(
						"Parameter 'workbook' must not be null.");
			}
			if (workbookExport == null) {
				throw new IllegalArgumentException(
						"Parameter 'workbookExport' must not be null.");
			}
			if (messageQueue == null) {
				throw new IllegalArgumentException(
						"Parameter 'messageQueue' must not be null.");
			}

			// No menu means no content.
			WorkbookRoot root = workbook.getRoot();
			if (root.getCartridgeMenuType() == CartridgeMenuType.NONE) {
				return null;
			}

			// If the menu was created, it can still be invalid.
			if (!cartridgeMenu.isValid()) {
				messageQueue.sendMessage(root,
						WorkbookRoot.Attributes.CARTRDIGE_MENU_TYPE,
						Messages.E420, cartridgeMenu.getCartridgeMenuType()
								.getText());
				return null;
			}
			byte[] result = cartridgeMenu.getContent(workbook, workbookExport,
					messageQueue);
			return result;
		}
	}

	public static final class MenuEntriesContentProvider extends
			ReservedContentProvider {

		@Override
		public void init(WorkbookRoot root) {
			if (root == null) {
				throw new IllegalArgumentException(
						"Parameter 'root' must not be null.");
			}

			// Entries shall be located at the end before the user space.
			UserSpaceContentProvider userSpaceContentProvider = new UserSpaceContentProvider();
			userSpaceContentProvider.init(root);

			int requiredBanksCount = 0;
			if (root.getCartridgeMenuType() == CartridgeMenuType.EXTENDED) {
				int genresBankCount = 1; // One bank is reserved for the genres
				// information.
				int entriesPerBank = root.getBankSize()
						/ CartridgeMenu.MENU_ENTRY_LENGTH;
				requiredBanksCount = genresBankCount
						+ (root.getBankCount() + entriesPerBank - 1)
						/ entriesPerBank;
			}
			int startBankNumber = userSpaceContentProvider.getStartBankNumber()
					- requiredBanksCount;
			startBankNumber = Math.max(0, startBankNumber);
			init(Texts.ReservedContentProvider_MenuEntries, CartDef.BANKSIZE,
					startBankNumber, requiredBanksCount, true);
		}

		@Override
		public byte[] createContent(Workbook workbook,
				WorkbookExport workbookExport, MessageQueue messageQueue) {
			if (workbook == null) {
				throw new IllegalArgumentException(
						"Parameter 'workbook' must not be null.");
			}
			if (workbookExport == null) {
				throw new IllegalArgumentException(
						"Parameter ' workbookExport' must not be null.");
			}
			if (messageQueue == null) {
				throw new IllegalArgumentException(
						"Parameter 'messageQueue' must not be null.");
			}
			if (requiredBanksCount == 0) {
				return null;
			}
			int bankSize = workbook.getRoot().getBankSize();
			int requiredSize = requiredBanksCount * bankSize;
			byte[] result = new byte[requiredSize];
			CartridgeMenu.setMenuEntriesContent(workbook, result, messageQueue);
			return result;
		}
	}

	/**
	 * Content provider for dedicated menu startup code. This is required for
	 * flash targets which do not start at bank 0.
	 * 
	 */
	public static final class MenuStartupContentProvider extends
			ReservedContentProvider {

		private byte[] startupBankContent;

		@Override
		public void init(WorkbookRoot root) {
			if (root == null) {
				throw new IllegalArgumentException(
						"Parameter 'root' must not be null.");
			}
			int bankSize;
			int requiredBanksCount;
			CartridgeMenuType cartridgeMenuType = root.getCartridgeMenuType();
			bankSize = root.getBankSize();
			requiredBanksCount = 0;

			// If a menu is active and the cart is an Atarimax cart, it
			// requires the last bank to be the startup bank.
			if (cartridgeMenuType != CartridgeMenuType.NONE
					&& (root.getCartridgeType().equals(
							CartridgeType.CARTRIDGE_ATMAX_128) || root
							.getCartridgeType().equals(
									CartridgeType.CARTRIDGE_ATMAX_1024))) {
				requiredBanksCount = 1;

				// Sample code starting at $bfff0
				//
				// .proc menu_foreign_init
				// lda #0
				// sta $d500 ;Activate bank 0 of AtariMax carts
				// jmp ($bffe)
				// .endp
				//
				// org $bffa ;Cartridge control block
				// .byte a(menu_foreign_init),$00,$04,a(menu_foreign_init)

				startupBankContent = new byte[bankSize];
				int[] startupCode = new int[] { 0xa9, 0x00, 0x8d, 0x00, 0xd5,
						0x6c, 0xfe, 0xbf, 0xea, 0xea, 0xf0, 0xbf, 0x00, 0x04,
						0xf0, 0xbf };
				int offset = bankSize - startupCode.length;
				for (int i = 0; i < startupCode.length; i++) {
					startupBankContent[offset + i] = (byte) startupCode[i];
				}
			}

			init(Texts.ReservedContentProvider_MenuStartup, bankSize,
					root.getBankCount() - requiredBanksCount,
					requiredBanksCount, true);
		}

		@Override
		public byte[] createContent(Workbook workbook,
				WorkbookExport workbookExport, MessageQueue messageQueue) {
			return startupBankContent;
		}
	}

	public static final class UserSpaceContentProvider extends
			ReservedContentProvider {
		@Override
		public void init(WorkbookRoot root) {
			if (root == null) {
				throw new IllegalArgumentException(
						"Parameter 'workbook' must not be null.");
			}

			MenuStartupContentProvider menuStartupContentProvider = new MenuStartupContentProvider();
			menuStartupContentProvider.init(root);

			int requiredBanksCount = root.getUserSpaceBanksCount();
			int startBankNumber = menuStartupContentProvider
					.getStartBankNumber() - requiredBanksCount;
			if (startBankNumber < 0) {
				startBankNumber = 0;
			}
			init(Texts.ReservedContentProvider_UserSpace, 0, startBankNumber,
					requiredBanksCount, false);

		}

		@Override
		public byte[] createContent(Workbook workbook,
				WorkbookExport workbookExport, MessageQueue messageQueue) {
			return null;
		}
	}
}
