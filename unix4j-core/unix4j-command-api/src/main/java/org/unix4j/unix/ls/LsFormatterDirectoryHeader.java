package org.unix4j.unix.ls;

import java.io.File;
import java.util.List;

import org.unix4j.io.Output;
import org.unix4j.util.FileUtil;

class LsFormatterDirectoryHeader implements LsFormatter {

	static Factory FACTORY = new Factory() {
		@Override
		public LsFormatter create(File relativeTo, File directory, List<File> directoryFiles, LsArgs args) {
			return new LsFormatterDirectoryHeader(directoryFiles, args);
		}
	};

	private final long totalBytes;

	LsFormatterDirectoryHeader(List<File> directoryFiles, LsArgs args) {
		long totalBytes = 0L;
		for (final File f : directoryFiles) {
			if (f.isFile()) {
				totalBytes += f.length();
			}
		}
		this.totalBytes = totalBytes;
	}

	@Override
	public void writeFormatted(File relativeTo, File file, LsArgs args, Output output) {
		final String relativePath = FileUtil.getRelativePath(relativeTo, file);
		if (!".".equals(relativePath)) {
			output.writeLine(relativePath);
		}
		output.writeLine("total: " + args.getSizeString(totalBytes));
	}

}
