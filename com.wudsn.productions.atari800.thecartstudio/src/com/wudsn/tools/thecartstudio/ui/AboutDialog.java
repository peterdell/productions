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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.wudsn.tools.base.common.Application;
import com.wudsn.tools.base.common.TextUtility;
import com.wudsn.tools.base.gui.Desktop;
import com.wudsn.tools.base.gui.SimpleDialog;
import com.wudsn.tools.thecartstudio.Messages;
import com.wudsn.tools.thecartstudio.Texts;
import com.wudsn.tools.base.atari.cartridge.CartridgeDatabase;
import com.wudsn.tools.thecartstudio.model.CartridgeMenu;
import com.wudsn.tools.thecartstudio.model.CartridgeMenuType;
import com.wudsn.tools.thecartstudio.model.ContentType;

/**
 * About dialog with version information and credits.
 * 
 * @author Peter Dell
 */

public final class AboutDialog extends SimpleDialog {
	private CartridgeDatabase cartridgeDatabase;
	private JLabel contentLabel;

	public AboutDialog(JFrame parent, CartridgeDatabase cartridgeDatabase) {
		super(parent, Texts.MainWindow_Title, true);
		if (cartridgeDatabase == null) {
			throw new IllegalArgumentException(
					"Parameter 'cartridgeDatabase' must not be null.");
		}
		this.cartridgeDatabase = cartridgeDatabase;
	}

	@Override
	protected void initComponents(JDialog dialog) {

		Container pane = dialog.getContentPane();
		contentLabel = new JLabel();
		JPanel panel = new JPanel();
		panel.add(contentLabel);
		pane.add(panel, BorderLayout.CENTER);

		contentLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					String url = Texts.AboutDialog_URL;
					Desktop.openBrowser(url);
				}
			}
		});
		dataToUI();
		dialog.setSize(230, 200);
		initButtonBar();
	}

	@Override
	protected void dataToUI() {
		String content = Texts.AboutDialog_Content;
		Application application = Application.getInstance();
		String localVersion = application.getLocalVersion();
		String webVersion = application.getWebVersion();
		content = content.replace("$localVersion$", localVersion);

		CartridgeMenu cartridgeMenu = CartridgeMenu
				.createInstance(CartridgeMenuType.SIMPLE);
		String cartridgeMenuVersion = cartridgeMenu.getVersion();
		if (!cartridgeMenu.isExternal()) {
			// INFO: Built-in Atari Software Version {0}
			cartridgeMenuVersion = Messages.I130.format(cartridgeMenuVersion);
		} else {
			// INFO: External Atari Software Version {0} loaded from {1}
			cartridgeMenuVersion = Messages.I131.format(cartridgeMenuVersion,
					cartridgeMenu.getExternalFilePath());
		}

		content = content.replace("$cartridgeMenuVersion$",
				cartridgeMenuVersion);
		content = content.replace("$definedContentTypesCount$",
				TextUtility.formatAsDecimal(ContentType.getValues().size()));
		content = content.replace("$knownTitlesCount$", TextUtility
				.formatAsDecimal(cartridgeDatabase.getKnownTitelsCount()));
		content = content
				.replace("$knownContentTypesCount$", TextUtility
						.formatAsDecimal(cartridgeDatabase
								.getKnownCartridgeTypesCount()));

		String javaRuntimeVersion = System.getProperty("java.runtime.name")
				+ " " + System.getProperty("java.runtime.version");
		String osVersion = System.getProperty("os.name") + " ("
				+ System.getProperty("os.version") + ", "
				+ System.getProperty("os.arch") + ")";
		String maximumMemory = application.getMemoryInfo().getMaximumMemoryMB();
		content = content.replace("$javaRuntimeVersion$", javaRuntimeVersion);
		content = content.replace("$osVersion$", osVersion);
		content = content.replace("$maximumMemory$", maximumMemory);

		String update;
		if (localVersion.equals(webVersion)) {
			// No update available
			update = Messages.I133.format();
		} else {
			if (!localVersion.equals(Application.UNKNOWN_VERSION)
					&& !webVersion.equals(Application.UNKNOWN_VERSION)) {

				if (localVersion.compareTo(webVersion) < 0) {
					// Newer version available
					update = Messages.I134.format(webVersion);
				} else {
					// Older version available
					update = Messages.I135.format(webVersion);
				}
			} else {
				// Other version available
				update = Messages.I136.format(webVersion);
			}
		}
		content = content.replace("$update", update);
		contentLabel.setText(content);
	}
}