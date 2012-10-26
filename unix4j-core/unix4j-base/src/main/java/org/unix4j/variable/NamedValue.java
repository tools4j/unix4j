package org.unix4j.variable;

/**
 * A NamedValue is a value that has a name associated with that value; typical
 * named values are constants and variables.
 * 
 * @param <V>
 *            the type of the value
 */
public interface NamedValue<V> {
	/**
	 * Returns the name associated with this value, usually the name of a
	 * constant or variable.
	 * 
	 * @return the name associated with the value, usually the constant or
	 *         variable name
	 */
	String getName();

	/**
	 * Returns the value, usually a constant or the current value of a variable.
	 * @return the value
	 */
	V getValue();
}
