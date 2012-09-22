package org.unix4j.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for {@link DecimalNumberStringComparator}.
 */
public class DecimalNumberStringComparatorTest {

	private final Random rnd = new Random();
	private final DecimalNumberStringComparator comparator = DecimalNumberStringComparator.getInstance();

	@Test
	public void testSmallIntegers() {
		assertSmaller("1", "2");
		assertSmaller("2", "3");
		assertSmaller("3", "4");
		assertSmaller("4", "5");
		assertSmaller("5", "6");
		assertSmaller("6", "7");
		assertSmaller("7", "8");
		assertSmaller("8", "9");
		assertSmaller("9", "10");
		assertSmaller("1", "10");
		
		assertGreater("-1", "-2");
		assertGreater("-2", "-3");
		assertGreater("-3", "-4");
		assertGreater("-4", "-5");
		assertGreater("-5", "-6");
		assertGreater("-6", "-7");
		assertGreater("-7", "-8");
		assertGreater("-8", "-9");
		assertGreater("-9", "-10");

		assertGreater("1", "-1");
		assertGreater("2", "-2");
		assertGreater("3", "-3");
		assertGreater("4", "-4");
		assertGreater("5", "-5");
		assertGreater("6", "-6");
		assertGreater("7", "-7");
		assertGreater("8", "-8");
		assertGreater("9", "-9");
		assertGreater("10", "-10");

		assertSmaller("1", "10");
		assertSmaller("10", "100");
		assertSmaller("99", "999");
		assertGreater("-1", "-10");
		assertGreater("-10", "-100");
		assertGreater("-66", "-666");
		
		assertSmaller(" 1", " \t 2");
		assertSmaller("-1", "0");
		assertSmaller("-1", "1");
		assertSmaller("-1", "2");
		assertSmaller("-1", "10");
		assertSmaller("-1", "20");
		assertSmaller("-10", "10");
	}
	
	@Test
	public void testAllBytePairs() {
		final int min = Byte.MIN_VALUE;
		final int max = Byte.MAX_VALUE;
		for (int i = min; i < max; i++) {
			for (int j = min; j < max; j++) {
				final int expected = Integer.compare(i, j);
				assertCompare(String.valueOf(i), String.valueOf(j), expected);
//				comparator.compare(String.valueOf(i), String.valueOf(j));
//				Long.compare(Long.parseLong(String.valueOf(i)), Long.parseLong(String.valueOf(j)));
//				new BigDecimal(String.valueOf(i)).compareTo(new BigDecimal(String.valueOf(j)));
			}
		}
	}
	@Test
	public void testRandomLongPairs() {
		final int runs = 10000;
		for (int i = 0; i < runs; i++) {
			final long val1 = rnd.nextLong();
			final long val2 = rnd.nextLong();
			final int expected = Long.compare(val1, val2);
			assertCompare(String.valueOf(val1), String.valueOf(val2), expected);
//			comparator.compare(String.valueOf(val1), String.valueOf(val2));
//			Long.compare(Long.parseLong(String.valueOf(val1)), Long.parseLong(String.valueOf(val2)));
//			new BigDecimal(String.valueOf(val1)).compareTo(new BigDecimal(String.valueOf(val2)));
		}
	}
	@Test
	public void testRandomVeryLongIntegerPairs() {
		final int runs = 1000;
		final int maxDigits = 1000;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < runs; i++) {
			final String val1 = randomInteger(sb, maxDigits);
			final String val2 = randomInteger(sb, maxDigits);
			final int expected = new BigInteger(val1).compareTo(new BigInteger(val2));
			assertCompare(val1, val2, expected);
		}
	}
	@Test
	public void testLargeSpecialIntegers() {
		assertSmaller("873948723948192384791283491283749812374981273948170984320548739845", "873948723948192384791283491283749812374981273948170984320548739846");
		assertSmaller("-873948723948192384791283491283749812374981273948170984320548739845", "-873948723948192384791283491283749812374981273948170984320548739844");
	}
	@Ignore	//FIXME make decimals work and enable unit test
	@Test
	public void testRandomDoublePairs() {
		final int runs = 10000;
		for (int i = 0; i < runs; i++) {
			final double val1 = rnd.nextDouble();
			final double val2 = rnd.nextDouble();
			final int expected = Double.compare(val1, val2);
			assertCompare(String.valueOf(val1), String.valueOf(val2), expected);
//			comparator.compare(String.valueOf(val1), String.valueOf(val2));
//			Long.compare(Long.parseLong(String.valueOf(val1)), Long.parseLong(String.valueOf(val2)));
//			new BigDecimal(String.valueOf(val1)).compareTo(new BigDecimal(String.valueOf(val2)));
		}
	}
	
	@Ignore	//FIXME make decimals work and enable unit test
	@Test
	public void testRandomDoublePairsAsDecimals() {
		final int runs = 10000;
		for (int i = 0; i < runs; i++) {
			final double val1 = rnd.nextDouble();
			final double val2 = rnd.nextDouble();
			final BigDecimal dec1 = BigDecimal.valueOf(val1);
			final BigDecimal dec2 = BigDecimal.valueOf(val2);
			final int expected = dec1.compareTo(dec2);
			assertCompare(dec1.toString(), dec2.toString(), expected);
//			comparator.compare(String.valueOf(val1), String.valueOf(val2));
//			Long.compare(Long.parseLong(String.valueOf(val1)), Long.parseLong(String.valueOf(val2)));
//			new BigDecimal(String.valueOf(val1)).compareTo(new BigDecimal(String.valueOf(val2)));
		}
	}
	@Test
	public void testLeadingZeros() {
		assertSmaller("-1", "00");
		assertSmaller("-01", "00");
		assertSmaller("01", "02");
		assertSmaller("01", "002");
		assertSmaller("-02", "-01");
		assertSmaller("-02", "-001");
		assertSmaller("-02", "0001");
		assertSmaller("000", "00000000000000000000000000000000000000000000000001");
		assertSmaller("-000", "00000000000000000000000000000000000000000000000001");
		assertSmaller("-00000000000000000000000000000000000000000000000001", "000");
		assertSmaller("-00000000000000000000000000000000000000000000000001", "-000");
		
		assertEqual("0", "0");
		assertEqual("  0", " 0");
		assertEqual("  0", " 0");
		assertEqual("  00", " 00");
		assertEqual("  000", " 00000");
		assertEqual("  00", " 0");
		assertEqual("-0", "0");
		assertEqual("-0", "-0");
		assertEqual("-00", "00");
		assertEqual("-00", "-00");
		assertEqual("-00", "00000");
		assertEqual("-00", "-00000");
	}
	
	private void assertSmaller(String s1, String s2) {
		assertCompare(s1, s2, -1);
		assertCompare(s2, s1, 1);
	}
	private void assertGreater(String s1, String s2) {
		assertCompare(s1, s2, 1);
		assertCompare(s2, s1, -1);
	}
	private void assertEqual(String s1, String s2) {
		assertCompare(s1, s2, 0);
		assertCompare(s2, s1, 0);
	}
	private void assertCompare(String s1, String s2, int expectedCompare) {
		final String msg = "Expected: " + s1 + comparator(expectedCompare) + s2;
		Assert.assertEquals(msg, signum(expectedCompare), signum(comparator.compare(s1, s2)));
	}

	private int signum(int value) {
		return value < 0 ? -1 : value > 0 ? 1 : 0;
	}

	private String comparator(int cmp) {
		return cmp < 0 ? "<" : cmp > 0 ? ">" : "==";
	}
	private String randomInteger(StringBuilder sb, int maxDigits) {
		sb.setLength(0);
		if (rnd.nextBoolean()) sb.append('-');
		final int digits = 1 + rnd.nextInt(maxDigits);
		for (int j = 0; j < digits; j++) {
			sb.append(rnd.nextInt(10));
		}
		return sb.toString();
	}
}
