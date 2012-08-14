package org.unix4j.optset;

/**
 * Interface usually implemented by option enums.
 */
public interface Option {
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
