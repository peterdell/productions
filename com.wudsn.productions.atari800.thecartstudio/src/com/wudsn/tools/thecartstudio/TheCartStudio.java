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
package com.wudsn.tools.thecartstudio;

import static com.wudsn.tools.base.common.ByteArrayUtility.MB;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabase;
import com.wudsn.tools.base.common.Application;
import com.wudsn.tools.base.common.DateUtility;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.Log;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.MessageQueueEntry;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.AttributeTable;
import com.wudsn.tools.base.gui.Desktop;
import com.wudsn.tools.base.gui.ElementFactory;
import com.wudsn.tools.base.gui.FileChooser;
import com.wudsn.tools.base.gui.FileDrop;
import com.wudsn.tools.base.gui.FileDrop.Listener;
import com.wudsn.tools.base.gui.HelpDialog;
import com.wudsn.tools.base.gui.MainWindow;
import com.wudsn.tools.base.gui.StandardDialog;
import com.wudsn.tools.base.gui.StatusBar;
import com.wudsn.tools.base.repository.Action;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.base.repository.Message;
import com.wudsn.tools.base.repository.NLS;
import com.wudsn.tools.thecartstudio.model.ExportFormat;
import com.wudsn.tools.thecartstudio.model.Exporter;
import com.wudsn.tools.thecartstudio.model.FlashTargetType;
import com.wudsn.tools.thecartstudio.model.Preferences;
import com.wudsn.tools.thecartstudio.model.Workbook;
import com.wudsn.tools.thecartstudio.model.WorkbookAddEntriesCallback;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry;
import com.wudsn.tools.thecartstudio.model.WorkbookExport;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot;
import com.wudsn.tools.thecartstudio.ui.AboutDialog;
import com.wudsn.tools.thecartstudio.ui.ContentTypesDialog;
import com.wudsn.tools.thecartstudio.ui.MainMenu;
import com.wudsn.tools.thecartstudio.ui.OptionsDialog;
import com.wudsn.tools.thecartstudio.ui.TheCartStudioConsole;
import com.wudsn.tools.thecartstudio.ui.WorkbookBanksPanel;
import com.wudsn.tools.thecartstudio.ui.WorkbookBanksSummaryPanel;
import com.wudsn.tools.thecartstudio.ui.WorkbookEntriesPanel;
import com.wudsn.tools.thecartstudio.ui.WorkbookOptionsDialog;

/**
 * TODO Implement favorites support <br/>
 * TODO Replay .SAP files, https://github.com/epi/sapemu. Requires file length
 * to be known, this would also allow for autostart XEX without INIADR/RUNADR
 * segments<<br/>
 * TODO Allow setting of one cart as the "Autorun" default, see
 * http://atariage.com/forums/user/8819-ebiguy message.<br/>
 * TODO Add support for "AtariMax" & "MegaCart" Bankswitching in the extended
 * menu<br/>
 * Actually typing in "P M", "Pac Man" or "Man Pac * " should find it. Only
 * letters and numbers are relevant, order of words should be ignored. That is
 * the point of the Google like search. But it looks at some point a bug has
 * creeped in and the space is taken as part of the word instead of the word
 * separator. I'll have a look, but it'll take a while."<br/>
 * TODO Save last 24 entries in EEPROM $A006: Read SPI EEPROM $A009: Write SPI
 * EEPROM
 * 
 * Der ZP Vektor in $E0/$E1 muß auf die zu lesenden/schreibenden Daten zeigen, X
 * den Byte-Count und Y die Adresse im EEPROM enthalten. Bei einem Fehler ist
 * nach dem Return das Carry-Flag gesetzt (clear bei OK).
 * 
 * Beim Schreiben von Daten musst Du folgendes beachten: Das EEPROM ist intern
 * in Blöcken zu 16-Bytes organisiert und der interne Adress-Zähler kann diese
 * Grenzen nicht überschreiten (er macht dann einen wrap-around).
 * 
 * Schreibst Du zB 3 Bytes ab Adresse 14, so landen die in den Adressen 14, 15,
 * 0.
 * 
 * Also am besten immer max. 16 Bytes schreiben und aufpassen, daß Du keine
 * 16-Byte Grenze überschreitest.
 * 
 * Wie's beim Lesen aussieht hab' ich noch nicht getestet, aber lieber aufpassen
 * und kleine Häppchen schreiben :)
 * 
 * Achja: Das CartMenu verwendet die EEPROM Adressen ab $F0, der Rest darunter
 * ist frei.<br/>
 * TODO: Have Google like filter in tables (esp. for entries)
 * 
 * @author Peter Dell
 */
public final class TheCartStudio implements ActionListener, Listener {

    public final class Commands {
	public static final String NEW_FILE = "new";
	public static final String NEW_FILE_CONFIRMED = "newConfirmed";
	public static final String OPEN_FILE = "open";
	public static final String OPEN_FILE_CONFIRMED = "openConfirmed";
	public static final String OPEN_RECENT_FILE = "openRecent";
	public static final String OPEN_FOLDER = "openFolder";
	public static final String SAVE_FILE = "save";
	public static final String SAVE_FILE_AS = "saveAs";
	public static final String CLOSE_FILE = "close";
	public static final String CLOSE_FILE_CONFIRMED = "closeConfirmed";
	public static final String EXPORT_TO_BIN_IMAGE = "exportToBinImage";
	public static final String EXPORT_TO_CAR_IMAGE = "exportToCarImage";
	public static final String EXPORT_TO_ATR_IMAGE = "exportToAtrImage";
	public static final String EXPORT_TO_ATR_IMAGES = "exportToAtrImages";
	public static final String PRINT = "print";
	public static final String EXIT = "exit";
	public static final String EXIT_CONFIRMED = "exitConfirmed";

	public static final String ADD_ENTRIES = "addEntries";
	public static final String ADD_USER_SPACE_ENTRY = "addUserSpaceEntry";
	public static final String REMOVE_ENTRIES = "removeEntries";
	public static final String SET_GENRE = "setGenre";
	public static final String ASSIGN_NEW_BANKS = "assignNewBanks";
	public static final String WORKBOOK_OPTIONS = "workbookOptions";

	public static final String PREVIEW = "preview";
	public static final String TEST = "test";
	public static final String OPTIONS = "options";

	public static final String HELP_CONTENTS = "helpContents";
	public static final String HELP_FILE_CONTENT_TYPES = "helpFileTypes";
	public static final String HELP_ABOUT = "helpAbout";

	public static final String DATA_FROM_AND_TO_UI = "dataFromAndToUI";

	public static final String CREATE_SAMPLE_FILES = "createSampleFiles";
    }

    /**
     * Base class for long running commands which send status bar updates during
     * execution.
     */
    @SuppressWarnings("synthetic-access")
    private abstract class CommandWorker extends SwingWorker<Void, Void> {
	private Cursor oldCursor;
	private Throwable throwable;

	public final void start() {
	    oldCursor = mainWindowFrame.getCursor();
	    mainWindowReady = false;
	    mainWindowFrame.setCursor(Cursor
		    .getPredefinedCursor(Cursor.WAIT_CURSOR));
	    throwable = null;
	    this.execute();
	}

	@Override
	protected final Void doInBackground() throws Exception {
	    try {
		performWork();

	    } catch (Throwable th) {
		Log.logError("Exception while executing {0} in background.",
			new Object[] { this }, th);
		throwable = th;
	    }
	    return null;
	}

	protected abstract void performWork() throws Exception;

	@Override
	protected final void done() {
	    mainWindowFrame.setCursor(oldCursor);
	    mainWindowReady = true;
	    if (throwable == null) {
		performDone();
	    } else {
		if (throwable instanceof OutOfMemoryError) {

		    String maximumMemory = Application.getInstance()
			    .getMemoryInfo().getMaximumMemoryMB();
		    long imageSize = workbook.getRoot().getImageSize();
		    imageSize = ((imageSize + MB - 1) / MB) * MB;
		    String jvmParameters = "java -Xmx" + (imageSize * 2) / MB
			    + "M -jar TheCartStudio.jar";
		    String imageSizeMB = TextUtility
			    .formatAsMemorySize(imageSize);
		    // ERROR: {0} are not enough free memory to perform the
		    // operation. Start the program with "{1}" to reserve enough
		    // memory for a workbook of size {2}.
		    messageQueue.sendMessage(workbook, null, Messages.E137,
			    maximumMemory, jvmParameters, imageSizeMB);
		} else {
		    StringWriter stringWriter = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(stringWriter);
		    throwable.printStackTrace(printWriter);
		    printWriter.close();

		    JOptionPane
			    .showMessageDialog(
				    mainWindowFrame,
				    stringWriter.toString()
					    + "\nStart the program from the console to see more details.",
				    throwable.getClass().getName(),
				    JOptionPane.ERROR_MESSAGE);

		}
	    }
	    dataToUI();
	}

	protected abstract void performDone();

    }

    // Static instance
    static TheCartStudio instance;

    // Message queue
    private MessageQueue messageQueue;

    // AttributeTablePreferences
    Preferences preferences;

    // Data model
    CartridgeDatabase cartridgeDatabase;
    Workbook workbook;

    // Main window: Menu
    boolean mainWindowReady;
    MainWindow mainWindow;
    JFrame mainWindowFrame;
    MainMenu mainMenu;
    JTabbedPane tabbedPane;

    // Main window: Workbook entries
    private WorkbookEntriesPanel workbookEntriesPanel;

    // Main window: Workbook banks
    private WorkbookBanksPanel workbookBanksPanel;

    // Main window: Workbook banks summary
    private WorkbookBanksSummaryPanel workbookBanksSummaryPanel;

    // Main window: Status bar
    private StatusBar statusBar;

    // Main window: File choosers
    private FileChooser workbookFileChooser;
    private FileChooser entriesFileChooser;
    private FileChooser exportFileChooser;

    // Main window: Dialogs
    private WorkbookOptionsDialog workbookOptionsDialog;
    private OptionsDialog optionsDialog;
    private HelpDialog helpDialog;
    private ContentTypesDialog contentTypesDialog;
    private AboutDialog aboutDialog;

    // Main window: Drag & drop
    @SuppressWarnings("unused")
    private FileDrop fileDrop;

    public static void main(final String[] args) {

	// Use the event dispatch thread for Swing components
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {

		Application
			.createInstance(
				"http://www.wudsn.com/productions/atari800/thecartstudio/thecartstudio.zip",
				"TheCartStudio.jar",
				"com/wudsn/tools/thecartstudio/TheCartStudio.version");
		instance = new TheCartStudio();
		instance.run(args);
	    }
	});

    }

    TheCartStudio() {

	messageQueue = new MessageQueue();
    }

    void run(String[] args) {
	if (args == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'args' must not be null.");
	}

	cartridgeDatabase = new CartridgeDatabase();
	cartridgeDatabase.load();
	workbook = new Workbook(cartridgeDatabase);
	workbook.create(messageQueue);
	if (messageQueue.containsError()) {
	    StandardDialog.showErrorMessage(new JFrame(), messageQueue
		    .getFirstError().getMessageText(), Texts.MainWindow_Title);
	    messageQueue.clear();
	}

	// Handle command line.
	TheCartStudioConsole console = new TheCartStudioConsole();
	if (console.runConsoleCommands(args, false, workbook, messageQueue)) {
	    return;
	}

	// Load preferences first.
	preferences = new Preferences();
	preferences.open(messageQueue);
	if (messageQueue.containsError()) {
	    StandardDialog.showErrorMessage(new JFrame(), messageQueue
		    .getFirstError().getMessageText(), Texts.MainWindow_Title);
	    messageQueue.clear();
	}

	// There was no command line, so start UI.
	createUI();
	createFileChoosers();
	mainWindowReady = true;

	// INFO: Welcome to The!Cart Studio.
	sendMessage(Messages.I100);
	dataToUI();

	Application.getInstance().checkForUpdate(new Runnable() {

	    @Override
	    public void run() {
		actionPerformed(new ActionEvent(this, 0, Commands.EXIT));

	    }
	});

    }

    private void createUI() {

	// Use current language settings.
	Locale locale = preferences.getLocale();
	Locale.setDefault(locale);
	NLS.initializeLocale(locale.toString());

	mainWindow = new MainWindow();
	mainWindowFrame = mainWindow.getFrame();

	// Make sure the program exits when the frame is closed.
	mainWindowFrame
		.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	mainWindowFrame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		actionPerformed(new ActionEvent(mainWindow, 0, Commands.EXIT));
	    }
	});

	mainWindowFrame.setTitle(Texts.MainWindow_Title);
	ImageIcon imageIcon = ElementFactory
		.createImageIcon("icons/main-48x48.png");

	mainWindowFrame.setIconImage(imageIcon.getImage());

	mainWindow.setWindowFromPreferences(preferences);

	mainWindowFrame.setLayout(new BorderLayout());
	mainMenu = new MainMenu(this, preferences);
	mainWindowFrame.add(mainMenu.menuBar, BorderLayout.NORTH);

	// Place the JTable object in a JScrollPane for a scrolling table
	tabbedPane = new JTabbedPane();
	workbookEntriesPanel = new WorkbookEntriesPanel(preferences, workbook);
	ElementFactory.createTab(tabbedPane, Actions.MainTabbedPane_EntriesTab,
		workbookEntriesPanel);
	workbookBanksPanel = new WorkbookBanksPanel(this, preferences, workbook);
	ElementFactory.createTab(tabbedPane, Actions.MainTabbedPane_BanksTab,
		workbookBanksPanel);
	tabbedPane.addChangeListener(new ChangeListener() {
	    @Override
	    public void stateChanged(ChangeEvent e) {
		actionPerformed(new ActionEvent(tabbedPane, 0,
			Commands.DATA_FROM_AND_TO_UI));
	    }

	});

	mainWindowFrame.add(tabbedPane, BorderLayout.CENTER);

	workbookBanksSummaryPanel = new WorkbookBanksSummaryPanel(preferences,
		workbook);
	statusBar = new StatusBar();
	messageQueue.setMessageQueueRenderer(statusBar);
	Box bottomBox = Box.createVerticalBox();
	bottomBox.add(workbookBanksSummaryPanel);
	bottomBox.add(statusBar.getComponent());
	mainWindowFrame.add(bottomBox, BorderLayout.SOUTH);

	workbookOptionsDialog = new WorkbookOptionsDialog(mainWindowFrame,
		preferences);
	optionsDialog = new OptionsDialog(mainWindowFrame);

	// Make main window visible and register global drag & drop.
	mainWindowFrame.setVisible(true);
	fileDrop = new FileDrop(mainWindowFrame, true, this);

    }

    /**
     * File choosers are created in advance, but all text related setup is
     * performed before the actual usage.
     */
    private void createFileChoosers() {

	workbookFileChooser = FileChooser.createInstance();

	entriesFileChooser = FileChooser.createInstance();
	entriesFileChooser.setMultiSelectionEnabled(true);

	exportFileChooser = FileChooser.createInstance();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
	if (actionEvent == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'actionEvent' must not be null.");
	}
	dataFromUI();

	String command = actionEvent.getActionCommand();
	if (!command.equals(Commands.DATA_FROM_AND_TO_UI)) {
	    messageQueue.clear();
	    statusBar.displayMessageQueue(messageQueue);
	}

	if (command.equals(Commands.NEW_FILE)) {
	    performShowConfirmSaveDialog(mainMenu.newMenuItem,
		    Commands.NEW_FILE_CONFIRMED);
	} else if (command.equals(Commands.NEW_FILE_CONFIRMED)) {
	    performNewFileConfirmed();
	} else if (command.equals(Commands.OPEN_FILE)) {
	    performShowConfirmSaveDialog(mainMenu.openMenuItem,
		    Commands.OPEN_FILE_CONFIRMED);
	} else if (command.startsWith(Commands.OPEN_RECENT_FILE)) {
	    String filePath = command.substring(Commands.OPEN_RECENT_FILE
		    .length() + 1);
	    performShowConfirmSaveDialog(mainMenu.openMenuItem,
		    Commands.OPEN_FILE_CONFIRMED + ":" + filePath);
	} else if (command.startsWith(Commands.OPEN_FILE_CONFIRMED)) {
	    File file;
	    int prefixLength = Commands.OPEN_FILE_CONFIRMED.length() + 1;
	    if (command.length() > prefixLength) {
		String filePath = command.substring(prefixLength);
		file = new File(filePath);
	    } else {
		file = null;
	    }
	    performOpenFileConfirmed(file);
	} else if (command.equals(Commands.OPEN_FOLDER)
		&& mainMenu.openFolderMenuItem.isEnabled()) {
	    performOpenFolder();
	} else if (command.equals(Commands.SAVE_FILE)
		&& mainMenu.saveMenuItem.isEnabled()) {
	    performSaveFile(null);
	} else if (command.equals(Commands.SAVE_FILE_AS)
		&& mainMenu.saveAsMenuItem.isEnabled()) {
	    performSaveFileAs(null);
	} else if (command.equals(Commands.CLOSE_FILE)) {
	    performShowConfirmSaveDialog(mainMenu.closeMenuItem,
		    Commands.CLOSE_FILE_CONFIRMED);
	} else if (command.equals(Commands.CLOSE_FILE_CONFIRMED)) {
	    performCloseConfirmed();
	} else if (command.equals(Commands.EXPORT_TO_BIN_IMAGE)) {
	    performExport(mainMenu.exportAsBinImageMenuItem,
		    ExportFormat.BIN_IMAGE);
	} else if (command.equals(Commands.EXPORT_TO_CAR_IMAGE)) {
	    performExport(mainMenu.exportAsCarImageMenuItem,
		    ExportFormat.CAR_IMAGE);
	} else if (command.equals(Commands.EXPORT_TO_ATR_IMAGE)) {
	    performExport(mainMenu.exportAsAtrImageMenuItem,
		    ExportFormat.ATR_IMAGE);
	} else if (command.equals(Commands.EXPORT_TO_ATR_IMAGES)) {
	    performExport(mainMenu.exportAsAtrImagesMenuItem,
		    ExportFormat.ATR_IMAGES);
	} else if (command.equals(Commands.PRINT)) {
	    performPrint();
	} else if (command.equals(Commands.EXIT)) {
	    performShowConfirmSaveDialog(mainMenu.exitMenuItem,
		    Commands.EXIT_CONFIRMED);
	} else if (command.equals(Commands.EXIT_CONFIRMED)) {
	    performExitConfirmed();
	} else if (command.equals(Commands.ADD_ENTRIES)
		&& mainMenu.addEntriesMenuItem.isEnabled()) {
	    performAddEntries();
	} else if (command.equals(Commands.ADD_USER_SPACE_ENTRY)
		&& mainMenu.addUserSpaceEntryMenuItem.isEnabled()) {
	    performAddUserSpaceEntry();
	} else if (command.equals(Commands.REMOVE_ENTRIES)
		&& mainMenu.removeEntriesMenuItem.isEnabled()) {
	    performRemoveEntries();
	} else if (command.equals(Commands.SET_GENRE)
		&& mainMenu.setGenreMenuItem.isEnabled()) {
	    performSetGenre();
	} else if (command.equals(Commands.ASSIGN_NEW_BANKS)
		&& mainMenu.assignNewBanksMenuItem.isEnabled()) {
	    performAssignNewBanks();
	} else if (command.equals(Commands.WORKBOOK_OPTIONS)
		&& mainMenu.workbookOptionsMenuItem.isEnabled()) {
	    performWorkbookOptions();
	} else if (command.equals(Commands.PREVIEW)
		&& mainMenu.previewMenuItem.isEnabled()) {
	    performPreview();
	} else if (command.equals(Commands.TEST)) {
	    performTest();
	} else if (command.equals(Commands.OPTIONS)) {
	    performOptions();
	} else if (command.equals(Commands.HELP_CONTENTS)) {
	    performHelpDialog();
	} else if (command.equals(Commands.HELP_FILE_CONTENT_TYPES)) {
	    performFileTypesDialog();
	} else if (command.equals(Commands.HELP_ABOUT)) {
	    performAboutDialog();
	}
	dataToUI();
    }

    private void performHelpDialog() {
	if (helpDialog == null) {
	    helpDialog = new HelpDialog(mainWindowFrame,
		    "help/TheCartStudio.html", 826, 320, null);
	}

	helpDialog.show();
    }

    private void performFileTypesDialog() {
	if (contentTypesDialog == null) {
	    contentTypesDialog = new ContentTypesDialog(mainWindowFrame,
		    preferences);
	}

	contentTypesDialog.show();
    }

    private void performAboutDialog() {
	if (aboutDialog == null) {
	    aboutDialog = new AboutDialog(mainWindowFrame, cartridgeDatabase);
	}
	aboutDialog.show();
    }

    private boolean performNewFileConfirmed() {
	if (!workbook.create(messageQueue)) {
	    return false;
	}
	if (workbookOptionsDialog.showModal(workbook, messageQueue)) {
	    // INFO: Workbook created. Add entries via drag & drop or via the
	    // \"Edit/Add Entries...\" menu
	    sendMessage(Messages.I104);
	    return true;
	}
	workbook.close();
	return false;
    }

    private void performOpenFileConfirmed(File file) {
	if (file == null) {
	    workbookFileChooser
		    .setDialogTitle(Texts.WorkbookFileChooserDialog_Open_Title);
	    FileFilter filefilter = FileUtility.createFileExtensionFileFilter(
		    Workbook.FILE_EXTENSION,
		    Texts.WorkbookFileChooserDialog_FilterDescription);
	    workbookFileChooser.setFileFilter(filefilter);
	    workbookFileChooser.setCurrentDirectory(new File(preferences
		    .getLastWorkbookFolderPath()));
	    int option = workbookFileChooser.showOpenDialog(mainWindowFrame);
	    preferences.setLastWorkbookFolderPath(workbookFileChooser
		    .getCurrentDirectory().getAbsolutePath());
	    if (option != JFileChooser.APPROVE_OPTION) {
		return;
	    }

	    file = workbookFileChooser.getSelectedFile();
	}

	preferences.updateRecentWorkBookFileList(file);

	workbook.open(file, messageQueue);
	if (messageQueue.containsError()) {
	    return;
	}

	// INFO: Workbook opened. Add entries via drag & drop or via the
	// \"Edit/Add Entries...\" menu
	sendMessage(Messages.I105);

    }

    private void performOpenFolder() {
	File folder = workbook.getFile().getParentFile();
	try {
	    Desktop.openFile(folder);
	} catch (IOException ex) {
	    // ERROR: Cannot open folder '{0}': {1}.
	    sendMessage(com.wudsn.tools.base.Messages.E215,
		    folder.getAbsolutePath(), ex.getMessage());
	}
    }

    /**
     * Shows a confirmation to save the current project "Yes", "No" and
     * "Cancel". The method fires the <code>confirmationCommand</code> if the
     * operation shall continue, i.e. if the workbook is unchanged or if the
     * workbook is changed but the user selected "No" or if the workbook is
     * changed, the user selected "Yes" and saved it successfully.
     * 
     * @param menuItem
     *            The menu item providing the title, not <code>null</code>.
     * @param confirmationCommand
     *            The command to be fired upon confirmation, not empty, not
     *            <code>null</code>.
     */
    private void performShowConfirmSaveDialog(JMenuItem menuItem,
	    String confirmationCommand) {
	if (menuItem == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'menuItem' must not be null.");
	}
	if (confirmationCommand == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'confirmationCommand' must not be null.");
	}
	if (StringUtility.isEmpty(confirmationCommand)) {
	    throw new IllegalArgumentException(
		    "Parameter 'confirmationCommand' must not be empty.");
	}

	// Do not save if unchanged.
	Action yesAction = Actions.ButtonBar_Save;
	Action noAction = Actions.ButtonBar_DontSave;
	Action cancelAction = com.wudsn.tools.base.Actions.ButtonBar_Cancel;
	Action result = cancelAction;
	if (workbook.isChanged()) {
	    String message;
	    if (workbook.isPersistent()) {
		// INFO: Save changes to {0}?
		message = Messages.I102.format(workbook.getFile().getName());
	    } else {
		// INFO: Save new workbook?
		message = Messages.I101.format(workbook.getRoot().getTitle());
	    }

	    Action[] actions = new Action[] { yesAction, noAction, cancelAction };
	    result = StandardDialog.showConfirmation(mainWindowFrame,
		    menuItem.getToolTipText(), message, null, actions,
		    yesAction, cancelAction)[StandardDialog.BUTTON_ACTION];
	    if (result == cancelAction) {
		sendOperationCancelled(menuItem);
		return;
	    }
	    if (result == yesAction) {
		performSaveFile(confirmationCommand);
		return;
	    }
	}

	actionPerformed(new ActionEvent(this, 0, confirmationCommand));
    }

    /**
     * Saves the workbook using the "SaveAs" dialog if required.
     * 
     * @param confirmationCommand
     *            The command to be fired upon confirmation or <code>null</code>
     *            .
     */
    private void performSaveFile(String confirmationCommand) {
	if (!workbook.isPersistent()) {
	    performSaveFileAs(confirmationCommand);
	    return;
	}

	performSaveFile(workbook.getFile(), confirmationCommand);
    }

    /**
     * Saves the workbook using the "SaveAs" dialog if required.
     * 
     * @param file
     *            The file to save the workbook to, not <code>null</code>.
     * @param confirmationCommand
     *            The command to be fired upon confirmation or <code>null</code>
     *            .
     */
    private void performSaveFile(final File file,
	    final String confirmationCommand) {
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	@SuppressWarnings("synthetic-access")
	CommandWorker commandWorker = new CommandWorker() {

	    @Override
	    protected void performWork() throws Exception {
		workbook.save(file, messageQueue);
	    }

	    @Override
	    protected void performDone() {
		if (messageQueue.containsError()) {
		    return;
		}

		preferences.updateRecentWorkBookFileList(file);

		// INFO: "Workbook saved."
		sendMessage(Messages.I106);
		if (confirmationCommand != null) {
		    actionPerformed(new ActionEvent(this, 0,
			    confirmationCommand));
		}
	    }
	};
	commandWorker.start();
    }

    /**
     * Saves the workbook using the "SaveAs" dialog.
     * 
     * @param confirmationCommand
     *            The command to be fired upon confirmation or <code>null</code>
     *            .
     */
    private void performSaveFileAs(String confirmationCommand) {

	workbookFileChooser.setCurrentDirectory(new File(preferences
		.getLastWorkbookFolderPath()));

	// Propose file name with proper file extension when dialog is opened.
	File file = workbook.getFile();
	if (file == null) {
	    // Default to title.
	    file = new File(workbookFileChooser.getCurrentDirectory(), workbook
		    .getRoot().getTitle());
	}
	file = FileUtility
		.normalizeFileExtension(file, Workbook.FILE_EXTENSION);
	workbookFileChooser.setSelectedFile(file);

	workbookFileChooser
		.setDialogTitle(Texts.WorkbookFileChooserDialog_SaveAs_Title);
	FileFilter filefilter = FileUtility.createFileExtensionFileFilter(
		Workbook.FILE_EXTENSION,
		Texts.WorkbookFileChooserDialog_FilterDescription);
	workbookFileChooser.setFileFilter(filefilter);

	int option = workbookFileChooser.showSaveDialog(mainWindowFrame);
	preferences.setLastWorkbookFolderPath(workbookFileChooser
		.getCurrentDirectory().getAbsolutePath());
	if (option != JFileChooser.APPROVE_OPTION) {
	    sendOperationCancelled(mainMenu.saveAsMenuItem);
	    return;
	}
	file = workbookFileChooser.getSelectedFile();
	file = FileUtility
		.normalizeFileExtension(file, Workbook.FILE_EXTENSION);

	performSaveFile(file, confirmationCommand);
    }

    private void performCloseConfirmed() {
	workbook.close();
	// INFO: Workbook closed.
	sendMessage(Messages.I107);
	return;
    }

    private void performPrint() {
	AttributeTable table = workbookEntriesPanel.getTable();
	try {
	    String headerText = Messages.I117.format(workbook.getRoot()
		    .getTitle());
	    String subHeaderText = workbook.isPersistent() ? workbook.getFile()
		    .getAbsolutePath() : "";
	    String footerText = Messages.I118.format("{0}",
		    DateUtility.getCurrentDateTimeString());

	    if (table.print(headerText, subHeaderText, footerText)) {
		// INFO: Workbook printed.
		sendMessage(Messages.I116);
		return;
	    }
	    sendOperationCancelled(mainMenu.printMenuItem);
	} catch (PrinterException ex) {
	    // ERROR: Error while printing workbook: {0}
	    sendMessage(Messages.E115, ex.getMessage());
	    return;
	}
    }

    private void performExitConfirmed() {
	workbook.close();

	// Get the current state into the preferences.
	mainWindow.setPreferencesFromWindow(preferences);

	if (contentTypesDialog != null) {
	    contentTypesDialog.getTable().saveLayout();
	}
	workbookEntriesPanel.getTable().saveLayout();

	preferences.save(messageQueue);
	if (messageQueue.containsError()) {
	    StandardDialog.showErrorMessage(mainWindowFrame, messageQueue
		    .getFirstError().getMessageText(), Texts.MainWindow_Title);
	}
	System.exit(0);
	return;
    }

    private void performExport(final JMenuItem menuItem, final int exportFormat) {
	if (menuItem == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'menuItem' must not be null.");
	}

	@SuppressWarnings("synthetic-access")
	CommandWorker commandWorker = new CommandWorker() {

	    @Override
	    protected void performWork() throws Exception {
		WorkbookExport workbookExport;
		workbookExport = workbook.export(exportFormat, messageQueue);
		performExportToFile(menuItem, exportFormat, workbookExport);
	    }

	    @Override
	    protected void performDone() {	
	    }

	};
	commandWorker.start();

    }

    private void performExportToFile(JMenuItem menuItem, int exportFormat,
	    WorkbookExport workbookExport) {
	if (messageQueue.containsError() || workbookExport == null) {
	    return;
	}

	Exporter exporter = new Exporter(workbookExport, messageQueue);
	File file;
	String dialogTitle = menuItem.getText();

	switch (exportFormat) {

	case ExportFormat.BIN_IMAGE:
	    file = performSelectExportFile(exportFormat, dialogTitle);
	    if (file == null) {
		return;
	    }
	    exporter.exportAsBinImage(file);
	    break;

	case ExportFormat.CAR_IMAGE:
	    file = performSelectExportFile(exportFormat, dialogTitle);
	    if (file == null) {
		return;
	    }
	    exporter.exportAsCarImage(file);
	    break;
	case ExportFormat.ATR_IMAGE:
	    file = performSelectExportFile(exportFormat, dialogTitle);
	    if (file == null) {
		return;
	    }
	    exporter.exportAsAtrImage(file);
	    break;

	case ExportFormat.ATR_IMAGES:
	    file = performSelectExportFile(exportFormat, dialogTitle);
	    if (file == null) {
		return;
	    }
	    exporter.exportAsAtrImages(file);
	    break;
	default:
	    throw new RuntimeException("Unknown export format " + exportFormat
		    + ".");
	}
    }

    private File performSelectExportFile(int exportFormat, String dialogTitle) {
	exportFileChooser.setDialogTitle(dialogTitle);

	String fileExtension = ExportFormat.getFileExtension(exportFormat);
	String filterDescription = ExportFormat
		.getFileFilterDescription(exportFormat);
	FileFilter filefilter = FileUtility.createFileExtensionFileFilter(
		fileExtension, filterDescription);

	// Propose file name with proper file extension when dialog is opened.
	File file = exportFileChooser.getSelectedFile();
	if (file == null) {
	    // Devices which use 16MB images can typically only display 8.3 file
	    // names.
	    // Therefore we default to a short enough name.
	    if (exportFormat == ExportFormat.ATR_IMAGES) {
		file = new File("Part");
	    } else {
		file = workbook.getFile();
		if (file == null) {
		    // Default to title.
		    file = new File(workbook.getRoot().getTitle());
		}
	    }
	}
	file = FileUtility.normalizeFileExtension(file, fileExtension);
	exportFileChooser.setSelectedFile(file);
	exportFileChooser.setFileFilter(filefilter);
	exportFileChooser.setCurrentDirectory(new File(preferences
		.getLastExportFolderPath()));

	int option = exportFileChooser.showSaveDialog(mainWindowFrame);
	preferences.setLastExportFolderPath(exportFileChooser
		.getCurrentDirectory().getAbsolutePath());
	if (option != JFileChooser.APPROVE_OPTION) {

	    // INFO: Operation '{0}' cancelled
	    sendMessage(Messages.I103, dialogTitle);
	    return null;
	}

	// Ensure the correct file extension after choosing the file.
	file = exportFileChooser.getSelectedFile();
	file = FileUtility.normalizeFileExtension(file, fileExtension);

	return file;
    }

    private void performAddEntries() {
	entriesFileChooser
		.setDialogTitle(Texts.EntriesFileChooserDialog_AddEntries_Title);
	entriesFileChooser.setCurrentDirectory(new File(preferences
		.getLastEntryFolderPath()));
	int option = entriesFileChooser.showOpenDialog(mainWindowFrame);
	preferences.setLastEntryFolderPath(entriesFileChooser
		.getCurrentDirectory().getAbsolutePath());
	if (option != JFileChooser.APPROVE_OPTION) {
	    return;
	}
	File[] selectedFiles = entriesFileChooser.getSelectedFiles();
	performAddEntries(selectedFiles);

    }

    private void performAddEntries(final File[] files) {
	if (files == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'files' must not be null.");
	}

	// Make sure we display the right tab.
	tabbedPane.setSelectedComponent(workbookEntriesPanel);

	final WorkbookAddEntriesCallback callback = new WorkbookAddEntriesCallback() {

	    @Override
	    public int confirmAdd(String existingEntryTitle,
		    String existingFileName, String newFilePath,
		    String renamedFileName) {
		if (addAll) {
		    return lastAddResult;
		}

		Action overwriteAction = Actions.ButtonBar_Overwrite;
		Action renameAction = new Action(TextUtility.format(
			Actions.ButtonBar_Rename.getLabel(), renamedFileName),
			Actions.ButtonBar_Rename.getToolTip(), null);
		Action skipAction = Actions.ButtonBar_Skip;

		Action yesAllAction = com.wudsn.tools.base.Actions.ButtonBar_YesAll;
		Action yesAction = com.wudsn.tools.base.Actions.ButtonBar_Yes;
		Action cancelAction = com.wudsn.tools.base.Actions.ButtonBar_Cancel;

		String message = Messages.I114.format(existingEntryTitle,
			existingFileName, newFilePath);
		Action[] result = StandardDialog.showConfirmation(
			mainWindowFrame, mainMenu.addEntriesMenuItem.getText(),
			message, new Action[] { overwriteAction, renameAction,
				skipAction }, new Action[] { yesAllAction,
				yesAction, cancelAction }, overwriteAction,
			cancelAction);
		Action choiceAction = result[StandardDialog.CHOICE_ACTION];
		Action buttonAction = result[StandardDialog.BUTTON_ACTION];
		if (choiceAction == overwriteAction) {
		    lastAddResult = AddResult.OVERWRITE;
		} else if (choiceAction == renameAction) {
		    lastAddResult = AddResult.RENAME;
		} else {
		    lastAddResult = AddResult.SKIP;
		}
		if (buttonAction == yesAllAction) {
		    addAll = true;
		} else if (buttonAction == cancelAction) {
		    lastAddResult = AddResult.CANCEL;
		    addAll = true;
		}
		return lastAddResult;
	    }

	    @Override
	    public int confirmUseTitleFromCartridgeDatabase(
		    String entryFileName, String cartridgeDatabaseEntryTitle) {
		if (useTitleAll) {
		    return lastUseTitleResult;
		}

		Action yesAllAction = com.wudsn.tools.base.Actions.ButtonBar_YesAll;
		Action yesAction = com.wudsn.tools.base.Actions.ButtonBar_Yes;
		Action noAction = com.wudsn.tools.base.Actions.ButtonBar_No;
		Action noAllAction = com.wudsn.tools.base.Actions.ButtonBar_NoAll;
		Action cancelAction = com.wudsn.tools.base.Actions.ButtonBar_Cancel;

		String message = Messages.I138.format(
			cartridgeDatabaseEntryTitle, entryFileName);
		Action[] result = StandardDialog.showConfirmation(
			mainWindowFrame, mainMenu.addEntriesMenuItem.getText(),
			message, null, new Action[] { yesAllAction, yesAction,
				noAction, noAllAction, cancelAction },
			yesAllAction, cancelAction);
		Action buttonAction = result[StandardDialog.BUTTON_ACTION];
		if (buttonAction == yesAction || buttonAction == yesAllAction) {
		    lastUseTitleResult = UseTitleResult.YES;
		    useTitleAll = (buttonAction == yesAllAction);
		} else if (buttonAction == noAction
			|| buttonAction == noAllAction) {
		    lastUseTitleResult = UseTitleResult.NO;
		    useTitleAll = (buttonAction == noAllAction);
		} else if (buttonAction == cancelAction) {
		    lastUseTitleResult = UseTitleResult.CANCEL;
		    useTitleAll = true;
		}
		return lastUseTitleResult;
	    }

	};

	@SuppressWarnings("synthetic-access")
	CommandWorker commandWorker = new CommandWorker() {
	    private WorkbookEntry firstEntry;

	    @Override
	    protected void performWork() throws Exception {
		firstEntry = workbook.addEntries(files, callback, messageQueue);
	    }

	    @Override
	    protected void performDone() {
		if (!messageQueue.containsError()) {
		    // INFO: {0} entries added, {1} files updated and {2} files
		    // skipped.
		    messageQueue.sendMessage(workbook, null, Messages.I108,
			    TextUtility.formatAsDecimal(callback
				    .getAddedEntriesCount()), TextUtility
				    .formatAsDecimal(callback
					    .getUpdatesEntriesCount()),
			    TextUtility.formatAsDecimal(callback
				    .getSkippedEntriesCount()));
		    if (firstEntry != null) {
			performShowWorkbookEntry(firstEntry,
				WorkbookEntry.Attributes.ID);
		    }
		    if (callback.isCancelled()) {
			sendOperationCancelled(mainMenu.addEntriesMenuItem);
		    }
		}
	    }
	};
	messageQueue.clear();
	commandWorker.start();

    }

    private void performAddUserSpaceEntry() {
	// Make sure we display the right tab.
	tabbedPane.setSelectedComponent(workbookEntriesPanel);

	// Make the content type and the start bank visible.
	workbookEntriesPanel.getTable().setColumnVisible(
		WorkbookEntriesPanel.TableModel.Columns.CONTENT_TYPE, true);
	workbookEntriesPanel.getTable().setColumnVisible(
		WorkbookEntriesPanel.TableModel.Columns.START_BANK, true);

	WorkbookEntry firstEntry = workbook.addUserSpaceEntry();
	// INFO: User space entry added. Specify the content type and the start
	// bank.
	messageQueue.sendMessage(workbook, null, Messages.I132);
	performShowWorkbookEntry(firstEntry, WorkbookEntry.Attributes.ID);
    }

    @Override
    public boolean isDropAllowed() {
	return mainWindowReady;
    }

    @Override
    public void filesDropped(File[] files) {
	if (files == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'files' must not be null.");
	}
	if (files.length == 0) {
	    return;
	}
	dataFromUI();
	if (!workbook.isValid()) {
	    if (!performNewFileConfirmed()) {
		return;
	    }
	}

	performAddEntries(files);

	// Use folder of last dropped files as new last entry folder path.
	preferences.setLastEntryFolderPath(files[files.length - 1]
		.getParentFile().getAbsolutePath());
    }

    private void performRemoveEntries() {
	workbookEntriesPanel.removeSelectedWorkbookEntries(messageQueue);
    }

    public void performShowWorkbookEntry(WorkbookEntry workbookEntry,
	    Attribute attribute) {
	if (workbookEntry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbookEntry' must not be null.");
	}
	tabbedPane.setSelectedComponent(workbookEntriesPanel);
	workbookEntriesPanel.setSelectedWorkbookEntry(workbookEntry, attribute);

    }

    public void performSetGenre() {

	if (workbookEntriesPanel.getTable().getSelectedRowCount() == 0) {
	    // ERROR: No entries selected. Select one or more entries and repeat
	    // the action '{0}'.
	    sendMessage(Messages.E129, mainMenu.setGenreMenuItem.getText());
	    return;
	}

	List<String> genreNames = new ArrayList<String>(workbook.getRoot()
		.getSortedGenreNamesList());
	String none = Texts.GenreName_None;
	genreNames.add(0, none);

	// INFO: Choose the new genre for the select entries.
	String genreName = (String) JOptionPane.showInputDialog(
		mainWindowFrame, Messages.I119.getShortText(),
		mainMenu.setGenreMenuItem.getText(), JOptionPane.PLAIN_MESSAGE,
		null, genreNames.toArray(), none);

	// If a string was returned, say so.
	if ((genreName == null) || StringUtility.isEmpty(genreName)) {
	    sendOperationCancelled(mainMenu.setGenreMenuItem);
	    return;

	}

	if (genreName.equals(none)) {
	    genreName = "";
	}
	workbookEntriesPanel.setSelectedWorkbookEntriesGenre(genreName,
		messageQueue);

    }

    private void performAssignNewBanks() {
	// Ask only if the CartridgeType supports incremental flashing,
	if (workbook.getRoot().getCartridgeType()
		.isIncrementalFlashingSupported()) {
	    // INFO: Reassigning the entries to banks can free space...
	    Action yesAction = com.wudsn.tools.base.Actions.ButtonBar_Yes;
	    Action noAction = com.wudsn.tools.base.Actions.ButtonBar_No;
	    Action result = StandardDialog.showConfirmation(mainWindowFrame,
		    mainMenu.assignNewBanksMenuItem.getText(),
		    Messages.I124.getShortText(), null, new Action[] {
			    yesAction, noAction }, yesAction, noAction)[StandardDialog.BUTTON_ACTION];
	    if (result != yesAction) {
		sendOperationCancelled(mainMenu.assignNewBanksMenuItem);
		return;
	    }
	}
	workbook.assignNewBanks(messageQueue);
	if (!messageQueue.containsError()) {
	    // INFO: All entries have been reassigned successfully.
	    sendMessage(Messages.I125);
	}

    }

    private void performWorkbookOptions() {
	workbookOptionsDialog.showModal(workbook, messageQueue);
    }

    private void performPreview() {
	@SuppressWarnings("synthetic-access")
	CommandWorker commandWorker = new CommandWorker() {
	    private WorkbookExport workbookExport;

	    @Override
	    protected void performWork() throws Exception {
		workbookExport = workbook.export(ExportFormat.CAR_IMAGE,
			messageQueue);
		if (messageQueue.containsError()) {
		    return;
		}
		File tempFolder = new File(System.getProperty("java.io.tmpdir"));
		File file = new File(tempFolder, "TheCartStudio-Preview.car");
		Exporter exporter = new Exporter(workbookExport, messageQueue);
		exporter.exportAsCarImage(file);
		if (messageQueue.containsError()) {
		    return;
		}

		// Try to open the .CAR file with the OS default program first.
		// This way, no configuration in the options is required at
		// first.
		String emulatorExecutablePath = preferences
			.getEmulatorExecutablePath();
		if (StringUtility.isEmpty(emulatorExecutablePath)) {
		    try {
			Desktop.openFile(file);
			return;
		    } catch (IOException ex) {
			// TODO: JDK bug, exception does not occur as expected.
			// Reported as JDK bug on 2018-06-06
			// https://bugs.openjdk.java.net/browse/JDK-8204537
		    }
		}
		while (StringUtility.isEmpty(emulatorExecutablePath)
			|| new File(emulatorExecutablePath).canExecute() == false) {
		    FileChooser fileChooser = FileChooser.createInstance();
		    fileChooser
			    .setDialogTitle(Texts.OptionsEmulatorExecutablePathDialog_Title);
		    int result = fileChooser.showOpenDialog(mainWindowFrame);
		    if (result == JFileChooser.CANCEL_OPTION) {
			// ERROR: Set the path to the emulator executable in the
			// options to enable the preview.
			messageQueue.sendMessage(workbook, null, Messages.E428);
			return;
		    }
		    emulatorExecutablePath = fileChooser.getSelectedFile()
			    .getAbsolutePath();
		}
		preferences.setEmulatorExecutablePath(emulatorExecutablePath);

		try {
		    // Start the emulator with its own directory as working
		    // directory to simplify usage of "atari800.exe".
		    Runtime.getRuntime().exec(
			    new String[] { emulatorExecutablePath,
				    file.getAbsolutePath() }, null,
			    new File(emulatorExecutablePath).getParentFile());

		} catch (IOException ex) {
		    String message = ex.getMessage();
		    // Error while executing the emulator: {0}
		    messageQueue.sendMessage(workbook, null, Messages.E429,
			    message);
		    return;
		}

	    }

	    @Override
	    protected void performDone() {

	    }

	};
	commandWorker.start();
    }

    private static void performTest() {

	System.out.println(new Date());
    }

    private void performOptions() {
	Locale oldLocale = preferences.getLocale();
	optionsDialog.showModal(preferences);
	// Re-create UI if locale changed effectively.
	if (!oldLocale.equals(preferences.getLocale())) {
	    mainWindowReady = false;
	    mainWindow.dispose();
	    createUI();
	    mainWindowReady = true;
	}
    }

    private void sendMessage(Message message, String... parameters) {
	if (message == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'message' must not be null.");
	}
	messageQueue.sendMessage(this, null, message, parameters);
    }

    private void sendOperationCancelled(JMenuItem menuItem) {
	if (menuItem == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'menuItem' must not be null.");
	}
	// INFO: Operation '{0}' cancelled
	sendMessage(Messages.I103, menuItem.getText());
    }

    private void dataFromUI() {
	workbookEntriesPanel.dataFromUI();
    }

    private void dataToUI() {
	boolean valid = workbook.isValid();
	boolean persistent = workbook.isPersistent();
	boolean entriesVisible = tabbedPane.getSelectedComponent() == workbookEntriesPanel;
	boolean banksVisible = tabbedPane.getSelectedComponent() == workbookBanksPanel;
	WorkbookRoot root = workbook.getRoot();
	boolean hasEntries = root.getEntryCount() > 0;

	String title;
	if (valid) {
	    if (persistent) {
		title = TextUtility.format(Texts.MainWindow_Title_Persistent,
			root.getTitle(), workbook.getFile().getAbsolutePath());
	    } else {
		title = TextUtility.format(Texts.MainWindow_Title_New,
			root.getTitle());
	    }
	} else {
	    title = Texts.MainWindow_Title;
	}
	mainWindowFrame.setTitle(title);

	// Menu: File
	mainMenu.newMenuItem.setEnabled(mainWindowReady);
	mainMenu.openMenuItem.setEnabled(mainWindowReady);
	mainMenu.openFolderMenuItem.setEnabled(mainWindowReady && valid
		&& persistent);
	mainMenu.saveMenuItem.setEnabled(mainWindowReady && valid);
	mainMenu.saveAsMenuItem.setEnabled(mainWindowReady && valid
		&& persistent);
	mainMenu.closeMenuItem.setEnabled(mainWindowReady && valid);
	mainMenu.printMenuItem.setEnabled(mainWindowReady && valid
		&& hasEntries);
	mainMenu.exitMenuItem.setEnabled(mainWindowReady);

	// Menu: Edit
	boolean exportEnabled = mainWindowReady && valid
		&& root.getEntryCount() > 0;
	FlashTargetType flashTargetType = root.getFlashTargetType();
	mainMenu.exportMenu.setEnabled(exportEnabled);
	mainMenu.exportAsBinImageMenuItem.setEnabled(exportEnabled
		&& flashTargetType
			.isExportFormatSupported(ExportFormat.BIN_IMAGE));
	mainMenu.exportAsCarImageMenuItem.setEnabled(exportEnabled
		&& flashTargetType
			.isExportFormatSupported(ExportFormat.CAR_IMAGE)
		&& root.getCartridgeType() != CartridgeType.UNKNOWN);
	mainMenu.exportAsAtrImageMenuItem.setEnabled(exportEnabled
		&& flashTargetType
			.isExportFormatSupported(ExportFormat.ATR_IMAGE));
	mainMenu.exportAsAtrImagesMenuItem.setEnabled(exportEnabled
		&& flashTargetType
			.isExportFormatSupported(ExportFormat.ATR_IMAGES));

	mainMenu.addEntriesMenuItem.setEnabled(mainWindowReady && valid);
	mainMenu.addUserSpaceEntryMenuItem.setEnabled(mainWindowReady && valid
		&& root.getUserSpaceSize() > 0);
	mainMenu.removeEntriesMenuItem.setEnabled(mainWindowReady && valid
		&& entriesVisible && hasEntries);
	mainMenu.setGenreMenuItem.setEnabled(mainWindowReady && valid
		&& entriesVisible && hasEntries);
	mainMenu.assignNewBanksMenuItem.setEnabled(mainWindowReady && valid
		&& hasEntries);
	mainMenu.workbookOptionsMenuItem.setEnabled(mainWindowReady && valid);

	// Menu: Tools
	mainMenu.previewMenuItem.setEnabled(mainWindowReady && valid
		&& hasEntries);
	mainMenu.optionsMenuItem.setEnabled(mainWindowReady);

	// Menu: Help - has amodal dialogs only
	workbookEntriesPanel.dataToUI();
	if (banksVisible) {
	    workbookBanksPanel.repaint();
	}
	workbookBanksSummaryPanel.repaint();

	statusBar.displayMessageQueue(messageQueue);
	MessageQueueEntry messageQueueEntry = messageQueue.getFirstError();
	if (messageQueueEntry == null) {
	    messageQueueEntry = messageQueue.getFirstInfo();
	}
	if (messageQueue.areEntriesChanged() && messageQueueEntry != null) {
	    Object owner = messageQueueEntry.getOwner();
	    if (owner instanceof WorkbookRoot) {
		performWorkbookOptions();
		messageQueue.clear();
		statusBar.displayMessageQueue(messageQueue);
	    } else if (owner instanceof WorkbookEntry) {
		if (!entriesVisible) {
		    tabbedPane.setSelectedComponent(workbookEntriesPanel);
		}
		workbookEntriesPanel
			.setSelectedWorkbookEntry((WorkbookEntry) owner,
				messageQueueEntry.getAttribute());
	    }
	}

    }
}