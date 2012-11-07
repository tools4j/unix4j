package org.unix4j.command;

import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.VariableContext;

/**
 * A command is an executable unit defined by the command {@link #getName()
 * name} and the command line {@link #getArguments(VariableContext) arguments}.
 * To execute a command, {@link #execute(ExecutionContext, LineProcessor)} is
 * called which returns a {@link LineProcessor} object to perform the
 * line-by-line command execution.
 * <p>
 * Commands can be {@link #join(Command) joined} to other commands which usually
 * means that the first command's output forms the input of the second command.
 * 
 * @param <A>
 *            the type parameter defining the arguments and options of the
 *            command
 */
public interface Command<A extends Arguments<A>> {
	/**
	 * Returns the name of this command, usually a lower-case string such as
	 * "grep" or "ls".
	 * 
	 * @return the command name, usually a lower case string
	 */
	String getName();

	/**
	 * Returns the implementation specific command arguments and options for the
	 * given variable context. 
	 * 
	 * @param context
	 *            the variable context for arguments defined as variables, or
	 *            null if no variables are used or if they need not be resolved
	 * @return the arguments and options for this command
	 */
	A getArguments(VariableContext context);

	/**
	 * Returns a new command representing the combination of {@code this}
	 * command with {@code next}. The returned command executes {@code this}
	 * command first and usually joins the output to the {@code next} command's
	 * input.
	 * <p>
	 * Note that some commands may use a slightly different interpretation of
	 * "joining a command". The {@code xargs} command for instance uses its
	 * joined command as target command; the values collected by {@code xargs}
	 * on its input stream are passed to the target command as arguments instead
	 * of as input.
	 * 
	 * @param next
	 *            the next command to join to this command
	 * @return a new command representing the combination of {@code this}
	 *         command joined to {@code next}
	 */
	Command<?> join(Command<?> next);

	/**
	 * Executes this command and returns a {@link LineProcessor} object. Calling
	 * this method initiates the command execution, but the real processing of
	 * the command takes place when lines are passed to the returned
	 * {@code LineProcessor} object. The command execution is terminated by
	 * calling {@link LineProcessor#finish()}.
	 * <p>
	 * The command writes its output to the specified {@code output} object.
	 * Depending on the command implementation, the output is written when lines
	 * are passed to the {@code LineProcessor} returned by this method, or when
	 * the execution terminates with the {@code finish()} call.
	 * 
	 * @param context
	 *            context object providing access to the current directory,
	 *            environment variables and other information useful for the
	 *            command during its execution
	 * @param output
	 *            the output to write to
	 * @return true if another invocation with more input is expected, and false
	 *         if this command has completed and no other invocation is
	 *         required.
	 */
	LineProcessor execute(ExecutionContext context, LineProcessor output);

	/**
	 * Returns a string representation of the command instance including the
	 * argument and option values defined for the command.
	 * 
	 * @return a string representation of the command including arguments and
	 *         options, such as "grep -matchString myString -ignoreCase"
	 */
	@Override
	String toString();
}
