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

import java.io.File;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.repository.DataType;
import com.wudsn.tools.base.repository.NLS;

/**
 * Text repository.
 * 
 * @author Peter Dell
 */
public final class DataTypes extends NLS {

    public static DataType ROM_FilePath = new DataType(File.class);
    public static DataType ROM_CartridgeType = new DataType(CartridgeType.class);
    public static DataType ROM_Content = new DataType(Byte[].class);

    static {
	initializeClass(DataTypes.class, null);
    }
}
