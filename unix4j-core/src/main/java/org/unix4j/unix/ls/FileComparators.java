package org.unix4j.unix.ls;

import java.io.File;
import java.util.Comparator;

import org.unix4j.util.CompositeComparator;
import org.unix4j.util.FileUtil;

/**
 * Non-instantiable class with static comparator constants used to sort files.
 */
class FileComparators {
	/**
	 * Sorts files according to file type. Hidden files are listed first, then
	 * normal files, all other files and finally the directories.
	 */
	static Comparator<File> FILE_TYPE = new Comparator<File>() {
		@Override
		public int compare(File f1, File f2) {
			if (f1.isHidden()) {
				return f2.isHidden() ? 0 : -1;
			} else if (f2.isHidden()) {
				return 1;
			}
			if (f1.isFile()) {
				return f2.isFile() ? 0 : -1;
			} else if (f2.isFile()) {
				return 1;
			}
			if (!f1.isDirectory()) {
				return !f2.isDirectory() ? 0 : -1;
			} else if (!f2.isDirectory()) {
				return 1;
			}
			return 0;// both directories
		}
	};

	/**
	 * Sorts files alphabetically according to the file name.
	 */
	static Comparator<File> FILE_NAME = new Comparator<File>() {
		@Override
		public int compare(File f1, File f2) {
			return f1.getName().compareTo(f2.getName());
		}
	};

	/**
	 * Sorts files alphabetically according to the relative file name from the
	 * specified root to the actual file.
	 * 
	 * @param relativeTo
	 *            the root for the relative file name
	 * @return a comparator for alphabetical ordering of the relative file names
	 */
	static Comparator<File> relativeFileName(final File relativeTo) {
		return new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				final String relName1 = FileUtil.getRelativePath(relativeTo, f1);
				final String relName2 = FileUtil.getRelativePath(relativeTo, f1);
				return relName1.compareTo(relName2);
			}
		};
	}
	
	/**
	 * Sorts . before .. and then all other files.
	 * 
	 * @param relativeTo
	 *            the root to evaluate the . and .. files
	 * @return a comparator sorting . before .. before everything else
	 */
	static Comparator<File> dotDotdotRest(final File relativeTo) {
		return new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				final String relName1 = FileUtil.getRelativePath(relativeTo, f1);
				final String relName2 = FileUtil.getRelativePath(relativeTo, f2);

				final boolean isCur1 = ".".equals(relName1);
				final boolean isCur2 = ".".equals(relName2);
				if (isCur1) {
					return isCur2 ? 0 : -1;
				} else if (isCur2) {
					return 1;
				}

				final boolean isPar1 = "..".equals(relName1);
				final boolean isPar2 = "..".equals(relName2);
				if (isPar1) {
					return isPar2 ? 0 : -1;
				} else if (isPar2) {
					return 1;
				}

				return 0;
			}
		};
	}

	/**
	 * Sorts files according to the last-modified time. More recently touched
	 * files appear first in a sorted list.
	 */
	static Comparator<File> FILE_LAST_MODIFIED = new Comparator<File>() {
		@Override
		public int compare(File f1, File f2) {
			final long lastMod1 = f1.lastModified();
			final long lastMod2 = f2.lastModified();
			return lastMod1 > lastMod2 ? -1 : lastMod1 < lastMod2 ? 1 : 0;
		}
	};

	/**
	 * Sorts files according to file type and then by relative file name. Hidden
	 * files are listed first, then normal files, all other files and finally
	 * the directories.
	 * 
	 * @param relativeTo
	 *            the root for the relative file name
	 * @return a comparator first sorting by file type and then by relative file
	 *         name
	 */
	static Comparator<File> typeAndRelativeFileName(File relativeTo) {
		return new CompositeComparator<File>(dotDotdotRest(relativeTo), FILE_TYPE, relativeFileName(relativeTo));
	}

	/**
	 * Sorts files according to the last modified time (last touched files
	 * first) and then by relative file name.
	 * @param relativeTo
	 *            the root for the relative file name
	 * @return a comparator first sorting by last modified time and then by relative file
	 *         name
	 */
	static Comparator<File> timeAndRelativeFileName(File relativeTo) {
		return new CompositeComparator<File>(FILE_LAST_MODIFIED, relativeFileName(relativeTo));
	}

	// no instances
	private FileComparators() {
		super();
	}

}
