package org.unix4j.unix.find;

import org.unix4j.util.RelativePathBase;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * File filter based on file name using regular expressions for the comparison.
 * Note that find compares the whole path when regex is used, which is supported
 * by this class through {@link #getRelativePathFilterForBase(RelativePathBase)}
 * .
 */
class RegexFilter implements FileFilter {
	private final Pattern namePattern;

	/**
	 * Constructor with pattern and ignore-case flag.
	 * 
	 * @param namePattern
	 *            the regex pattern
	 * @param ignoreCase
	 *            true if matching should be case insensitive
	 */
	public RegexFilter(String namePattern, boolean ignoreCase) {
		if (ignoreCase) {
			this.namePattern = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);
		} else {
			this.namePattern = Pattern.compile(namePattern);
		}
	}

	/**
	 * Filter if name only is matched, NOT THE WHOLE PATH. Note that find
	 * usually matches the whole path (see
	 * {@link #getRelativePathFilterForBase(RelativePathBase)}).
	 */
	@Override
	public boolean accept(File file) {
		return namePattern.matcher(file.getName()).matches();
	}

	/**
	 * Returns a file filter that matches the whole (relative) path of the file
	 * instead of just the file name. The path is derived relative to the given
	 * base.
	 * 
	 * @param relativePathBase
	 *            basis for relative paths
	 * @return a filter performing a regexp match on the relative path
	 */
	public FileFilter getRelativePathFilterForBase(final RelativePathBase relativePathBase) {
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				final String relativePath = relativePathBase.getRelativePathFor(file);
				return namePattern.matcher(relativePath).matches();
			}
		};
	}
}
