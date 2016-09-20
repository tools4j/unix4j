package org.unix4j.util;

import org.junit.Test;
import org.unix4j.util.sort.UnitsNumberStringComparator;

import java.util.Comparator;
import java.util.Locale;

/**
 * Unit test for {@link UnitsNumberStringComparator}.
 */
public class UnitsNumberStringComparatorTest extends DecimalNumberStringComparatorTest {

	private static final String SI_CHARS = "KMGTPEZY";

	@Override
	protected Comparator<? super String> initComparator() {
		return UnitsNumberStringComparator.getInstance(Locale.US);
	}

	@Test
	public void testAllFromMinusToPlusHundred() {
		for (int si1Val = 0; si1Val < SI_CHARS.length(); si1Val++) {
			final char si1 = SI_CHARS.charAt(si1Val);
			for (int si2Val = 0; si2Val < SI_CHARS.length(); si2Val++) {
				final char si2 = SI_CHARS.charAt(si2Val);
				for (int num1 = -100; num1 < 100; num1++) {
					for (int num2 = -100; num2 < 100; num2++) {
						final String val1 = "" + num1 + si1;
						final String val2 = "" + num2 + si2;
						final int sign1 = num1 < 0 ? -1 : 1;
						final int sign2 = num2 < 0 ? -1 : 1;
						if (si1Val ==si2Val && num1 == num2) {
							assertEqual(val1, val2);
						} else if (sign1 != sign2) {
							assertCompare(val1, val2, Integer.compare(num1, num2));
						} else if (si1Val != si2Val) {
							assertCompare(val1, val2, Integer.compare(si1Val*sign1, si2Val*sign2));
						} else {
							assertCompare(val1, val2, Integer.compare(num1, num2));
						}
					}
				}
			}
		}
	}

	@Test
	public void testZeroNonEqualWithUnit() {
		assertSmaller("0", "0K");
		assertSmaller("0K", "0M");
		assertSmaller("0M", "0G");
		assertSmaller("0G", "0T");
		assertSmaller("0T", "0P");
		assertGreater("-0", "-0K");
		assertGreater("-0M", "-0G");
		assertGreater("-0G", "-0T");
		assertGreater("-0T", "-0P");
	}

	@Test
	public void testPositiveNegativeZeroWithUnit() {
		assertEqual("-0k", "0K");
		assertEqual("-0k", "0.K");
		assertEqual("-0M", "0.0M");
		assertEqual("-0G", ".0G");

		assertEqual("-0.T", "0T");
		assertEqual("-0.P", "0.P");
		assertEqual("-0.E", "0.0E");
		assertEqual("-0.Z", ".0Z");

		assertEqual("-0.0Y", "0Y");
		assertEqual("-0.0M", "0.G");
		assertEqual("-0.0G", "0.0M");
		assertEqual("-0.0k", ".0K");
	}

	@Test
	public void testLargeIntegersWithUnit() {
		assertSmaller("873948723948192384791283491283749812374981273948170984320548739845K", "873948723948192384791283491283749812374981273948170984320548739846K");
		assertSmaller("873948723948192384791283491283749812374981273948170984320548739845K", "873948723948192384791283491283749812374981273948170984320548739844M");
		assertSmaller("-873948723948192384791283491283749812374981273948170984320548739845G", "-873948723948192384791283491283749812374981273948170984320548739844G");
		assertSmaller("-873948723948192384791283491283749812374981273948170984320548739845T", "-873948723948192384791283491283749812374981273948170984320548739846G");
	}
	
	@Test
	public void testLargeDecimalsWithUnit() {
		assertSmaller("00049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845Y", "049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739846Y");
		assertSmaller("-0049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845E", "-049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739844E");
		assertSmaller("00049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845", "049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845k");
		assertSmaller("-0049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845k", "-049502948350938450983540698094560934506983405968.873948723948192384791283491283749812374981273948170984320548739845");
	}

	@Test
	public void testIntegersWithThousandsSeparatorsWithUnit() {
		assertGreater("1,000,005M", "1000004M");
		assertSmaller("1,000,005K", "1000004M");
		assertSmaller("1,000,005k", "1000006k");
		assertGreater("1,0,0,0,0,0,5k", "1,000,004k");
		assertSmaller("1,0,0,0,0,0,5k", "1,000,006k");
		assertGreater(",1,0,0,0,0,0,5,k", "1,000,004k");
		assertSmaller(",1,0,0,0,0,0,5,k", "1,000,006k");
		assertSmaller("1,,0,0,0,0,0,5k", "1,000,004k");//no longer treated as a number
		assertSmaller("1,,0,0,0,0,0,5k", "1,000,006k");//no longer treated as a number
	}
	@Test
	public void testDecimalsWithThousandsSeparatorsWithUnit() {
		assertGreater("1,000,000.001k", "1000000.0001k");
		assertSmaller("1,000,000.001k", "1000000.0011k");
		assertGreater("1,0,0,0,000.001k", "1,000,000.0001k");
		assertSmaller("1,0,0,0,000.001k", "1,000,000.0011k");
		assertGreater("1,0,0,0,000.000001k", "1,000,000.0000001k");
		assertSmaller("1,0,0,0,000.000001k", "1,000,000.0000011k");
		assertSmaller("1,0,0,0,000.000,001k", "1,000,000.0000001k");//no longer treated as a number
		assertSmaller("1,0,0,0,000.000,001k", "1,000,000.0000011k");//no longer treated as a number
	}
}
