package org.unix4j.command.unix;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;

/**
 * Non-instantiable module with inner types making up the grep command.
 */
public final class Grep {

	/**
	 * The "grep" command name.
	 */
	public static final String NAME = "grep";

	/**
	 * Interface defining all method signatures for the grep command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command builder providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Filters the input lines and writes the matching lines to the output.
		 * A line matches if it contains the given {@code matchString} using
		 * case-sensitive string comparison.
		 * 
		 * @param matchString
		 *            the string to be matched by the lines
		 * @return the generic return type {@link R}, usually a new command
		 *         signature or a builder with methods for chained invocation of
		 *         following commands
		 */
		R grep(String matchString);

		/**
		 * Filters the input lines and writes the matching lines to the output.
		 * Whether or not a line matches the given {@code matchString} depends
		 * on the specified {@code options}.
		 * 
		 * @param matchString
		 *            the string to be matched by the lines
		 * @param options
		 *            the grep options
		 * @return the generic return type {@link R}, usually a new command
		 *         signature or a builder with methods for chained invocation of
		 *         following commands
		 */
		R grep(String matchString, Option... options);
	}

	/**
	 * Option flags for the grep command.
	 */
	public static enum Option {
		/**
		 * Match lines ignoring the case when comparing the strings, equivalent
		 * to option {@link #ignoreCase}.
		 */
		i, 
		/**
		 * Match lines ignoring the case when comparing the strings, equivalent
		 * to option {@link #i}.
		 */
		ignoreCase, 
		/**
		 * Invert the match result, that is, a non-matching line is writen to
		 * the output and a matching line is not. This option is equivalent to
		 * the {@link #invert} option.
		 */
		v, 
		/**
		 * Invert the match result, that is, a non-matching line is writen to
		 * the output and a matching line is not. This option is equivalent to
		 * the {@link #v} option.
		 */
		invert;
	}

	/**
	 * Arguments and options for the grep command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<String> MATCH_STRING = TypedMap.DefaultKey.keyFor("matchString", String.class);

		public Args(String matchString) {
			super(Option.class);
			if (matchString == null) {
				throw new NullPointerException("matchString cannot be null");
			}
			setArg(MATCH_STRING, matchString);
		}

		public Args(String matchString, Option... options) {
			this(matchString);
			setOpts(options);
		}

		public String getMatchString() {
			return getArg(MATCH_STRING);
		}
	}

	/**
	 * Singleton {@link Factory} for the grep command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command grep(String matchString) {
			return new Command(new Args(matchString));
		}

		@Override
		public Command grep(String matchString, Option... options) {
			return new Command(new Args(matchString, options));
		}
	};

	/**
	 * Grep command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.LineByLine, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		@Override
		public void executeBatch(Input input, Output output) {
			final Args args = getArguments();
			final String matchString = args.getMatchString();
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				boolean matches;
				if (args.hasOpt(Option.i) || args.hasOpt(Option.ignoreCase)) {
					matches = line.toLowerCase().contains(matchString.toLowerCase());
				} else {
					matches = line.contains(matchString);
				}
				if (args.hasOpt(Option.v) || args.hasOpt(Option.invert)) {
					matches = !matches;
				}
				if (matches) {
					output.writeLine(line);
				}
			}
		}
	}

	// no instances
	private Grep() {
		super();
	}
}
