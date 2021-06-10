package org.unix4j.unix.sort;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.processor.*;
import org.unix4j.unix.Sort;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the {@link Sort sort} command.
 */
class SortCommand extends AbstractCommand<SortArguments> {

	public SortCommand(SortArguments arguments) {
		super(Sort.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final SortArguments args = getArguments(context);
		
		if (args.isMerge()) {
			return getMergeProcessor(context, output, args);
		} else if (args.isCheck()) {
			return getCheckProcessor(context, output, args);
		} else {
			return getSortProcessor(context, output, args);
		}
	}
	
	private LineProcessor getSortProcessor(ExecutionContext context, LineProcessor output, SortArguments args) {
		final LineProcessor standardInputProcessor;
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
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return new RedirectInputLineProcessor(inputs, standardInputProcessor);
		} else if (args.isInputsSet()) {
			final List<Input> inputs = Arrays.asList(args.getInputs());
			return new RedirectInputLineProcessor(inputs, standardInputProcessor);
		}
		return standardInputProcessor;
	}

	private LineProcessor getMergeProcessor(ExecutionContext context, LineProcessor output, SortArguments args) {
		//input from file?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return new MergeProcessor(this, context, output, inputs);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return new MergeProcessor(this, context, output, inputs);
		} else if (args.isInputsSet()) {
			final List<Input> inputs = Arrays.asList(args.getInputs());
			return new MergeProcessor(this, context, output, inputs);
		} else {
			final List<Input> empty = Collections.emptyList();
			return new MergeProcessor(this, context, output, empty);
		}
	}
	
	private LineProcessor getCheckProcessor(ExecutionContext context, LineProcessor output, SortArguments args) {
		final CheckProcessor standardInputProcessor = new CheckProcessor(this, context, output);
		//input from file?
		List<? extends Input> inputs = null;
		if (args.isFilesSet()) {
			inputs = FileInput.multiple(args.getFiles());
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
			inputs = FileInput.multiple(files);
		} else if (args.isInputsSet()) {
			inputs = Arrays.asList(args.getInputs());
		}
		if (inputs != null) {
			if (inputs.size() < 2) {
				return new RedirectInputLineProcessor(inputs, standardInputProcessor);
			}
			final InputProcessor resetPerFileProcessor = new DefaultInputProcessor() {
				@Override
				public void finish(Input input, LineProcessor output) {
					standardInputProcessor.reset();
				}
			};
			return new MultipleInputLineProcessor(inputs, resetPerFileProcessor, standardInputProcessor);
		}
		return standardInputProcessor;
	}

}
