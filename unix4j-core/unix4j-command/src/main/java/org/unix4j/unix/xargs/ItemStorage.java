package org.unix4j.unix.xargs;

import org.unix4j.variable.VariableContext;

/**
 * Stores items found by an {@link Itemizer}. The items are usually stored in
 * a {@link VariableContext} in form of string variables.
 */
public interface ItemStorage {
	/**
	 * Stores the specified item
	 * @param item
	 */
	void storeItem(String item);
	/**
	 * Returns the number of items that have been stored so far.
	 */
	int size();
}
