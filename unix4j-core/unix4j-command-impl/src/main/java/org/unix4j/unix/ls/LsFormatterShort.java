package org.unix4j.unix.ls;

import java.io.File;

import org.unix4j.line.LineProcessor;
import org.unix4j.line.SimpleLine;
import org.unix4j.util.FileUtil;

class LsFormatterShort implements LsFormatter {
	
	static final LsFormatterShort INSTANCE = new LsFormatterShort();
	
	@Override
	public void writeFormatted(File relativeTo, File file, LsArgs args, LineProcessor output) {
		final String relativePath = FileUtil.getRelativePath(relativeTo, file);
		output.processLine(new SimpleLine(relativePath));
	}
	
	//constructor used only for singleton INSTANCE
	private LsFormatterShort() {
		super();
	}
}
