package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.io.InputOutputJoin;
import org.unix4j.io.Output;

/**
 * A composite command joining two commands. The output of the
 * {@link #getFirst() first} command is joined to the input of the
 * {@link #getSecond() second} command.
 */
public class JoinedCommand<A extends Arguments<A>, L1, L2> implements Command<A, JoinedCommand.Local<L1,L2>> {
	
	public static class Local<L1, L2> {
		private ExecutionContext<L1> context1;
		private ExecutionContext<L2> context2;
	}

	private final Command<A, L1> first;
	private final Command<?, L2> second;

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
	public JoinedCommand(Command<A, L1> first, Command<?, L2> second) {
		if (first == null) {
			throw new NullPointerException("first command cannot be null");
		}
		if (second == null) {
			throw new NullPointerException("second command cannot be null");
		}
		this.first = first;
		this.second = second;
	}

	public static <A extends Arguments<A>, L1, L2> JoinedCommand<A, L1, L2> join(Command<A, L1> first, Command<?, L2> second) {
		return new JoinedCommand<A, L1, L2>(first, second);
	}

	/**
	 * Returns the first command of this joined command
	 * 
	 * @return the first command in the join
	 */
	public Command<A, L1> getFirst() {
		return first;
	}

	/**
	 * Returns the second command of this joined command
	 * 
	 * @return the second command in the join
	 */
	public Command<?, L2> getSecond() {
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

	@Override
	public A getArguments() {
		return getArguments();
	}

	@Override
	public JoinedCommand<A, L1, L2> withArgs(A arguments) {
		return new JoinedCommand<A, L1, L2>(first.withArgs(arguments), second);
	}
	
	@Override
	public Local<L1, L2> initializeLocal() {
		return new Local<L1, L2>();
	}

	@Override
	public <L3> Command<?, ?> join(Command<?, L3> next) {
		return JoinedCommand.join(getFirst(), getSecond().join(next));
	}

	/**
	 * Executes this joined command. The first command reads from the given
	 * {@code input} and writes to a new {@link JoinedOutput} instance buffering
	 * the lines if necessary. The joined output object triggers the execution
	 * of the second command. It depends on the type of the second command
	 * whether it is called immediately for every line written by the first
	 * command or only once at the end when the first command calls
	 * {@link JoinedOutput#finish() finish()}.
	 * 
	 * @param input
	 *            the input for the first command
	 * @param output
	 *            the output for the second command
	 */
	@Override
	public boolean execute(ExecutionContext<Local<L1,L2>> context, Input input, Output output) {
		final Local<L1,L2> local = context.getLocal();
		if (context.isInitial()) {
			local.context1 = DefaultExecutionContext.deriveForNextCommand(context, getFirst(), context.isTerminal());
		}
		final InputOutputJoin join = new InputOutputJoin();
		final boolean terminate1 = !getFirst().execute(local.context1, input, join.getOutput()) || context.isTerminal();
		if (terminate1 || !join.isEmpty()) {
			if (local.context2 == null) {
				local.context2 = DefaultExecutionContext.deriveForNextCommand(local.context1, getSecond(), terminate1);
			}
			final boolean terminate2 = getSecond().execute(local.context2, join.getInput(), output);
			return terminate1 || terminate2;
		}
		return terminate1;
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
