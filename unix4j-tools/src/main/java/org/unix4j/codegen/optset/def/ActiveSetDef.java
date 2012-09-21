package org.unix4j.codegen.optset.def;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.def.AbstractElementDef;
import org.unix4j.codegen.optset.OptionHelper;

public class ActiveSetDef extends AbstractElementDef {
	
	public ActiveSetDef(Map<String, OptionDef> active) {
		this(new OptionHelper().getNameWithOptionPostfix("Active", active.values()), active.keySet());
	}
	public ActiveSetDef(String name, Set<String> active) {
		this.name = name;
		this.active.addAll(active);
	}

	public final String name;
	public final SortedSet<String> active = new TreeSet<String>();								//option (long) names
	public final Map<String, ActiveSetDef> next = new LinkedHashMap<String, ActiveSetDef>();	//key: option (long) names (does not contain self refs)

	public Set<String> getAllOptions() {
		final Set<String> options = new TreeSet<String>();
		options.addAll(active);
		options.addAll(next.keySet());
		return options;
	}
	@Override
	public String toString() {
		return "\n" + 
			"\tname:\t" + name + "\n" +
			"\tactive:\t" + active + "\n" +
			"\tnext:\t" + toNextString() + "\n";
	}

	private String toNextString() {
		final Map<String, String> nextMap = new LinkedHashMap<String, String>();
		for (final Map.Entry<String, ActiveSetDef> e : next.entrySet()) {
			nextMap.put(e.getKey(), e.getValue().name);
		}
		return nextMap.toString();
	}
}