package org.unix4j.codegen.optset.constraint;

import java.util.Set;

/**
 * Constraint for an option that is only enabled if at least one option from a
 * set of "enabler options" is also active.
 */
public class EnabledOptionConstraint implements OptionConstraint {
	
	private final String option;
	private final Set<String> enabledBy;

	/**
	 * Constructor with the option which is only enabled if any one of the 
	 * options in the given set is active. In other words, the constraint 
	 * renders sets invalid which contain {@code option} but none of 
	 * {@code enabledBy}.
	 *  
	 * @param option		the constrained option
	 * @param enabledBy	the "enabler options" for the specified option
	 */
	public EnabledOptionConstraint(String option, Set<String> enabledBy) {
		this.option = option;
		this.enabledBy = enabledBy;
	}

	@Override
	public boolean isValidActiveSet(Set<String> activeSet) {
		if (activeSet.contains(option)) {
			for (final String enabler : enabledBy) {
				if (activeSet.contains(enabler)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

}
