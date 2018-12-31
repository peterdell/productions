package com.wudsn.productions.atari2600.tiatunes;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

import com.wudsn.tools.base.common.CoreException;
import com.wudsn.tools.base.common.FileUtility;
import com.wudsn.tools.base.common.StringUtility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TTTParser {

    final static class Artist implements Comparable<Artist> {
	public final String name;
	public final String folder;
	public final String demozooId;

	public Artist(String name, String folder, String demozooId) {
	    this.name = name;
	    this.folder = folder;
	    this.demozooId = demozooId;
	}

	@Override
	public int compareTo(Artist o) {
	    return name.compareTo(o.name);
	}
    }

    private Map<String, Artist> artistMap;

    public static void main(String[] args) {
	new TTTParser().run();
    }

    private TTTParser() {
	artistMap = new TreeMap<String, Artist>();
	addArtist("Glafouk", "glafouk", "68415");

    }

    private void addArtist(String name, String folder, String demozooId) {
	Artist artist = new Artist(name, folder, demozooId);
	artistMap.put(artist.name, artist);
    }

    private void run() {

	String sourceFolderName = "C:\\Users\\JAC\\Desktop\\Projects\\TIA-Tunes\\FlushVCSMusicCartVol.1\\Flush VCS Music Cart Vol.1";
	String targetFolderName = "C:\\jac\\system\\WWW\\Sites\\www.wudsn.com\\productions\\www\\mirror\\ftp.scene.org\\pub\\music\\artists";
	File sourceFolderFile = new File(sourceFolderName);
	File[] files = sourceFolderFile.listFiles(new FilenameFilter() {

	    @Override
	    public boolean accept(File dir, String filename) {
		return filename.toLowerCase().endsWith(".ttt");
	    }
	});
	for (File sourceFile : files) {
	    try {
		String fileContent = FileUtility.readString(sourceFile,
			FileUtility.MAX_SIZE_1MB);
		String fileName = sourceFile.getName();
		JSONObject jsonObject = new JSONObject(fileContent);
		String songName = jsonObject.getString("metaName");
		String author = jsonObject.getString("metaAuthor");
		String comment = jsonObject.getString("metaComment");
		log("File Name:" + fileName);
		log("Song Name:" + songName);

		log("Author:" + author);
		log("Comment:" + comment.replace("\n", " "));
		if (!(songName.replace(" ", "").toLowerCase() + ".ttt")
			.equals(fileName.replace("_", "").toLowerCase())) {
		    logError("File name to song name mismatch");

		} else {
		    log("");
		}
		Artist artist = artistMap.get(author);
		if (artist == null) {
		    logError("Unknown author");
		    continue;

		}
		String targetFileName = fileName.toLowerCase();
		targetFileName = targetFileName.substring(0,
			targetFileName.length() - 4);

		String artistFolder = targetFolderName + "/" + artist.folder;

		String NL = "\r\n";
		StringBuilder dizBuilder = new StringBuilder();
		dizBuilder.append("File     : " + fileName + NL);
		dizBuilder.append("Type     : Atari VCS Music" + NL);
		dizBuilder.append("Format   : TIATracker" + NL);
		dizBuilder.append("Artist   : " + artist.name);
		if (StringUtility.isSpecified(artist.demozooId)) {
		    dizBuilder.append(" ( https://demozoo.org/sceners/"
			    + artist.demozooId + "/ )");
		}
		dizBuilder.append(NL);

		dizBuilder.append("Title    : " + songName + NL);

		if (StringUtility.isSpecified(comment)) {
		    if (comment.contains("\n")) {
			dizBuilder.append("Comment  :" + NL + NL
				+ comment.replace("\n", NL));
		    } else {
			dizBuilder.append("Comment  :" + comment + NL);
		    }

		}

		File dizFile = new File(artistFolder, targetFileName + ".diz");
		FileUtility.writeString(dizFile, dizBuilder.toString());
		File zipFile = new File(artistFolder, targetFileName + ".zip");
		createZIP(zipFile, new String[] { sourceFile.getName(),
			"File_id.diz" }, new File[] { sourceFile, dizFile });

	    } catch (CoreException ex) {
		throw new RuntimeException(ex);
	    }
	}
    }

    private static void logError(String text) {
	System.err.println(text);
    }

    private static void log(String text) {
	System.out.println(text);
    }

    private static void createZIP(File zipFile, String[] fileNames, File[] files) {

	try {

	    // create byte buffer
	    byte[] buffer = new byte[1024];

	    FileOutputStream fos = new FileOutputStream(zipFile);

	    ZipOutputStream zos = new ZipOutputStream(fos);

	    for (int i = 0; i < files.length; i++) {

		File srcFile = files[i];

		FileInputStream fis = new FileInputStream(srcFile);

		// begin writing a new ZIP entry, positions the stream to the
		// start of the entry data
		zos.putNextEntry(new ZipEntry(fileNames[i]));

		int length;

		while ((length = fis.read(buffer)) > 0) {
		    zos.write(buffer, 0, length);
		}

		zos.closeEntry();

		// close the InputStream
		fis.close();

	    }

	    // close the ZipOutputStream
	    zos.close();

	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}

    }
};
