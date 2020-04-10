package org.unix4j.util;

import org.junit.Test;
import org.unix4j.util.sort.MonthStringComparator;

import java.util.Comparator;
import java.util.Locale;

/**
 * Unit test for {@link MonthStringComparator}.
 */
public class MonthStringComparatorTest extends AbstractStringComparatorTest {

	@Override
	protected Comparator<? super String> initComparator() {
		return MonthStringComparator.getInstance(Locale.US);
	}

	@Test
	public void testEqual() {
		assertEqual("Jan", "Jan");
		assertEqual("January", "Jan");
		assertEqual("January", "JanUARy");
		assertEqual("january", "JAN");
		assertEqual("Feb", "Feb");
		assertEqual("February", "Feb");
		assertEqual("Mar", "March");
		assertEqual("Apr", "April");
		assertEqual("May", "MAy");
		assertEqual("Jun", "JUNE");
		assertEqual("Jul", "JULY");
		assertEqual("Aug", "AUGUST");
		assertEqual("Sep", "September");
		assertEqual("Oct", "October");
		assertEqual("Nov", "November");
		assertEqual("Dec", "December");
	}

	@Test
	public void testCompareUS() {
		final String[] lMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		final String[] sMonths = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
		for (int i = 0; i < 12; i++) {
			assertEqual(lMonths[i], lMonths[i]);
			assertEqual(sMonths[i], sMonths[i]);
			assertEqual(lMonths[i], sMonths[i]);
			assertGreater(lMonths[i], "ABC");
			assertGreater(lMonths[i], "ZZZ");
			assertGreater(sMonths[i], "ABC");
			assertGreater(sMonths[i], "ZZZ");
			for (int j = 0; j < 12; j++) {
				if (i < j) {
					assertSmaller(lMonths[i], sMonths[j]);
				} else if (i > j) {
					assertGreater(lMonths[i], sMonths[j]);
				} else {
					assertEqual(lMonths[i], sMonths[j]);
				}
			}
		}
	}

	@Test
	public void testCompareGerman() {
		final int EQUAL = 0;
		final int GREATER = 1;
		final MonthStringComparator c = MonthStringComparator.getInstance(Locale.GERMAN);
		final String[] lMonths = {"Januar", "Februar", "M\u00C4rz", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
		final String[] sMonths = {"JAN", "FEB", "M\u00E4R", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"};
		for (int i = 0; i < 12; i++) {
			assertCompare(c, lMonths[i], lMonths[i], EQUAL);
			assertCompare(c, sMonths[i], sMonths[i], EQUAL);
			assertCompare(c, lMonths[i], sMonths[i], EQUAL);
			assertCompare(c, lMonths[i], "ABC", GREATER);
			assertCompare(c, lMonths[i], "ZZZ", GREATER);
			assertCompare(c, sMonths[i], "ABC", GREATER);
			assertCompare(c, sMonths[i], "ZZZ", GREATER);
			for (int j = 0; j < 12; j++) {
				assertCompare(c, lMonths[i], sMonths[j], Integer.compare(i, j));
			}
		}
	}
}
