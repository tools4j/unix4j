package org.unix4j.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class with static methods for cloning, for instance to deep-clone
 * nested lists, sets and maps.
 */
public class CloneUtil {
	
	/**
	 * Creates a deep clone of the specified value and returns it. Deep clone
	 * means that lists, sets, maps, arrays and other cloneable objects are 
	 * recursively cloned.
	 * 
	 * @param <T>	the type of the value to clone 
	 * @param value	the value to clone
	 * @return a deep clone of the specified value
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneDeep(T value) {
		if (value instanceof List) {
			return (T) cloneList((List<?>) value);
		}
		if (value instanceof Set) {
			return (T) cloneSet((Set<?>) value);
		}
		if (value instanceof Map) {
			return (T) cloneMap((Map<?, ?>) value);
		}
		if (value instanceof Cloneable || value.getClass().isArray()) {
			return (T) cloneReflective(value);
		}
		return value;
	}

	/**
	 * Clones the given value and returns the non-deep clone. Cloning is
	 * performed by calling the clone() method through reflection. If no such
	 * method exists or if calling the clone method fails, an exception is
	 * thrown.
	 * 
	 * @param <T>
	 *            the type of the value to clone
	 * @param value
	 *            the value to clone
	 * @return the non-deep clone of the value
	 * @throws IllegalArgumentException
	 *             if no clone method exists or if invoking the clone method
	 *             caused an exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneReflective(T value) {
		try {
			return (T) value.getClass().getMethod("clone").invoke(value);
		} catch (Exception e) {
			throw new IllegalArgumentException("clone failed for value type " + value.getClass().getName() + ", e=" + e, e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map cloneMap(Map value) {
		final Map<Object, Object> clone = cloneReflective(value);
		for (final Map.Entry e : clone.entrySet()) {
			e.setValue(cloneDeep(e.getValue()));
		}
		return clone;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List cloneList(List value) {
		final List<Object> clone;
		if (value.getClass().getName().equals("java.util.Arrays$ArrayList")) {
			clone = Arrays.asList(value.toArray());
		} else {
			clone = cloneReflective(value);
		}
		for (int i = 0; i < clone.size(); i++) {
			clone.set(i, cloneDeep(clone.get(i)));
		}
		return clone;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Set cloneSet(Set value) {
		final Set<Object> clone = cloneReflective(value);
		clone.clear();
		for (final Object element : value) {
			clone.add(cloneDeep(element));
		}
		return clone;
	}

	// no instances
	private CloneUtil() {
		super();
	}
}
