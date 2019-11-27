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

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;

import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.AttributeTable;
import com.wudsn.tools.base.gui.AttributeTableModel;
import com.wudsn.tools.base.gui.SimpleDialog;
import com.wudsn.tools.thecartstudio.Texts;
import com.wudsn.tools.thecartstudio.model.ContentType;
import com.wudsn.tools.thecartstudio.model.ContentType.Attributes;
import com.wudsn.tools.thecartstudio.model.Preferences;
import com.wudsn.tools.thecartstudio.model.TheCartMode;

/**
 * Content types overview dialog. <br/>
 * 
 * @author Peter Dell
 */
public final class ContentTypesDialog extends SimpleDialog {

    @SuppressWarnings("serial")
    private final static class TableModel extends AttributeTableModel {

	private final static class Columns {
	    public final static int TEXT = 0;
	    public final static int ID = 1;
	    public final static int SIZE_IN_KB = 2;
	    public final static int CARTRDIGE_TYPE_NUMERIC_ID = 3;
	    public final static int THE_CART_MODE = 4;
	    public final static int BANK_SIZE = 5;
	    public final static int START_BANK_NUMBER = 6;
	}

	private List<ContentType> contentTypes;

	public TableModel() {
	    addColumn(Attributes.TEXT, Column.FIXED | Column.SORTABLE);
	    addColumn(Attributes.ID, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.SIZE, Column.VISIBLE | Column.SORTABLE);
	    addColumn(Attributes.CARTRIDGE_TYPE_NUMERIC_ID, Column.VISIBLE
		    | Column.SORTABLE);
	    addColumn(Attributes.THE_CART_MODE, Column.VISIBLE
		    | Column.SORTABLE, null, new DefaultTableCellRenderer() {
		@Override
		protected void setValue(Object value) {
		    int theCartMode = ((Integer) value).intValue();
		    if (theCartMode == TheCartMode.TC_MODE_NOT_SUPPORTED) {
			setText("");
		    } else {
			setText(TextUtility.formatAsDecimal(theCartMode));
		    }
		}
	    }, null);
	    addColumn(Attributes.BANK_SIZE, Column.HIDDEN | Column.SORTABLE);
	    addColumn(Attributes.INITIAL_BANK_NUMBER, Column.HIDDEN
		    | Column.SORTABLE);

	    contentTypes = new ArrayList<ContentType>(ContentType.getValues());
	    contentTypes.remove(ContentType.UNKNOWN);
	    contentTypes.remove(ContentType.FILE_BINARY);
	    contentTypes.remove(ContentType.FILE_EXECUTABLE);
	}

	@Override
	public int getRowCount() {
	    return contentTypes.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
	    ContentType contentType = contentTypes.get(row);
	    switch (column) {
	    case Columns.ID:
		return contentType.getId();
	    case Columns.TEXT:
		return contentType.getText();
	    case Columns.SIZE_IN_KB:
		return Integer.valueOf(contentType.getCartridgeType()
			.getSizeInKB() * KB);
	    case Columns.CARTRDIGE_TYPE_NUMERIC_ID:
		return Integer.valueOf(contentType.getCartridgeType()
			.getNumericId());
	    case Columns.THE_CART_MODE:
		return Integer.valueOf(contentType.getTheCartMode());
	    case Columns.BANK_SIZE:
		return Integer.valueOf(contentType.getCartridgeType()
			.getBankSize());
	    case Columns.START_BANK_NUMBER:
		return Integer.valueOf(contentType.getCartridgeType()
			.getInitialBankNumber());
	    default:
		throw new IllegalArgumentException("Invalid column " + column
			+ ".");
	    }

	}
    }

    private Preferences preferences;
    TableModel tableModel;
    private AttributeTable table;

    public ContentTypesDialog(JFrame parent, Preferences preferences) {
	super(parent, Texts.ContentTypesDialog_Title, false);
	if (preferences == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'parent, prefernces' must not be null.");
	}
	this.preferences = preferences;
    }

    @Override
    protected void initComponents(JDialog dialog) {
	Container pane = dialog.getContentPane();

	tableModel = new TableModel();
	table = new AttributeTable(tableModel, preferences, "contentTypesTable");
	table.setAutoCreateRowSorter(true);
	pane.add(new JScrollPane(table), BorderLayout.CENTER);

	// Sort by description text by default.
	table.getRowSorter().toggleSortOrder(TableModel.Columns.TEXT);

	// initColumnSizes(table);
	initButtonBar();
	dialog.setSize(640, 480);
    }

    /**
     * Gets the content types table.
     * 
     * @return The content types table, not <code>null</code>.
     */
    public AttributeTable getTable() {
	return table;
    }
}