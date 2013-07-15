package org.unix4j.util.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A comparator based on a list of underlying comparators used in the specified
 * order. If the first underlying comparator returns a value different from 0
 * indicating non-equality, the value is returned. If all underlying comparators
 * return 0, this composite comparator returns 0.
 */
public final class CompositeComparator<T> implements Comparator<T> {

	private final List<Comparator<? super T>> comparators;

	/**
	 * Constructs a comparator based on the specified underlying comparators.
	 * 
	 * @param comparators
	 *            the underlying comparators
	 */
	@SuppressWarnings("unchecked")
	public CompositeComparator(Comparator<? super T>... comparators) {
		this(Arrays.asList(comparators));
	}

	/**
	 * Constructs a comparator based on the specified underlying comparators.
	 * 
	 * @param comparator1
	 *            the first underlying comparators
	 * @param comparator2
	 *            the second underlying comparators
	 */
	@SuppressWarnings("unchecked")
	public CompositeComparator(Comparator<? super T> comparator1, Comparator<? super T> comparator2) {
		this(new Comparator[] { comparator1, comparator2 });
	}

	/**
	 * Constructs a comparator based on the specified underlying comparators.
	 * 
	 * @param comparator1
	 *            the first underlying comparators
	 * @param comparator2
	 *            the second underlying comparators
	 * @param comparator3
	 *            the third underlying comparators
	 */
	@SuppressWarnings("unchecked")
	public CompositeComparator(Comparator<? super T> comparator1, Comparator<? super T> comparator2, Comparator<? super T> comparator3) {
		this(new Comparator[] { comparator1, comparator2, comparator3 });
	}

	/**
	 * Constructs a comparator based on the specified underlying comparators.
	 * 
	 * @param comparators
	 *            the underlying comparators
	 */
	public CompositeComparator(List<Comparator<? super T>> comparators) {
		this.comparators = comparators;
	}

	@Override
	public int compare(T o1, T o2) {
		for (final Comparator<? super T> comparator : comparators) {
			final int cmp = comparator.compare(o1, o2);
			if (cmp != 0) {
				return cmp;
			}
		}
		return 0;
	}
}
