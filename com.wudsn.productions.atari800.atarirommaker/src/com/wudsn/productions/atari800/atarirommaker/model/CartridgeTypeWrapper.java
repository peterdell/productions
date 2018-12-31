/**
 * Copyright (C) 2015 - 2016 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of the Atari ROM Maker.
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

package com.wudsn.productions.atari800.atarirommaker.model;

import com.wudsn.tools.base.atari.CartridgeType;

/**
 * Wrapper to put cartridge types into locations where a "toString()" is
 * required.
 * 
 * @author Peter Dell
 */
public class CartridgeTypeWrapper {
    private CartridgeType cartridgeType;

    public CartridgeTypeWrapper(CartridgeType cartridgeType) {
	if (cartridgeType == null) {
	    throw new IllegalArgumentException("Parameter 'cartridgeType' must not be null.");
	}
	this.cartridgeType = cartridgeType;
    }

    public CartridgeType GetCartridgeType() {
	return cartridgeType;
    }

    @Override
    public String toString() {
	return cartridgeType.getText()+ " - ("+cartridgeType.getNumericId()+")";
    }
}