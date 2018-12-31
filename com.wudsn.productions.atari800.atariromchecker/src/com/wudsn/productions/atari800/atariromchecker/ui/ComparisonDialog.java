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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.wudsn.productions.atari800.atariromchecker.DataTypes;
import com.wudsn.productions.atari800.atariromchecker.Preferences;
import com.wudsn.productions.atari800.atariromchecker.Texts;
import com.wudsn.productions.atari800.atariromchecker.model.Comparison;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.gui.IntegerField;
import com.wudsn.tools.base.gui.ModalDialog;
import com.wudsn.tools.base.gui.SpringUtilities;

@SuppressWarnings("serial")
public final class ComparisonDialog extends ModalDialog {

    private IntegerField entriesCountField;
    private ComparisonEntryPanel entryPanel;

    private Comparison comparison;

    public ComparisonDialog(JFrame parent, Preferences preferences, Comparison comparison) {
	super(parent, Texts.ComparisonDialog_Title);

	if (comparison == null) {
	    throw new IllegalArgumentException("Parameter 'comparison' must not be null.");
	}
	this.comparison = comparison;

	JPanel topPanel = new JPanel(new SpringLayout());
	entriesCountField = SpringUtilities.createIntegerField(topPanel, DataTypes.Comparison_EntriesCount);
	entriesCountField.setEditable(false);
	SpringUtilities.makeCompactGrid(topPanel, 1, 2, // rows, cols
		6, 6, // initX, initY
		6, 6); // xPad, yPad

	entryPanel = new ComparisonEntryPanel(preferences, comparison);
	fieldsPane.setLayout(new BorderLayout());
	fieldsPane.add(topPanel, BorderLayout.NORTH);
	fieldsPane.add(entryPanel, BorderLayout.CENTER);
    }

    public void showModal(MessageQueue messageQueue) {

	if (messageQueue == null) {
	    throw new IllegalArgumentException("Parameter 'messageQueue' must not be null.");
	}

	showModal(entryPanel.getTable());
    }

    @Override
    protected void dataToUi() {
	entriesCountField.setVisible(comparison.getWorkbookEntryCount() > 1);
	entriesCountField.setValue(comparison.getEntryCount());
    }
}
