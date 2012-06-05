package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgVals;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.DefaultArgVals;

public class Echo extends AbstractCommand<Echo.ArgName> {
	
	public static final String NAME = "echo";
	
	public static class Argument {
		public static final Arg<ArgName,String> message = new DefaultArg<ArgName,String>(ArgName.message, String.class, 0, Integer.MAX_VALUE);
		public static ArgVals<ArgName, String> message(String... messages) {
			return new DefaultArgVals<Echo.ArgName, String>(message, messages);
		}
	}
	public static enum ArgName {
		message
	}

	public Echo() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		getOutput().writeLine(getArgs(Argument.message).toArgsString());
	}

}
