package org.unix4j.unix;

import java.util.HashMap;
import java.util.Map;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.Arguments;
import org.unix4j.command.CommandInterface;
import org.unix4j.command.JoinedCommand;
import org.unix4j.io.Input;
import org.unix4j.io.NullInput;
import org.unix4j.io.Output;
import org.unix4j.util.Variables;

/**
 * Non-instantiable module with inner types making up the xargs command.
 */
public final class Xargs {

	/**
	 * The "xargs" command name.
	 */
	public static final String NAME = "xargs";
	/**
	 * Encoded variable for first "xarg" value. Can be used as argument value in
	 * the command following xargs.
	 */
	public static final String XARG = xarg(0);

	/**
	 * Encoded variable for <code>i<sup>th</sup></code> "xarg" value, where
	 * {@code i} specifies the column index of the value on the input line. Can
	 * be used as argument value in the command following xargs.
	 * 
	 * @param i
	 *            the zero-based column index of the value in the input line
	 * @throws IndexOutOfBoundsException
	 *             if {@code i < 0}
	 */
	public static final String xarg(int i) {
		return Variables.encode("xarg", i);
	}

	/**
	 * Interface defining all method signatures for the xargs command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command builder providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Converts lines from the input into arguments for the command
		 * following the xargs command. Each word from the input line forms a
		 * separate argument.
		 * <p>
		 * The arguments from xargs can be passed to the next command as string
		 * argument values using {@link Xargs#XARG} and {@link Xargs#xarg(int)}.
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
		R xargs();
	}

	/**
	 * Option flags for the xargs command.
	 */
	public static enum Option {
		// no options for now
	}

	/**
	 * Arguments and options for the xargs command.
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
	 * Singleton {@link Factory} for the xargs command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command xargs() {
			return new Command(new Args());
		}
	};

	/**
	 * Xargs command implementation.
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
		public org.unix4j.command.Command<?> join(org.unix4j.command.Command<?> next) {
			return join(this, next);
		}

		private static <A1 extends Arguments<A1>, A2 extends Arguments<A2>> org.unix4j.command.Command<A1> join(org.unix4j.command.Command<A1> first, final org.unix4j.command.Command<A2> second) {
			return new JoinedCommand<A1>(first, second) {
				@Override
				public void execute(Input input, Output output) {
					final Map<String, String> xargs = new HashMap<String, String>();
					final A2 args = second.getArguments().clone(true /*
																	 * deep
																	 * clone
																	 */);
					while (input.hasMoreLines()) {
						final String line = input.readLine();
						final String[] words = line.split("\\s+");
						for (int i = 0; i < words.length; i++) {
							xargs.put(xarg(i), words[i]);
						}
						args.resolve(xargs);
						second.withArgs(args).execute(NullInput.INSTANCE, output);
						xargs.clear();
					}
				}
			};
		}

		@Override
		public void executeBatch(Input input, Output output) {
			while (input.hasMoreLines()) {
				final String line = input.readLine();
				final String[] words = line.split("\\s+");
				for (int i = 0; i < words.length; i++) {
					output.writeLine(words[i]);
				}
			}
		}
	}

	// no instances
	private Xargs() {
		super();
	}

}
