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
	 * Returns true if the string representation of this option set should use
	 * option {@link Option#acronym() acronyms} instead of the long option
	 * {@link Option#name() names}.
	 * 
	 * @return true if option acronyms should be used for string representations
	 *         of this option set
	 */
	boolean useAcronym();
}
