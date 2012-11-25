package org.unix4j.variable;

import org.unix4j.convert.ValueConverter;

public interface VariableContext {
	/**
	 * Sets the specified value for the variable with the given name. If the
	 * value is null, the variable is cleared. Returns the old value held by the
	 * value before setting it, or null if no such variable existed.
	 * 
	 * @param name
	 *            the variabel name
	 * @param value
	 *            the new value for the variable, null to clear the variable
	 * @return the old value held by the variable before setting it, or null if
	 *         the variable did not exist before
	 */
	Object setValue(String name, Object value);

	/**
	 * Returns the value currently set for the variable given by its name.
	 * Returns null if no such variable exists.
	 * 
	 * @param name
	 *            the variable name
	 * @return the value of the variable, or null if the variable does not exist
	 */
	Object getValue(String name);

	/**
	 * Returns the value of the variable given by name and converts it into the
	 * target type {@code <V>} using the given converter. Returns null if no
	 * such variable exists. An exception is thrown if the conversion fails, or
	 * which is indicated by a null value returned by the converter for a
	 * non-null input value.
	 * 
	 * @param <V>
	 *            the target value type
	 * @param name
	 *            the variable name
	 * @param converter
	 *            a converter suitable to convert values into the target type
	 *            {@code <V>}
	 * @return the converted value, or null if the variable does not exist
	 * @throws IllegalArgumentException
	 *             if the value conversion fails, that is, if the converter
	 *             returns null for the non-null input value
	 */
	<V> V getValue(String name, ValueConverter<V> converter) throws IllegalArgumentException;
}
