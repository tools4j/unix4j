package org.unix4j.util;

/**
 * Utility class with static methods useful to implement {@code equals(Object)}
 * methods.
 */
public class EqualsUtil {

	public static boolean equalObjects(Object o1, Object o2) {
		return (o1 == o2) || (o1 != null && o1.equals(o2));
	}
	//no instances
	private EqualsUtil() {
		super();
	}
}
