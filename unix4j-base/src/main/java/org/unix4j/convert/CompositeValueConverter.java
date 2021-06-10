package org.unix4j.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompositeValueConverter<V> implements ValueConverter<V> {
	private final List<ValueConverter<? extends V>> converters = new ArrayList<ValueConverter<? extends V>>();
	public CompositeValueConverter() {
		super();
	}
	public CompositeValueConverter(Collection<? extends ValueConverter<? extends V>> converters) {
		addAll(converters);
	}
	protected CompositeValueConverter<V> add(ValueConverter<? extends V> converter) {
		this.converters.add(converter);
		return this;
	}
	protected CompositeValueConverter<V> addAll(Collection<? extends ValueConverter<? extends V>> converters) {
		this.converters.addAll(converters);
		return this;
	}
	@Override
	public V convert(Object value) throws IllegalArgumentException {
		for (final ValueConverter<? extends V> converter : converters) {
			final V converted = converter.convert(value);
			if (converted != null) {
				return converted;
			}
		}
		return null;
	}
}
