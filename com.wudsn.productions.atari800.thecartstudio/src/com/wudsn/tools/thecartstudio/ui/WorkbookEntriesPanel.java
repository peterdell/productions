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

package com.wudsn.tools.thecartstudio.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.wudsn.tools.base.common.JDK.DefaultComboBoxModel;
import com.wudsn.tools.base.common.JDK.JComboBox;
import com.wudsn.tools.base.common.Log;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.AttributeTableModel;
import com.wudsn.tools.base.gui.AttributeTablePanel;
import com.wudsn.tools.base.gui.ValueSetField;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.thecartstudio.Texts;
import com.wudsn.tools.thecartstudio.model.ContentType;
import com.wudsn.tools.thecartstudio.model.DisplayMode;
import com.wudsn.tools.thecartstudio.model.FileHeaderType;
import com.wudsn.tools.thecartstudio.model.Preferences;
import com.wudsn.tools.thecartstudio.model.Workbook;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry.Attributes;
import com.wudsn.tools.thecartstudio.model.WorkbookEntryType;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot;

@SuppressWarnings("serial")
public final class WorkbookEntriesPanel extends AttributeTablePanel {

    public final static class TableModel extends AttributeTableModel {

	public final class Columns {
	    public final static int ID = 0;
	    public final static int TYPE = 1;
	    public final static int TITLE = 2;
	    public final static int GENRE_NAME = 3;
	    public final static int FAVORITE = 4;
	    public final static int FILE_PATH = 5;
	    public final static int FILE_NAME = 6;
	    public final static int FILE_SIZE = 7;
	    public final static int FILE_HEADER_TYPE = 8;
	    public final static int CONTENT_SIZE = 9;
	    public final static int CONTENT_CRC32 = 10;
	    public final static int CONTENT_TYPE = 11;
	    public final static int DISPLAY_MODE = 12;
	    public final static int PARAMETERS = 13;
	    public final static int START_BANK_FIXED = 14;
	    public final static int START_BANK = 15;
	    public final static int BANKS_REQUIRED = 16;
	    public final static int BANKS_ASSIGNED = 17;
	}

	private Workbook workbook;
	Font monoSpacedFont;
	ContentTypeComboxBoxModelFactory contentTypeComboxBoxModelFactory;
	private List<String> genreNamesList;
	private DefaultComboBoxModel<String> genreComboBoxModel;

	public TableModel(final Workbook workbook) {
	    if (workbook == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbook' must not be null.");
	    }

	    this.workbook = workbook;

	    HashMap<TextAttribute, String> attributes = new HashMap<TextAttribute, String>();
	    attributes.put(TextAttribute.FAMILY, Font.MONOSPACED);
	    monoSpacedFont = Font.getFont(attributes);

	    // Create the JTable using the model
	    addColumn(Attributes.ID, Column.VISIBLE);
	    addColumn(Attributes.TYPE, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.TITLE, Column.FIXED | Column.SORTABLE,
		    StringUtility.CASE_INSENSITIVE_COMPARATOR, null, null);

	    genreNamesList = Collections.emptyList();
	    genreComboBoxModel = new DefaultComboBoxModel<String>();
	    JComboBox<String> genreComboBox = new JComboBox<String>(
		    genreComboBoxModel);
	    genreComboBox.setEditable(true);
	    addColumn(Attributes.GENRE_NAME, Column.VISIBLE | Column.SORTABLE,
		    StringUtility.CASE_INSENSITIVE_COMPARATOR, null,
		    new DefaultCellEditor(genreComboBox));
	    addColumn(Attributes.FAVORITE_INDICATOR, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(Attributes.FILE_PATH, Column.HIDDEN | Column.SORTABLE,
		    StringUtility.CASE_INSENSITIVE_COMPARATOR, null, null);
	    addColumn(Attributes.FILE_NAME, Column.VISIBLE | Column.SORTABLE,
		    StringUtility.CASE_INSENSITIVE_COMPARATOR, null, null);
	    addColumn(Attributes.FILE_SIZE, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.FILE_HEADER_TYPE, Column.HIDDEN
		    | Column.SORTABLE);
	    addColumn(Attributes.CONTENT_SIZE, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.CONTENT_CRC32,
		    Column.HIDDEN | Column.SORTABLE, null,
		    new DefaultTableCellRenderer() {
			@Override
			protected void setValue(Object value) {
			    int crc32 = ((Integer) value).intValue();
			    if (crc32 == 0) {
				setText("");
			    } else {
				setText(Long.toHexString(crc32 & 0xffffffffl)
					.toUpperCase());
			    }
			    setFont(monoSpacedFont);
			}
		    }, null);
	    addColumn(Attributes.CONTENT_TYPE,
		    Column.VISIBLE | Column.SORTABLE, null, null,
		    new DefaultCellEditor(new ValueSetField<ContentType>(
			    ContentType.class)) {
			@Override
			public Component getTableCellEditorComponent(
				JTable table, Object value, boolean isSelected,
				int viewRowIndex, int viewColumnIndex) {
			    Component component = super
				    .getTableCellEditorComponent(table, value,
					    isSelected, viewRowIndex,
					    viewColumnIndex);
			    @SuppressWarnings("unchecked")
			    JComboBox<ContentType> comboBox = (JComboBox<ContentType>) component;
			    // Row and column here are table index, not model
			    // indexes,
			    // so the must be mapped.
			    int modelRowIndex = table
				    .convertRowIndexToModel(viewRowIndex);
			    WorkbookRoot root = workbook.getRoot();
			    WorkbookEntry entry = root.getEntry(modelRowIndex);
			    boolean filterByFileContentSize = entry.getType() == WorkbookEntryType.FILE_ENTRY;
			    int fileContentSize = entry.getRequiredBanksCount()
				    * root.getBankSize();
			    ContentType currentValue = entry.getContentType();
			    comboBox.setModel(ContentTypeComboxBoxModelFactory
				    .getModel(filterByFileContentSize,
					    fileContentSize,
					    root.getFlashTargetType(),
					    currentValue));
			    comboBox.setSelectedItem(currentValue);
			    return component;
			}
		    });
	    addColumn(Attributes.DISPLAY_MODE,
		    Column.VISIBLE | Column.SORTABLE, null, null,
		    new DefaultCellEditor(new ValueSetField<DisplayMode>(
			    DisplayMode.class)));
	    addColumn(Attributes.PARAMETERS, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.START_BANK_FIXED_INDICATOR, Column.HIDDEN
		    | Column.SORTABLE);
	    addColumn(Attributes.START_BANK, Column.HIDDEN | Column.SORTABLE,
		    null, new DefaultTableCellRenderer() {
			@Override
			protected void setValue(Object value) {
			    int startBank = ((Integer) value).intValue();
			    if (startBank == WorkbookEntry.START_BANK_UNDEFINED) {
				setText(Texts.WorkbookEntry_StartBankUndefined);
				setHorizontalAlignment(SwingConstants.RIGHT);
			    } else {
				setText(TextUtility.formatAsDecimal(startBank));
				setHorizontalAlignment(SwingConstants.RIGHT);
			    }
			}
		    }, null);
	    addColumn(Attributes.REQUIRED_BANKS_COUNT, Column.HIDDEN
		    | Column.SORTABLE);
	    addColumn(Attributes.BANKS_ASSIGNED_INDICATOR, Column.VISIBLE
		    | Column.SORTABLE);

	}

	public void updateFromWorkbook() {
	    List<String> genreNamesList = workbook.getRoot()
		    .getSortedGenreNamesList();
	    if (!genreNamesList.equals(this.genreNamesList)) {
		genreComboBoxModel.removeAllElements();
		for (String genreName : genreNamesList) {
		    genreComboBoxModel.addElement(genreName);
		}
		this.genreNamesList = genreNamesList;
	    }
	}

	@Override
	public int getRowCount() {
	    return workbook.getRoot().getEntryCount();
	}

	@Override
	public Object getValueAt(int row, int column) {
	    WorkbookEntry entry = workbook.getRoot().getEntry(row);
	    switch (column) {
	    case Columns.ID:
		return Integer.valueOf(table.convertRowIndexToView(row) + 1);
	    case Columns.TYPE:
		return entry.getType();
	    case Columns.TITLE:
		return entry.getTitle();
	    case Columns.GENRE_NAME:
		return entry.getGenreName();
	    case Columns.FAVORITE:
		return Boolean.valueOf(entry.getFavoriteIndicator());
	    case Columns.FILE_PATH:
		return entry.getFilePath();
	    case Columns.FILE_NAME:
		if (entry.getType() == WorkbookEntryType.FILE_ENTRY) {
		    return entry.getFileName();
		}
		return Texts.WorkbookEntry_FileNameUndefined;
	    case Columns.FILE_SIZE:
		return Long.valueOf(entry.getFileSize());
	    case Columns.FILE_HEADER_TYPE:
		return entry.getFileHeaderType();
	    case Columns.CONTENT_SIZE:
		return Long.valueOf(entry.getContentSize());
	    case Columns.CONTENT_CRC32:
		return Integer.valueOf(entry.getContentCRC32());
	    case Columns.CONTENT_TYPE:
		return entry.getContentType();
	    case Columns.DISPLAY_MODE:
		return entry.getDisplayMode();
	    case Columns.PARAMETERS:
		return entry.getParameters();
	    case Columns.START_BANK_FIXED:
		return Boolean.valueOf(entry.isStartBankFixed());
	    case Columns.START_BANK:
		return Integer.valueOf(entry.getStartBankNumber());
	    case Columns.BANKS_REQUIRED:
		return Integer.valueOf(entry.getRequiredBanksCount());
	    case Columns.BANKS_ASSIGNED:
		return Boolean.valueOf(entry.areBanksAssigned());
	    default:
		throw new IllegalArgumentException("Invalid column " + column
			+ ".");
	    }

	}

	@Override
	public boolean isCellEditable(int row, int column) {
	    WorkbookEntry entry = workbook.getRoot().getEntry(row);

	    switch (column) {
	    case Columns.TYPE:
		return false;
	    case Columns.TITLE:
		return true;
	    case Columns.GENRE_NAME:
		return true;
	    case Columns.FAVORITE:
		return true;

	    case Columns.CONTENT_TYPE:
		// Content type of file entries is only changable, if it not a
		// .CAR file.
		// Content type of user user space entries is always
		// changeable.
		return (entry.getType().equals(WorkbookEntryType.FILE_ENTRY) && entry
			.getFileHeaderType() != FileHeaderType.CART)
			|| (entry.getType()
				.equals(WorkbookEntryType.USER_SPACE_ENTRY));

	    case Columns.DISPLAY_MODE:
		return true;

	    case Columns.PARAMETERS:
		return true;
	    case Columns.START_BANK_FIXED:
		// Start bank for file entries is fixed if the user requests
		// this.
		// Start bank for user space entries is always fixed.
		return entry.getType().equals(WorkbookEntryType.FILE_ENTRY);

	    case Columns.START_BANK:
		return entry.isStartBankFixed();
	    }

	    return false;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
	    try {
		WorkbookEntry entry = workbook.getRoot().getEntry(row);
		switch (column) {
		case Columns.TITLE:
		    entry.setTitle(value.toString());
		    break;
		case Columns.GENRE_NAME:
		    entry.setGenreName(value.toString());
		    break;
		case Columns.FAVORITE:
		    entry.setFavoriteIndicator(((Boolean) value).booleanValue());
		    break;
		case Columns.CONTENT_TYPE:
		    entry.setContentType((ContentType) value);
		    break;
		case Columns.DISPLAY_MODE:
		    entry.setDisplayMode((DisplayMode) value);
		    break;
		case Columns.PARAMETERS:
		    entry.setParameters(value.toString());
		    break;
		case Columns.START_BANK_FIXED:
		    entry.setStartBankFixedIndicator(((Boolean) value)
			    .booleanValue());
		    break;
		case Columns.START_BANK:
		    if (entry.areBanksAssigned()) {
			workbook.unassignBanks(entry);
		    }
		    entry.setStartBankNumber(((Integer) value).intValue());
		    workbook.assignBanks(entry, entry.getStartBankNumber());

		    fireTableCellUpdated(row, Columns.BANKS_ASSIGNED);

		    break;
		default:
		    throw new IllegalArgumentException(
			    "Invalid or not editable column " + column + ".");
		}
		fireTableCellUpdated(row, column);
	    } catch (RuntimeException ignore) {
		Log.logError("Inconsistent workbook entries table", null,
			ignore);
	    }
	}
    }

    private Workbook workbook;

    public WorkbookEntriesPanel(Preferences preferences, Workbook workbook) {
	super(new TableModel(workbook), preferences, "workbookEntriesTable");
	this.workbook = workbook;
	workbook.setEntriesTableModel((AttributeTableModel) getTable()
		.getModel());
    }

    /**
     * Sets the selected workbook entry, if it exists.
     * 
     * @param workbookEntry
     *            The workbook entry, not <code>null</code>.
     * 
     * @param attribute
     *            The attribute, not <code>null</code>.
     */
    public void setSelectedWorkbookEntry(WorkbookEntry workbookEntry,
	    Attribute attribute) {
	if (workbookEntry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbookEntry' must not be null.");
	}
	WorkbookRoot root = workbook.getRoot();
	int modelRowIndex = root.getUnmodifiableEntriesList().indexOf(
		workbookEntry);
	table.selectCell(modelRowIndex, attribute);
    }

    public void removeSelectedWorkbookEntries(MessageQueue messageQueue) {
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	int[] selectedViewRowIndexes = table.getSelectedRows();
	int[] selectedModelRowIndexes = new int[selectedViewRowIndexes.length];
	for (int i = 0; i < selectedViewRowIndexes.length; i++) {
	    selectedModelRowIndexes[i] = table
		    .convertRowIndexToModel(selectedViewRowIndexes[i]);
	}
	workbook.removeEntries(selectedModelRowIndexes, messageQueue);
	int minRow = 0;
	if (selectedViewRowIndexes.length > 0) {
	    minRow = selectedViewRowIndexes[0];
	}
	int size = workbook.getRoot().getEntryCount();
	if (minRow > size - 1) {
	    minRow--;
	}
	table.getSelectionModel().setSelectionInterval(minRow, minRow);
    }

    public void setSelectedWorkbookEntriesGenre(String genreName,
	    MessageQueue messageQueue) {
	if (genreName == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'genreName' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	int[] selectedViewRowIndexes = table.getSelectedRows();
	int[] selectedModelRowIndexes = new int[selectedViewRowIndexes.length];
	for (int i = 0; i < selectedViewRowIndexes.length; i++) {
	    selectedModelRowIndexes[i] = table
		    .convertRowIndexToModel(selectedViewRowIndexes[i]);
	}
	workbook.setEntriesGenreName(genreName, selectedModelRowIndexes,
		messageQueue);
    }

    /**
     * Update columns and value helps which depend of the workbook root.
     */
    @Override
    public void dataToUI() {
	((TableModel) getTable().getModel()).updateFromWorkbook();
    }
}
