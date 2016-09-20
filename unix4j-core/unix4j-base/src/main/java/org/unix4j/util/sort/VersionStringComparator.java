package org.unix4j.util.sort;

import org.unix4j.util.StringUtil;

import java.util.Comparator;

/**
 * A comparator for version strings similar to "V1.34.123" consisting of optional blanks.
 * The characters '.', '-' and ':' are treated as group delimiters. Each group is sorted
 * numerically if possible. If two values are non-numeric, the string are compared instead.
 * If one value is non-numeric, it comes first.
 */
public class VersionStringComparator implements Comparator<CharSequence> {

	/**
	 * The singelton instance.
	 */
	public static final VersionStringComparator INSTANCE = new VersionStringComparator();

	/**
	 * Private constructor used to create the singleton {@link #INSTANCE}.
	 */
	private VersionStringComparator() {
		super();
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		final int start1 = StringUtil.findStartTrimWhitespace(s1);
		final int end1 = StringUtil.findEndTrimWhitespace(s1);
		final int start2 = StringUtil.findStartTrimWhitespace(s2);
		final int end2 = StringUtil.findEndTrimWhitespace(s2);
		int grpStart1 = start1;
		int grpStart2 = start2;
		int grpEnd1 = findGroupEnd(s1, start1, end1);
		int grpEnd2= findGroupEnd(s2, start2, end2);
		while (grpStart1 < grpEnd1 && grpStart2 < grpEnd2) {
			final char ch1 = s1.charAt(grpStart1);
			final char ch2 = s2.charAt(grpStart2);
			int cmp;
			if (Character.isDigit(ch1) && Character.isDigit(ch2)) {
				cmp = compareGroupNumerically(s1, grpStart1, grpEnd1, s2, grpStart2, grpEnd2);
			} else {
				cmp = compareGroupLiterally(s1, grpStart1, grpEnd1, s2, grpStart2, grpEnd2);
			}
			if (cmp != 0) {
				return cmp;
			}
			grpStart1 = grpEnd1;
			grpStart2 = grpEnd2;
			grpEnd1 = findGroupEnd(s1, grpStart1, end1);
			grpEnd2= findGroupEnd(s2, grpStart2, end2);
		}
		return grpStart1 < grpEnd1 ? 1 : grpStart2 < grpEnd2 ? -1 : 0;
	}

	//PREDONDITION: end1 > start1 && end2 > start2
	private static int compareGroupNumerically(CharSequence s1, int start1, int end1,
											   CharSequence s2, int start2, int end2) {
		final int len1 = end1 - start1;
		final int len2 = end2 - start2;
		final int maxlen = Math.max(len1, len2);
		final int pad1 = maxlen - len1;
		final int pad2 = maxlen - len2;
		int cmp = 0;
		for (int i = 0; i < maxlen; i++) {
			final char ch1 = i < pad1 ? '0' : s1.charAt(start1 + i - pad1);
			final char ch2 = i < pad2 ? '0' : s2.charAt(start2 + i - pad2);
			if (cmp == 0) {
				cmp = ch1 < ch2 ? -1 : ch1 > ch2 ? 1 : 0;
			}
		}
		if (cmp == 0) {
			//e.g. s1="123" < s2="0123"
			return pad1 < pad2 ? -1 : pad1 > pad2 ? 1 : 0;
		}
		return cmp;
	}

	private static int compareGroupLiterally(CharSequence s1, int start1, int end1,
											 CharSequence s2, int start2, int end2) {
		final int len1 = end1 - start1;
		final int len2 = end2 - start2;
		final int minlen = Math.min(len1, len2);
		for (int i = 0; i < minlen; i++) {
			final char ch1 = s1.charAt(start1 + i);
			final char ch2 = s2.charAt(start2 + i);
			if (ch1 < ch2) return -1;
			if (ch1 > ch2) return +1;
		}
		//shorter string comes first
		return len1 > minlen ? 1 : len2 > minlen ? -1 : 0;
	}

	private static int findGroupEnd(CharSequence s, int start, int end) {
		Boolean isNumeric = null;
		for (int i = start; i < end; i++) {
			final boolean isDigit = Character.isDigit(s.charAt(i));
			if (isNumeric == null) {
				isNumeric = isDigit;
			} else {
				if (isNumeric.booleanValue() != isDigit) {
					return i;
				}
			}
		}
		return end;
	}
}