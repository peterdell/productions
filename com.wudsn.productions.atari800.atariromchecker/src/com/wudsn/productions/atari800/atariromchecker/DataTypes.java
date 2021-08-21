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

import com.wudsn.tools.base.common.MemorySize;
import com.wudsn.tools.base.repository.DataType;
import com.wudsn.tools.base.repository.NLS;

/**
 * Text repository.
 * 
 * @author Peter Dell
 */
public final class DataTypes extends NLS {

    public static DataType WorkbookEntry_Id = new DataType(Integer.class);
    public static DataType WorkbookEntry_FolderPath = new DataType(String.class);
    public static DataType WorkbookEntry_FileName = new DataType(String.class);
    public static DataType WorkbookEntry_FileSize = new DataType(
	    MemorySize.class);

    public static DataType WorkbookEntry_Size = new DataType(MemorySize.class);
    public static DataType WorkbookEntry_MD5 = new DataType(String.class);
    public static DataType WorkbookEntry_CRC32 = new DataType(String.class);
    public static DataType WorkbookEntry_CheckSums = new DataType(String.class);
    public static DataType WorkbookEntry_Message = new DataType(String.class);

    public static DataType ROMVersion_FileSize = new DataType(MemorySize.class);
    public static DataType ROMVersion_CRC32 = new DataType(String.class);
    public static DataType ROMVersion_MD5 = new DataType(String.class);
    public static DataType ROMVersion_Type = new DataType(String.class);
    public static DataType ROMVersion_Id = new DataType(String.class);
    public static DataType ROMVersion_Publisher = new DataType(String.class);
    public static DataType ROMVersion_Date = new DataType(String.class);
    public static DataType ROMVersion_Revision = new DataType(String.class);
    public static DataType ROMVersion_Norm = new DataType(String.class);
    public static DataType ROMVersion_Parts = new DataType(String.class);
    public static DataType ROMVersion_Comment = new DataType(String.class);

    public static DataType Comparison_EntriesCount = new DataType(Integer.class);
    public static DataType ComparisonEntry_Id = new DataType(Integer.class);
    public static DataType ComparisonEntry_Offset = new DataType(String.class);
    public static DataType ComparisonEntry_Address = new DataType(String.class);
    public static DataType ComparisonEntry_Value = new DataType(String.class);

    static {
	initializeClass(DataTypes.class, null);
    }
}
