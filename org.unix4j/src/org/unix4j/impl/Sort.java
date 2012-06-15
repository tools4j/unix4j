package org.unix4j.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.unix4j.CommandInterface;
import org.unix4j.Input;
import org.unix4j.Output;

public final class Sort {

	public static final String NAME = "sort";

	public static interface Interface<S> extends CommandInterface<S> {
		S sort();
		S sort(Option... options);
	}
	public static enum Option {
		ascending, descending
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
		public Command sort() {
			return new Command(new Args());
		}
		@Override
		public Command sort(Option... options) {
			return new Command(new Args(options));
		}
	};
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.CompleteInput, arguments);
		}
		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}
		@Override
		public void executeBatch(Input input, Output output) {
			final boolean isAsc = getArguments().hasOpt(Option.ascending);
			final boolean isDesc = getArguments().hasOpt(Option.descending);
			if (isAsc && isDesc) {
				throw new IllegalArgumentException("Options " + Option.ascending + " and " + Option.descending + " cannot be specified at the same time");
			}
			final List<String> lines = new ArrayList<String>();
			while (input.hasMoreLines()) {
				lines.add(input.readLine());
			}
			Collections.sort(lines);
			if (isDesc) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					output.writeLine(lines.get(i));
				}
			} else {
				for (final String line : lines) {
					output.writeLine(line);
				}
			}
		}
	}

	// no instances
	private Sort() {
		super();
	}
}
