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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;

import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.model.Preferences;
import com.wudsn.tools.thecartstudio.model.Workbook;
import com.wudsn.tools.thecartstudio.model.WorkbookBanksSummary;
import com.wudsn.tools.thecartstudio.model.WorkbookRoot;

@SuppressWarnings("serial")
public final class WorkbookBanksSummaryPanel extends JPanel {

	private final static class Bar {

		public final int start;
		public final int end;
		public final String text;

		public Bar(int start, int end, String text) {
			if (start > end) {
				throw new IllegalArgumentException(
						"Parameter 'start' must not be greater then parameter 'end'. Specified values are "
								+ start + " and " + end + ".");
			}
			if (text == null) {
				throw new IllegalArgumentException(
						"Parameter 'text' must not be null.");
			}
			this.start = start;
			this.end = end;
			this.text = text;
		}
	}

	private final Preferences preferences;
	private final Workbook workbook;
	private final List<Bar> bars;

	public WorkbookBanksSummaryPanel(Preferences preferences, Workbook workbook) {
		if (preferences == null) {
			throw new IllegalArgumentException(
					"Parameter 'preferences' must not be null.");
		}
		if (workbook == null) {
			throw new IllegalArgumentException(
					"Parameter 'workbook' must not be null.");
		}
		this.preferences = preferences;
		this.workbook = workbook;
		bars = new ArrayList<Bar>();
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
		});
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	protected void computeToolTipText(MouseEvent e) {
		for (Bar bar : bars) {
			if (bar.start <= e.getX() && e.getX() <= bar.end) {
				setToolTipText(bar.text);
				return;
			}
		}

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// No tooltips
		bars.clear();

		if (!workbook.isValid()) {
			return;
		}

		WorkbookRoot root = workbook.getRoot();
		if (!root.isBanksListInitialized()) {
			return;
		}

		WorkbookBanksSummary summary = root.getWorkbookBanksSummary();

		Dimension d = getSize();
		int clientWidth = d.width;
		int clientHeight = d.height;
		if (clientWidth == 0 || clientHeight == 0) {
			return;
		}

		int bankSize = workbook.getRoot().getBankSize();
		int banksDefined = summary.getDefinedBanks();
		int banksTotal = summary.getTotalBanks();
		int reservedBanksCount = summary.getReservedBanks();
		int requiredBanksCount = summary.getRequiredBanks();

		int freeBanksCount = banksDefined - requiredBanksCount
				- reservedBanksCount;
		reservedBanksCount = Math.min(reservedBanksCount, banksDefined);
		requiredBanksCount = Math.min(requiredBanksCount, banksDefined
				- reservedBanksCount);

		int startReserved = 0;
		int endReserved = startReserved
				+ ((clientWidth * reservedBanksCount) / banksTotal);
		int startRequired = endReserved;
		int endRequired = startRequired
				+ ((clientWidth * requiredBanksCount) / banksTotal);
		int startFree = endRequired;
		int endFree = clientWidth - 1;

		String text;
		if (reservedBanksCount >= 0) {
			text = Messages.I150.format(TextUtility
					.formatAsDecimal(reservedBanksCount), TextUtility
					.formatAsDecimalPercent(reservedBanksCount, banksDefined),
					TextUtility.formatAsMemorySize(reservedBanksCount
							* bankSize));
			addBar(g, preferences.getReservedBankColor(), startReserved,
					endReserved, text);
		}
		if (requiredBanksCount >= 0) {
			text = Messages.I151.format(TextUtility
					.formatAsDecimal(requiredBanksCount), TextUtility
					.formatAsDecimalPercent(requiredBanksCount, banksDefined),
					TextUtility.formatAsMemorySize(requiredBanksCount
							* bankSize));
			addBar(g, preferences.getUsedOddBankColor(), startRequired,
					endRequired, text);
		}
		if (freeBanksCount > 0) {
			text = Messages.I152.format(TextUtility
					.formatAsDecimal(freeBanksCount), TextUtility
					.formatAsDecimalPercent(freeBanksCount, banksDefined),
					TextUtility.formatAsMemorySize(freeBanksCount * bankSize));
			addBar(g, preferences.getFreeBankColor(), startFree, endFree, text);
		} else if (freeBanksCount < 0) {
			int missingBanksCount = -freeBanksCount;
			text = Messages.I153.format(TextUtility
					.formatAsDecimal(missingBanksCount), TextUtility
					.formatAsMemorySize(missingBanksCount * bankSize));
			addBar(g, preferences.getNotAvailableBankColor(), startFree,
					endFree, text);
		}
	}

	private void addBar(Graphics graphics, Color color, int start, int end,
			String text) {
		if (graphics == null) {
			throw new IllegalArgumentException(
					"Parameter 'graphics' must not be null.");
		}
		if (color == null) {
			throw new IllegalArgumentException(
					"Parameter 'color' must not be null.");
		}
		if (text == null) {
			throw new IllegalArgumentException(
					"Parameter 'text' must not be null.");
		}

		int startY = getLocation().y;
		int barHeight = 16;

		Bar bar = new Bar(start, end, text);
		bars.add(bar);

		graphics.setColor(color);
		graphics.fillRect(start, startY, end - start, barHeight);
	}
}
