package org.unix4j.codegen.model.command;

import org.unix4j.codegen.model.AbstractModelElement;

public class OperandDef extends AbstractModelElement {
	public OperandDef(String name, String type, String desc) {
		this.name = name;
		this.type = type;
		this.desc = desc;
	}
	public final String name;
	public final String type;
	public final String desc;
}
