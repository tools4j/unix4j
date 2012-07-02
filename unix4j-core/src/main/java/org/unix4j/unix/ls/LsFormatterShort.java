package org.unix4j.unix.ls;

import java.io.File;

import org.unix4j.io.Output;
import org.unix4j.util.FileUtil;

class LsFormatterShort implements LsFormatter {
	
	static final LsFormatterShort INSTANCE = new LsFormatterShort();
	
	@Override
	public void writeFormatted(File relativeTo, File file, LsArgs args, Output output) {
		final String relativePath = FileUtil.getRelativePath(relativeTo, file);
		output.writeLine(relativePath);
	}
	
	//constructor used only for singleton INSTANCE
	private LsFormatterShort() {
		super();
	}
}
