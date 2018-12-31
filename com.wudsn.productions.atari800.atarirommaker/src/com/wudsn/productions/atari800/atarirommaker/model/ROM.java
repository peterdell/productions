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

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;

import java.io.File;
import java.util.List;

import com.wudsn.productions.atari800.atarirommaker.DataTypes;
import com.wudsn.productions.atari800.atarirommaker.Messages;
import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.repository.Attribute;

/**
 * ROM model
 * 
 * @author Peter Dell
 * 
 */
public final class ROM {

    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute FILE_PATH = new Attribute("filePath", DataTypes.ROM_FilePath);
	public static final Attribute CARTRDIGE_TYPE = new Attribute("cartridgeType", DataTypes.ROM_CartridgeType);
	public static final Attribute CONTENT = new Attribute("content", DataTypes.ROM_Content);

    }

    private byte[] content;

    public ROM() {
	content = new byte[0];
    }

    public boolean load(File file, MessageQueue messageQueue) {
	if (file == null) {
	    throw new IllegalArgumentException("Parameter 'file' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}
	try {
	    content = FileUtility.readBytes(file, 128 * ByteArrayUtility.MB, true);
	} catch (CoreException ex) {
	    messageQueue.sendMessage(ex.createMessageQueueEntry(this, Attributes.FILE_PATH));
	    return false;
	}
	// INFO: Input file '{0}' loaded with {1} bytes.
	messageQueue.sendMessage(this, null, Messages.I100, file.getAbsolutePath(),
		TextUtility.formatAsMemorySize(file.length()));
	return true;
    }

    public boolean save(File file, MessageQueue messageQueue) {
	if (file == null) {
	    throw new IllegalArgumentException("Parameter 'file' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}
	try {
	    FileUtility.writeBytes(file, content);
	} catch (CoreException ex) {
	    messageQueue.sendMessage(ex.createMessageQueueEntry(this, Attributes.FILE_PATH));
	    return false;
	}
	// INFO: Output file '{0}' saved with {1} bytes.
	messageQueue.sendMessage(this, null, Messages.I101, file.getAbsolutePath(),
		TextUtility.formatAsMemorySize(file.length()));
	return true;
    }

    public boolean convertToCAR(String cartridgeTypeId, MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}
	CartridgeType cartridgeType = null;
	List<CartridgeType> cartridgeTypes = CartridgeType.getValues();
	for (int i = 0; i < cartridgeTypes.size() && cartridgeType == null; i++) {
	    if (Integer.toString(cartridgeTypes.get(i).getNumericId()).equals(cartridgeTypeId)) {
		cartridgeType = cartridgeTypes.get(i);
	    }
	}
	if (cartridgeType == null) {
	    // ERROR: Unknown cartridge type {0}.
	    messageQueue.sendMessage(this, Attributes.CARTRDIGE_TYPE, Messages.E103, cartridgeTypeId);
	    return false;
	}
	return convertToCAR(cartridgeType, messageQueue);
    }

    public boolean isEmpty() {
	return content.length == 0;
    }

    public boolean isValidCAR() {
	int cartridgeTypeNumericID = CartridgeFileUtility.getCartridgeTypeNumericId(content);
	if (cartridgeTypeNumericID == 0) {
	    return false;
	}
	CartridgeType cartridgeType = CartridgeType.getInstance(cartridgeTypeNumericID);
	if (cartridgeType == null) {
	    return false;
	}

	// Create new header from content body and compare with old header.
	byte[] newContent = new byte[content.length - CartridgeFileUtility.CART_HEADER_SIZE];
	System.arraycopy(content, CartridgeFileUtility.CART_HEADER_SIZE, newContent, 0, newContent.length);
	byte[] newHeader = CartridgeFileUtility.createCartridgeHeaderWithCheckSum(cartridgeType.getNumericId(),
		newContent);
	for (int i = 0; i < newHeader.length; i++) {
	    if (content[i] != newHeader[i]) {
		return false;
	    }
	}
	return true;
    }

    public boolean convertToCAR(CartridgeType cartridgeType, MessageQueue messageQueue) {
	if (cartridgeType == null) {
	    throw new IllegalArgumentException("Parameter 'cartridgeType' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}
	if (isEmpty()) {
	    // ERROR: File buffer is empty. You have to first load a file.
	    messageQueue.sendMessage(this, Attributes.CONTENT, Messages.E209);
	    return false;
	}

	String cartridgeTypeText = new CartridgeTypeWrapper(cartridgeType).toString();
	long sourceSize = content.length;
	long targetSize = cartridgeType.getSizeInKB() * KB;
	if (sourceSize != targetSize) {
	    // ERROR: Size {0} of the file buffer does not match the size {1} of
	    // the target cartridge type {2}.
	    messageQueue.sendMessage(this, Attributes.CARTRDIGE_TYPE, Messages.E104,
		    TextUtility.formatAsMemorySize(sourceSize), TextUtility.formatAsMemorySize(targetSize),
		    cartridgeTypeText);
	    return false;
	}
	byte[] header = CartridgeFileUtility.createCartridgeHeaderWithCheckSum(cartridgeType.getNumericId(), content);
	byte[] newContent = new byte[header.length + content.length];
	System.arraycopy(header, 0, newContent, 0, header.length);
	System.arraycopy(content, 0, newContent, header.length, content.length);
	content = newContent;

	// INFO: File converted to cartridge of type {1}.
	messageQueue.sendMessage(this, null, Messages.I102, cartridgeTypeText);
	return true;
    }

    public boolean convertToROM(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}
	if (isEmpty()) {
	    // ERROR: File buffer is empty. You have to first load a file.
	    messageQueue.sendMessage(this, Attributes.CONTENT, Messages.E209);
	    return false;
	}
	if (!isValidCAR()) {
	    // ERROR: File buffer does not contain a valid cartridge image file.
	    messageQueue.sendMessage(this, Attributes.CARTRDIGE_TYPE, Messages.E207);
	    return false;
	}
	byte[] newContent = new byte[content.length - CartridgeFileUtility.CART_HEADER_SIZE];
	System.arraycopy(content, CartridgeFileUtility.CART_HEADER_SIZE, newContent, 0, newContent.length);
	content = newContent;

	// INFO: File buffer converted to ROM of size {0}.
	messageQueue.sendMessage(this, null, Messages.I208, TextUtility.formatAsMemorySize(content.length));
	return true;
    }
}
