package org.unix4j.impl;

import java.util.HashMap;
import java.util.Map;

import org.unix4j.Arguments;
import org.unix4j.CommandInterface;
import org.unix4j.Input;
import org.unix4j.JoinedCommand;
import org.unix4j.Output;
import org.unix4j.Variables;
import org.unix4j.io.NullInput;

public final class Xargs {

	public static final String NAME = "xargs";
	public static final String XARG = xarg(0);
	
	public static final String xarg(int index) {
		return Variables.encode("xarg", index);
	}

	public static interface Interface<S> extends CommandInterface<S> {
		S xargs();
	}

	public static enum Option {
		//no options for now
	}

	public static class Args extends AbstractArgs<Option, Args> {
		public Args() {
			super(Option.class);
		}

		public Args(Option... options) {
			this();
			setOpts(options);
		}
	}

	public static Interface<Command> FACTORY = new Interface<Command>() {
		@Override
		public Command xargs() {
			return new Command(new Args());
		}
	};

	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.LineByLine, arguments);
		}
		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}
		@Override
		public org.unix4j.Command<?> join(org.unix4j.Command<?> next) {
			return join(this, next);
		}

		private static <A1 extends Arguments<A1>,A2 extends Arguments<A2>> org.unix4j.Command<A1> join(org.unix4j.Command<A1> first, final org.unix4j.Command<A2> second) {
			return new JoinedCommand<A1>(first, second) {
				@Override
				public void execute(Input input, Output output) {
					final Map<String,String> xargs = new HashMap<String, String>();
					final A2 args = second.getArguments().clone();
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
