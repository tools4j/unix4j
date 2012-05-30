package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.Command;
import org.unix4j.JoinedCommand;
import org.unix4j.arg.Arg;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.Opt;

public class Xargs extends AbstractCommand<Xargs.E> {
	
	public static final String NAME = "xargs";
	
	public static interface Argument {
		Arg<E,Integer> L = new DefaultArg<E, Integer>(E.linesToInvokeTarget, Integer.class, 0, 1);
		Arg<E,Integer> linesToInvokeTarget = new DefaultArg<E, Integer>(E.linesToInvokeTarget, Integer.class, 0, 1);
	}
	protected static enum E {
		/** -L n : call target every n lines*/
		linesToInvokeTarget
	}
	
	private static class Target<ET extends Enum<ET>> implements Cloneable {
		private Command<ET> command;
		private Arg<ET, String> arg;
		public Target(Command<ET> command, Arg<ET, String> arg) {
			this.command = command;
			this.arg = arg;
		}
		@Override
		public Target<ET> clone() {
			return new Target<ET>(command.clone(), arg);
		}
		public void withArgs(String... args) {
			command = command.withArgs(arg, args);
		}
	}
	
	private Target<?> target;
	
	public Xargs() {
		super(NAME, false);
	}
	
	public <V> Xargs withArg(Arg<E,V> arg, V value) {
		super.withArg(arg, value);
		return this;
	}
	
	@Override
	public Xargs withOpt(Opt<E> opt) {
		super.withOpt(opt);
		return this;
	}
	
	public <ET extends Enum<ET>> JoinedCommand<ET> withTarget(Command<ET> target, Arg<ET, String> targetArg) {
		if (target == null) {
			throw new NullPointerException("target cannot be null");
		}
		if (this.target != null) {
			throw new IllegalStateException("target has already been set to " + this.target);
		}
		this.target = new Target<ET>(target, targetArg);
		return new JoinedCommand<ET>(this, target);
	}

	@Override
	protected void executeBatch() {
		if (target == null) {
			throw new IllegalStateException("target must be set before execution");
		}
		Target<?> target = this.target.clone();
		int linesOpt = getArgs(Argument.L).getFirst();
		int lines = 0;
		while (getInput().hasMoreLines()) {
			final String line = getInput().readLine();
			lines++;
			final String[] words = line.split("\\s+");
			target.withArgs(words);
			if (linesOpt > 0 && (lines % linesOpt) == 0) {
				target.command.execute();
				target = this.target.clone();
			}
		}
		if (linesOpt == 0 || (lines % linesOpt) != 0) {
			target.command.execute();
		}
	}
}
