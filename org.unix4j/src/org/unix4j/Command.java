package org.unix4j;

public interface Command<O extends Enum<O>> extends Cloneable {
	String getName();
	boolean isBatchable();
	Command<O> withOpt(O option);
	Command<O> withArg(O option, String argValue);
	Command<O> withArgs(O option, String ... argValues);
	Command<O> withOpts(O... options);
	Command<O> withArgs(String ... values);
	Command<O> withArg(String value);
	Command<O> writeTo(Output output);
	<O2 extends Enum<O2>> JoinedCommand<O2> join(Command<O2> next);
	Command<O> clone();
	Command<O> readFrom(Input input);
	O getDefaultArgumentOption();
	void execute();
}