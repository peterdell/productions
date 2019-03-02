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

package com.wudsn.tools.thecartstudio;

import java.awt.Color;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.MemorySize;
import com.wudsn.tools.base.repository.DataType;
import com.wudsn.tools.base.repository.NLS;
import com.wudsn.tools.thecartstudio.model.CartridgeMenuType;
import com.wudsn.tools.thecartstudio.model.ContentType;
import com.wudsn.tools.thecartstudio.model.DisplayMode;
import com.wudsn.tools.thecartstudio.model.FileHeaderType;
import com.wudsn.tools.thecartstudio.model.FlashTargetType;
import com.wudsn.tools.thecartstudio.model.Language;
import com.wudsn.tools.thecartstudio.model.WorkbookEntryType;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot;

/**
 * Text repository.
 * 
 * @author Peter Dell
 */
public final class DataTypes extends NLS {

	public static DataType ContentType_Id = new DataType(String.class);
	public static DataType ContentType_Text = new DataType(String.class);
	public static DataType ContentType_CartridgeTypeNumericId = new DataType(
			Integer.class);
	public static DataType ContentType_Size = new DataType(MemorySize.class);
	public static DataType ContentType_TheCartMode = new DataType(Integer.class);

	public static DataType Workbook_FilePath = new DataType(String.class);
	public static DataType Workbook_FolderPath = new DataType(String.class);

	public static DataType WorkbookRoot_Title = new DataType(ASCIIString.class,
			40, WorkbookRoot.TITLE_CHARACTERS);
	public static DataType WorkbookRoot_FlashTargetType = new DataType(
			FlashTargetType.class);
	public static DataType WorkbookRoot_BankCount = new DataType(Integer.class);
	public static DataType WorkbookRoot_BankSize = new DataType(
			MemorySize.class);
	public static DataType WorkbookRoot_CartridgeType = new DataType(
			CartridgeType.class);
	public static DataType WorkbookRoot_CartridgeMenuType = new DataType(
			CartridgeMenuType.class);
	public static DataType WorkbookRoot_UserSpaceSize = new DataType(
			MemorySize.class);
	public static DataType WorkbookRoot_GenreNames = new DataType(
			ASCIIString.class);

	public static DataType WorkbookGenre_Name = new DataType(ASCIIString.class,
			20, WorkbookRoot.TITLE_CHARACTERS);

	public static DataType WorkbookEntry_Id = new DataType(Integer.class);
	public static DataType WorkbookEntry_Type = new DataType(
			WorkbookEntryType.class);
	public static DataType WorkbookEntry_Title = new DataType(
			ASCIIString.class, 40, WorkbookRoot.TITLE_CHARACTERS);
	public static DataType WorkbookEntry_GenreName = new DataType(
			ASCIIString.class);
	public static DataType WorkbookEntry_FavoriteIndicator = new DataType(
			Boolean.class);
	public static DataType WorkbookEntry_FilePath = new DataType(String.class);
	public static DataType WorkbookEntry_FileName = new DataType(
			ASCIIString.class,
			256,
			new ASCIIString(
					"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&'()+,-.;=@[]^_ "));
	public static DataType WorkbookEntry_FileSize = new DataType(
			MemorySize.class);
	public static DataType WorkbookEntry_FileHeaderType = new DataType(
			FileHeaderType.class);
	public static DataType WorkbookEntry_ContentCRC32 = new DataType(
			Integer.class);
	public static DataType WorkbookEntry_ContentSize = new DataType(
			MemorySize.class);
	public static DataType WorkbookEntry_ContentType = new DataType(
			ContentType.class);
	public static DataType WorkbookEntry_DisplayMode = new DataType(
			DisplayMode.class);
	public static DataType WorkbookEntry_Parameters = new DataType(String.class);
	public static DataType WorkbookEntry_StartBankFixedIndicator = new DataType(
			Boolean.class);
	public static DataType WorkbookEntry_StartBankNumber = new DataType(
			Integer.class);
	public static DataType WorkbookEntry_RequiredBanksCount = new DataType(
			Integer.class);
	public static DataType WorkbookEntry_BanksAssignedIndicator = new DataType(
			Boolean.class);

	public static DataType WorkbookBank_Number = new DataType(Integer.class);

	public static DataType Preferences_Language = new DataType(Language.class);
	public static DataType Preferences_UpdateCheckIndicator = new DataType(
			Boolean.class);
	public static DataType Preferences_EmulatorExecutablePath = new DataType(
			String.class);
	public static DataType Preferences_FreeBankColor = new DataType(Color.class);
	public static DataType Preferences_ReservedBankColor = new DataType(
			Color.class);
	public static DataType Preferences_ReservedUserSpaceBankColor = new DataType(
			Color.class);
	public static DataType Preferences_UsedOddBankColor = new DataType(
			Color.class);
	public static DataType Preferences_UsedEvenBankColor = new DataType(
			Color.class);
	public static DataType Preferences_ConflictBankColor = new DataType(
			Color.class);

	static {
		initializeClass(DataTypes.class, null);
	}
}
