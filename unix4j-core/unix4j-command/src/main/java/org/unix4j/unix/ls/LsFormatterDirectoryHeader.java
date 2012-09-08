package org.unix4j.unix.ls;

import java.io.File;
import java.util.List;

import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.FileUtil;

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
		final String relativePath = FileUtil.getRelativePath(relativeTo, file);
		if (!".".equals(relativePath)) {
			if (!output.processLine(new SimpleLine(relativePath))) {
				return false;
			}
		}
		return output.processLine(new SimpleLine("total: " + LsCommand.getSizeString(args, totalBytes)));
	}

}
