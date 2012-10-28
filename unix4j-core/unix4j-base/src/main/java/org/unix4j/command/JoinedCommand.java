package org.unix4j.command;

import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.VariableContext;

/**
 * A composite command joining two commands. The output of the
 * {@link #getFirst() first} command is joined to the input of the
 * {@link #getSecond() second} command.
 */
public class JoinedCommand<A extends Arguments<A>> implements Command<A> {

	private final Command<A> first;
	private final Command<?> second;

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
	public JoinedCommand(Command<A> first, Command<?> second) {
		if (first == null) {
			throw new NullPointerException("first command cannot be null");
		}
		if (second == null) {
			throw new NullPointerException("second command cannot be null");
		}
		this.first = first;
		this.second = second;
	}

	public static <A extends Arguments<A>> JoinedCommand<A> join(Command<A> first, Command<?> second) {
		return new JoinedCommand<A>(first, second);
	}

	/**
	 * Returns the first command of this joined command
	 * 
	 * @return the first command in the join
	 */
	public Command<A> getFirst() {
		return first;
	}

	/**
	 * Returns the second command of this joined command
	 * 
	 * @return the second command in the join
	 */
	public Command<?> getSecond() {
		return second;
	}

	/**
	 * Returns the joined command names, a string like "echo | grep"
	 * 
	 * @return the joined command names, a string like "echo | grep"
	 */
	@Override
	public String getName() {
		return getFirst().getName() + " | " + getSecond().getName();
	}

	@Override
	public A getArguments(VariableContext context) {
		return getFirst().getArguments(context);
	}

	@Override
	public Command<?> join(Command<?> next) {
		return JoinedCommand.join(getFirst(), getSecond().join(next));
	}

	/**
	 * Executes this joined command redirecting the output of the first command
	 * such that it becomes the input of the second command.
	 * <p>
	 * The specified {@code output} object is passed to the second command's
	 * {@code execute(..)} method. The {@link LineProcessor} returned by the
	 * second command is passed to the {@code execute(..)} method of the first
	 * command, with the effect that the first command provides its output to
	 * the second command as input.
	 * 
	 * @param context
	 *            context object providing access to the current directory,
	 *            environment variables and other information useful for the
	 *            commands during their execution
	 * @param output
	 *            the output for the second command
	 */
	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		return getFirst().execute(context, getSecond().execute(context, output));
	}

	/**
	 * Returns the string representation of this joined command, consisting of
	 * first and second command including arguments and options.
	 * <p>
	 * An example string returned by a joined command is
	 * 
	 * <pre>
	 * &quot;echo -messages [Hello WORLD] | grep -matchString world -ignoreCase&quot;
	 * </pre>
	 * 
	 * @return the string representation of this joined command, such as
	 * 
	 *         <pre>
	 * &quot;echo -messages [Hello WORLD] | grep -matchString world -ignoreCase&quot;
	 * </pre>
	 */
	@Override
	public String toString() {
		return first + " | " + second;
	}

}
