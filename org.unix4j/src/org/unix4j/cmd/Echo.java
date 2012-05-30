package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.arg.Arg;
import org.unix4j.arg.DefaultArg;

public class Echo extends AbstractCommand<Echo.E> {
	
	public static final String NAME = "echo";
	
	public static interface Argument {
		Arg<E,String> message = new DefaultArg<E,String>(E.message, String.class, 0, Integer.MAX_VALUE);
	}
	protected static enum E {
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
