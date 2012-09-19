package org.unix4j.codegen.optset.constraint;

import java.util.Set;

public interface OptionConstraint {
	boolean isValidActiveSet(Set<String> activeSet);
}
