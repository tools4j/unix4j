package org.unix4j.arg;

public class DefaultArg<E extends Enum<E>, V> implements Arg<E, V> {
	private final E key;
	private final Class<V> valueType;
	private final int minOccurance;
	private final int maxOccurance;

	public DefaultArg(E key, Class<V> valueType, int minOccurance, int maxOccurance) {
		if (key == null) {
			throw new NullPointerException("key cannot be null");
		}
		if (valueType == null) {
			throw new NullPointerException("valueType cannot be null");
		}
		if (minOccurance < 0) {
			throw new IllegalArgumentException("minOccurance must be >= 0, but was " + minOccurance);
		}
		if (maxOccurance < 1 && !Void.TYPE.equals(valueType)) {
			throw new IllegalArgumentException("maxOccurance must be >= 1, but was " + maxOccurance);
		}
		if (maxOccurance < minOccurance) {
			throw new IllegalArgumentException("maxOccurance must be >= minOccurance, but " + maxOccurance + " < " + minOccurance);
		}
		this.key = key;
		this.valueType = valueType;
		this.minOccurance = minOccurance;
		this.maxOccurance = maxOccurance;
	}
	
	@Override
	public E getKey() {
		return key;
	}

	@Override
	public Class<V> getValueType() {
		return valueType;
	}

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (getClass().equals(obj.getClass())) {
			final DefaultArg<?, ?> arg = (DefaultArg<?, ?>) obj;
			return getKey().equals(arg.getKey()) && getValueType().equals(arg.getValueType()) && getMinOccurance() == arg.getMinOccurance() && getMaxOccurance() == arg.getMaxOccurance();
		}
		return false;
	}

	@Override
	public int getMinOccurance() {
		return minOccurance;
	}

	@Override
	public int getMaxOccurance() {
		return maxOccurance;
	}

	@Override
	public String toString() {
		return getKey().toString();
	}
}
