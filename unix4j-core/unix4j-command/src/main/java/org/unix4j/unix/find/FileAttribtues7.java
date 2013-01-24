package org.unix4j.unix.find;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Helper to access file attribtues if compiled and run with Java 7 or newer. 
 */
/* NOTE: must be public for reflection */
public class FileAttribtues7 extends FileAttributes {
	
	/**
	 * Done use this constructor, use {@link FileAttributes#INSTANCE} instead.
	 */
	//must be public for reflection
	public FileAttribtues7() {
		super();
	}

	private BasicFileAttributes getBasicFileAttributes(File file) {
		try {
			return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public long getLastModifiedTime(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.lastModifiedTime().toMillis() : super.getLastModifiedTime(file);
	}
	@Override
	public long getCreationTime(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.creationTime().toMillis() : super.getCreationTime(file);
	}
	@Override
	public long getLastAccessedTime(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.lastAccessTime().toMillis() : super.getLastAccessedTime(file);
	}
	
	@Override
	public boolean isDirectory(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isDirectory() : super.isDirectory(file);
	}
	
	@Override
	public boolean isRegularFile(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isRegularFile() : super.isRegularFile(file);
	}
	
	@Override
	public boolean isSymbolicLink(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isSymbolicLink() : super.isSymbolicLink(file);
	}
	
	@Override
	public boolean isOther(File file) {
		final BasicFileAttributes atts = getBasicFileAttributes(file);
		return atts != null ? atts.isOther() : super.isOther(file);
	}
}
