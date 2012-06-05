package org.unix4j.arg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultArgVals<E extends Enum<E>, V> implements ArgVals<E, V> {
	
	private final Arg<E, V> arg;
	private final List<V> values;
	
	public DefaultArgVals(Arg<E, V> arg, V value) {
		this(arg, Collections.singletonList(value));
	}

	public DefaultArgVals(Arg<E, V> arg, V... values) {
		this(arg, Arrays.asList(values));
	}

	public DefaultArgVals(Arg<E, V> arg, List<V> values) {
		this.arg = arg;
		this.values = values;
	}
	
	@Override
	public Arg<E, V> getArg() {
		return arg;
	}
	@Override
	public List<V> getValues() {
		return values;
	}
}
