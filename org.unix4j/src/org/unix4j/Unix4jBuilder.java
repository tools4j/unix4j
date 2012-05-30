package org.unix4j;

import org.unix4j.cmd.Echo;
import org.unix4j.cmd.Grep;
import org.unix4j.cmd.Ls;
import org.unix4j.cmd.Sort;
import org.unix4j.cmd.Xargs;


public class Unix4jBuilder {
	private Command<?> lastCommand;
	private Unix4jBuilder() {
		super();
	}

	public static Unix4jBuilder echo(final String str) {
		Unix4jBuilder unix4j = new Unix4jBuilder();
		unix4j.lastCommand = new Echo().withArg(str);
		return unix4j;
	}

	public static Unix4jBuilder ls() {
		Unix4jBuilder unix4j = new Unix4jBuilder();
		unix4j.lastCommand = new Ls();
		return unix4j;
	}

	public Unix4jBuilder grep(final String expression, final Grep.Option... options) {
		Grep grep = new Grep();
		grep.withArg(Grep.Option.expression, expression);
		if (options != null && options.length > 0) grep.withOpts(options);
		joinToPreviousCommand(grep);
		return this;
	}

	public Unix4jBuilder grep(final String expression) {
		Grep grep = new Grep();
		grep.withArg(Grep.Option.expression, expression);
		joinToPreviousCommand(grep);
		return this;
	}

	public Unix4jBuilder sort(final Sort.Option... options) {
		Sort sort = new Sort();
		if (options != null && options.length > 0) sort.withOpts(options);
		joinToPreviousCommand(sort);
		return this;
	}

	public Unix4jBuilder sort() {
		Sort sort = new Sort();
		joinToPreviousCommand(sort);
		return this;
	}

	public Unix4jBuilder xargs() {
		Xargs xargs = new Xargs();
		joinToPreviousCommand(xargs);
		return this;
	}

	public Unix4jBuilder xargs(final Integer callTargetEveryXLines) {
		Xargs xargs = new Xargs();
		xargs.withArg(Xargs.Option.L, callTargetEveryXLines.toString());
		joinToPreviousCommand(xargs);
		return this;
	}

	public Unix4jBuilder xargs(final Command<?> target) {
		Xargs xargs = new Xargs();
		joinToPreviousCommand(xargs);
		return this;
	}

	private void joinToPreviousCommand(final Command<?> currentCommand) {
		if (lastCommand instanceof Xargs) {
			lastCommand = ((Xargs) lastCommand).withTarget(currentCommand);

		} else if (lastCommand instanceof JoinedCommand && (((JoinedCommand<?>) lastCommand).getSecond() instanceof Xargs)){
			JoinedCommand<?> lastCommandAsJoinedCommand = (JoinedCommand<?>) lastCommand;
			((Xargs) lastCommandAsJoinedCommand.getSecond()).withTarget(currentCommand);
			lastCommand = lastCommandAsJoinedCommand.join(currentCommand);
		} else {
			lastCommand = lastCommand.join(currentCommand);
		}
	}

	public void execute() {
		lastCommand.execute();
	}

	@Override
	public String toString() {
		return lastCommand.toString();
	}

}