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

import com.wudsn.tools.base.repository.NLS;

/**
 * Text repository.
 * 
 * @author Peter Dell
 */
public final class Texts extends NLS {

	// Model texts:
	public static String GenreName_None;

	public static String ReservedContentProvider_Menu;
	public static String ReservedContentProvider_MenuEntries;
	public static String ReservedContentProvider_MenuStartup;
	public static String ReservedContentProvider_CheckSums;
	public static String ReservedContentProvider_UserSpace;

	// UI Texts: Title
	public static String MainWindow_Title;
	public static String MainWindow_Title_Unnamed;
	public static String MainWindow_Title_New;
	public static String MainWindow_Title_Persistent;

	// UI Texts: Dialogs
	public static String OptionsDialog_Title;
	public static String OptionsEmulatorExecutablePathDialog_Title;

	public static String WorkbookOptionsDialog_Title;
	public static String WorkbookEntry_UserSpaceEntryTitle;
	public static String WorkbookEntry_FileNameUndefined;
	public static String WorkbookEntry_StartBankUndefined;

	public static String WorkbookFileChooserDialog_Open_Title;
	public static String WorkbookFileChooserDialog_SaveAs_Title;
	public static String WorkbookFileChooserDialog_FilterDescription;

	public static String EntriesFileChooserDialog_AddEntries_Title;

	public static String ExportFormat_BIN_IMAGE_FileFilterDescription;
	public static String ExportFormat_CAR_IMAGE_FileFilterDescription;
	public static String ExportFormat_ATR_IMAGE_FileFilterDescription;
	public static String ExportFormat_ATR_IMAGES_FileFilterDescription;

	// UI Texts: Dialogs
	public static String ContentTypesDialog_Title;
	public static String AboutDialog_URL;
	public static String AboutDialog_Content;

	static {
		initializeClass(Texts.class, null);
	}
}
