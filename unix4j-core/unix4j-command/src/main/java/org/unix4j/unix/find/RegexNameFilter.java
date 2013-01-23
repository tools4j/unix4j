package org.unix4j.unix.find;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * File filter based on file name using regular expressions for the comparison.
 */
class RegexNameFilter implements FileFilter {
	private final Pattern namePattern;
	public RegexNameFilter(String namePattern, boolean ignoreCase) {
		if (ignoreCase) {
			this.namePattern = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);
		} else {
			this.namePattern = Pattern.compile(namePattern);
		}
	}
	@Override
	public boolean accept(File file) {
		return namePattern.matcher(file.getName()).matches();
	}
}
