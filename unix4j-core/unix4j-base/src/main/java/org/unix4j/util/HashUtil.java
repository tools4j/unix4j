package org.unix4j.util;

/**
 * Utility class with static methods useful to implement {@code hashCode()}
 * methods.
 */
public class HashUtil {

	public static int hashObject(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	//no instances
	private HashUtil() {
		super();
	}
}
