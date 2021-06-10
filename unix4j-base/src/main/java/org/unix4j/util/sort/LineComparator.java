package org.unix4j.util.sort;

import org.unix4j.line.Line;
import org.unix4j.util.sort.TrimBlanksStringComparator.Mode;

import java.text.Collator;
import java.util.Comparator;
import java.util.Objects;

/**
 * Comparator for a {@link Line} without line ending; forwards the comparison of
 * the content string to a delegate string comparator. Some constants exist for
 * common line comparators.
 */
public class LineComparator implements Comparator<Line> {

	private final Comparator<? super String> comparator;

	/**
	 * Constructor for a line comparator that uses the specified
	 * {@code comparator} to compare the {@link Line#getContent() content}
	 * string of the line.
	 * 
	 * @param comparator
	 *            the comparator used to compare the line content string
	 */
	public LineComparator(Comparator<? super String> comparator) {
		this.comparator = Objects.requireNonNull(comparator);
	}

	/**
	 * Constructor with options for line comparision.
	 *
	 * @param ignoreCase			true to ignore case, false for case sensitive comparison
	 * @param ignoreLeadingBlanks	true if leading blanks shall be trimmed before the comparison
	 * @param onlyDictionaryChars	true if only blanks and alphanumeric characters shall be considered
     */
	public LineComparator(boolean ignoreCase, boolean ignoreLeadingBlanks, boolean onlyDictionaryChars) {
		this(getCollator(ignoreCase, ignoreLeadingBlanks, onlyDictionaryChars));
	}

	@Override
	public int compare(Line line1, Line line2) {
		return comparator.compare(line1.getContent(), line2.getContent());
	}

	private static Comparator<? super String> getCollator(boolean ignoreCase, boolean ignoreLeadingBlanks, boolean onlyDictionaryChars) {
		final Collator collator = Collator.getInstance();
		collator.setStrength(ignoreCase ? Collator.SECONDARY : Math.max(Collator.TERTIARY, collator.getStrength()));
		final Comparator<? super String> comparator;
		if (ignoreLeadingBlanks) {
			comparator = new TrimBlanksStringComparator(Mode.Leading, collator);
		} else {
			comparator = collator;
		}
		if (onlyDictionaryChars) {
			return new DictionaryStringComparator(comparator);
		}
		return comparator;
	};
}
