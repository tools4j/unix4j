package org.unix4j.io;

import org.unix4j.line.Line;

import java.util.Iterator;

/**
 * Represents a line-by-line input device.
 */
public interface Input extends Iterable<Line>, AutoCloseable {
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
	Line readLine();

	/**
	 * Returns an immutable iterator over all lines returned by this input
	 * object.
	 * 
	 * @return an immutable line iterator
	 */
	@Override
	Iterator<Line> iterator();

	/**
	 * Close underlying resources if any, as for instance the source file.
	 */
	@Override
	void close();
}
