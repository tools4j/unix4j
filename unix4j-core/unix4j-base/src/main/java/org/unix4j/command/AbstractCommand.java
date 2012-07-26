package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.io.Output;

/**
 * Abstract base class suitable for most command implementations. Name, type and
 * arguments are passed to the constructor.
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
abstract public class AbstractCommand<A extends Arguments<A>, L> implements Command<A, L> {

	private final String name;
	private final A arguments;

	/**
	 * Constructor with command name, type and arguments.
	 * 
	 * @param name
	 *            the name of the command, usually a lower case string such as
	 *            "ls" or "grep"
	 * @param arguments
	 *            the command specific arguments for the new command instance
	 */
	public AbstractCommand(String name, A arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public A getArguments() {
		return arguments;
	}

	@Override
	public <L2> Command<?, ?> join(Command<?, L2> next) {
		return JoinedCommand.join(this, next);
	}

	@Override
	public String toString() {
		final String args = getArguments().toString();
		return args.isEmpty() ? getName() : getName() + " " + args;
	}
}
