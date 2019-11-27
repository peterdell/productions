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

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.ASCIIString;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.thecartstudio.DataTypes;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.CheckSumContentProvider;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.MenuContentProvider;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.MenuEntriesContentProvider;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.MenuStartupContentProvider;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.UserSpaceContentProvider;

/**
 * State container for Workbook
 * 
 * @author Peter Dell
 */
public final class WorkbookRoot {

    public static final class Attributes {
	private Attributes() {
	}

	public static final String ELEMENT_NAME = "workbook";

	public static final Attribute TITLE = new Attribute("title",
		DataTypes.WorkbookRoot_Title);
	public static final Attribute FLASH_TARGET_TYPE = new Attribute(
		"flashTargetType", DataTypes.WorkbookRoot_FlashTargetType);
	public static final Attribute BANK_COUNT = new Attribute("bankCount",
		DataTypes.WorkbookRoot_BankCount);
	public static final Attribute BANK_SIZE = new Attribute("bankSize",
		DataTypes.WorkbookRoot_BankSize);
	public static final Attribute CARTRDIGE_TYPE = new Attribute(
		"cartridgeType", DataTypes.WorkbookRoot_CartridgeType);
	public static final Attribute CARTRDIGE_MENU_TYPE = new Attribute(
		"cartridgeMenuType", DataTypes.WorkbookRoot_CartridgeMenuType);
	public static final Attribute USER_SPACE_SIZE = new Attribute(
		"userSpaceSize", DataTypes.WorkbookRoot_UserSpaceSize);
    }

    // Constants
    public static final int TITLE_LENGTH = 40;
    public static final ASCIIString TITLE_CHARACTERS = new ASCIIString(
	    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_ ");

    public static final int MAX_BANK_COUNT = 65536;
    public static final int MIN_BANK_SIZE = 1 * KB;
    public static final int MAX_BANK_SIZE = 64 * KB;

    // Ensure that all genres pointers fit into one page.
    // This also implicitly ensures that all genre names fit into one bank
    // because the maximum length per genre is 20.
    public static final int MAX_GENRE_COUNT = 128;

    private String title;
    private FlashTargetType flashTargetType;
    private int bankCount;
    private int bankSize;
    private CartridgeType cartridgeType;
    private CartridgeMenuType cartridgeMenuType;
    private int userSpaceSize;

    private transient MenuContentProvider menuContentProvider;
    private transient MenuEntriesContentProvider menuEntriesContentProvider;
    private transient MenuStartupContentProvider menuStartupContentProvider;

    private transient CheckSumContentProvider checkSumContentProvider;
    private transient UserSpaceContentProvider userSpaceContentProvider;
    private transient boolean banksListInitialized;
    private transient List<WorkbookBank> banksList;
    private transient int reservedBanksCount;

    private List<WorkbookGenre> genresList;
    private List<WorkbookGenre> unmodifiableGenresList;

    private List<WorkbookEntry> entriesList;
    private List<WorkbookEntry> unmodifiableEntriesList;
    private transient Map<String, WorkbookEntry> fileEntriesMap;
    private transient int requiredBanksCount;
    private transient List<WorkbookMenuEntry> menuEntriesList;

    /**
     * Created by workbook only.
     */
    WorkbookRoot() {
	title = "";

	menuContentProvider = new MenuContentProvider();
	menuEntriesContentProvider = new MenuEntriesContentProvider();
	menuStartupContentProvider = new MenuStartupContentProvider();

	checkSumContentProvider = new CheckSumContentProvider();
	userSpaceContentProvider = new UserSpaceContentProvider();

	banksListInitialized = false;
	banksList = new ArrayList<WorkbookBank>();
	reservedBanksCount = 0;

	setFlashTargetType(FlashTargetType.THECART_128MB,
		CartridgeType.CARTRIDGE_THECART_128M, 0, 0);
	cartridgeMenuType = CartridgeMenuType.EXTENDED;
	userSpaceSize = 0;

	genresList = new ArrayList<WorkbookGenre>();
	unmodifiableGenresList = Collections.unmodifiableList(genresList);

	entriesList = new ArrayList<WorkbookEntry>();
	unmodifiableEntriesList = Collections.unmodifiableList(entriesList);
	fileEntriesMap = new TreeMap<String, WorkbookEntry>();
	requiredBanksCount = 0;
	menuEntriesList = new ArrayList<WorkbookMenuEntry>();
    }

    public void setTitle(String title) {
	if (title == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'title' must not be null.");
	}
	this.title = title.trim();
    }

    public String getTitle() {
	return title;
    }

    public FlashTargetType getFlashTargetType() {
	return flashTargetType;
    }

    public void setFlashTargetType(FlashTargetType flashTargetType,
	    CartridgeType cartridgeType, int bankCount, int bankSize) {
	if (flashTargetType == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'flashTargetType' must not be null.");
	}
	if (cartridgeType == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'cartridgeType' must not be null.");
	}
	this.flashTargetType = flashTargetType;

	// Override parameters with target type defaults?
	if (flashTargetType != FlashTargetType.USER_DEFINED) {
	    cartridgeType = flashTargetType.getCartridgeType();
	    bankCount = flashTargetType.getBankCount();
	    bankSize = flashTargetType.getBankSize();
	}

	setCartridgeType(cartridgeType);

	// Normalize parameter bounds.
	if (bankCount < 0) {
	    bankCount = 1;
	}
	if (bankCount > MAX_BANK_COUNT) {
	    bankCount = MAX_BANK_COUNT;
	}
	this.bankCount = bankCount;

	// Normalize parameter bounds, align to 1k.
	bankSize &= ~(MIN_BANK_SIZE - 1);
	if (bankSize <= 0) {
	    bankSize = 8192;
	}
	if (bankSize > MAX_BANK_SIZE) {
	    bankSize = MAX_BANK_SIZE;
	}
	this.bankSize = bankSize;
    }

    public int getBankCount() {
	return bankCount;
    }

    public int getBankSize() {
	return bankSize;
    }

    /**
     * Gets the complete image size which is always a multiple of the bank size.
     * 
     * @return The complete image size which is always a multiple of the bank
     *         size.
     */
    public int getImageSize() {
	return bankCount * bankSize;
    }

    public void setCartridgeType(CartridgeType cartridgeType) {
	if (cartridgeType == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'cartridgeType' must not be null.");
	}
	this.cartridgeType = cartridgeType;
    }

    public CartridgeType getCartridgeType() {
	return cartridgeType;
    }

    public void setCartridgeMenuType(CartridgeMenuType cartridgeMenuType) {
	if (cartridgeMenuType == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'cartridgeMenuType' must not be null.");
	}
	this.cartridgeMenuType = cartridgeMenuType;
    }

    public CartridgeMenuType getCartridgeMenuType() {
	return cartridgeMenuType;
    }

    /**
     * Gets the user space size in bytes.
     * 
     * @return The user space size in bytes, non-negative integer.
     */
    public int getUserSpaceSize() {
	return userSpaceSize;
    }

    /**
     * Gets the size of the user space alignment. This is the maximum of the
     * actual bank size and the flask block size. Technically this is a
     * limitation of the flasher software, but there not really a point in
     * having smaller blocks.
     * 
     * @return The user space bank size in bytes or <code>0</code>
     */
    public int getUserSpaceAlignmentSize() {
	return Math.max(bankSize, cartridgeType.getFlashBlockSize());
    }

    /**
     * Gets the number of user space banks.
     * 
     * @return The number of user space banks, a non-negative integer.
     */
    public int getUserSpaceBanksCount() {
	return (userSpaceSize + bankSize - 1) / bankSize;
    }

    /**
     * Set the user space size in bytes.
     * 
     * @param userSpaceSize
     *            The user space size in bytes, non-negative integer.
     */
    public void setUserSpaceSize(int userSpaceSize) {
	this.userSpaceSize = userSpaceSize;
    }

    /**
     * Gets the modifiable list of genres.
     * 
     * @return The modifiable list of genres, may be empty, not
     *         <code>null</code>.
     */
    public List<WorkbookGenre> getGenresList() {
	return genresList;
    }

    /**
     * Gets the modifiable list of genres.
     * 
     * @return The modifiable list of genres, may be empty, not
     *         <code>null</code>.
     */
    public List<WorkbookGenre> getUnmodifiableGenresList() {
	return unmodifiableGenresList;
    }

    /**
     * Gets the list of genre names.
     * 
     * @return The unmodifiable sorted list of genre names, may be empty, not
     *         <code>null</code>.
     */
    public List<String> getSortedGenreNamesList() {
	List<String> result = new ArrayList<String>(genresList.size());
	for (WorkbookGenre genre : genresList) {
	    result.add(genre.getName());
	}
	Collections.sort(result, StringUtility.CASE_INSENSITIVE_COMPARATOR);
	Collections.unmodifiableList(result);
	return result;
    }

    final List<ReservedContentProvider> getReservedContentProviders() {
	List<ReservedContentProvider> result = new ArrayList<ReservedContentProvider>(
		4);
	result.add(menuContentProvider);
	result.add(menuEntriesContentProvider);
	result.add(menuStartupContentProvider);
	result.add(checkSumContentProvider);
	result.add(userSpaceContentProvider);
	return result;
    }

    final MenuEntriesContentProvider getMenuEntriesContentProvider() {
	return menuEntriesContentProvider;
    }

    public boolean isBanksListInitialized() {
	return banksListInitialized;
    }

    final void clearBanksList() {
	banksListInitialized = false;
	banksList.clear();
	for (int bankNumber = 0; bankNumber < bankCount; bankNumber++) {
	    banksList.add(new WorkbookBank(bankNumber));
	}
	reservedBanksCount = 0;
    }

    final void setBanksListInitialized(boolean banksListInitialized,
	    int reservedBanksCount) {
	this.banksListInitialized = banksListInitialized;
	this.reservedBanksCount = reservedBanksCount;
    }

    public List<WorkbookBank> getBanksList() {
	return banksList;
    }

    public WorkbookBanksSummary getWorkbookBanksSummary() {
	WorkbookBanksSummary result = new WorkbookBanksSummary();
	result.definedBanksCount = bankCount;
	result.reservedBanksCount = reservedBanksCount;
	result.requiredBanksCount = requiredBanksCount;
	return result;
    }

    public WorkbookEntry getEntry(int index) {
	return entriesList.get(index);
    }

    public int getEntryCount() {
	return entriesList.size();
    }

    /**
     * Gets the a modifiable list of entries.
     * 
     * @return The modifiable list of entries, maybe be empty, not
     *         <code>null</code>.
     */
    final List<WorkbookEntry> getEntriesList() {
	return entriesList;
    }

    final WorkbookEntry getEntry(String fileName) {
	if (fileName == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'fileName' must not be null.");
	}
	fileName = fileName.toUpperCase();
	return fileEntriesMap.get(fileName);
    }

    /**
     * Atomic option, does not fire mode change. This must be done by the
     * caller.
     * 
     * @param index
     *            The index in the entries list, a non-negative integer.
     * @param entry
     *            The entry, not <code>null</code>.
     */
    final void addEntry(int index, WorkbookEntry entry) {
	if (entry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'entry' must not be null.");
	}
	// File entries must have a unique file name.
	if (entry.getType() == WorkbookEntryType.FILE_ENTRY) {
	    String key = entry.getFileName().toUpperCase();
	    if (fileEntriesMap.containsKey(key)) {
		throw new RuntimeException("Entry with file name '" + key
			+ "' is already present.");
	    }
	    fileEntriesMap.put(key, entry);
	}
	entriesList.add(index, entry);
	requiredBanksCount += entry.getRequiredBanksCount();
    }

    /**
     * Atomic option, does not fire mode change. This must be done by the
     * caller.
     * 
     * @param index
     *            The index in the entries list, a non-negative integer.
     * @return entry The entry or <code>null</code>.
     */
    final WorkbookEntry removeEntry(int index) {
	WorkbookEntry result = entriesList.remove(index);
	requiredBanksCount -= result.getRequiredBanksCount();
	if (result.getType() == WorkbookEntryType.FILE_ENTRY) {
	    String key = result.getFileName().toUpperCase();
	    fileEntriesMap.remove(key);
	}
	return result;
    }

    /**
     * Gets the unmodifiable list of entries.
     * 
     * @return The unmodifiable list of entries, may be empty, not
     *         <code>null</code>.
     */
    public List<WorkbookEntry> getUnmodifiableEntriesList() {
	return unmodifiableEntriesList;
    }

    /**
     * Gets the modifiable list of menu entries.
     * 
     * @return the modifiable list of menu entries, may be empty, not
     *         <code>null</code>.
     */
    public List<WorkbookMenuEntry> getMenuEntriesList() {
	return menuEntriesList;
    }

    /**
     * Creates a copy including all non-transient attributes.
     * 
     * @return The copied instance, not <code>null</code>.
     */
    public WorkbookRoot createCopy() {
	WorkbookRoot result = new WorkbookRoot();
	result.title = title;
	result.flashTargetType = flashTargetType;
	result.cartridgeType = cartridgeType;
	result.bankCount = bankCount;
	result.bankSize = bankSize;
	result.cartridgeMenuType = cartridgeMenuType;
	result.userSpaceSize = userSpaceSize;

	// The maps are currently not really required as there are no persistent
	// foreign keys.
	Map<WorkbookEntry, WorkbookEntry> entryMap = new IdentityHashMap<WorkbookEntry, WorkbookEntry>();
	for (WorkbookEntry entry : unmodifiableEntriesList) {
	    WorkbookEntry copy = entry.createCopy();
	    entryMap.put(entry, copy);
	    result.entriesList.add(entry.createCopy());
	}

	Map<WorkbookGenre, WorkbookGenre> genreMap = new IdentityHashMap<WorkbookGenre, WorkbookGenre>();
	for (WorkbookGenre genre : unmodifiableGenresList) {
	    WorkbookGenre copy = genre.createCopy();
	    genreMap.put(genre, copy);
	    result.genresList.add(genre.createCopy());
	}
	return result;
    }

    public boolean contentStructureEquals(WorkbookRoot other) {
	if (other == null) {
	    return false;
	}
	if (!other.flashTargetType.equals(flashTargetType)) {
	    return false;
	}
	if (!other.cartridgeType.equals(cartridgeType)) {
	    return false;
	}
	if (other.bankCount != bankCount) {
	    return false;
	}
	if (other.bankSize != bankSize) {
	    return false;
	}
	if (!other.cartridgeMenuType.equals(cartridgeMenuType)) {
	    return false;
	}
	if (other.userSpaceSize != userSpaceSize) {
	    return false;
	}

	return true;

    }

    public boolean contentEquals(WorkbookRoot other) {
	if (other == null) {
	    return false;
	}
	if (!other.title.equals(title)) {
	    return false;
	}
	if (!contentStructureEquals(other)) {
	    return false;
	}
	if (!(other.unmodifiableGenresList.size() == unmodifiableGenresList
		.size())) {
	    return false;
	}
	for (int i = 0; i < other.unmodifiableGenresList.size(); i++) {
	    if (!(other.unmodifiableGenresList.get(i)
		    .equals(unmodifiableGenresList.get(i)))) {
		return false;
	    }
	}
	if (!(other.unmodifiableEntriesList.size() == unmodifiableEntriesList
		.size())) {
	    return false;
	}
	for (int i = 0; i < other.unmodifiableEntriesList.size(); i++) {
	    if (!(other.unmodifiableEntriesList.get(i)
		    .equals(unmodifiableEntriesList.get(i)))) {
		return false;
	    }
	}

	return true;

    }

    final void serialize(Element element) {
	if (element == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'element' must not be null.");
	}
	Attributes.TITLE.serializeString(element, title);
	Attributes.FLASH_TARGET_TYPE
		.serializeValueSet(element, flashTargetType);
	Attributes.BANK_COUNT.serializeInteger(element, bankCount);
	Attributes.BANK_SIZE.serializeInteger(element, bankSize);
	Attributes.CARTRDIGE_TYPE.serializeValueSet(element, cartridgeType);
	Attributes.CARTRDIGE_MENU_TYPE.serializeValueSet(element,
		cartridgeMenuType);
	Attributes.USER_SPACE_SIZE.serializeInteger(element, userSpaceSize);

    }

    final void deserialize(org.xml.sax.Attributes attributes)
	    throws SAXException {
	if (attributes == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'attributes' must not be null.");
	}
	String value;
	value = Attributes.TITLE.deserializeString(attributes);
	if (value != null) {
	    title = value;
	}

	FlashTargetType flashTargetType = Attributes.FLASH_TARGET_TYPE
		.deserializeValueSet(attributes, FlashTargetType.class);
	if (flashTargetType == null) {
	    flashTargetType = this.flashTargetType;
	}
	CartridgeType cartridgeType = Attributes.CARTRDIGE_TYPE
		.deserializeValueSet(attributes, CartridgeType.class);
	if (cartridgeType == null) {
	    cartridgeType = this.cartridgeType;
	}
	int bankCount = Attributes.BANK_COUNT.deserializeInteger(attributes);
	int bankSize = Attributes.BANK_SIZE.deserializeInteger(attributes);

	// Re-adapt bank count and size to current flash target type.
	setFlashTargetType(flashTargetType, cartridgeType, bankCount, bankSize);

	cartridgeMenuType = Attributes.CARTRDIGE_MENU_TYPE.deserializeValueSet(
		attributes, CartridgeMenuType.class);

	userSpaceSize = Attributes.USER_SPACE_SIZE
		.deserializeInteger(attributes);
    }

    @Override
    public String toString() {
	return "title=" + title + " flashTargetType=" + flashTargetType
		+ " bankCount=" + bankCount + " bankSize=" + bankSize
		+ " cartridgeMenuType=" + cartridgeMenuType + " userSpaceSize="
		+ userSpaceSize + " genresList=" + unmodifiableGenresList
		+ " entriesList=" + unmodifiableEntriesList;
    }
}
