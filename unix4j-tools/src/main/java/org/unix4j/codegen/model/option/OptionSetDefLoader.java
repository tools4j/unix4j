package org.unix4j.codegen.model.option;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.unix4j.codegen.model.TypeDef;
import org.unix4j.codegen.model.command.CommandDef;
import org.unix4j.codegen.model.command.OptionDef;

public class OptionSetDefLoader {

	public OptionSetDef create(CommandDef commandDef) {
		final OptionSetDef def = createOptionSetDef(commandDef);
		defineOptions(commandDef, def);
		defineOptionSets(commandDef, def);
		return def;
	}

	private OptionSetDef createOptionSetDef(CommandDef commandDef) {
		final Command cmd = new Command(commandDef.commandName, commandDef.command.simpleName, commandDef.command.pkg.name);
		final TypeDef optionType = new TypeDef(commandDef.command.simpleName + "Option", commandDef.commandPackage.name);
		final OptionSetDef def = new OptionSetDef(cmd, optionType, commandDef.commandPackage.name);
		return def;
	}

	private void defineOptions(CommandDef commandDef, OptionSetDef def) {
		for (final OptionDef optDef : commandDef.options.values()) {
			def.options.put(optDef.acronym, optDef.name);
			def.javadoc.put(optDef.acronym, optDef.desc);
		}
	}

	private void defineOptionSets(CommandDef commandDef, OptionSetDef def) {
		defineOptionSetsRecursive(commandDef, def, Collections.singletonList(def.options.keySet()));
	}

	private void defineOptionSetsRecursive(CommandDef commandDef, OptionSetDef def, Collection<Set<String>> thisLevelActiveOptions) {
		final Set<Set<String>> nextLevelActiveOptions = new LinkedHashSet<Set<String>>();
		for (final Set<String> active : thisLevelActiveOptions) {
			final OptionSet optionSet = defineOptionSet(def, active);
			def.optionSets.add(optionSet);
			for (final String newInactive : active) {
				final Set<String> nextActive = new LinkedHashSet<String>(active);
				nextActive.remove(newInactive);
				nextLevelActiveOptions.add(nextActive);
			}
		}
		if (!nextLevelActiveOptions.isEmpty()) {
			defineOptionSetsRecursive(commandDef, def, nextLevelActiveOptions);
		}
	}
	private OptionSet defineOptionSet(OptionSetDef def, Set<String> activeOptions) {
		final String name = getOptionSetName(def.optionSetType.simpleName, activeOptions);
		final OptionSet set = new OptionSet(name);
		for (final String active : activeOptions) {
			set.active.add(active);
		}
		final Set<String> inactiveOptions = new LinkedHashSet<String>(def.options.keySet());
		inactiveOptions.removeAll(activeOptions);
		for (final String inactive : inactiveOptions) {
			activeOptions.add(inactive);
			final String nextName = getOptionSetName(def.optionSetType.simpleName, activeOptions);
			set.next.put(inactive, nextName);
			activeOptions.remove(inactive);
		}
		return set;
	}

	private String getOptionSetName(String baseName, Set<String> active) {
		final StringBuilder sb = new StringBuilder(baseName);
		for (final String act : active) {
			sb.append('_').append(act);
		}
		return sb.toString();
	}
}
