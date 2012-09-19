package org.unix4j.codegen.optset;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.optset.constraint.OptionConstraint;
import org.unix4j.codegen.optset.def.ActiveSetDef;

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

	public SortedMap<String, ActiveSetDef> generateActiveSetsForGroup(Map<String, OptionDef> options) {
		return generateNextActiveSets(options, new TreeSet<String>());
	}
	private SortedMap<String, ActiveSetDef> generateNextActiveSets(Map<String, OptionDef> options, SortedSet<String> active) {
		final SortedMap<String, ActiveSetDef> activeSets = new TreeMap<String, ActiveSetDef>();
		for (final String opt : options.keySet()) {
			if (!active.contains(opt)) {
				final SortedMap<String, OptionDef> newActive = new TreeMap<String, OptionDef>();
				for (final String act : active) {
					newActive.put(act, options.get(act));
				}
				newActive.put(opt, options.get(opt));
				if (isValidActiveSet(newActive.keySet())) {
					final ActiveSetDef activeSet = new ActiveSetDef(newActive);
					activeSet.next.putAll(generateNextActiveSets(options, activeSet.active));
					activeSets.put(opt, activeSet);
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
