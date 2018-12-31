package com.wudsn.ideas.cube;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class OutputFile {
    private File file;
    private FileOutputStream fos;
    private boolean fileOpen;

    public OutputFile() {
    }

    public void open(String fileName) {
	if (fileName == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'fileName' must not be null.");
	}
	if (fileOpen) {
	    throw new IllegalStateException("File already opened");
	}
	file = new File(fileName);
	try {
	    fos = new FileOutputStream(file);
	} catch (FileNotFoundException ex) {
	    throw new RuntimeException("Cannot open file '" + fileName + "'",
		    ex);
	}

	fileOpen = true;
    }

    public boolean isOpen() {
	return fileOpen;
    }

    public void close() {
	if (!fileOpen) {
	    return;
	}
	try {
	    fos.close();
	} catch (IOException ex) {
	    throw new RuntimeException("Cannot close file '"
		    + file.getAbsolutePath() + "'", ex);
	}

	System.out.println("Output file '" + file.getAbsolutePath()
		+ "' created with " + Long.toHexString(file.length())
		+ " bytes .");

	fileOpen = false;

    }

    public void writeByte(int b) {
	try {
	    fos.write(b);
	} catch (IOException ex) {
	    throw new RuntimeException("Cannot write to file '"
		    + file.getAbsolutePath() + "'", ex);
	}
    }

    public void writeWord(int w) {
	writeByte(w & 0xff);
	writeByte((w & 0xff00) >> 8);
    }

}