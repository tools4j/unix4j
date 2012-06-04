package org.unix4j;

import java.util.List;

import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.Opt;

/**
 * A command is a basic unix-like program or tool that can be {@link #execute()
 * executed}.
 * <p>
 * A command can have arguments and options and can redirect input and output to
 * files or other streams. A command can be joined to another command, meaning
 * that its output becomes the input of the next command.
 * 
 * @param <E>
 *            enum defining argument and option keywords for this command
 */
public interface Command<E extends Enum<E>> extends Cloneable {

	/**
	 * Returns the name of this command. The name is usually a lower case word
	 * such as "echo", "ls" or "grep".
	 * 
	 * @return the command name, usually one word in lower case
	 */
	String getName();

	/**
	 * Returns true if this command execute input in batches. If true is
	 * returned, the command might be called once per line if it is part of a
	 * command join. Returning false indicates that the program needs to operate
	 * on the complete input.
	 * <p>
	 * An example of a typical batch command is grep; it filters lines based on
	 * a given keyword or expression. Sort is a classic non-batchable command as
	 * it requires the complete input before it can be sorted and written to the
	 * output.
	 * 
	 * @return true if the command can be executed in batchs, and false if it
	 *         requires the complete input to operate
	 */
	boolean isBatchable();

	/**
	 * Returns a deep clone of this command. Argument lists and option map are
	 * also clone, but not the arguments and options themselves since they are
	 * usually not modified during the execution.
	 * <p>
	 * Clones are for instance used when commands are executed as parts of a
	 * join.
	 * 
	 * @return a deep clone of this command
	 */
	Command<E> clone();

	/**
	 * Appends the given argument value to this command and returns {@code this}
	 * command for further processing.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @param value
	 *            the argument value
	 * @return {@code this} command for further chained processing
	 */
	<V> Command<E> withArg(Arg<E, V> arg, V value);

	/**
	 * Appends the given argument values to this command and returns
	 * {@code this} command for further processing.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @param values
	 *            the argument values
	 * @return {@code this} command for further chained processing
	 */
	<V> Command<E> withArgs(Arg<E, V> arg, V... values);

	/**
	 * Appends the given argument values to this command and returns
	 * {@code this} command for further processing.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @param values
	 *            the argument values
	 * @return {@code this} command for further chained processing
	 */
	<V> Command<E> withArgs(Arg<E, V> arg, List<? extends V> values);

	/**
	 * Returns the argument list currently set for this command and the given
	 * argument key.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @return the argument list with the values currently set for this command
	 *         and the given {@code arg} keyword
	 */
	<V> ArgList<E, V> getArgs(Arg<E, V> arg);

	/**
	 * Sets the given command option
	 * 
	 * @param opt
	 *            the option to set
	 * @return {@code this} command for further chained processing
	 */
	Command<E> withOpt(Opt<E> opt);

	/**
	 * Returns true if the specified option is currently set for this command.
	 * 
	 * @param opt
	 *            the option to check
	 * @return true if the given option is currently set for the command, and
	 *         false otherwise
	 */
	boolean isOptSet(Opt<E> opt);

	/**
	 * Joins {@code this} command with {@code next} and returns a new command
	 * representing the joined command.
	 * <p>
	 * If arguments or options are added or set on the returned joined command,
	 * they are applied to the second command in the join. Redirecting the
	 * output also applies to the second command; input redirection affects the
	 * first command in the join. Executing the joined command means executing
	 * the first command, which usually triggers execution of the second command
	 * through the input/output join between {@code this} and {@code next}.
	 * 
	 * @param <E2>
	 *            the argument/options enum defining the keywords applicable to
	 *            {@code next}
	 * @param next
	 *            the command to be joined with {@code this}
	 * @return the joined command {@code "this | next"}
	 */
	<E2 extends Enum<E2>> JoinedCommand<E2> join(Command<E2> next);

	/**
	 * Redirects the given {@code input} to this command. It is good practice to
	 * throw an exception if a command does not support reading from an input
	 * object, for instance because it requires no input.
	 * 
	 * @param input
	 *            the input for this command
	 * @return {@code this} command for further chained processing
	 * @throws IllegalStateException
	 *             if this command does not support input redirection, for
	 *             instance because it requires no input
	 */
	Command<E> readFrom(Input input);

	/**
	 * Redirects the output of this command to the given {@code output}. It is
	 * good practice to throw an exception if a command does not support writing
	 * to an output object, for instance because it produces no output.
	 * 
	 * @param output
	 *            the output for this command
	 * @return {@code this} command for further chained processing
	 * @throws IllegalStateException
	 *             if this command does not support output redirection, for
	 *             instance because it produces no output
	 */
	Command<E> writeTo(Output output);

	/**
	 * Executes this command. Possibly reads from input and writes to output
	 * depending on the command implementation.
	 */
	void execute();

	/**
	 * Returns the string representation of this command including arguments and
	 * options. 
	 * <p>
	 * An example string returned by a command is
	 * <pre>
	 * "grep -i -expression hello world"
	 * </pre>
	 * 
	 * @return the string representation of this command, such as
	 *         "grep -i -expression hello world"
	 */
	String toString();
}
