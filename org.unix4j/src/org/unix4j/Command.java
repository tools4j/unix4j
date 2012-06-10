package org.unix4j;

import org.unix4j.CommandInterface;

public interface Command<I extends CommandInterface<? extends Command<I, A>>, A> {
	String getName();
	I getInterface();
	A getArguments();
	boolean isBatchable();
	void execute(Input input, Output output);
}
