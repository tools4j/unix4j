package org.unix4j.codegen.optset.constraint;

import org.unix4j.codegen.optset.def.ActiveSetDef;

public interface OptionConstraint {
	boolean isValidActiveSet(ActiveSetDef activeSet);
}
