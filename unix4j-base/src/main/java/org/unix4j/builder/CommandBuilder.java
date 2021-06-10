package org.unix4j.builder;

import org.unix4j.command.Command;
import org.unix4j.command.NoOp;
import org.unix4j.operation.LineOperation;

/**
 * A builder used to build a single command or a chain of joined commands. Every
 * command other than the last command redirects its output to the standard
 * input of the next command. The last command writes its output to the final
 * output destination. Executing a command and returning the output in different
 * formats is reflected by the {@code toXXX(..)} methods inherited from the
 * pseudo {@link To} command.
 */
public interface CommandBuilder extends To {

	/**
	 * Adds the specified command to the chain of commands held by this builder.
	 * Application code rarely needs to call this method directly; the command
	 * specific methods are usually called instead to join an instance of a
	 * certain command, e.g. one of the {@code grep(..)} methods to join a grep
	 * command.
	 * <p>
	 * If this is the fist joined command, the builder <i>builds</i> this
	 * command and returns or executes it if {@link #build()} or
	 * {@code toXXX(..)} is called, respectively. If the command argument joins
	 * already existing commands, the last command in the chain redirects its
	 * output to the standard input of the specified command.
	 * 
	 * @param command
	 *            the command to join the last previously joined command
	 * @return the builder for chained method invocation to join other commands
	 * @throws NullPointerException
	 *             if the command argument is null
	 */
	CommandBuilder join(Command<?> command);
	
	/**
	 * Adds a new command based on the specified operation and adds it to the
	 * chain of commands held by this builder.
	 * <p>
	 * If this is the fist joined command, the builder <i>builds</i> the command
	 * created from the operation and returns or executes it if {@link #build()} 
	 * or {@code toXXX(..)} is called, respectively. If the command argument joins
	 * already existing commands, the last command in the chain redirects its
	 * output to the standard input of the specified command.
	 * 
	 * @param operation
	 *            the operation on which the added command is based
	 * @return the builder for chained method invocation to join other commands
	 * @throws NullPointerException
	 *             if the operation argument is null
	 */
	CommandBuilder apply(LineOperation operation);

	/**
	 * Resets this command builder to its initial state. All joined commands are
	 * removed from the command chain leaving the builder in a state to build a
	 * {@link NoOp} command. New commands can be {@link #join(Command) joined}
	 * subsequently to create a new command chain.
	 * 
	 * @return the builder for chained method invocation for instance to join
	 *         the first new command
	 */
	CommandBuilder reset();

	/**
	 * Builds the composite command and returns it. The returned command
	 * contains a join of all the commands that have been joined up by invoking
	 * command specific methods of this builder.
	 * <p>
	 * This method is rarely used by application code. Usually one of the
	 * toXXX(..) methods is invoked to execute the command and return the output
	 * directly. To get a string representation of the built command, the
	 * command's toString() method can be used.
	 * 
	 * @return a newly created composite command based on the commands joined up
	 *         by invoking command specific methods of this builder
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
	 * Use {@link #toStringResult()} instead to execute the command and return
	 * the output as a string.
	 * 
	 * @return the composite command string with joined commands including
	 *         arguments and options
	 */
	@Override
	String toString();
}
