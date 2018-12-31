/*
 * Copyright (C) 2013 - 2016 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
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
package com.wudsn.productions.atari800.atarirommaker;

import java.io.File;
import java.util.List;

import com.wudsn.productions.atari800.atarirommaker.model.ROM;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.console.Console;
import com.wudsn.tools.base.console.ConsoleCommandExecution;
import com.wudsn.tools.base.console.ConsoleCommandParameter.Cardinality;
import com.wudsn.tools.base.console.ConsoleCommandParser;

public final class AtariROMMakerConsole {

    private ConsoleCommandParser consoleCommandParser;

    public AtariROMMakerConsole() {
	// Create the list of valid command line commands.
	consoleCommandParser = new ConsoleCommandParser("-jar AtariROMMaker.jar");
	consoleCommandParser.addCommand(Actions.Load, AtariROMMaker.Commands.LOAD).addParameter(
		ROM.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.ConvertToCAR, AtariROMMaker.Commands.CONVERT_TO_CAR).addParameter(
		ROM.Attributes.CARTRDIGE_TYPE, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.ConvertToROM, AtariROMMaker.Commands.CONVERT_TO_ROM);
	consoleCommandParser.addCommand(Actions.Save, AtariROMMaker.Commands.SAVE).addParameter(
		ROM.Attributes.FILE_PATH, Cardinality.MANDATORY);
    }

    public boolean runConsoleCommands(String[] args, boolean mandatory, ROM rom, MessageQueue messageQueue) {
	if (args == null) {
	    throw new IllegalArgumentException("Parameter 'args' must not be null.");
	}
	if (rom == null) {
	    throw new IllegalArgumentException("Parameter 'rom' must not be null.");
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
		if (actionCommand.equals(AtariROMMaker.Commands.LOAD)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(ROM.Attributes.FILE_PATH, true);
			if (file != null) {
			    rom.load(file, messageQueue);
			}

		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, ROM.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(AtariROMMaker.Commands.SAVE)) {
		    try {

			File file = commandExecution.getParameterValueAsFile(ROM.Attributes.FILE_PATH, true);
			if (file != null) {
			    rom.save(file, messageQueue);
			}

		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, ROM.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(AtariROMMaker.Commands.CONVERT_TO_CAR)) {
		    try {
			String cartridgeTypeId = commandExecution.getParameterValueAsString(
				ROM.Attributes.CARTRDIGE_TYPE, true);
			if (cartridgeTypeId != null) {
			    rom.convertToCAR(cartridgeTypeId, messageQueue);
			}

		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(this, ROM.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(AtariROMMaker.Commands.CONVERT_TO_ROM)) {
		    rom.convertToROM(messageQueue);

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
