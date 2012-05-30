package org.unix4j.arg;

public class DefaultOpt<E extends Enum<E>> extends DefaultArg<E, Void> implements Opt<E> {
	public DefaultOpt(E key) {
		super(key, Void.TYPE, 0, 0);
	}
}
