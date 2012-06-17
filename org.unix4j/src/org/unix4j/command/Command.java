package org.unix4j.command;

import org.unix4j.command.unix.Echo;
import org.unix4j.command.unix.Xargs;
import org.unix4j.io.Input;
import org.unix4j.io.Output;

/**
 * A command is an executable unit defined by its {@link #getName() name} and
 * {@link #getArguments() arguments}. A command can be
 * {@link #execute(Input, Output) executed} providing and {@link Input} and
 * {@link Output} object. The {@link #getType() type} of the command specifies
 * how a command is processed, for instance line by line or requiring the
 * complete input as a prerequisite.
 * <p>
 * Commands can be {@link #join(Command) joined} to another command usually
 * meaning that this command's output forms the input of the joined command. A
 * new command instance of the same type can be derived from an existing command
 * by {@link #withArgs(Arguments)}.
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
	 * Returns the command type defining how this command needs to be processed,
	 * for instance line by line or requiring the complete input as a
	 * prerequisite for execution.
	 * <p>
	 * Note that the type returned here defines a contractual obligation for the
	 * way this command processes its input, as described in more detail in
	 * {@link #execute(Input, Output)}.
	 * 
	 * @return the type defining the way this command needs to be processed
	 */
	Type getType();

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
	Command<A> withArgs(A arguments);

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
	Command<?> join(Command<?> next);

	/**
	 * Executes this command reading from the given {@code input} and writing to
	 * the {@code output} argument.
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
	 * @param output
	 */
	void execute(Input input, Output output);

	/**
	 * The command type defining how a command processes its input. The command
	 * type returned by {@link Command#getType()} defines a contractual
	 * obligation for a command. More details are available in the description
	 * of the type constants and in {@link Command#execute(Input, Output)}.
	 */
	enum Type {
		/**
		 * The command processes no input at all. There is no obligation for the
		 * command to read the input.
		 */
		NoInput,
		/**
		 * The command processes its input line by line. It is, however, a
		 * contractual obligation for the command to read the complete input
		 * passed to the execute method.
		 * <p>
		 * The command can process the input line-by-line and write to the
		 * output when processing a single line, but it is a requirement that
		 * all available input lines have been read when returning from
		 * {@link Command#execute(Input, Output)}
		 */
		LineByLine,
		/**
		 * The command processes its input as a whole. It is at the same time a
		 * contractual obligation for the command to read the complete input
		 * passed to the {@link Command#execute(Input, Output) execute} method.
		 */
		CompleteInput;

		/**
		 * Convenience method returning true if this constant equals
		 * {@link #LineByLine}.
		 * 
		 * @return true if {@code this==LineByLine}
		 */
		public boolean isLineByLine() {
			return LineByLine.equals(this);
		}
	}
}
