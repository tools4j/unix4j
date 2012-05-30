package org.unix4j;

import java.util.List;

import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.Opt;

public interface Command<E extends Enum<E>> extends Cloneable {
	String getName();
	boolean isBatchable();
	Command<E> clone();
	
	<V> Command<E> withArg(Arg<E,V> arg, V value);
	<V> Command<E> withArgs(Arg<E,V> arg, V... values);
	<V> Command<E> withArgs(Arg<E,V> arg, List<? extends V> values);
	<V> ArgList<E,V> getArgs(Arg<E,V> arg);
	Command<E> withOpt(Opt<E> opt);
	boolean isOptSet(Opt<E> opt);
	
	<O2 extends Enum<O2>> JoinedCommand<O2> join(Command<O2> next);
	Command<E> readFrom(Input input);
	Command<E> writeTo(Output output);
	void execute();
}
