package org.unix4j.command;


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
	 * Returns a string representation of the command arguments and options.
	 * 
	 * @return a string representation of the command arguments and options,
	 *         such as "-matchString myString -ignoreCase"
	 */
	@Override
	String toString();
}
