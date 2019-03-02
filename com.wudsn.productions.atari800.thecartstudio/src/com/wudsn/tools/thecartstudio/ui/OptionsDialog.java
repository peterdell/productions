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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.wudsn.tools.base.gui.ColorField;
import com.wudsn.tools.base.gui.ElementFactory;
import com.wudsn.tools.base.gui.ModalDialog;
import com.wudsn.tools.base.gui.SpringUtilities;
import com.wudsn.tools.base.gui.ValueSetField;
import com.wudsn.tools.thecartstudio.Actions;
import com.wudsn.tools.thecartstudio.DataTypes;
import com.wudsn.tools.thecartstudio.Texts;
import com.wudsn.tools.thecartstudio.model.Language;
import com.wudsn.tools.thecartstudio.model.Preferences;

@SuppressWarnings("serial")
public final class OptionsDialog extends ModalDialog {

	private static final int ROWS = 9;

	private final ValueSetField<Language> languageField;
	private final JCheckBox updateCheckIndicatorField;
	private final ColorField freeBankColorField;
	private final JTextField emulatorExecuablePathField;
	private final ColorField reservedBankColorField;
	private final ColorField reservedUserSpaceBankColorField;
	private final ColorField usedOddBankColorField;
	private final ColorField usedEvenBankColorField;
	private final ColorField conflictBankColorField;

	Preferences preferences;

	public OptionsDialog(JFrame parent) {
		super(parent, Texts.OptionsDialog_Title);

		languageField = SpringUtilities.createValueSetField(fieldsPane,
				DataTypes.Preferences_Language, Language.class);
		updateCheckIndicatorField = SpringUtilities.createCheckBox(fieldsPane,
				DataTypes.Preferences_UpdateCheckIndicator);
		emulatorExecuablePathField = SpringUtilities.createTextField(
				fieldsPane, DataTypes.Preferences_EmulatorExecutablePath);
		freeBankColorField = SpringUtilities.createColorField(fieldsPane,
				DataTypes.Preferences_FreeBankColor);
		reservedBankColorField = SpringUtilities.createColorField(fieldsPane,
				DataTypes.Preferences_ReservedBankColor);
		reservedUserSpaceBankColorField = SpringUtilities.createColorField(
				fieldsPane, DataTypes.Preferences_ReservedUserSpaceBankColor);
		usedOddBankColorField = SpringUtilities.createColorField(fieldsPane,
				DataTypes.Preferences_UsedOddBankColor);
		usedEvenBankColorField = SpringUtilities.createColorField(fieldsPane,
				DataTypes.Preferences_UsedEvenBankColor);
		conflictBankColorField = SpringUtilities.createColorField(fieldsPane,
				DataTypes.Preferences_ConflictBankColor);

		JButton resetButton = ElementFactory.createButton(
				Actions.Preferences_ButtonBar_Reset, true);
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preferences.setDefaultBankColors();
				dataToUi();
			}
		});
		addButtonBarButton(resetButton);
	}

	public boolean showModal(Preferences preferences) {
		if (preferences == null) {
			throw new IllegalArgumentException(
					"Parameter 'preferences' must not be null.");
		}

		SpringUtilities.makeCompactGrid(fieldsPane, ROWS, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		this.preferences = new Preferences();
		preferences.copyTo(this.preferences);
		showModal(languageField);
		if (okPressed) {
			this.preferences.copyTo(preferences);
		}
		return okPressed;
	}

	@Override
	protected void dataFromUi() {
		preferences.setLanguage(languageField.getValue());
		preferences.setUpdateCheckIndicator(updateCheckIndicatorField
				.isSelected());
		preferences.setEmulatorExecutablePath(emulatorExecuablePathField
				.getText());
		preferences.setFreeBankColor(freeBankColorField.getValue());
		preferences.setReservedBankColor(reservedBankColorField.getValue());
		preferences
				.setReservedUserSpaceBankColor(reservedUserSpaceBankColorField
						.getValue());
		preferences.setUsedOddBankColor(usedOddBankColorField.getValue());
		preferences.setUsedEvenBankColor(usedEvenBankColorField.getValue());
		preferences.setConflictBankColor(conflictBankColorField.getValue());

	}

	@Override
	protected void dataToUi() {
		languageField.setValue(preferences.getLanguage());
		updateCheckIndicatorField.setSelected(preferences
				.getUpdateCheckIndicator());
		emulatorExecuablePathField.setText(preferences
				.getEmulatorExecutablePath());
		freeBankColorField.setValue(preferences.getFreeBankColor());
		reservedBankColorField.setValue(preferences.getReservedBankColor());
		reservedUserSpaceBankColorField.setValue(preferences
				.getReservedUserSpaceBankColor());
		usedOddBankColorField.setValue(preferences.getUsedOddBankColor());
		usedEvenBankColorField.setValue(preferences.getUsedEvenBankColor());
		conflictBankColorField.setValue(preferences.getConflictBankColor());
	}

}