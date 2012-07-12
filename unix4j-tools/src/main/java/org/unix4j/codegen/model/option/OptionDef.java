package org.unix4j.codegen.model.option;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.unix4j.codegen.model.AbstractModelElement;
import org.unix4j.codegen.model.TypeDef;

public class OptionDef extends AbstractModelElement {
	public <E extends Enum<?>> OptionDef(String commandName, Class<?> commandClass, Class<? extends E> optionClass, String optionSetPackage) {
		this(new Command(commandName, commandClass), new TypeDef(optionClass), optionSetPackage);
	}
	public <E extends Enum<?>> OptionDef(Command command, TypeDef optionType, String optionSetPackage) {
		this(command, optionType, new TypeDef(optionType.name + "Set", optionSetPackage));
	}
	public <E extends Enum<?>> OptionDef(Command command, TypeDef optionType, TypeDef optionSetType) {
		this.command = command;
		this.optionType = optionType;
		this.optionSetType = optionSetType;
	}
	public final Command command;
	public final TypeDef optionType;
	public final TypeDef optionSetType;
	public final Map<String, String> javadoc = new LinkedHashMap<String, String>();
	public final Map<String, String> options = new LinkedHashMap<String, String>();
	public final List<OptionSet> optionSets = new ArrayList<OptionSet>();
	
	@Override
	public String toString() {
		return "\n" + toString("");
	}

}
