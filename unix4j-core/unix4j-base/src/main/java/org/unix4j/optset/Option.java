package org.unix4j.optset;

/**
 * Interface implemented by option enums.
 * 
 * @param <O>
 *            the recursive type definition for the implementing option, usually
 *            an enum
 */
public interface Option<O extends Option<O>> {
	/**
	 * Returns the option name, usually the same as the enum constant name.
	 * 
	 * @return the option long name
	 */
	String name();

	/**
	 * Returns a one letter acronym for this option.
	 * 
	 * @return the acronym character used for this option.
	 */
	char acronym();
}
