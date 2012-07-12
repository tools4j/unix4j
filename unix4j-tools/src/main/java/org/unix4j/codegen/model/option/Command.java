package org.unix4j.codegen.model.option;

import org.unix4j.codegen.model.AbstractModelElement;
import org.unix4j.codegen.model.TypeDef;

public class Command extends AbstractModelElement {
	
	public Command(String name, Class<?> type) {
		this(name, new TypeDef(type));
	}
	public Command(String name, TypeDef type) {
		this.name = name;
		this.type = type;
	}
	public final String name;
	public final TypeDef type;

	@Override
	public String toString(String indent) {
		return	indent + "name: " + name + "\n" +
				indent + "type: " + "\n" + type.toString(indent + "\t");
	}
}