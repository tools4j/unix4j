package org.unix4j.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CloneUtil {
	@SuppressWarnings("unchecked")
	public static <T> T cloneDeep(T value) {
		if (value instanceof List) {
			return (T)cloneList((List<?>)value);
		}
		if (value instanceof Map) {
			return (T)cloneMap((Map<?,?>)value);
		}
		if (value instanceof Cloneable) {
			return (T)cloneReflective(value);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cloneReflective(T value) {
		try {
			return (T)value.getClass().getMethod("clone").invoke(value);
		} catch (Exception e) {
			throw new IllegalStateException("clone failed for value type " + value.getClass().getName() + ", e=" + e, e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map cloneMap(Map value) {
		final Map<Object,Object> clone = cloneReflective(value);
		for (final Map.Entry e : clone.entrySet()) {
			e.setValue(cloneDeep(e.getValue()));
		}
		return clone;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List cloneList(List value) {
		if (value.getClass().getName().equals("java.util.Arrays$ArrayList")) {
			return Arrays.asList(value.toArray());
		}
		final List<Object> clone = cloneReflective(value);
		for (int i = 0; i < clone.size(); i++) {
			clone.set(i, cloneDeep(clone.get(i)));
		}
		return clone;
	}

	//no instances
	private CloneUtil() {
		super();
	}
}
