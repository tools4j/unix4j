package org.unix4j;

import java.util.List;

import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.Opt;

/**
 * A composite command joining two commands. The output of the
 * {@link #getFirst() first} command is joined to the input of the
 * {@link #getSecond() second} command. An instance of this class is returned by
 * the {@link Command#join(Command) join(..)} method of a {@link Command}.
 * <p>
 * If arguments or options are added or set on a joined command, they are
 * applied to the second command in the join. Redirecting the output also
 * applies to the second command; input redirection affects the first command in
 * the join. Executing the joined command means executing the first command,
 * which usually triggers execution of the second command through the
 * input/output join.
 * 
 * @param <E2>
 *            the argument/options enum defining the keywords applicable to the
 *            second command in the join
 */
public class JoinedCommand<E2 extends Enum<E2>> implements Command<E2> {

	private final Command<?> first;
	private final Command<E2> second;

	/**
	 * Constructor with first and second command in the join.
	 * 
	 * @param first
	 *            the first command in the join, whose output is joined to the
	 *            input of the second command
	 * @param second
	 *            the second command in the join, whose input is joined from the
	 *            output of the first command
	 * @throws NullPointerException
	 *             if any of the command arguments is null
	 * 
	 */
	public JoinedCommand(Command<?> first, Command<E2> second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns the first command of this joined command
	 * 
	 * @return the first command in the join
	 */
	public Command<?> getFirst() {
		return first;
	}

	/**
	 * Returns the second command of this joined command
	 * 
	 * @return the second command in the join
	 */
	public Command<E2> getSecond() {
		return second;
	}

	/**
	 * Returns the joined command names, a string like "echo | grep"
	 * 
	 * @return the joined command names, a string like "echo | grep"
	 */
	@Override
	public String getName() {
		return first.getName() + " | " + second.getName();
	}

	/**
	 * Returns true if the first command is batchable.
	 * 
	 * @return true if the first command supports batch execution.
	 */
	@Override
	public boolean isBatchable() {
		return first.isBatchable();
	}

	/**
	 * Appends the given argument value to the second command in the join and
	 * returns {@code this} joined command for further processing.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @param value
	 *            the argument value
	 * @return {@code this} joined command for further chained processing
	 */
	@Override
	public <V> JoinedCommand<E2> withArg(Arg<E2, V> arg, V value) {
		second.withArg(arg, value);
		return this;
	}

	/**
	 * Appends the given argument values to the second command in the join and
	 * returns {@code this} joined command for further processing.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @param values
	 *            the argument values
	 * @return {@code this} joined command for further chained processing
	 */
	@Override
	public <V> JoinedCommand<E2> withArgs(Arg<E2, V> arg, V... values) {
		second.withArgs(arg, values);
		return this;
	}

	/**
	 * Appends the given argument values to the second command in the join and
	 * returns {@code this} joined command for further processing.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @param values
	 *            the argument values
	 * @return {@code this} joined command for further chained processing
	 */
	@Override
	public <V> JoinedCommand<E2> withArgs(Arg<E2, V> arg, List<? extends V> values) {
		second.withArgs(arg, values);
		return this;
	}

	/**
	 * Returns the argument list currently set for the second command in the
	 * join.
	 * 
	 * @param <V>
	 *            the argument value type
	 * @param arg
	 *            the argument key
	 * @return the argument list with the values currently set for the second
	 *         command and the given {@code arg} keyword
	 */
	@Override
	public <V> ArgList<E2, V> getArgs(Arg<E2, V> arg) {
		return second.getArgs(arg);
	}

	/**
	 * Sets the given option for the second command in the join.
	 * 
	 * @param opt
	 *            the option to set
	 * @return {@code this} joined command for further chained processing
	 */
	@Override
	public JoinedCommand<E2> withOpt(Opt<E2> opt) {
		second.withOpt(opt);
		return this;
	}

	/**
	 * Returns true if the specified option is currently set for the second
	 * command in the join.
	 * 
	 * @param opt
	 *            the option to check
	 * @return true if the given option is currently set for the second command,
	 *         and false otherwise
	 */
	@Override
	public boolean isOptSet(Opt<E2> opt) {
		return second.isOptSet(opt);
	}

	/**
	 * Joins this command with another command, resulting in a join
	 * {@code "first | second | next"}.
	 * 
	 * @param <E3>
	 *            the argument/options enum defining the keywords applicable to
	 *            {@code next}
	 * @param next
	 *            the command to be joined with {@code this} joined command
	 * @return the joined command {@code "first | second | next"}
	 */
	@Override
	public <E3 extends Enum<E3>> JoinedCommand<E3> join(Command<E3> next) {
		return new JoinedCommand<E3>(this, next);
	}

	/**
	 * Redirects the given {@code input} to the first command of this joined
	 * command.
	 * 
	 * @param input
	 *            the input for the first command in the join
	 * @return {@code this} joined command for further chained processing
	 * @throws IllegalStateException
	 *             if the first command does not support input redirection, for
	 *             instance because it requires no input
	 */
	@Override
	public JoinedCommand<E2> readFrom(Input input) {
		first.readFrom(input);
		return this;
	}

	/**
	 * Redirects the output of the second command of the join to the given
	 * {@code output}.
	 * 
	 * @param output
	 *            the output for the second command in the join
	 * @return {@code this} joined command for further chained processing
	 * @throws IllegalStateException
	 *             if the second command does not support output redirection,
	 *             for instance because it produces no output
	 */
	@Override
	public JoinedCommand<E2> writeTo(Output output) {
		second.writeTo(output);
		return this;
	}

	/**
	 * Executes this joined command. The execution is invoked by calling the
	 * execute method of the first command of the join, which usually triggers
	 * execution of the second command through the input/output join between
	 * {@link #getFirst() first} and {@link #getSecond() second}.
	 */
	@Override
	public void execute() {
		new Join(this).execute();
	}

	/**
	 * Returns a deep clone of this joined command. The joined commands are
	 * cloned and a new joined command instance is returned.
	 * 
	 * @return a deep clone of this command
	 */
	public JoinedCommand<E2> clone() {
		return new JoinedCommand<E2>(first.clone(), second.clone());
	}

	/**
	 * Returns the string representation of this joined command, consisting of
	 * first and second command including arguments and options.
	 * <p>
	 * An example string returned by a joined command is
	 * 
	 * <pre>
	 * &quot;echo -message Hello World | grep -i -expression hello world&quot;
	 * </pre>
	 * 
	 * @return the string representation of this joined command, such as
	 *         "echo -message Hello World | grep -i -expression hello world"
	 */
	@Override
	public String toString() {
		return first + " | " + second;
	}

}
