package org.unix4j.codegen.optset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.optset.constraint.ExcludedOptionsConstraint;
import org.unix4j.codegen.optset.constraint.OptionConstraint;
import org.unix4j.codegen.optset.def.ActiveSetDef;

public class ConstraintEnforcer {
	
	public void enforceConstraintsAndFilterActiveSets(CommandDef commandDef, Map<String, ActiveSetDef> activeSets) {
		removeActiveSetsThatAreViolatingAnOptionConstraint(commandDef, activeSets);
	}

	private void removeActiveSetsThatAreViolatingAnOptionConstraint(CommandDef commandDef, Map<String, ActiveSetDef> activeSets) {
		final Collection<OptionConstraint> constraints = getOptionConstraints(commandDef);
		final Iterator<Map.Entry<String, ActiveSetDef>> it = activeSets.entrySet().iterator();
		while (it.hasNext()) {
			final Map.Entry<String, ActiveSetDef> e = it.next();
			final ActiveSetDef activeSet = e.getValue();
			if (isValidActiveSet(constraints, activeSet)) {
				removeActiveSetsThatAreViolatingAnOptionConstraint(commandDef, activeSet.next);
			} else {
				it.remove();
			}
		}
	}
	
	private boolean isValidActiveSet(Collection<OptionConstraint> constraints, ActiveSetDef activeSet) {
		for (final OptionConstraint constraint : constraints) {
			if (!constraint.isValidActiveSet(activeSet)) {
				return false;
			}
		}
		return true;
	}

	private Collection<OptionConstraint> getOptionConstraints(CommandDef commandDef) {
		final List<OptionConstraint> constraints = new ArrayList<OptionConstraint>();
		addExclusiveOptionConstraints(commandDef, constraints);
		return constraints;
	}

	private void addExclusiveOptionConstraints(CommandDef commandDef, List<OptionConstraint> constraints) {
		for (final OptionDef opt : commandDef.options.values()) {
			if (!opt.excludes.isEmpty()) {
				constraints.add(new ExcludedOptionsConstraint(opt.name, opt.excludes));
			}
		}
	}
}
