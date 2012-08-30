package org.unix4j.codegen.command.def;

import org.unix4j.codegen.def.AbstractElementDef;

public class OperandDef extends AbstractElementDef {
	public OperandDef(String name, String type, String desc) {
		this.name = name;
		this.type = type;
		this.desc = desc;
	}
	public final String name;
	public final String type;
	public final String desc;
}
