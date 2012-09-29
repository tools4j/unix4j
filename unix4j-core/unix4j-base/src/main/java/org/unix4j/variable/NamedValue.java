package org.unix4j.variable;

/**
 * A NamedValue is value that has a name associated with that value; typical
 * named values are constants and variables.
 * 
 * @param <V>
 *            the type of the value
 */
public interface NamedValue<V> {
	/**
	 * Returns the value type. 
	 * <p>
	 * Note that for generic types, the returned type represents the raw type of 
	 * the value, for instance {@code Collection.class} for a value type 
	 * {@code Collection<String>}.
	 * 
	 * @return the value type class
	 */
	Class<V> getType();

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
