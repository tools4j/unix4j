package org.unix4j.unix;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.Counter;
import org.unix4j.util.TypedMap;

import static org.unix4j.util.Assert.assertArgGreaterThanOrEqualTo;

/**
 * <b>NAME</b>
 * <p>
 * head - output the first part of an input
 * <p>
 * <b>SYNOPSIS</b>
 *
 * <pre>
 * head -lines n
 * </pre>
 *
 * <b>DESCRIPTION</b>
 * <p>
 * The head utility shall copy its input to the output, ending the output for each file at a designated point.
 * Copying shall end at the point indicated by the -n (lines) number option. The option-argument number
 * shall be counted in units of lines.
 * </p>
 * <b>NOTES</b>
 * <p>
 * <ul>
 * <li>Currently only supports input from other commands, not from a specified files.</li>
 * </ul>
 * </p>
 * <b>OPTIONS</b>
 * <p>
 * The following options are supported:
 * <pre>
 * -n	 --lines	The first number lines of each input shall be copied to output. The application shall ensure that the number option-argument is a positive decimal integer.
 * </pre>
 * <p>
 * When a file contains less than number lines, it shall be copied to standard output in its entirety.
 * This shall not be an error.
 * </p><p>
 * If no options are specified, head shall act as if -n 10 had been specified.
 * </p>
 */
public final class Head {
	private static final String NAME = "head";

	/**
	 * Interface defining all method signatures for the head command.
	 *
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * @param lines
		 *            the number of lines to send to the input.  If a negative
		 *            value is given, an IllegalArgumentException will be thrown.
		 *            If the number of lines specified is greater than the number
		 *            of lines in the input, the application will not error. All
		 *            lines of the input will be sent to the output.
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link org.unix4j.io.Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R head(int lines);

		/**
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link org.unix4j.io.Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R head();
	}

	/**
	 * Option flags for the head command.
	 */
	public static enum Option {
		// no options
	}

	/**
	 * Arguments and options for the head command.
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
	 * Singleton {@link Factory} for the head command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		private final static int DEFAULT_LINES = 10;

		@Override
		public Command head(int lines) {
			return new Command(new Args(lines));
		}

		@Override
		public Command head() {
			return new Command(new Args(DEFAULT_LINES));
		}
	}

	/**
	 * Head command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		private static final TypedMap.Key<Counter> COUNTER_KEY = TypedMap.keyFor("counter", Counter.class);
		public Command(Args arguments) {
			super(NAME, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		private Counter getCounter(ExecutionContext context) {
			Counter counter = context.getCommandStorage().get(COUNTER_KEY);
			if (counter == null) {
				counter = new Counter();
				context.getCommandStorage().put(COUNTER_KEY, counter);
			}
			return counter;
		}
		@Override
		public boolean execute(ExecutionContext context, Input input, Output output) {
			final int linesToOutput = getArguments().getLines();
			final Counter counter = getCounter(context);
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				if (counter.getCount() < linesToOutput) {
					output.writeLine(line);
					counter.increment();
				} else {
					return false;
				}
			}
			return counter.getCount() < linesToOutput;
		}
	}

	// no instances
	private Head() {
		super();
	}
}
