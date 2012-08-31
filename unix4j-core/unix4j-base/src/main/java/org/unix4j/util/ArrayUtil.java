package org.unix4j.util;

import java.util.Arrays;

/**
 * Utility class with static methods for arrays.
 */
public class ArrayUtil {

	/**
	 * Returns a (deep) string representation for the given array or throws an
	 * exception if {@code array} is not an array or null.
	 * <p>
	 * The method returns {@code "null"} if the given {@code array} is null.
	 * 
	 * @param array
	 *            the array to return as a string
	 * @return a (deep) string representation of the array
	 * @throws IllegalArgumentException
	 *             if {@code array} is not an array
	 * @see Arrays#deepToString(Object[])
	 */
	public static String toString(Object array) {
		if (array != null) {
			if (array instanceof Object[]) {
				return Arrays.deepToString((Object[]) array);
			}
			if (!array.getClass().isArray()) {
				throw new IllegalArgumentException(array.getClass().getName() + " is not an array: " + array);
			}
			final String s = Arrays.deepToString(new Object[] { array });
			return s.substring(1, s.length() - 1);// cut off leading [ and
													// trailing ]
		}
		return String.valueOf(array);
	}

	// no instances
	private ArrayUtil() {
		super();
	}
}
