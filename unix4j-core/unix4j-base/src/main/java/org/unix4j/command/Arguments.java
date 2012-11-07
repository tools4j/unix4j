package org.unix4j.command;

import org.unix4j.variable.VariableContext;

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
	 * Returns an arguments object with resolved variables where this is
	 * possible in the given variable context.
	 * <p>
	 * The returned value is the same instance as the current arguments object
	 * if no variables are used to define argument values. If variables are used 
	 * to define argument values, the returned arguments object is a derivative 
	 * of this instance where all variables defined in {@code context} have 
	 * been resolved.
	 * 
	 * @param context
	 *            the variable context with values for the variables
	 * @return an arguments object with resolved variables where possible
	 */
	A getForContext(VariableContext context);

	/**
	 * Returns a string representation of the command arguments and options.
	 * 
	 * @return a string representation of the command arguments and options,
	 *         such as "-matchString myString -ignoreCase"
	 */
	@Override
	String toString();
}
