package org.unix4j.codegen.model.command;

import org.unix4j.codegen.model.AbstractModelElement;

public class OptionDef extends AbstractModelElement {
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
	public final String desc;
}
