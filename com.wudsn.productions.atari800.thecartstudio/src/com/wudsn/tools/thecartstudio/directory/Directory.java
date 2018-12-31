package com.wudsn.tools.thecartstudio.directory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.wudsn.tools.base.gui.HTMLWriter;
import com.wudsn.tools.base.io.CSVReader;

public final class Directory {

	// A high byte of zero indicates the end of a record list.
	public static final int RECORD_ID_OFFSET = 256;
	public static final int BANKS_FOR_BANK_AND_OFFSET = 3;

	private String indexedCharacters;
	private List<DirectoryRecord> records;

	private int totalTitleLength;
	private int maximumTitleLength;

	private Set<String> allWordsSet;
	private int allWordsTotalLength;
	private int allWordsMaximumLength;

	private List<DirectoryLevel> levels;

	private int bankSize;
	private int totalBankCount;
	private int totalPaddingBytes;

	public Directory() {
		indexedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		records = new ArrayList<DirectoryRecord>();
		allWordsSet = new TreeSet<String>();
		levels = new ArrayList<DirectoryLevel>();
		bankSize = 8192;
	}

	public String getIndexedCharacters() {
		return indexedCharacters;
	}

	public void readRecords(String path, String titleColumnName) {
		if (path == null) {
			throw new IllegalArgumentException(
					"Parameter 'path' must not be null.");
		}
		if (titleColumnName == null) {
			throw new IllegalArgumentException(
					"Parameter 'titleColumnName' must not be null.");
		}
		records.clear();
		totalTitleLength = 0;
		maximumTitleLength = 0;

		allWordsSet.clear();
		allWordsTotalLength = 0;
		allWordsMaximumLength = 0;

		levels.clear();

		totalBankCount = 0;
		totalPaddingBytes = 0;

		CSVReader csvReader;
		csvReader = new CSVReader();
		csvReader.open(new File(path), ',', Charset.defaultCharset().name());
		int titleIndex = csvReader.getColumnIndex(titleColumnName);
		StringBuilder builder = new StringBuilder();

		Map<String, DirectoryRecord> allWordsMap = new TreeMap<String, DirectoryRecord>();

		while (csvReader.readNextRow()) {
			DirectoryRecord directoryRecord = new DirectoryRecord();
			String title = csvReader.getColumnValue(titleIndex);
			directoryRecord.setTitle(title);
			int titleLength = title.length();
			if (titleLength == 0) {
				continue;
			}
			totalTitleLength += titleLength;
			if (titleLength > maximumTitleLength) {
				maximumTitleLength = titleLength;
			}
			List<String> words = directoryRecord.getWords();
			for (int i = 0; i < titleLength; i++) {
				char c = title.charAt(i);
				c = Character.toUpperCase(c);
				if (indexedCharacters.indexOf(c) >= 0) {
					builder.append(c);
				} else {
					if (builder.length() > 0) {
						words.add(builder.toString());
						builder.setLength(0);
					}
				}
			}
			if (builder.length() > 0) {
				words.add(builder.toString());
				builder.setLength(0);
			}

			Collections.sort(words);
			records.add(directoryRecord);

			for (String word : words) {
				if (!allWordsSet.contains(word)) {
					allWordsSet.add(word);
					allWordsMap.put(word, directoryRecord);
					int wordLength = word.length();
					allWordsTotalLength += wordLength;
					if (wordLength > allWordsMaximumLength) {
						allWordsMaximumLength = wordLength;
					}
				}
			}

		}

		Collections.sort(records, new RecordTitleComparator());
		for (int i = 0; i < records.size(); i++) {
			records.get(i).setId(i + RECORD_ID_OFFSET);
		}
	}

	public int getTotalTitleLength() {
		return totalTitleLength;
	}

	public int getMaximumTitleLength() {
		return maximumTitleLength;
	}

	public Set<String> getAllWordsSet() {
		return allWordsSet;
	}

	public int getAllWordsTotalLength() {
		return allWordsTotalLength;
	}

	public int getAllWordsMaximumLength() {
		return allWordsMaximumLength;
	}

	public List<DirectoryRecord> getRecords() {
		return records;
	}

	public void createLevels() {
		DirectoryLevel root = new DirectoryLevel();
		root.getWords().addAll(allWordsSet);
		root.getRecords().addAll(records);
		root.createChildren(this, 1);
	}

	DirectoryLevel createLevel() {
		DirectoryLevel level = new DirectoryLevel();
		levels.add(level);
		level.setId(levels.size());
		return level;
	}

	public List<DirectoryLevel> getLevels() {
		return levels;
	}

	public int writeAsBinary(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException(
					"Parameter 'path' must not be null.");
		}
		FileOutputStream outputStream;

		outputStream = new FileOutputStream(path + ".dir");

		DirectoryWriter directoryWriter;
		directoryWriter = new DirectoryWriter();

		try {
			// Write low, high, bank of records.
			writeBanksAndOffsetsAsBinary(records, directoryWriter);

			// Write low, high, bank of levels.
			writeBanksAndOffsetsAsBinary(levels, directoryWriter);

			// Write the records.
			for (DirectoryRecord record : records) {
				record.writeAsBinary(directoryWriter);
			}

			// Write the levels.
			for (DirectoryLevel level : levels) {
				level.writeAsBinary(directoryWriter);
			}
			outputStream.write(directoryWriter.toByteArray());

			outputStream.close();

		} finally {
			try {
				outputStream.close();
			} catch (IOException ex1) {
				// ignore
			}
		}
		return directoryWriter.toByteArray().length;
	}

	private void writeBanksAndOffsetsAsBinary(
			List<? extends DirectoryEntry> entries,
			DirectoryWriter directoryWriter) {
		if (entries == null) {
			throw new IllegalArgumentException(
					"Parameter 'entries' must not be null.");
		}
		if (directoryWriter == null) {
			throw new IllegalArgumentException(
					"Parameter 'directoryWriter' must not be null.");
		}
		int paddingBytes = bankSize - entries.size();

		// Write the directory records low byte, high byte and banks.
		for (DirectoryEntry entry : entries) {
			directoryWriter.writeByte(entry.getOffset() & 0xff);
		}
		directoryWriter.writePaddingBytes(paddingBytes);

		for (DirectoryEntry entry : entries) {
			directoryWriter.writeByte(entry.getOffset() >>> 8);
		}
		directoryWriter.writePaddingBytes(paddingBytes);
		for (DirectoryEntry entry : entries) {
			directoryWriter.writeByte(entry.getBank());
		}
		directoryWriter.writePaddingBytes(paddingBytes);
	}

	public void writeAsHTML(String path) throws IOException {
		FileOutputStream outputStream;

		outputStream = new FileOutputStream(path + ".html");

		try {
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);

			HTMLWriter htmlWriter;
			htmlWriter = new HTMLWriter();

			htmlWriter.beginTable(true);
			htmlWriter.beginTableRow();
			htmlWriter.writeTableHeader("ID");
			htmlWriter.writeTableHeader("Bank");
			htmlWriter.writeTableHeader("Offset");
			htmlWriter.writeTableHeader("Raw Size");
			htmlWriter.writeTableHeader("Padding");
			htmlWriter.writeTableHeader("Title");
			htmlWriter.writeTableHeader("Words");
			htmlWriter.end();

			for (DirectoryRecord record : records) {
				htmlWriter.begin("tr", "id=\"record_" + record.getIdString()
						+ "\"");
				record.writeAsHTML(htmlWriter);
				htmlWriter.end();
			}
			htmlWriter.end();

			htmlWriter.beginTable(true);
			htmlWriter.beginTableRow();
			htmlWriter.writeTableHeader("ID");
			htmlWriter.writeTableHeader("Bank");
			htmlWriter.writeTableHeader("Offset");
			htmlWriter.writeTableHeader("Raw Size");
			htmlWriter.writeTableHeader("Padding");
			htmlWriter.writeTableHeader("Prefix");
			htmlWriter.writeTableHeader("Children");
			htmlWriter.writeTableHeader("Records Count");
			htmlWriter.writeTableHeader("Records");
			htmlWriter.end();
			for (DirectoryLevel level : levels) {
				htmlWriter.begin("tr", "id=\"level_" + level.getIdString()
						+ "\"");
				level.writeAsHTML(htmlWriter);
				htmlWriter.end();
			}
			htmlWriter.end();

			writer.write(htmlWriter.toHTML());

			writer.close();

		} finally {
			try {
				outputStream.close();
			} catch (IOException ex1) {
				// ignore
			}
		}
	}

	public void alignToBankSize(int bankSize) {
		this.bankSize = bankSize;

		int offset = 0;
		int bank = 0;
		int paddingBytes;
		totalPaddingBytes = 0;
		DirectoryEntry lastEntry = null;
		for (DirectoryRecord record : records) {
			int newOffset = offset + record.getRawSize();
			if (newOffset < bankSize) {
				record.setBankAndOffset(bank, offset);
				offset = newOffset;
			} else {
				paddingBytes = bankSize - offset;
				lastEntry.setPaddingBytes(paddingBytes);
				totalPaddingBytes += paddingBytes;
				bank++;
				record.setBankAndOffset(bank, 0);
				offset = record.getRawSize();
			}
			lastEntry = record;
		}
		for (DirectoryLevel level : levels) {
			int newOffset = offset + level.getRawSize();
			if (newOffset < bankSize) {
				level.setBankAndOffset(bank, offset);
				offset = newOffset;
			} else {
				paddingBytes = bankSize - offset;
				lastEntry.setPaddingBytes(paddingBytes);
				totalPaddingBytes += paddingBytes;
				bank++;
				level.setBankAndOffset(bank, 0);
				offset = level.getRawSize();

			}
			lastEntry = level;
		}
		if (lastEntry != null && offset != 0) {
			paddingBytes = bankSize - offset;
			lastEntry.setPaddingBytes(paddingBytes);
			totalPaddingBytes += paddingBytes;
		}
		totalBankCount = bank;
	}

	public int getBankSize() {
		return bankSize;
	}

	public int getTotalBankCount() {
		return totalBankCount + BANKS_FOR_BANK_AND_OFFSET * 2;
	}

	public int getTotalPaddingBytes() {
		return totalPaddingBytes;
	}

}
