package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.io.Output;

/**
 * A command is an executable unit defined by the command {@link #getName()
 * name} and the command line {@link #getArguments() arguments}. To execute a
 * command, an {@link ExecutionContext} object is passed to the
 * {@link #execute(ExecutionContext, Input, Output) execute(..)} method along
 * with {@link Input} and {@link Output} for the command.
 * <p>
 * Commands can be {@link #join(Command) joined} to other commands which usually
 * means that the first command's output forms the input of the second command.
 * 
 * @param <A>
 *            the type parameter defining the arguments and options of the
 *            command
 * @param <L>
 *            the type parameter defining the local variable accessible
 *            throughout command execution via {@code context} parameter in the
 *            {@link #execute(ExecutionContext, Input, Output) execute(..)}
 *            method
 */
public interface Command<A extends Arguments<A>, L> {
	/**
	 * Returns the name of this command, usually a lower-case string such as
	 * "grep" or "ls".
	 * 
	 * @return the command nama, usually a lower case string
	 */
	String getName();

	/**
	 * Returns the implementation specific command arguments and options
	 * 
	 * @return the arguments and options for this command
	 */
	A getArguments();

	/**
	 * Returns a new command instance of the same type as this command. The new
	 * command instance uses the arguments and options specified by the given
	 * {@code arguments} parameter.
	 * 
	 * 
	 * @param arguments
	 *            the arguments and options to be used by the newly created
	 *            command
	 * @return a new command of the same type as this command using the
	 *         specified {@code arguments}
	 */
	Command<A, L> withArgs(A arguments);

	/**
	 * Creates and returns an initial local variable available throughout the
	 * {@link #execute(ExecutionContext, Input, Output) execute(..)} invocations
	 * of a command execution. The context local is initialized by and
	 * accessible through the {@link ExecutionContext} passed to the
	 * {@code execute(..)} method.
	 * 
	 * @return an initial local variable accessible throughout command execution
	 *         via {@code context} parameter in the
	 *         {@link #execute(ExecutionContext, Input, Output) execute(..)}
	 *         method
	 */
	L initializeLocal();

	/**
	 * Returns a new command representing the combination of {@code this}
	 * command with {@code next}. The returned command executes {@code this}
	 * command first and usually joins the output to the {@code next} command's
	 * input.
	 * <p>
	 * Note that some commands may use a slightly different interpretation of
	 * "joining a command". The {@link Xargs xargs} command for instance uses
	 * its joined command as target command; the values collected by xargs on
	 * its input stream are passed to the target command as arguments instead of
	 * as input.
	 * 
	 * @param next
	 *            the next command to join to this command
	 * @return a new command representing the combination of {@code this}
	 *         command joined to {@code next}
	 */
	<L2> Command<?, ?> join(Command<?, L2> next);

	/**
	 * Executes this command reading from the given {@code input} and writing to
	 * the {@code output} object. Note that the command execution can consist of
	 * a single or multiple {@code execute} method invocations as indicated by
	 * the {@link ExecutionContext#isInitial() initial} and
	 * {@link ExecutionContext#isTerminal() terminal} flags in the
	 * {@code context} object.
	 * <p>
	 * The method returns true if it expects another invocation with more input
	 * to be processed, and false if execution can be aborted because there is
	 * no need to process any further input. The return value is irrelevant and
	 * ignored if the {@code context} indicates that this is the
	 * {@link ExecutionContext#isTerminal() terminal} invocation.
	 * 
	 * @param context
	 *            the command execution context
	 * @param input
	 *            the input for the command
	 * @param output
	 *            the output to write to
	 * @return true if another invocation with more input is expected, and false
	 *         if this command has completed and no other invocation is
	 *         required.
	 */
	boolean execute(ExecutionContext<L> context, Input input, Output output);

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
