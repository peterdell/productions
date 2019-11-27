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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.common.MessageQueue;
import com.wudsn.tools.base.common.StringUtility;
import com.wudsn.tools.base.gui.IntegerField;
import com.wudsn.tools.base.gui.MemorySizeField;
import com.wudsn.tools.base.gui.ModalDialog;
import com.wudsn.tools.base.gui.SpringUtilities;
import com.wudsn.tools.base.gui.StandardDialog;
import com.wudsn.tools.base.gui.ValueSetField;
import com.wudsn.tools.thecartstudio.DataTypes;
import com.wudsn.tools.thecartstudio.Texts;
import com.wudsn.tools.thecartstudio.model.CartridgeMenuType;
import com.wudsn.tools.thecartstudio.model.FlashTargetType;
import com.wudsn.tools.thecartstudio.model.Preferences;
import com.wudsn.tools.thecartstudio.model.Workbook;
import com.wudsn.tools.thecartstudio.model.WorkbookGenre;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot;
import com.wudsn.tools.thecartstudio.model.WorkbookRootValidation;

@SuppressWarnings("serial")
public final class WorkbookOptionsDialog extends ModalDialog {

    private static final int ROWS = 8;

    private final JTextField titleTextField;
    private final ValueSetField<FlashTargetType> flashTargetTypeField;
    private final ValueSetField<CartridgeType> cartridgeTypeField;
    private final IntegerField bankCountField;
    private final MemorySizeField bankSizeField;
    private final ValueSetField<CartridgeMenuType> cartridgeMenuTypeField;
    private final MemorySizeField userSpaceSizeField;
    private final JTextField genreNamesField;

    transient WorkbookRoot root;

    public WorkbookOptionsDialog(JFrame parent, Preferences preferences) {
	super(parent, Texts.WorkbookOptionsDialog_Title);

	titleTextField = SpringUtilities.createTextField(fieldsPane,
		DataTypes.WorkbookRoot_Title);
	flashTargetTypeField = SpringUtilities.createValueSetField(fieldsPane,
		DataTypes.WorkbookRoot_FlashTargetType, FlashTargetType.class);
	flashTargetTypeField.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		performFlashTargetTypeChanged();

	    }
	});
	cartridgeTypeField = SpringUtilities.createValueSetField(fieldsPane,
		DataTypes.WorkbookRoot_CartridgeType, CartridgeType.class);
	bankCountField = SpringUtilities.createIntegerField(fieldsPane,
		DataTypes.WorkbookRoot_BankCount);
	bankSizeField = SpringUtilities.createMemorySizeField(fieldsPane,
		DataTypes.WorkbookRoot_BankSize);

	cartridgeMenuTypeField = SpringUtilities.createValueSetField(
		fieldsPane, DataTypes.WorkbookRoot_CartridgeMenuType,
		CartridgeMenuType.class);
	userSpaceSizeField = SpringUtilities.createMemorySizeField(fieldsPane,
		DataTypes.WorkbookRoot_UserSpaceSize);
	genreNamesField = SpringUtilities.createMemorySizeField(fieldsPane,
		DataTypes.WorkbookRoot_GenreNames);
    }

    public boolean showModal(Workbook workbook, MessageQueue messageQueue) {
	if (workbook == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'workbook' must not be null.");
	}
	if (messageQueue == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'messageQueue' must not be null.");
	}
	this.root = workbook.getRoot().createCopy();
	dataToUi();
	okPressed = false;
	// Lay out the panel.
	int stringWidth = getParent().getGraphics().getFontMetrics()
		.stringWidth("a")
		* WorkbookRoot.Attributes.TITLE.getDataType()
			.getMaximumLength();
	Dimension size = titleTextField.getPreferredSize();
	titleTextField
		.setPreferredSize(new Dimension(stringWidth, size.height));

	SpringUtilities.makeCompactGrid(fieldsPane, ROWS, 2, // rows, cols
		6, 6, // initX, initY
		6, 6); // xPad, yPad

	// genresModel.fireTableDataChanged();
	showModal(titleTextField);
	if (okPressed) {
	    workbook.getRoot().setTitle(root.getTitle());
	    boolean contentStructureEquals = workbook.getRoot().contentEquals(
		    root);
	    if (!contentStructureEquals) {
		workbook.getRoot().setFlashTargetType(
			root.getFlashTargetType(), root.getCartridgeType(),
			root.getBankCount(), root.getBankSize());
		workbook.getRoot().setCartridgeMenuType(
			root.getCartridgeMenuType());
		workbook.getRoot().setUserSpaceSize(root.getUserSpaceSize());
		workbook.unassignAllBanks(messageQueue);
		Workbook.initializeBanksList(workbook.getRoot(), messageQueue);
		workbook.assignNewBanks(messageQueue);
	    }
	    workbook.getRoot().getGenresList().clear();
	    workbook.getRoot().getGenresList().addAll(root.getGenresList());
	}
	return okPressed;
    }

    @Override
    protected void dataFromUi() {
	root.setTitle(titleTextField.getText());
	root.setFlashTargetType(flashTargetTypeField.getValue(),
		cartridgeTypeField.getValue(), bankCountField.getValue(),
		bankSizeField.getValue());
	root.setCartridgeMenuType(cartridgeMenuTypeField.getValue());
	root.setUserSpaceSize(userSpaceSizeField.getValue());

	String[] genreNames = genreNamesField.getText().split(",");
	for (int i = 0; i < genreNames.length; i++) {
	    genreNames[i] = genreNames[i].trim();
	}
	Arrays.sort(genreNames, StringUtility.CASE_INSENSITIVE_COMPARATOR);

	List<WorkbookGenre> genres = root.getGenresList();
	genres.clear();
	for (String genreName : genreNames) {
	    WorkbookGenre genre = new WorkbookGenre();
	    genreName = genreName.trim();
	    // Skip empty entries
	    if (StringUtility.isSpecified(genreName)) {
		genre.setName(genreName);
		genres.add(genre);
	    }
	}

    }

    @Override
    protected void dataToUi() {
	titleTextField.setText(root.getTitle());
	flashTargetTypeField.setValue(root.getFlashTargetType());
	boolean editable = root.getFlashTargetType().equals(
		FlashTargetType.USER_DEFINED);
	cartridgeTypeField.setEditable(editable);
	cartridgeTypeField.setEnabled(editable);

	cartridgeTypeField.setValue(root.getCartridgeType());
	bankCountField.setEditable(editable);
	bankSizeField.setEditable(editable);
	bankCountField.setValue(root.getBankCount());
	bankSizeField.setValue(root.getBankSize());
	editable = (root.getFlashTargetType().getSupportedCartridgeMenuTypes()
		.size() > 1);
	cartridgeMenuTypeField.setEditable(editable);
	cartridgeMenuTypeField.setEnabled(editable);
	CartridgeMenuType cartridgeMenuType = root.getCartridgeMenuType();
	if (!root.getFlashTargetType().isCartridgeMenuTypeSupported(
		cartridgeMenuType)) {
	    cartridgeMenuType = CartridgeMenuType.NONE;
	}
	cartridgeMenuTypeField.setValue(cartridgeMenuType);
	userSpaceSizeField.setValue(root.getUserSpaceSize());

	StringBuilder builder = new StringBuilder();

	for (WorkbookGenre genre : root.getUnmodifiableGenresList()) {
	    if (builder.length() > 0) {
		builder.append(", ");
	    }
	    builder.append(genre.getName());
	}
	genreNamesField.setText(builder.toString());
    }

    void performFlashTargetTypeChanged() {
	dataFromUi();
	FlashTargetType flashTargetType = root.getFlashTargetType();
	if (!flashTargetType.isCartridgeMenuTypeSupported(root
		.getCartridgeMenuType())) {
	    root.setCartridgeMenuType(flashTargetType
		    .getSupportedCartridgeMenuTypes().get(0));
	}
	dataToUi();
    }

    @Override
    protected boolean validateOK() {

	WorkbookRootValidation validation = WorkbookRootValidation
		.createInstance();
	MessageQueue messageQueue = new MessageQueue();
	validation.validateSave(root, messageQueue, false);
	if (!messageQueue.containsError()) {
	    Workbook.initializeBanksList(root, messageQueue);
	}
	if (messageQueue.containsError()) {
	    StandardDialog.showErrorMessage(this, messageQueue.getFirstError()
		    .getMessageText(), getTitle());
	    return false;
	}
	return true;
    }

}