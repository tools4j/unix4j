package org.unix4j.command;

import java.util.Map;

import org.unix4j.util.Variables;

/**
 * Interface implemented by command arguments. Arguments is usually a container
 * for argument values of different types as well as for option flags. The
 * implementing arguments class is command specific, hence the generic {@code A}
 * parameter specified by the concrete command implementation.
 * 
 * @param <A>
 *            the concrete command specific arguments type
 */
public interface Arguments<A extends Arguments<A>> {
	/**
	 * Returns a clone of this arguments object. If {@code deepClone} is true,
	 * all values (such as list argument values) are also cloned, recursively.
	 * 
	 * @param deepClone
	 *            if all values should be recursively cloned
	 * @return a clone of this arguments object, deeply cloned if
	 *         {@code deepClone==true}
	 */
	A clone(boolean deepClone);

	/**
	 * Resolves string argument values if they are variable expressions as
	 * defined by {@link Variables#isVariable(String)}. The variable values used
	 * to resolve variable names are taken from the specified {@code variables}
	 * map.
	 * 
	 * @param variables
	 *            the map with variable name (encoded) as map key and variable
	 *            value as map value
	 * @see Variables#isVariable(String)
	 * @see Variables#resolve(String, Map)
	 */
	void resolve(Map<String, String> variables);
}
