package org.unix4j.unix.ls;

import java.io.File;
import java.util.Comparator;

import org.unix4j.util.CompositeComparator;

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
	 * Sorts files according to file type and then by name. Hidden files are
	 * listed first, then normal files, all other files and finally the
	 * directories.
	 */
	static Comparator<File> DEFAULT = new CompositeComparator<File>(FILE_TYPE, FILE_NAME);

	/**
	 * Sorts files according to the last modified time (last touched files first)
	 * and then by name.
	 */
	static Comparator<File> TIME = new CompositeComparator<File>(FILE_LAST_MODIFIED, FILE_NAME);

	// no instances
	private FileComparators() {
		super();
	}

}
