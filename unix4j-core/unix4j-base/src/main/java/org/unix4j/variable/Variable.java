package org.unix4j.variable;

/**
 * A variable is a named value; the value assigned to a variable can change.
 *
 * @param <V> the type of the value
 */
public interface Variable<V> extends NamedValue<V> {
	/**
	 * Sets the value of this variable.
	 * @param value the new value for this variable.
	 */
	void setValue(V value);
}
