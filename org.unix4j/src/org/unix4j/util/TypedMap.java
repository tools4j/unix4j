package org.unix4j.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A typed map is very similar to a {@link Map} but it supports values with
 * different types still providing type-safe access to those values. The trick
 * is very simple: the key determines the type of the value.
 * <p>
 * A typed map only accepts keys implementing the {@link TypedMap.Key}
 * interface. For most cases, the {@link TypedMap.DefaultKey} should provide a
 * satisfactory implementation for keys based on an arbitrary object making up
 * the key identity together with the value type.
 */
public class TypedMap implements Cloneable {
	public static interface Key<T> {
		Object getKey();

		Class<? extends T> getValueType();

		boolean equals(Object value);

		int hashCode();
	}

	public static class DefaultKey<T> implements Key<T> {
		private final Object key;
		private final Class<? extends T> valueType;

		public DefaultKey(Object key, Class<? extends T> valueType) {
			if (key == null)
				throw new NullPointerException("key cannot be null");
			if (valueType == null)
				throw new NullPointerException("valueType cannot be null");
			this.key = key;
			this.valueType = valueType;
		}
		@SuppressWarnings("unchecked")
		public DefaultKey(Object key, T valueTemplate) {
			this(key, (Class<? extends T>)valueTemplate.getClass());
		}

		public static <E> DefaultKey<E> keyFor(Object key, Class<E> valueType) {
			return new DefaultKey<E>(key, valueType);
		}

		@SuppressWarnings("unchecked")
		public static <E> DefaultKey<List<E>> keyForListOf(Object key, Class<E> elementType) {
			return new DefaultKey<List<E>>(key, (Class<List<E>>) ((Object) List.class));
		}

		public Object getKey() {
			return key;
		}

		@Override
		public Class<? extends T> getValueType() {
			return valueType;
		}

		@Override
		public int hashCode() {
			return getKey().hashCode() * 31 + getValueType().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj instanceof DefaultKey) {
				final DefaultKey<?> other = (DefaultKey<?>) obj;
				return getKey().equals(other.getKey()) && getValueType().equals(other.getValueType());
			}
			return false;
		}

		@Override
		public String toString() {
			return "key[" + getKey() + ",valueType=" + getValueType() + "]";
		}
	}
	
	public TypedMap() {
		super();
	}
	
	public TypedMap(TypedMap toCopy) {
		map.putAll(toCopy.map);
	}

	private Map<Key<?>, Object> map = createMap();

	public <T> T put(Key<T> key, T value) {
		final Object old = map.put(key, value);
		return key.getValueType().cast(old);
	}

	public boolean containsKey(Key<?> key) {
		return map.containsKey(key);
	}

	public <T> T get(Key<T> key) {
		return key.getValueType().cast(map.get(key));
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<Key<?>> keySet() {
		return map.keySet();
	}

	public Set<Map.Entry<Key<?>, Object>> entrySet() {
		return map.entrySet();
	}

	public Collection<Object> values() {
		return map.values();
	}

	/**
	 * Returns the underlying map made unmodifiable.
	 * @return an unmodifiable version of the underlying map.
	 */
	public Map<Key<?>, Object> asMap() {
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Returns a modifiable copy of the underlying map.
	 * @return a modifiable copy of the underlying map.
	 */
	public Map<Key<?>, Object> createMap() {
		return new LinkedHashMap<TypedMap.Key<?>, Object>();
	}
	
	public TypedMap clone(boolean cloneDeep) {
		try {
			final TypedMap clone = (TypedMap)super.clone();
			if (cloneDeep) {
				clone.map = CloneUtil.cloneDeep(clone.map);
			} else {
				clone.map = createMap();
				clone.map.putAll(this.map);
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(getClass().getName() + " should be cloneable", e);
		}
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof TypedMap) {
			return ((TypedMap) obj).map.equals(map);
		}
		return false;
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
