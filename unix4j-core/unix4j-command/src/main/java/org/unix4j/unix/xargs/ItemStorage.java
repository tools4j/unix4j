package org.unix4j.unix.xargs;

import org.unix4j.context.VariableContext;

/**
 * Stores items found by an {@link Itemizer}. The items are usually stored in
 * a {@link VariableContext} in form of string variables.
 */
interface ItemStorage {
	/**
	 * Stores the specified item. Storing an item may also trigger a command
	 * invocation if the max-args condition is fulfilled.
	 * 
	 * @param item the item to store
	 */
	void storeItem(String item);
	
	/**
	 * Increments the line count by one. Usually called once for every line, but
	 * sometimes a line is logically combined with the next line and therefore
	 * not counted.
	 */
	void incrementLineCount();
}
