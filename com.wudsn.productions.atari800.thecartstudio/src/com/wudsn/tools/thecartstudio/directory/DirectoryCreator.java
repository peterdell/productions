package com.wudsn.tools.thecartstudio.directory;

import java.io.IOException;

import com.wudsn.tools.base.common.Main;

public final class DirectoryCreator extends Main {

	public static void main(String[] args) {

		Directory directory = new Directory();

		String path;
		path = "C:\\jac\\system\\Java\\Programming\\Repositories\\Productions\\"
				+ "com.wudsn.productions.atari800.thecartstudio\\tst\\fandal\\atari.fandal.cz.csv";

		directory.readRecords(path, "files_nazev");

		logInfo("Total Record Count: " + directory.getRecords().size());
		logInfo("Total Title Length: " + directory.getTotalTitleLength());
		logInfo("Maximum Title Length: " + directory.getMaximumTitleLength());

		logInfo("Total Word Count: " + directory.getAllWordsSet().size());
		logInfo("Total Word Length: " + directory.getAllWordsTotalLength());
		logInfo("Maximum Word Length: " + directory.getAllWordsMaximumLength());
		logInfo("Words: " + directory.getAllWordsSet());
		logInfo(directory.getRecords().toString());

		directory.createLevels();
		directory.alignToBankSize(8192);
		logInfo("Bank Size: " + directory.getBankSize() + " ($"
				+ Integer.toHexString(directory.getBankSize()) + ")");
		logInfo("Total Bank Count: " + directory.getTotalBankCount());
		logInfo("Total Padding Bytes: " + directory.getTotalPaddingBytes());

		logInfo("Total Level Count: " + directory.getLevels().size());

		try {
			int directorySize = directory.writeAsBinary(path);
			logInfo("Total Directory Size: " + directorySize + " ($"
					+ Integer.toHexString(directorySize) + ")");

		} catch (IOException ex) {
			logError("Cannot write directory as binary. " + ex.getMessage());
		}

		try {
			directory.writeAsHTML(path);
		} catch (IOException ex) {
			logError("Cannot write directory as HTML. " + ex.getMessage());
		}
	}

}
