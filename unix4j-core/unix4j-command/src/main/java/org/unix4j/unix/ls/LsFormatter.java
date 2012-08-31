package org.unix4j.unix.ls;

import java.io.File;
import java.util.List;

import org.unix4j.line.LineProcessor;

/**
 * Interface used by the different output formats of the ls command.
 */
interface LsFormatter {
	/**
	 * Writes information of the given file to the {@code output}.
	 * 
	 * @param relativeTo
	 *            the starting directory to use as starting point to get to
	 *            {@code file}; all path elements from this directory to
	 *            {@code file} should be printed
	 * @param file
	 *            the file whose name or other information is written to
	 *            {@code output}
	 * @param args
	 *            arguments possibly used to lookup some formatting options
	 * @param output
	 *            the output object to write to
	 * @return true if the {@code output} object is ready to take more output
	 *         lines, and false if not
	 */
	boolean writeFormatted(File relativeTo, File file, LsArguments args, LineProcessor output);

	interface Factory {
		/**
		 * Creates and returns a new formatter for the files in the specified
		 * {@code directory}.
		 * 
		 * @param relativeTo
		 *            the starting directory to use as starting point to get to
		 *            {@code directory}; all path elements from the relative
		 *            directory to the files in {@code directory} should be
		 *            printed
		 * @param directory
		 *            the directory whose files should be formatted
		 * @param directoryFiles
		 *            the directory files to be formatted by the returned
		 *            formatter
		 * @param args
		 *            arguments possibly used to lookup some formatting options
		 */
		LsFormatter create(File relativeTo, File directory, List<File> directoryFiles, LsArguments args);
	}

}