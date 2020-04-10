package org.unix4j.unix.find;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter based on file type.
 */
enum TypeFilter implements FileFilter, Optionable<FindOption> {
	Directory(FindOption.typeDirectory) {
		@Override
		public boolean accept(File file) {
			return FileAttributes.isDirectory(file);
		}
	},
	RegularFile(FindOption.typeFile) {
		@Override
		public boolean accept(File file) {
			return FileAttributes.isRegularFile(file);
		}
	},
	SymbolicLink(FindOption.typeSymlink) {
		@Override
		public boolean accept(File file) {
			return FileAttributes.isSymbolicLink(file);
		}
	},
	Other(FindOption.typeOther) {
		@Override
		public boolean accept(File file) {
			return FileAttributes.isOther(file);
		}
	};
	private final FindOption option;

	TypeFilter(FindOption option) {
		this.option = option;
	}

	@Override
	public FindOption getOption() {
		return option;
	}

	/**
	 * Returns the (first) type filter constant if such an option is set, and
	 * null if no type option is found.
	 * 
	 * @param options
	 *            the options
	 * @return the type filter constant based on the given options, or null if
	 *         no type option is set
	 */
	public static TypeFilter valueOf(FindOptions options) {
		return OptionableUtil.findFirstEnumByOptionInSet(TypeFilter.class, options, null);
	}
}
