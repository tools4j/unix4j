package org.unix4j.unix.xargs;

import org.unix4j.line.Line;

/**
 * Itemizes one or multiple lines and stores the found items in an
 * {@link ItemStorage}.
 */
interface Itemizer {
	/**
	 * Itemize the given line and stores all found items in the item storage. 
	 * 
	 * @param line
	 *            the line to itemize
	 * @param itemStorage
	 *            the storage for the found items
	 */
	void itemizeLine(Line line, ItemStorage itemStorage);
	
	/**
	 * Called after the last line to flush remaining items.
	 * 
	 * @param itemStorage
	 *            the storage for the found items
	 */
	void finish(ItemStorage itemStorage);
}
