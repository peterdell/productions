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
package com.wudsn.productions.atari800.atariromchecker.model;

public final class CheckSum {

    private String name;
    private int offsetLowByte;
    private int offsetHighByte;
    private CheckSumEvaluator evaluator;

    public CheckSum(String name, int offsetLowByte, int offsetHighByte, CheckSumEvaluator evaluator) {
	if (name == null) {
	    throw new IllegalArgumentException("Parameter 'name' must not be null.");
	}
	if (evaluator == null) {
	    throw new IllegalArgumentException("Parameter 'evaluator' must not be null.");
	}
	this.name = name;
	this.offsetLowByte = offsetLowByte;
	this.offsetHighByte = offsetHighByte;
	this.evaluator=evaluator;
    }

    public String getName() {
	return name;
    }

    public int getOffsetLowByte() {
	return offsetLowByte;
    }

    public int getOffsetHighByte() {
	return offsetHighByte;
    }

    public int getCurrentValue(ByteArray content) {
	return content.getByte(getOffsetLowByte()) + (content.getByte(getOffsetHighByte()) << 8);
    }

    public int getEvaluatedValue(ByteArray content) {
	return evaluator.evaluate(content);
    }

    public void setEvaluatedValue(ByteArray content) {
	int value = getEvaluatedValue(content);
	content.setByte(getOffsetLowByte(), value & 0xff);
	content.setByte(getOffsetHighByte(), (value >>> 8));
    }
}