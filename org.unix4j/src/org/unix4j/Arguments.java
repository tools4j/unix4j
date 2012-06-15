package org.unix4j;

import java.util.Map;

public interface Arguments<A extends Arguments<A>> {
	A clone();
	void resolve(Map<String, String> variables);
}
