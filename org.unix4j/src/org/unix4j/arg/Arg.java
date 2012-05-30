package org.unix4j.arg;

public interface Arg<E extends Enum<E>, V> {
	E getKey();
	Class<V> getValueType();
	int getMinOccurance();
	int getMaxOccurance();
}
