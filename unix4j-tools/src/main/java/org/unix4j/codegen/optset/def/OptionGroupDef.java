package org.unix4j.codegen.optset.def;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.def.AbstractElementDef;
import org.unix4j.codegen.def.TypeDef;
import org.unix4j.codegen.optset.OptionHelper;

public class OptionGroupDef extends AbstractElementDef {
	
	public OptionGroupDef(CommandDef commandDef, Collection<OptionDef> options) {
		final String groupName = commandDef.command.simpleName + "OptionSet";
		this.groupType = new TypeDef(new OptionHelper().getNameWithOptionPostfix(groupName, options), commandDef.pkg);
	}

	public final TypeDef groupType;
	public final SortedMap<String, OptionDef> options = new TreeMap<String, OptionDef>();	//key: option (long) names
	public final SortedMap<String, OptionGroupDef> optionToNextGroup = new TreeMap<String, OptionGroupDef>();	//key: option (long) names
	public final List<SortedMap<String, ActiveSetDef>> levelActiveSets = new ArrayList<SortedMap<String, ActiveSetDef>>();//key in map:activeSet.name / level 0: initial n active options, level 1: n+1 active option, ...

	@Override
	public String toString() {
		return "{\n" + 
		"\tgroupType:\t" + groupType.simpleName + "\n" + 
		"\toptions:\t" + options.keySet() + "\n" + 
		"\toptionToNextGroup:\t" + getOptionToNextGroupString() + "\n" + 
		"\tlevelActiveSets:\t" + getLevelActiveSetString() + "\n" +
		"}";
	}

	private String getOptionToNextGroupString() {
		final Map<String, String> sm = new LinkedHashMap<String, String>();
		for (final Map.Entry<String, OptionGroupDef> e : optionToNextGroup.entrySet()) {
			sm.put(e.getKey(), e.getValue().groupType.simpleName);
		}
		return sm.toString();
	}

	private String getLevelActiveSetString() {
		final StringBuilder sb = new StringBuilder();
		int index = 0;
		for (final SortedMap<String, ActiveSetDef> levelSets : levelActiveSets) {
			sb.append("\t[").append(index).append("]=");
			boolean first = true;
			for (final String setName : levelSets.keySet()) {
				if (!first) sb.append(", ");
				else first = false;
				sb.append(setName);
			}
			index++;
		}
		return sb.toString();
	}
}