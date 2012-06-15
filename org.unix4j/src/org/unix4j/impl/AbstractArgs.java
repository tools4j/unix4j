package org.unix4j.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.unix4j.Arguments;
import org.unix4j.Variables;
import org.unix4j.util.TypedMap;
import org.unix4j.util.TypedMap.Key;

abstract public class AbstractArgs<O extends Enum<O>, A extends AbstractArgs<O, A>> implements Arguments<A>, Cloneable {
	private TypedMap args;
	private EnumSet<O> opts;
	
	public AbstractArgs(Class<O> optClass) {
		args = new TypedMap();
		opts = EnumSet.noneOf(optClass);
	}
	
	public <T> void setArg(TypedMap.Key<T> key, T value) {
		args.put(key, value);
	}
	public <T> T getArg(TypedMap.Key<T> key) {
		return args.get(key);
	}
	public boolean hasArg(TypedMap.Key<?> key) {
		return args.containsKey(key);
	}
	public TypedMap getArgs() {
		return args;
	}
	public void setOpt(O opt) {
		opts.add(opt);
	}
	public void setOpts(O... opts) {
		for (final O opt : opts) {
			setOpt(opt);
		}
	}
	public boolean hasOpt(O opt) {
		return opts.contains(opt);
	}
	public Set<O> getOpts() {
		return opts;
	}
	@Override
	public void resolve(Map<String, String> variables) {
		for (final Map.Entry<Key<?>,?> e : args.entrySet()) {
			if (String.class.equals(e.getKey().getValueType())) {
				@SuppressWarnings("unchecked")
				final Map.Entry<Key<String>,String> se = (Map.Entry<Key<String>,String>)(Object)e;
				final String value = se.getValue();
				if (Variables.isVariable(value)) {
					se.setValue(Variables.resolve(value, variables));
				}
			} else if (List.class.equals(e.getKey().getValueType())) {
				final List<?> list = (List<?>)e.getValue();
				for (int i = 0; i < list.size(); i++) {
					final Object value = list.get(i);
					if (value instanceof String && Variables.isVariable(value.toString())) {
						@SuppressWarnings("unchecked")
						final List<String> stringList = (List<String>)list;
						stringList.set(i, Variables.resolve(value.toString(), variables));
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public A clone() {
		try {
			final A clone = (A)super.clone();
			clone.args = clone.args.clone();
			clone.opts = clone.opts.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(getClass().getName() + " should be cloneable", e);
		}
	}
	
	@Override
	public String toString() {
		if (args.isEmpty() && opts.isEmpty()) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<TypedMap.Key<?>, ?> e : args.entrySet()) {
			if (sb.length() > 0) sb.append(' ');
			sb.append("-").append(e.getKey().getKey());
			sb.append(" ").append(e.getValue());
		}
		for (final O opt : opts) {
			if (sb.length() > 0) sb.append(' ');
			sb.append("-").append(opt);
		}
		return sb.toString();
	}
}
