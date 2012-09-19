package org.unix4j.codegen.command.def;

import java.util.Arrays;
import java.util.List;

import org.unix4j.codegen.def.AbstractElementDef;

public class MethodDef extends AbstractElementDef {
	public MethodDef(String name, String desc, boolean usesStandardInput, String... args) {
		this.name = name;
		this.desc = desc;
		this.usesStandardInput = usesStandardInput;
		this.args = Arrays.asList(args);
	}
	public final String name;
	public final String desc;
	public final boolean usesStandardInput;
	public final List<String> args;
}
