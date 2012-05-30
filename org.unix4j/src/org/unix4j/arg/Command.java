package org.unix4j.arg;

import java.util.List;


public interface Command<E extends Enum<E>> extends Cloneable {
	<V> Command<E> setArg(Arg<E,V> arg, V value);
	<V> Command<E> setArgs(Arg<E,V> arg, V... values);
	<V> Command<E> setArgs(Arg<E,V> arg, List<? extends V> values);
	<V> V getArg(Arg<E,V> arg);
	<V> List<V> getArgs(Arg<E,V> arg);
	Command<E> setOpt(Opt<E> opt);
	boolean isOptSet(Opt<E> opt);
}
