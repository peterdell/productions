/**
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

package com.wudsn.tools.thecartstudio.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.wudsn.tools.base.common.DateUtility;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.XMLUtility;
import com.wudsn.tools.base.gui.AttributeTablePreferences;
import com.wudsn.tools.base.gui.MainWindowPreferences;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.thecartstudio.DataTypes;

/**
 * Persistent preferences.
 * 
 * @author Peter Dell
 * 
 */
public final class Preferences implements MainWindowPreferences,
		AttributeTablePreferences {

	private static final String FOLDER_NAME = ".tcs";
	private static final String FILE_NAME = "preferences.xml";

	public static final int MAX_HISTORY_SIZE = 5;

	private static final class XMLElements {
		public static final String PREFERENCES = "preferences";
		public static final String RECENT_WORKBOOK = "recentWorkoook";
		public static final String LAYOUT_PROPERTIES = "layoutProperties";
	}

	private static final class Attributes {
		// AttributeTablePreferences
		public static final Attribute LANGUAGE = new Attribute("language",
				DataTypes.Preferences_Language);
		public static final Attribute UPDATE_CHECK_INDICATOR = new Attribute(
				"updateCheckIndicator",
				DataTypes.Preferences_UpdateCheckIndicator);
		public static final Attribute EMULATOR_EXECUTABLE_PATH = new Attribute(
				"emulatorExecuablePath",
				DataTypes.Preferences_EmulatorExecutablePath);
		public static final Attribute FREE_BANK_COLOR = new Attribute(
				"freeBankColor", DataTypes.Preferences_FreeBankColor);
		public static final Attribute RESERVED_BANK_COLOR = new Attribute(
				"reservedBankColor");
		public static final Attribute RESERVED_USER_SPACE_BANK_COLOR = new Attribute(
				"reservedUserSpaceBankColor");
		public static final Attribute USED_ODD_BANK_COLOR = new Attribute(
				"usedOddBankColor");
		public static final Attribute USED_EVEN_BANK_COLOR = new Attribute(
				"usedEvenBankColor");
		public static final Attribute CONFLICT_BANK_COLOR = new Attribute(
				"conflictBankColor");

		public static final Attribute LAST_WORKBOOK_FOLDER_PATH = new Attribute(
				"lastWorkbookFolderPath");
		public static final Attribute LAST_ENTRY_FOLDER_PATH = new Attribute(
				"lastEntryFolderPath");
		public static final Attribute LAST_EXPORT_FOLDER_PATH = new Attribute(
				"lastExportFolderPath");

		public static final Attribute MAIN_WINDOW_EXTENDED_STATE = new Attribute(
				"mainWindowExtendedState");
		public static final Attribute MAIN_WINDOW_LOCATION = new Attribute(
				"mainWindowLocation");
		public static final Attribute MAIN_WINDOW_SIZE = new Attribute(
				"mainWindowSize");

		// Recent Workbook
		public static final Attribute FILE_PATH = new Attribute("filePath");

		// Layout properties
		public static final Attribute PROPERTY_KEY = new Attribute("key");
		public static final Attribute PROPERTY_VALUE = new Attribute("value");
	}

	private static final class XMLHandler extends
			com.wudsn.tools.base.common.XMLHandler {

		private Preferences preferences;

		public XMLHandler(Preferences preferences) {
			if (preferences == null) {
				throw new IllegalArgumentException(
						"Parameter 'preferences' must not be null.");
			}
			this.preferences = preferences;
		}

		@Override
		public void startOpen(MessageQueue messageQueue) {
			if (messageQueue == null) {
				throw new IllegalArgumentException(
						"Parameter 'messageQueue' must not be null.");
			}
			// Keep paths, but don't keep history.
			preferences.recentWorkbookFilePathsList.clear();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				org.xml.sax.Attributes attributes) throws SAXException {
			if (qName == null) {
				throw new IllegalArgumentException(
						"Parameter 'qName' must not be null.");
			}
			if (attributes == null) {
				throw new IllegalArgumentException(
						"Parameter 'attributes' must not be null.");
			}
			String value;
			if (qName.equals(XMLElements.PREFERENCES)) {

				Language language = Attributes.LANGUAGE.deserializeValueSet(
						attributes, Language.class);
				if (language != null) {
					preferences.setLanguage(language);
				}

				preferences
						.setUpdateCheckIndicator(Attributes.UPDATE_CHECK_INDICATOR
								.deserializeBoolean(attributes,
										preferences.getUpdateCheckIndicator()));

				value = Attributes.EMULATOR_EXECUTABLE_PATH
						.deserializeString(attributes);
				if (value != null) {
					preferences.setEmulatorExecutablePath(value);
				}

				Color color = Attributes.FREE_BANK_COLOR
						.deserializeColor(attributes);
				if (color != null) {
					preferences.setFreeBankColor(color);
				}
				color = Attributes.RESERVED_BANK_COLOR
						.deserializeColor(attributes);
				if (color != null) {
					preferences.setReservedBankColor(color);
				}
				color = Attributes.RESERVED_USER_SPACE_BANK_COLOR
						.deserializeColor(attributes);
				if (color != null) {
					preferences.setReservedUserSpaceBankColor(color);
				}
				color = Attributes.USED_ODD_BANK_COLOR
						.deserializeColor(attributes);
				if (color != null) {
					preferences.setUsedOddBankColor(color);
				}
				color = Attributes.USED_EVEN_BANK_COLOR
						.deserializeColor(attributes);
				if (color != null) {
					preferences.setUsedEvenBankColor(color);
				}
				color = Attributes.CONFLICT_BANK_COLOR
						.deserializeColor(attributes);
				if (color != null) {
					preferences.setConflictBankColor(color);
				}

				int mainWindowExtendedState = Attributes.MAIN_WINDOW_EXTENDED_STATE
						.deserializeInteger(attributes);
				preferences.setMainWindowExtendedState(mainWindowExtendedState);

				Point mainWindowLocation = Attributes.MAIN_WINDOW_LOCATION
						.deserializePoint(attributes);
				if (mainWindowLocation != null) {
					preferences.setMainWindowLocation(mainWindowLocation);
				}

				Dimension mainWindowSize = Attributes.MAIN_WINDOW_SIZE
						.deserializeDimension(attributes);
				if (mainWindowSize != null) {
					preferences.setMainWindowSize(mainWindowSize);
				}

				value = Attributes.LAST_WORKBOOK_FOLDER_PATH
						.deserializeString(attributes);
				if (value != null) {
					preferences.setLastWorkbookFolderPath(value);
				}

				value = Attributes.LAST_ENTRY_FOLDER_PATH
						.deserializeString(attributes);
				if (value != null) {
					preferences.setLastEntryFolderPath(value);
				}

				value = Attributes.LAST_EXPORT_FOLDER_PATH
						.deserializeString(attributes);
				if (value != null) {
					preferences.setLastExportFolderPath(value);

				}

			} else if (qName.equals(XMLElements.RECENT_WORKBOOK)) {
				List<String> recentWorkBookFilePathsList = preferences.recentWorkbookFilePathsList;
				value = Attributes.FILE_PATH.deserializeString(attributes);
				if (value != null
						&& recentWorkBookFilePathsList.size() < MAX_HISTORY_SIZE) {
					recentWorkBookFilePathsList.add(value);
				}
			} else if (qName.equals(XMLElements.LAYOUT_PROPERTIES)) {
				Map<String, String> layoutProperties = preferences.layoutProperties;
				String key = Attributes.PROPERTY_KEY
						.deserializeString(attributes);
				value = Attributes.PROPERTY_VALUE.deserializeString(attributes);
				if (key != null) {
					layoutProperties.put(key, value);
				}
			}
		}

		@Override
		public void startSave(Document document, MessageQueue messageQueue) {
			if (document == null) {
				throw new IllegalArgumentException(
						"Parameter 'document' must not be null.");
			}
			if (messageQueue == null) {
				throw new IllegalArgumentException(
						"Parameter 'messageQueue' must not be null.");
			}
			String dateTime = DateUtility.getCurrentDateTimeString();
			Comment comment = document
					.createComment("The!Cart Studio AttributeTablePreferences - "
							+ dateTime);
			document.appendChild(comment);

			Element rootElement = document
					.createElement(XMLElements.PREFERENCES);
			document.appendChild(rootElement);

			Attributes.LANGUAGE.serializeValueSet(rootElement,
					preferences.getLanguage());
			Attributes.UPDATE_CHECK_INDICATOR.serializeBoolean(rootElement,
					preferences.getUpdateCheckIndicator());
			Attributes.EMULATOR_EXECUTABLE_PATH.serializeString(rootElement,
					preferences.getEmulatorExecutablePath());

			Attributes.FREE_BANK_COLOR.serializeColor(rootElement,
					preferences.getFreeBankColor());
			Attributes.RESERVED_BANK_COLOR.serializeColor(rootElement,
					preferences.getReservedBankColor());
			Attributes.RESERVED_USER_SPACE_BANK_COLOR.serializeColor(
					rootElement, preferences.getReservedUserSpaceBankColor());
			Attributes.USED_ODD_BANK_COLOR.serializeColor(rootElement,
					preferences.getUsedOddBankColor());
			Attributes.USED_EVEN_BANK_COLOR.serializeColor(rootElement,
					preferences.getUsedEvenBankColor());
			Attributes.CONFLICT_BANK_COLOR.serializeColor(rootElement,
					preferences.getConflictBankColor());

			Attributes.MAIN_WINDOW_EXTENDED_STATE.serializeInteger(rootElement,
					preferences.getMainWindowExtendedState());
			Attributes.MAIN_WINDOW_LOCATION.serializePoint(rootElement,
					preferences.getMainWindowLocation());
			Attributes.MAIN_WINDOW_SIZE.serializeDimension(rootElement,
					preferences.getMainWindowSize());

			Attributes.LAST_WORKBOOK_FOLDER_PATH.serializeString(rootElement,
					preferences.getLastWorkbookFolderPath());
			Attributes.LAST_ENTRY_FOLDER_PATH.serializeString(rootElement,
					preferences.getLastEntryFolderPath());
			Attributes.LAST_EXPORT_FOLDER_PATH.serializeString(rootElement,
					preferences.getLastExportFolderPath());

			// Recent workbook file paths
			for (String filePath : preferences.getRecentWorkbookFilePathsList()) {
				Element recentWorkbookElement = document
						.createElement(XMLElements.RECENT_WORKBOOK);
				rootElement.appendChild(recentWorkbookElement);
				Attributes.FILE_PATH.serializeString(recentWorkbookElement,
						filePath);
			}

			// Layout properties
			for (String key : preferences.getLayoutProperties().keySet()) {
				Element layoutPropertiesElement = document
						.createElement(XMLElements.LAYOUT_PROPERTIES);
				rootElement.appendChild(layoutPropertiesElement);
				Attributes.PROPERTY_KEY.serializeString(
						layoutPropertiesElement, key);
				Attributes.PROPERTY_VALUE.serializeString(
						layoutPropertiesElement, preferences
								.getLayoutProperties().get(key));
			}
		}

	}

	// Instance attributes.
	private File file;

	private Language language;
	private boolean updateCheckIndicator;
	private String emulatorExecutablePath;
	private Color freeBankColor;
	private Color reservedBankColor;
	private Color reservedUserSpaceBankColor;
	private Color usedOddBankColor;
	private Color usedEvenBankColor;
	private Color conflictBankColor;

	private int mainWindowExtendedState;
	private Point mainWindowLocation;
	private Dimension mainWindowSize;

	private String lastWorkbookFolderPath;
	private String lastEntryFolderPath;
	private String lastExportFolderPath;
	List<String> recentWorkbookFilePathsList;

	Map<String, String> layoutProperties;

	public Preferences() {
		file = new File(System.getProperty("user.home"), FOLDER_NAME);
		file = new File(file, FILE_NAME);
		file = file.getAbsoluteFile();

		language = Language.DEFAULT;
		updateCheckIndicator = true;
		emulatorExecutablePath = "";
		setDefaultBankColors();

		mainWindowLocation = null;
		mainWindowSize = new Dimension(640, 480);
		lastWorkbookFolderPath = "";
		lastEntryFolderPath = "";
		lastExportFolderPath = "";
		recentWorkbookFilePathsList = new ArrayList<String>();

		layoutProperties = new TreeMap<String, String>();
	}

	/**
	 * Sets the language.
	 * 
	 * @param language
	 *            The locale, may be empty, not <code>null</code>.
	 */
	public void setLanguage(Language language) {
		if (language == null) {
			throw new IllegalArgumentException(
					"Parameter 'language' must not be null.");
		}
		this.language = language;
	}

	/**
	 * Gets the language.
	 * 
	 * @return The language, may be empty, not <code>null</code>.
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * Sets the update check indicator.
	 * 
	 * @param updateCheckIndicator
	 *            <code>true</code> if the update check is enabled,
	 *            <code>false</code> otherwise.
	 */
	public void setUpdateCheckIndicator(boolean updateCheckIndicator) {
		this.updateCheckIndicator = updateCheckIndicator;
	}

	/**
	 * Gets the update check indicator..
	 * 
	 * @return <code>true</code> if the update check is enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean getUpdateCheckIndicator() {
		return updateCheckIndicator;
	}

	/**
	 * Gets the locale as object.
	 * 
	 * @return The locale object, not <code>null</code>.
	 */
	public Locale getLocale() {

		Locale result;
		Language language;
		language = this.language;
		if (language.equals(Language.DEFAULT)) {
			String userLanguage = System.getProperty("user.language");
			if (userLanguage.toLowerCase().startsWith("de")) {
				language = Language.DE;
			} else {
				language = Language.EN;
			}
		}
		if (language.equals(Language.DE)) {
			result = Locale.GERMAN;
		} else {
			result = Locale.ENGLISH;
		}
		return result;
	}

	/**
	 * Sets the emulator executable path.
	 * 
	 * @param emulatorExecutablePath
	 *            The emulator executable path, may be empty, not
	 *            <code>null</code>.
	 */
	public void setEmulatorExecutablePath(String emulatorExecutablePath) {
		if (emulatorExecutablePath == null) {
			throw new IllegalArgumentException(
					"Parameter 'emulatorExecutablePath' must not be null.");
		}
		this.emulatorExecutablePath = emulatorExecutablePath;

	}

	/**
	 * Gets the emulator executable path.
	 * 
	 * @return emulatorExecutablePath The emulator executable path, may be
	 *         empty, not <code>null</code>.
	 */
	public String getEmulatorExecutablePath() {
		return emulatorExecutablePath;
	}

	public void setDefaultBankColors() {
		freeBankColor = new Color(0x9fee00);
		reservedBankColor = new Color(0x33cccc);
		reservedUserSpaceBankColor = new Color(0xe6399b);
		usedEvenBankColor = new Color(0xff4040);
		usedOddBankColor = new Color(0xff7373);
		conflictBankColor = new Color(0xffff00);
	}

	/**
	 * Sets the free bank color.
	 * 
	 * @param color
	 *            The color, not <code>null</code>.
	 */
	public void setFreeBankColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		this.freeBankColor = color;
	}

	/**
	 * Gets the free bank color.
	 * 
	 * @return The free bank color, not <code>null</code>.
	 */
	public Color getFreeBankColor() {
		return freeBankColor;
	}

	/**
	 * Sets the reserved bank color.
	 * 
	 * @param color
	 *            The color, not <code>null</code>.
	 */
	public void setReservedBankColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		this.reservedBankColor = color;
	}

	/**
	 * Gets the reserved bank color.
	 * 
	 * @return The reserved bank color, not <code>null</code>.
	 */
	public Color getReservedBankColor() {
		return reservedBankColor;
	}

	/**
	 * Sets the reserved user space bank color.
	 * 
	 * @param color
	 *            The color, not <code>null</code>.
	 */
	public void setReservedUserSpaceBankColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		this.reservedUserSpaceBankColor = color;
	}

	/**
	 * Gets the reserved user space bank color.
	 * 
	 * @return The reserved user space bank color, not <code>null</code>.
	 */
	public Color getReservedUserSpaceBankColor() {
		return reservedUserSpaceBankColor;
	}

	/**
	 * Sets the used odd bank color.
	 * 
	 * @param color
	 *            The color, not <code>null</code>.
	 */
	public void setUsedOddBankColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		this.usedOddBankColor = color;
	}

	/**
	 * Gets the used odd bank color.
	 * 
	 * @return The used odd bank color, not <code>null</code>.
	 */
	public Color getUsedOddBankColor() {
		return usedOddBankColor;
	}

	/**
	 * Sets the used even bank color.
	 * 
	 * @param color
	 *            The color, not <code>null</code>.
	 */
	public void setUsedEvenBankColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		this.usedEvenBankColor = color;
	}

	/**
	 * Gets the used even bank color.
	 * 
	 * @return The used even bank color, not <code>null</code>.
	 */
	public Color getUsedEvenBankColor() {
		return usedEvenBankColor;
	}

	/**
	 * Sets the conflict bank color.
	 * 
	 * @param color
	 *            The color, not <code>null</code>.
	 */
	public void setConflictBankColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		this.conflictBankColor = color;
	}

	/**
	 * Gets the conflict bank color.
	 * 
	 * @return The used even bank color, not <code>null</code>.
	 */
	public Color getConflictBankColor() {
		return conflictBankColor;
	}

	/**
	 * Gets the not available bank color.
	 * 
	 * @return The not available bank color, not <code>null</code>.
	 */
	public Color getNotAvailableBankColor() {
		return getConflictBankColor();
	}

	@Override
	public void setMainWindowExtendedState(int mainWindowExtendedState) {
		this.mainWindowExtendedState = mainWindowExtendedState;
	}

	@Override
	public int getMainWindowExtendedState() {
		return mainWindowExtendedState;
	}

	@Override
	public void setMainWindowLocation(Point mainWindowLocation) {
		if (mainWindowLocation == null) {
			throw new IllegalArgumentException(
					"Parameter 'mainWindowLocation' must not be null.");
		}
		this.mainWindowLocation = mainWindowLocation;
	}

	@Override
	public Point getMainWindowLocation() {
		return mainWindowLocation;
	}

	@Override
	public void setMainWindowSize(Dimension mainWindowSize) {
		if (mainWindowSize == null) {
			throw new IllegalArgumentException(
					"Parameter 'mainWindowSize' must not be null.");
		}
		this.mainWindowSize = mainWindowSize;
	}

	@Override
	public Dimension getMainWindowSize() {
		return mainWindowSize;
	}

	/**
	 * Sets the folder path of the last used workbook.
	 * 
	 * @param folderPath
	 *            The folder path, may be empty, not <code>null</code>.
	 */
	public void setLastWorkbookFolderPath(String folderPath) {
		if (folderPath == null) {
			throw new IllegalArgumentException(
					"Parameter 'folderPath' must not be null.");
		}
		lastWorkbookFolderPath = folderPath;
	}

	/**
	 * Gets the folder path of the last used workbook.
	 * 
	 * @return folderPath The folder path, may be empty, not <code>null</code>.
	 */
	public String getLastWorkbookFolderPath() {
		return lastWorkbookFolderPath;
	}

	/**
	 * Sets the folder path of the last added entry.
	 * 
	 * @param folderPath
	 *            The folder path, may be empty, not <code>null</code>.
	 */
	public void setLastEntryFolderPath(String folderPath) {
		if (folderPath == null) {
			throw new IllegalArgumentException(
					"Parameter 'folderPath' must not be null.");
		}
		lastEntryFolderPath = folderPath;
	}

	/**
	 * Gets the folder path of the last added entry.
	 * 
	 * @return folderPath The folder path, may be empty, not <code>null</code>.
	 */
	public String getLastEntryFolderPath() {
		return lastEntryFolderPath;
	}

	/**
	 * Sets the folder path of the last export.
	 * 
	 * @param folderPath
	 *            The folder path, may be empty, not <code>null</code>.
	 */
	public void setLastExportFolderPath(String folderPath) {
		if (folderPath == null) {
			throw new IllegalArgumentException(
					"Parameter 'folderPath' must not be null.");
		}
		lastExportFolderPath = folderPath;
	}

	/**
	 * Gets the folder path of the last export.
	 * 
	 * @return folderPath The folder path, may be empty, not <code>null</code>.
	 */
	public String getLastExportFolderPath() {
		return lastExportFolderPath;
	}

	/**
	 * Gets the unmodifiable list of file paths for the recently used workbooks.
	 * The list never contains more than {@link #MAX_HISTORY_SIZE} entries.
	 * 
	 * @return The unmodifiable list of file paths for the recently used
	 *         workbooks, may be empty, not <code>null</code>.
	 */
	public List<String> getRecentWorkbookFilePathsList() {
		return Collections.unmodifiableList(recentWorkbookFilePathsList);
	}

	@Override
	public Map<String, String> getLayoutProperties() {
		return layoutProperties;
	}

	/**
	 * Updates the recent workbook list list with the most recently opened/saved
	 * file.
	 * 
	 * @param file
	 */
	public void updateRecentWorkBookFileList(File file) {

		if (file == null) {
			throw new IllegalArgumentException(
					"Parameter 'file' must not be null.");
		}
		if (file.getName().isEmpty()) {
			return;
		}

		// Ensure file can be read.
		String filePath = FileUtility.getAbsolutePath(file.getPath());
		int index = recentWorkbookFilePathsList.indexOf(filePath);

		if (index != -1) {
			recentWorkbookFilePathsList.remove(index);
		}
		recentWorkbookFilePathsList.add(0, filePath);
		for (int i = recentWorkbookFilePathsList.size() - 1; i >= MAX_HISTORY_SIZE; i--) {
			recentWorkbookFilePathsList.remove(i);
		}
	}

	/**
	 * Opens the preferences.
	 * 
	 * @param messageQueue
	 *            The message queue, not <code>null</code>. Will contain error
	 *            messages if the file does not exist or cannot be read.
	 */
	public void open(MessageQueue messageQueue) {
		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}

		// Ensure file exists.
		if (!file.exists()) {
			return;
		}
		// Ensure file can be read.
		if (!file.canRead()) {
			return;
		}

		XMLUtility.open(file, new XMLHandler(this), messageQueue);
	}

	/**
	 * Saves the preferences.
	 * 
	 * @param messageQueue
	 *            The message queue, not <code>null</code>. Will contain error
	 *            messages if the file cannot be created or updated.
	 */
	public void save(MessageQueue messageQueue) {

		if (messageQueue == null) {
			throw new IllegalArgumentException(
					"Parameter 'messageQueue' must not be null.");
		}
		XMLUtility.save(file, new XMLHandler(this), messageQueue);
	}

	/**
	 * Copy the values over to another preferences instance.
	 * 
	 * @param other
	 *            The other preferences instance.
	 */
	public void copyTo(Preferences other) {
		if (other == null) {
			throw new IllegalArgumentException(
					"Parameter 'other' must not be null.");
		}
		XMLUtility.copyTo(new XMLHandler(this), new XMLHandler(other));
	}

	@Override
	public String toString() {
		return "language=" + language + " updateCheckIndicator="
				+ updateCheckIndicator + " emulatorExecutablePath="
				+ emulatorExecutablePath + " lastWorkbookFolderPath="
				+ lastWorkbookFolderPath + " lastEntryFolderPath="
				+ lastEntryFolderPath + " lastExportFolderPath="
				+ lastExportFolderPath + " recentWorkbookFilePathsList="
				+ recentWorkbookFilePathsList;
	}

}
