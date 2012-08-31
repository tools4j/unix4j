package org.unix4j.unix;

import static org.unix4j.util.Assert.assertArgFalse;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.Output;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.line.SimpleLine;
import org.unix4j.util.Counter;

/**
 * <b>NAME</b>
 * <p>
 * wc - word, line, and byte or character count
 * <p>
 * <b>SYNOPSIS</b>
 *
 * <pre>
 * wc[-lwm]
 * </pre>
 *
 * <b>DESCRIPTION</b>
 * <p>
 * The wc utility reads one or more input files and, by default, writes the
 * number of lines, words, and bytes contained in each input file to the
 * standard output. The utility also writes a total count for all named files,
 * if more than one input file is specified. The wc utility considers a word to
 * be a non-zero-length string of characters delimited by white space.
 * </p>
 * <b>NOTES</b>
 * <p>
 * <ul>
 * <li>Count by byte is currently not supported.</li>
 * <li>Line ending characters are currently NOT included in count by char.</li>
 * </ul>
 * </p>
 * <b>OPTIONS</b>
 * <p>
 * The following options are supported:
 * <pre>
 * -l	 --lines	Writes to the standard output the number of <newline>s in each input file.
 * -m	 --chars	Writes to the standard output the number of characters in each input file.
 * -w	 --words	Writes to the standard output the number of words in each input file.
 * </pre>
 * <p>
 * When any option is specified, wc reports only the information requested by
 * the specified options.
 * <p>
 * If only one count type is requested, the count is outputted as an integer. If
 * more than one count is requested, a fixed width formatting is used, with the
 * counts being right aligned. The width of each field is equal to the width of
 * the widest field (count) plus two characters.
 */
public final class Wc {

	/**
	 * The "wc" command name.
	 */
	public static final String NAME = "wc";

	/**
	 * Interface defining all method signatures for the wc command.
	 *
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Executes a count of lines, words and chars in the given input and
		 * writes them to the output
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command
		 *         {@link org.unix4j.unix.ls.LsFactory} to return a new
		 *         {@link org.unix4j.command.Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R wc();

		/**
		 * Executes a count of lines, words and chars in the given input and
		 * writes them to the output
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command
		 *         {@link org.unix4j.unix.ls.LsFactory} to return a new
		 *         {@link org.unix4j.command.Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R wcCountLinesWordsAndChars();

		/**
		 * Executes a one or more counts, depending on the given options, and
		 * writes them to the output
		 *
		 * @param options
		 *            the options defining the counts to execute.
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command
		 *         {@link org.unix4j.unix.ls.LsFactory} to return a new
		 *         {@link org.unix4j.command.Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R wc(Option... options);

		/**
		 * Executes a count of lines and writes this count to the output.
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command
		 *         {@link org.unix4j.unix.ls.LsFactory} to return a new
		 *         {@link org.unix4j.command.Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R wcCountLines();

		/**
		 * Executes a count of chars and writes this count to the output.
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command
		 *         {@link org.unix4j.unix.ls.LsFactory} to return a new
		 *         {@link org.unix4j.command.Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R wcCountChars();

		/**
		 * Executes a count of words and writes this count to the output.
		 *
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command
		 *         {@link org.unix4j.unix.ls.LsFactory} to return a new
		 *         {@link org.unix4j.command.Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R wcCountWords();
	}

	/**
	 * Option flags for the wc command.
	 */
	public static enum Option implements org.unix4j.option.Option {
		lines('l'), chars('m'), words('w');
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
	 * Arguments and options for the wc command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {

		public Args(Option... options) {
			super(Option.class);
			setOpts(options);
		}

		public boolean isCountChars() {
			return hasOpt(Option.chars);
		}

		public boolean isCountLines() {
			return hasOpt(Option.lines);
		}

		public boolean isCountWords() {
			return hasOpt(Option.words);
		}

		public boolean isNoCountTypeSpecified() {
			return !(isCountChars() || isCountLines() || isCountWords());
		}
	}

	/**
	 * Singleton {@link Factory} for the wc command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {

		@Override
		public Command wc() {
			return wcCountLinesWordsAndChars();
		}

		@Override
		public Command wcCountLinesWordsAndChars() {
			return new Command(new Args(Option.lines, Option.words, Option.chars));
		}

		@Override
		public Command wc(Option... options) {
			if (options.length == 0) {
				return wc();
			} else {
				return new Command(new Args(options));
			}
		}

		@Override
		public Command wcCountLines() {
			return new Command(new Args(Option.lines));
		}

		@Override
		public Command wcCountChars() {
			return new Command(new Args(Option.chars));
		}

		@Override
		public Command wcCountWords() {
			return new Command(new Args(Option.words));
		}
	};

	/**
	 * wc command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		private final static int MIN_COUNT_PADDING = 2;

		public Command(Args arguments) {
			super(NAME, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		@Override
		public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
			final Args args = getArguments();
			assertArgFalse("No count type specified. At least one count type required.", args.isNoCountTypeSpecified());
			return new LineProcessor() {
				private final Counter lineCounter = new Counter();
				private final Counter wordCounter = args.isCountWords() ? new Counter() : null;
				private final Counter charCounter = new Counter();
				@Override
				public boolean processLine(Line line) {
					lineCounter.increment();
					if (wordCounter != null) {
						wordCounter.increment(wordCount(line));
					}
					charCounter.increment(line.length());
					return true;//we want more lines
				}
				
				@Override
				public void finish() {
					if (lineCounter.getCount() == 1 && charCounter.getCount() == 0) {
						lineCounter.reset();
					}
		
					final List<Long> counts = new ArrayList<Long>();
					if (args.isCountLines())
						counts.add(lineCounter.getCount());
					if (args.isCountWords())
						counts.add(wordCounter.getCount());
					if (args.isCountChars())
						counts.add(charCounter.getCount());
		
					output.processLine(formatCounts(counts));
					output.finish();
				}
			};
		}

		private int wordCount(Line line) {
			final String[] words = line.getContent().split("\\s+");
			int wordCount = 0;
			for (final String word : words) {
				if (word.length() > 0) {
					wordCount += 1;
				}
			}
			return wordCount;
		}

		private Line formatCounts(List<Long> counts) {
			final StringBuilder format = new StringBuilder();

			if (counts.size() > 1) {
				int widestCount = getWidestCount(counts);
				int fixedWidth = widestCount + MIN_COUNT_PADDING;
				for (int i = 0; i < counts.size(); i++) {
					format.append("%").append(fixedWidth).append("d");
				}
			} else {
				format.append("%d");
			}

			final Formatter formatter = new Formatter().format(format.toString(), counts.toArray());
			return new SimpleLine(formatter.toString());
		}

		private int getWidestCount(List<Long> counts) {
			int widestCount = 0;
			for (final long count : counts) {
				final int width = String.valueOf(count).length();
				if (width > widestCount) {
					widestCount = width;
				}
			}
			return widestCount;
		}
	}

	// no instances
	private Wc() {
		super();
	}
}
