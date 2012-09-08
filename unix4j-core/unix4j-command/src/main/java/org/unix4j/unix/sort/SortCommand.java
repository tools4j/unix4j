package org.unix4j.unix.sort;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Sort;
import org.unix4j.util.FileUtil;

class SortCommand extends AbstractCommand<SortArguments> {

	public SortCommand(SortArguments arguments) {
		super(Sort.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final SortArguments args = getArguments();
		
		final LineProcessor standardInputProcessor;
		if (args.isMerge()) {
			standardInputProcessor = new MergeProcessor(this, context, output);
		} else if (args.isCheck()) {
			standardInputProcessor = new CheckProcessor(this, context, output);
		} else {
			//normal sort
			if (args.isUnique()) {
				standardInputProcessor = new UniqueSortProcessor(this, context, output);
			} else {
				standardInputProcessor = new SortProcessor(this, context, output);
			}
			
			//input from file?
			if (args.isFilesSet()) {
				final List<FileInput> inputs = FileInput.multiple(args.getFiles());
				return new RedirectInputLineProcessor(inputs, standardInputProcessor);
			} else if (args.isPathsSet()) {
				final List<File> files = FileUtil.expandFiles(args.getPaths());
				final List<FileInput> inputs = FileInput.multiple(files);
				return new RedirectInputLineProcessor(inputs, standardInputProcessor);
			}
		}
		return standardInputProcessor;
	}

}
