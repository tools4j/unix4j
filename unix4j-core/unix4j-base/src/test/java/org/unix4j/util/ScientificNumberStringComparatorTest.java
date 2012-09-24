package org.unix4j.util;

import java.util.Comparator;

import org.junit.Test;
import org.unix4j.util.sort.ScientificNumberStringComparator;

/**
 * Unit test for {@link ScientificNumberStringComparator}.
 */
public class ScientificNumberStringComparatorTest extends AbstractNumberStringComparatorTest {
	
	private static final int DOUBLE_DIGITS = String.valueOf(1L << 51).length() - 1;//mantissa length is 52 bits

	@Override
	protected Comparator<? super String> initComparator() {
		return ScientificNumberStringComparator.INSTANCE;
	}

	@Test
	public void testPositiveNegativeZero() {
		assertSmaller("-0", "0");
		assertSmaller("-0", "0.");
		assertSmaller("-0", "0.0");
		assertSmaller("-0", ".0");

		assertSmaller("-0.", "0");
		assertSmaller("-0.", "0.");
		assertSmaller("-0.", "0.0");
		assertSmaller("-0.", ".0");

		assertSmaller("-0.0", "0");
		assertSmaller("-0.0", "0.");
		assertSmaller("-0.0", "0.0");
		assertSmaller("-0.0", ".0");

		assertSmaller("-.0", "0");
		assertSmaller("-.0", "0.");
		assertSmaller("-.0", "0.0");
		assertSmaller("-.0", ".0");

		assertSmaller("-0", "0");
		assertSmaller("-0", "00");
		assertSmaller("-0", "000");
		assertSmaller("-00", "0");
		assertSmaller("-00", "00");
		assertSmaller("-00", "000");
		assertSmaller("-000", "0");
		assertSmaller("-000", "00");
		assertSmaller("-000", "000");
	}
	@Test
	public void testRandomVeryLongIntegerPairs() {
		final int runs = 10000;
		final int maxDigits = DOUBLE_DIGITS;
		super.testRandomVeryLongIntegerPairs(runs, maxDigits);
	}
	@Test
	public void testRandomVeryLongDecimalPairs() {
		final int runs = 10000;
		final int maxPreDecimalDigits = DOUBLE_DIGITS/2;
		final int maxPostDecimalDigits = DOUBLE_DIGITS/2;
		super.testRandomVeryLongDecimalPairs(runs, maxPreDecimalDigits, maxPostDecimalDigits);
	}
	@Test
	public void testRandomMixedIntegerAndDecimalPairs() {
		final int runs = 100;
		final int maxPreDecimalDigits = DOUBLE_DIGITS/2;
		final int maxPostDecimalDigits = DOUBLE_DIGITS/2;
		super.testRandomMixedIntegerAndDecimalPairs(runs, maxPreDecimalDigits, maxPostDecimalDigits);
	}
	@Test
	public void testLongMinMaxValue() {
		assertSmaller("" + (Long.MAX_VALUE / 2), "" + Long.MAX_VALUE);
		assertSmaller("" + Long.MIN_VALUE, "" + (Long.MIN_VALUE / 2));
	}
	
	@Test
	public void testRandomDoublePairs() {
		super.testRandomDoublePairs(true);
	}
	@Test
	public void testRandomDoublePairsAsDecimals() {
		super.testRandomDoublePairs(false);
	}
	@Test
	public void testDoubleMinMaxValue() {
		assertSmaller("" + Double.MAX_VALUE, "" + Double.POSITIVE_INFINITY);
		assertSmaller("" + (Double.MAX_VALUE / 2), "" + Double.MAX_VALUE);
		assertSmaller("" + Double.MIN_VALUE / 2, "" + Double.MIN_VALUE);
		assertSmaller("0", "" + Double.MIN_VALUE);

		assertGreater("" + -Double.MAX_VALUE, "" + -Double.POSITIVE_INFINITY);
		assertGreater("" + (-Double.MAX_VALUE / 2), "" + -Double.MAX_VALUE);
		assertGreater("" + -Double.MIN_VALUE / 2, "" + -Double.MIN_VALUE);
		assertGreater("0", "" + -Double.MIN_VALUE);
		
		assertEqual("" + Double.NaN, "" + Double.NaN);
		assertGreater("" + Double.NaN, "" + Double.POSITIVE_INFINITY);
		assertSmaller("ReallyNotANumber", "" + Double.NEGATIVE_INFINITY);
		assertGreater("ReallyNotANumber", "ReallyNotANumbaaaaaaaaaa");
	}

	@Test
	public void testLargeDecimals() {
		assertSmaller("0005968.48739845", "05968.48739846");
		assertSmaller("-0005968.48739845", "-05968.48739844");
	}

}
