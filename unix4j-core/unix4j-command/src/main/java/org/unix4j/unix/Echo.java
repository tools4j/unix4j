package org.unix4j.unix;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.Output;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.util.StringUtil;
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
	 *            new command instance or a command fromFile providing methods
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
	public static enum Option implements org.unix4j.option.Option {
		// no options?
		;
		private final char acronym;

		private Option(char acronym) {
			this.acronym = acronym;
		}

		@Override
		public char acronym() {
			return acronym;
		}
	}

	/**
	 * Arguments and options for the echo command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<List<String>> MESSAGES = TypedMap.keyForListOf("messages", String.class);

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
			super(NAME, arguments);
		}

		@Override
		public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
			return new LineProcessor() {
				@Override
				public boolean processLine(Line line) {
					return false;// we want no input
				}

				@Override
				public void finish() {
					final String messages = joinMessages();
					final List<Line> lines = StringUtil.splitLines(messages);
					boolean more = true;
					for (int i = 0; more && i < lines.size(); i++) {
						more = output.processLine(lines.get(i));
					}
					output.finish();
				}
			};
		}

		private String joinMessages() {
			final List<String> messages = getArguments().getMessages();
			final StringBuilder sb = new StringBuilder();
			if (!messages.isEmpty()) {
				sb.append(messages.get(0));
				for (int i = 1; i < messages.size(); i++) {
					sb.append(' ').append(messages.get(i));
				}
			}
			return sb.toString();
		}
	}

	// no instances
	private Echo() {
		super();
	}
}
