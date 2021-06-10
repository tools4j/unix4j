package org.unix4j.convert;

public class ConcatenatedConverter<V> implements ValueConverter<V> {
	
	private final ValueConverter<?> first;
	private final ValueConverter<V> second;
	
	public ConcatenatedConverter(ValueConverter<?> sourceConverter, ValueConverter<V> targetConverter) {
		this.first = sourceConverter;
		this.second = targetConverter;
	}
	
	public static <V> ConcatenatedConverter<V> concat(ValueConverter<?> first, ValueConverter<V> second) {
		return new ConcatenatedConverter<V>(first, second);
	}
	public <N> ConcatenatedConverter<N> concat(ValueConverter<N> next) {
		return concat(this, next);
	}
	
	@Override
	public V convert(Object value) throws IllegalArgumentException {
		final Object source = first.convert(value);
		return second.convert(source);
	}
}
