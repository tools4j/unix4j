package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.io.Output;

public interface Command<A extends Arguments<A>> {
	String getName();
	A getArguments();
	Type getType();
	Command<A> withArgs(A arguments);
	Command<?> join(Command<?> next);
	void execute(Input input, Output output);
	
	enum Type {
		NoInput,
		LineByLine,
		CompleteInput;
		public boolean isLineByLine() {
			return LineByLine.equals(this);
		}
	}
}
