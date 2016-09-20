package org.unix4j.unix.sort;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.sort.*;

import java.util.Comparator;

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
				comparator = UnitsNumberStringComparator.getInstance(getContext().getLocale());
			} else if (args.isMonthSort()) {
				comparator = MonthStringComparator.getInstance(getContext().getLocale());
			} else if (args.isVersionSort()) {
				comparator = VersionStringComparator.INSTANCE;
			} else {
				comparator = new LineComparator(args.isIgnoreCase(), args.isIgnoreLeadingBlanks(), args.isDictionaryOrder());
			}
		}
		if (args.isReverse()) {
			return ReverseOrderComparator.reverse(comparator);
		}
		return comparator;
	}

}
