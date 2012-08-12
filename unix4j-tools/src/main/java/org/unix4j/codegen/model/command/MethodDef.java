package org.unix4j.codegen.model.command;

import java.util.Arrays;
import java.util.List;

import org.unix4j.codegen.model.AbstractModelElement;

public class MethodDef extends AbstractModelElement {
	public MethodDef(String name, String desc, String... args) {
		this.name = name;
		this.desc = desc;
		this.args = Arrays.asList(args);
	}
	public final String name;
	public final String desc;
	public final List<String> args;
}
