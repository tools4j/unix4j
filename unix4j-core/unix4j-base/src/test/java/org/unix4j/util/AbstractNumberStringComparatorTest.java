package org.unix4j.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static java.lang.Long.compare;

/**
 * Base class for number comparator tests.
 */
abstract public class AbstractNumberStringComparatorTest extends AbstractStringComparatorTest {

	private final Random rnd = new Random();

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
	public void testSmallDecimals() {
		assertSmaller("1.1", "1.2");
		assertSmaller("2.2", "2.3");
		assertSmaller("3.3", "3.4");
		assertSmaller("4.4", "4.5");
		assertSmaller("5.5", "5.6");
		assertSmaller("6.6", "6.7");
		assertSmaller("7.7", "7.8");
		assertSmaller("8.8", "8.9");
		assertGreater("9.9", "9.10");
		assertEqual("10.1", "10.10");
		
		assertGreater("-1.1", "-1.2");
		assertGreater("-2.2", "-2.3");
		assertGreater("-3.3", "-3.4");
		assertGreater("-4.4", "-4.5");
		assertGreater("-5.5", "-5.6");
		assertGreater("-6.6", "-6.7");
		assertGreater("-7.7", "-7.8");
		assertGreater("-8.8", "-8.9");
		assertSmaller("-9.9", "-9.10");
		assertEqual("-10.1", "-10.10");
		
		assertGreater("1.1", "-1.1");
		assertGreater("1.2", "-1.2");
		assertGreater("1.3", "-1.3");
		assertGreater("1.4", "-1.4");
		assertGreater("1.5", "-1.5");
		assertGreater("1.6", "-1.6");
		assertGreater("1.7", "-1.7");
		assertGreater("1.8", "-1.8");
		assertGreater("1.9", "-1.9");
		assertGreater("1.10", "-1.10");

		assertSmaller("1.00", "10.0");
		assertSmaller("10.000", "100.0");
		assertSmaller("99.999", "999.9");
		assertGreater("-1.0", "-1.01");
		assertGreater("0.1", "0.001");
		assertSmaller("-0.01", "-0.001");
		assertGreater("-.66", "-.666");
		
		assertSmaller(" .1", " \t .2");
		assertSmaller("-.1", ".0");
		assertSmaller("-.1", ".1");
		assertSmaller("-.1", ".2");
		assertSmaller("-.1", ".10");
		assertSmaller("-.1", ".2");
		assertSmaller("-.01", ".01");

		assertSmaller("76", "76.1");
		assertSmaller("-76", "76.1");
		assertSmaller("-22", "22.3");
		assertSmaller("22", "76.34");
		assertSmaller("76.34", "99");
		assertSmaller("76.34", "99.");
		assertSmaller("76.34", "99.0");
		assertGreater("76.34", "76");
		assertGreater("76.34", "76.");
		assertGreater("76.34", "76.0");
		assertGreater("76.34", "75");
		assertGreater("76.34", "75.");
		assertGreater("76.34", "75.0");
		assertSmaller("76.34", "750");
		assertSmaller("76.34", "750.0");
		assertSmaller("76.34", "751");
		assertGreater("76.34", "75.5");

		assertEqual("76.000", "76");
		assertEqual("76.000", "76.");
		assertEqual("76.000", "76.0");
		assertGreater("76.001", "76");
		assertGreater("76.001", "76.");
		assertGreater("76.001", "76.000");
	}

	@Test
	public void testAllBytePairs() {
		final int min = Byte.MIN_VALUE;
		final int max = Byte.MAX_VALUE;
		for (int i = min; i < max; i++) {
			for (int j = min; j < max; j++) {
				final int expected = i - j;
				assertCompare(String.valueOf(i), String.valueOf(j), expected);
//				comparator.compare(String.valueOf(i), String.valueOf(j));
//				compare(Long.parseLong(String.valueOf(i)), Long.parseLong(String.valueOf(j)));
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
			final int expected = compare(val1, val2);
			assertCompare(String.valueOf(val1), String.valueOf(val2), expected);
//			comparator.compare(String.valueOf(val1), String.valueOf(val2));
//			compare(Long.parseLong(String.valueOf(val1)), Long.parseLong(String.valueOf(val2)));
//			new BigDecimal(String.valueOf(val1)).compareTo(new BigDecimal(String.valueOf(val2)));
		}
	}
	
	protected void testRandomVeryLongIntegerPairs(int runs, int maxDigits) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < runs; i++) {
			final String val1 = randomInteger(sb, maxDigits);
			final String val2 = randomInteger(sb, maxDigits);
			final BigInteger int1 = new BigInteger(val1);
			final BigInteger int2 = new BigInteger(val2);
			if (int1.signum() != 0 || int2.signum() != 0) {
				//both zero is a problem for double, since -0.0 < +0.0
				final int expected = int1.compareTo(int2);
				assertCompare(val1, val2, expected);
			}
		}
	}

	protected void testRandomVeryLongDecimalPairs(int runs, int maxPreDecimalDigits, int maxPostDecimalDigits) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < runs; i++) {
			final String val1 = randomDecimal(sb, maxPreDecimalDigits, maxPostDecimalDigits);
			final String val2 = randomDecimal(sb, maxPreDecimalDigits, maxPostDecimalDigits);
			final BigDecimal dec1 = new BigDecimal(val1);
			final BigDecimal dec2 = new BigDecimal(val2);
			if (dec1.signum() != 0 || dec2.signum() != 0) {
				//both zero is a problem for double, since -0.0 < +0.0
				final int expected = dec1.compareTo(dec2);
				assertCompare(val1, val2, expected);
			}
		}
	}
	
	public void testRandomMixedIntegerAndDecimalPairs(int runs, int maxPreDecimalDigits, int maxPostDecimalDigits) {
		final StringBuilder sb = new StringBuilder();
		for (int preDigits1 = 0; preDigits1 <= maxPreDecimalDigits; preDigits1++) {
			for (int postDigits1 = 0; postDigits1 <= maxPostDecimalDigits; postDigits1++) {
				final String val1 = randomDecimal(sb, preDigits1, postDigits1);
				for (int preDigits2 = 0; preDigits2 <= maxPreDecimalDigits; preDigits2++) {
					for (int postDigits2 = 0; postDigits2 <= maxPostDecimalDigits; postDigits2++) {
						final String val2 = randomDecimal(sb, preDigits2, postDigits2);
						for (int i = 0; i < runs; i++) {
							final BigDecimal dec1 = new BigDecimal(val1);
							final BigDecimal dec2 = new BigDecimal(val2);
							if (dec1.signum() != 0 || dec2.signum() != 0) {
								//both zero is a problem for double, since -0.0 < +0.0
								final int expected = dec1.compareTo(dec2);
								assertCompare(val1, val2, expected);
							}
						}
					}
				}
			}
		}
	}

	protected void testRandomDoublePairs(boolean allowScientificNotation) {
		final int runs = 100000;
		for (int i = 0; i < runs; i++) {
			final double val1 = rnd.nextDouble();
			final double val2 = rnd.nextDouble();
			final BigDecimal dec1 = BigDecimal.valueOf(val1);
			final BigDecimal dec2 = BigDecimal.valueOf(val2);
			final int expected = dec1.compareTo(dec2);
			if (allowScientificNotation) {
				assertCompare(dec1.toString(), dec2.toString(), expected);
			} else {
				assertCompare(dec1.toPlainString(), dec2.toPlainString(), expected);
			}
//			comparator.compare(String.valueOf(val1), String.valueOf(val2));
//			compare(Long.parseLong(String.valueOf(val1)), Long.parseLong(String.valueOf(val2)));
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
		assertSmaller("000", "00000000001");
		assertSmaller("-000", "00000000001");
		assertSmaller("-00000000001", "000");
		assertSmaller("-00000000001", "-000");
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
		assertEqual("-0", "-0");
		assertEqual("-00", "-00");
		assertEqual("-00", "-00000");
	}
	
	protected String decimal(double value) {
		return new BigDecimal(value).toPlainString();
	}
	protected String randomInteger(StringBuilder sb, int maxDigits) {
		sb.setLength(0);
		if (rnd.nextBoolean()) sb.append('-');
		final int digits = 1 + rnd.nextInt(maxDigits);
		appendRandomDigits(sb, digits);
		return sb.toString();
	}
	protected String randomDecimal(StringBuilder sb, int maxPreDecimalDigits, int maxPostDecimalDigits) {
		//lets half of the decimals share the integer part with the previous value stored in sb
		final int preDigits;
		final int postDigits = rnd.nextInt(1 + maxPostDecimalDigits);
		if (sb.length() > 0 && rnd.nextBoolean()) {
			preDigits = sb.indexOf(".");
			sb.setLength(preDigits + 1);
		} else {
			sb.setLength(0);
			if (rnd.nextBoolean()) {
				sb.append('-');
				preDigits = 1 + rnd.nextInt(Math.max(1, maxPreDecimalDigits));
			} else {
				preDigits = rnd.nextInt(1 + maxPreDecimalDigits);
			}
			appendRandomDigits(sb, preDigits);
			if (preDigits == 0 || postDigits > 0) {
				sb.append('.');
			}
		}
		appendRandomDigits(sb, postDigits);
		if (sb.length() == 0 || (sb.length() == 1 && sb.charAt(0) == '.')) {
			return "0.0".substring(rnd.nextInt(3));
		}
		return sb.toString();
	}
	private void appendRandomDigits(StringBuilder sb, int digits) {
		for (int j = 0; j < digits; j++) {
			sb.append(rnd.nextInt(10));
		}
	}
}
