package org.unix4j.unix.ls;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.List;

class LsFormatterDirectoryHeader implements LsFormatter {

	static Factory FACTORY = new Factory() {
		@Override
		public LsFormatter create(File relativeTo, File directory, List<File> directoryFiles, LsArguments args) {
			return new LsFormatterDirectoryHeader(directoryFiles, args);
		}
	};

	private final long totalBytes;

	LsFormatterDirectoryHeader(List<File> directoryFiles, LsArguments args) {
		long totalBytes = 0L;
		for (final File f : directoryFiles) {
			if (f.isFile()) {
				totalBytes += f.length();
			}
		}
		this.totalBytes = totalBytes;
	}

	@Override
	public boolean writeFormatted(File relativeTo, File file, LsArguments args, LineProcessor output) {
		String relativePath = FileUtil.getRelativePath(relativeTo, file);
		if (!relativePath.startsWith(".") && !relativePath.startsWith("/")) {
			relativePath = "./" + relativePath;
		}
		if (!".".equals(relativePath)) {
			if (!output.processLine(Line.EMPTY_LINE)) {
				return false;
			}
			if (!output.processLine(new SimpleLine(relativePath + ":"))) {
				return false;
			}
		}
		if (args.isLongFormat()) {
			return output.processLine(new SimpleLine("total: " + LsCommand.getSizeString(args, totalBytes)));
		}
		return true;
	}

}
