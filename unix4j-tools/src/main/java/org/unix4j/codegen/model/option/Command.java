package org.unix4j.codegen.model.option;

import org.unix4j.codegen.model.TypeDef;

public class Command extends TypeDef {
	
	public Command(String name, Class<?> type) {
		super(type);
		this.name = name;
	}
	public Command(String name, String typeName, String packageName) {
		super(typeName, packageName);
		this.name = name;
	}

	public final String name;

}