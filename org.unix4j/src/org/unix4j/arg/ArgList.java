package org.unix4j.arg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArgList<E extends Enum<E>, V> implements Iterable<V>, Cloneable {
	
	private final Arg<E,V> arg;
	private final List<V> values;
	
	private List<V> unmodifiable;

	public ArgList(Arg<E,V> arg) {
		if (arg == null) {
			throw new NullPointerException("arg cannot be null");
		}
		this.arg = arg;
		this.values = new ArrayList<V>(Math.min(Math.max(4, arg.getMinOccurance()), arg.getMaxOccurance()));
	}
	protected ArgList(ArgList<E,V> toCopy) {
		this.arg = toCopy.arg;
		this.values = new ArrayList<V>(toCopy.values);
	}
	
	public Arg<E, V> getArg() {
		return arg;
	}
	
	public void add(V value) {
		checkMaxOccurance(1);
		checkNotNull(value);
		values.add(value);
	}
	public void add(V... values) {
		checkMaxOccurance(values.length);
		for (final V value : values) {
			checkNotNull(value);
			this.values.add(value);
		}
	}
	public void add(List<? extends V> values) {
		checkMaxOccurance(values.size());
		for (final V value : values) {
			checkNotNull(value);
			this.values.add(value);
		}
	}
	private void checkMaxOccurance(int add) {
		final int newSize = values.size() + add; 
		if (newSize > arg.getMaxOccurance()) {
			throw new IllegalStateException("cannot add " + add + " values as new size " + newSize + " exceeds max occurance " + arg.getMaxOccurance() + " of " + arg.getKey());
		}
	}
	private void checkNotNull(V value) {
		if (value == null) {
			throw new NullPointerException("arg value cannot be null");
		}
	}
	public void checkMinOccurance() {
		if (values.size() < arg.getMinOccurance()) {
			throw new IllegalStateException("too few values, " + values.size() + " is below min occurance " + arg.getMinOccurance() + " of " + arg.getKey());
		}
	}
	public V getFirst() {
		return get(0);
	}
	public V getLast() {
		return get(values.size() - 1);
	}
	
	public V get(int index) {
		return values.get(index);
	}
	
	public int size() {
		return values.size();
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public List<V> getAll() {
		if (unmodifiable == null) {
			unmodifiable = Collections.unmodifiableList(values);
		}
		return unmodifiable;
	}
	
	@Override
	public Iterator<V> iterator() {
		return getAll().iterator();
	}
	
	@Override
	public String toString() {
		return "-" + getArg() + " " + toArgsString();
	}
	public String toArgsString() {
		final StringBuilder sb = new StringBuilder();
		for (final V arg : values) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append(arg);
		}
		return sb.toString();
	}
	
	public ArgList<E,V> clone() {
		return new ArgList<E, V>(this);
	}

}
