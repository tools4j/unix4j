package org.unix4j.unix.find;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter based on file length.
 */
class SizeFilter implements FileFilter {
	public static enum SizeComparator {
		AtLeast {
			@Override
			public boolean accept(File file, long sizeInBytes) {
				return file.length() >= sizeInBytes;
			}
		}, 
		AtMost {
			@Override
			public boolean accept(File file, long sizeInBytes) {
				return file.length() <= sizeInBytes;
			}
		}; 
		public abstract boolean accept(File file, long size);
	}

	private final long size;
	private final SizeComparator comparator;;
	
	public SizeFilter(long size) {
		this(size, size > 0 ? SizeComparator.AtLeast : SizeComparator.AtMost);
	}
	public SizeFilter(long size, SizeComparator comparator) {
		this.size = Math.abs(size);
		this.comparator = comparator;
	}
	@Override
	public boolean accept(File file) {
		return comparator.accept(file, size);
	}

}
