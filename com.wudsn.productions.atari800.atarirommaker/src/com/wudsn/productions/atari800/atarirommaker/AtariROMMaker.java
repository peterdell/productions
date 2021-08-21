/**
 * Copyright (C) 2015 - 2016 <a href="https://www.wudsn.com" target="_top">Peter Dell</a>
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

package com.wudsn.productions.atari800.atarirommaker;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.wudsn.productions.atari800.atarirommaker.model.CartridgeTypeWrapper;
import com.wudsn.productions.atari800.atarirommaker.model.ROM;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.Application;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.Desktop;
import com.wudsn.tools.base.gui.ElementFactory;
import com.wudsn.tools.base.gui.FileDrop;
import com.wudsn.tools.base.gui.MainWindow;
import com.wudsn.tools.base.gui.StandardDialog;
import com.wudsn.tools.base.repository.Action;
import com.wudsn.tools.base.repository.Message;

/**
 * Atari ROM Maker by JAC!
 * 
 * @author Peter Dell
 */

public final class AtariROMMaker implements FileDrop.Listener {

	public final class Commands {
		public static final String LOAD = "load";
		public static final String CONVERT_TO_CAR = "convertToCAR";
		public static final String CONVERT_TO_ROM = "convertToROM";
		public static final String SAVE = "save";
	}

	// Constants
	static final int FOLDER_MODE_UNDEFINED = 0;
	static final int FOLDER_MODE_YES = 1;
	static final int FOLDER_MODE_NO = 2;

	// Static instance
	static AtariROMMaker instance;

	// Message queue
	private MessageQueue messageQueue;
	private ROM rom;

	// UI
	private MainWindow mainWindow;
	private JFrame mainWindowFrame;
	private JLabel logLabel;
	private String logText;

	// Main window: Drag & drop
	@SuppressWarnings("unused")
	private FileDrop fileDrop;

	private boolean cancelled;

	public static void main(final String[] args) {
		if (args == null) {
			throw new IllegalArgumentException("Parameter 'args' must not be null.");
		}

		// Use the event dispatch thread for Swing components
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				Application.createInstance("https://www.wudsn.com/productions/atari800/atarirommaker/atarirommaker.zip",
						"AtariROMMaker.jar", AtariROMMaker.class);
				instance = new AtariROMMaker();
				instance.run(args);
			}
		});
	}

	AtariROMMaker() {
	}

	void run(String[] args) {
		if (args == null) {
			throw new IllegalArgumentException("Parameter 'args' must not be null.");
		}

		messageQueue = new MessageQueue();
		rom = new ROM();

		// Handle command line.
		AtariROMMakerConsole console = new AtariROMMakerConsole();
		if (console.runConsoleCommands(args, false, rom, messageQueue)) {
			return;
		}

		// There was no command line, so start UI.
		createUI();
	}

	private void createUI() {

		mainWindow = new MainWindow();
		mainWindowFrame = mainWindow.getFrame();

		// Make sure the program exits when the frame is closed.
		mainWindowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		mainWindowFrame
				.setTitle(TextUtility.format(Texts.MainWindow_Title, Application.getInstance().getLocalVersion()));
		ImageIcon imageIcon = ElementFactory.createImageIcon("icons/main-16x16.png");
		mainWindowFrame.setIconImage(imageIcon.getImage());
		mainWindowFrame.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		Border labelBorder = new EmptyBorder(0, 5, 5, 5);

		String content = Texts.MainWindow_Text;
		JLabel contentLabel = new JLabel(content);
		contentLabel.setBorder(labelBorder);
		topPanel.add(contentLabel, BorderLayout.NORTH);

		String link = Texts.MainWindow_Link.replace("$url$", Texts.MainWindow_URL);
		JLabel linkLabel = new JLabel(link);
		linkLabel.setBorder(labelBorder);
		linkLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					Desktop.openBrowser(Texts.MainWindow_URL);
				}
			}
		});
		topPanel.add(linkLabel, BorderLayout.SOUTH);

		mainWindowFrame.add(topPanel, BorderLayout.NORTH);
		logLabel = new JLabel();
		logLabel.setBorder(labelBorder);
		logLabel.setVerticalAlignment(SwingConstants.TOP);

		mainWindowFrame.add(new JScrollPane(logLabel), BorderLayout.CENTER);

		// Make main window visible and register global drag & drop.
		mainWindowFrame.pack();
		mainWindowFrame.setLocationRelativeTo(null);
		mainWindowFrame.setVisible(true);

		fileDrop = new FileDrop(mainWindowFrame, true, this);

	}

	@Override
	public boolean isDropAllowed() {
		return true;
	}

	@Override
	public void filesDropped(File[] files) {
		if (files == null) {
			throw new IllegalArgumentException("Parameter 'files' must not be null.");
		}
		clearLog();
		cancelled = false;
		convertToCAR(files, FOLDER_MODE_UNDEFINED);
	}

	private void convertToCAR(File[] files, int folderMode) {
		if (files == null || files.length == 0) {
			return;
		}
		if (cancelled) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			convertToCAR(files[i], folderMode);
		}
	}

	private void convertFolderToCAR(File sourceFolder, int folderMode) {
		if (sourceFolder == null) {
			throw new IllegalArgumentException("Parameter 'sourceFolder' must not be null.");
		}

		File[] files = sourceFolder.listFiles();
		if (files == null) {
			files = new File[0];
		}
		// INFO: Converting {0} sub-folders and files in '{1}'.
		logInfo(Messages.I204, TextUtility.formatAsDecimal(files.length), sourceFolder.getAbsolutePath());
		convertToCAR(files, folderMode);

	}

	private void convertToCAR(File sourceFile, int folderMode) {
		if (sourceFile == null) {
			throw new IllegalArgumentException("Parameter 'sourceFile' must not be null.");
		}
		if (cancelled) {
			return;
		}
		if (sourceFile.isDirectory()) {

			switch (folderMode) {

			case FOLDER_MODE_UNDEFINED:
				Action yesAction = com.wudsn.tools.base.Actions.ButtonBar_Yes;
				Action noAction = com.wudsn.tools.base.Actions.ButtonBar_No;
				Action result = StandardDialog.showConfirmation(mainWindowFrame,
						TextUtility.format(Texts.FolderDialog_Title, sourceFile.getAbsolutePath()),
						Texts.FolderDialog_Action, null, new Action[] { yesAction, noAction }, yesAction,
						noAction)[StandardDialog.BUTTON_ACTION];
				if (result == yesAction) {
					folderMode = FOLDER_MODE_YES;
					convertFolderToCAR(sourceFile, FOLDER_MODE_YES);
				} else {
					// INFO: Skipping folder {1}'.
					logInfo(Messages.I205, sourceFile.getAbsolutePath());
				}
				return;

			case FOLDER_MODE_YES:
				convertFolderToCAR(sourceFile, folderMode);
				return;

			case FOLDER_MODE_NO:
				return;
			}
		}

		long fileSize = sourceFile.length();

		// INFO: Opening ROM image file '{0}'.
		logInfo(Messages.I200, sourceFile.getAbsolutePath());
		messageQueue.clear();
		rom.load(sourceFile, messageQueue);
		if (messageQueue.containsError()) {
			logError(messageQueue.getFirstError().getMessage());
			return;
		}

		if (rom.isValidCAR()) {
			// INFO: File is already a valid cartridge image file.
			logInfo(Messages.I206);
			return;
		}
		List<CartridgeTypeWrapper> possibleCartridgeTypes = new ArrayList<CartridgeTypeWrapper>();
		for (CartridgeType cartridgeType : CartridgeType.getValues()) {
			if (cartridgeType.getSizeInKB() * 1024 == fileSize) {
				possibleCartridgeTypes.add(new CartridgeTypeWrapper(cartridgeType));
			}
		}
		if (possibleCartridgeTypes.isEmpty()) {
			// ERROR: ROM image file size of {0} does not match any supported
			// cartridge type.
			logError(Messages.E201, TextUtility.formatAsMemorySize(fileSize));
			return;

		}

		CartridgeTypeWrapper cartridgeType;
		if (possibleCartridgeTypes.size() == 1) {
			cartridgeType = possibleCartridgeTypes.get(0);
		} else {

			CartridgeTypeWrapper[] cartridgeTypesArray = possibleCartridgeTypes
					.toArray(new CartridgeTypeWrapper[possibleCartridgeTypes.size()]);
			String title = TextUtility.format(Texts.CartridgeTypeDialog_Title, sourceFile.getName());
			String action = Texts.CartridgeTypeDialog_Action;
			cartridgeType = (CartridgeTypeWrapper) JOptionPane.showInputDialog(null, action, title,
					JOptionPane.QUESTION_MESSAGE, null, // Use default icon
					cartridgeTypesArray, // Array of choices
					cartridgeTypesArray[1]); // Initial choice
			if (cartridgeType == null) {
				cancelled = true;
				// ERROR: Conversion cancelled by user
				logError(Messages.E202);
				return;
			}
		}

		rom.convertToCAR(cartridgeType.GetCartridgeType(), messageQueue);
		if (messageQueue.containsError()) {
			logError(messageQueue.getFirstError().getMessage());
			return;
		}
		File destinationFile = FileUtility.normalizeFileExtension(sourceFile, ".car");
		logInfo(Messages.I203, destinationFile.getAbsolutePath(), cartridgeType.toString());

		rom.save(destinationFile, messageQueue);
		if (messageQueue.containsError()) {
			logError(messageQueue.getFirstError().getMessage());
			return;
		}
	}

	private void clearLog() {
		logText = "";
		logLabel.setText("");
	}

	private void logInternal(String line) {
		logText = logText + line + "<br>";
		logLabel.setText("<html>" + logText + "</html>");
		mainWindowFrame.pack();
	}

	private void logInfo(Message message, String... parameters) {
		if (message == null) {
			throw new IllegalArgumentException("Parameter 'message' must not be null.");
		}
		logInternal(message.format(parameters));
	}

	private void logError(Message message, String... parameters) {
		if (message == null) {
			throw new IllegalArgumentException("Parameter 'message' must not be null.");
		}
		logInternal("<font color='red'>" + message.format(parameters) + "</font>");
	}

}