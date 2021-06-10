package org.unix4j.codegen.command.def;

import org.unix4j.codegen.def.AbstractElementDef;

import java.util.LinkedHashSet;
import java.util.Set;

public class OptionDef extends AbstractElementDef {
	public OptionDef(String name, String acronym, String desc) {
		if (acronym.length() != 1) {
			throw new IllegalArgumentException("acronym must be one character, but was '" + acronym + "' for option " + name); 
		}
		this.name = name;
		this.acronym = acronym;
		this.desc = desc;
	}
	public final String name;
	public final String acronym;
	public final Set<String> excludes	= new LinkedHashSet<String>();	//option (long) name
	public final Set<String> enabledBy	= new LinkedHashSet<String>();	//option (long) name
	public final String desc;
}
