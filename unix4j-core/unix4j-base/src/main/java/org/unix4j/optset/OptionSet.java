package org.unix4j.optset;

import java.util.Set;

/**
 * An option set is an unmodifiable set of options.
 * 
 * @param <E>
 *            the enum option type
 */
public interface OptionSet<E extends Enum<E>> {
	/**
	 * Returns true if the specified {@code option} is set and false otherwise
	 * 
	 * @param option
	 *            the option to test
	 * 
	 * @return true if {@code option} is set in this {@code OptionSet}
	 */
	boolean isSet(E option);

	/**
	 * Returns an unmodifiable version of the backing set with options.
	 * 
	 * @return an unmodifiable set containing all set options
	 */
	Set<E> asSet();
}
