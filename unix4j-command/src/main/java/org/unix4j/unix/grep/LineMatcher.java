package org.unix4j.unix.grep;

import org.unix4j.line.Line;

/**
 * Returns true if a line matches a certain matching criterion. Different
 * implementations exist for the different grep options.
 */
interface LineMatcher {
	/**
	 * Returns true if the given {@code line} matches, and false otherwise.
	 * 
	 * @param line
	 *            the line to test
	 * @return true if the line matches
	 */
	boolean matches(Line line);
}
