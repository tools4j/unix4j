package org.unix4j.unix.ls;

import java.io.File;

import org.unix4j.io.Output;

/**
 * Interface used by the different output formats of the ls command.
 */
interface LsFormatter {
	/**
	 * Writes information of the given file to the {@code output}.
	 * 
	 * @param file
	 *            the file whose name or other information is written to
	 *            {@code output}
	 * @param args
	 *            arguments possibly used to lookup some formatting options
	 * @param output
	 *            the output object to write to
	 */
	void writeFormatted(File file, LsArgs args, Output output);
	
	LsFormatter SHORT = new LsFormatter() {
		@Override
		public void writeFormatted(File file, LsArgs args, Output output) {
			output.writeLine(file.getPath());
		}
	};
	
	LsFormatter DIRECTORY_HEADER = new LsFormatter() {
		@Override
		public void writeFormatted(File file, LsArgs args, Output output) {
			output.writeLine(file.getPath());
			long totalBytes = 0;
			for (final File f : file.listFiles()) {
				if (f.isFile()) {
					totalBytes += file.length();
				}
			}
			output.writeLine("total: " + args.getSizeString(totalBytes));
		}
	};

	/**
	 * Long file output format.
	 */
	LsFormatter LONG = LsFormatterLong.getInstance();;

}