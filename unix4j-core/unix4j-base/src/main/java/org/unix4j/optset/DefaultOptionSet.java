package org.unix4j.optset;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

public class DefaultOptionSet<E extends Enum<E> & Option<E>> implements OptionSet<E>, Iterable<E>, Cloneable {

	private final Class<E> optionType;
	private EnumSet<E> options;
	private boolean useAcronym;

	public DefaultOptionSet(E option) {
		this.optionType = option.getDeclaringClass();
		this.options = EnumSet.of(option);
	}

	public DefaultOptionSet(E first, E... rest) {
		this.optionType = first.getDeclaringClass();
		this.options = EnumSet.of(first, rest);
	}

	public DefaultOptionSet(Class<E> optionType) {
		this.optionType = optionType;
		this.options = EnumSet.noneOf(optionType);
	}

	public Class<E> optionType() {
		return optionType;
	}

	/**
	 * Sets the specified {@code option} and returns this {@code OptionSet} for
	 * following chained operations. This {@code OptionSet} is not altered if
	 * the specified {@code option} was already set.
	 * 
	 * @param option
	 *            the option to set
	 * @return this {@code OptionSet} for following chained operations
	 */
	public DefaultOptionSet<E> set(E option) {
		options.add(option);
		return this;
	}

	/**
	 * Sets all specified {@code options} and returns this {@code OptionSet} for
	 * following chained operations. Only options that were not already set
	 * before will alter this {@code OptionSet}.
	 * 
	 * @param options
	 *            the options to set
	 * @return this {@code OptionSet} for following chained operations
	 */
	public DefaultOptionSet<E> setAll(E... options) {
		for (int i = 0; i < options.length; i++) {
			set(options[i]);
		}
		return this;
	}

	/**
	 * Sets all specified {@code options} and returns this {@code OptionSet} for
	 * following chained operations. Only options that were not already set
	 * before will alter this {@code OptionSet}.
	 * 
	 * @param options
	 *            the options to set
	 * @return this {@code OptionSet} for following chained operations
	 */
	public DefaultOptionSet<E> setAll(Collection<? extends E> options) {
		for (final E option : options) {
			set(option);
		}
		return this;
	}

	/**
	 * Sets all the options contained in the specified {@code optionSet} and
	 * returns this {@code OptionSet} for following chained operations. Only
	 * options that were not already set before will alter this
	 * {@code OptionSet}.
	 * 
	 * @param optionSet
	 *            the optionSet with options to be set in this {@code OptionSet}
	 * @return this {@code OptionSet} for following chained operations
	 */
	public DefaultOptionSet<E> setAll(OptionSet<E> optionSet) {
		if (optionSet instanceof DefaultOptionSet) {
			options.addAll(((DefaultOptionSet<E>) optionSet).asSet());
		} else {
			for (final E option : optionType.getEnumConstants()) {
				if (optionSet.isSet(option)) {
					set(option);
				}
			}
		}
		return this;
	}

	@Override
	public boolean isSet(E option) {
		return options.contains(option);
	}

	/**
	 * Returns the number of set options in this {@code OptionSet}
	 * 
	 * @return the number of set options
	 */
	public int size() {
		return options.size();
	}

	/**
	 * Returns true if no option is set.
	 * 
	 * @return true if no option is set.
	 */
	public boolean isEmpty() {
		return options.isEmpty();
	}

	/**
	 * Returns the underlying backing {@link EnumSet}. Changing the returned set
	 * will also alter this {@code OptionSet} and vice versa.
	 * 
	 * @return the underlying backing enum set
	 */
	@Override
	public EnumSet<E> asSet() {
		return options;
	}

	/**
	 * Returns an iterator over all set options in this {@code OptionSet}.
	 * 
	 * @return an iterator over all set options
	 */
	@Override
	public Iterator<E> iterator() {
		return options.iterator();
	}

	@Override
	public int hashCode() {
		return options.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof DefaultOptionSet) {
			return options.equals(((DefaultOptionSet<?>) o).options);
		}
		return false;
	}

	@Override
	public DefaultOptionSet<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			final DefaultOptionSet<E> clone = (DefaultOptionSet<E>) super.clone();
			clone.options = clone.options.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable", e);
		}
	}

	public OptionSet<E> copy() {
		return clone();
	}

	@Override
	public String toString() {
		return options.toString();
	}

	@Override
	public boolean useAcronym() {
		return useAcronym;
	}

	/**
	 * Sets the flag indicating whether string representations of this option
	 * set should use option {@link Option#acronym() acronyms} instead of the
	 * long option {@link Option#name() names}.
	 * 
	 * @param useAcronym
	 *            new flag value to set, true if option acronyms should be used
	 *            for string representations of this option set
	 */
	public void setUseAcronym(boolean useAcronym) {
		this.useAcronym = useAcronym;
	}

}
