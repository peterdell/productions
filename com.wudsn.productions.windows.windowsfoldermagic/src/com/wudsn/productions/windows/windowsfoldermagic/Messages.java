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
package com.wudsn.productions.windows.windowsfoldermagic;

import com.wudsn.tools.base.repository.Message;
import com.wudsn.tools.base.repository.NLS;

/**
 * Message repository.
 * 
 * @author Peter Dell
 */
public final class Messages extends NLS {

    public static Message I100;
    public static Message E101;
    public static Message E102;
    public static Message E103;
    static {
	initializeClass(Messages.class, null);
    }
}
