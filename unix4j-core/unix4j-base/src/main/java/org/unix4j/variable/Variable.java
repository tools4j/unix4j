package org.unix4j.variable;

/**
 * A variable is a name associated with a value; the value however is stored in
 * a {@link VariableContext} and therefore not part of this type.
 */
public interface Variable {
	/**
	 * Returns the variable name
	 * @return the name of the variable
	 */
	String getName();
}
