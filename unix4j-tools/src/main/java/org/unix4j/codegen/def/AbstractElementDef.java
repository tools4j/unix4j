package org.unix4j.codegen.def;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Abstract base class for all element definitions. Public fields define visible
 * properties or nested elements.
 */
abstract public class AbstractElementDef implements TemplateHashModel {

	private Map<String, Field> fields;

	private final Map<String, Field> getFields() {
		if (fields == null) {
			fields = initFields();
		}
		return fields;
	}
	private final Map<String, Field> initFields() {
		final Map<String, Field> fields = new LinkedHashMap<String, Field>();
		for (final Field field : getClass().getFields()) {
			if (0 == (field.getModifiers() & Modifier.STATIC)) {
				fields.put(field.getName(), field);
			}
		}
		return fields;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return getFields().isEmpty();
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		final Object value = getFieldValue(key);
		if (value instanceof TemplateModel) {
			return (TemplateModel) value;
		}
		return value == null ? null : ObjectWrapper.DEFAULT_WRAPPER.wrap(value);
	}

	public Object getFieldValue(String name) {
		final Field field = getFields().get(name);
		try {
			return field == null ? null : field.get(this);
		} catch (Exception e) {
			return "ERROR: cannot read field " + field.getName() + "e=" + e;
		}
	}

	@Override
	public String toString() {
		return toMap().toString();
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (final String name : getFields().keySet()) {
			map.put(name, getFieldValue(name));
		}
		return map;
	}

	public String toString(String indent) {
		return toMultiLineString(indent, toMap());
	}

	protected static String toMultiLineString(String indent, Map<String, ?> map) {
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
