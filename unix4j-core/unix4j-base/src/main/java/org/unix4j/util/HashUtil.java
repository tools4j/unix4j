package org.unix4j.util;

/**
 * Utility class with static methods useful to implement {@code hashCode()}
 * methods.
 */
public class HashUtil {

	/**
	 * Null-safe hash code method for objects. Returns the object hash code if
	 * it is not null, and 0 otherwise.
	 * 
	 * @param o	the object
	 * @return the object's hash code, or 0 if {@code o==null}
	 */
	public static int hashObject(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	//no instances
	private HashUtil() {
		super();
	}
}
