/**
 * Copyright (C) 2015 - 2016 <a href="https://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of the Atari ROM Maker.
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
 * along with the Atari ROM Maker  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wudsn.productions.atari800.atarirommaker;

import com.wudsn.tools.base.repository.NLS;

/**
 * Text repository.
 * 
 * @author Peter Dell
 */
public final class Texts extends NLS {

    // UI Texts: Main Window
    public static String MainWindow_Title;

    public static String MainWindow_Text;
    public static String MainWindow_Link;
    public static String MainWindow_URL;
    
    public static String FolderDialog_Title;
    public static String FolderDialog_Action;
    
    public static String CartridgeTypeDialog_Title;
    public static String CartridgeTypeDialog_Action;

    static {
	initializeClass(Texts.class, null);
    }
}
