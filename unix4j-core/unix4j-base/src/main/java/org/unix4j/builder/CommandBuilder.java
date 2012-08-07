package org.unix4j.builder;

import org.unix4j.command.Command;
import org.unix4j.redirect.To;

public interface CommandBuilder extends To {
	/**
	 * Builds the composite command and returns it. The returned command
	 * contains a join of all the commands that have been joined up by invoking
	 * command specific methods of this fromFile.
	 * <p>
	 * This method is rarely used by application code. Usually one of the
	 * execute(..) methods is invoked directly. To get a string representation
	 * of the built command, the command fromFile's toString() method can be
	 * used.
	 *
	 * @return a newly created composite command based on the commands joined up
	 *         by invoking command specific methods of this fromFile
	 */
	Command<?> build();

	/**
	 * Returns a string representation of the composite command that would be
	 * returned by {@link #build()}. A composite command string looks for
	 * instance like this:
	 *
	 * <pre>
	 * &quot;echo -messages [Hello WORLD] | grep -matchString world -ignoreCase&quot;
	 * </pre>
	 *
	 * <p>
	 * Use {@link #toStringResult()} instead to execute the command and return the
	 * output as a string.
	 *
	 * @return the composite command string with joined commands including
	 *         arguments and options
	 */
	@Override
	String toString();
}
