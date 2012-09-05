package org.unix4j.util;

import java.text.Collator;
import java.util.Comparator;

import org.unix4j.line.Line;
import org.unix4j.util.TrimBlanksStringComparator.Mode;

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
	private LineComparator(Comparator<? super String> comparator) {
		this.comparator = comparator;
	}

	@Override
	public int compare(Line line1, Line line2) {
		return comparator.compare(line1.getContent(), line2.getContent());
	}
	
	/**
	 * Line comparator using case sensitive comparison based on the current
	 * local's collation order.
	 */
	public static final LineComparator COLLATOR = new LineComparator(getCollator(false, false));

	/**
	 * Line comparator using case insensitive comparison based on the current
	 * local's collation order.
	 */
	public static final LineComparator COLLATOR_IGNORE_CASE = new LineComparator(getCollator(true, false));

	/**
	 * Line comparator using case sensitive comparison based on the current
	 * local's collation order, ignoring leading blanks (spaces and tabs) in the
	 * content string of the line.
	 */
	public static final LineComparator COLLATOR_IGNORE_LEADING_BLANKS = new LineComparator(getCollator(false, true));

	/**
	 * Line comparator using case insensitive comparison based on the current
	 * local's collation order, ignoring leading blanks (spaces and tabs) in the
	 * content string of the line.
	 */
	public static final LineComparator COLLATOR_IGNORE_CASE_AND_LEADING_BLANKS = new LineComparator(getCollator(true, true));

	private static Comparator<? super String> getCollator(boolean ignoreCase, boolean ignoreLeadingBlanks) {
		final Collator collator = Collator.getInstance();
		if (ignoreCase) {
			collator.setStrength(Collator.SECONDARY);
		}
		if (ignoreLeadingBlanks) {
			return new TrimBlanksStringComparator(Mode.Leading, collator);
		}
		return collator;
	};
}
