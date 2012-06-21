package org.unix4j.io;

/**
 * Line-oriented output destination for a command.
 */
public interface Output {
	/**
	 * Writes a line to the output.
	 * 
	 * @param line
	 *            the line to write to the output.
	 */
	void writeLine(String line);

	/**
	 * Indicates that all output has been written. May trigger a flush operation
	 * or close the underlying reader or stream.
	 */
	void finish();
}
