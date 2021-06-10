package org.unix4j.unix.find;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter based on file name.
 */
class NameFilter implements FileFilter {
	private final String name;
	private final boolean ignoreCase;
	public NameFilter(String name, boolean ignoreCase) {
		this.name = name;
		this.ignoreCase = ignoreCase;
	}
	@Override
	public boolean accept(File file) {
		return ignoreCase ? name.equalsIgnoreCase(file.getName()) : name.equals(file.getName());
	}
}
