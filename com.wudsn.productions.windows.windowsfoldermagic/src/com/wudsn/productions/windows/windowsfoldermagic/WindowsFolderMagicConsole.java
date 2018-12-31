/*
 * Copyright (C) 2013 - 2014 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
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
package com.wudsn.productions.windows.windowsfoldermagic;

import java.io.File;
import java.util.List;

import com.wudsn.productions.windows.windowsfoldermagic.model.Folder;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.console.Console;
import com.wudsn.tools.base.console.ConsoleCommandExecution;
import com.wudsn.tools.base.console.ConsoleCommandParameter.Cardinality;
import com.wudsn.tools.base.console.ConsoleCommandParser;

public final class WindowsFolderMagicConsole {

    private ConsoleCommandParser consoleCommandParser;

    public WindowsFolderMagicConsole() {
	// Create the list of valid command line commands.
	consoleCommandParser = new ConsoleCommandParser("-jar WindowsFolderMagic.jar");
	consoleCommandParser.addCommand(Actions.SetDefaultIconFile, WindowsFolderMagic.Commands.SET_DEFAULT_ICON_FILE)
		.addParameter(Folder.Attributes.DEFAULT_ICON_FILE, Cardinality.MANDATORY);
	consoleCommandParser
		.addCommand(Actions.SetDefaultIconIndex, WindowsFolderMagic.Commands.SET_DEFAULT_ICON_INDEX)
		.addParameter(Folder.Attributes.DEFAULT_ICON_INDEX, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.SetEmptyIconFile, WindowsFolderMagic.Commands.SET_EMPTY_ICON_FILE)
		.addParameter(Folder.Attributes.EMPTY_ICON_FILE, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.SetEmptyIconIndex, WindowsFolderMagic.Commands.SET_EMPTY_ICON_INDEX)
		.addParameter(Folder.Attributes.EMPTY_ICON_INDEX, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.UpdateFolderIcon, WindowsFolderMagic.Commands.UPDATE_FOLDER_ICON)
		.addParameter(Folder.Attributes.PATH, Cardinality.MANDATORY);

    }

    public boolean runConsoleCommands(String[] args, boolean mandatory, Folder folder, MessageQueue messageQueue) {
	if (args == null) {
	    throw new IllegalArgumentException("Parameter 'args' must not be null.");
	}
	if (folder == null) {
	    throw new IllegalArgumentException("Parameter 'folder' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}

	consoleCommandParser.parse(args, mandatory, messageQueue);

	// Any command specified?
	List<ConsoleCommandExecution> parseResult = consoleCommandParser.getParseResult();
	Console console = consoleCommandParser.getConsole();
	if (!parseResult.isEmpty()) {
	    boolean errorOccurred = false;
	    for (int i = 0; i < parseResult.size() && !errorOccurred; i++) {
		ConsoleCommandExecution commandExecution = parseResult.get(i);
		messageQueue.clear();
		// INFO: Processing command '{0}'.
		messageQueue.sendMessage(this, null, com.wudsn.tools.base.Messages.S254, commandExecution.toString());
		String actionCommand = commandExecution.getConsoleCommand().getActionCommand();
		if (actionCommand.equals(WindowsFolderMagic.Commands.SET_DEFAULT_ICON_FILE)) {
		    try {
			folder.setDefaultIconFile(commandExecution.getParameterValueAsString(
				Folder.Attributes.DEFAULT_ICON_FILE, true));
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, Folder.Attributes.DEFAULT_ICON_FILE));
		    }

		} else if (actionCommand.equals(WindowsFolderMagic.Commands.SET_DEFAULT_ICON_INDEX)) {
		    try {
			folder.setDefaultIconIndex(commandExecution.getParameterValueAsInteger(
				Folder.Attributes.DEFAULT_ICON_INDEX, true).intValue());
		    } catch (CoreException ex) {
			messageQueue
				.sendMessage(ex.createMessageQueueEntry(this, Folder.Attributes.DEFAULT_ICON_INDEX));
		    }

		}else if (actionCommand.equals(WindowsFolderMagic.Commands.SET_EMPTY_ICON_FILE)) {
		    try {
			folder.setEmptyIconFile(commandExecution.getParameterValueAsString(
				Folder.Attributes.EMPTY_ICON_FILE, true));
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, Folder.Attributes.EMPTY_ICON_FILE));
		    }

		} else if (actionCommand.equals(WindowsFolderMagic.Commands.SET_EMPTY_ICON_INDEX)) {
		    try {
			folder.setEmptyIconIndex(commandExecution.getParameterValueAsInteger(
				Folder.Attributes.EMPTY_ICON_INDEX, true).intValue());
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, Folder.Attributes.EMPTY_ICON_INDEX));
		    }

		} else if (actionCommand.equals(WindowsFolderMagic.Commands.UPDATE_FOLDER_ICON)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(Folder.Attributes.PATH, true);
			folder.updateFolderIcon(file, messageQueue);

		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, Folder.Attributes.EMPTY_ICON_INDEX));
		    }

		} else {
		    throw new RuntimeException("Unhandled action command '" + actionCommand + "'.");
		}
		errorOccurred = messageQueue.containsError();
		console.displayMessageQueue(messageQueue);
	    }
	    return true;

	}
	return false;
    }
}
