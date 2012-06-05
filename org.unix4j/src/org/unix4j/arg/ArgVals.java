package org.unix4j.arg;

import java.util.List;

public interface ArgVals<E extends Enum<E>,V> {
	Arg<E, V> getArg();
	List<V> getValues();
}
