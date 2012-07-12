package org.unix4j.optset;

import java.util.EnumSet;

public class DefaultOptionSet<E extends Enum<E>> implements OptionSet<E>, Cloneable {
	private EnumSet<E> options;
	public DefaultOptionSet(E option) {
		this.options = EnumSet.of(option);
	}
	public DefaultOptionSet(E first, E... rest) {
		this.options = EnumSet.of(first, rest);
	}
	public DefaultOptionSet(Class<E> optionType) {
		this.options = EnumSet.noneOf(optionType);
	}
	public DefaultOptionSet<E> set(E option) {
		options.add(option);
		return this;
	}
	public DefaultOptionSet<E> setAll(E... options) {
		for (int i = 0; i < options.length; i++) {
			set(options[i]);
		}
		return this;
	}
	@Override
	public boolean isSet(E option) {
		return options.contains(option);
	}
	@Override
	public EnumSet<E> asSet() {
		return options;
	}
	@Override
	public int hashCode() {
		return options.hashCode();
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof DefaultOptionSet) {
			return options.equals(((DefaultOptionSet<?>)o).options);
		}
		return false;
	}
	@Override
	public DefaultOptionSet<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			final DefaultOptionSet<E> clone = (DefaultOptionSet<E>)super.clone();
			clone.options = clone.options.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable", e);
		}
	}
	public OptionSet<E> copy() {
		return clone();
	}
	@Override
	public String toString() {
		return options.toString();
	}

}
