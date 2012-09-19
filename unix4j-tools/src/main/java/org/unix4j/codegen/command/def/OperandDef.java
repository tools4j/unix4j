package org.unix4j.codegen.command.def;

import org.unix4j.codegen.def.AbstractElementDef;

public class OperandDef extends AbstractElementDef {
	public OperandDef(String name, String type, String desc, boolean isRedirected) {
		this.name = name;
		this.type = type;
		this.desc = desc;
		this.isRedirected = isRedirected;
	}
	public final String name;
	public final String type;
	public final String desc;
	public final boolean isRedirected;
}
