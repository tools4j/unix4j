package org.unix4j.util;

import java.util.Map;

import org.unix4j.command.Command;

/**
 * Variables contains static utility methods related to variables or place
 * holders used as {@link Command} arguments. Variables are represented as
 * string of the form <tt>"{varname}"</tt>, where {@code varname} refers to the
 * name of the variable.
 */
public class Variables {

	/**
	 * Returns the string encoding for a variable with the given {@code name}.
	 * The returned string has the form <tt>"{varname}"</tt>. For instance, a
	 * variable named <tt>"hostname"</tt> would result in a string
	 * <tt>"{hostname}"</tt>.
	 * 
	 * @param varname
	 *            the name of the variable
	 * @return the encoded variable, a string of the form <tt>"{varname}"</tt>
	 */
	public static String encode(String varname) {
		return "{" + varname + "}";
	}

	/**
	 * Returns the string encoding for an indexed variable with the given
	 * {@code name} and {@code index}. The returned string has the form
	 * <tt>"{varname[index]}"</tt>. For instance, the second element of a
	 * variable named <tt>"args"</tt> would result in a string
	 * <tt>"{args[1]}"</tt>.
	 * 
	 * @param varname
	 *            the name of the variable
	 * @param index
	 *            the zero-based variable index
	 * @return the encoded variable, a string of the form
	 *         <tt>"{varname[index]}"</tt>
	 */
	public static String encode(String varname, int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("index must be >= 0, but was " + index);
		}
		return "{" + varname + "[" + index + "]}";
	}

	/**
	 * Returns true if the given {@code expression} represents an encoded
	 * variable. A variable expression starts with <tt>"{"</tt> and ends with
	 * <tt>"}"</tt> as described for {@link #encode(String)} and
	 * {@link #encode(String, int)}.
	 * 
	 * @param expression
	 *            the expression to be checked
	 * @return true if the expression represents a variable, that is, if it has
	 *         the form <tt>"{varname}"</tt>
	 */
	public static boolean isVariable(String expression) {
		final int len = expression == null ? 0 : expression.length();
		return len > 2 && expression.charAt(0) == '{' && expression.charAt(len - 1) == '}';
	}

	/**
	 * If {@code expression} represents an encoded variable, it is resolved
	 * using the values in the given {@code map}. The key in the map must
	 * exactly match the encoded expression to be successfully resolved. If no
	 * value is found for the variable expression, an expression is thrown.
	 * <p>
	 * If the given expression does not represent a variable, it is returned
	 * unchanged.
	 * 
	 * @param expression
	 *            the expression to resolve
	 * @param variables
	 *            the variable values with the encoded variable name as key
	 * @return the resolved variable if {@code expression} represents a
	 *         variable, and the unchanged expression otherwise
	 * @throws IllegalArgumentException
	 *             if {@code expression} represents a variable but no value for
	 *             this variable is found in the given {@code variables} map
	 */
	public static String resolve(String expression, Map<String, String> variables) {
		if (isVariable(expression)) {
			if (variables.containsKey(expression)) {
				return variables.get(expression);
			}
			throw new IllegalArgumentException("unresolved variable: " + expression);
		}
		return expression;
	}

	// no instances
	private Variables() {
		super();
	}
}