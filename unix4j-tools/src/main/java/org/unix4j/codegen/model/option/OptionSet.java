package org.unix4j.codegen.model.option;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.unix4j.codegen.model.AbstractModelElement;

public class OptionSet extends AbstractModelElement {
	
	public OptionSet(String name) {
		this.name = name;
	}

	public final String name;
	public final Set<String> active = new LinkedHashSet<String>();
	public final Map<String, String> next = new LinkedHashMap<String, String>();

	@Override
	public String toString() {
		return "\n" + toString("\t");
	}
}