package org.unix4j.optset;

import java.util.Set;

/**
 * An option set is a very simple unmodifiable set of options.
 * 
 * @param <O>
 *            the recursive type definition for the implementing option, usually
 *            an enum
 */
public interface OptionSet<O extends Option> {
	/**
	 * Returns true if the specified {@code option} is set and false otherwise
	 * 
	 * @param option
	 *            the option to test
	 * 
	 * @return true if {@code option} is set in this {@code OptionSet}
	 */
	boolean isSet(O option);

	/**
	 * Returns the active options in a {@link Set}. It depends on the
	 * implementation whether the returned set is modifiable or not.
	 * 
	 * @return an set containing all active options
	 */
	Set<O> asSet();

	/**
	 * Returns true if the {@link Option#acronym() acronym} should be used in
	 * for the specified {@code option} string representations. Note that some
	 * implementations may return the same value for all options.
	 * 
	 * @param option
	 *            the option of interest
	 * 
	 * @return true if the acronym should be used in string representations for
	 *         the specified {@code option}
	 */
	boolean useAcronymFor(O option);
}
