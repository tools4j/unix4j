package org.unix4j;

import java.util.Map;

public class Variables {
	public static String encode(String name) {
		return "{" + name + "}";
	}
	public static String encode(String name, int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("index must be >= 0, but was " + index);
		}
		return "{" + name + "[" + index + "]}";
	}
	public static boolean isVariable(String expression) {
		final int len = expression == null ? 0 : expression.length();
		return len > 2 && expression.charAt(0) == '{' && expression.charAt(len - 1) == '}'; 
	}
	public static String resolve(String expression, Map<String,String> variables) {
		if (isVariable(expression)) {
			if (variables.containsKey(expression)) {
				return variables.get(expression);
			}
			throw new IllegalArgumentException("unresolved variable: " + expression);
		}
		return expression;
	}
	
	//no instances
	private Variables() {
		super();
	}
}