package org.unix4j.unix.find;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A file filter that accepts a file only if all component filters accept the 
 * file. In other words, the composite filter performs an AND operation on the
 * outcome of all component filters. 
 */
class CompositeFileFilter implements FileFilter {
	
	private final List<FileFilter> componentFilters = new ArrayList<FileFilter>();
	
	public CompositeFileFilter() {
		super();
	}
	
	public void addIfNotNull(FileFilter filter) {
		if (filter != null) {
			add(filter);
		}
	}
	public void add(FileFilter filter) {
		componentFilters.add(filter);
	}
	
	public FileFilter simplify() {
		if (componentFilters.isEmpty()) {
			return new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return true;
				}
			};
		}
		return this;
	}
	
	@Override
	public boolean accept(File file) {
		for (final FileFilter filter : componentFilters) {
			if (!filter.accept(file)) {
				return false;
			}
		}
		return true;
	}

}
