package org.unix4j.unix.sort;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Sort;

class SortCommand extends AbstractCommand<SortArguments> {

	public SortCommand(SortArguments arguments) {
		super(Sort.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final SortArguments args = getArguments();
		if (args.isMerge()) {
			return new MergeProcessor(this, context, output);
		} else if (args.isCheck()) {
			return new CheckProcessor(this, context, output);
		}
		
		//normal sort
		if (args.isUnique()) {
			return new UniqueSortProcessor(this, context, output);
		} else {
			return new SortProcessor(this, context, output);
		}
	}

}
