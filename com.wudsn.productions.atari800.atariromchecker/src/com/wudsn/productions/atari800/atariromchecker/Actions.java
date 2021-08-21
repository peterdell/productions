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
package com.wudsn.productions.atari800.atariromchecker;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.wudsn.tools.base.repository.Action;
import com.wudsn.tools.base.repository.NLS;

/**
 * Action repository.
 * 
 * @author Peter Dell
 */
public final class Actions extends NLS {

    // Actions: Menu
    public static Action MainMenu_File_Exit = new Action(KeyEvent.VK_F4,
	    ActionEvent.ALT_MASK);
    public static Action MainMenu_Edit_ShowEntry = new Action(KeyEvent.VK_SPACE, 0);
    public static Action MainMenu_Edit_CompareEntries = new Action(
	    KeyEvent.VK_ENTER, ActionEvent.ALT_MASK);

    static {
	initializeClass(Actions.class, null);
    }
}
