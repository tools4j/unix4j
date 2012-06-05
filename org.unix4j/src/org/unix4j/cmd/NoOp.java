package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.Command;

public class NoOp extends AbstractCommand<NoOp.ArgName> {
	
	public static final String NAME = "nop";
	
	public static enum ArgName {
		//no args or opts
	}
	
	public NoOp() {
		super(NAME, true);
	}
	
	@Override
	public <O2 extends Enum<O2>> Command<O2> join(Command<O2> next) {
		next.readFrom(getInput());
		return next;
	}

	@Override
	public void executeBatch() {
		while (getInput().hasMoreLines()) {
			getOutput().writeLine(getInput().readLine());
		}
	}

}
