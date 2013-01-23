package org.unix4j.unix.find;

import org.unix4j.option.Option;
import org.unix4j.option.OptionSet;

class OptionableUtil {

	/**
	 * Finds an enum constant by a given option through the enum/option
	 * association given by the {@link Optionable} interface implemented by the
	 * enum. If no enum constant is found for the given option, null is
	 * returned.
	 * 
	 * @param <O>
	 *            the option type
	 * @param <E>
	 *            the enum type
	 * @param enumClass
	 *            the enum class
	 * @param option
	 *            the option to look for
	 * @return the enum constant that is associated with the given option
	 */
	public static <O extends Option, E extends Enum<E> & Optionable<O>> E findEnumByOption(Class<E> enumClass, O option) {
		for (E e : enumClass.getEnumConstants()) {
			if (option.equals(e.getOption())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Finds an enum constant by a comparing the options associated with the
	 * enum constant with the options in the specified set. The enum/option
	 * association is defined by the {@link Optionable} interface implemented by
	 * the enum.
	 * <p>
	 * If no enum constant is found with an associated option in the given set,
	 * the specified {@code defaultEnum} value is returned. If multiple enum
	 * constants have an associated option in the given set, the first enum with
	 * such an option is returned.
	 * 
	 * @param <O>
	 *            the option type
	 * @param <E>
	 *            the enum type
	 * @param enumClass
	 *            the enum class
	 * @param optionSet
	 *            the option set to lookup associated options
	 * @param defaultEnum
	 *            the default enum value returned if no enum has an associated
	 *            option in the given option set
	 * @return the enum constant that is associated with the given option
	 */
	public static <O extends Option, E extends Enum<E> & Optionable<O>> E findFirstEnumByOptionInSet(Class<E> enumClass, OptionSet<O> optionSet, E defaultEnum) {
		for (E e : enumClass.getEnumConstants()) {
			if (optionSet.isSet(e.getOption())) {
				return e;
			}
		}
		return defaultEnum;
	}

	// no instances
	private OptionableUtil() {
		super();
	};
}
