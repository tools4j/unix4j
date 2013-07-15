package org.unix4j.option;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * Default implementation of a modifiable option set. The option set is backed
 * by an efficient {@link EnumSet}.
 * 
 * @param <E>
 *            the option enum type
 */
public class DefaultOptionSet<E extends Enum<E> & Option> implements OptionSet<E>, Iterable<E>, Cloneable {

	private final Class<E> optionType;
	private EnumSet<E> options; // cannot be final because of clone
	private EnumSet<E> useAcronym; // cannot be final because of clone

	/**
	 * Constructor for option set initialized with a single option.
	 * 
	 * @param option
	 *            the option to be set
	 */
	public DefaultOptionSet(E option) {
		this.optionType = option.getDeclaringClass();
		this.options = EnumSet.of(option);
		this.useAcronym = EnumSet.noneOf(optionType);
	}

	/**
	 * Constructor for option set initialized with at least one active options.
	 * 
	 * @param first
	 *            the first option to be set
	 * @param rest
	 *            the remaining options to be set
	 */
	@SafeVarargs
	public DefaultOptionSet(E first, E... rest) {
		this.optionType = first.getDeclaringClass();
		this.options = EnumSet.of(first, rest);
		this.useAcronym = EnumSet.noneOf(optionType);
	}

	/**
	 * Constructor for an empty option set with no active options.
	 * 
	 * @param optionType
	 *            the option type class
	 */
	public DefaultOptionSet(Class<E> optionType) {
		this.optionType = optionType;
		this.options = EnumSet.noneOf(optionType);
		this.useAcronym = EnumSet.noneOf(optionType);
	}

	@Override
	public Class<E> optionType() {
		return optionType;
	}

	/**
	 * Sets the specified {@code option}.
	 * 
	 * @param option
	 *            the option to set
	 */
	public void set(E option) {
		options.add(option);
	}

	/**
	 * Sets all specified {@code options}.
	 * 
	 * @param options
	 *            the options to set
	 */
	@SuppressWarnings("unchecked")
	public void setAll(E... options) {
		for (int i = 0; i < options.length; i++) {
			set(options[i]);
		}
	}

	/**
	 * Sets all specified {@code options}.
	 * 
	 * @param options
	 *            the options to set
	 */
	public void setAll(Collection<? extends E> options) {
		for (final E option : options) {
			set(option);
			setUseAcronymFor(option, false);
		}
	}

	/**
	 * Sets all the options contained in the specified {@code optionSet}.
	 * <p>
	 * Note that also the {@link #useAcronymFor(Option)} flags are also
	 * inherited from the specified {@code optionSet}.
	 * 
	 * @param optionSet
	 *            the optionSet with options to be set in this {@code OptionSet}
	 */
	public void setAll(OptionSet<E> optionSet) {
		options.addAll(optionSet.asSet());
		for (E option : optionType.getEnumConstants()) {
			setUseAcronymFor(option, optionSet.useAcronymFor(option));
		}
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
	@Override
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
	 * Removing an option from the
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
			clone.useAcronym = clone.useAcronym.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable", e);
		}
	}

	@Override
	public boolean useAcronymFor(E option) {
		if (useAcronym.contains(option)) {
			if (options.contains(option)) {
				return true;
			}
			useAcronym.remove(option);
		}
		return false;
	}

	/**
	 * Sets the flag indicating whether string representations should use the
	 * {@link Option#acronym() acronym} instead of the long option
	 * {@link Option#name() name} for all options.
	 * 
	 * @param useAcronym
	 *            new flag value to set, true if the option acronym should be
	 *            used for all options
	 * @see #setUseAcronymFor(Enum, boolean)
	 */
	public void setUseAcronymForAll(boolean useAcronym) {
		if (useAcronym) {
			this.useAcronym.addAll(EnumSet.complementOf(this.useAcronym));
		} else {
			this.useAcronym.removeAll(EnumSet.copyOf(this.useAcronym));
		}
	}

	/**
	 * Sets the flag indicating whether string representations should use the
	 * {@link Option#acronym() acronym} instead of the long option
	 * {@link Option#name() name} for the specified {@code option}.
	 * 
	 * @param option
	 *            the option for which this flag is set
	 * @param useAcronym
	 *            new flag value to set, true if the option acronym should be
	 *            used for the specified {@code option}
	 * @see #setUseAcronymForAll(boolean)
	 */
	public void setUseAcronymFor(E option, boolean useAcronym) {
		if (useAcronym) {
			this.useAcronym.add(option);
		} else {
			this.useAcronym.remove(option);
		}
	}

	@Override
	public String toString() {
		return toString(this);
	}

	public static <O extends Option> String toString(OptionSet<O> optionSet) {
		final StringBuilder sb = new StringBuilder();
		// first, the options with acronym
		for (final O opt : optionSet) {
			if (optionSet.useAcronymFor(opt)) {
				if (sb.length() == 0) {
					sb.append("-");
				}
				sb.append(opt.acronym());
			}
		}
		// now all options with long names
		for (final O opt : optionSet) {
			if (!optionSet.useAcronymFor(opt)) {
				if (sb.length() != 0) {
					sb.append(" ");
				}
				sb.append("--").append(opt.name());
			}
		}
		return sb.toString();
	}
}
