package org.unix4j.io;

/**
 * Line based input for a command.
 */
public interface Input {
	/**
	 * Returns true if there are more lines to be read.
	 * 
	 * @return true if more lines exist
	 */
	boolean hasMoreLines();

	/**
	 * Reads the next line. Returns null if no next line exists.
	 * 
	 * @return the next line, or null if no next line exists.
	 */
	String readLine();
}
