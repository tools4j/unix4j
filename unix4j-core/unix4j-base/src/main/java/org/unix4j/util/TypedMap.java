package org.unix4j.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

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
	/**
	 * Interface implemented by keys of a typed map. The key determines the
	 * value type. Equality of a key is made up of a key object and the value
	 * type.
	 * 
	 * @param <T>
	 *            the type of values associated with this key in a typed map
	 */
	public static interface Key<T> {
		/**
		 * Returns the key object used to make a key distinguishable from other
		 * keys, in particular from other keys with the same value type.
		 * 
		 * @return the key object
		 */
		Object getKey();

		/**
		 * Returns the class defining the type of values associated with this
		 * key in a typed map.
		 * 
		 * @return the type of values associated with this key in a typed map
		 */
		Class<T> getValueType();

		/**
		 * Returns true if the specified value is also a key of the same class
		 * and if both the {@link #getKey() key} object and the
		 * {@link #getValueType() valueType} are equal for this key and the
		 * given value.
		 * 
		 * @param value
		 *            the value to be compared with
		 * @return true if the given value is a key (same class) and if key
		 *         object and value type are the same
		 */
		@Override
		boolean equals(Object value);

		/**
		 * Returns a hash code based on the {@link #getKey() key} object and the
		 * {@link #getValueType() valueType} of this key.
		 * 
		 * @return the hash code for this key
		 */
		@Override
		int hashCode();
	}

	/**
	 * Default implementation for {@link Key}. Key object and value type are
	 * passed to the constructor. Note that static keyFor...() methods are
	 * available as alternatives to the constructors.
	 * 
	 * @param <T>
	 *            the type of values associated with this key in a typed map
	 */
	public static class DefaultKey<T> implements Key<T> {
		private final Object key;
		private final Class<T> valueType;

		/**
		 * Constructor with key object and value type.
		 * 
		 * @param key
		 *            object used to make this key distinguishable from other
		 *            keys, in particular from other keys with the same value
		 *            type
		 * @param valueType
		 *            type of values associated with this key in a typed map
		 */
		public DefaultKey(Object key, Class<T> valueType) {
			if (key == null)
				throw new NullPointerException("key cannot be null");
			if (valueType == null)
				throw new NullPointerException("valueType cannot be null");
			this.key = key;
			this.valueType = valueType;
		}

		@Override
		public Object getKey() {
			return key;
		}

		@Override
		public Class<T> getValueType() {
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

	/**
	 * Creates and returns a key for the specified key object and value type.
	 * 
	 * @param key
	 *            object used to make the key distinguishable from other keys,
	 *            in particular from other keys with the same value type
	 * @param valueType
	 *            type of values associated with the returned key in a typed map
	 */
	public static <E> DefaultKey<E> keyFor(Object key, Class<E> valueType) {
		return new DefaultKey<E>(key, valueType);
	}

	/**
	 * Creates and returns a key for the specified key object and values of type
	 * {@link List} containing elements of the given {@code elementType}.
	 * 
	 * @param key
	 *            object used to make the key distinguishable from other keys,
	 *            in particular from other keys with the same value type
	 * @param elementType
	 *            type of elements in the list associated with the returned key
	 *            in a typed map
	 */
	@SuppressWarnings("unchecked")
	public static <E> DefaultKey<List<E>> keyForListOf(Object key, Class<E> elementType) {
		return new DefaultKey<List<E>>(key, (Class<List<E>>) ((Object) List.class));
	}

	/**
	 * Constructor for an empty typed map.
	 */
	public TypedMap() {
		super();
	}

	/**
	 * Constructor for a typed map containing all the values of the
	 * {@code toCopy} map.
	 */
	public TypedMap(TypedMap toCopy) {
		map.putAll(toCopy.map);
	}

	private Map<Key<?>, Object> map = createMap();

	/**
	 * Returns a new map instance used to back this typed map. A new
	 * {@link LinkedHashMap} instance is returned by this default
	 * implementation; subclasses can override this method if another map type
	 * should be used as backing map, for instance a {@link TreeMap} or a
	 * {@link ConcurrentMap} if thread-safety is an issue.
	 * 
	 * @return a new map instance used to back this typed map
	 */
	protected Map<Key<?>, Object> createMap() {
		return new LinkedHashMap<TypedMap.Key<?>, Object>();
	}

	/**
	 * Adds a new value to the map returning the old previously associated with
	 * the given key if such an mapping exists. Null is returned if no mapping
	 * existed with the given key or if the null value was associated with the
	 * key.
	 * 
	 * @param <T>
	 *            the value type
	 * @param key
	 *            the key
	 * @param value
	 *            the value to be associated with the given key in this map
	 * @return the value associated with the given key prior to calling this
	 *         method, or null if no mapping exists with the given key.
	 */
	public <T> T put(Key<T> key, T value) {
		final Object old = map.put(key, value);
		return key.getValueType().cast(old);
	}

	/**
	 * Returns true if this map contains an mapping for the given key.
	 * <p>
	 * Note that this method returns true if {@link #get(Key)} returns a value
	 * different from null, but it also returns true if the null value is
	 * currently associated with the specified key.
	 * 
	 * @param key
	 *            the key to check
	 * @return true if a value is currently associated with the given key in
	 *         this map
	 */
	public boolean containsKey(Key<?> key) {
		return map.containsKey(key);
	}

	/**
	 * Returns the value currently associated with the given key in this map.
	 * Null is returned if no mapping exists with the given key or if the null
	 * value is associated with the key.
	 * 
	 * @param <T>
	 *            the value type
	 * @param key
	 *            the key
	 * @return the value currently associated with the given key, or null if no
	 *         mapping exists with the given key.
	 */
	public <T> T get(Key<T> key) {
		return key.getValueType().cast(map.get(key));
	}

	/**
	 * Returns the number of key/value mappings currently stored in this map.
	 * 
	 * @return the number of key/value mappings in this map
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Returns true if this map contains no key/value mappings. Note that this
	 * method returns true if and only if {@link #size()} returns 0.
	 * 
	 * @return true if this map contains no key/value mappings
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Returns a {@link Set} view of the keys contained in this map. The set is
	 * backed by the map, so changes to the map are reflected in the set, and
	 * vice-versa. If the map is modified while an iteration over the set is in
	 * progress (except through the iterator's own <tt>remove</tt> operation),
	 * the results of the iteration are undefined. The set supports element
	 * removal, which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
	 * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support
	 * the <tt>add</tt> or <tt>addAll</tt> operations.
	 * 
	 * @return a set view of the keys contained in this map
	 */
	public Set<Key<?>> keySet() {
		return map.keySet();
	}

	/**
	 * Returns a {@link Collection} view of the values contained in this map.
	 * The collection is backed by the map, so changes to the map are reflected
	 * in the collection, and vice-versa. If the map is modified while an
	 * iteration over the collection is in progress (except through the
	 * iterator's own <tt>remove</tt> operation), the results of the iteration
	 * are undefined. The collection supports element removal, which removes the
	 * corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Collection.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations. It does not support the <tt>add</tt> or
	 * <tt>addAll</tt> operations.
	 * 
	 * @return a collection view of the values contained in this map
	 */
	public Collection<Object> values() {
		return map.values();
	}

	/**
	 * Returns an unmodifiable version of the underlying map. This method does
	 * not need to copy all the mappings in the map and is therefore possibly
	 * preferable over {@link #toMap()} for large maps.
	 * 
	 * @return an unmodifiable version of the underlying map.
	 * @see #toMap()
	 */
	public Map<Key<?>, Object> asMap() {
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Returns a modifiable copy of the underlying map. This method copies all
	 * values of this map into a new map, as opposed to {@link #asMap()} which
	 * returns an unmodifiable map but does not copy all the values.
	 * 
	 * @return a modifiable copy of the underlying map.
	 * @see #asMap()
	 */
	public Map<Key<?>, Object> toMap() {
		return new LinkedHashMap<TypedMap.Key<?>, Object>();
	}

	/**
	 * Returns a clone of this map.
	 * <p>
	 * If <tt>cloneDeep</tt> is true, the values in the map are recursively
	 * cloned, meaning that cloneable objects are also cloned, and elements of
	 * containers like lists, sets and arrays are cloned again and so on. Note
	 * that keys are not cloned as they are assumed to be immutable.
	 * <p>
	 * If <tt>cloneDeep</tt> is false, this map is cloned but not the values.
	 * 
	 * @param cloneDeep
	 *            whether or not to deep-clone values of this map
	 * @return a clone of this map, values cloned recursively if
	 *         <tt>cloneDeep</tt> is true
	 */
	public TypedMap clone(boolean cloneDeep) {
		try {
			final TypedMap clone = (TypedMap) super.clone();
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

	/**
	 * Returns the hash code value for this typed map based on the hash code of
	 * the backing map.
	 * 
	 * @return the hash code value for this map
	 * @see Map#hashCode()
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		return map.hashCode();
	}

	/**
	 * Returns true if {@code obj} is a typed map and if the backing map of this
	 * and {@code obj} are equal. In other words, the method returns true for
	 * another typed map containing equal key/value mappings as this map.
	 * 
	 * @return true if {@code obj} is a typed map with equal key/value mappings
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof TypedMap) {
			return ((TypedMap) obj).map.equals(map);
		}
		return false;
	}

	/**
	 * Returns a string representation of this typed map. The string
	 * representation consists of a list of key-value mappings in the order
	 * returned by the map's <tt>keySet</tt> view's iterator, enclosed in braces
	 * (<tt>"{}"</tt>). Adjacent mappings are separated by the characters
	 * <tt>", "</tt> (comma and space). Each key-value mapping is rendered as
	 * the key followed by an equals sign (<tt>"="</tt>) followed by the
	 * associated value. Keys and values are converted to strings as by
	 * {@link String#valueOf(Object)}.
	 * 
	 * @return a string representation of this map
	 */
	@Override
	public String toString() {
		return map.toString();
	}

}
