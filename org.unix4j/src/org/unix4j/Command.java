package org.unix4j;

public interface Command<O extends Enum<O>> extends Cloneable {
	String getName();
	boolean isBatchable();
	Command<O> withArg(String arg);
	Command<O> withArgs(String... args);
	Command<O> withOpt(O option);
	Command<O> withOpt(O option, Object optionValue);
	Command<O> withOpts(O... options);
	Command<O> writeTo(Output output);
	<O2 extends Enum<O2>> JoinedCommand<O2> join(Command<O2> next);
	Command<O> clone();
	Command<O> readFrom(Input input);
	void execute();
}
