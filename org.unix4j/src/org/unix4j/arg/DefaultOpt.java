package org.unix4j.arg;

import java.util.Collections;
import java.util.List;

public class DefaultOpt<E extends Enum<E>> extends DefaultArg<E, Void> implements Opt<E> {
	public DefaultOpt(E key) {
		super(key, Void.TYPE, 0, 0);
	}
	
	@Override
	public Opt<E> getArg() {
		return this;
	}
	
	@Override
	public List<Void> getValues() {
		return Collections.emptyList();
	}
}
