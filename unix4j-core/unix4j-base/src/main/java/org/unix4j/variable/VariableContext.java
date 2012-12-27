package org.unix4j.variable;

import org.unix4j.convert.ValueConverter;

/**
 * The variable context is an extension of the somewhat simplistic 
 * {@link VariableResolver}. It extends the resolver by allowing registration of 
 * delegate resolvers and adding a convenience method to return converted 
 * variable values.
 */
public interface VariableContext extends VariableResolver {
	/**
	 * Adds a variable resolver to this variable context. The last recently 
	 * added resolver has preference over previously added resolvers. This 
	 * allows hiding of variables for instance to support different variable
	 * scopes with the same variable names.
	 *  
	 * @param resolver the resolver to add
	 */
	void addVariableResolver(VariableResolver resolver);

	/**
	 * Removes the specified variable resolver if it exists and does nothing
	 * otherwise. If this resolver exists multiple times, the most recently
	 * added object is removed.
	 *  
	 * @param resolver the resolver to remove
	 */
	void removeVariableResolver(VariableResolver resolver);

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
