package org.unix4j.util;

import java.util.Comparator;

/**
 * Forwards calls to another {@link Comparator} but reverses the order. The
 * class provides static {@link #reverse(Comparator)} method but no public
 * constructor.
 */
public final class ReverseOrderComparator<T> implements Comparator<T> {

	private final Comparator<T> comparator;

	/**
	 * Private constructor used by {@link #reverse(Comparator)}.
	 * 
	 * @param comparator
	 *            the comparator to reverse
	 */
	private ReverseOrderComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Reverses the given comparator and returns the resulting comparator.
	 * 
	 * @param <T>
	 *            the generic type for the compared values
	 * @param comparator
	 *            the comparator to reverse
	 * @return a comparator sorting elements in reverse order compared to the
	 *         given {@code comparator} argument
	 */
	public static <T> Comparator<T> reverse(Comparator<T> comparator) {
		if (comparator instanceof ReverseOrderComparator) {
			return ((ReverseOrderComparator<T>) comparator).comparator;
		}
		return new ReverseOrderComparator<T>(comparator);
	}

	@Override
	public int compare(T o1, T o2) {
		return -comparator.compare(o1, o2);
	}
}
