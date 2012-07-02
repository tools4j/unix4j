package org.unix4j.optset;

import java.util.EnumSet;

public interface OptionSet<E extends Enum<E>> {
	OptionSet<E> set(E option);

	OptionSet<E> setAll(E... options);

	boolean isSet(E option);

	EnumSet<E> asSet();

	OptionSet<E> copy();
}
