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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.wudsn.productions.atari800.atariromchecker.Preferences;
import com.wudsn.productions.atari800.atariromchecker.model.Comparison;
import com.wudsn.productions.atari800.atariromchecker.model.ComparisonEntry;
import com.wudsn.productions.atari800.atariromchecker.model.WorkbookEntry;
import com.wudsn.productions.atari800.atariromchecker.model.ComparisonEntry.Attributes;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.AttributeTableModel;
import com.wudsn.tools.base.gui.AttributeTablePanel;
import com.wudsn.tools.base.repository.Attribute;
import com.wudsn.tools.base.repository.DataType;

@SuppressWarnings("serial")
public final class ComparisonEntryPanel extends AttributeTablePanel {

    public final static class TableModel extends AttributeTableModel {

	public final class Columns {
	    public final static int ID = 0;
	    public final static int OFFSET = 1;
	    public final static int ADDRESS = 2;
	    public final static int VALUE = 3;
	}

	private Comparison comparison;

	public TableModel(final Comparison comparison) {
	    if (comparison == null) {
		throw new IllegalArgumentException(
			"Parameter 'comparison' must not be null.");
	    }

	    this.comparison = comparison;

	    final Font monoSpacedFont = new Font(Font.MONOSPACED, Font.PLAIN,
		    12);
	    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {

		@Override
		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		    Component result = super.getTableCellRendererComponent(
			    table, value, isSelected, hasFocus, row, column);
		    int modelRowIndex = table.convertRowIndexToModel(row);
		    int modelColumnIndex = table
			    .convertColumnIndexToModel(column);
		    setForeground(table.getForeground()); // Ignore selection.
		    if (modelColumnIndex >= Columns.VALUE) {
			setBackground(comparison.getEntry(modelRowIndex)
				.getColor(modelColumnIndex - Columns.VALUE));
		    } else {
			setBackground(table.getBackground());
		    }
		    setFont(monoSpacedFont);

		    return result;
		}
	    };

	    addColumn(Attributes.ID, Column.VISIBLE);
	    addColumn(Attributes.OFFSET, Column.VISIBLE | Column.SORTABLE,
		    null, cellRenderer, null);
	    addColumn(Attributes.ADDRESS, Column.VISIBLE | Column.SORTABLE,
		    null, cellRenderer, null);
	    for (int i = 0; i < comparison.getWorkbookEntryCount(); i++) {
		DataType dataType = new DataType(String.class);
		WorkbookEntry workbookEntry = comparison.getWorkbookEntry(i);
		dataType.setTexts(workbookEntry.getFileName(),
			workbookEntry.getFolderPath());
		Attribute attribute = new Attribute("value" + i, dataType);
		addColumn(attribute, Column.VISIBLE | Column.SORTABLE, null,
			cellRenderer, null);
	    }
	}

	@Override
	public Object getValueAt(int row, int column) {
	    ComparisonEntry entry = comparison.getEntry(row);

	    switch (column) {
	    case Columns.ID:
		return Integer.valueOf(table.convertRowIndexToView(row) + 1);
	    case Columns.OFFSET:
		return entry.getOffset() >= 0 ? TextUtility
			.formatAsHexaDecimal(entry.getOffset(),
				comparison.getMaximumOffset()) : "";
	    case Columns.ADDRESS:
		return entry.getAddress() >= 0 ? TextUtility
			.formatAsHexaDecimal(entry.getAddress(),
				comparison.getMaximumAddress()) : "";
	    default:
		return entry.getValue(column - Columns.VALUE);
	    }

	}

	@Override
	public boolean isCellEditable(int row, int column) {
	    return false;
	}

	@Override
	public int getRowCount() {
	    return comparison.getEntryCount();
	}
    }

    private Comparison comparison;

    public ComparisonEntryPanel(Preferences preferences,
	    final Comparison comparison) {
	super(new TableModel(comparison), preferences, "comparisonEntriesTable");
	this.comparison = comparison;
	comparison.setEntriesTableModel((AttributeTableModel) getTable()
		.getModel());
    }

    /**
     * Sets the selected comparison entry, if it exists.
     * 
     * @param comparisonEntry
     *            The comparison entry, not <code>null</code>.
     * 
     * @param attribute
     *            The attribute, not <code>null</code>.
     */
    public void setSelectedComparisonEntry(ComparisonEntry comparisonEntry,
	    Attribute attribute) {
	if (comparisonEntry == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'comparisonEntry' must not be null.");
	}
	int modelRowIndex = comparison.getUnmodifiableEntriesList().indexOf(
		comparisonEntry);
	table.selectCell(modelRowIndex, attribute);
    }
}
