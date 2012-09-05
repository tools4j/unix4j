package org.unix4j.redirect;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.io.ReaderInput;
import org.unix4j.io.ResourceInput;
import org.unix4j.io.StreamInput;
import org.unix4j.io.StringInput;
import org.unix4j.io.URLInput;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.util.TypedMap;

/**
 * Non-instantiable module with inner types making up the "from" pseudo-command.
 * The "from" pseudo-command is used to redirect an input source into a command
 * or command chain.
 */
public final class From {
	/**
	 * The "from" command name.
	 */
	public static final String NAME = "from";

	/**
	 * Interface defining all method signatures for the "from" command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Uses the given string as input for the next command. If the string
		 * contains line ending codes (UNIX or DOS independent from the host
		 * operating system), the string is split into multiple lines.
		 * 
		 * @param input
		 *            the string to use as input
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
		R fromString(String input);

		/**
		 * Uses the given strings as input for the next command. Each string
		 * usually represents a single line of the input; however, if any of the
		 * strings contains line ending codes (UNIX or DOS independent it is
		 * split into from the host operating system), it is split into multiple
		 * lines.
		 * 
		 * @param input
		 *            the input lines
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
		R fromStrings(String... input);

		/**
		 * Uses the strings in the specified {@code input} collection as input
		 * lines for the next command. Each string usually represents a single
		 * line of the input; however, if any of the strings contains line
		 * ending codes (UNIX or DOS independent it is split into from the host
		 * operating system), it is split into multiple lines.
		 * 
		 * @param input
		 *            collection with input lines
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
		R from(Collection<String> input);

		/**
		 * Redirects the contents of the given file into the next command. This
		 * is essentially equivalent to the following syntax in a unix command
		 * shell:
		 * 
		 * <pre>
		 * {@code file > ...}
		 * </pre>
		 * 
		 * @param file
		 *            the file to use as input
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
		R fromFile(String file);

		/**
		 * Reads from the given resource relative to the classpath and redirects
		 * the contents into the next command. The resource is usually a file or
		 * URL on the classpath. The resource is read using
		 * {@link Class#getResourceAsStream(String)}.
		 * 
		 * @param resource
		 *            a path to the file to to redirect to the next command The
		 *            will need to be on the classpath. If the file is in the
		 *            root directory, the filename should be prefixed with a
		 *            forward slash. e.g.:
		 * 
		 *            <pre>
		 * /test-file.txt
		 * </pre>
		 * 
		 *            If the file is in a package, then the package should be
		 *            specified prefixed with a forward slash, and with each dot
		 *            "." replaced with a forward slash. e.g.:
		 * 
		 *            <pre>
		 * /org/company/my/package/test-file.txt
		 * </pre>
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
		R fromResource(String resource);

		/**
		 * Redirects the contents of the given file into the next command. This
		 * is essentially equivalent to the following syntax in a unix command
		 * shell:
		 * 
		 * <pre>
		 * {@code file > ...}
		 * </pre>
		 * 
		 * @param file
		 *            the file to use as input
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
		R from(File file);

		/**
		 * Reads from the given input stream and redirects the contents into the
		 * next command.
		 * 
		 * @param stream
		 *            the input stream to read from
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
		R from(InputStream stream);

		/**
		 * Reads from the given URL and redirects the contents into thenext
		 * command.
		 * 
		 * @param url
		 *            the URL to read from
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
		R from(URL url);

		/**
		 * Uses the given reader and redirects the read input into the next
		 * command.
		 * 
		 * @param reader
		 *            the reader used to read the input
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
		R from(Reader reader);

		/**
		 * Reads from the given input object and redirects the contents into the
		 * next command.
		 * 
		 * @param input
		 *            the input object to read from
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
		R from(Input input);
	}

	/**
	 * Option flags for the from command.
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
	 * Arguments and options for the from command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<Input> INPUT = TypedMap.keyFor("input", Input.class);

		public Args(File file) {
			this(new FileInput(file));
		}

		public Args(InputStream stream) {
			this(new StreamInput(stream));
		}

		public Args(URL url) {
			this(new URLInput(url));
		}

		public Args(Reader reader) {
			this(new ReaderInput(reader));
		}

		public Args(Input input) {
			super(Option.class);
			setArg(INPUT, input);
		}

		public Input getInput() {
			return getArg(INPUT);
		}
	}

	/**
	 * Singleton {@link Factory} for the from command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command fromString(String input) {
			return new Command(new Args(new StringInput(input)));
		}

		@Override
		public Command fromStrings(String... input) {
			return new Command(new Args(new StringInput(input)));
		}

		@Override
		public Command from(Collection<String> input) {
			final LinkedList<String> linkedList = input instanceof LinkedList ? (LinkedList<String>) input : new LinkedList<String>(input);
			return new Command(new Args(new StringInput(linkedList)));
		}

		@Override
		public Command fromFile(String file) {
			return new Command(new Args(new File(file)));
		}

		@Override
		public Command fromResource(String resource) {
			return new Command(new Args(new ResourceInput(resource)));
		}

		@Override
		public Command from(File file) {
			return new Command(new Args(file));
		}

		@Override
		public Command from(InputStream stream) {
			return new Command(new Args(stream));
		}

		@Override
		public Command from(URL url) {
			return new Command(new Args(url));
		}

		@Override
		public Command from(Reader reader) {
			return new Command(new Args(reader));
		}

		@Override
		public Command from(Input input) {
			return new Command(new Args(input));
		}
	};

	/**
	 * "From" command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, arguments);
		}

		@Override
		public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
			return new LineProcessor() {
				@Override
				public boolean processLine(Line line) {
					// ignore input, we ARE the redirected input
					return false;// no more lines expected
				}

				@Override
				public void finish() {
					final Input input = getArguments().getInput();
					boolean more = true;
					while (more && input.hasMoreLines()) {
						more = output.processLine(input.readLine());
					}
					output.finish();
				}
			};
		}

	}

	// no instances
	private From() {
		super();
	}
}
