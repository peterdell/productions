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

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;
import static com.wudsn.tools.base.common.ByteArrayUtility.MB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.wudsn.tools.base.atari.AtrFile;
import com.wudsn.tools.base.atari.CartridgeFileUtility;
import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.atari.ExecutableFile;
import com.wudsn.tools.base.atari.SAPFile;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.JDK;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.MessageQueueEntry;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.common.XMLUtility;
import com.wudsn.tools.base.gui.AttributeTableModel;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.base.atari.cartridge.*;
import com.wudsn.tools.base.repository.Message;
import com.wudsn.tools.base.repository.RepositoryValidation;
import com.wudsn.tools.thecartstudio.DataTypes;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.Texts;
import com.wudsn.tools.thecartstudio.model.Importer.ImportResult;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.UserSpaceContentProvider;
import com.wudsn.tools.thecartstudio.model.atrfile.AtrFileMenu;

/**
 * Persistent workbook.
 * 
 * @author Peter Dell
 * 
 */
public final class Workbook {

    public static final class Attributes {
	private Attributes() {
	}

	public static final Attribute FILE_PATH = new Attribute("filePath",
		DataTypes.Workbook_FilePath);
	public static final Attribute FOLDER_PATH = new Attribute("folderPath",
		DataTypes.Workbook_FolderPath);

    }

    public static final String FILE_EXTENSION = ".tcw";
    private static final String DATA_FOLDER_FILE_EXTENSION = ".tcd";

    private static final long ENTRY_MAX_FILE_SIZE = 128 * MB;
    private static final int SINGLE_ROWS_LIMIT = 100;

    private static final class XMLHandler extends
	    com.wudsn.tools.base.common.XMLHandler {

	private Workbook workbook;

	public XMLHandler(Workbook workbook) {
	    if (workbook == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbook' must not be null.");
	    }
	    this.workbook = workbook;
	}

	@Override
	public void startOpen(MessageQueue messageQueue) {
	    if (messageQueue == null) {
		throw new IllegalArgumentException(
			"Parameter 'messageQueue' must not be null.");
	    }
	    workbook.create(messageQueue);
	    workbook.getRoot().getGenresList().clear();
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
	    if (qName.equals(WorkbookRoot.Attributes.ELEMENT_NAME)) {
		workbook.getRoot().deserialize(attributes);

	    } else if (qName.equals(WorkbookGenre.Attributes.ELEMENT_NAME)) {
		WorkbookGenre genre = new WorkbookGenre();
		genre.deserialize(attributes);
		WorkbookRoot root = workbook.getRoot();
		root.getGenresList().add(genre);
	    } else if (qName.equals(WorkbookEntry.Attributes.ELEMENT_NAME)) {
		WorkbookEntry entry = new WorkbookEntry();
		entry.deserialize(attributes);
		WorkbookRoot root = workbook.getRoot();
		WorkbookEntry otherEntry = root.getEntry(entry.getFileName());
		if (otherEntry != null) {
		    throw new SAXException(
			    "There is already an entry with file name '"
				    + entry.getFileName() + "'.");
		}
		root.addEntry(root.getEntryCount(), entry);
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
	    // Root elements
	    Element workbookElement = document
		    .createElement(WorkbookRoot.Attributes.ELEMENT_NAME);

	    document.appendChild(workbookElement);
	    WorkbookRoot root = workbook.getRoot();
	    root.serialize(workbookElement);

	    // Genres
	    for (WorkbookGenre entry : root.getUnmodifiableGenresList()) {
		Element entryElement = document
			.createElement(WorkbookGenre.Attributes.ELEMENT_NAME);
		workbookElement.appendChild(entryElement);
		entry.serialize(entryElement);
	    }

	    // Entries
	    for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
		Element entryElement = document
			.createElement(WorkbookEntry.Attributes.ELEMENT_NAME);
		workbookElement.appendChild(entryElement);
		entry.serialize(entryElement);
	    }

	}
    }

    private static final class FileToCopy {
	public final WorkbookEntry entry;
	public final File sourceFile;
	public final File targetFile;

	public FileToCopy(WorkbookEntry entry, File sourceFile, File targetFile) {
	    if (entry == null) {
		throw new IllegalArgumentException(
			"Parameter 'entry' must not be null.");
	    }
	    if (sourceFile == null) {
		throw new IllegalArgumentException(
			"Parameter 'sourceFile' must not be null.");
	    }
	    if (targetFile == null) {
		throw new IllegalArgumentException(
			"Parameter 'targetFile' must not be null.");
	    }
	    this.entry = entry;
	    this.sourceFile = sourceFile;
	    this.targetFile = targetFile;
	}
    }

    private CartridgeDatabase cartridgeDatabase;
    private boolean valid;
    private WorkbookRoot root;
    private File rootFile;
    private WorkbookRoot backup;
    private AttributeTableModel entriesModel;

    public Workbook(CartridgeDatabase cartridgeDatabase) {
	if (cartridgeDatabase == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'cartridgeDatabase' must not be null.");
	}
	this.cartridgeDatabase = cartridgeDatabase;

	valid = false;
	root = new WorkbookRoot();
	rootFile = null;

	backup = null;
	entriesModel = null;

    }

    public CartridgeDatabase getCartridgeDatabase() {
	return cartridgeDatabase;
    }

    public void setEntriesTableModel(AttributeTableModel entriesModel) {
	this.entriesModel = entriesModel;
    }

    public boolean isValid() {
	return valid;
    }

    public boolean isChanged() {
	if (!valid) {
	    return false;
	}
	if (backup == null) {
	    return true;
	}
	return !root.contentEquals(backup);
    }

    public boolean isPersistent() {
	return rootFile != null;
    }

    public WorkbookRoot getRoot() {
	return root;
    }

    public File getFile() {
	return rootFile;
    }

    public void close() {
	clear();
    }

    public boolean create(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	clear();
	valid = true;
	root.setTitle(Texts.MainWindow_Title_Unnamed);
	WorkbookGenre genre = new WorkbookGenre();
	genre.setName("Games");
	root.getGenresList().add(genre);
	genre = new WorkbookGenre();
	genre.setName("Demos");
	root.getGenresList().add(genre);
	genre = new WorkbookGenre();
	genre.setName("Tools");
	root.getGenresList().add(genre);
	backup = root.createCopy();

	return initializeBanksList(root, messageQueue);
    }

    private void clear() {
	valid = false;
	root = new WorkbookRoot();
	rootFile = null;
	backup = null;
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    /**
     * Adds a list of file to the workbook.
     * 
     * @param files
     *            The list of files, may be empty, not <code>null</code>.
     * @param callback
     *            confirmation call in case of adding already present files, not
     *            <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     * @return The first added entry or <code>null</code>.
     */
    public WorkbookEntry addEntries(File[] files,
	    WorkbookAddEntriesCallback callback, MessageQueue messageQueue) {
	if (files == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'files' must not be null.");
	}
	if (callback == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'callback' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	// Sort files descending by size to minimize probability of late bank
	// reassignment.
	List<File> filesList = Arrays.asList(files);
	Collections.sort(filesList, new Comparator<File>() {

	    @Override
	    public int compare(File o1, File o2) {
		return JDK.Long.compare(o2.length(), o1.length());
	    }
	});

	WorkbookEntry firstAddedEntry = null;
	int totalNumber = filesList.size();
	int steps = Math.max(10, totalNumber / 100);
	try {

	    for (int i = 0; i < filesList.size() && !callback.isCancelled(); i++) {
		File entryFile = filesList.get(i);

		try {

		    // STATUS: Adding file {0} of {1} ({2}).
		    int number = i + 1;
		    if (number % steps == 0) {
			messageQueue.sendMessage(entryFile,
				WorkbookEntry.Attributes.FILE_PATH,
				Messages.S416, TextUtility
					.formatAsDecimal(number), TextUtility
					.formatAsDecimal(totalNumber),
				TextUtility.formatAsDecimalPercent(number,
					totalNumber));
		    }

		    WorkbookEntry entry = addEntry(entryFile, callback,
			    messageQueue);
		    if (entry != null) {
			firstAddedEntry = entry;
		    }
		} catch (Throwable ex) {
		    throw new RuntimeException("Exception while adding "
			    + entryFile.getName(), ex);
		}
	    }

	} finally {
	    if (entriesModel != null) {
		entriesModel.fireTableDataChanged();
	    }
	}
	return firstAddedEntry;
    }

    /**
     * Adds a new user space entry at the end of a the list. User space entries
     * have no file and there can be any number of them.
     * 
     * @return The new workbook entry, not <code>null</code>.
     */
    public WorkbookEntry addUserSpaceEntry() {
	WorkbookEntry entry = new WorkbookEntry();
	entry.setType(WorkbookEntryType.USER_SPACE_ENTRY);
	entry.setTitle(Texts.WorkbookEntry_UserSpaceEntryTitle);
	entry.setStartBankFixedIndicator(true);

	UserSpaceContentProvider userSpaceContentProvider = new UserSpaceContentProvider();
	userSpaceContentProvider.init(root);
	entry.setStartBankNumber(userSpaceContentProvider.getStartBankNumber());

	int index = root.getUnmodifiableEntriesList().size();
	root.addEntry(index, entry);
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
	return entry;
    }

    /**
     * Adds a new entry based on a file. Atomic option, does not fire mode
     * change. This must be done by the caller.
     * 
     * @param file
     *            The file, not <code>null</code>.
     * @param callback
     *            confirmation call in case of adding already present files, not
     *            <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>. Will contain error
     *            messages if the file does not exist or cannot be read.
     * 
     * @return The new workbook entry or <code>null</code>.
     */
    private WorkbookEntry addEntry(File file,
	    WorkbookAddEntriesCallback callback, MessageQueue messageQueue) {
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	if (callback == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'callback' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	// Perform auto conversion before adding if required.
	ImportResult importResult = Importer.autoConvertFile(this, file,
		messageQueue);
	if (importResult.convertedFileException != null) {
	    if (importResult.convertedFileException.getSeverity() == Message.ERROR) {
		MessageQueueEntry messageQueueEntry = importResult.convertedFileException
			.createMessageQueueEntry(file,
				WorkbookEntry.Attributes.FILE_NAME);
		messageQueue.sendMessage(messageQueueEntry);
		return null;
	    }
	}
	// Use the converted file, if there is one.
	if (importResult.convertedFile != null) {
	    file = importResult.convertedFile;
	}

	String filePath = file.getAbsolutePath();
	String fileName = file.getName();

	WorkbookEntry entry = root.getEntry(fileName);
	int index;
	String title = null;
	boolean insert = false;
	if (entry != null) {
	    int i = fileName.lastIndexOf('.');
	    String fileNameWithoutExtension = fileName;
	    String fileExtension = "";
	    if (i >= 0) {
		fileNameWithoutExtension = fileName.substring(0, i);
		fileExtension = fileName.substring(i);
	    }

	    String renamedFileName = fileName;
	    File renamedFile = file;
	    WorkbookEntry otherEntry = null;

	    // Try to find a free name by appending the source directory path
	    // incrementally.
	    String parentPath = "";
	    while ((otherEntry = root.getEntry(renamedFileName)) != null
		    && (renamedFile != null && StringUtility
			    .isSpecified(renamedFile.getName()))) {
		renamedFile = renamedFile.getParentFile();
		if (renamedFile != null
			&& StringUtility.isSpecified(renamedFile.getName())) {
		    parentPath = parentPath + "_" + renamedFile.getName();
		    renamedFileName = fileNameWithoutExtension + parentPath
			    + fileExtension;
		}
	    }
	    if (otherEntry != null) {
		// Still no success, start enumerating.
		int number = 1;
		renamedFileName = fileNameWithoutExtension + "_" + number
			+ fileExtension;
		while ((otherEntry = root.getEntry(renamedFileName)) != null
			&& renamedFile != null) {
		    number++;
		    renamedFileName = fileNameWithoutExtension + "_" + number
			    + fileExtension;
		}
	    }

	    int result = callback.confirmAdd(entry.getTitle(),
		    entry.getFileName(), filePath, renamedFileName);
	    switch (result) {

	    case WorkbookAddEntriesCallback.AddResult.CANCEL:
		return null; // Action cancelled by user

	    case WorkbookAddEntriesCallback.AddResult.OVERWRITE:
		// If size is identical, re-use title and bank assignment.
		// Otherwise
		// unassigned only banks first.
		title = entry.getTitle();
		if (file.length() != entry.getFileSize()) {
		    unassignBanks(entry);
		}
		index = root.getUnmodifiableEntriesList().indexOf(entry);
		break;

	    case WorkbookAddEntriesCallback.AddResult.RENAME:
		entry = new WorkbookEntry();
		index = root.getUnmodifiableEntriesList().size();
		fileName = renamedFileName;
		insert = true;
		break;

	    case WorkbookAddEntriesCallback.AddResult.SKIP:
		callback.skippedEntriesCount++;
		return null;
	    default:
		throw new RuntimeException("Illegal result " + result + ".");
	    }
	} else {
	    entry = new WorkbookEntry();
	    index = root.getUnmodifiableEntriesList().size();
	    insert = true;
	}
	entry.setFilePath(filePath);
	entry.setFileName(fileName);
	int nameIndex = fileName.lastIndexOf('.');
	if (nameIndex >= 0) {
	    fileName = fileName.substring(0, nameIndex);
	}
	if (title == null) {
	    title = fileName;
	}
	entry.setTitle(title);

	long fileSizeLong = file.length();

	// The following is only relevant if the file is present and really
	// file.
	if (file.exists() && file.isFile()) {
	    if (fileSizeLong == 0) {
		// INFO: File '{0}' was skipped because it is empty.
		messageQueue.sendMessage(entry,
			WorkbookEntry.Attributes.FILE_PATH, Messages.S419,
			file.getAbsolutePath());
		callback.skippedEntriesCount++;
		return null;
	    }
	    if (fileSizeLong > ENTRY_MAX_FILE_SIZE) {
		// ERROR: File '{0}' has {1} and exceeds the maximum size of
		// {2}.
		messageQueue.sendMessage(entry,
			WorkbookEntry.Attributes.FILE_PATH,
			com.wudsn.tools.base.Messages.E214,
			file.getAbsolutePath(),
			TextUtility.formatAsMemorySize(fileSizeLong),
			TextUtility.formatAsMemorySize(ENTRY_MAX_FILE_SIZE));
		return null;
	    }
	}
	int fileSize = (int) fileSizeLong;
	entry.setFileSize(fileSize);

	byte[] content;
	try {
	    content = FileUtility.readBytes(file, ENTRY_MAX_FILE_SIZE, true);
	} catch (CoreException ex) {
	    // This includes the case that the file does not exist or is no
	    // file.
	    messageQueue.sendMessage(ex.createMessageQueueEntry(file,
		    WorkbookEntry.Attributes.FILE_PATH));
	    return null;
	}

	int cartridgeType = CartridgeFileUtility
		.getCartridgeTypeNumericId(content);
	if (cartridgeType > 0) {
	    entry.setFileHeaderType(FileHeaderType.CART);
	    int headerSize = entry.getFileHeaderType().getHeaderSize();
	    fileSize -= headerSize;
	    content = CartridgeFileUtility.getCartridgeContent(content);
	    entry.setContentType(ContentType
		    .getInstanceByCartridgeType(cartridgeType));
	} else {
	    entry.setFileHeaderType(FileHeaderType.NONE);
	}
	entry.setContentCRC32(ByteArrayUtility.getCRC32(content));
	if (!applyEntryDefaults(entry, importResult, content, callback)) {
	    return null; // Action cancelled by user
	}

	int requiredBanks = (fileSize + root.getBankSize() - 1)
		/ root.getBankSize();

	entry.setRequiredBanksCount(requiredBanks);

	// Add new entries only after they have been initialized completely.
	if (insert) {
	    root.addEntry(index, entry);
	    callback.addedEntriesCount++;
	} else {
	    callback.updatedEntriesCount++;
	}
	assignNewBanks(entry, messageQueue);
	return entry;
    }

    /**
     * Apply defaults to a newly added entry.
     * 
     * @param entry
     *            The entry to apply the default to, not <code>null</code>.
     * @param importResult
     *            The import result from the auto conversion, not
     *            <code>null</code>.
     * @param content
     *            The file content of the file associated to the entry, not
     *            <code>null</code>.
     * @param callback
     *            The callback to confirm user interactions, not
     *            <code>null</code>.
     * @return <code>true</code> if the processing can continue,
     *         <code>false</code> if the processing was cancelled by the user.
     */
    private boolean applyEntryDefaults(WorkbookEntry entry,
	    ImportResult importResult, byte[] content,
	    WorkbookAddEntriesCallback callback) {
	if (entry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'entry' must not be null.");
	}
	if (content == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'content' must not be null.");
	}
	if (callback == null) {
	    throw new IllegalArgumentException(
		    "Parameter callback must not be null.");
	}
	// First apply cartridge database defaults.
	CartridgeDatabaseEntry cartridgeDatabaseEntry;
	cartridgeDatabaseEntry = importResult.cartridgeDatabaseEntry;
	if (cartridgeDatabaseEntry == null) {
	    List<CartridgeDatabaseEntry> cartridgeDatabaseEntries = cartridgeDatabase
		    .getEntriesBySizeAndCRC32(content.length / KB,
			    entry.getContentCRC32());
	    // Unique match that can be used as proposal?
	    if (cartridgeDatabaseEntries.size() == 1) {
		cartridgeDatabaseEntry = cartridgeDatabaseEntries.get(0);
	    }
	}
	// Different title available in cartridge database?
	if (cartridgeDatabaseEntry != null
		&& !entry.getTitle().equals(cartridgeDatabaseEntry.getTitle())) {

	    // Check if title is compatible.
	    MessageQueue messageQueue = new MessageQueue();
	    RepositoryValidation rv = RepositoryValidation
		    .createInstance(messageQueue);
	    rv.isStringValid(cartridgeDatabaseEntry,
		    WorkbookEntry.Attributes.TITLE,
		    cartridgeDatabaseEntry.getTitle());
	    if (!messageQueue.containsError()) {

		// If title is compatible, apply the default title from the
		// database.
		entry.setContentType(ContentType
			.getInstanceByCartridgeType(cartridgeDatabaseEntry
				.getCartridgeType().getNumericId()));
		int result = callback.confirmUseTitleFromCartridgeDatabase(
			entry.getFileName(), cartridgeDatabaseEntry.getTitle());
		switch (result) {
		case WorkbookAddEntriesCallback.UseTitleResult.YES:
		    entry.setTitle(cartridgeDatabaseEntry.getTitle());
		    break;
		case WorkbookAddEntriesCallback.UseTitleResult.NO:
		    // Keep file name as default
		    break;
		case WorkbookAddEntriesCallback.UseTitleResult.CANCEL:
		    // Cancel further processing.
		    return false;
		}
	    }
	}

	// Detect non-ROM files.
	if (AtrFile.isHeader(content)) {
	    entry.setContentType(ContentType.FILE_ATR);
	} else if (ExecutableFile.isHeader(content, 0)) {
	    entry.setContentType(ContentType.FILE_EXECUTABLE);
	} else if (SAPFile.isHeader(content)) {
	    entry.setContentType(ContentType.FILE_SAP);
	}

	// Special defaults for modules with known menus.
	List<ContentType> contentTypes;
	ContentType entryContentType = entry.getContentType();
	if (!entryContentType.equals(CartridgeType.UNKNOWN)) {
	    contentTypes = Collections.singletonList(entryContentType);
	} else {
	    contentTypes = ContentType.getValues();
	}

	// Check if there is an importable menu contained and default to
	// "Multiple Entries" if that's the case.
	ImportableMenu importableMenu = null;
	for (ContentType contentType : contentTypes) {
	    int contentSizeInKB = contentType.getCartridgeType().getSizeInKB();
	    if (contentSizeInKB == 0
		    || entry.getContentSize() == contentSizeInKB * KB) {
		importableMenu = contentType.createImportableMenu(content);
		if (importableMenu != null) {
		    if (importableMenu.hasMenuEntries()) {
			entry.setContentType(contentType);
			entry.setDisplayMode(DisplayMode.MULTIPLE_ENTRIES);
			break;
		    }
		}
	    }
	}

	// Special defaults for ATR files.
	if (entry.getContentType().equals(ContentType.FILE_ATR)) {
	    AtrLoader.setDefaultParameters(entry, (AtrFileMenu) importableMenu,
		    content);
	}
	return true;
    }

    /**
     * Removes a number of entries from the workbook.
     * 
     * @param indexes
     *            The array of entry indexes, may be empty, not
     *            <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     */
    public void removeEntries(int[] indexes, MessageQueue messageQueue) {
	if (indexes == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'indexes' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	try {
	    Arrays.sort(indexes);
	    int minRow = 0;
	    for (int i = indexes.length - 1; i >= 0; i--) {
		minRow = indexes[i];
		try {
		    WorkbookEntry entry = root.removeEntry(minRow);
		    unassignBanks(entry);
		    // Prevent too many individual events.
		} finally {
		    if (entriesModel != null
			    && indexes.length < SINGLE_ROWS_LIMIT) {
			entriesModel.fireTableRowsDeleted(minRow, minRow);
		    }
		}
	    }
	} finally {
	    if (entriesModel != null && indexes.length >= SINGLE_ROWS_LIMIT) {
		entriesModel.fireTableDataChanged();
	    }
	}
    }

    /**
     * Sets the genre name for a number of entries from the workbook.
     * 
     * @param genreName
     *            The genre name, may be empty, not <code>null</code>.
     * @param indexes
     *            The array of entry indexes, may be empty, not
     *            <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     */
    public void setEntriesGenreName(String genreName, int[] indexes,
	    MessageQueue messageQueue) {
	if (genreName == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'genreName' must not be null.");
	}
	if (indexes == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'indexes' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	List<WorkbookEntry> entries = root.getUnmodifiableEntriesList();
	for (int i : indexes) {
	    entries.get(i).setGenreName(genreName);
	}
	if (entriesModel != null && indexes.length > 0) {
	    entriesModel.fireTableDataChanged();
	}
    }

    /**
     * For entries with a non-empty transient absolute file path, the transient
     * value is used. For entries with an empty transient absolute file path.
     * 
     * @param entry
     *            The workbook entry, not <code>null</code>.
     * @return The absolute file path, not empty, not <code>null</code>.
     */
    private String getEntryAbsoluteFilePath(WorkbookEntry entry) {
	if (entry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'entry' must not be null.");
	}
	String entryFilePath = entry.getFilePath();
	if (StringUtility.isEmpty(entryFilePath)) {
	    String dataFolderPath = getDataFolderPath();
	    entryFilePath = dataFolderPath + entry.getFileName();

	}
	return entryFilePath;
    }

    /**
     * Opens the workbook.
     * 
     * @param file
     *            The file, not <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>. Will contain error
     *            messages if the file does not exist or cannot be read.
     */
    public void open(File file, MessageQueue messageQueue) {

	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	XMLUtility.open(file, new XMLHandler(this), messageQueue);
	if (!messageQueue.containsError()) {

	    valid = true;
	    rootFile = file;
	    backup = root.createCopy();

	    initializeBanksList(root, messageQueue);
	    assignCurrentBanks(messageQueue);
	}

	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    /**
     * Saves the workbook.
     * 
     * @param file
     *            The file, not <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>. Will contain error
     *            messages if the file cannot be created or updated.
     */
    public void save(File file, MessageQueue messageQueue) {
	if (file == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'file' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	WorkbookRootValidation validation = WorkbookRootValidation
		.createInstance();
	validation.validateSave(root, messageQueue, true);
	if (messageQueue.containsError()) {
	    return;
	}

	// Is this a "Save As", so we have to copy all entries from the source
	// data folder a new target data folder?
	if (rootFile != null && !rootFile.equals(file)) {
	    for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
		if (StringUtility.isEmpty(entry.getFilePath())) {
		    entry.setFilePath(new File(getDataFolderPath(), entry
			    .getFileName()).getAbsolutePath());
		}
	    }
	}

	rootFile = file;

	// Sort entry by default before saving.
	Collections.sort(root.getEntriesList());
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}

	XMLUtility.save(file, new XMLHandler(this), messageQueue);
	if (messageQueue.containsError()) {
	    return;
	}
	saveEntryFilesToDataFolder(messageQueue);
	if (messageQueue.containsError()) {
	    return;
	}
	backup = root.createCopy();
    }

    /**
     * Saves copies of all yet unsaved entry files to the data folder.
     * 
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     */
    private void saveEntryFilesToDataFolder(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	String dataFolderPath = getDataFolderPath();
	File dataFolderFile = new File(dataFolderPath);
	if (!dataFolderFile.exists()) {
	    if (!dataFolderFile.mkdirs()) {
		// ERROR: Cannot create folder '{0}'
		messageQueue.sendMessage(root, null,
			com.wudsn.tools.base.Messages.E203, dataFolderPath);
		return;
	    }
	}

	// Prepare copying of files.
	List<FileToCopy> filesToCopy = new ArrayList<FileToCopy>();
	for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
	    if (StringUtility.isSpecified(entry.getFilePath())) {
		String sourceFilePath = entry.getFilePath();
		String targetFilePath = dataFolderPath + entry.getFileName();
		if (!sourceFilePath.equals(targetFilePath)) {
		    File sourceFile = new File(sourceFilePath);
		    File targetFile = new File(targetFilePath);
		    filesToCopy.add(new FileToCopy(entry, sourceFile,
			    targetFile));
		}
	    } else {
		entry.setFilePath("");
	    }

	}

	// Perform copying of files.
	int totalNumber = filesToCopy.size();
	int steps = Math.max(10, totalNumber / 100);
	for (int i = 0; i < totalNumber; i++) {
	    FileToCopy fileToCopy = filesToCopy.get(i);
	    try {
		int number = i + 1;

		if (number % steps == 0) {
		    // STATUS: Copying file {0} of {1} ({2}).
		    messageQueue.sendMessage(fileToCopy.entry,
			    WorkbookEntry.Attributes.FILE_PATH, Messages.S417,
			    TextUtility.formatAsDecimal(number), TextUtility
				    .formatAsDecimal(totalNumber),
			    TextUtility.formatAsDecimalPercent(number,
				    totalNumber));
		}
		FileUtility.copyFile(fileToCopy.sourceFile,
			fileToCopy.targetFile);
		fileToCopy.entry.setFilePath("");

	    } catch (CoreException ex) {
		messageQueue.sendMessage(ex.createMessageQueueEntry(
			fileToCopy.entry, WorkbookEntry.Attributes.FILE_PATH));
	    }
	}

	// Prepare removal of obsolete files.
	// Sometimes files are not deleted immediately and .gc() can help.
	System.gc();
	File[] files = dataFolderFile.listFiles();
	List<File> filesToDelete = new ArrayList<File>();
	if (files != null) {
	    for (File file : files) {
		if (file.isFile()) {
		    if (root.getEntry(file.getName()) == null) {
			filesToDelete.add(file);
		    }
		}
	    }
	}

	// Remove obsolete files.
	totalNumber = filesToDelete.size();
	for (int i = 0; i < totalNumber; i++) {
	    File file = filesToDelete.get(i);

	    // STATUS: Deleting file {0} of {1} ({2}).
	    int number = i + 1;
	    if (number % 10 == 0) {
		messageQueue
			.sendMessage(file, WorkbookEntry.Attributes.FILE_PATH,
				Messages.S418, TextUtility
					.formatAsDecimal(number), TextUtility
					.formatAsDecimal(totalNumber),
				TextUtility.formatAsDecimalPercent(number,
					totalNumber));
	    }

	    if (!file.delete()) {
		// ERROR: Cannot delete file '{0}'.
		messageQueue.sendMessage(file, null,
			com.wudsn.tools.base.Messages.E219,
			file.getAbsolutePath());
	    }

	}
	if (entriesModel != null) {
	    entriesModel.fireTableDataChanged();
	}
    }

    /**
     * Initialize the banks list and assign all reserved content providers.
     * 
     * @param root
     *            The workbook root to be initialized, not <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     * 
     * @return <code>true</code> if the banks list is initialized now,
     *         <code>false</code> otherwise.
     */
    public static boolean initializeBanksList(WorkbookRoot root,
	    MessageQueue messageQueue) {
	if (root == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'root' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	root.clearBanksList();
	int reservedBanksCount = 0;

	for (ReservedContentProvider reservedContentProvider : root
		.getReservedContentProviders()) {
	    reservedContentProvider.init(root);
	    if (reservedContentProvider.getRequiredBanksCount() > 0) {
		if (reservedContentProvider.getRequiredBankSize() > 0
			&& reservedContentProvider.getRequiredBankSize() != root
				.getBankSize()) {
		    // ERROR: Reserved content provider '{0}' requires a bank
		    // size of {1}.
		    messageQueue.sendMessage(reservedContentProvider, null,
			    Messages.E407, reservedContentProvider.getTitle(),
			    TextUtility
				    .formatAsMemorySize(reservedContentProvider
					    .getRequiredBankSize()));
		    continue;
		}
		reservedBanksCount += reservedContentProvider
			.getRequiredBanksCount();
		int endBank = reservedContentProvider.getStartBankNumber()
			+ reservedContentProvider.getRequiredBanksCount();
		if (endBank > root.getBankCount()) {
		    // ERROR: Reserved content provider '{0}' requires {1}
		    // banks. This means a total of {2} banks is required but
		    // only {3} banks are defined.
		    messageQueue.sendMessage(reservedContentProvider, null,
			    Messages.E408, reservedContentProvider.getTitle(),
			    TextUtility.formatAsDecimal(reservedContentProvider
				    .getRequiredBanksCount()), TextUtility
				    .formatAsDecimal(endBank), TextUtility
				    .formatAsDecimal(root.getBankCount()));
		    continue;

		}
		for (int bankNumber = reservedContentProvider
			.getStartBankNumber(); bankNumber < endBank; bankNumber++) {
		    WorkbookBank bank = root.getBanksList().get(bankNumber);
		    ReservedContentProvider otherReservedContentProvider = bank
			    .getReservedContentProvider();
		    if (otherReservedContentProvider == null) {
			bank.setReservedContentProvider(reservedContentProvider);
		    } else {
			// ERROR: Bank {0} is already reserved for content
			// provider '{1}' and cannot be reserved also for
			// content provider '{2}'.
			messageQueue.sendMessage(reservedContentProvider, null,
				Messages.E409,
				TextUtility.formatAsDecimal(bankNumber),
				otherReservedContentProvider.getTitle(),
				reservedContentProvider.getTitle());
		    }
		}
	    }
	}

	if (messageQueue.containsError()) {
	    return false;
	}
	root.setBanksListInitialized(true, reservedBanksCount);
	return true;
    }

    private boolean validateBanksList(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	if (!root.isBanksListInitialized()) {
	    // ERROR: Banks are not initialized. Adapt the flash target type,
	    // bank count and bank size first to initialize the banks.
	    messageQueue.sendMessage(this, null, Messages.E411);
	    return false;
	}
	return true;
    }

    /**
     * Removes all assigned workbook entries from all banks. All reserved banks
     * remain reserved. All start banks remain unchanged.
     * 
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     * 
     * @return <code>true</code> If all banks were unassigned successfully,
     *         <code>false</code> otherwise.
     */
    public boolean unassignAllBanks(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	if (!validateBanksList(messageQueue)) {
	    return false;
	}
	for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
	    unassignBanks(entry);
	}
	return true;
    }

    /**
     * Assigns the entries to their banks. Conflicts are ignored, so multiple
     * entries may end up on the same bank.
     * 
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     */
    private void assignCurrentBanks(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	if (!unassignAllBanks(messageQueue)) {
	    return;
	}

	int failedEntriesCount = 0;
	RepositoryValidation rv = RepositoryValidation
		.createInstance(messageQueue);

	for (WorkbookEntry entry : root.getUnmodifiableEntriesList()) {
	    int startBank = entry.getStartBankNumber();

	    if (startBank >= 0) {
		if (rv.isLongValid(entry, WorkbookEntry.Attributes.START_BANK,
			0, root.getBankCount() - 1, startBank)) {
		    if (!assignBanks(entry, startBank)) {
			failedEntriesCount++;
		    }
		}
	    }
	}
	if (failedEntriesCount > 0) {
	    // ERROR: {0} entries could not be assigned. Remove entries from the
	    // workbook to free enough space an try again.
	    messageQueue.sendMessage(this, null, Messages.E126,
		    TextUtility.formatAsDecimal(failedEntriesCount));
	    return;
	}
    }

    /**
     * Assigns new banks to all existing entries without overlapping.
     * 
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     */
    public void assignNewBanks(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	if (!unassignAllBanks(messageQueue)) {
	    return;
	}

	List<WorkbookEntry> entries = new ArrayList<WorkbookEntry>(
		root.getUnmodifiableEntriesList());
	Collections.sort(entries, new Comparator<WorkbookEntry>() {

	    @Override
	    public int compare(WorkbookEntry o1, WorkbookEntry o2) {
		int result;
		// Sort entries with fixed start banks first, then descending by
		// size, then ascending by file name.
		result = JDK.Boolean.compare(o2.isStartBankFixed(),
			o1.isStartBankFixed());
		if (result == 0) {
		    // This is to ensure minimal gaps.
		    result = (o2.getRequiredBanksCount() - o1
			    .getRequiredBanksCount());
		}
		if (result == 0) {
		    // This is to ensure stable sequence.
		    result = o1.getFileName().compareTo(o2.getFileName());
		}
		return result;
	    }
	});

	int failedEntriesCount = 0;
	for (WorkbookEntry entry : entries) {
	    if (!assignNewBanks(entry, messageQueue)) {
		failedEntriesCount++;
	    }
	}
	if (failedEntriesCount > 0 && !messageQueue.containsError()) {
	    // ERROR: {0} entries could not be assigned. Remove entries from the
	    // workbook to free enough space an try again.
	    messageQueue.sendMessage(this, null, Messages.E126,
		    TextUtility.formatAsDecimal(failedEntriesCount));
	}
    }

    /**
     * Finds a free sequence of banks which either starts the fixed bank
     * specified by the user or at properly aligned free space.
     * 
     * @param entry
     *            The entry, not <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     * @return <code>true</code> if the entry is empty or if the entry is not
     *         empty and was assigned properly, <code>false</code> otherwise.
     */
    private boolean assignNewBanks(WorkbookEntry entry,
	    MessageQueue messageQueue) {
	if (entry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'entry' must not be null.");
	}
	unassignBanks(entry);

	// Clear the start bank for the pathological case of "0 banks required".
	if (!entry.isStartBankFixed()) {
	    entry.setStartBankNumber(WorkbookEntry.START_BANK_UNDEFINED);
	}
	if (entry.getRequiredBanksCount() == 0) {
	    return true;
	}
	boolean bankFound;
	if (entry.isStartBankFixed()) {
	    RepositoryValidation rv = RepositoryValidation
		    .createInstance(messageQueue);
	    if (rv.isLongValid(entry, WorkbookEntry.Attributes.START_BANK, 0,
		    root.getBankCount() - 1, entry.getStartBankNumber())) {
		bankFound = assignBanks(entry, entry.getStartBankNumber());
	    } else {
		bankFound = false;
	    }
	} else {
	    entry.setStartBankNumber(WorkbookEntry.START_BANK_UNDEFINED);
	    bankFound = false;
	    for (int bankNumber = 0; !bankFound
		    && bankNumber < root.getBankCount(); bankNumber++) {
		if (bankNumber % entry.getAlignmentBanksCount() == 0) {
		    bankFound = assignBanks(entry, bankNumber);
		}
	    }
	}
	return bankFound;
    }

    /**
     * Tries to assigned an entry to all required banks starting at
     * <code>startBank</code>. The start bank of the entry remains unchanged.
     * 
     * @param entry
     *            The entry, not <code>null</code>.
     * @param startBank
     *            The start bank, a non-negative integer. Normally the current
     *            start bank of the entry, except during re-assignment of to new
     *            banks.
     * @return <code>true</code> if all bank were assigned and the startBank was
     *         set, <code>false</code> otherwise.
     */
    public boolean assignBanks(WorkbookEntry entry, int startBank) {
	if (entry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'entry' must not be null.");
	}
	if (entry.areBanksAssigned()) {
	    throw new IllegalArgumentException("Entry for file '"
		    + entry.getFileName() + "' starting at bank "
		    + entry.getStartBankNumber()
		    + " already has banks assigned.");
	}
	List<WorkbookBank> banks = root.getBanksList();
	boolean allBanksFree = true;
	for (int j = 0; allBanksFree && j < entry.getRequiredBanksCount(); j++) {
	    if (startBank + j < banks.size()) {
		WorkbookBank bank = banks.get(startBank + j);

		if (bank.isUsed()) {
		    allBanksFree = false;
		}
	    } else {
		allBanksFree = false;
	    }
	}
	if (allBanksFree) {
	    entry.setStartBankNumber(startBank);
	    for (int j = 0; j < entry.getRequiredBanksCount(); j++) {
		banks.get(startBank + j).getEntries().add(entry);
	    }
	}
	entry.setBanksAssigned(allBanksFree);
	return allBanksFree;
    }

    /**
     * Unassigns an entry from all its assigned banks. The start bank remains
     * unchanged.
     * 
     * @param entry
     *            The entry, not <code>null</code>.
     */
    public void unassignBanks(WorkbookEntry entry) {
	if (entry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'entry' must not be null.");
	}
	if (!entry.areBanksAssigned()) {
	    return;
	}
	entry.setBanksAssigned(false);
	List<WorkbookBank> banks = root.getBanksList();
	for (int bankNumber = entry.getStartBankNumber(); bankNumber < entry
		.getStartBankNumber() + entry.getRequiredBanksCount()
		&& bankNumber < root.getBankCount(); bankNumber++) {
	    WorkbookBank bank = banks.get(bankNumber);
	    bank.getEntries().remove(entry);
	}

	// This check of the post condition is not active by default. It can be
	// activated in of problems.
	// for (WorkbookBank bank : banks) {
	// if (bank.getEntries().contains(entry)) {
	// throw new IllegalStateException("Entry '" + entry.getFileName() +
	// "' not unassigned from bank "
	// + bank.getNumber());
	// }
	// }
    }

    /**
     * Gets the absolute file path to the folder of the workbook.
     * 
     * @return The absolute file path to the folder of the workbook or
     *         <code>null</code>.
     */
    private String getDataFolderPath() {
	if (rootFile == null) {
	    throw new IllegalStateException("Workbook is not yet persistent.");
	}

	String result = FileUtility.getAbsolutePath(rootFile.getAbsolutePath());
	int index = result.lastIndexOf('.');
	if (index < 0) {
	    throw new IllegalStateException("Workbook path '" + result
		    + "' has no file extensions.");
	}
	result = result.substring(0, index) + DATA_FOLDER_FILE_EXTENSION
		+ File.separator;
	return result;
    }

    /**
     * Exports the current workbook in the given export format.
     * 
     * @param exportFormat
     *            The export format, see {@link ExportFormat}.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     * 
     * @return The content, or <code>null</code> in case of error messages.
     */
    public WorkbookExport export(int exportFormat, MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	// Validate export
	WorkbookRootValidation validation = WorkbookRootValidation
		.createInstance();
	validation.validateExport(root, messageQueue);
	if (messageQueue.containsError()) {
	    return null;
	}

	// Build content
	int imageSize = root.getImageSize();
	byte[] data = new byte[imageSize];
	WorkbookExport result = new WorkbookExport(this, data);

	// Fill all banks with $ff.
	int bankSize = root.getBankSize();
	byte[] emptyBank = new byte[bankSize];
	for (int i = 0; i < bankSize; i++) {
	    emptyBank[i] = WorkbookBank.UNUSED_BYTE;
	}
	for (int i = 0; i < imageSize; i += bankSize) {
	    System.arraycopy(emptyBank, 0, data, i, bankSize);
	}

	for (int bankNumber = 0; bankNumber < root.getBankCount(); bankNumber++) {
	    WorkbookBank bank = root.getBanksList().get(bankNumber);
	    ReservedContentProvider reservedContentProvider = bank
		    .getReservedContentProvider();
	    if (reservedContentProvider != null
		    && reservedContentProvider.getStartBankNumber() == bankNumber) {
		byte[] reservedContent = reservedContentProvider.createContent(
			this, result, messageQueue);
		if (reservedContent != null) {
		    int expectedLength = reservedContentProvider
			    .getRequiredBanksCount() * bankSize;
		    if (reservedContent.length <= expectedLength) {
			System.arraycopy(reservedContent, 0, data, bankNumber
				* bankSize, reservedContent.length);
		    } else {
			throw new RuntimeException(
				"Reserved content provider '"
					+ reservedContentProvider
					+ "' returned too much content. Expected "
					+ expectedLength + " bytes and found "
					+ reservedContent.length + " bytes.");
		    }
		    bankNumber += reservedContentProvider
			    .getRequiredBanksCount() - 1;
		}
	    } else {
		List<WorkbookEntry> entries = bank.getEntries();
		if (!entries.isEmpty()) {
		    WorkbookEntry entry = entries.get(0);
		    byte[] entryContent = getEntryFileContent(entry,
			    messageQueue);

		    // Any file content available?
		    if (entryContent != null) {

			// Modify file content? This will extended the file size
			// to a multiple of {@link AtrFile#SECTOR_SIZE_SD}
			if (entry.getContentType().equals(ContentType.FILE_ATR)) {
			    try {
				entryContent = AtrLoader.modifyContent(entry,
					entryContent);
			    } catch (RuntimeException ex) {
				throw new RuntimeException("Entry '"
					+ entry.getTitle()
					+ "' with file path "
					+ entry.getFilePath()
					+ " is no valid ATR file", ex);
			    }
			}

			// Copy to file content to output array.
			System.arraycopy(entryContent, 0, data, bankNumber
				* bankSize, entryContent.length);
		    }
		    bankNumber += entry.getRequiredBanksCount() - 1;
		}
	    }
	}
	switch (exportFormat) {
	case ExportFormat.CAR_IMAGE:

	    CartridgeType cartridgeType = root.getCartridgeType();
	    if (cartridgeType == CartridgeType.UNKNOWN) {
		// ERROR: Cartridge target type {0} does not support export as
		// CAR
		// cartridge image. Check the workbook options.
		messageQueue.sendMessage(data, null, Messages.E404, root
			.getFlashTargetType().getText());
	    }
	    break;
	case ExportFormat.ATR_IMAGE:
	    if (data.length % AtrFile.SECTOR_SIZE_8K != 0) {
		// ERROR: Size {0} of content is not a multiple of {1}.
		messageQueue.sendMessage(data, null, Messages.E410,
			TextUtility.formatAsMemorySize(data.length),
			TextUtility.formatAsMemorySize(AtrFile.SECTOR_SIZE_8K));
	    }
	    break;
	}
	if (messageQueue.containsError()) {
	    return null;
	}

	return result;
    }

    /**
     * Gets the file content of the file of the workbook entry, excluding the
     * possibly existing file header.
     * 
     * @param entry
     *            The entry, not <code>null</code>.
     * @param messageQueue
     *            The message queue, not <code>null</code>.
     * @return The entry content, or <code>null</code> in case of errors.
     */
    public byte[] getEntryFileContent(WorkbookEntry entry,
	    MessageQueue messageQueue) {
	String filePath = getEntryAbsoluteFilePath(entry);

	File entryFile = new File(filePath);
	if (!entryFile.exists()) {
	    // ERROR: File '{0}' does not exist.
	    messageQueue.sendMessage(entry, WorkbookEntry.Attributes.FILE_PATH,
		    com.wudsn.tools.base.Messages.E203, filePath);
	    return null;
	}
	long entryFileSize = entryFile.length();
	if (entryFileSize != entry.getFileSize()) {
	    // ERROR: File '{0}' has size {1} now,
	    // but had size {2} when it was added. Remove the
	    // file and add it again.
	    messageQueue.sendMessage(entry, WorkbookEntry.Attributes.FILE_PATH,
		    Messages.E403, filePath,
		    TextUtility.formatAsMemorySize(entryFileSize),
		    TextUtility.formatAsMemorySize(entry.getFileSize()));

	} else {

	    CartridgeType cartridgeType = entry.getContentType()
		    .getCartridgeType();
	    long cartridgeTypeSize = cartridgeType.getSizeInKB() * KB;
	    if (cartridgeType != CartridgeType.UNKNOWN
		    && entry.getContentSize() != cartridgeTypeSize) {
		// ERROR: File '{0}' has content size {1} but
		// the specified content type '{2}' requires
		// content {3}.
		// Choose a suitable content type.
		messageQueue.sendMessage(entry,
			WorkbookEntry.Attributes.CONTENT_TYPE, Messages.E413,
			filePath,
			TextUtility.formatAsMemorySize(entry.getContentSize()),
			entry.getContentType().getText(),
			TextUtility.formatAsMemorySize(cartridgeTypeSize));
	    } else {
		InputStream inputStream = null;
		try {
		    inputStream = FileUtility.openInputStream(entryFile);
		    int headerSize = entry.getFileHeaderType().getHeaderSize();
		    if (headerSize > 0) {
			try {

			    if (inputStream.read(new byte[headerSize]) != headerSize) {
				throw new RuntimeException();
			    }
			} catch (IOException ex) {
			    // ERROR: Cannot read content of file '{0}'.
			    // Original error message:
			    // {1}
			    throw new CoreException(
				    com.wudsn.tools.base.Messages.E206,
				    filePath, ex.getLocalizedMessage());
			}
		    }
		    byte[] entryContent = FileUtility.readBytes(filePath,
			    inputStream, ENTRY_MAX_FILE_SIZE, true);

		    return entryContent;
		} catch (CoreException ex) {
		    messageQueue.sendMessage(ex.createMessageQueueEntry(entry,
			    WorkbookEntry.Attributes.FILE_PATH));
		} finally {
		    if (inputStream != null) {
			try {
			    FileUtility
				    .closeInputStream(entryFile, inputStream);
			} catch (CoreException ex) {
			    messageQueue
				    .sendMessage(ex.createMessageQueueEntry(
					    entry,
					    WorkbookEntry.Attributes.FILE_PATH));
			}
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public String toString() {
	return root.toString();
    }

}
