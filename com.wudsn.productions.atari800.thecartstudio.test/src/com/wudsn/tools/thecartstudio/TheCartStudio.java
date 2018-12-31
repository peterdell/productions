/*
 * Copyright (C) 2013 - 2014 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
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
package com.wudsn.tools.thecartstudio;

import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Java 1.5 test stub
 * 
 * @author Peter Dell
 */
public final class TheCartStudio {
    public static void main(String[] args) {
	String text = "TheCartStudio: " + (new Date().toString());
	JOptionPane.showMessageDialog(null, text);
	System.out.println(text);
	System.out.flush();
    }
}