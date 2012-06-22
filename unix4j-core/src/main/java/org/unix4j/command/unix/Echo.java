package org.unix4j.command.unix;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;

/**
 * Non-instantiable module with inner types making up the echo command.
 */
public final class Echo {
	/**
	 * The "echo" command name.
	 */
	public static final String NAME = "echo";

	/**
	 * Interface defining all method signatures for the echo command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command builder providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Echos the given input argument to the output.
		 * 
		 * @param message
		 *            the message to echo
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R echo(String message);

		/**
		 * Echos the given input arguments to the output. The input arguments
		 * are separated with a single space character.
		 * 
		 * @param messages
		 *            the messages to echo
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R echo(String... messages);
	}

	/**
	 * Option flags for the echo command.
	 */
	public static enum Option {
		// no options?
	}

	/**
	 * Arguments and options for the echo command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<List<String>> MESSAGES = TypedMap.DefaultKey.keyForListOf("messages", String.class);

		public Args(String message) {
			this(Collections.singletonList(message));
		}

		public Args(String... messages) {
			this(Arrays.asList(messages));
		}

		public Args(List<String> messages) {
			super(Option.class);
			setArg(MESSAGES, messages);
		}

		public List<String> getMessages() {
			return getArg(MESSAGES);
		}
	}

	/**
	 * Singleton {@link Factory} for the echo command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command echo(String message) {
			return new Command(new Args(message));
		}

		@Override
		public Command echo(String... messages) {
			return new Command(new Args(messages));
		}
	};

	/**
	 * Echo command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.NoInput, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		@Override
		public void executeBatch(Input input, Output output) {
			final List<String> messages = getArguments().getMessages();
			final StringBuilder sb = new StringBuilder();
			if (!messages.isEmpty()) {
				sb.append(messages.get(0));
				for (int i = 1; i < messages.size(); i++) {
					sb.append(' ').append(messages.get(i));
				}
			}
			output.writeLine(sb.toString());
		}
	}

	// no instances
	private Echo() {
		super();
	}
}
