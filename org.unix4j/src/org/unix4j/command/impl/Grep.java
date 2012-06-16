package org.unix4j.command.impl;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;

public final class Grep {

	public static final String NAME = "grep";
	
	public static interface Interface<S> extends CommandInterface<S> {
		S grep(String matchString);
		S grep(String matchString, Option... options);
	}

	public static enum Option {
		i, ignoreCase, 
		v, invert;
	}

	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<String> MATCH_STRING = TypedMap.DefaultKey.keyFor("matchString", String.class);
		public Args(String matchString) {
			super(Option.class);
			if (matchString == null) {
				throw new NullPointerException("matchString cannot be null");
			}
			setArg(MATCH_STRING, matchString);
		}
		public Args(String matchString, Option... options) {
			this(matchString);
			setOpts(options);
		}
		public String getMatchString() {
			return getArg(MATCH_STRING);
		}
	}
	public static Interface <Command> FACTORY = new Interface<Command>() {
		@Override
		public Command grep(String matchString) {
			return new Command(new Args(matchString));
		}
		@Override
		public Command grep(String matchString, Option... options) {
			return new Command(new Args(matchString, options));
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
		public void executeBatch(Input input, Output output) {
			while (input.hasMoreLines()) {
				final Args args = getArguments();
				final String matchString = args.getMatchString();
				final String line = input.readLine();
				boolean matches;
				if (args.hasOpt(Option.i) || args.hasOpt(Option.ignoreCase)) {
					matches = line.toLowerCase().contains(matchString.toLowerCase());
				} else {
					matches = line.contains(matchString);
				}
				if (args.hasOpt(Option.v) || args.hasOpt(Option.invert)) {
					matches = !matches;
				}
				if (matches) {
					output.writeLine(line);
				}
			}
		}
	}

	// no instances
	private Grep() {
		super();
	}
}
