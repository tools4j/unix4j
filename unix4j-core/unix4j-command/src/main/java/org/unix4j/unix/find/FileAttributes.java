package org.unix4j.unix.find;

import java.io.File;

import org.unix4j.util.Java7Util;

/**
 * Helper to access file attributes if compiled and run with Java 6 or older. 
 * Supports only last modified time and some basic attributes.
 * <p>
 * Use the singleton {@link #INSTANCE} which loads the Java7 version if possible
 * with support for more file attributes.
 */
class FileAttributes {
	
	/**
	 * Singleton instance loading the Java7 version if possible with support for
	 * more file attributes.
	 */
	public static final FileAttributes INSTANCE = Java7Util.newInstance(FileAttributes.class, new FileAttributes());
	
	/**
	 * For subclass only, use {@link #INSTANCE} instead.
	 */
	protected FileAttributes() {
		super();
	}
	
	public long getLastModifiedTime(File file) {
		return file.lastModified();
	}
	public long getLastAccessedTime(File file) {
		return file.lastModified();//fallback if no Java7
	}
	public long getCreationTime(File file) {
		return file.lastModified();//fallback if no Java7
	}
	
	public boolean isDirectory(File file) {
		return file.isDirectory();
	}
	public boolean isRegularFile(File file) {
		return file.isFile();
	}
	public boolean isSymbolicLink(File file) {
		return false;
	}
	public boolean isOther(File file) {
		return !isDirectory(file) && !isRegularFile(file) && !isSymbolicLink(file);
	}
}
