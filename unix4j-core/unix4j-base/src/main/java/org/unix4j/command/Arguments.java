package org.unix4j.command;

import org.unix4j.context.ExecutionContext;
import org.unix4j.context.VariableContext;
import org.unix4j.convert.ValueConverter;

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
	 * Returns an arguments object for the given execution context with resolved
	 * variables if necessary and possible. If variables are defined in this
	 * arguments instance, a new arguments instance is created with values
	 * instead of variables for those variables that are defined in the
	 * specified context object. If this arguments instance uses no variables,
	 * {@code this} arguments instance is simply returned.
	 * <p>
	 * If variables are present, they are resolved if such a variable is defined
	 * in the {@link VariableContext} returned by 
	 * {@code context.}{@link ExecutionContext#getVariableContext() getVariableContext()}.
	 * Values are converted if necessary with the {@link ValueConverter}s
	 * returned by 
	 * {@code context.}{@link ExecutionContext#getValueConverterFor(Class)
	 * getValueConverterFor(Class)}.
	 * 
	 * @param context
	 *            the execution context providing access to variables and
	 *            converters
	 * @return an arguments object with resolved variables where possible
	 * @throws NullPointerException
	 *             if context is null
	 * @throws IllegalArgumentException
	 *             if variables are defined but cannot be converted into the
	 *             target type
	 */
	A getForContext(ExecutionContext context);

	/**
	 * Returns a string representation of the command arguments and options.
	 * 
	 * @return a string representation of the command arguments and options,
	 *         such as "-matchString myString -ignoreCase"
	 */
	@Override
	String toString();
}
