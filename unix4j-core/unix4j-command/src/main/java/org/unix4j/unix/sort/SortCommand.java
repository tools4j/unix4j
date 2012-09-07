package org.unix4j.unix.sort;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.command.InputArgumentLineProcessor;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Sort;

class SortCommand extends AbstractCommand<SortArguments> {

	public SortCommand(SortArguments arguments) {
		super(Sort.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final SortArguments args = getArguments();
		
		final LineProcessor processor;
		if (args.isMerge()) {
			processor = new MergeProcessor(this, context, output);
		} else if (args.isCheck()) {
			processor = new CheckProcessor(this, context, output);
		} else {
			//normal sort
			if (args.isUnique()) {
				processor = new UniqueSortProcessor(this, context, output);
			} else {
				processor = new SortProcessor(this, context, output);
			}
			
			//input from file?
			if (args.isFilesSet()) {
				final Input input = FileInput.composite(args.getFiles());
				return new InputArgumentLineProcessor(input, processor);
			} else if (args.isPathsSet()) {
				final Input input = FileInput.composite(args.getPaths());
				return new InputArgumentLineProcessor(input, processor);
			}
		}
		return processor;
	}

}
