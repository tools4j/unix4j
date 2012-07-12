package org.unix4j.codegen.model.option;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class OptionSet {
	public String name;
	public Set<String> active = new LinkedHashSet<String>();
	public Map<String, String> next = new LinkedHashMap<String, String>();

	@Override
	public String toString() {
		return "\n\tname:   " + name + "\n" +
				"\tactive: " + active + "\n" +
				"\tnext:   " + next + "\n";
	}
}