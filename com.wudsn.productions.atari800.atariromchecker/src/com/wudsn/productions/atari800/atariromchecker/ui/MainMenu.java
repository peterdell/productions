/**
 * Copyright (C) 2014 <a href="https://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of the Atari ROM Checker.
 * 
 * ROM Checker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * ROM Checker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Atari ROM Checker  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wudsn.productions.atari800.atariromchecker.ui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.wudsn.productions.atari800.atariromchecker.Actions;
import com.wudsn.productions.atari800.atariromchecker.Preferences;
import com.wudsn.productions.atari800.atariromchecker.AtariROMChecker.Commands;
import com.wudsn.tools.base.gui.ElementFactory;

/**
 * Main menu component. See
 * https://msdn.microsoft.com/en-us/library/windows/desktop/dn742392.aspx for
 * Microsoft Standards on menus.
 * 
 * @author Peter Dell
 */
public final class MainMenu {
    public final JMenuBar menuBar;

    public final JMenuItem exitMenuItem;

    public final JMenuItem showEntryMenuItem;
    public final JMenuItem compareEntriesMenuItem;

    public MainMenu(final ActionListener actionListener, final Preferences preferences) {
	if (actionListener == null) {
	    throw new IllegalArgumentException("Parameter 'actionListener' must not be null.");
	}
	if (preferences == null) {
	    throw new IllegalArgumentException("Parameter 'preferences' must not be null.");
	}
	menuBar = new JMenuBar();

	// File menu
	JMenu menu = ElementFactory.createMenu(com.wudsn.tools.base.Actions.MainMenu_File);

	menuBar.add(menu);

	exitMenuItem = ElementFactory.createMenuItem(Actions.MainMenu_File_Exit, Commands.EXIT);
	exitMenuItem.addActionListener(actionListener);
	menu.add(exitMenuItem);

	// Edit menu
	menu = ElementFactory.createMenu(com.wudsn.tools.base.Actions.MainMenu_Edit);
	menuBar.add(menu);
	showEntryMenuItem = ElementFactory.createMenuItem(Actions.MainMenu_Edit_ShowEntry,
		Commands.SHOW_ENTRY);
	showEntryMenuItem.addActionListener(actionListener);
	menu.add(showEntryMenuItem);

	compareEntriesMenuItem = ElementFactory.createMenuItem(Actions.MainMenu_Edit_CompareEntries,
		Commands.COMPARE_ENTRIES);
	compareEntriesMenuItem.addActionListener(actionListener);
	menu.add(compareEntriesMenuItem);

	// Edit menu
	// Help menu
	JMenuItem helpMenuItem;
	menu = ElementFactory.createMenu(com.wudsn.tools.base.Actions.MainMenu_Help);
	menuBar.add(menu);

	helpMenuItem = ElementFactory.createMenuItem(com.wudsn.tools.base.Actions.MainMenu_Help_HelpContents,
		Commands.HELP_CONTENTS);
	helpMenuItem.addActionListener(actionListener);
	menu.add(helpMenuItem);

    }

}