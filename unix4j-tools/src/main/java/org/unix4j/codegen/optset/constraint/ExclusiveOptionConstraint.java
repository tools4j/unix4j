package org.unix4j.codegen.optset.constraint;

import java.util.Set;

import org.unix4j.codegen.optset.def.ActiveSetDef;

/**
 * Constraint for a set of options that are excluded if a certain option is
 * active.
 */
public class ExclusiveOptionConstraint implements OptionConstraint {

	private final String option;
	private final Set<String> excluded;

	/**
	 * Constructor with option and set of excluded options if {@code option} is
	 * active.
	 * 
	 * @param option
	 *            the option (long) name; if active the {@code excluded} options
	 *            are not allowed to be active
	 * @param excluded
	 *            the (long) names of options that cannot be active if
	 *            {@code option} is active
	 */
	public ExclusiveOptionConstraint(String option, Set<String> excluded) {
		this.option = option;
		this.excluded = excluded;
	}

	@Override
	public boolean isValidActiveSet(ActiveSetDef activeSet) {
		if (activeSet.active.contains(option)) {
			for (final String dont : excluded) {
				if (activeSet.active.contains(dont)) {
					return false;
				}
			}
		}
		return true;
	}

}
