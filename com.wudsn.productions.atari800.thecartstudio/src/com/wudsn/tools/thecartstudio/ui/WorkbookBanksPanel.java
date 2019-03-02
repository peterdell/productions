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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;

import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.TheCartStudio;
import com.wudsn.tools.thecartstudio.model.Preferences;
import com.wudsn.tools.thecartstudio.model.ReservedContentProvider;
import com.wudsn.tools.thecartstudio.model.ReservedContentProviderFactory.UserSpaceContentProvider;
import com.wudsn.tools.thecartstudio.model.Workbook;
import com.wudsn.tools.thecartstudio.model.WorkbookBank;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot;

@SuppressWarnings("serial")
public class WorkbookBanksPanel extends JPanel {

	private final TheCartStudio studio;
	private final Preferences preferences;
	private final Workbook workbook;
	private List<WorkbookBank> banksList;

	private int startX;
	private int startY;
	private int banksPerRow;
	private int rows;
	private int barSize;

	public WorkbookBanksPanel(TheCartStudio studio, Preferences preferences,
			Workbook workbook) {
		if (studio == null) {
			throw new IllegalArgumentException(
					"Parameter 'studio' must not be null.");
		}
		if (preferences == null) {
			throw new IllegalArgumentException(
					"Parameter 'preferences' must not be null.");
		}
		if (workbook == null) {
			throw new IllegalArgumentException(
					"Parameter 'workbook' must not be null.");
		}
		this.studio = studio;
		this.preferences = preferences;
		this.workbook = workbook;
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				computeToolTipText(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		addMouseListener(new MouseAdapter() {

			private int defaultInitialDelay;
			private int defaultDismissDelay;

			@Override
			public void mouseEntered(MouseEvent me) {
				ToolTipManager manager = ToolTipManager.sharedInstance();
				defaultInitialDelay = manager.getInitialDelay();
				defaultDismissDelay = manager.getDismissDelay();
				manager.setInitialDelay(50);
				manager.setDismissDelay(60000);
			}

			@Override
			public void mouseExited(MouseEvent me) {
				ToolTipManager manager = ToolTipManager.sharedInstance();
				manager.setInitialDelay(defaultInitialDelay);
				manager.setDismissDelay(defaultDismissDelay);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				showWorkbookEntry(e);
			}
		});

	}

	@Override
	public void paintComponent(Graphics g) {
		final int minBarSize = 4;

		super.paintComponent(g);
		barSize = 0;

		if (!workbook.isValid()) {
			return;
		}

		WorkbookRoot root = workbook.getRoot();
		if (!root.isBanksListInitialized()) {
			return;
		}

		banksList = root.getBanksList();
		if (banksList.isEmpty()) {
			return;
		}

		Dimension d = getSize();
		int clientWidth = d.width;
		int clientHeight = d.height;
		if (clientWidth == 0 || clientHeight == 0) {
			return;
		}

		int size = banksList.size();
		double clientScale = 0.98;
		double area = clientWidth * clientHeight * (clientScale * clientScale);
		double side = Math.sqrt(area / size);

		banksPerRow = (int) (clientWidth * clientScale / side);
		rows = (size + banksPerRow - 1) / banksPerRow;
		barSize = (int) side;
		barSize = Math.max(barSize, minBarSize);

		int usedWidth = barSize * banksPerRow;
		int usedHeight = barSize * rows;
		startX = 0;
		startY = 0;
		if (usedWidth < clientWidth) {
			startX = (clientWidth - usedWidth) / 2;
		}
		if (usedHeight < clientHeight) {
			startY = (clientHeight - usedHeight) / 2;
		}

		WorkbookEntry lastEntry = null;
		boolean lastColorToggle = false;
		int bankNumber = 0;
		for (int positionY = 0; positionY < rows; positionY++) {
			for (int positionX = 0; positionX < banksPerRow
					&& bankNumber < banksList.size();) {

				WorkbookBank bank = banksList.get(bankNumber);

				Color borderColor;
				Color fillColor;
				borderColor = Color.BLACK;
				int usedBanksCount = 1;

				if (bank.isReserved()) {
					fillColor = preferences.getReservedBankColor();
					ReservedContentProvider reservedContentProvider = bank
							.getReservedContentProvider();
					if (reservedContentProvider instanceof UserSpaceContentProvider) {
						fillColor = preferences.getReservedUserSpaceBankColor();
					}
					usedBanksCount = reservedContentProvider
							.getStartBankNumber()
							+ reservedContentProvider.getRequiredBanksCount()
							- bankNumber;
					if (usedBanksCount > banksPerRow - positionX) {
						usedBanksCount = banksPerRow - positionX;
					}

					if (!bank.getEntries().isEmpty()) {
						fillColor = preferences.getConflictBankColor();
					}
				} else {
					switch (bank.getEntries().size()) {
					case 0:
						borderColor = Color.LIGHT_GRAY;
						fillColor = preferences.getFreeBankColor();
						break;
					case 1:
						WorkbookEntry entry = bank.getEntries().get(0);
						usedBanksCount = entry.getStartBankNumber()
								+ entry.getRequiredBanksCount() - bankNumber;
						if (usedBanksCount > banksPerRow - positionX) {
							usedBanksCount = banksPerRow - positionX;
						}

						if (entry != lastEntry) {
							lastColorToggle = !lastColorToggle;
							lastEntry = entry;
						}

						if (lastColorToggle) {
							fillColor = preferences.getUsedOddBankColor();

						} else {
							fillColor = preferences.getUsedEvenBankColor();
						}

						break;
					default:
						fillColor = preferences.getConflictBankColor();
						break;
					}
				}

				int cellX = (positionX * barSize) + startX;
				int cellY = (positionY * barSize) + startY;

				int fillWidth = barSize * usedBanksCount;
				if (barSize > minBarSize) {
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillRect(cellX + 1, cellY + 1, fillWidth - 3,
								barSize - 3);
					}
					for (int j = 1; j < usedBanksCount; j++) {
						g.setColor(BankColorUtility.getLineColor(fillColor));
						int x = cellX + 1 + j * barSize;
						g.drawLine(x, cellY + 1, x, cellY + 1 + barSize - 3);
					}
					g.setColor(borderColor);
					g.drawRect(cellX, cellY, fillWidth - 2, barSize - 2);

				} else {
					if (fillColor != null) {
						g.setColor(fillColor);
					} else {
						g.setColor(borderColor);
					}
					g.fillRect(cellX, cellY, fillWidth - 2, barSize - 2);

				}

				positionX += usedBanksCount;
				bankNumber += usedBanksCount;
			}
		}
		g.setColor(Color.white);
	}

	/**
	 * Computes the bank number which corresponds to the mouse position of a
	 * mouse event.
	 * 
	 * @param e
	 *            The mouse event, not <code>null</code>.
	 * 
	 * @return The workbook bank or <code>null</code>.
	 */
	private WorkbookBank getBankAt(MouseEvent e) {
		if (e == null) {
			throw new IllegalArgumentException(
					"Parameter 'e' must not be null.");
		}
		if (barSize > 0) {
			Point p = e.getPoint();
			int positionX = p.x - startX;
			int positionY = p.y - startY;
			if (positionX >= 0 && positionY >= 0) {
				positionX /= barSize;
				positionY /= barSize;
				if (positionX < banksPerRow && positionY < rows) {
					int bankNumber = positionX + positionY * banksPerRow;
					if (bankNumber < banksList.size()) {
						return banksList.get(bankNumber);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Computes the tool tip text for a cell.
	 * 
	 * @param e
	 *            The mouse event, not <code>null</code>.
	 */
	void computeToolTipText(MouseEvent e) {
		if (e == null) {
			throw new IllegalArgumentException(
					"Parameter 'e' must not be null.");
		}
		String text;
		text = null;
		WorkbookBank bank = getBankAt(e);
		if (bank != null) {
			int bankNumber = bank.getNumber();
			List<WorkbookEntry> entries = bank.getEntries();
			String bankNumberText = TextUtility.formatAsDecimal(bankNumber);
			WorkbookRoot root = workbook.getRoot();
			int bankSize = root.getBankSize();
			int maxBankOffsetValue = root.getBankCount() * bankSize;
			int bankOffsetStart = bank.getNumber() * bankSize;
			int flashBlockSize = root.getCartridgeType().getFlashBlockSize();
			String bankOffsetText;
			if (flashBlockSize == 0) {
				bankOffsetText = Messages.I127.format(TextUtility
						.formatAsHexaDecimal(bankOffsetStart,
								maxBankOffsetValue), TextUtility
						.formatAsHexaDecimal(bankOffsetStart + bankSize - 1,
								maxBankOffsetValue));
			} else {
				int blockNumber = bankOffsetStart / flashBlockSize;
				bankOffsetText = Messages.I128.format(TextUtility
						.formatAsHexaDecimal(bankOffsetStart,
								maxBankOffsetValue), TextUtility
						.formatAsHexaDecimal(bankOffsetStart + bankSize - 1,
								maxBankOffsetValue), TextUtility
						.formatAsDecimal(blockNumber));
			}

			ReservedContentProvider reservedContentProvider = bank
					.getReservedContentProvider();
			if (entries.isEmpty()) {
				if (bank.isReserved()) {
					// INFO: ROM bank {0} is reserved for '{1}'<br/>{2}
					text = Messages.I121.format(bankNumberText,
							reservedContentProvider.getTitle(), bankOffsetText);
				} else {
					// INFO: ROM bank {0} is free<br/>{1}
					text = Messages.I120.format(bankNumberText, bankOffsetText);
				}
			} else {
				if (bank.isReserved()) {
					// INFO: ROM bank {0} is reserved for '{1}'<br/>{2}
					text = Messages.I121.format(bankNumberText,
							reservedContentProvider.getTitle(), "");
				} else {
					text = "";
				}
				if (entries.size() == 1) {
					WorkbookEntry entry = entries.get(0);
					// INFO: ROM bank {0} contains entry
					// {1}<br/>{2}<br/>{3}<br/>Entry bank {4} of {5}
					text += Messages.I122.format(bankNumberText, entry
							.getTitle(), bankOffsetText, entry.getFileName(),
							TextUtility.formatAsDecimal(bankNumber + 1
									- entry.getStartBankNumber()), TextUtility
									.formatAsDecimal(entry
											.getRequiredBanksCount()));
				} else {
					// INFO: ROM bank {0} is assigned to multiple
					// entries<br/>{1}
					text += Messages.I123
							.format(bankNumberText, bankOffsetText);
				}
			}
			text = "<html>" + text + "</html>";
		}

		setToolTipText(text);
	}

	void showWorkbookEntry(MouseEvent e) {
		if (e == null) {
			throw new IllegalArgumentException(
					"Parameter 'e' must not be null.");
		}
		WorkbookBank bank = getBankAt(e);
		if (bank != null && bank.getEntries().size() == 1) {
			studio.performShowWorkbookEntry(bank.getEntries().get(0),
					WorkbookEntry.Attributes.ID);
		}

	}
}
