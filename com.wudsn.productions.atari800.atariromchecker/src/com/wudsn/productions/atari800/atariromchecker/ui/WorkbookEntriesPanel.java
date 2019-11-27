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

package com.wudsn.productions.atari800.atariromchecker.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.wudsn.productions.atari800.atariromchecker.Preferences;
import com.wudsn.productions.atari800.atariromchecker.model.ROM;
import com.wudsn.productions.atari800.atariromchecker.model.ROMVersion;
import com.wudsn.productions.atari800.atariromchecker.model.Workbook;
import com.wudsn.productions.atari800.atariromchecker.model.WorkbookEntry;
import com.wudsn.productions.atari800.atariromchecker.model.WorkbookEntry.Attributes;
import com.wudsn.tools.base.gui.AttributeTableModel;
import com.wudsn.tools.base.gui.AttributeTablePanel;
import com.wudsn.tools.base.repository.Attribute;

@SuppressWarnings("serial")
public final class WorkbookEntriesPanel extends AttributeTablePanel {

    public final static class TableModel extends AttributeTableModel {

	public final class Columns {
	    public final static int ENTRY_ID = 0;
	    public final static int FOLDER_PATH = 1;
	    public final static int FILE_NAME = 2;
	    public final static int FILE_SIZE = 3;

	    public final static int SIZE = 4;
	    public final static int MD5 = 5;
	    public final static int CRC32 = 6;

	    public final static int TYPE = 7;
	    public final static int ID = 8;
	    public final static int PUBLISHER = 9;
	    public final static int DATE = 10;
	    public final static int REVISION = 11;
	    public final static int NORM = 12;

	    public final static int PARTS = 13;
	    public final static int CHECK_SUM_VALUES = 14;

	    public final static int COMMENT = 15;
	    public final static int MESSAGE = 16;

	}

	private Workbook workbook;

	public TableModel(final Workbook workbook) {
	    if (workbook == null) {
		throw new IllegalArgumentException(
			"Parameter 'workbook' must not be null.");
	    }

	    this.workbook = workbook;

	    final Font monoSpacedFont = new Font(Font.MONOSPACED, Font.PLAIN,
		    12);
	    DefaultTableCellRenderer monoSpacedFontCellRenderer = new DefaultTableCellRenderer() {
		@Override
		protected void setValue(Object value) {
		    setFont(monoSpacedFont);
		    super.setValue(value);
		}
	    };

	    DefaultTableCellRenderer md5CellRenderer = new DefaultTableCellRenderer() {

		@Override
		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		    Component result = super.getTableCellRendererComponent(
			    table, value, isSelected, hasFocus, row, column);
		    int modelRowIndex = table.convertRowIndexToModel(row);
		    setForeground(table.getForeground()); // Ignore selection.
		    WorkbookEntry entry = workbook.getEntry(modelRowIndex);
		    if (!entry.isMD5forCRC32()) {
			setBackground(Color.red);
		    } else {
			setBackground(table.getBackground());
		    }
		    setFont(monoSpacedFont);

		    return result;
		}
	    };
	    addColumn(Attributes.ID, Column.VISIBLE);
	    addColumn(Attributes.FOLDER_PATH, Column.VISIBLE | Column.SORTABLE);
	    addColumn(Attributes.FILE_NAME, Column.VISIBLE | Column.SORTABLE);
	    addColumn(Attributes.FILE_SIZE, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.SIZE, Column.VISIBLE | Column.SORTABLE);
	    addColumn(Attributes.MD5, Column.VISIBLE | Column.SORTABLE, null,
		    md5CellRenderer, null);
	    addColumn(Attributes.CRC32, Column.VISIBLE | Column.SORTABLE, null,
		    monoSpacedFontCellRenderer, null);

	    addColumn(ROMVersion.Attributes.TYPE, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(ROMVersion.Attributes.ID, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(ROMVersion.Attributes.PUBLISHER, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(ROMVersion.Attributes.DATE, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(ROMVersion.Attributes.REVISION, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(ROMVersion.Attributes.NORM, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(ROMVersion.Attributes.PARTS, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(Attributes.CHECK_SUMS, Column.VISIBLE | Column.SORTABLE,
		    null, monoSpacedFontCellRenderer, null);

	    addColumn(ROMVersion.Attributes.COMMENT, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(Attributes.MESSAGE, Column.VISIBLE | Column.SORTABLE);

	}

	@Override
	public Object getValueAt(int row, int column) {
	    WorkbookEntry entry = workbook.getEntry(row);
	    ROM rom = entry.getROM();
	    ROMVersion romVersion = entry.getROMVersion();
	    if (romVersion == null) {
		romVersion = ROMVersion.UNKNOWN;
	    }
	    switch (column) {
	    case Columns.ENTRY_ID:
		return Integer.valueOf(table.convertRowIndexToView(row) + 1);
	    case Columns.FOLDER_PATH:
		return entry.getFolderPath();
	    case Columns.FILE_NAME:
		return entry.getFileName();
	    case Columns.FILE_SIZE:
		return entry.getFileSize();
	    case Columns.SIZE:
		return rom.getSize();
	    case Columns.MD5:
		return entry.getMD5();
	    case Columns.CRC32:
		return entry.getCRC32();

	    case Columns.TYPE:
		return romVersion.getType();
	    case Columns.ID:
		return romVersion.getId();
	    case Columns.PUBLISHER:
		return romVersion.getPublisher();
	    case Columns.DATE:
		return romVersion.getDate();
	    case Columns.REVISION:
		return romVersion.getRevision();
	    case Columns.NORM:
		return romVersion.getNorm();
	    case Columns.PARTS:
		return romVersion.getParts();
	    case Columns.CHECK_SUM_VALUES:
		return rom.getCheckSumValues();
	    case Columns.COMMENT:
		return romVersion.getComment();
	    case Columns.MESSAGE:
		return entry.getMessage();

	    default:
		throw new IllegalArgumentException("Invalid column " + column
			+ ".");
	    }

	}

	@Override
	public boolean isCellEditable(int row, int column) {
	    return false;
	}

	@Override
	public int getRowCount() {
	    return workbook.getEntryCount();
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
	int modelRowIndex = workbook.getUnmodifiableEntriesList().indexOf(
		workbookEntry);

	table.selectCell(modelRowIndex, attribute);
    }
}
