package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.Command;
import org.unix4j.JoinedCommand;
import org.unix4j.Output;
import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgVals;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.DefaultArgVals;
import org.unix4j.arg.Opt;

public class Xargs extends AbstractCommand<Xargs.ArgName> {

	public static final String NAME = "xargs";

	public static class Argument {
		public static final Arg<ArgName,Target<?>> target = new DefaultArg<ArgName,Target<?>>(ArgName.target, Target.genericClass(), 1, 1);
		public static final Arg<ArgName,Integer> L = new DefaultArg<ArgName, Integer>(ArgName.linesToInvokeTarget, Integer.class, 1, 1);
		public static final Arg<ArgName,Integer> linesToInvokeTarget = new DefaultArg<ArgName, Integer>(ArgName.linesToInvokeTarget, Integer.class, 1, 1);
		public static ArgVals<ArgName,Integer> L(int lines) {
			return linesToInvokeTarget(lines);
		}
		public static ArgVals<ArgName,Integer> linesToInvokeTarget(int lines) {
			return new DefaultArgVals<Xargs.ArgName, Integer>(linesToInvokeTarget, lines);
		}
		public static <ET extends Enum<ET>> ArgVals<ArgName,Target<?>> target(Command<ET> targetCommand, Arg<ET, String> targetArg) {
			final Target<ET> target = new Target<ET>(targetCommand, targetArg);
			return new DefaultArgVals<Xargs.ArgName, Target<?>>(Argument.target, target);
		}
	}
	public static enum ArgName {
		target,
		/** -L n : call target every n lines*/
		linesToInvokeTarget
	}

	public static class Target<ET extends Enum<ET>> implements Cloneable {
		@SuppressWarnings("unchecked")
		private static Class<Target<?>> genericClass() {
			final Class<?> clazz = Target.class;
			return (Class<Target<?>>) clazz;
		}
		private final Arg<ET, String> arg;
		private Command<ET> targetCommand;
		private Command<?> executeCommand;
		public Target(Command<ET> command, Arg<ET, String> arg) {
			this(command, command, arg);
		}
		private Target(Command<ET> targetCommand, Command<?> executeCommand, Arg<ET, String> arg) {
			this.targetCommand = targetCommand;
			this.executeCommand = executeCommand;
			this.arg = arg;
		}
		@Override
		public Target<ET> clone() {
			if (targetCommand == executeCommand) {
				return new Target<ET>(targetCommand.clone(), arg);
			} else {
				return new Target<ET>(targetCommand.clone(), executeCommand.clone(), arg);
			}
		}
		public void withArgs(String... args) {
			targetCommand = targetCommand.withArgs(arg, args);
		}
		@Override
		public String toString() {
			final Target<ET> clone = clone();
			clone.targetCommand = clone.targetCommand.withArg(arg, "{}");
			return clone.executeCommand.toString();
		}
	}

	public Xargs() {
		super(NAME, false);
	}

	public <E2 extends Enum<E2>> Command<E2> withTarget(Command<E2> targetCommand, Arg<E2, String> targetArg) {
		if (targetCommand == null) {
			throw new NullPointerException("target cannot be null");
		}
		if (targetArg == null) {
			throw new NullPointerException("target argument cannot be null");
		}
		withArgVals(Argument.target(targetCommand, targetArg));
		return new JoinedCommand<E2>(this, targetCommand);
	}

	@Override
	public <E2 extends Enum<E2>> Command<E2> join(Command<E2> next) {
		final Target<?> targetArg = getArgs(Argument.target).getFirst();
		final Command<E2> joinedCommand = targetArg.executeCommand.join(next);
		targetArg.executeCommand = joinedCommand;
		return new JoinedCommand<E2>(this, joinedCommand);
	}

	@Override
	public Command<ArgName> writeTo(Output output) {
		final Target<?> targetArg = getArgs(Argument.target).getFirst();
		targetArg.executeCommand.writeTo(output);
		return this;
	}

	public <V> Xargs withArg(Arg<ArgName,V> arg, V value) {
		super.withArg(arg, value);
		return this;
	}

	@Override
	public Xargs withOpt(Opt<ArgName> opt) {
		super.withOpt(opt);
		return this;
	}

	@Override
	protected void executeBatch() {
		final Target<?> targetArg = getArgs(Argument.target).getFirst();
		Target<?> target = targetArg.clone();
		int linesOpt = getArgs(Argument.L).getFirst();
		int lines = 0;
		while (getInput().hasMoreLines()) {
			final String line = getInput().readLine();
			lines++;
			final String[] words = line.split("\\s+");
			target.withArgs(words);
			if (linesOpt > 0 && (lines % linesOpt) == 0) {
				target.executeCommand.execute();
				target = targetArg.clone();
			}
		}
		if (linesOpt == 0 || (lines % linesOpt) != 0) {
			target.executeCommand.execute();
		}
	}

}
