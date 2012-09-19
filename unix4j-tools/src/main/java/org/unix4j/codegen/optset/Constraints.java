package org.unix4j.codegen.optset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.command.def.OptionDef;
import org.unix4j.codegen.optset.constraint.EnabledOptionConstraint;
import org.unix4j.codegen.optset.constraint.ExclusiveOptionConstraint;
import org.unix4j.codegen.optset.constraint.OptionConstraint;

public class Constraints {
	
	public static Collection<OptionConstraint> getOptionConstraints(CommandDef commandDef) {
		final List<OptionConstraint> constraints = new ArrayList<OptionConstraint>();
		addExclusiveOptionConstraints(commandDef, constraints);
		addEnabledOptionConstraints(commandDef, constraints);
		return constraints;
	}

	private static void addExclusiveOptionConstraints(CommandDef commandDef, List<OptionConstraint> constraints) {
		for (final OptionDef opt : commandDef.options.values()) {
			if (!opt.excludes.isEmpty()) {
				constraints.add(new ExclusiveOptionConstraint(opt.name, opt.excludes));
			}
		}
	}
	private static void addEnabledOptionConstraints(CommandDef commandDef, List<OptionConstraint> constraints) {
		for (final OptionDef opt : commandDef.options.values()) {
			if (!opt.enabledBy.isEmpty()) {
				constraints.add(new EnabledOptionConstraint(opt.name, opt.enabledBy));
			}
		}
	}
}
