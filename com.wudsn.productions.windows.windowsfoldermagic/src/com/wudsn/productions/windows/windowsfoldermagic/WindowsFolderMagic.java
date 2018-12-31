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

import java.awt.EventQueue;

import com.wudsn.productions.windows.windowsfoldermagic.model.Folder;
import com.wudsn.tools.base.common.Application;
import com.wudsn.tools.base.common.MessageQueue;

/**
 * Windows Folder Magic by JAC!
 * 
 * @author Peter Dell
 */

public final class WindowsFolderMagic {

    public final class Commands {
	public static final String SET_DEFAULT_ICON_FILE = "setDefaultIconFile";
	public static final String SET_DEFAULT_ICON_INDEX = "setDefaultIconIndex";
	public static final String SET_EMPTY_ICON_FILE = "setEmptyIconFile";
	public static final String SET_EMPTY_ICON_INDEX = "setEmptyIconIndex";
	public static final String UPDATE_FOLDER_ICON = "updateFolderIcon";

    }

    // Static instance
    static WindowsFolderMagic instance;

    // Message queue
    private MessageQueue messageQueue;

    public static void main(final String[] args) {
	if (args == null) {
	    throw new IllegalArgumentException("Parameter 'args' must not be null.");
	}

	// Use the event dispatch thread for Swing components
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {

		Application.createInstance(
			"http://www.wudsn.com/productions/windows/windowsfoldermagic/windowsfoldermagic.zip",
			"WindowsFolderMagic.jar",
			"com/wudsn/productions/windows/windowsfoldermagic/WindowsFolderMagic.version");
		instance = new WindowsFolderMagic();
		instance.run(args);
	    }
	});
    }

    WindowsFolderMagic() {
    }

    void run(String[] args) {
	if (args == null) {
	    throw new IllegalArgumentException("Parameter 'args' must not be null.");
	}

	Folder folder = new Folder();
	messageQueue = new MessageQueue();

	// Handle command line.
	WindowsFolderMagicConsole console = new WindowsFolderMagicConsole();
	if (console.runConsoleCommands(args, true, folder, messageQueue)) {
	    return;
	}

    }

}