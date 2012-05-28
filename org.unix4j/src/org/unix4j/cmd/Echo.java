package org.unix4j.cmd;

import org.unix4j.AbstractCommand;

public class Echo extends AbstractCommand<Echo.Option> {
	
	public static final String NAME = "echo";
	public static enum Option {
		
	}
	
	public Echo() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		getOutput().writeLine(getArgsAsString());
	}

}
