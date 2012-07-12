package org.unix4j.codegen.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

abstract public class AbstractModelElement {

	@Override
	public String toString() {
		return toMap().toString();
	}
	public Map<String,Object> toMap() {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		toMap(getClass(), map);
		return map;
	}

	private void toMap(Class<?> clazz, Map<String, Object> map) {
		final Class<?> superClass = clazz.getSuperclass();
		while (superClass != null && !Object.class.equals(superClass) && !AbstractModelElement.class.equals(superClass)) {
			toMap(clazz.getSuperclass(), map);
		}
		for (final Field field : clazz.getFields()) {
			if (0 == (field.getModifiers() & Modifier.STATIC)) {
				try {
					map.put(field.getName(), field.get(this));
				} catch (Exception e) {
					map.put(field.getName(), "ERROR: " + e);
				}
			}
		}
	}
	public String toString(String indent) {
		return toMultiLineString(indent, toMap());
	}
	
	protected static String toMultiLineString(String indent, Map<String,?> map) {
		final StringBuilder sb = new StringBuilder();
		int maxKeyLen = 0;
		for (final String key : map.keySet()) {
			maxKeyLen = Math.max(maxKeyLen, String.valueOf(key).length());
		}
		for (final Map.Entry<String, ?> e : map.entrySet()) {
			sb.append(indent).append(e.getKey()).append(": ");
			final int keyLen = String.valueOf(e.getKey()).length();
			for (int i = keyLen; i < maxKeyLen; i++) {
				sb.append(' ');
			}
			sb.append(e.getValue()).append('\n');
		}
		return sb.toString();
	}
}
