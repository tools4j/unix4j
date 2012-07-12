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
 */
abstract public class AbstractCommand<A extends Arguments<A>> implements Command<A> {
	private final String name;
	private final Type type;
	private final A arguments;

	/**
	 * Constructor with command name, type and arguments.
	 * 
	 * @param name
	 *            the name of the command, usually a lower case string such as
	 *            "ls" or "grep"
	 * @param type
	 *            the command type defining how this command needs to be
	 *            processed, for instance line by line or requiring the complete
	 *            input as a prerequisite for execution
	 * 
	 * @param arguments
	 *            the command specific arguments for the new command instance
	 */
	public AbstractCommand(String name, Type type, A arguments) {
		this.name = name;
		this.type = type;
		this.arguments = arguments;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public A getArguments() {
		return arguments;
	}

	@Override
	public Command<?> join(Command<?> next) {
		return JoinedCommand.join(this, next);
	}

	/**
	 * Executes this command reading from the given {@code input} and writing to
	 * the {@code output} argument. The actual command execution is forwarded to
	 * {@link #executeBatch(Input, Output)}.
	 * <p>
	 * For command {@link #getType() type} of this command,
	 * {@code executeBatch(..)} is called once or multiple times.
	 * <p>
	 * It depends on the concrete command implementation whether or not
	 * {@code input} and {@code output} are actually used. Some commands may not
	 * read from the input or not produce any output, such as the {@link Echo
	 * echo} command which usually does not read from any input.
	 * <p>
	 * Note that the {@link #getType() type} defines how a command processes its
	 * input. It is a contractual obligation for a command to process its input
	 * in the way defined by the {@link Type} constant. In particular,
	 * {@link Type#LineByLine LineByLine} and {@link Type#CompleteInput
	 * CompleteInput} commands should always read all input lines provided to
	 * the command. A program can end up in an infinite loop if a command fails
	 * to comply with the contractual obligation implied by the return value of
	 * {@link #getType()}.
	 * 
	 * @param input
	 *            the input for the command
	 * @param output
	 *            the output to write to
	 */
	@Override
	public final void execute(Input input, Output output) {
		switch (getType()) {
		case NoInput:
			executeBatch(input, output);
			break;
		case LineByLine:
			do {
				executeBatch(input, output);
			} while (input.hasMoreLines());
			break;
		case CompleteInput:
			executeBatch(input, output);
			if (input.hasMoreLines()) {
				throw new IllegalStateException("Command " + getName() + " has type " + Type.CompleteInput.name() + ", hence it should read the complete input");
			}
			break;
		default:
			throw new IllegalStateException("unknown command type: " + getType());
		}
		output.finish();
	}

	/**
	 * Executes a single batch of the command. For {@link Type#NoInput NoInput}
	 * and {@link Type#CompleteInput CompleteInput} commands, this is equivalent
	 * to the complete and single execution. For {@link Type#LineByLine
	 * LineByLine} commands, this method may be called once or multiple times
	 * depending on the nature of the input and of potential previous commands.
	 * <p>
	 * Note that it is a requirement for a {@link Type#CompleteInput
	 * CompleteInput} to read the complete input; a {@link Type#LineByLine
	 * LineByLine} command is required to read and process at least one line
	 * from the input. Commands of type {@link Type#NoInput NoInput} do not
	 * require to read any input.
	 * 
	 * @param input
	 *            the input for the command
	 * @param output
	 *            the output to write to
	 */
	abstract protected void executeBatch(Input input, Output output);

	@Override
	public String toString() {
		final String args = getArguments().toString();
		return args.isEmpty() ? getName() : getName() + " " + args;
	}
}
