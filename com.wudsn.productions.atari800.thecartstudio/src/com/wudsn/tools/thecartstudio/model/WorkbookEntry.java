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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.HexUtility;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.thecartstudio.DataTypes;
import com.wudsn.tools.thecartstudio.Messages;

/**
 * Container for a single entry.
 * 
 * @author Peter Dell
 */
public final class WorkbookEntry implements Comparable<WorkbookEntry> {

	public static final class Attributes {
		private Attributes() {
		}

		public static final String ELEMENT_NAME = "entry";

		public static final Attribute ID = new Attribute("id",
				DataTypes.WorkbookEntry_Id);
		public static final Attribute TYPE = new Attribute("type",
				DataTypes.WorkbookEntry_Type);
		public static final Attribute TITLE = new Attribute("title",
				DataTypes.WorkbookEntry_Title);
		public static final Attribute GENRE_NAME = new Attribute("genreName",
				DataTypes.WorkbookEntry_GenreName);
		public static final Attribute FAVORITE_INDICATOR = new Attribute(
				"favoriteIndicator", DataTypes.WorkbookEntry_FavoriteIndicator);
		public static final Attribute FILE_PATH = new Attribute("filePath",
				DataTypes.WorkbookEntry_FilePath);
		public static final Attribute FILE_NAME = new Attribute("fileName",
				DataTypes.WorkbookEntry_FileName);
		public static final Attribute FILE_SIZE = new Attribute("fileSize",
				DataTypes.WorkbookEntry_FileSize);
		public static final Attribute FILE_HEADER_TYPE = new Attribute(
				"fileHeaderType", DataTypes.WorkbookEntry_FileHeaderType);
		public static final Attribute CONTENT_SIZE = new Attribute(
				"contentSize", DataTypes.WorkbookEntry_ContentSize);
		public static final Attribute CONTENT_CRC32 = new Attribute(
				"contentCRC32", DataTypes.WorkbookEntry_ContentCRC32);
		public static final Attribute CONTENT_TYPE = new Attribute(
				"contentType", DataTypes.WorkbookEntry_ContentType);
		public static final Attribute DISPLAY_MODE = new Attribute(
				"displayMode", DataTypes.WorkbookEntry_DisplayMode);
		public static final Attribute PARAMETERS = new Attribute("parameters",
				DataTypes.WorkbookEntry_Parameters);
		public static final Attribute START_BANK_FIXED_INDICATOR = new Attribute(
				"startBankFixedIndicator",
				DataTypes.WorkbookEntry_StartBankFixedIndicator);
		public static final Attribute START_BANK = new Attribute(
				"startBankNumber", DataTypes.WorkbookEntry_StartBankNumber);
		public static final Attribute REQUIRED_BANKS_COUNT = new Attribute(
				"requiredBanksCount",
				DataTypes.WorkbookEntry_RequiredBanksCount);
		public static final Attribute BANKS_ASSIGNED_INDICATOR = new Attribute(
				"banksAssignedIndicator",
				DataTypes.WorkbookEntry_BanksAssignedIndicator);
	}

	public static final class Parameter implements Comparable<Parameter> {
		public final String key;
		public final String value;

		public Parameter(int key, String value) {
			this("$" + HexUtility.getLongValueHexString(key), value);
		}

		public Parameter(String key, int value) {
			this(key, "$" + HexUtility.getLongValueHexString(value));
		}

		public Parameter(int key, int value) {
			this("$" + HexUtility.getLongValueHexString(key), "$"
					+ HexUtility.getLongValueHexString(value));
		}

		public Parameter(String key, String value) {
			if (key == null) {
				throw new IllegalArgumentException(
						"Parameter 'key' must not be null.");
			}
			if (StringUtility.isEmpty(key)) {
				throw new IllegalArgumentException(
						"Parameter 'key' must not be empty.");

			}
			if (value == null) {
				throw new IllegalArgumentException(
						"Parameter 'value' must not be null.");
			}
			if (StringUtility.isEmpty(value)) {
				throw new IllegalArgumentException(
						"Parameter 'value' must not be empty.");
			}
			this.key = key.trim().toLowerCase();
			this.value = value.trim().toLowerCase();
		}

		public boolean isKeyInteger() {
			return isInteger(key);
		}

		public int getKeyAsInteger() {
			if (isKeyInteger()) {
				return Integer.parseInt(key.substring(1), 16);
			}
			return -1;
		}

		public boolean isValueInteger() {
			return isInteger(value);
		}

		public int getValueAsInteger() {
			if (isValueInteger()) {
				return Integer.parseInt(value.substring(1), 16);
			}
			return -1;
		}

		/**
		 * Determine if the specified value is a valid, non-negative integer in
		 * hex notation.
		 * 
		 * @param value
		 *            The value, may be empty, not <code>null</code>.
		 * @return <code>true</code> if the specified value is a valid,
		 *         non-negative integer in hex notation, <code>false</code>
		 *         otherwise.
		 */
		private static boolean isInteger(String value) {
			if (value == null) {
				throw new IllegalArgumentException(
						"Parameter 'value' must not be null.");
			}
			if (!value.startsWith("$")) {
				return false;
			}
			try {
				int result = Integer.parseInt(value.substring(1), 16);
				return result >= 0;
			} catch (NumberFormatException ignore) {
				return false;

			}
		}

		@Override
		public int hashCode() {
			return key.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return key.equals(((Parameter) o).key);
		}

		@Override
		public int compareTo(Parameter o) {
			// Sort non integer keys first, then by keys
			if (!isKeyInteger() && o.isKeyInteger()) {
				return -1;
			}
			if (isKeyInteger() && !o.isKeyInteger()) {
				return +1;
			}
			// Both keys integers?
			if (isKeyInteger()) {
				return getKeyAsInteger() - o.getKeyAsInteger();
			}

			// Both keys string.
			return value.compareTo(o.value);

		}

		@Override
		public String toString() {
			return key + "=" + value;
		}

		public static String getParametersString(List<Parameter> parameters) {
			if (parameters == null) {
				throw new IllegalArgumentException(
						"Parameter 'parameters' must not be null.");
			}
			StringBuilder builder = new StringBuilder();
			for (Parameter parameter : parameters) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(parameter.key);
				builder.append("=");
				builder.append(parameter.value);
			}
			return builder.toString().toLowerCase();
		}

		public static List<Parameter> getParametersList(String parameters)
				throws CoreException {
			if (parameters == null) {
				throw new IllegalArgumentException(
						"Parameter 'parameters' must not be null.");
			}
			List<Parameter> result = new ArrayList<Parameter>();
			String[] parts = parameters.split(",");
			for (String part : parts) {
				if (StringUtility.isSpecified(part)) {
					String[] keyAndValue = part.split("=");
					if (keyAndValue.length != 2) {
						// ERROR: Invalid parameter syntax '{0}'. Specify
						// parameters
						// in
						// the form 'base=$100' for assignments and '$1234=siov'
						// for
						// patches.
						throw new CoreException(Messages.E424, part);
					}
					Parameter parameter = new Parameter(keyAndValue[0],
							keyAndValue[1]);
					if (!parameter.isValid()) {
						// ERROR: Invalid parameter syntax '{0}'. Specify
						// parameters
						// in
						// the form 'base=$100' for assignments and '$1234=siov'
						// for
						// patches.
						throw new CoreException(Messages.E424, part);
					}
					result.add(parameter);
				}
			}
			return result;
		}

		private boolean isValid() {
			// The key or the value must be an integer.
			boolean assignment = !isKeyInteger() && isValueInteger();
			boolean symbolicPatch = isKeyInteger() && !isValueInteger();
			boolean directPatch = isKeyInteger() && isValueInteger()
					&& getValueAsInteger() < 256;
			return assignment || symbolicPatch || directPatch;
		}
	}

	public static final int START_BANK_UNDEFINED = -1;
	public static final int TITLE_LENGTH = Attributes.TITLE.getDataType()
			.getMaximumLength();

	private WorkbookEntryType type;
	private String title;
	private String genreName;
	private boolean favoriteIndicator;

	private transient String filePath;
	private String fileName;
	private long fileSize;
	private FileHeaderType fileHeaderType;
	private int contentCRC32;
	private ContentType contentType;

	private DisplayMode displayMode;
	private String parameters;
	private transient List<Parameter> parametersList;

	private boolean startBankFixedIndicator;
	private int startBankNumber;
	private int requiredBanksCount;
	private transient boolean alignmentBanksCountValid;
	private transient int alignmentBanksCount;
	private transient boolean banksAssigned;

	/**
	 * Created by workbook only.
	 */
	WorkbookEntry() {
		type = WorkbookEntryType.FILE_ENTRY;
		title = "";
		genreName = "";
		favoriteIndicator = false;
		filePath = "";
		fileName = "";
		fileSize = 0;
		fileHeaderType = FileHeaderType.NONE;
		contentCRC32 = 0;
		contentType = ContentType.UNKNOWN;
		displayMode = DisplayMode.SINGLE_ENTRY;
		parameters = "";
		parametersList = null;
		startBankNumber = START_BANK_UNDEFINED;
		requiredBanksCount = 0;
		alignmentBanksCountValid = false;
		alignmentBanksCount = 0;
		banksAssigned = false;
	}

	public WorkbookEntryType getType() {
		return type;
	}

	public void setType(WorkbookEntryType type) {
		if (type == null) {
			throw new IllegalArgumentException(
					"Parameter 'type' must not be null.");
		}
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null) {
			throw new IllegalArgumentException(
					"Parameter 'title' must not be null.");
		}
		title = title.trim();
		// Always truncate title to simplify handling.
		int maximumLength = Attributes.TITLE.getDataType().getMaximumLength();
		if (title.length() > maximumLength) {
			title = title.substring(0, maximumLength);
		}
		this.title = title;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		if (genreName == null) {
			throw new IllegalArgumentException(
					"Parameter 'genreName' must not be null.");
		}
		this.genreName = genreName;
	}

	public boolean getFavoriteIndicator() {
		return favoriteIndicator;
	}

	public void setFavoriteIndicator(boolean favoriteIndicator) {
		this.favoriteIndicator = favoriteIndicator;
	}

	/**
	 * Gets the transient absolute file path.
	 * 
	 * @return The transient absolute file path, may be empty, not
	 *         <code>null</code>.
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the transient absolute file path.
	 * 
	 * @param filePath
	 *            The transient absolute file path, may be empty, not
	 *            <code>null</code>.
	 */
	public void setFilePath(String filePath) {
		if (filePath == null) {
			throw new IllegalArgumentException(
					"Parameter 'filePath' must not be null.");
		}
		this.filePath = filePath;
	}

	/**
	 * Gets the file name.
	 * 
	 * @return The file name, may be empty, not <code>null</code>.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 * 
	 * @param fileName
	 *            The file name, may be empty, not <code>null</code>.
	 */
	public void setFileName(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException(
					"Parameter 'fileName' must not be null.");
		}
		this.fileName = fileName;
	}

	public String getInstanceString() {
		return title + " (" + filePath + ")";
	}

	/**
	 * Gets the file size, i.e. the file size including the header size.
	 * 
	 * @return The file size, a non-negative long value.
	 */
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public FileHeaderType getFileHeaderType() {
		return fileHeaderType;
	}

	public void setFileHeaderType(FileHeaderType fileHeaderType) {
		if (fileHeaderType == null) {
			throw new IllegalArgumentException(
					"Parameter 'fileHeaderType' must not be null.");
		}
		this.fileHeaderType = fileHeaderType;
	}

	/**
	 * Gets the file content size in bytes, i.e. the file size without the
	 * header size.
	 * 
	 * @return The file content size in bytes, a non-negative long value.
	 */
	public long getContentSize() {
		return fileSize - fileHeaderType.getHeaderSize();
	}

	public int getContentCRC32() {
		return contentCRC32;
	}

	public void setContentCRC32(int contentCRC32) {
		this.contentCRC32 = contentCRC32;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		if (contentType == null) {
			throw new IllegalArgumentException(
					"Parameter 'contentType' must not be null.");
		}
		this.contentType = contentType;
		alignmentBanksCountValid = false;
	}

	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(DisplayMode displayMode) {
		if (displayMode == null) {
			throw new IllegalArgumentException(
					"Parameter 'displayMode' must not be null.");
		}
		this.displayMode = displayMode;
	}

	public void setParameters(String parameters) {
		if (parameters == null) {
			throw new IllegalArgumentException(
					"Parameter 'parameters' must not be null.");
		}
		this.parameters = parameters;
		this.parametersList = null;
	}

	/**
	 * Gets the parameters as string.
	 * 
	 * @return The parameters as string, may be empty, not <code>null</code>.
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * Set the parameters as list after validation.
	 * 
	 * @param parametersList
	 *            The list of parameters after successful validation or
	 *            <code>null</code> if the parameters are no valid.
	 */
	public void setParametersList(List<Parameter> parametersList) {
		this.parametersList = parametersList;
	}

	/**
	 * Gets the parameters as list after validation.
	 * 
	 * @return The list of parameters after successful validation or
	 *         <code>null</code> if the parameter have not been validated yet.
	 */
	public List<Parameter> getParametersList() {
		return parametersList;
	}

	public boolean isStartBankFixed() {
		return startBankFixedIndicator;
	}

	public void setStartBankFixedIndicator(boolean startBankFixedIndicator) {
		this.startBankFixedIndicator = startBankFixedIndicator;
	}

	public int getStartBankNumber() {
		return startBankNumber;
	}

	public void setStartBankNumber(int startBank) {
		this.startBankNumber = startBank;
	}

	public int getRequiredBanksCount() {
		return requiredBanksCount;
	}

	public void setRequiredBanksCount(int requiredBanks) {
		this.requiredBanksCount = requiredBanks;
		alignmentBanksCountValid = false;
	}

	/**
	 * Gets the number of banks required as alignment. Alignment is performed in
	 * way that the result is always a power of 2.
	 * 
	 * @return The number of banks required as alignment. For unknown and plain
	 *         file entries, the alignment is a single bank, no matter what the
	 *         bank size is.
	 */
	public int getAlignmentBanksCount() {
		if (!alignmentBanksCountValid) {
			if (contentType.isAligmentRequired()) {
				alignmentBanksCount = Integer.highestOneBit(requiredBanksCount);
				if (alignmentBanksCount != Integer
						.lowestOneBit(requiredBanksCount)) {
					alignmentBanksCount = alignmentBanksCount << 1;
				}
				if (alignmentBanksCount == 0) {
					alignmentBanksCount = 1;
				}
			} else {
				alignmentBanksCount = 1;
			}
			alignmentBanksCountValid = true;
		}
		return alignmentBanksCount;
	}

	public boolean areBanksAssigned() {
		return banksAssigned;
	}

	public void setBanksAssigned(boolean banksAssigned) {
		this.banksAssigned = banksAssigned;
	}

	final void serialize(Element element) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Parameter 'element' must not be null.");
		}
		Attributes.TYPE.serializeValueSet(element, getType());
		Attributes.TITLE.serializeString(element, getTitle());
		Attributes.GENRE_NAME.serializeString(element, getGenreName());
		Attributes.FAVORITE_INDICATOR.serializeBoolean(element,
				getFavoriteIndicator());
		Attributes.FILE_NAME.serializeString(element, getFileName());
		Attributes.FILE_SIZE.serializeLong(element, getFileSize());
		Attributes.FILE_HEADER_TYPE.serializeValueSet(element,
				getFileHeaderType());
		Attributes.CONTENT_CRC32.serializeLongAsHex(element,
				getContentCRC32() & 0xffffffffl);
		Attributes.CONTENT_TYPE.serializeValueSet(element, getContentType());
		Attributes.DISPLAY_MODE.serializeValueSet(element, getDisplayMode());
		Attributes.PARAMETERS.serializeString(element, getParameters());
		Attributes.START_BANK_FIXED_INDICATOR.serializeBoolean(element,
				isStartBankFixed());
		Attributes.START_BANK.serializeInteger(element, getStartBankNumber());
		Attributes.REQUIRED_BANKS_COUNT.serializeInteger(element,
				getRequiredBanksCount());
	}

	final void deserialize(org.xml.sax.Attributes attributes)
			throws SAXException {
		if (attributes == null) {
			throw new IllegalArgumentException(
					"Parameter 'attributes' must not be null.");
		}

		String value;

		WorkbookEntryType type = Attributes.TYPE.deserializeValueSet(
				attributes, WorkbookEntryType.class);
		if (type != null) {
			setType(type);
		}

		value = Attributes.TITLE.deserializeString(attributes);
		if (value != null) {
			setTitle(value);
		}
		value = Attributes.GENRE_NAME.deserializeString(attributes);
		if (value != null) {
			setGenreName(value);
		}
		setFavoriteIndicator(Attributes.FAVORITE_INDICATOR.deserializeBoolean(
				attributes, favoriteIndicator));

		value = Attributes.FILE_NAME.deserializeString(attributes);
		if (value != null) {
			setFileName(value);
		}

		setFileSize(Attributes.FILE_SIZE.deserializeLong(attributes));

		FileHeaderType fileHeaderType = Attributes.FILE_HEADER_TYPE
				.deserializeValueSet(attributes, FileHeaderType.class);
		if (fileHeaderType != null) {
			setFileHeaderType(fileHeaderType);
		}

		setContentCRC32((int) Attributes.CONTENT_CRC32
				.deserializeLongAsHex(attributes));

		ContentType contentType = Attributes.CONTENT_TYPE.deserializeValueSet(
				attributes, ContentType.class);
		if (contentType != null) {
			setContentType(contentType);
		}

		value = attributes.getValue(Attributes.DISPLAY_MODE.getName());
		if (value != null) {
			// Backward compatibility
			if (value.equals("SINGLE_ENTRY_WITH_INDEX")) {
				value = DisplayMode.SINGLE_ENTRY.getId();
			} else if (value.equals("MANY_ENTRIES")) {
				value = DisplayMode.MULTIPLE_ENTRIES.getId();
			}
			DisplayMode displayMode = DisplayMode.getInstance(value);
			if (displayMode != null) {
				setDisplayMode(displayMode);
			}
		}
		value = Attributes.PARAMETERS.deserializeString(attributes);
		if (value != null) {
			setParameters(value);
		}

		setStartBankFixedIndicator(Attributes.START_BANK_FIXED_INDICATOR
				.deserializeBoolean(attributes, startBankFixedIndicator));
		setStartBankNumber(Attributes.START_BANK.deserializeInteger(attributes));
		setRequiredBanksCount(Attributes.REQUIRED_BANKS_COUNT
				.deserializeInteger(attributes));
	}

	@Override
	public String toString() {
		return "type=" + type + " title=" + title + " filePath=" + filePath
				+ " contentType=" + contentType + " fileSize=" + fileSize
				+ " displayMode=" + displayMode + " startBankFixedIndicator="
				+ startBankFixedIndicator + " startBankNumber="
				+ startBankNumber + " requiredBanksCount=" + requiredBanksCount
				+ " banksAssigned=" + banksAssigned;
	}

	public WorkbookEntry createCopy() {
		WorkbookEntry result = new WorkbookEntry();
		result.type = type;
		result.title = title;
		result.genreName = genreName;
		result.filePath = filePath;
		result.contentType = contentType;
		result.fileSize = fileSize;
		result.displayMode = displayMode;
		result.startBankNumber = startBankNumber;
		result.requiredBanksCount = requiredBanksCount;
		return result;
	}

	public boolean equals(WorkbookEntry other) {
		if (other == null) {
			throw new IllegalArgumentException(
					"Parameter 'other' must not be null.");
		}
		return other.type.equals(type) && other.title.equals(title)
				&& other.genreName.equals(genreName)
				&& other.filePath.equals(filePath)
				&& other.fileSize == fileSize
				&& other.contentType.equals(contentType)
				&& other.displayMode.equals(displayMode)
				&& other.startBankNumber == startBankNumber
				&& other.requiredBanksCount == requiredBanksCount;
	}

	@Override
	public int compareTo(WorkbookEntry o) {
		return getTitle().compareToIgnoreCase(o.getTitle());
	}
}
