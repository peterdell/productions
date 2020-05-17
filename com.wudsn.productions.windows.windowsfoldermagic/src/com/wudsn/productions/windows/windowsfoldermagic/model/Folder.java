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

package com.wudsn.productions.windows.windowsfoldermagic.model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.wudsn.productions.windows.windowsfoldermagic.DataTypes;
import com.wudsn.productions.windows.windowsfoldermagic.Messages;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.repository.Attribute;

/**
 * Folder model
 * 
 * @author Peter Dell
 * 
 */
public final class Folder {

    private static final class FolderInfo {

	public FolderInfo(File file) {
	    if (file == null) {
		throw new IllegalArgumentException("Parameter 'file' must not be null.");
	    }
	    this.file = file;
	}

	public final File file;
	public int folders;
	public int files;
	public long bytes;
    }

    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute PATH = new Attribute("folderPath", DataTypes.Folder_Path);
	public static final Attribute DEFAULT_ICON_FILE = new Attribute("iconFilePath",
		DataTypes.Folder_DefaultIconFile);
	public static final Attribute DEFAULT_ICON_INDEX = new Attribute("iconIndex", DataTypes.Folder_DefaultIconIndex);
	public static final Attribute EMPTY_ICON_FILE = new Attribute("iconFilePath", DataTypes.Folder_EmptyIconFile);
	public static final Attribute EMPTY_ICON_INDEX = new Attribute("iconIndex", DataTypes.Folder_EmptyIconIndex);

    }

    private String defaultIconFile;
    private int defaultIconIndex;
    private String emptyIconFile;
    private int emptyIconIndex;

    public Folder() {
	defaultIconFile = "";
	defaultIconIndex = -1;
	emptyIconFile = "%SystemRoot%\\system32\\SHELL32.dll";
	emptyIconIndex = 205;
    }

    public void setDefaultIconFile(String iconFile) {
	if (iconFile == null) {
	    throw new IllegalArgumentException("Parameter 'iconFile' must not be null.");
	}
	this.defaultIconFile = iconFile;
    }

    public void setDefaultIconIndex(int iconIndex) {
	this.defaultIconIndex = iconIndex;
    }

    public void setEmptyIconFile(String iconFile) {
	if (iconFile == null) {
	    throw new IllegalArgumentException("Parameter 'iconFile' must not be null.");
	}
	this.emptyIconFile = iconFile;
    }

    public void setEmptyIconIndex(int iconIndex) {
	this.emptyIconIndex = iconIndex;
    }

    public boolean updateFolderIcon(File file, MessageQueue messageQueue) {
	if (file == null) {
	    throw new IllegalArgumentException("Parameter 'file' must not be null.");
	}

	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}
	FolderInfo folderInfo = new FolderInfo(file);
	processFolders(folderInfo, messageQueue);

	return false;
    }

    private void processFolders(FolderInfo folderInfo, MessageQueue messageQueue) {
	if (folderInfo == null) {
	    throw new IllegalArgumentException("Parameter 'folderInfo' must not be null.");
	}
	if (!folderInfo.file.exists()) {
	    // Folder '{0}' does not exist
	    messageQueue.sendMessage(this, null, com.wudsn.tools.base.Messages.E200, folderInfo.file.getAbsolutePath());
	    return;
	}

	if (!folderInfo.file.isDirectory()) {
	    // '{0}' is no folder but a file
	    messageQueue.sendMessage(this, null, com.wudsn.tools.base.Messages.E201, folderInfo.file.getAbsolutePath());
	    return;

	}

	File[] files = folderInfo.file.listFiles();
	if (files != null) {
	    Arrays.sort(files);
	    for (File file : files) {
		if (file.getName().equalsIgnoreCase("Desktop.ini")) {
		    continue;
		}
		if (file.isDirectory()) {
		    folderInfo.folders++;

		    FolderInfo subFolderfolderInfo = new FolderInfo(file);
		    processFolders(subFolderfolderInfo, messageQueue);
		    folderInfo.folders += subFolderfolderInfo.folders;
		    folderInfo.files += subFolderfolderInfo.files;
		    folderInfo.bytes += subFolderfolderInfo.bytes;
		} else {
		    folderInfo.files++;
		    folderInfo.bytes += file.length();
		}
	    }
	}
	messageQueue.sendMessage(this, null, Messages.I100, folderInfo.file.getAbsolutePath(),
		TextUtility.formatAsDecimal(folderInfo.folders), TextUtility.formatAsDecimal(folderInfo.files),
		TextUtility.formatAsMemorySize(folderInfo.bytes));

	String path;
	path = folderInfo.file.getAbsolutePath();

	exec("attrib -r \"" + path + "\"", messageQueue);
	exec("attrib +r \"" + path + "\"", messageQueue);

	File desktopIniFile = new File(folderInfo.file, "Desktop.ini");
	String content;
	if (folderInfo.files == 0) {
	    content = getDesktopIniContent(emptyIconFile, emptyIconIndex);
	    try {
		FileUtility.writeString(desktopIniFile, content);
	    } catch (CoreException ex) {
		messageQueue.sendMessage(ex.createMessageQueueEntry(this, null));
	    }
	} else {
	    desktopIniFile.setWritable(true);
	    if (StringUtility.isSpecified(defaultIconFile)) {
		content = getDesktopIniContent(defaultIconFile, defaultIconIndex);

		try {
		    FileUtility.writeString(desktopIniFile, content);
		} catch (CoreException ex) {
		    messageQueue.sendMessage(ex.createMessageQueueEntry(this, null));
		}
	    } else {
		desktopIniFile.delete();
	    }
	}
    }

    private static String getDesktopIniContent(String iconFile, int iconIndex) {
	return "[.ShellClassInfo]\r\n" + "IconResource=" + iconFile + "," + iconIndex + "\r\n" + "IconFile=" + iconFile
		+ "\r\n" + "IconIndex=" + iconIndex + "\r\n";
    }

    private static void exec(String command, MessageQueue messageQueue) {
	if (command == null) {
	    throw new IllegalArgumentException("Parameter 'command' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueues' must not be null.");
	}

	Process process;
	try {
	    process = Runtime.getRuntime().exec(command);
	} catch (IOException ioex) {
	    // ERROR: Command '{0}' ended with error {1}
	    messageQueue.sendMessage(null, null, Messages.E103, command, ioex.getMessage());
	    return;
	}
	try {
	    int result = process.waitFor();
	    if (result != 0) {
		// ERROR: Command '{0}' ended with return code {1}
		messageQueue.sendMessage(null, null, Messages.E102, command, TextUtility.formatAsDecimal(result));
		return;
	    }
	} catch (InterruptedException ex) {
	    // ERROR: Command '{0}' was interrupted
	    messageQueue.sendMessage(null, null, Messages.E101, command);
	    return;
	}
    }
}
