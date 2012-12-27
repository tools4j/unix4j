package org.unix4j.variable;

/**
 * A variable resolver returns a variable value given the name of the variable.
 */
public interface VariableResolver {
	/**
	 * Returns the value currently set for the variable given by its name, or 
	 * null if no such variable exists.
	 * 
	 * @param name
	 *            the variable name
	 * @return the value of the variable, or null if the variable does not exist
	 */
	Object getValue(String name);
}
