package org.unix4j.builder;

import org.unix4j.command.Command;
import org.unix4j.io.Output;

import java.io.File;

public interface CommandBuilder {
	/**
	 * Builds the composite command and returns it. The returned command
	 * contains a join of all the commands that have been joined up by invoking
	 * command specific methods of this fromFile.
	 * <p>
	 * This method is rarely used by application code. Usually one of the
	 * toFile(..) methods is invoked directly. To get a string representation
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
	 * @return the composite command string with joined commands including
	 *         arguments and options
	 */
	@Override
	public String toString();

	/**
	 * Executes the composite command and writes the result to the standard
	 * output.
	 */
	void toStdOut();

	/**
	 * Executes the composite command and writes the result to the given file.
	 */
	void toFile(File file);

	/**
	 * Executes the composite command and writes the result to the given output.
	 */
	void toOutput(Output output);

	/**
	 * Executes the composite command and returns the result as string.
	 */
	String toStringResult();
}
