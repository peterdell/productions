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

import java.awt.event.KeyEvent;

import com.wudsn.tools.base.gui.KeyStroke;
import com.wudsn.tools.base.repository.Action;
import com.wudsn.tools.base.repository.NLS;

/**
 * Action repository.
 * 
 * @author Peter Dell
 */
public final class Actions extends NLS {

	// Actions: Console
	public static Action Console_CreateSampleFiles;

	// Actions: Menu
	public static Action MainMenu_File_New = new Action(KeyEvent.VK_N,
			KeyStroke.M1);
	public static Action MainMenu_File_Open = new Action(KeyEvent.VK_O,
			KeyStroke.M1);
	public static Action MainMenu_File_OpenFolder;
	public static Action MainMenu_File_Save = new Action(KeyEvent.VK_S,
			KeyStroke.M1);
	public static Action MainMenu_File_SaveAs;
	public static Action MainMenu_File_Close = new Action(KeyEvent.VK_F4,
			KeyStroke.M1);
	public static Action MainMenu_File_Export;
	public static Action MainMenu_File_ExportToBinImage = new Action(
			KeyEvent.VK_E, KeyStroke.M2 | KeyStroke.M1);
	public static Action MainMenu_File_ExportToCarImage = new Action(
			KeyEvent.VK_C, KeyStroke.M2 | KeyStroke.M1);
	public static Action MainMenu_File_ExportToAtrImage = new Action(
			KeyEvent.VK_P, KeyStroke.M2 | KeyStroke.M1);
	public static Action MainMenu_File_ExportToAtrImages = new Action(
			KeyEvent.VK_P, KeyStroke.M2 | KeyStroke.M3);
	public static Action MainMenu_File_Print = new Action(KeyEvent.VK_P,
			KeyStroke.M1);
	public static Action MainMenu_File_Exit = new Action(KeyEvent.VK_F4,
			KeyStroke.M3);

	public static Action MainMenu_Edit_AddEntries = new Action(
			KeyEvent.VK_INSERT, 0);
	public static Action MainMenu_Edit_AddUserSpaceEntry = new Action(
			KeyEvent.VK_INSERT, KeyStroke.M2);
	public static Action MainMenu_Edit_RemoveEntries = new Action(
			KeyEvent.VK_DELETE, 0);
	public static Action MainMenu_Edit_SetGenre = new Action(KeyEvent.VK_G,
			KeyStroke.M2 | KeyStroke.M1);
	public static Action MainMenu_Edit_AssignNewBanks = new Action(
			KeyEvent.VK_R, KeyStroke.M2 | KeyStroke.M1);
	public static Action MainMenu_Edit_WorkbookOptions = new Action(
			KeyEvent.VK_ENTER, KeyStroke.M3);

	public static Action MainMenu_Tools_Preview = new Action(KeyEvent.VK_T,
			KeyStroke.M1);
	public static Action MainMenu_Tools_Test;
	public static Action MainMenu_Tools_Options;

	public static Action MainMenu_Help_ContentTypes;
	public static Action MainMenu_Help_About;

	// Actions: Tabs
	public static Action MainTabbedPane_BanksTab;
	public static Action MainTabbedPane_EntriesTab;

	// Actions: Preferences Dialog Button Bar
	public static Action Preferences_ButtonBar_Reset;

	// Actions: Workbook Entries Dialog Button Bar
	public static Action ButtonBar_Save;
	public static Action ButtonBar_DontSave;

	public static Action ButtonBar_Overwrite;
	public static Action ButtonBar_Rename;
	public static Action ButtonBar_Skip;

	static {
		initializeClass(Actions.class, null);
	}
}
