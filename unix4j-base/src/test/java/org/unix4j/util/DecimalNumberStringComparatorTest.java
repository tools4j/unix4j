package org.unix4j.util;

import org.junit.Test;
import org.unix4j.util.sort.DecimalNumberStringComparator;

import java.util.Comparator;
import java.util.Locale;

/**
 * Unit test for {@link DecimalNumberStringComparator}.
 */
public class DecimalNumberStringComparatorTest extends AbstractNumberStringComparatorTest {

	@Override
	protected Comparator<? super String> initComparator() {
		return DecimalNumberStringComparator.getInstance(Locale.US);
	}

	@Test
	public void testPositiveNegativeZero() {
		assertEqual("-0", "0");
		assertEqual("-0", "0.");
		assertEqual("-0", "0.0");
		assertEqual("-0", ".0");

		assertEqual("-0.", "0");
		assertEqual("-0.", "0.");
		assertEqual("-0.", "0.0");
		assertEqual("-0.", ".0");

		assertEqual("-0.0", "0");
		assertEqual("-0.0", "0.");
		assertEqual("-0.0", "0.0");
		assertEqual("-0.0", ".0");

		assertEqual("-.0", "0");
		assertEqual("-.0", "0.");
		assertEqual("-.0", "0.0");
		assertEqual("-.0", ".0");

		assertEqual("-0", "0");
		assertEqual("-0", "00");
		assertEqual("-0", "000");
		assertEqual("-00", "0");
		assertEqual("-00", "00");
		assertEqual("-00", "000");
		assertEqual("-000", "0");
		assertEqual("-000", "00");
		assertEqual("-000", "000");
	}

	@Test
	public void testRandomVeryLongIntegerPairs() {
		final int runs = 1000;
		final int maxDigits = 1000;
		super.testRandomVeryLongIntegerPairs(runs, maxDigits);
	}
	@Test
	public void testRandomVeryLongDecimalPairs() {
		final int runs = 1000;
		final int maxPreDecimalDigits = 1000;
		final int maxPostDecimalDigits = 1000;
		super.testRandomVeryLongDecimalPairs(runs, maxPreDecimalDigits, maxPostDecimalDigits);
	}
	@Test
	public void testRandomMixedIntegerAndDecimalPairs() {
		final int runs = 3;
		final int maxPreDecimalDigits = 20;
		final int maxPostDecimalDigits = 20;
		super.testRandomMixedIntegerAndDecimalPairs(runs, maxPreDecimalDigits, maxPostDecimalDigits);
	}
	
	@Test
	public void testRandomDoublePairs() {
		super.testRandomDoublePairs(false);
	}
	@Test
	public void testLongMinMaxValue() {
		assertSmaller("" + (Long.MAX_VALUE - 1), "" + Long.MAX_VALUE);
		assertSmaller("" + Long.MIN_VALUE, "" + (Long.MIN_VALUE + 1));
	}
	@Test
	public void testDoubleMinMaxValue() {
		assertSmaller(decimal(Double.MAX_VALUE / 2), decimal(Double.MAX_VALUE));
		assertSmaller(decimal(Double.MIN_VALUE / 2), decimal(Double.MIN_VALUE));
		assertSmaller("0", decimal(Double.MIN_VALUE));

		assertGreater(decimal(-Double.MAX_VALUE / 2), decimal(-Double.MAX_VALUE));
		assertGreater(decimal(-Double.MIN_VALUE / 2), decimal(-Double.MIN_VALUE));
		assertGreater("0", decimal(-Double.MIN_VALUE));
		
		assertGreater("" + Double.NaN, "Na");
		assertSmaller("" + Double.NaN, "NaNa");
		assertGreater("ReallyNotANumber", "ReallyNotANumbaaaaaaaaaa");
		assertGreater("2.00ReallyNotANumber", "2.00ReallyNotANumbaaaaaaaaaa");
	}
	@Test
	public void testLargeIntegers() {
		assertSmaller("873948723948192384791283491283749812374981273948170984320548739845", "873948723948192384791283491283749812374981273948170984320548739846");
		assertSmaller("-873948723948192384791283491283749812374981273948170984320548739845", "-873948723948192384791283491283749812374981273948170984320548739844");
	}
	
	@Test
	public void testLargeDecimals() {
		assertSmaller("00049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845", "049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739846");
		assertSmaller("-0049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845", "-049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739844");
	}

	@Test
	public void testIntegersWithThousandsSeparators() {
		assertGreater("1,000,005", "1000004");
		assertSmaller("1,000,005", "1000006");
		assertGreater("1,0,0,0,0,0,5", "1,000,004");
		assertSmaller("1,0,0,0,0,0,5", "1,000,006");
		assertGreater(",1,0,0,0,0,0,5,", "1,000,004");
		assertSmaller(",1,0,0,0,0,0,5,", "1,000,006");
		assertSmaller("1,,0,0,0,0,0,5", "1,000,004");//no longer treated as a number
		assertSmaller("1,,0,0,0,0,0,5", "1,000,006");//no longer treated as a number
	}
	@Test
	public void testDecimalsWithThousandsSeparators() {
		assertGreater("1,000,000.001", "1000000.0001");
		assertSmaller("1,000,000.001", "1000000.0011");
		assertGreater("1,0,0,0,000.001", "1,000,000.0001");
		assertSmaller("1,0,0,0,000.001", "1,000,000.0011");
		assertGreater("1,0,0,0,000.000001", "1,000,000.0000001");
		assertSmaller("1,0,0,0,000.000001", "1,000,000.0000011");
		assertSmaller("1,0,0,0,000.000,001", "1,000,000.0000001");//no longer treated as a number
		assertSmaller("1,0,0,0,000.000,001", "1,000,000.0000011");//no longer treated as a number
	}
}
