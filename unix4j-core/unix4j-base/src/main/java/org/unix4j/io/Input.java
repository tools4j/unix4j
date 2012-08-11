package org.unix4j.io;

import java.util.Iterator;

import org.unix4j.line.Line;

/**
 * Represents a line based input device.
 */
public interface Input extends Iterable<Line> {
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
	 * Returns an immutable iterator over all liens returned by this input
	 * object.
	 * 
	 * @return an immutable line iterator
	 */
	@Override
	public Iterator<Line> iterator();
}
