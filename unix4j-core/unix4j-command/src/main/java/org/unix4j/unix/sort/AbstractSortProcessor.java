package org.unix4j.unix.sort;

import java.util.Comparator;

import org.unix4j.command.AbstractLineProcessor;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.util.LineComparator;
import org.unix4j.util.ReverseOrderComparator;

abstract class AbstractSortProcessor extends AbstractLineProcessor<SortArguments> {
	
	private final Comparator<? super Line> comparator;

	public AbstractSortProcessor(SortCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		this.comparator = initComparator();
	}
	
	protected Comparator<? super Line> getComparator() {
		return comparator;
	}
	
	private Comparator<? super Line> initComparator() {
		final SortArguments args = getArguments();
		final Comparator<? super Line> comparator;
		if (args.isComparatorSet()) {
			comparator = args.getComparator();
		} else {
			if (args.isIgnoreLeadingBlanks()) {
				comparator = args.isIgnoreCase() ? LineComparator.COLLATOR_IGNORE_CASE_AND_LEADING_BLANKS : LineComparator.COLLATOR_IGNORE_LEADING_BLANKS;
			} else {
				comparator = args.isIgnoreCase() ? LineComparator.COLLATOR_IGNORE_CASE : LineComparator.COLLATOR;
			}
		}
		if (args.isReverse()) {
			return ReverseOrderComparator.reverse(comparator);
		}
		return comparator;
	}

}
