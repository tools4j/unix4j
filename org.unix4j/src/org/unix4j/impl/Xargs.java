package org.unix4j.impl;

import org.unix4j.Arguments;
import org.unix4j.CommandInterface;
import org.unix4j.Input;
import org.unix4j.JoinedCommand;
import org.unix4j.Output;

public final class Xargs {

	public static final String NAME = "xargs";
	public static final String XARG = xarg(0);
	
	public static final String xarg(int index) {
		return Arguments.Variables.encode("xarg", index);
	}

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

	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.LineByLine, arguments);
		}
		
		@Override
		public org.unix4j.Command<?> join(org.unix4j.Command<?> next) {
			return new JoinedCommand<Args>(this, next) {
				@Override
				public void execute(Input input, Output output) {
//					getSecond().getArguments().resolve(bla)
					super.execute(input, output);
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
