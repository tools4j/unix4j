package org.unix4j.util;

import org.junit.Assert;

import java.util.Comparator;

import static java.lang.Integer.signum;

/**
 * Base class for unit tests of string {@link Comparator}s.
 */
abstract public class AbstractStringComparatorTest {

	private final Comparator<? super String> comparator = initComparator();
	
	abstract protected Comparator<? super String> initComparator(); 

	protected void assertSmaller(String s1, String s2) {
		assertCompare(s1, s2, -1);
		assertCompare(s2, s1, 1);
	}
	protected void assertGreater(String s1, String s2) {
		assertCompare(s1, s2, 1);
		assertCompare(s2, s1, -1);
	}
	protected void assertEqual(String s1, String s2) {
		assertCompare(s1, s2, 0);
		assertCompare(s2, s1, 0);
	}
	protected void assertCompare(String s1, String s2, int expectedCompare) {
		assertCompare(comparator, s1, s2, expectedCompare);
	}
	protected static void assertCompare(Comparator<? super String> comparator, String s1, String s2, int expectedCompare) {
		final String msg = "Expected: " + s1 + comparator(expectedCompare) + s2;
		Assert.assertEquals(msg, signum(expectedCompare), signum(comparator.compare(s1, s2)));
	}

	protected static String comparator(int cmp) {
		return cmp < 0 ? "<" : cmp > 0 ? ">" : "==";
	}
}
