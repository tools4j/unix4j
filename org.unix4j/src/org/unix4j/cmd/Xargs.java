package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.Command;
import org.unix4j.JoinedCommand;

public class Xargs extends AbstractCommand<Xargs.Option> {
	public static final String NAME = "xargs";

	public static enum Option {
		/** -L n : call target every n lines */
		L;
	}

	private Command<?> target;

	public Xargs() {
		super(NAME, false);
	}

	@Override
	public Xargs withArg(Option option, String value) {
		super.withArg(option, value);
		return this;
	}

	public <O2 extends Enum<O2>> JoinedCommand<O2> withTarget(Command<O2> target) {
		if (target == null) { throw new NullPointerException("target cannot be null"); }
		if (this.target != null) { throw new IllegalStateException("target has already been set to " + this.target); }
		this.target = target;
		return new JoinedCommand<O2>(this, target);
	}

	@Override
	protected void executeBatch() {
		if (target == null) { throw new IllegalStateException("target must be set before execution"); }
		Command<?> command = target.clone();
		int linesOpt = getLinesOpt();
		int lines = 0;
		while (getInput().hasMoreLines()) {
			final String line = getInput().readLine();
			lines++;
			final String[] words = line.split("\\s+");
			command.withArgs(words);
			if (linesOpt > 0 && (lines % linesOpt) == 0) {
				command.execute();
				command = target.clone();
			}
		}
		if (linesOpt == 0 || (lines % linesOpt) != 0) {
			command.execute();
		}
	}

	@Override
	public Option getDefaultArgumentOption() {
		return Option.L;
	}

	private int getLinesOpt() {
		final String value = super.getArgs().getArgumentValue(Option.L);
		if (value != null) {
			try {
				return Integer.parseInt(value.toString());
			} catch (NumberFormatException e) {
				throw new IllegalStateException("expected numeric value for option " + Option.L + " in command=" + this, e);
			}
		}
		return 0;
	}
}