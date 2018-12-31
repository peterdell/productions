/**
 * Copyright (C) 2014 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
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

package com.wudsn.productions.atari800.atariromchecker;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import com.wudsn.productions.atari800.atariromchecker.model.Comparison;
import com.wudsn.productions.atari800.atariromchecker.model.ROMVersionDatabase;
import com.wudsn.productions.atari800.atariromchecker.model.ROMVersionWriter;
import com.wudsn.productions.atari800.atariromchecker.model.Workbook;
import com.wudsn.productions.atari800.atariromchecker.model.WorkbookEntry;
import com.wudsn.productions.atari800.atariromchecker.model.WorkbookLogic;
import com.wudsn.productions.atari800.atariromchecker.ui.ComparisonDialog;
import com.wudsn.productions.atari800.atariromchecker.ui.MainMenu;
import com.wudsn.productions.atari800.atariromchecker.ui.WorkbookEntriesPanel;
import com.wudsn.productions.atari800.atariromchecker.ui.WorkbookEntryDialog;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabase;
import com.wudsn.tools.base.common.Application;
import com.wudsn.tools.base.common.Log;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.ResourceUtility.ResourceModifier;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.AttributeTable;
import com.wudsn.tools.base.gui.FileDrop;
import com.wudsn.tools.base.gui.FileDrop.Listener;
import com.wudsn.tools.base.gui.HelpDialog;
import com.wudsn.tools.base.gui.StatusBar;
import com.wudsn.tools.base.gui.UIManager;

/**
 * Atari ROM Checker by JAC!
 * 
 * @author Peter Dell
 */

public final class AtariROMChecker implements ActionListener, Listener {

    public final class Commands {
	public static final String EXIT = "exit";
	public static final String SHOW_ENTRY = "showEntry";
	public static final String COMPARE_ENTRIES = "compareEntries";
	public static final String HELP_CONTENTS = "helpContents";
    }

    // Static instance
    static AtariROMChecker instance;

    // Message queue
    private MessageQueue messageQueue;

    // AttributeTablePreferences
    private Preferences preferences;

    private WorkbookLogic workbookLogic;
    private Workbook workbook;

    private JFrame mainWindowFrame;
    private MainMenu mainMenu;
    private WorkbookEntriesPanel entriesPanel;
    private StatusBar statusBar;

    // Dialogs
    private HelpDialog helpDialog;

    @SuppressWarnings("unused")
    private FileDrop fileDrop;

    public static void main(final String[] args) {
	if (args == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'args' must not be null.");
	}

	// Use the event dispatch thread for Swing components
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {

		Application
			.createInstance(
				"http://www.wudsn.com/productions/atari800/atariromchecker/atariromchecker.zip",
				"AtariROMChecker.jar",
				"com/wudsn/tools/atariromchecker/AtariROMChecker.version");
		instance = new AtariROMChecker();
		instance.run(args);
	    }
	});
    }

    AtariROMChecker() {
    }

    void run(String[] args) {
	if (args == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'args' must not be null.");
	}

	UIManager.init();

	messageQueue = new MessageQueue();
	preferences = new Preferences();

	ROMVersionDatabase romVersionDatabase = new ROMVersionDatabase();
	romVersionDatabase.load();

	CartridgeDatabase cartridgeDatabase = new CartridgeDatabase();
	cartridgeDatabase.load();

	workbookLogic = new WorkbookLogic(romVersionDatabase, cartridgeDatabase);
	workbook = new Workbook();

	createUI();

	// INFO: Welcome to the Atari ROM Checker Version {0} by JAC!. Drop your
	// folders or files on the window.
	messageQueue.sendMessage(null, null, Messages.I100, Application
		.getInstance().getLocalVersion());
	dataToUI();

	List<File> filesList = new ArrayList<File>();
	for (String arg : args) {
	    filesList.add(new File(arg));
	}
	if (!filesList.isEmpty()) {
	    performFilesDropped(filesList);
	}
    }

    private void createUI() {
	mainWindowFrame = new JFrame(Texts.MainWindow_Title);
	mainWindowFrame.setSize(800, 600);
	mainWindowFrame.setLocationRelativeTo(null);

	mainWindowFrame
		.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	mainWindowFrame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		actionPerformed(new ActionEvent(this, 0, Commands.EXIT));
	    }
	});

	mainWindowFrame.setLayout(new BorderLayout());

	mainMenu = new MainMenu(this, preferences);
	mainWindowFrame.add(mainMenu.menuBar, BorderLayout.NORTH);
	entriesPanel = new WorkbookEntriesPanel(preferences, workbook);
	mainWindowFrame.add(entriesPanel, BorderLayout.CENTER);
	entriesPanel.getTable().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent mouseEvent) {
		JTable table = (JTable) mouseEvent.getSource();
		if (mouseEvent.getClickCount() == 2
			&& table.getSelectedRow() != -1) {
		    performShowEntry();
		}
	    }
	});
	statusBar = new StatusBar();
	messageQueue.setMessageQueueRenderer(statusBar);
	mainWindowFrame.add(statusBar.getComponent(), BorderLayout.SOUTH);

	mainWindowFrame.setVisible(true);
	fileDrop = new FileDrop(mainWindowFrame, true, this);
    }

    public void dataFromUI() {

    }

    public void dataToUI() {
	entriesPanel.dataToUI();
	statusBar.displayMessageQueue(messageQueue);
    }

    public void performFilesDropped(List<File> filesList) {
	if (filesList == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'filesList' must not be null.");
	}
	dataFromUI();
	messageQueue.clear();
	try {
	    workbookLogic.checkFiles(workbook, filesList);
	    // INFO: {0} files found and analyzed.
	    messageQueue.sendMessage(null, null, Messages.I102, TextUtility
		    .formatAsDecimal(workbook.getResolvedFilesCount()));

	} catch (RuntimeException ex) {
	    // ERROR: Exception occurred: {0}
	    messageQueue
		    .sendMessage(null, null, Messages.E101, ex.getMessage());
	    Log.logError("Exception while checking files.", null, ex);
	}
	dataToUI();
    }

    @Override
    public boolean isDropAllowed() {
	return true;
    }

    @Override
    public void filesDropped(File[] files) {
	if (files == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'files' must not be null.");
	}
	performFilesDropped(Arrays.asList(files));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
	if (actionEvent == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'actionEvent' must not be null.");
	}
	dataFromUI();

	String command = actionEvent.getActionCommand();
	messageQueue.clear();
	statusBar.displayMessageQueue(messageQueue);

	if (command.equals(Commands.EXIT)) {
	    performExit();
	} else if (command.equals(Commands.SHOW_ENTRY)
		&& mainMenu.showEntryMenuItem.isEnabled()) {
	    performShowEntry();
	} else if (command.equals(Commands.COMPARE_ENTRIES)
		&& mainMenu.compareEntriesMenuItem.isEnabled()) {
	    performCompareEntries();
	} else if (command.equals(Commands.HELP_CONTENTS)) {
	    performHelpDialog();
	}
	dataToUI();
    }

    @SuppressWarnings("static-method")
    private void performExit() {
	System.exit(0);
	return;
    }

    private void performHelpDialog() {
	if (helpDialog == null) {
	    final String path = "help/AtariROMChecker.html";
	    helpDialog = new HelpDialog(mainWindowFrame, path, 640, 320,
		    new ResourceModifier() {

			@Override
			public byte[] modifyResource(URL url, byte[] data) {
			    if (url.getPath().endsWith(path)) {
				try {
				    final String charset = "UTF-8";
				    String content = new String(data, charset);
				    String html = ROMVersionWriter.getHTML();
				    content = content.replace("${romVersions}",
					    html);
				    data = content.getBytes(charset);
				} catch (UnsupportedEncodingException ex) {
				    data = ex.getMessage().getBytes();
				}
			    }
			    return data;
			}
		    });
	}

	helpDialog.show();
    }

    private int[] getSelectedRowIndexes() {
	AttributeTable entriesTable = entriesPanel.getTable();
	int[] selectedViewRowIndexes = entriesTable.getSelectedRows();
	int[] selectedModelRowIndexes = new int[selectedViewRowIndexes.length];
	for (int i = 0; i < selectedViewRowIndexes.length; i++) {
	    selectedModelRowIndexes[i] = entriesTable
		    .convertRowIndexToModel(selectedViewRowIndexes[i]);
	}
	return selectedModelRowIndexes;
    }

    private List<WorkbookEntry> getSelectedWorkbookEntries() {
	int[] selectedModelRowIndexes = getSelectedRowIndexes();
	List<WorkbookEntry> entries = workbook.getUnmodifiableEntriesList();
	List<WorkbookEntry> selectedEntries = new ArrayList<WorkbookEntry>();
	for (int i = 0; i < selectedModelRowIndexes.length; i++) {
	    selectedEntries.add(entries.get(selectedModelRowIndexes[i]));
	}
	return selectedEntries;
    }

    private void performShowEntry() {
	int[] selectedModelRowIndexes = getSelectedRowIndexes();
	if (selectedModelRowIndexes.length == 1) {
	    int selectedRow = selectedModelRowIndexes[0];
	    WorkbookEntryDialog dialog = new WorkbookEntryDialog(
		    mainWindowFrame, entriesPanel, selectedRow);
	    dialog.showModal(messageQueue);
	} else {
	    // ERROR: Select exactly only entry.
	    messageQueue.sendMessage(null, null, Messages.E106);
	}
    }

    private void performCompareEntries() {
	List<WorkbookEntry> selectedEntries = getSelectedWorkbookEntries();
	Comparison comparison = workbookLogic.compareEntries(workbook,
		selectedEntries, messageQueue);
	if (comparison != null) {
	    ComparisonDialog dialog = new ComparisonDialog(mainWindowFrame,
		    preferences, comparison);
	    dialog.showModal(messageQueue);
	}

    }
}