package org.unix4j;

public interface CommandInterface<R> {
	// interface defines no methods as they are all defined by the command
	// interfaces being sub-interfaces of this class
	/**
	 * Returns a string representation of the command instance including the
	 * argument and option values defined for the command.
	 * 
	 * @return a string representation of the command including arguments and
	 *         options, such as "grep -matchString myString -ignoreCase"
	 */
	String toString();
}
