/**
 * Copyright (C) 2015 - 2016 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
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

import com.wudsn.tools.base.repository.Action;
import com.wudsn.tools.base.repository.NLS;

/**
 * Action repository.
 * 
 * @author Peter Dell
 */
public final class Actions extends NLS {

    // Actions: Command line
    public static Action Load;
    public static Action Save;
    public static Action ConvertToCAR;
    public static Action ConvertToROM;
    
    static {
	initializeClass(Actions.class, null);
    }
}
