package org.unix4j.command;


/**
 * Abstract base class suitable for most command implementations. Name and
 * arguments are passed to the constructor.
 * 
 * @param <A>
 *            the type parameter defining the arguments and options of the
 *            command
 */
abstract public class AbstractCommand<A extends Arguments<A>> implements Command<A> {

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
	public Command<?> join(Command<?> next) {
		return JoinedCommand.join(this, next);
	}
	
	@Override
	public String toString() {
		final String args = getArguments().toString();
		return args.isEmpty() ? getName() : getName() + " " + args;
	}
}
