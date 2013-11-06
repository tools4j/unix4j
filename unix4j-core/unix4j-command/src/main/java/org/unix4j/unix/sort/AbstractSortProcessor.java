package org.unix4j.unix.sort;

import java.util.Comparator;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.sort.DecimalNumberStringComparator;
import org.unix4j.util.sort.LineComparator;
import org.unix4j.util.sort.ReverseOrderComparator;
import org.unix4j.util.sort.ScientificNumberStringComparator;

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
			if (args.isNumericSort()) {
				comparator = DecimalNumberStringComparator.getInstance(getContext().getLocale());
			} else if (args.isGeneralNumericSort()) {
				comparator = ScientificNumberStringComparator.INSTANCE;
			} else if (args.isHumanNumericSort()) {
				throw new RuntimeException("option " + SortOption.humanNumericSort + " is not implemented");
			} else if (args.isDictionaryOrder()) {
				throw new RuntimeException("option " + SortOption.dictionaryOrder + " is not implemented");
			} else if (args.isMonthSort()) {
				throw new RuntimeException("option " + SortOption.monthSort + " is not implemented");
			} else if (args.isVersionSort()) {
				throw new RuntimeException("option " + SortOption.versionSort + " is not implemented");
			} else { 
				if (args.isIgnoreLeadingBlanks()) {
					comparator = args.isIgnoreCase() ? LineComparator.COLLATOR_IGNORE_CASE_AND_LEADING_BLANKS : LineComparator.COLLATOR_IGNORE_LEADING_BLANKS;
				} else {
					comparator = args.isIgnoreCase() ? LineComparator.COLLATOR_IGNORE_CASE : LineComparator.COLLATOR;
				}
			}
		}
		if (args.isReverse()) {
			return ReverseOrderComparator.reverse(comparator);
		}
		return comparator;
	}

}
