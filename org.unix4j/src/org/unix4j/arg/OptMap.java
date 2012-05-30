package org.unix4j.arg;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OptMap<E extends Enum<E>> implements Cloneable {
	
	private final Map<Arg<E, ?>, ArgList<E,?>> opts = new LinkedHashMap<Arg<E,?>, ArgList<E,?>>(); 
	
	public OptMap() {
		super();
	}
	
	public <V> void setArgList(ArgList<E,V> argList) {
		if (argList == null) {
			throw new NullPointerException("arg list cannot be null");
		}
		opts.put(argList.getArg(), argList);
	}
	@SuppressWarnings("unchecked")
	public <V> ArgList<E,V> getArgListOrNull(Arg<E,V> arg) {
		return (ArgList<E,V>)opts.get(arg);
	}
	public <V> ArgList<E,V> getArgList(Arg<E,V> arg, boolean setIfEmpty) {
		ArgList<E,V> argList = getArgListOrNull(arg);
		if (argList == null) {
			argList = new ArgList<E, V>(arg);
			if (setIfEmpty) {
				setArgList(argList);
			}
		}
		return argList;
	}
	public <V> void addArg(Arg<E,V> arg, V value) {
		getArgList(arg, true).add(value);
	}
	public <V> void addArgs(Arg<E,V> arg, V... values) {
		getArgList(arg, true).add(values);
	}
	public <V> void addArgs(Arg<E,V> arg, List<? extends V> values) {
		getArgList(arg, true).add(values);
	}
	public boolean isArgSet(Arg<E,?> arg) {
		final ArgList<E,?> argList = getArgListOrNull(arg);
		return argList != null && !argList.isEmpty();
	}
	public int getArgCount(Arg<E,?> arg) {
		final ArgList<E,?> argList = getArgListOrNull(arg);
		return argList == null ? 0 : argList.size();
	}
	public void setOpt(Opt<E> opt) {
		opts.put(opt, null);
	}
	
	public boolean isOptSet(Opt<E> opt) {
		return opts.containsKey(opt);
	}
	
	public OptMap<E> clone() {
		final OptMap<E> clone = new OptMap<E>();
		for (final Map.Entry<Arg<E,?>, ArgList<E, ?>> e : opts.entrySet()) {
			clone.opts.put(e.getKey(), e.getValue() == null ? null : e.getValue().clone());
		}
		return clone;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<Arg<E,?>, ArgList<E, ?>> e : opts.entrySet()) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			if (e.getValue() == null) {
				sb.append('-').append(e.getKey());
			} else {
				sb.append(e.getValue());
			}
		}
		return sb.toString();
	}
}
