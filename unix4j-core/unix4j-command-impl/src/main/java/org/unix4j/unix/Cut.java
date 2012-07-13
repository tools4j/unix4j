package org.unix4j.unix;

import static org.unix4j.util.Assert.assertArgNotNull;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;

/**
 * Non-instantiable module with inner types making up the cut command.
 */
public final class Cut {

	/**
	 * The "cut" command name.
	 */
	public static final String NAME = "cut";

	/**
	 * Interface defining all method signatures for the cut command.
	 *
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Splits the line into words using whitespace as delimiter, cuts the
		 * <code>i<sup>th</sup></code> word and writes it to the output, where
		 * {@code i=fieldIndex}.
		 *
		 * @param fieldIndex
		 *            the one-based index of the word to cut out
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
		R cut(int fieldIndex);

		/**
		 * Splits the line into words using the given delimiter, cuts every
		 * <code>i<sup>th</sup></code> word for every {@code i} in
		 * {@code fieldIndices} and writes the cut words to the output using a
		 * single tab as delimiter.
		 *
		 * @param delimiter
		 *            the delimiter separating the words on the the input line
		 * @param fieldIndices
		 *            the one-based indices of the words to cut out
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
		R cut(String delimiter, int... fieldIndices);

		/**
		 * Splits the line into words using the given input delimiter, cuts
		 * every <code>i<sup>th</sup></code> word for every {@code i} in
		 * {@code fieldIndices} and writes the cut words to the output using the
		 * specified output delimiter.
		 *
		 * @param inputDelimiter
		 *            the delimiter separating the words on the the input line
		 * @param outputDelimiter
		 *            the delimiter used to separate the words on the output
		 *            line
		 * @param fieldIndices
		 *            the zero-based indices of the words to cut out
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
		R cut(String inputDelimiter, String outputDelimiter, int... fieldIndices);

		/**
		 * Cuts {@code n=length} characters from the input line starting from
		 * {@code start} and writes them to the output.
		 *
		 * @param start
		 *            the one-based start index for the first character in the
		 *            cut
		 * @param length
		 *            the number of characters in the cut
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
		R cut(int start, int length);

		/**
		 * Cuts the characters {@code charIndices[0]}, {@code charIndices[1]},
		 * etc. from the input line and writes them to the output.
		 *
		 * @param charIndices
		 *            the one-based indices of the characters added to the cut
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
		R cut(int[] charIndices);
	}

	/**
	 * Option flags for the cut command.
	 */
	public static enum Option {
		// no options?
	}

	/**
	 * Arguments and options for the cut command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public enum Type {
			Fields, Range, Chars;
		}

		public static final TypedMap.Key<int[]> FIELDS = TypedMap.DefaultKey.keyFor("fields", int[].class);
		public static final TypedMap.Key<String> INPUT_DELIMITER = TypedMap.DefaultKey.keyFor("inputDelimiter", String.class);
		public static final TypedMap.Key<String> OUTPUT_DELIMITER = TypedMap.DefaultKey.keyFor("outputDelimiter", String.class);
		public static final TypedMap.Key<int[]> RANGE = TypedMap.DefaultKey.keyFor("range", int[].class);
		public static final TypedMap.Key<int[]> CHARS = TypedMap.DefaultKey.keyFor("chars", int[].class);

		private final Type type;

		public Args(String inputDelimiter, String outputDelimiter, int... fields) {
			super(Option.class);
			assertArgNotNull("fields cannot be null", fields);
			this.type = Type.Fields;
			setArg(INPUT_DELIMITER, inputDelimiter == null ? "\\s+" : inputDelimiter);
			setArg(OUTPUT_DELIMITER, inputDelimiter == null ? " " : outputDelimiter);
			setArg(FIELDS, fields);
		}

		public Args(int start, int length) {
			super(Option.class);
			this.type = Type.Range;
			setArg(RANGE, new int[] { start, length });
		}

		public Args(int[] charIndices) {
			super(Option.class);
			assertArgNotNull("charIndices cannot be null", charIndices);
			this.type = Type.Chars;
			setArg(CHARS, charIndices);
		}

		public Type getType() {
			return type;
		}

		public int[] getFields() {
			return getArg(FIELDS);
		}

		public String getInputDelimiter() {
			return getArg(INPUT_DELIMITER);
		}

		public String getOutputDelimiter() {
			return getArg(OUTPUT_DELIMITER);
		}

		public int getRangeStart() {
			return getArg(RANGE)[0];
		}

		public int getRangeLength() {
			return getArg(RANGE)[1];
		}

		public int[] getChars() {
			return getArg(CHARS);
		}
	}

	/**
	 * Singleton {@link Factory} for the cut command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command cut(int fieldIndex) {
			return new Command(new Args(null, null, fieldIndex));
		}

		@Override
		public Command cut(String delimiter, int... fieldIndices) {
			return new Command(new Args(delimiter, null, fieldIndices));
		}

		@Override
		public Command cut(String inputDelimiter, String outputDelimiter, int... fieldIndices) {
			return new Command(new Args(inputDelimiter, outputDelimiter, fieldIndices));
		}

		@Override
		public Command cut(int start, int length) {
			return new Command(new Args(start, length));
		}

		@Override
		public Command cut(int[] charIndices) {
			return new Command(new Args(charIndices));
		}
	};

	/**
	 * Cut command implementation.
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
			switch (getArguments().getType()) {
			case Fields:
				cutByFields(input, output);
				break;
			case Range:
				cutByRange(input, output);
				break;
			case Chars:
				cutByChars(input, output);
				break;
			default:
				throw new IllegalArgumentException("unknown type: " + getArguments().getType());
			}
		}

		private void cutByFields(Input input, Output output) {
			final Args args = getArguments();
			final String inputDelim = args.getInputDelimiter();
			final String outputDelim = args.getOutputDelimiter();
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				final String[] splitLine = line.split(inputDelim);
				final StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (final int field : args.getFields()) {
					if (first) {
						first = false;
					} else {
						sb.append(outputDelim);
					}
					if (splitLine.length >= field && field > 0) {
						sb.append(splitLine[field - 1]);
					}
				}
				output.writeLine(sb.toString());
				sb.setLength(0);
			}
		}

		private void cutByRange(Input input, Output output) {
			final Args args = getArguments();
			final int start = Math.max(0, args.getRangeStart() - 1);
			final int end = start + args.getRangeLength();
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				if (start < line.length()) {
					output.writeLine(line.substring(start, Math.min(end, line.length())));
				} else {
					output.writeLine("");
				}
			}
		}

		private void cutByChars(Input input, Output output) {
			final Args args = getArguments();
			final StringBuilder sb = new StringBuilder();
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				for (final int charNum : args.getChars()) {
					if (charNum > 0 && charNum <= line.length()) {
						sb.append(line.charAt(charNum - 1));
					}
				}
				output.writeLine(sb.toString());
				sb.setLength(0);
			}
		}
	}

	// no instances
	private Cut() {
		super();
	}
}
