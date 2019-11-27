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
 * but WITHOUT ANY WARRANTY// without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with The!Cart Studio. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wudsn.tools.thecartstudio.model;

/**
 * From /src/flash.inc" of the "The!Cart" software assembler source. The
 * following modes have no corresponding CART id:
 * <ul>
 * <li>TC_MODE_FLEXI (no alignment)</li>
 * <li>TC_MODE_MEGAMAX16 (might get one in future)</li>
 * <li>TC_MODE_MEGA_2048 (might get one in future)</li>
 * <li>TC_MODE_8K_RAM (makes no sense as CART id)</li>
 * <li>TC_MODE_PICODOS (makes no sense as CART id)</li>
 * <li>TC_MODE_ATR_FILE (no alignment)</li>
 * <li>TC_MODE_BINARY_FILE (no alignment)</li>
 * <li>TC_MODE_EXECUTABLE_FILE (no alignment)</li>
 * <li>TC_MODE_SAP_FILE (no alignment)</li>
 * </ul>
 * 
 * @author Peter Dell, Matthias Reichel
 */
public final class TheCartMode {

    // Symbolic modes
    public static final int TC_MODE_NOT_SUPPORTED = 0x00;
    public static final int TC_MODE_NOT_SUPPORTED_5200 = 0x00;

    // 8k modes ($A000-$BFFF)
    public static final int TC_MODE_8K = 0x01; // 8k banks at $A000
    // 8k using Atarimax 1MBit compatible banking
    public static final int TC_MODE_ATARIMAX1 = 0x02;
    // 8k using Atarimax 8MBit compatible banking
    public static final int TC_MODE_ATARIMAX8 = 0x03;
    // 16k OSS cart, M091 banking
    public static final int TC_MODE_OSS = 0x04;
    // SDX 64k cart, $D5Ex banking
    public static final int TC_MODE_SDX64 = 0x08;
    // Diamond GOS 64k cart, $D5Dx banking
    public static final int TC_MODE_DIAMOND64 = 0x09;
    // Express 64k cart, $D57x banking
    public static final int TC_MODE_EXPRESS64 = 0x0A;
    // Decoded Atrax 128k cart
    public static final int TC_MODE_ATRAX128 = 0x0C;
    // Williams 64k cart
    public static final int TC_MODE_WILLIAMS64 = 0x0D;

    // 8k with Turbo Freezer 2005 bank select (D540-7F,80/81/84/85)
    // TC_MODE_8K_OLD = 0x1F;

    // 16k modes ($8000-$BFFF)

    // Flexi mode
    public static final int TC_MODE_FLEXI = 0x20;

    // 16k banks at $8000-$BFFF
    public static final int TC_MODE_16K = 0x21;
    // MegaMax 16k mode (up to 2MB)
    public static final int TC_MODE_MEGAMAX16 = 0x22;
    // Blizzard 16k
    public static final int TC_MODE_BLIZZARD = 0x23;
    // Sic!Cart 512k
    public static final int TC_MODE_SIC = 0x24;

    // Switchable MegaCarts
    public static final int TC_MODE_MEGA_16 = 0x28;
    public static final int TC_MODE_MEGA_32 = 0x29;
    public static final int TC_MODE_MEGA_64 = 0x2A;
    public static final int TC_MODE_MEGA_128 = 0x2B;
    public static final int TC_MODE_MEGA_256 = 0x2C;
    public static final int TC_MODE_MEGA_512 = 0x2D;
    public static final int TC_MODE_MEGA_1024 = 0x2E;
    public static final int TC_MODE_MEGA_2048 = 0x2F;

    // Non-switchable XEGS carts
    public static final int TC_MODE_XEGS_32 = 0x30;
    public static final int TC_MODE_XEGS_64 = 0x31;
    public static final int TC_MODE_XEGS_128 = 0x32;
    public static final int TC_MODE_XEGS_256 = 0x33;
    public static final int TC_MODE_XEGS_512 = 0x34;
    public static final int TC_MODE_XEGS_1024 = 0x35;

    // Switchable XEGS carts
    public static final int TC_MODE_SXEGS_32 = 0x38;
    public static final int TC_MODE_SXEGS_64 = 0x39;
    public static final int TC_MODE_SXEGS_128 = 0x3A;
    public static final int TC_MODE_SXEGS_256 = 0x3B;
    public static final int TC_MODE_SXEGS_512 = 0x3C;
    public static final int TC_MODE_SXEGS_1024 = 0x3D;

    // Pseudo values: Handled by flexi mode
    // Behaves like ROM by default, but can be made write enabled
    public static final int TC_MODE_8K_THECART = 0xf0;
    // Standard Atari 800 right module
    public static final int TC_MODE_8K_RIGHT = 0xf1;
    // Not relevant externally.
    public static final int TC_MODE_FLASHER = 0xfd;
    public static final int TC_MODE_PICODOS = 0xfe;

    // Pseudo values: Handled by the studio
    public static final int TC_MODE_ATR_FILE = 0xe0;
    public static final int TC_MODE_BINARY_FILE = 0xe1;
    public static final int TC_MODE_EXECUTABLE_FILE = 0xe2;
    public static final int TC_MODE_SAP_FILE = 0xe3;

}