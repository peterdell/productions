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

package com.wudsn.tools.thecartstudio.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.base.repository.ValueSet;
import com.wudsn.tools.thecartstudio.DataTypes;
import com.wudsn.tools.thecartstudio.ValueSets;
import com.wudsn.tools.thecartstudio.model.atrfile.AtrFileMenu;
import com.wudsn.tools.thecartstudio.model.maxflash.MaxflashMenu;
import com.wudsn.tools.thecartstudio.model.megacart.MegacartMenu;

/**
 * Value set representing all known content types, including all cartridge
 * types.
 */
public final class ContentType extends ValueSet {

    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute TEXT = new Attribute("text",
		DataTypes.ContentType_Text);
	public static final Attribute ID = new Attribute("id",
		DataTypes.ContentType_Id);
	public static final Attribute SIZE = new Attribute("size",
		DataTypes.ContentType_Size);
	public static final Attribute CARTRIDGE_TYPE_NUMERIC_ID = new Attribute(
		"cartridgeTypeNumericId",
		DataTypes.ContentType_CartridgeTypeNumericId);
	public static final Attribute THE_CART_MODE = new Attribute(
		"theCartMode", DataTypes.ContentType_TheCartMode);
	public static final Attribute BANK_SIZE = new Attribute("bankSize",
		com.wudsn.tools.base.atari.DataTypes.CartridgeType_BankSize);
	public static final Attribute INITIAL_BANK_NUMBER = new Attribute(
		"initialBankNumber",
		com.wudsn.tools.base.atari.DataTypes.CartridgeType_InitialBankNumber);

    }

    public static final ContentType UNKNOWN;

    public static final ContentType CARTRIDGE_STD_8;
    public static final ContentType CARTRIDGE_STD_16;
    public static final ContentType CARTRIDGE_OSS_034M_16;
    public static final ContentType CARTRIDGE_5200_32;
    public static final ContentType CARTRIDGE_DB_32;
    public static final ContentType CARTRIDGE_5200_EE_16;
    public static final ContentType CARTRIDGE_5200_40;
    public static final ContentType CARTRIDGE_WILL_64;
    public static final ContentType CARTRIDGE_EXP_64;
    public static final ContentType CARTRIDGE_DIAMOND_64;
    public static final ContentType CARTRIDGE_SDX_64;
    public static final ContentType CARTRIDGE_XEGS_32;
    public static final ContentType CARTRIDGE_XEGS_64;
    public static final ContentType CARTRIDGE_XEGS_128;
    public static final ContentType CARTRIDGE_OSS_M091_16;
    public static final ContentType CARTRIDGE_5200_NS_16;
    public static final ContentType CARTRIDGE_ATRAX_DEC_128;
    public static final ContentType CARTRIDGE_BBSB_40;
    public static final ContentType CARTRIDGE_5200_8;
    public static final ContentType CARTRIDGE_5200_4;
    public static final ContentType CARTRIDGE_RIGHT_8;
    public static final ContentType CARTRIDGE_WILL_32;
    public static final ContentType CARTRIDGE_XEGS_256;
    public static final ContentType CARTRIDGE_XEGS_512;
    public static final ContentType CARTRIDGE_XEGS_1024;
    public static final ContentType CARTRIDGE_MEGA_16;
    public static final ContentType CARTRIDGE_MEGA_32;
    public static final ContentType CARTRIDGE_MEGA_64;
    public static final ContentType CARTRIDGE_MEGA_128;
    public static final ContentType CARTRIDGE_MEGA_256;
    public static final ContentType CARTRIDGE_MEGA_512;
    public static final ContentType CARTRIDGE_MEGA_1024;
    public static final ContentType CARTRIDGE_SWXEGS_32;
    public static final ContentType CARTRIDGE_SWXEGS_64;
    public static final ContentType CARTRIDGE_SWXEGS_128;
    public static final ContentType CARTRIDGE_SWXEGS_256;
    public static final ContentType CARTRIDGE_SWXEGS_512;
    public static final ContentType CARTRIDGE_SWXEGS_1024;
    public static final ContentType CARTRIDGE_PHOENIX_8;
    public static final ContentType CARTRIDGE_BLIZZARD_16;
    public static final ContentType CARTRIDGE_ATMAX_128;
    public static final ContentType CARTRIDGE_ATMAX_1024;
    public static final ContentType CARTRIDGE_SDX_128;
    public static final ContentType CARTRIDGE_OSS_8;
    public static final ContentType CARTRIDGE_OSS_043M_16;
    public static final ContentType CARTRIDGE_BLIZZARD_4;
    public static final ContentType CARTRIDGE_AST_32;
    public static final ContentType CARTRIDGE_ATRAX_SDX_64;
    public static final ContentType CARTRIDGE_ATRAX_SDX_128;
    public static final ContentType CARTRIDGE_TURBOSOFT_64;
    public static final ContentType CARTRIDGE_TURBOSOFT_128;
    public static final ContentType CARTRIDGE_ULTRACART_32;
    public static final ContentType CARTRIDGE_LOW_BANK_8;
    public static final ContentType CARTRIDGE_SIC_128;
    public static final ContentType CARTRIDGE_SIC_256;
    public static final ContentType CARTRIDGE_SIC_512;
    public static final ContentType CARTRIDGE_STD_2;
    public static final ContentType CARTRIDGE_STD_4;
    public static final ContentType CARTRIDGE_RIGHT_4;
    public static final ContentType CARTRIDGE_BLIZZARD_32;

    public static final ContentType CARTRIDGE_MEGAMAX_2048;
    public static final ContentType CARTRIDGE_THECART_8;
    public static final ContentType CARTRIDGE_THECART_128M;
    public static final ContentType CARTRIDGE_MEGA_4096;
    public static final ContentType CARTRIDGE_MEGA_2048;
    public static final ContentType CARTRIDGE_THECART_32M;
    public static final ContentType CARTRIDGE_THECART_64M;
    public static final ContentType CARTRIDGE_XEGS_8F_64;
    public static final ContentType CARTRIDGE_ATRAX_128;
    public static final ContentType CARTRIDGE_ADAWLIAH_32;
    public static final ContentType CARTRIDGE_ADAWLIAH_64;

    public static final ContentType FILE_ATR;
    public static final ContentType FILE_BINARY;
    public static final ContentType FILE_EXECUTABLE;
    public static final ContentType FILE_SAP;

    // Instances
    private static final Map<String, ContentType> values;

    // Instance attributes
    private CartridgeType cartridgeType;
    private int theCartMode;

    static {

	values = new TreeMap<String, ContentType>();

	UNKNOWN = add("UNKNOWN", CartridgeType.UNKNOWN,
		TheCartMode.TC_MODE_NOT_SUPPORTED);

	CARTRIDGE_STD_8 = add("CARTRIDGE_STD_8", CartridgeType.CARTRIDGE_STD_8,
		TheCartMode.TC_MODE_8K);
	CARTRIDGE_STD_16 = add("CARTRIDGE_STD_16",
		CartridgeType.CARTRIDGE_STD_16, TheCartMode.TC_MODE_16K);
	CARTRIDGE_OSS_034M_16 = add("CARTRIDGE_OSS_034M_16",
		CartridgeType.CARTRIDGE_OSS_034M_16,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_5200_32 = add("CARTRIDGE_5200_32",
		CartridgeType.CARTRIDGE_5200_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED_5200);
	CARTRIDGE_DB_32 = add("CARTRIDGE_DB_32", CartridgeType.CARTRIDGE_DB_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_5200_EE_16 = add("CARTRIDGE_5200_EE_16",
		CartridgeType.CARTRIDGE_5200_EE_16,
		TheCartMode.TC_MODE_NOT_SUPPORTED_5200);
	CARTRIDGE_5200_40 = add("CARTRIDGE_5200_40",
		CartridgeType.CARTRIDGE_5200_40,
		TheCartMode.TC_MODE_NOT_SUPPORTED_5200);
	CARTRIDGE_WILL_64 = add("CARTRIDGE_WILL_64",
		CartridgeType.CARTRIDGE_WILL_64, TheCartMode.TC_MODE_WILLIAMS64);
	CARTRIDGE_EXP_64 = add("CARTRIDGE_EXP_64",
		CartridgeType.CARTRIDGE_EXP_64, TheCartMode.TC_MODE_EXPRESS64);
	CARTRIDGE_DIAMOND_64 = add("CARTRIDGE_DIAMOND_64",
		CartridgeType.CARTRIDGE_DIAMOND_64,
		TheCartMode.TC_MODE_DIAMOND64);
	CARTRIDGE_SDX_64 = add("CARTRIDGE_SDX_64",
		CartridgeType.CARTRIDGE_SDX_64, TheCartMode.TC_MODE_SDX64);
	CARTRIDGE_XEGS_32 = add("CARTRIDGE_XEGS_32",
		CartridgeType.CARTRIDGE_XEGS_32, TheCartMode.TC_MODE_XEGS_32);
	CARTRIDGE_XEGS_64 = add("CARTRIDGE_XEGS_64",
		CartridgeType.CARTRIDGE_XEGS_64, TheCartMode.TC_MODE_XEGS_64);
	CARTRIDGE_XEGS_128 = add("CARTRIDGE_XEGS_128",
		CartridgeType.CARTRIDGE_XEGS_128, TheCartMode.TC_MODE_XEGS_128);
	CARTRIDGE_OSS_M091_16 = add("CARTRIDGE_OSS_M091_16",
		CartridgeType.CARTRIDGE_OSS_M091_16, TheCartMode.TC_MODE_OSS);
	CARTRIDGE_5200_NS_16 = add("CARTRIDGE_5200_NS_16",
		CartridgeType.CARTRIDGE_5200_NS_16,
		TheCartMode.TC_MODE_NOT_SUPPORTED_5200);
	CARTRIDGE_ATRAX_DEC_128 = add("CARTRIDGE_ATRAX_DEC_128",
		CartridgeType.CARTRIDGE_ATRAX_DEC_128,
		TheCartMode.TC_MODE_ATRAX128);
	CARTRIDGE_BBSB_40 = add("CARTRIDGE_BBSB_40",
		CartridgeType.CARTRIDGE_BBSB_40,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_5200_8 = add("CARTRIDGE_5200_8",
		CartridgeType.CARTRIDGE_5200_8,
		TheCartMode.TC_MODE_NOT_SUPPORTED_5200);
	CARTRIDGE_5200_4 = add("CARTRIDGE_5200_4",
		CartridgeType.CARTRIDGE_5200_4,
		TheCartMode.TC_MODE_NOT_SUPPORTED_5200);
	CARTRIDGE_RIGHT_8 = add("CARTRIDGE_RIGHT_8",
		CartridgeType.CARTRIDGE_RIGHT_8, TheCartMode.TC_MODE_8K_RIGHT);
	CARTRIDGE_WILL_32 = add("CARTRIDGE_WILL_32",
		CartridgeType.CARTRIDGE_WILL_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_XEGS_256 = add("CARTRIDGE_XEGS_256",
		CartridgeType.CARTRIDGE_XEGS_256, TheCartMode.TC_MODE_XEGS_256);
	CARTRIDGE_XEGS_512 = add("CARTRIDGE_XEGS_512",
		CartridgeType.CARTRIDGE_XEGS_512, TheCartMode.TC_MODE_XEGS_512);
	CARTRIDGE_XEGS_1024 = add("CARTRIDGE_XEGS_1024",
		CartridgeType.CARTRIDGE_XEGS_1024,
		TheCartMode.TC_MODE_XEGS_1024);
	CARTRIDGE_MEGA_16 = add("CARTRIDGE_MEGA_16",
		CartridgeType.CARTRIDGE_MEGA_16, TheCartMode.TC_MODE_MEGA_16);
	CARTRIDGE_MEGA_32 = add("CARTRIDGE_MEGA_32",
		CartridgeType.CARTRIDGE_MEGA_32, TheCartMode.TC_MODE_MEGA_32);
	CARTRIDGE_MEGA_64 = add("CARTRIDGE_MEGA_64",
		CartridgeType.CARTRIDGE_MEGA_64, TheCartMode.TC_MODE_MEGA_64);
	CARTRIDGE_MEGA_128 = add("CARTRIDGE_MEGA_128",
		CartridgeType.CARTRIDGE_MEGA_128, TheCartMode.TC_MODE_MEGA_128);
	CARTRIDGE_MEGA_256 = add("CARTRIDGE_MEGA_256",
		CartridgeType.CARTRIDGE_MEGA_256, TheCartMode.TC_MODE_MEGA_256);
	CARTRIDGE_MEGA_512 = add("CARTRIDGE_MEGA_512",
		CartridgeType.CARTRIDGE_MEGA_512, TheCartMode.TC_MODE_MEGA_512);
	CARTRIDGE_MEGA_1024 = add("CARTRIDGE_MEGA_1024",
		CartridgeType.CARTRIDGE_MEGA_1024,
		TheCartMode.TC_MODE_MEGA_1024);

	CARTRIDGE_SWXEGS_32 = add("CARTRIDGE_SWXEGS_32",
		CartridgeType.CARTRIDGE_SWXEGS_32, TheCartMode.TC_MODE_SXEGS_32);
	CARTRIDGE_SWXEGS_64 = add("CARTRIDGE_SWXEGS_64",
		CartridgeType.CARTRIDGE_SWXEGS_64, TheCartMode.TC_MODE_SXEGS_64);
	CARTRIDGE_SWXEGS_128 = add("CARTRIDGE_SWXEGS_128",
		CartridgeType.CARTRIDGE_SWXEGS_128,
		TheCartMode.TC_MODE_SXEGS_128);
	CARTRIDGE_SWXEGS_256 = add("CARTRIDGE_SWXEGS_256",
		CartridgeType.CARTRIDGE_SWXEGS_256,
		TheCartMode.TC_MODE_SXEGS_256);
	CARTRIDGE_SWXEGS_512 = add("CARTRIDGE_SWXEGS_512",
		CartridgeType.CARTRIDGE_SWXEGS_512,
		TheCartMode.TC_MODE_SXEGS_512);
	CARTRIDGE_SWXEGS_1024 = add("CARTRIDGE_SWXEGS_1024",
		CartridgeType.CARTRIDGE_SWXEGS_1024,
		TheCartMode.TC_MODE_SXEGS_1024);
	CARTRIDGE_PHOENIX_8 = add("CARTRIDGE_PHOENIX_8",
		CartridgeType.CARTRIDGE_PHOENIX_8,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_BLIZZARD_16 = add("CARTRIDGE_BLIZZARD_16",
		CartridgeType.CARTRIDGE_BLIZZARD_16,
		TheCartMode.TC_MODE_BLIZZARD);
	CARTRIDGE_ATMAX_128 = add("CARTRIDGE_ATMAX_128",
		CartridgeType.CARTRIDGE_ATMAX_128,
		TheCartMode.TC_MODE_ATARIMAX1);
	CARTRIDGE_ATMAX_1024 = add("CARTRIDGE_ATMAX_1024",
		CartridgeType.CARTRIDGE_ATMAX_1024,
		TheCartMode.TC_MODE_ATARIMAX8);
	CARTRIDGE_SDX_128 = add("CARTRIDGE_SDX_128",
		CartridgeType.CARTRIDGE_SDX_128,
		TheCartMode.TC_MODE_NOT_SUPPORTED);

	CARTRIDGE_OSS_8 = add("CARTRIDGE_OSS_8", CartridgeType.CARTRIDGE_OSS_8,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_OSS_043M_16 = add("CARTRIDGE_OSS_043M_16",
		CartridgeType.CARTRIDGE_OSS_043M_16,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_BLIZZARD_4 = add("CARTRIDGE_BLIZZARD_4",
		CartridgeType.CARTRIDGE_BLIZZARD_4,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_AST_32 = add("CARTRIDGE_AST_32",
		CartridgeType.CARTRIDGE_AST_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_ATRAX_SDX_64 = add("CARTRIDGE_ATRAX_SDX_64",
		CartridgeType.CARTRIDGE_ATRAX_SDX_64,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_ATRAX_SDX_128 = add("CARTRIDGE_ATRAX_SDX_128",
		CartridgeType.CARTRIDGE_ATRAX_SDX_128,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_TURBOSOFT_64 = add("CARTRIDGE_TURBOSOFT_64",
		CartridgeType.CARTRIDGE_TURBOSOFT_64,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_TURBOSOFT_128 = add("CARTRIDGE_TURBOSOFT_128",
		CartridgeType.CARTRIDGE_TURBOSOFT_128,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_ULTRACART_32 = add("CARTRIDGE_ULTRACART_32",
		CartridgeType.CARTRIDGE_ULTRACART_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_LOW_BANK_8 = add("CARTRIDGE_LOW_BANK_8",
		CartridgeType.CARTRIDGE_LOW_BANK_8,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_SIC_128 = add("CARTRIDGE_SIC_128",
		CartridgeType.CARTRIDGE_SIC_128,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_SIC_256 = add("CARTRIDGE_SIC_256",
		CartridgeType.CARTRIDGE_SIC_256,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_SIC_512 = add("CARTRIDGE_SIC_512",
		CartridgeType.CARTRIDGE_SIC_512, TheCartMode.TC_MODE_SIC);
	CARTRIDGE_STD_2 = add("CARTRIDGE_STD_2", CartridgeType.CARTRIDGE_STD_2,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_STD_4 = add("CARTRIDGE_STD_4", CartridgeType.CARTRIDGE_STD_4,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_RIGHT_4 = add("CARTRIDGE_RIGHT_4",
		CartridgeType.CARTRIDGE_RIGHT_4,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_BLIZZARD_32 = add("CARTRIDGE_BLIZZARD_32",
		CartridgeType.CARTRIDGE_BLIZZARD_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED);

	CARTRIDGE_MEGAMAX_2048 = add("CARTRIDGE_MEGAMAX_2048",
		CartridgeType.CARTRIDGE_MEGAMAX_2048,
		TheCartMode.TC_MODE_MEGAMAX16);
	// Native The!Cart (8k mode)
	CARTRIDGE_THECART_8 = add("CARTRIDGE_THECART_8",
		CartridgeType.CARTRIDGE_STD_8, TheCartMode.TC_MODE_8K_THECART);
	CARTRIDGE_THECART_128M = add("CARTRIDGE_THECART_128M",
		CartridgeType.CARTRIDGE_THECART_128M,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_MEGA_4096 = add("CARTRIDGE_MEGA_4096",
		CartridgeType.CARTRIDGE_MEGA_4096,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_MEGA_2048 = add("CARTRIDGE_MEGA_2048",
		CartridgeType.CARTRIDGE_MEGA_2048,
		TheCartMode.TC_MODE_MEGA_2048);
	CARTRIDGE_THECART_32M = add("CARTRIDGE_THECART_32M",
		CartridgeType.CARTRIDGE_THECART_32M,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_THECART_64M = add("CARTRIDGE_THECART_64M",
		CartridgeType.CARTRIDGE_THECART_64M,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_XEGS_8F_64 = add("CARTRIDGE_XEGS_8F_64",
		CartridgeType.CARTRIDGE_XEGS_8F_64,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_ATRAX_128 = add("CARTRIDGE_ATRAX_128",
		CartridgeType.CARTRIDGE_ATRAX_128,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_ADAWLIAH_32 = add("CARTRIDGE_ADAWLIAH_32",
		CartridgeType.CARTRIDGE_ADAWLIAH_32,
		TheCartMode.TC_MODE_NOT_SUPPORTED);
	CARTRIDGE_ADAWLIAH_64 = add("CARTRIDGE_ADAWLIAH_64",
		CartridgeType.CARTRIDGE_ADAWLIAH_64,
		TheCartMode.TC_MODE_NOT_SUPPORTED);

	FILE_ATR = add("FILE_ATR", CartridgeType.UNKNOWN,
		TheCartMode.TC_MODE_ATR_FILE);
	FILE_BINARY = add("FILE_BINARY", CartridgeType.UNKNOWN,
		TheCartMode.TC_MODE_BINARY_FILE);
	FILE_EXECUTABLE = add("FILE_EXECUTABLE", CartridgeType.UNKNOWN,
		TheCartMode.TC_MODE_EXECUTABLE_FILE);
	FILE_SAP = add("FILE_SAP", CartridgeType.UNKNOWN,
		TheCartMode.TC_MODE_SAP_FILE);

	initializeClass(ContentType.class, ValueSets.class);
    }

    private ContentType(String id, CartridgeType cartridgeType, int theCartMode) {
	super(id, 0);
	this.cartridgeType = cartridgeType;
	this.theCartMode = theCartMode;
    }

    private static ContentType add(String id, CartridgeType cartridgeType,
	    int theCartMode) {
	if (id == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'id' must not be null.");
	}

	if (cartridgeType == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'cartridgeType' must not be null.");
	}
	ContentType result = new ContentType(id, cartridgeType, theCartMode);
	values.put(id, result);
	return result;
    }

    /**
     * Gets the unmodifiable list of all values
     * 
     * @return The unmodifiable list of all values, not <code>null</code>.
     */
    public static List<ContentType> getValues() {
	return Collections.unmodifiableList(new ArrayList<ContentType>(values
		.values()));
    }

    /**
     * Gets a value set instance by its id.
     * 
     * @param id
     *            The id, not <code>null</code>.
     * @return The value set instance or <code>null</code>.
     */
    public static ContentType getInstance(String id) {
	if (id == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'id' must not be null.");
	}
	return values.get(id);
    }

    /**
     * Gets a value set instance by its cartridge type.
     * 
     * @param cartridgeTypeNumericId
     *            The cartridge type numeric id.
     * @return The value set instance or <code>null</code>.
     */
    public static ContentType getInstanceByCartridgeType(
	    int cartridgeTypeNumericId) {
	ContentType result = UNKNOWN;
	if (cartridgeTypeNumericId != CartridgeType.UNKNOWN.getNumericId()) {
	    for (ContentType contentType : values.values()) {
		if (contentType.getCartridgeType().getNumericId() == cartridgeTypeNumericId) {
		    return contentType;
		}
	    }
	}
	return result;
    }

    /**
     * Content types which are cartridge emulation types require a special
     * alignment of the start bank for the entry. This method if the content
     * type requires an alignment to a bank that has a number which is the next
     * power of two greater or equal to the entry size.
     * 
     * @return <code>true</code> if the content type requires an alignment based
     *         on its size.
     */
    public boolean isAligmentRequired() {
	if (this == UNKNOWN || this == FILE_ATR || this == FILE_BINARY
		|| this == FILE_EXECUTABLE || this == FILE_SAP) {
	    return false;
	}
	return true;

    }

    /**
     * Gets the cartridge type.
     * 
     * @return The cartridge type, not <code>null</code>.
     */
    public CartridgeType getCartridgeType() {
	return cartridgeType;
    }

    /**
     * Gets the The!Cart mode for emulating this content type, see
     * {@link TheCartMode}.
     * 
     * @return The The!Cart mode for emulating this content type or
     *         {@link TheCartMode#TC_MODE_NOT_SUPPORTED} or
     *         {@link TheCartMode#TC_MODE_NOT_SUPPORTED_5200} if the content
     *         type cannot be emulated.
     */
    public int getTheCartMode() {
	return theCartMode;
    }

    /**
     * Determines the class to detect the resolution of the original menu into
     * multiple entries of the extended menu.
     * 
     * @param content
     *            The file content, not <code>null</code>.
     * @return The class or <code>null</code> if no detection is supported.
     */
    public ImportableMenu createImportableMenu(byte[] content) {
	if (this.equals(CARTRIDGE_ATMAX_128)
		|| this.equals(CARTRIDGE_ATMAX_1024)) {
	    return new MaxflashMenu(content);
	}
	if (this.equals(CARTRIDGE_MEGA_512) || this.equals(CARTRIDGE_MEGA_2048)) {
	    return new MegacartMenu(content);
	}
	if (this.equals(FILE_ATR)) {
	    return new AtrFileMenu(content);
	}
	return null;
    }

    /**
     * Gets the comma separate list of texts for cartridge types that support
     * importable menus.
     * 
     * @return The comma separated list, may be empty, not <code>null</code>.
     */
    public static String getImportableMenuList() {
	List<ContentType> list = new ArrayList<ContentType>();
	list.add(CARTRIDGE_ATMAX_128);
	list.add(CARTRIDGE_ATMAX_1024);
	list.add(CARTRIDGE_MEGA_512);
	list.add(CARTRIDGE_MEGA_2048);
	list.add(FILE_ATR);
	StringBuilder builder = new StringBuilder();
	for (ContentType contentType : list) {
	    if (builder.length() > 0) {
		builder.append(", ");
	    }
	    builder.append(contentType.getText());
	}
	return builder.toString();
    }
}
