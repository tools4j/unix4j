package org.unix4j.unix;

import static org.unix4j.util.Assert.assertArgGreaterThanOrEqualTo;

import java.util.LinkedList;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.util.TypedMap;

/**
 * <b>NAME</b>
 * <p>
 * tail - output the last part of an input
 * <p>
 * <b>SYNOPSIS</b>
 * 
 * <pre>
 * tail -lines=n
 * </pre>
 * 
 * <b>DESCRIPTION</b>
 * <p>
 * The tail utility shall copy its input to the output beginning at a designated
 * place. Copying shall begin at the point in the file indicated by the lines
 * argument. The last n number of lines shall be returned. If the number given
 * with the lines argument is larger than the number
 * </p>
 * <b>NOTES</b>
 * <p>
 * <ul>
 * <li>Currently only supports input from other commands, not from a specified
 * files.</li>
 * </ul>
 * </p>
 * <b>OPTIONS</b>
 * <p>
 * The following options are supported:
 * 
 * <pre>
 * -n	 --lines	The last number lines of each input shall be copied to output. The application shall ensure that the number option-argument is a positive decimal integer.
 * </pre>
 * <p>
 * When a file contains less than number lines, it shall be copied to standard
 * output in its entirety. This shall not be an error.
 * </p>
 * <p>
 * If no options are specified, tail shall act as if -n 10 had been specified.
 * </p>
 */
public final class Tail {
	private static final String NAME = "tail";

	/**
	 * Interface defining all method signatures for the tail command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * @param lines
		 *            the number of lines from the end of the input which will
		 *            be sent to the output. If a negative value is given, an
		 *            IllegalArgumentException will be thrown. If the number of
		 *            lines specified is greater than the number of lines in the
		 *            input, the application will not error. All lines of the
		 *            input will be sent to the output.
		 * 
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link org.unix4j.io.Output} object. This
		 *         serves implementing classes like the command {@link Factory}
		 *         to return a new {@link Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R tail(int lines);

		/**
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link org.unix4j.io.Output} object. This
		 *         serves implementing classes like the command {@link Factory}
		 *         to return a new {@link Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R tail();
	}

	/**
	 * Option flags for the tail command.
	 */
	public static enum Option implements org.unix4j.optset.Option {
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
	 * Arguments and options for the tail command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<Integer> LINES = TypedMap.keyFor("lines", Integer.class);

		public Args(int lines) {
			super(Option.class);
			assertArgGreaterThanOrEqualTo("Lines must not be negative.", lines, 0);
			setArg(LINES, lines);
		}

		public int getLines() {
			return getArg(LINES);
		}
	}

	/**
	 * Singleton {@link Factory} for the tail command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		private final static int DEFAULT_LINES = 10;

		@Override
		public Command tail(int lines) {
			return new Command(new Args(lines));
		}

		@Override
		public Command tail() {
			return new Command(new Args(DEFAULT_LINES));
		}
	}

	/**
	 * Tail command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		@Override
		public LineProcessor execute(final LineProcessor output) {
			return new LineProcessor() {
				final int linesToOutput = getArguments().getLines();
				private final LinkedList<Line> tailLines = new LinkedList<Line>();// linked
																					// list
																					// is
																					// most
																					// efficient
																					// to
																					// remove
																					// first
																					// line

				@Override
				public boolean processLine(Line line) {
					tailLines.add(line);
					if (tailLines.size() > linesToOutput) {
						tailLines.removeFirst();
					}
					return true;
				}

				@Override
				public void finish() {
					boolean more = true;
					while (more && !tailLines.isEmpty()) {
						more = output.processLine(tailLines.removeFirst());
					}
					output.finish();
				}
			};
		}
	}

	// no instances
	private Tail() {
		super();
	}
}
