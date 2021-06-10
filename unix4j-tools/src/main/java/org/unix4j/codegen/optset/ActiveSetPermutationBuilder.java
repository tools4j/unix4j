package org.unix4j.codegen.optset;

import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.optset.constraint.OptionConstraint;
import org.unix4j.codegen.optset.def.ActiveSetDef;

import java.util.*;

/**
 * Generates all "active option" permutations ignoring possible constraints on
 * the options. For {@code n} options, this class generates all possible 
 * <tt>2<sup>n</sup>-1</tt> active permutations (the set with no active options
 * is omitted). 
 */
public class ActiveSetPermutationBuilder {
	
	private final Collection<OptionConstraint> constraints;

	public ActiveSetPermutationBuilder(Collection<OptionConstraint> constraints) {
		this.constraints = constraints;
	}

	public Map<String, ActiveSetDef> generateActiveSetsForGroup(Map<String, OptionDef> options) {
		return generateNextActiveSets(options, new HashMap<String, ActiveSetDef>(), new TreeSet<String>());
	}
	private Map<String, ActiveSetDef> generateNextActiveSets(Map<String, OptionDef> options, Map<String, ActiveSetDef> activeSetsByName, SortedSet<String> active) {
		final Map<String, ActiveSetDef> activeSets = new LinkedHashMap<String, ActiveSetDef>();
		for (final String opt : options.keySet()) {
			if (!active.contains(opt)) {
				final SortedMap<String, OptionDef> newActive = new TreeMap<String, OptionDef>();
				for (final String act : active) {
					newActive.put(act, options.get(act));
				}
				newActive.put(opt, options.get(opt));
				if (isValidActiveSet(newActive.keySet())) {
					final ActiveSetDef activeSet = new ActiveSetDef(newActive);
					if (activeSetsByName.containsKey(activeSet.name)) {
						activeSets.put(opt, activeSetsByName.get(activeSet.name));
					} else {
						activeSetsByName.put(activeSet.name, activeSet);
						activeSet.next.putAll(generateNextActiveSets(options, activeSetsByName, activeSet.active));
						activeSets.put(opt, activeSet);
					}
				}
			}
		}
		return activeSets;
	}
	
	private boolean isValidActiveSet(Set<String> activeSet) {
		for (final OptionConstraint constraint : constraints) {
			if (!constraint.isValidActiveSet(activeSet)) {
				return false;
			}
		}
		return true;
	}
}
