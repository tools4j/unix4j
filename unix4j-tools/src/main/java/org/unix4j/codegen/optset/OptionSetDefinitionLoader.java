package org.unix4j.codegen.optset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.def.TypeDef;
import org.unix4j.codegen.optset.constraint.OptionConstraint;
import org.unix4j.codegen.optset.def.ActiveSetDef;
import org.unix4j.codegen.optset.def.OptionGroupDef;
import org.unix4j.codegen.optset.def.OptionSetDef;

public class OptionSetDefinitionLoader {

	public OptionSetDef create(CommandDef commandDef) {
		final OptionSetDef def = createOptionSetDef(commandDef);
		final Collection<OptionConstraint> constraints = Constraints.getOptionConstraints(commandDef);
		final Map<String, ActiveSetDef> initialActiveSets = new ActiveSetPermutationBuilder(constraints).generateActiveSetsForGroup(commandDef.options);
//		print("", initialActiveSets);
//		if (true) return null;
		addGroupsDerivedFromActiveSets(def, initialActiveSets);
		return def;
	}

	@SuppressWarnings("unused")
	private void print(String indent, Map<String, ActiveSetDef> activeSets) {
		for (final Map.Entry<String, ActiveSetDef> e : activeSets.entrySet()) {
			System.out.println(indent + e.getKey() + ": " + e.getValue().name);
			print(indent + "\t", e.getValue().next);
		}
	}

	private void addGroupsDerivedFromActiveSets(OptionSetDef def, Map<String, ActiveSetDef> initialActiveSets) {
		//create groups first
		final Map<Set<String>, OptionGroupDef> optionsToGroup = createGroupsFromActiveSets(def, initialActiveSets);

//		//initial level
		for (final ActiveSetDef initialSet : initialActiveSets.values()) {
			addGroupsDerivedFromActiveSets(optionsToGroup, initialSet);
		}

		//recurse all other levels now
		int level = 1;
		while (populateLevelActiveSetsToGroups(optionsToGroup, level)) {
			level++;
		}
		
		//set initial group
		def.initialGroup = optionsToGroup.get(def.command.options.keySet());
		
		//now add groups to def
		def.groups.addAll(optionsToGroup.values());
	}

	private Map<Set<String>, OptionGroupDef> createGroupsFromActiveSets(OptionSetDef def, Map<String, ActiveSetDef> initialActiveSets) {
		final Map<Set<String>, OptionGroupDef> optionsToGroup = new LinkedHashMap<Set<String>, OptionGroupDef>();
		//first, simply add the options
		addGroupsForActiveSets(def.command, optionsToGroup, initialActiveSets.values());
		final Set<String> allOptions = def.command.options.keySet();
		if (!optionsToGroup.containsKey(allOptions)) {
			optionsToGroup.put(allOptions, createGroupFor(def.command, allOptions));
		}
		//now, fill the optionToNextGroup values
		final OptionGroupDef allOptionsGroup = getGroupForOptions(optionsToGroup, allOptions);
		fillOptionToNextGroup(optionsToGroup, allOptionsGroup, initialActiveSets);
		//done
		return optionsToGroup;
	}

	private void addGroupsForActiveSets(CommandDef commandDef, Map<Set<String>, OptionGroupDef> optionsToGroup, Iterable<ActiveSetDef> activeSets) {
		for (final ActiveSetDef activeSet : activeSets) {
			final Set<String> options = activeSet.getAllOptions();
			OptionGroupDef grp = optionsToGroup.get(options);
			if (grp == null) {
				grp = createGroupFor(commandDef, options);
				optionsToGroup.put(options, grp);
				addGroupsForActiveSets(commandDef, optionsToGroup, activeSet.next.values());
			}
		}
	}

	private void addGroupsDerivedFromActiveSets(final Map<Set<String>, OptionGroupDef> optionsToGroup, ActiveSetDef activeSet) {
		final OptionGroupDef group = getGroupForActiveSet(optionsToGroup, activeSet);
		if (group == null) {
			throw new IllegalArgumentException("group not found for active options " + activeSet.getAllOptions() + ", group options=" + optionsToGroup.keySet());
		}
		getLevelSetFor(group, activeSet.active.size(), true).put(activeSet.name, activeSet);
	}
	
	private void fillOptionToNextGroup(Map<Set<String>, OptionGroupDef> optionsToGroup, OptionGroupDef group, Map<String, ActiveSetDef> newActiveToActiveSet) {
		for (final Map.Entry<String, ActiveSetDef> e : newActiveToActiveSet.entrySet()) {
			final String newActive = e.getKey(); 
			final ActiveSetDef activeSet = e.getValue();
			final OptionGroupDef nextGroup = getGroupForActiveSet(optionsToGroup, activeSet);
			group.optionToNextGroup.put(newActive, nextGroup);
			fillOptionToNextGroup(optionsToGroup, nextGroup, activeSet.next);
		}
	}

	private OptionGroupDef getGroupForActiveSet(final Map<Set<String>, OptionGroupDef> optionsToGroup, final ActiveSetDef activeSet) {
		return getGroupForOptions(optionsToGroup, activeSet.getAllOptions());
	}
	private OptionGroupDef getGroupForOptions(final Map<Set<String>, OptionGroupDef> optionsToGroup, final Set<String> options) {
		final OptionGroupDef grp = optionsToGroup.get(options);
		if (grp == null) {
			throw new IllegalArgumentException("no option group for options=" + options + ", groups exist for " + optionsToGroup.keySet());
		}
		return grp;
	}

	private boolean populateLevelActiveSetsToGroups(final Map<Set<String>, OptionGroupDef> optionsToGroup, int level) {
		boolean anyAdded = false;
		for (final OptionGroupDef group : optionsToGroup.values()) {
			final Map<String, ActiveSetDef> setsForThisLevel = getLevelSetFor(group, level, false);
			for (final ActiveSetDef activeSet : setsForThisLevel.values()) {
				for (ActiveSetDef next : activeSet.next.values()) {
					addGroupsDerivedFromActiveSets(optionsToGroup, next);
					anyAdded = true;
				}
			}
		}
		return anyAdded;
	}

	private Map<String, ActiveSetDef> getLevelSetFor(OptionGroupDef group, int level, boolean create) {
		final Map<String, ActiveSetDef> lastLevelSet = getLastLevelSet(group);
		if (lastLevelSet != null && lastLevelSet.get(lastLevelSet.keySet().iterator().next()).active.size() == level) {
			return lastLevelSet;
		}
		if (create) {
			final Map<String, ActiveSetDef> newLastLevelSet = new LinkedHashMap<String, ActiveSetDef>();
			group.levelActiveSets.add(newLastLevelSet);
			return newLastLevelSet;
		}
		return new TreeMap<String, ActiveSetDef>();
	}
	private Map<String, ActiveSetDef> getLastLevelSet(OptionGroupDef group) {
		final int size = group.levelActiveSets.size();
		return size == 0 ? null : group.levelActiveSets.get(size - 1);
	}
	
	private OptionSetDef createOptionSetDef(CommandDef commandDef) {
		final TypeDef optionType = new TypeDef(commandDef.command.simpleName + "Option", commandDef.pkg.name);
		final OptionSetDef def = new OptionSetDef(commandDef, optionType);
		return def;
	}

	private OptionGroupDef createGroupFor(CommandDef commandDef, Set<String> options) {
		final List<OptionDef> optionDefs = new ArrayList<OptionDef>(options.size());
		for (final String opt : options) {
			optionDefs.add(commandDef.options.get(opt));
		}
		return createGroupFor(commandDef, optionDefs);
	}
	private OptionGroupDef createGroupFor(CommandDef commandDef, Collection<OptionDef> options) {
		final OptionGroupDef grp = new OptionGroupDef(commandDef, options);
		for (final OptionDef opt : options) {
			grp.options.put(opt.name, opt);
		}
		return grp;
	}

}
