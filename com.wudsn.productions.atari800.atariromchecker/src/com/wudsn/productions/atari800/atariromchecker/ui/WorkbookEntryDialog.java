/**
 * Copyright (C) 2014 <a href="https://www.wudsn.com" target="_top">Peter Dell</a>
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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import com.wudsn.productions.atari800.atariromchecker.Texts;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.gui.AttributeTable;
import com.wudsn.tools.base.gui.AttributeTablePanel;
import com.wudsn.tools.base.gui.ModalDialog;
import com.wudsn.tools.base.gui.SpringUtilities;

@SuppressWarnings("serial")
public final class WorkbookEntryDialog extends ModalDialog {

    private TableModel tableModel;

    public WorkbookEntryDialog(JFrame parent, AttributeTablePanel tablePanel,
	    int selectedRow) {
	super(parent, Texts.WorkbookEntryDialog_Title);

	JPanel rowsPanel = new JPanel(new SpringLayout());

	AttributeTable table = tablePanel.getTable();
	this.tableModel = table.getModel();
	int rows = tableModel.getColumnCount();

	for (int i = 0; i < rows; i++) {
	    String value = tableModel.getValueAt(selectedRow, i).toString();

	    String columnName = tableModel.getColumnName(i);
	    JTextComponent textComponent;
	    int textLength = value.length();
	    int textColums = 72;
	    if (textLength < textColums) {
		textComponent = new JTextField();

	    } else {
		int textRows = (textLength + textColums) / textColums;
		JTextArea textArea = new JTextArea(textRows, textColums);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(rowsPanel.getFont());
		textComponent =textArea;
	    }
	    JLabel label = new JLabel(columnName);
	    rowsPanel.add(label);
	    rowsPanel.add(textComponent);
	    textComponent.setEditable(false);
	    textComponent.setText(value);
	}

	SpringUtilities.makeCompactGrid(rowsPanel, rows, 2, // rows, cols
		6, 6, // initX, initY
		6, 6); // xPad, yPad

	fieldsPane.setLayout(new BorderLayout());
	fieldsPane.add(rowsPanel, BorderLayout.CENTER);
    }

    public void showModal(MessageQueue messageQueue) {

	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}

	showModal(fieldsPane);
    }

    @Override
    protected void dataToUi() {

    }
}
