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

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.wudsn.tools.base.gui.DynamicMenuItemList;
import com.wudsn.tools.base.gui.ElementFactory;
import com.wudsn.tools.thecartstudio.Actions;
import com.wudsn.tools.thecartstudio.TheCartStudio.Commands;
import com.wudsn.tools.thecartstudio.model.Preferences;

/**
 * Main menu component. See
 * https://msdn.microsoft.com/en-us/library/windows/desktop/dn742392.aspx for
 * Microsoft Standards on menus.
 * 
 * @author Peter Dell
 */
public final class MainMenu {
	public final JMenuBar menuBar;

	public final JMenuItem newMenuItem;
	public final JMenuItem openMenuItem;
	public final JMenuItem openFolderMenuItem;
	public final JMenuItem saveMenuItem;
	public final JMenuItem saveAsMenuItem;
	public final JMenuItem closeMenuItem;
	public final JMenu exportMenu;
	public final JMenuItem exportAsBinImageMenuItem;
	public final JMenuItem exportAsCarImageMenuItem;
	public final JMenuItem exportAsAtrImageMenuItem;
	public final JMenuItem exportAsAtrImagesMenuItem;
	public final DynamicMenuItemList recentWorkbooksMenuItemList;
	public final JMenuItem printMenuItem;
	public final JMenuItem exitMenuItem;

	public final JMenuItem addEntriesMenuItem;
	public final JMenuItem addUserSpaceEntryMenuItem;
	public final JMenuItem removeEntriesMenuItem;
	public final JMenuItem setGenreMenuItem;
	public final JMenuItem assignNewBanksMenuItem;
	public final JMenuItem workbookOptionsMenuItem;

	public final JMenuItem previewMenuItem;
	public final JMenuItem testMenuItem;
	public final JMenuItem optionsMenuItem;

	public MainMenu(final ActionListener actionListener,
			final Preferences preferences) {
		if (actionListener == null) {
			throw new IllegalArgumentException(
					"Parameter 'actionListener' must not be null.");
		}
		if (preferences == null) {
			throw new IllegalArgumentException(
					"Parameter 'preferences' must not be null.");
		}
		menuBar = new JMenuBar();

		// File menu
		JMenu menu = ElementFactory
				.createMenu(com.wudsn.tools.base.Actions.MainMenu_File);
		menu.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				List<String> filePathsList = preferences
						.getRecentWorkbookFilePathsList();
				List<JMenuItem> menuItems = new ArrayList<JMenuItem>();
				for (int i = 0; i < filePathsList.size(); i++) {
					String filePath = filePathsList.get(i);
					File file = new File(filePath);
					String fileName = file.getName();
					String folderPath = file.getParent();
					char number = (char) ('1' + i);
					String text = number + " " + fileName;
					JMenuItem menuItem = new JMenuItem(text, number);
					menuItem.setToolTipText(folderPath);
					menuItem.setActionCommand(Commands.OPEN_RECENT_FILE + ":"
							+ filePath);
					menuItem.addActionListener(actionListener);
					menuItems.add(menuItem);

				}
				recentWorkbooksMenuItemList.setMenuItems(menuItems,
						openMenuItem.isEnabled());
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		menuBar.add(menu);

		newMenuItem = ElementFactory.createMenuItem(Actions.MainMenu_File_New,
				Commands.NEW_FILE);
		newMenuItem.addActionListener(actionListener);
		menu.add(newMenuItem);

		openMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_Open, Commands.OPEN_FILE);
		openMenuItem.addActionListener(actionListener);
		menu.add(openMenuItem);

		openFolderMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_OpenFolder, Commands.OPEN_FOLDER);
		openFolderMenuItem.addActionListener(actionListener);
		menu.add(openFolderMenuItem);

		saveMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_Save, Commands.SAVE_FILE);
		saveMenuItem.addActionListener(actionListener);
		menu.add(saveMenuItem);

		saveAsMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_SaveAs, Commands.SAVE_FILE_AS);
		saveAsMenuItem.addActionListener(actionListener);
		menu.add(saveAsMenuItem);

		closeMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_Close, Commands.CLOSE_FILE);
		closeMenuItem.addActionListener(actionListener);
		menu.add(closeMenuItem);

		// Create "Export" menu and sub-menu items.
		menu.addSeparator();
		exportMenu = ElementFactory.createMenu(Actions.MainMenu_File_Export);

		exportAsBinImageMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_ExportToBinImage,
				Commands.EXPORT_TO_BIN_IMAGE);
		exportAsBinImageMenuItem.addActionListener(actionListener);
		exportAsCarImageMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_ExportToCarImage,
				Commands.EXPORT_TO_CAR_IMAGE);
		exportAsCarImageMenuItem.addActionListener(actionListener);
		exportAsAtrImageMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_ExportToAtrImage,
				Commands.EXPORT_TO_ATR_IMAGE);
		exportAsAtrImageMenuItem.addActionListener(actionListener);
		exportAsAtrImagesMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_ExportToAtrImages,
				Commands.EXPORT_TO_ATR_IMAGES);
		exportAsAtrImagesMenuItem.addActionListener(actionListener);
		exportMenu.add(exportAsBinImageMenuItem);
		exportMenu.add(exportAsCarImageMenuItem);
		exportMenu.add(exportAsAtrImageMenuItem);
		exportMenu.add(exportAsAtrImagesMenuItem);
		menu.add(exportMenu);

		// Create dynamic recent workbook menu items.
		recentWorkbooksMenuItemList = new DynamicMenuItemList(menu);

		menu.addSeparator();
		printMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_Print, Commands.PRINT);
		printMenuItem.addActionListener(actionListener);
		menu.add(printMenuItem);

		menu.addSeparator();
		exitMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_File_Exit, Commands.EXIT);
		exitMenuItem.addActionListener(actionListener);
		menu.add(exitMenuItem);

		// Edit menu
		menu = ElementFactory
				.createMenu(com.wudsn.tools.base.Actions.MainMenu_Edit);
		menuBar.add(menu);

		addEntriesMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Edit_AddEntries, Commands.ADD_ENTRIES);
		addEntriesMenuItem.addActionListener(actionListener);
		menu.add(addEntriesMenuItem);

		addUserSpaceEntryMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Edit_AddUserSpaceEntry,
				Commands.ADD_USER_SPACE_ENTRY);
		addUserSpaceEntryMenuItem.addActionListener(actionListener);
		menu.add(addUserSpaceEntryMenuItem);

		removeEntriesMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Edit_RemoveEntries, Commands.REMOVE_ENTRIES);
		removeEntriesMenuItem.addActionListener(actionListener);
		menu.add(removeEntriesMenuItem);

		setGenreMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Edit_SetGenre, Commands.SET_GENRE);
		setGenreMenuItem.addActionListener(actionListener);
		menu.add(setGenreMenuItem);

		assignNewBanksMenuItem = ElementFactory
				.createMenuItem(Actions.MainMenu_Edit_AssignNewBanks,
						Commands.ASSIGN_NEW_BANKS);
		assignNewBanksMenuItem.addActionListener(actionListener);
		menu.add(assignNewBanksMenuItem);

		menu.addSeparator();

		workbookOptionsMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Edit_WorkbookOptions,
				Commands.WORKBOOK_OPTIONS);
		workbookOptionsMenuItem.addActionListener(actionListener);
		menu.add(workbookOptionsMenuItem);

		// Tools menu
		menu = ElementFactory
				.createMenu(com.wudsn.tools.base.Actions.MainMenu_Tools);
		menuBar.add(menu);
		previewMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Tools_Preview, Commands.PREVIEW);
		previewMenuItem.addActionListener(actionListener);
		menu.add(previewMenuItem);

		testMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Tools_Test, Commands.TEST);
		testMenuItem.addActionListener(actionListener);
		// menu.add(testMenuItem); Only for testing new functions during
		// development

		optionsMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Tools_Options, Commands.OPTIONS);
		optionsMenuItem.addActionListener(actionListener);
		menu.add(optionsMenuItem);

		menu.add(optionsMenuItem);
		// Help menu
		JMenuItem helpMenuItem;
		menu = ElementFactory
				.createMenu(com.wudsn.tools.base.Actions.MainMenu_Help);
		menuBar.add(menu);

		helpMenuItem = ElementFactory.createMenuItem(
				com.wudsn.tools.base.Actions.MainMenu_Help_HelpContents,
				Commands.HELP_CONTENTS);
		helpMenuItem.addActionListener(actionListener);
		menu.add(helpMenuItem);

		helpMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Help_ContentTypes,
				Commands.HELP_FILE_CONTENT_TYPES);
		helpMenuItem.addActionListener(actionListener);
		menu.add(helpMenuItem);

		helpMenuItem = ElementFactory.createMenuItem(
				Actions.MainMenu_Help_About, Commands.HELP_ABOUT);
		helpMenuItem.addActionListener(actionListener);
		menu.add(helpMenuItem);
	}

}