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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.ClassPathUtility;
import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.Log;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.ResourceUtility;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.ImportableMenu.Collector;
import com.wudsn.tools.thecartstudio.model.ImportableMenu.Result;

public final class CartridgeMenu {

    private static final String SIMPLE_MENU = "data/cartmenu.rom";
    private static final String EXTENDED_MENU = "data/cartmenu-extended.rom";

    private static final String EXTERNAL_SIMPLE_MENU = "cartmenu.rom";
    private static final String EXTERNAL_EXTENDED_MENU = "cartmenu-extended.rom";

    public static final int MENU_ENTRY_LENGTH = 64; // Must be a divisor of $2000

    // Create genre entries.
    // The first block is an array of genre records.
    // Each genre record is 4 bytes long.
    // - byte 0 Flag byte, $00 means no favorites, $80 means favorites exist
    // - byte 1 Reserved
    // - byte 2/3 word offset relative to the start of the array.
    // It points to the genre text in ASCII with zero termination.
    private static final class Genre {
	public static final int RECORD_SIZE = 4;
	public static final int FLAGS = 0;
	public static final int TEXT_LENGTH = 1;
	public static final int TEXT_OFFSET = 2;

	private static final int MAX_TEXT_LENGTH = 40;
	private final int number;
	private final String name;
	private final byte[] nameBytes;
	private final byte[] result;

	public Genre(int number, String name, int nameOffset, byte[] result) {
	    if (name == null) {
		throw new IllegalArgumentException(
			"Parameter name must not be null.");
	    }
	    if (result == null) {
		throw new IllegalArgumentException(
			"Parameter result must not be null.");
	    }
	    this.number = number;
	    this.name = name;
	    this.result = result;
	    int offset = number * RECORD_SIZE;
	    setByte(result, offset + FLAGS, 0);

	    String text = name;
	    if (text.length() > MAX_TEXT_LENGTH) {
		text = text.substring(0, MAX_TEXT_LENGTH);
	    }
	    setByte(result, offset + TEXT_LENGTH, text.length());
	    setWord(result, offset + TEXT_OFFSET, nameOffset);

	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    try {
		buffer.write(ASCIIString.getBytes(text));
	    } catch (IOException ex) {
		throw new RuntimeException(ex);
	    }
	    buffer.write(0);
	    nameBytes = buffer.toByteArray();
	    System.arraycopy(nameBytes, 0, result, nameOffset, nameBytes.length);

	}

	public int getNumber() {
	    return number;
	}

	public String getName() {
	    return name;
	}

	public byte[] getNameBytes() {
	    return nameBytes;
	}

	public void setFavoritesAvailableIndicator() {
	    result[number * RECORD_SIZE + FLAGS] |= (byte) 0x80;
	}

	public static List<Genre> setGenreEntriesContent(Workbook workbook,
		byte[] result) {
	    if (workbook == null) {
		throw new IllegalArgumentException(
			"Parameter workbook must not be null.");
	    }
	    if (result == null) {
		throw new IllegalArgumentException(
			"Parameter result must not be null.");
	    }
	    List<WorkbookGenre> workbookGenresList = new ArrayList<WorkbookGenre>(
		    workbook.getRoot().getUnmodifiableGenresList());

	    // Insert "All" at position 0.
	    WorkbookGenre allWorkbookGenre = new WorkbookGenre();
	    allWorkbookGenre.setName(WorkbookGenre.ALL);
	    workbookGenresList.add(0, allWorkbookGenre);

	    // Convert workbook genres to cartridge menu genres.
	    int genreCount = workbookGenresList.size();
	    int genreNameOffset = genreCount * RECORD_SIZE;
	    List<Genre> genresList = new ArrayList<Genre>(genreCount);
	    for (int i = 0; i < genreCount; i++) {
		WorkbookGenre workbookGenre = workbookGenresList.get(i);
		Genre genre = new Genre(i, workbookGenre.getName(),
			genreNameOffset, result);
		genresList.add(genre);
		genreNameOffset += genre.getNameBytes().length;
	    }

	    return genresList;

	}
    }

    public static final class Offsets {

	// The JMP to the default cart menu. This point to the simple menu by
	// default. If the extended menu is available, it points to the extended
	// menu.
	public static final int ENTRY_DEFAULT_CARTSTART = 0x000f;

	// Cartridge initialization vector. The address here is copied to
	// ENTRY_DEFAULT_CARTSTART.
	public static final int CARTCS = 0x1ffa;

	// The start offset of the reserved studio area in the first bank.
	public static final int MENU_STUDIO_ROM = 0x1800;

	// The start offset of the extended menu that check which menu shall be
	// started.
	public static final int MENU_START_ROM = 0x1e00;

	// The Master Control Block (MCB) for the Studio starts at $BF00 and
	// must end before the signature at $BFE0.
	public static final int MENU_MCB_ROM = 0x1f00;

	public static final int CARTRIDGE_TYPE = MENU_MCB_ROM + 0x0000; // Byte
	public static final int BANK_COUNT = MENU_MCB_ROM + 0x0002; // Word
	public static final int MENU_GENRES_COUNT = MENU_MCB_ROM + 0x0004; // Word
	public static final int MENU_GENRES_START_BANK_NUMBER = MENU_MCB_ROM + 0x0006; // Word
	public static final int MENU_ENTRIES_COUNT = MENU_MCB_ROM + 0x0008; // Word
	public static final int MENU_ENTRIES_START_BANK_NUMBER = MENU_MCB_ROM + 0x000a; // Word

	public static final int MENU_TITLE = MENU_MCB_ROM + 0x000c; // 40
	// Characters

	// 16 bytes like "The!Cart99991231"
	public static final int CARTMENU_SIGNATURE = 0x1fe0;
	public static final int CARTMENU_SIGNATURE_LENGTH = 0x10;

	// The 8 first banks (64k) are reserved for the original "cartmenu.rom".
	public static final int CARTMENU_FIRST_BANK_END = 0x2000;

	// Relative offsets in the menu entries.
	public static final int MENU_ENTRY_NUMBER = 0; // Word
	public static final int MENU_ENTRY_THE_CART_MODE = 2; // Byte
	public static final int MENU_ENTRY_CONTENT_SIZE = 4; // 4 Bytes
	public static final int MENU_ENTRY_START_BANK_NUMBER = 8; // Word
	public static final int MENU_ENTRY_INITIAL_BANK_NUMBER = 10; // Word
	public static final int MENU_ENTRY_LOADER_BASE_ADDRESS = 12; // Word
	public static final int MENU_ENTRY_SOURCE_TYPE = 14; // Byte
	public static final int MENU_ENTRY_ITEM_MENU_VERSION = 15; // Byte
	public static final int MENU_ENTRY_ITEM_NUMBER = 16; // Byte
	public static final int MENU_ENTRY_TITLE_LENGTH = 17; // Byte
	public static final int MENU_ENTRY_TITLE = 18; // 40 Bytes
	public static final int MENU_ENTRY_GENRE_NUMBER = 58; // Byte
	public static final int MENU_ENTRY_FAVORITE_INDICATOR = 59; // Byte
    }

    /**
     * Collector callback to get the entries from an importable cartridge menu
     * into the workbook menu.
     * 
     */
    private final class InnerMenuCollector implements Collector {
	private Workbook workbook;
	private boolean logMenus;
	private boolean logMenuEntries;

	public InnerMenuCollector(Workbook workbook) {
	    if (workbook == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbook' must not be null.");
	    }
	    this.workbook = workbook;
	    logMenus = false;
	    logMenuEntries = false;
	}

	@Override
	public void collectMenu(Object owner, String info) {
	    if (owner == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbookEntry' must not be null.");
	    }
	    if (info == null) {
		throw new IllegalArgumentException(
			"Parameter 'info' must not be null.");
	    }
	    WorkbookEntry workbookEntry = (WorkbookEntry) owner;
	    workbookEntry.setTitle(info);
	    if (logMenus) {
		Log.logInfo("Found importable menu: {0} - '{1}' - '{2}'",
			new Object[] {
				workbookEntry.getContentType().toString(),
				info, workbookEntry.getTitle() });
	    }

	}

	@Override
	public final void collectMenuEntry(Object owner, int itemMenuVersion,
		int itemNumber, String title) {
	    if (workbook == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbook' must not be null.");
	    }
	    if (owner == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbookEntry' must not be null.");
	    }
	    if (title == null) {
		throw new IllegalArgumentException(
			"Parameter 'title' must not be null.");
	    }
	    WorkbookEntry workbookEntry = (WorkbookEntry) owner;
	    title = title.trim();
	    if (StringUtility.isSpecified(title)) {
		if (logMenuEntries) {
		    Log.logInfo("Found importable menu entry: '{0}' - '{1}'",
			    new Object[] { workbookEntry.getTitle(), title });
		}
		WorkbookMenuEntry workbookMenuEntry = new WorkbookMenuEntry(
			workbookEntry);
		workbookMenuEntry.setTitle(title);
		workbookMenuEntry
			.setSourceType(WorkbookMenuEntry.SOURCE_TYPE_MENU_ENTRY_ITEM);
		workbookMenuEntry.setItemMenuVersion(itemMenuVersion);
		workbookMenuEntry.setItemNumber(itemNumber);
		workbook.getRoot().getMenuEntriesList().add(workbookMenuEntry);
	    }
	}

    }

    private CartridgeMenuType cartridgeMenuType;
    private boolean valid;
    private boolean external;
    private String externalFilePath;
    private String version;
    private byte[] simpleMenuContent;
    private byte[] extendedMenuContent;

    public static CartridgeMenu createInstance(
	    CartridgeMenuType cartridgeMenuType) {
	if (cartridgeMenuType == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'cartridgeMenuType' must not be null.");
	}
	if (cartridgeMenuType.equals(CartridgeMenuType.NONE)) {
	    return null;
	}
	CartridgeMenu instance = new CartridgeMenu(cartridgeMenuType);
	return instance;
    }

    private CartridgeMenu(CartridgeMenuType cartridgeMenuType) {
	this.cartridgeMenuType = cartridgeMenuType;

	valid = false;
	external = false;
	externalFilePath = "";
	version = "????-??-??";

	boolean simpleMenuValid = false;
	boolean extendedMenuValid = false;

	File jarFolder = ClassPathUtility.getJarFolder();

	// Load simple menu.
	byte[] externalSimpleMenuContent = null;
	if (jarFolder != null) {
	    try {
		File externalSimpleMenuFile = new File(jarFolder,
			EXTERNAL_SIMPLE_MENU);
		externalSimpleMenuContent = FileUtility.readBytes(
			externalSimpleMenuFile, FileUtility.MAX_SIZE_1MB, true);
		externalFilePath = externalSimpleMenuFile.getAbsolutePath();
	    } catch (CoreException ignore) {
	    }
	}

	// Load simple menu from .jar file.
	simpleMenuContent = ResourceUtility
		.loadResourceAsByteArray(SIMPLE_MENU);

	// Override with external file?
	if (externalSimpleMenuContent != null
		&& externalSimpleMenuContent.length == simpleMenuContent.length) {
	    simpleMenuContent = externalSimpleMenuContent;
	    external = true;
	}

	if (simpleMenuContent != null && simpleMenuContent.length > 0x2000) {
	    String signature = new String(simpleMenuContent,
		    Offsets.CARTMENU_SIGNATURE, 16);
	    if (signature.startsWith("The!Cart")) {
		version = signature.substring(8, 12) + "-"
			+ signature.substring(12, 14) + "-"
			+ signature.substring(14, 16);
	    }
	    simpleMenuValid = true;
	}

	// Load extended menu.
	byte[] externalExtendedMenuContent = null;
	if (jarFolder != null) {
	    try {
		File externalExtendedMenuFile = new File(jarFolder,
			EXTERNAL_EXTENDED_MENU);
		externalExtendedMenuContent = FileUtility.readBytes(
			externalExtendedMenuFile, FileUtility.MAX_SIZE_1MB,
			true);
	    } catch (CoreException ignore) {
	    }
	}

	extendedMenuContent = ResourceUtility
		.loadResourceAsByteArray(EXTENDED_MENU);

	if (externalExtendedMenuContent != null
		&& externalExtendedMenuContent.length > 0x2000) {
	    extendedMenuContent = externalExtendedMenuContent;
	}
	if (extendedMenuContent != null && extendedMenuContent.length > 0x2000) {
	    extendedMenuValid = true;
	}

	if (cartridgeMenuType.equals(CartridgeMenuType.SIMPLE)) {
	    valid = simpleMenuValid;
	} else if (cartridgeMenuType.equals(CartridgeMenuType.EXTENDED)) {
	    valid = simpleMenuValid && extendedMenuValid;
	}

    }

    public CartridgeMenuType getCartridgeMenuType() {
	return cartridgeMenuType;
    }

    public boolean isValid() {
	return valid;
    }

    public boolean isExternal() {
	return external;
    }

    public String getExternalFilePath() {
	return externalFilePath;
    }

    public String getVersion() {
	return version;
    }

    public byte[] getContent(Workbook workbook, WorkbookExport workbookExport,
	    MessageQueue messageQueue) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (workbookExport == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbookExport' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	byte[] content = null;
	if (valid) {
	    WorkbookRoot root = workbook.getRoot();
	    if (root.getCartridgeMenuType() == CartridgeMenuType.SIMPLE) {
		content = simpleMenuContent;
	    } else if (root.getCartridgeMenuType() == CartridgeMenuType.EXTENDED) {

		// Always use the current simple menu content for the extended
		// menu also. Copy the start bank main code, signature and all
		// following banks.
		content = extendedMenuContent;
		if (content != null) {

		    // Menu itself is there, let's build the list of menu
		    // entries.
		    createMenuEntries(workbook, workbookExport, messageQueue);
		    if (messageQueue.containsError()) {
			return null;
		    }

		    // Merge extended menu with (latest) simple menu.
		    // Copy first part (simple menu) and signature.
		    System.arraycopy(simpleMenuContent, 0, extendedMenuContent,
			    0, Offsets.MENU_STUDIO_ROM);
		    System.arraycopy(simpleMenuContent,
			    Offsets.CARTMENU_SIGNATURE, extendedMenuContent,
			    Offsets.CARTMENU_SIGNATURE,
			    Offsets.CARTMENU_SIGNATURE_LENGTH);

		    // Adapt default menu start vector (2 bytes) from cartridge
		    // initialization to jump table..
		    System.arraycopy(extendedMenuContent, Offsets.CARTCS,
			    simpleMenuContent,
			    Offsets.ENTRY_DEFAULT_CARTSTART + 1, 2);

		    // Copy second part (flasher and PicoDos)
		    System.arraycopy(simpleMenuContent,
			    Offsets.CARTMENU_FIRST_BANK_END,
			    extendedMenuContent,
			    Offsets.CARTMENU_FIRST_BANK_END,
			    simpleMenuContent.length
				    - Offsets.CARTMENU_FIRST_BANK_END);

		    // Set MCB content.
		    int startBankNumber = root.getMenuEntriesContentProvider()
			    .getStartBankNumber();
		    setByte(content, Offsets.CARTRIDGE_TYPE, root
			    .getCartridgeType().getNumericId());
		    setWord(content, Offsets.BANK_COUNT, root.getBankCount());
		    // +1 for "All"
		    setWord(content, Offsets.MENU_GENRES_COUNT, root
			    .getUnmodifiableGenresList().size() + 1);
		    setWord(content, Offsets.MENU_GENRES_START_BANK_NUMBER,
			    startBankNumber + 0);
		    setWord(content, Offsets.MENU_ENTRIES_COUNT, root
			    .getMenuEntriesList().size());
		    setWord(content, Offsets.MENU_ENTRIES_START_BANK_NUMBER,
			    startBankNumber + 1);

		    // Create centered title.
		    StringBuffer title = new StringBuffer(root.getTitle());
		    int padding = Math.max(0,
			    (WorkbookEntry.TITLE_LENGTH - title.length()) / 2);
		    for (int i = 0; i < padding; i++) {
			title.insert(0, ' ');
		    }
		    setString(content, Offsets.MENU_TITLE, title.toString(),
			    WorkbookEntry.TITLE_LENGTH);
		}
	    }
	}

	if (content == null) {
	    content = new byte[0];
	}
	return content;
    }

    private void createMenuEntries(Workbook workbook,
	    WorkbookExport workbookExport, MessageQueue messageQueue) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (workbookExport == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbookExport' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	// Create menu entries from workbook entries.
	WorkbookRoot root = workbook.getRoot();
	List<WorkbookEntry> workbookEntries = root.getUnmodifiableEntriesList();
	List<WorkbookMenuEntry> workbookMenuEntries = root.getMenuEntriesList();
	workbookMenuEntries.clear();

	// Prepare conversion of inner menu entries?
	Collector collector = new InnerMenuCollector(workbook);

	for (int i = 0; i < workbookEntries.size(); i++) {
	    WorkbookEntry workbookEntry = workbookEntries.get(i);

	    ImportableMenu importableMenu = null;
	    if (workbookEntry.getDisplayMode().equals(
		    DisplayMode.MULTIPLE_ENTRIES)) {
		importableMenu = workbookEntry.getContentType()
			.createImportableMenu(new byte[0]);
		if (importableMenu == null) {
		    // ERROR: Content type '{0}' does not support display mode
		    // '{1}'.
		    messageQueue.sendMessage(workbookEntry,
			    WorkbookEntry.Attributes.DISPLAY_MODE,
			    Messages.E422, workbookEntry.getContentType()
				    .toString(), DisplayMode.MULTIPLE_ENTRIES
				    .toString(), ContentType
				    .getImportableMenuList());
		    continue;
		}
	    }

	    if (importableMenu != null) {
		// Try to read the file content.
		byte[] content = workbook.getEntryFileContent(workbookEntry,
			messageQueue);

		if (content != null) {
		    importableMenu = workbookEntry.getContentType()
			    .createImportableMenu(content);

		    int result = importableMenu.collectMenuEntries(
			    workbookEntry, collector);
		    switch (result) {
		    case Result.NO_MENU_FOUND:
		    case Result.NOT_SUPPORTED_MENU_VERSION_FOUND:
		    case Result.NO_MENU_ENTRIES_FOUND:
			// ERROR: Entry has no or a not yet supported menu
			// version and its entries cannot be resolved. Select
			// display mode
			// "Single Entry" to use it. If the entry has a standard
			// menu provide the The!Cart team your example, so we
			// can add
			// it.
			messageQueue.sendMessage(workbookEntry,
				WorkbookEntry.Attributes.DISPLAY_MODE,
				Messages.E423);
			break;
		    case Result.MENU_ENTRIES_FOUND_BUT_NOT_STARTABLE:
			// INFO: To start these entries directly, export the
			// '{0}'
			// project of this ROM with the latest version and
			// import
			// the ROM into this workbook again.
			messageQueue.sendMessage(workbookEntry,
				WorkbookEntry.Attributes.DISPLAY_MODE,
				Messages.I425,
				importableMenu.getCreatingToolName());
			break;
		    case Result.MENU_ENTRIES_FOUND_AND_STARTABLE:
			break;
		    default:
			throw new RuntimeException("Unsupported result code "
				+ result + ".");
		    }
		}
	    } else {
		workbookMenuEntries.add(new WorkbookMenuEntry(workbookEntry));
	    }
	}

	// Sort menu entries.
	Collections.sort(workbookMenuEntries);
	workbookExport.setMenuEntries(workbookMenuEntries);
    }

    public static void setMenuEntriesContent(Workbook workbook, byte[] result,
	    MessageQueue messageQueue) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (result == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'result' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	// Clear result.
	Arrays.fill(result, (byte) 0xff);

	WorkbookRoot root = workbook.getRoot();

	List<Genre> genresList = Genre.setGenreEntriesContent(workbook, result);

	// Create menu entries from workbook entries.
	List<WorkbookMenuEntry> workbookMenuEntries = root.getMenuEntriesList();
	int bankSize = workbook.getRoot().getBankSize();

	int entryOffset = bankSize;
	Genre allGenre = genresList.get(0);
	StringBuilder builder = new StringBuilder(WorkbookEntry.TITLE_LENGTH);
	for (int i = 0; i < workbookMenuEntries.size(); i++) {
	    WorkbookMenuEntry workbookMenuEntry = workbookMenuEntries.get(i);
	    WorkbookEntry workbookEntry = workbookMenuEntry.getWorkbookEntry();

	    // Technical properties are written in the first step.
	    int offset = entryOffset + Offsets.MENU_ENTRY_NUMBER;
	    setWord(result, offset, i);

	    offset = entryOffset + Offsets.MENU_ENTRY_THE_CART_MODE;
	    setByte(result, offset, workbookEntry.getContentType()
		    .getTheCartMode());

	    offset = entryOffset + Offsets.MENU_ENTRY_CONTENT_SIZE;
	    setLong(result, offset, workbookEntry.getContentSize());

	    offset = entryOffset + Offsets.MENU_ENTRY_START_BANK_NUMBER;
	    setWord(result, offset, workbookEntry.getStartBankNumber());

	    // Some cartridge types do not have relative start bank 0.
	    // Therefore The!Cart supports "uneven" start banks in modes greater
	    // then 8k.
	    int initialBankNumber = workbookEntry.getContentType()
		    .getCartridgeType().getInitialBankNumber();
	    offset = entryOffset + Offsets.MENU_ENTRY_INITIAL_BANK_NUMBER;
	    setWord(result, offset, initialBankNumber);

	    int loaderBaseAddress = AtrLoader.getBaseAddress(workbookEntry);
	    offset = entryOffset + Offsets.MENU_ENTRY_LOADER_BASE_ADDRESS;
	    setWord(result, offset, loaderBaseAddress);

	    offset = entryOffset + Offsets.MENU_ENTRY_SOURCE_TYPE;
	    setByte(result, offset, workbookMenuEntry.getSourceType());

	    offset = entryOffset + Offsets.MENU_ENTRY_ITEM_MENU_VERSION;
	    setByte(result, offset, workbookMenuEntry.getItemMenuVersion());

	    offset = entryOffset + Offsets.MENU_ENTRY_ITEM_NUMBER;
	    setByte(result, offset, workbookMenuEntry.getItemNumber());

	    // Text properties are written in the second step.
	    String title = workbookMenuEntry.getTitle().trim();
	    offset = entryOffset + Offsets.MENU_ENTRY_TITLE_LENGTH;
	    setByte(result, offset, title.length());
	    offset = entryOffset + Offsets.MENU_ENTRY_TITLE;
	    builder.setLength(0);
	    builder.append(workbookMenuEntry.getTitle());
	    builder.append("                                        ");
	    for (int j = 0; j < WorkbookEntry.TITLE_LENGTH; j++) {
		result[offset++] = (byte) builder.charAt(j);
	    }

	    // Grouping and filter properties are written in the third step.
	    offset = entryOffset + Offsets.MENU_ENTRY_GENRE_NUMBER;
	    String genreName = workbookMenuEntry.getWorkbookEntry()
		    .getGenreName();
	    Genre genre = null;
	    if (StringUtility.isSpecified(genreName)) {
		for (Genre otherGenre : genresList) {
		    if (otherGenre.getName().equals(genreName)) {
			genre = otherGenre;
			break;
		    }
		}
	    }

	    // Default genre to "All".
	    if (genre == null) {
		genre = allGenre;
	    }
	    setByte(result, offset, genre.getNumber());

	    offset = entryOffset + Offsets.MENU_ENTRY_FAVORITE_INDICATOR;
	    setByte(result, offset, workbookEntry.getFavoriteIndicator() ? 0x80
		    : 0x00);
	    if (workbookEntry.getFavoriteIndicator()) {
		genre.setFavoritesAvailableIndicator();
		allGenre.setFavoritesAvailableIndicator();
	    }
	    entryOffset += MENU_ENTRY_LENGTH;

	}
    }

    private static void setByte(byte[] content, int offset, int value) {
	if (value > 256) {
	    throw new IllegalArgumentException("Value " + value
		    + " is no byte value");
	}
	content[offset] = (byte) (value & 0xff);
    }

    private static void setWord(byte[] content, int offset, int value) {
	content[offset + 0] = (byte) (value & 0xff);
	content[offset + 1] = (byte) (value >>> 8 & 0xff);
    }

    private static void setLong(byte[] content, int offset, long value) {
	content[offset + 0] = (byte) (value & 0xff);
	content[offset + 1] = (byte) (value >>> 8 & 0xff);
	content[offset + 2] = (byte) (value >>> 16 & 0xff);
	content[offset + 3] = (byte) (value >>> 24 & 0xff);
    }

    private static void setString(byte[] content, int offset, String value,
	    int length) {
	for (int i = 0; i < length; i++) {
	    char c;
	    if (i < value.length()) {
		c = value.charAt(i);
	    } else {
		c = 32;
	    }
	    setByte(content, offset + i, c);
	}
    }
}