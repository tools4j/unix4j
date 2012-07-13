package org.unix4j.unix;

import java.util.regex.Pattern;

import static org.unix4j.util.Assert.assertArgNotNull;

import org.unix4j.builder.CommandBuilder;
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
	 *            new command instance or a command fromFile providing methods
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
		invert,
		/**
		 * Uses fixed-strings matching instead of regular expressions. This
		 * option is equivalent to the {@link #fixedStrings} option.
		 */
		f,
		/**
		 * Uses fixed-strings matching instead of regular expressions. This
		 * option is equivalent to the {@link #f} option.
		 */
		fixedStrings;
	}

	/**
	 * Arguments and options for the grep command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<String> MATCH_STRING = TypedMap.DefaultKey.keyFor("matchString", String.class);

		public Args(String matchString) {
			super(Option.class);
			assertArgNotNull("matchString cannot be null", matchString);
			setArg(MATCH_STRING, matchString);
		}

		public Args(String matchString, Option... options) {
			this(matchString);
			setOpts(options);
		}

		public String getMatchString() {
			return getArg(MATCH_STRING);
		}

		public boolean isIgnoreCase() {
			return hasOpt(Option.i) || hasOpt(Option.ignoreCase);
		}

		public boolean isFixedStrings() {
			return hasOpt(Option.f) || hasOpt(Option.fixedStrings);
		}

		public boolean isInvert() {
			return hasOpt(Option.v) || hasOpt(Option.invert);
		}

		public String getRegexToRun() {
			return ".*" + getMatchString() + ".*";
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
			if (getArguments().isFixedStrings()) {
				grepWithFixedStrings(input, output);
			} else {
				grepWithRegularExpression(input, output);
			}
		}

		private void grepWithRegularExpression(Input input, Output output) {
			final Args args = getArguments();
			final boolean invert = args.isInvert();
			final String regex = getArguments().getRegexToRun();
			final Pattern pattern = Pattern.compile(regex, args.isIgnoreCase() ? Pattern.CASE_INSENSITIVE : 0);
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				final boolean matches = pattern.matcher(line).matches();
				if (invert ^ matches) {
					output.writeLine(line);
				}
			}
		}

		private void grepWithFixedStrings(Input input, Output output) {
			final Args args = getArguments();
			final boolean ignoreCase = args.isIgnoreCase();
			final boolean invert = args.isInvert();
			String matchString = getArguments().getMatchString();
			if (ignoreCase) {
				matchString = matchString.toLowerCase();
			}
			while (input.hasMoreLines()) {
				String line = input.readLine();
				if (ignoreCase) {
					line = line.toLowerCase();
				}
				final boolean matches = line.contains(matchString);
				if (invert ^ matches) {
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
