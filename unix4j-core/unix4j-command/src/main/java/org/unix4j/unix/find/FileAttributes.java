package org.unix4j.unix.find;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Helper to access file attributes.
 */
enum FileAttributes {
	;

	private static BasicFileAttributes getBasicFileAttributes(File file) {
		try {
			return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		} catch (IOException e) {
			return null;
		}
	}

	static long getLastModifiedTime(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.lastModifiedTime().toMillis() : file.lastModified();
	}

	static long getCreationTime(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.creationTime().toMillis() : file.lastModified();
	}
	
	static long getLastAccessedTime(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.lastAccessTime().toMillis() : file.lastModified();
	}
	
	static boolean isDirectory(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isDirectory() : file.isDirectory();
	}
	
	static boolean isRegularFile(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isRegularFile() : file.isFile();
	}
	
	static boolean isSymbolicLink(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isSymbolicLink() : false;
	}
	
	static boolean isOther(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isOther() : !isDirectory(file) && !isRegularFile(file) && !isSymbolicLink(file);
	}
}
