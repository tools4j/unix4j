package org.unix4j.line;

/**
 * A {@code LineProcessor} is a program or device that processes line based
 * data.
 * <p>
 * An simple example of a {@code LineProcessor} is an output device that writes
 * the lines to a file; another more sophisticated {@code LineProcessor}
 * performs algorithmic operations based on the line input, for instance a
 * command that counts the lines and writes only the line count to the
 * destination file or stream.
 */
public interface LineProcessor {
	/**
	 * Processes a single line and returns true if this {@code LineProcessor} is
	 * ready to process more lines. Returning false indicates that the process
	 * can be {@link #finish() finished} because subsequent lines would not
	 * change the result anyway.
	 * 
	 * @param line
	 *            the line to process
	 * @return true if this {@code LineProcessor} is ready to process more
	 *         lines, and false if it does not require any more input lines
	 */
	boolean processLine(Line line);

	/**
	 * Indicates that this line processing task is complete and can finished.
	 * <p>
	 * Simple output devices usually perform a {@code flush} operation in this
	 * method. More complex commands may perform the real operation in this
	 * method, for instance write the total count of lines or words to the
	 * target file or stream.
	 */
	void finish();
}
