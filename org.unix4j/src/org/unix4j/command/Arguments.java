package org.unix4j.command;

import java.util.Map;

public interface Arguments<A extends Arguments<A>> {
	A clone(boolean deepClone);
	void resolve(Map<String, String> variables);
}
