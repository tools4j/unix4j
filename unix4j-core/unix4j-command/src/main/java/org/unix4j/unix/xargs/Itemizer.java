package org.unix4j.unix.xargs;

import org.unix4j.line.Line;

/**
 * Itemizes one or multiple lines and stores the found items in an
 * {@link ItemStorage}.
 */
public interface Itemizer {
	/**
	 * Itemize the given line and store all found items in the given item
	 * storage. Returns true if enough items have been provided for a command
	 * invocation, and false if more lines should be itemized before the next
	 * invocation.
	 * 
	 * @param line
	 *            the line to itemize
	 * @param itemStorage
	 *            the storage for the found items
	 * @return true if a command invocation should be triggered, and false if
	 *         more lines should be itemized before the next invocation
	 */
	boolean itemizeLine(Line line, boolean eof, ItemStorage itemStorage);
}
