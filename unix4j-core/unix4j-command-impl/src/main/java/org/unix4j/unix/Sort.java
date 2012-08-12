package org.unix4j.unix;

import static org.unix4j.util.Assert.assertArgFalse;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Output;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

/**
 * Non-instantiable module with inner types making up the sort command.
 */
public final class Sort {

	/**
	 * The "sort" command name.
	 */
	public static final String NAME = "sort";

	/**
	 * Interface defining all method signatures for the sort command.
	 *
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Sorts the input in ascending order. Equivalent to
		 * <tt>sort(Sort.Option.ascending}</tt>.
		 *
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
		R sort();

		/**
		 * Sorts the input in ascending order. Equivalent to
		 * <tt>sort(Sort.Option.ascending}</tt>.
		 *
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
		R sortAscending();


		/**
		 * Sorts the input in descending order. Equivalent to
		 * <tt>sort(Sort.Option.descending}</tt>.
		 *
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
		R sortDescending();

		/**
		 * Sorts the input using the specified sort {@link Option option}.
		 *
		 * @param options
		 *            the sort options
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
		R sort(Option... options);
	}

	/**
	 * Option flags for the sort command.
	 */
	public static enum Option implements org.unix4j.optset.Option<Option> {
		//TODO the real options are different, e.g. 'reverse' for descending
		/**
		 * Sort in ascending order (the default).
		 */
		ascending('a'),
		/**
		 * Sort in descending order.
		 */
		descending('d');
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
	 * Arguments and options for the sort command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public Args() {
			super(Option.class);
		}

		public Args(Option... options) {
			this();
			setOpts(options);
		}
	}

	/**
	 * Singleton {@link Factory} for the sort command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command sort() {
			return new Command(new Args());
		}

		@Override
		public Command sortAscending() {
			return sort();
		}

		@Override
		public Command sortDescending() {
			return new Command(new Args(Option.descending));
		}

		@Override
		public Command sort(Option... options) {
			return new Command(new Args(options));
		}
	};

	/**
	 * Sort command implementation.
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
			final boolean isAsc = getArguments().hasOpt(Option.ascending);
			final boolean isDesc = getArguments().hasOpt(Option.descending);
			assertArgFalse("Options " + Option.ascending + " and " + Option.descending + " cannot be specified at the same time", (isAsc && isDesc));
			return new LineProcessor() {
				private final List<Line> lines = new ArrayList<Line>();//array list is good for sorting;
				@Override
				public boolean processLine(Line line) {
					lines.add(line);
					return true;//we want it all!
				}
				@Override
				public void finish() {
					outputSorted(lines, output, isDesc);
					output.finish();
				}
			};
		}

		private static final Comparator<Line> COMPARATOR = new Comparator<Line>() {
			private final Collator collator = Collator.getInstance();
			@Override
			public int compare(Line o1, Line o2) {
				return collator.compare(o1.getContent(), o2.getContent());
			}
		};
		private void outputSorted(List<Line> lines, LineProcessor output, boolean isDesc) {
			Collections.sort(lines, COMPARATOR);
			if (isDesc) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					if (!output.processLine(lines.get(i))) {
						return;
					}
				}
			} else {
				for (int i = 0; i < lines.size(); i++) {
					if (!output.processLine(lines.get(i))) {
						return;
					}
				}
			}
		}
	}

	// no instances
	private Sort() {
		super();
	}
}
