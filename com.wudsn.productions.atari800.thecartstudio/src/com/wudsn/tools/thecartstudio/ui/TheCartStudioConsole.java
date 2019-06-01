/*
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
package com.wudsn.tools.thecartstudio.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.console.Console;
import com.wudsn.tools.base.console.ConsoleCommandExecution;
import com.wudsn.tools.base.console.ConsoleCommandParameter.Cardinality;
import com.wudsn.tools.base.console.ConsoleCommandParser;
import com.wudsn.tools.thecartstudio.Actions;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.TheCartStudio.Commands;
import com.wudsn.tools.thecartstudio.model.CartridgeTypeSampleCreator;
import com.wudsn.tools.thecartstudio.model.ExportFormat;
import com.wudsn.tools.thecartstudio.model.Exporter;
import com.wudsn.tools.thecartstudio.model.Workbook;
import com.wudsn.tools.thecartstudio.model.WorkbookAddEntriesCallback;
import com.wudsn.tools.thecartstudio.model.WorkbookExport;

public final class TheCartStudioConsole {

    private ConsoleCommandParser consoleCommandParser;

    public TheCartStudioConsole() {
	// Create the list of valid command line commands.
	consoleCommandParser = new ConsoleCommandParser(
		"-jar TheCartStudio.jar");
	consoleCommandParser.addCommand(Actions.MainMenu_File_New,
		Commands.NEW_FILE);
	consoleCommandParser.addCommand(Actions.MainMenu_File_Open,
		Commands.OPEN_FILE).addParameter(Workbook.Attributes.FILE_PATH,
		Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.MainMenu_File_Save,
		Commands.SAVE_FILE);
	consoleCommandParser.addCommand(Actions.MainMenu_File_SaveAs,
		Commands.SAVE_FILE_AS).addParameter(
		Workbook.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.MainMenu_File_Close,
		Commands.CLOSE_FILE);
	consoleCommandParser.addCommand(Actions.MainMenu_File_ExportToBinImage,
		Commands.EXPORT_TO_BIN_IMAGE).addParameter(
		Workbook.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.MainMenu_File_ExportToCarImage,
		Commands.EXPORT_TO_CAR_IMAGE).addParameter(
		Workbook.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.MainMenu_File_ExportToAtrImage,
		Commands.EXPORT_TO_ATR_IMAGE).addParameter(
		Workbook.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(
		Actions.MainMenu_File_ExportToAtrImages,
		Commands.EXPORT_TO_ATR_IMAGES).addParameter(
		Workbook.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.MainMenu_Edit_AddEntries,
		Commands.ADD_ENTRIES).addParameter(
		Workbook.Attributes.FILE_PATH, Cardinality.MANDATORY);
	consoleCommandParser.addCommand(Actions.MainMenu_Edit_AssignNewBanks,
		Commands.ASSIGN_NEW_BANKS);
	consoleCommandParser.addCommand(Actions.Console_CreateSampleFiles,
		Commands.CREATE_SAMPLE_FILES).addParameter(
		Workbook.Attributes.FOLDER_PATH, Cardinality.MANDATORY);
    }

    public boolean runConsoleCommands(String[] args, boolean mandatory,
	    Workbook workbook, MessageQueue messageQueue) {
	if (args == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'args' must not be null.");
	}
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	// Transform default drag & drop file argument to "-open:<file>"
	boolean exit = true;
	if (args.length == 1 && args[0].toLowerCase().endsWith(".tcw")) {
	    args[0] = "-" + Commands.OPEN_FILE + ":" + args[0];
	    exit = false;
	}
	consoleCommandParser.parse(args, mandatory, messageQueue);

	// Any command specified?
	List<ConsoleCommandExecution> parseResult = consoleCommandParser
		.getParseResult();
	Console console = consoleCommandParser.getConsole();
	if (!parseResult.isEmpty()) {
	    boolean errorOccurred = false;
	    for (int i = 0; i < parseResult.size() && !errorOccurred; i++) {
		ConsoleCommandExecution commandExecution = parseResult.get(i);
		messageQueue.clear();
		// INFO: Processing command '{0}'.
		messageQueue.sendMessage(this, null,
			com.wudsn.tools.base.Messages.S254,
			commandExecution.toString());
		String actionCommand = commandExecution.getConsoleCommand()
			.getActionCommand();
		if (actionCommand.equals(Commands.NEW_FILE)) {
		    if (workbook.create(messageQueue)) {
			// INFO: Workbook created.
			messageQueue.sendMessage(this, null, Messages.I140);
		    }
		} else if (actionCommand.equals(Commands.OPEN_FILE)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FILE_PATH, true);
			workbook.open(file, messageQueue);
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(Commands.SAVE_FILE)) {
		    File file = workbook.getFile();
		    if (file == null) {
			// Only workbooks which have been saved before can be
			// saved with "-save". Use "-saveAs"" instead.
			messageQueue.sendMessage(this, null, Messages.E141);
		    } else {
			workbook.save(file, messageQueue);
			if (!messageQueue.containsError()) {
			    // INFO: Workbook saved.
			    messageQueue.sendMessage(this, null, Messages.I106);
			}
		    }
		} else if (actionCommand.equals(Commands.SAVE_FILE_AS)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FILE_PATH, true);
			workbook.save(file, messageQueue);
			if (!messageQueue.containsError()) {
			    // INFO: Workbook saved.
			    messageQueue.sendMessage(this, null, Messages.I106);
			}
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(Commands.CLOSE_FILE)) {
		    workbook.close();
		    if (!messageQueue.containsError()) {
			// INFO: Workbook closed.
			messageQueue.sendMessage(this, null, Messages.I107);
		    }
		} else if (actionCommand.equals(Commands.EXPORT_TO_BIN_IMAGE)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FILE_PATH, true);
			WorkbookExport workbookExport = workbook.export(
				ExportFormat.BIN_IMAGE, messageQueue);
			if (!messageQueue.containsError()) {
			    // The exporter also sends the info messages
			    Exporter exporter = new Exporter(workbookExport,
				    messageQueue);
			    exporter.exportAsBinImage(file);
			}
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(Commands.EXPORT_TO_CAR_IMAGE)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FILE_PATH, true);
			WorkbookExport workbookExport = workbook.export(
				ExportFormat.CAR_IMAGE, messageQueue);
			if (!messageQueue.containsError()) {
			    // The exporter also sends the info messages
			    Exporter exporter = new Exporter(workbookExport,
				    messageQueue);
			    exporter.exportAsCarImage(file);
			}
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(Commands.EXPORT_TO_ATR_IMAGE)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FILE_PATH, true);
			WorkbookExport workbookExport = workbook.export(
				ExportFormat.ATR_IMAGE, messageQueue);
			if (!messageQueue.containsError()) {
			    // The exporter also sends the info messages
			    Exporter exporter = new Exporter(workbookExport,
				    messageQueue);
			    exporter.exportAsAtrImage(file);
			}
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }

		} else if (actionCommand.equals(Commands.EXPORT_TO_ATR_IMAGES)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FILE_PATH, true);
			WorkbookExport workbookExport = workbook.export(
				ExportFormat.ATR_IMAGES, messageQueue);
			if (!messageQueue.containsError()) {
			    // The exporter also sends the info messages
			    Exporter exporter = new Exporter(workbookExport,
				    messageQueue);
			    exporter.exportAsAtrImages(file);
			}
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }
		} else if (actionCommand.equals(Commands.ADD_ENTRIES)) {
		    List<String> parameterValues = commandExecution
			    .getParameterValuesAsString(Workbook.Attributes.FILE_PATH);
		    List<File> filesList = new ArrayList<File>();
		    for (String parameterValue : parameterValues) {
			File file = new File(parameterValue);
			String fileName = file.getName();
			if (fileName.contains("*")) {
			    final Pattern pattern = Pattern.compile(fileName
				    .replaceAll("\\*", ".*"));
			    File[] files = file.getParentFile().listFiles(
				    new FilenameFilter() {

					@Override
					public boolean accept(File dir,
						String name) {
					    return pattern.matcher(name)
						    .matches();
					}
				    });
			    if (files != null) {
				for (File otherFile : files) {
				    filesList.add(otherFile);
				}
			    }
			} else {
			    filesList.add(file);
			}
		    }
		    WorkbookAddEntriesCallback callback = new WorkbookAddEntriesCallback() {
			@Override
			public int confirmAdd(String existingEntryTitle,
				String existingFileName, String newFilePath,
				String renamedFileName) {
			    return AddResult.OVERWRITE;
			}

			@Override
			public int confirmUseTitleFromCartridgeDatabase(
				String existingEntryTitle,
				String existingFileName) {
			    return UseTitleResult.YES;
			}
		    };
		    workbook.addEntries(
			    filesList.toArray(new File[filesList.size()]),
			    callback, messageQueue);
		    if (!messageQueue.containsError()) {
			// INFO: {0} entries added, {1} files updated and {2}
			// files skipped.
			messageQueue.sendMessage(workbook, null, Messages.I108,
				TextUtility.formatAsDecimal(callback
					.getAddedEntriesCount()), TextUtility
					.formatAsDecimal(callback
						.getUpdatesEntriesCount()),
				TextUtility.formatAsDecimal(callback
					.getSkippedEntriesCount()));
		    }

		} else if (actionCommand.equals(Commands.ASSIGN_NEW_BANKS)) {
		    workbook.assignNewBanks(messageQueue);
		    if (!messageQueue.containsError()) {
			// INFO: All entries have been reassigned successfully.
			messageQueue.sendMessage(workbook, null, Messages.I125);
		    }
		} else if (actionCommand.equals(Commands.CREATE_SAMPLE_FILES)) {
		    try {
			File file = commandExecution.getParameterValueAsFile(
				Workbook.Attributes.FOLDER_PATH, true);
			CartridgeTypeSampleCreator instance = new CartridgeTypeSampleCreator();
			// This bypasses the message queue for console output.
			// Therefore we output the current status first.
			console.displayMessageQueue(messageQueue);
			messageQueue.clear();
			try {
			    instance.run(file);
			} catch (CoreException ex) {
			    messageQueue.sendMessage(ex
				    .createMessageQueueEntry(null, null));
			}
		    } catch (CoreException ex) {
			messageQueue.sendMessage(ex.createMessageQueueEntry(
				this, Workbook.Attributes.FILE_PATH));
		    }
		} else {
		    throw new RuntimeException("Unhandled action command '"
			    + actionCommand + "'.");
		}
		errorOccurred = messageQueue.containsError();
		console.displayMessageQueue(messageQueue);
	    }
	    return exit;

	}
	return false;
    }
}
