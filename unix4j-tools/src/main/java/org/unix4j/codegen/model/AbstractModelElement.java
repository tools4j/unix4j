package org.unix4j.codegen.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

abstract public class AbstractModelElement implements TemplateHashModel {

	private final Map<String, Field> fields = new LinkedHashMap<String, Field>();
	
	public AbstractModelElement() {
		for (final Field field : getClass().getFields()) {
			if (0 == (field.getModifiers() & Modifier.STATIC)) {
				fields.put(field.getName(), field);
			}
		}
	}
	
	@Override
	public boolean isEmpty() throws TemplateModelException {
		return fields.isEmpty();
	}
	
	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		final Object value = getFieldValue(key);
		if (value instanceof TemplateModel) {
			return (TemplateModel)value;
		}
		return value == null ? null : ObjectWrapper.DEFAULT_WRAPPER.wrap(value);
	}

	public Object getFieldValue(String name) {
		final Field field = fields.get(name);
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
	public Map<String,Object> toMap() {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (final String name : fields.keySet()) {
			map.put(name, getFieldValue(name));
		}
		return map;
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
