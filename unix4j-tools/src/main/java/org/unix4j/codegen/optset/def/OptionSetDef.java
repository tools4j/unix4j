package org.unix4j.codegen.optset.def;

import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.def.AbstractElementDef;
import org.unix4j.codegen.def.TypeDef;

import java.util.ArrayList;
import java.util.List;

public class OptionSetDef extends AbstractElementDef {
	public <E extends Enum<?>> OptionSetDef(CommandDef command, TypeDef optionType) {
		this.command = command;
		this.optionType = optionType;
	}
	public final CommandDef command;
	public final TypeDef optionType;
	public final List<OptionGroupDef> groups = new ArrayList<OptionGroupDef>();
	public OptionGroupDef initialGroup;
	
	@Override
	public String toString() {
		return "\n" + toString("");
	}

}
