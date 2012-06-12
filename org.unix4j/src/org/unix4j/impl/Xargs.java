package org.unix4j.impl;

import org.unix4j.CommandInterface;
import org.unix4j.Input;
import org.unix4j.Output;

public final class Xargs {

	public static final String NAME = "xargs";
	public static final String XARG = "{}";

	public static interface Interface<S> extends CommandInterface<S> {
		S xargs();
	}

	public static enum Option {
		//no options for now
	}

	public static class Args extends AbstractArgs<Option> {
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

	public static class Command extends AbstractCommand<Interface<Command>, Args> {
		public Command(Args arguments) {
			super(NAME, true, true, FACTORY, arguments);
		}

		@Override
		public void execute(Input input, Output output) {
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
