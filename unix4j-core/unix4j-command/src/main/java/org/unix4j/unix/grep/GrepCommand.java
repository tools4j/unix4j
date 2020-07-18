package org.unix4j.unix.grep;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.io.NullInput;
import org.unix4j.processor.*;
import org.unix4j.unix.Grep;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Implementation of the {@link Grep grep} command.
 */
class GrepCommand extends AbstractCommand<GrepArguments> {
	public GrepCommand(GrepArguments arguments) {
		super(Grep.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final GrepArguments args = getArguments(context);

		//from file?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return getFileInputProcessor(inputs, context, output, args);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output, args);
		} else if (args.isStreamsSet()) {
		    final List<FileInput> inputs = FileInput.multiple(args.getStreams());
		    return getFileInputProcessor(inputs, context, output, args);
		}

		//from standard input
		return getStandardInputProcessor(context, output, args);
	}

	private LineMatcher getMatcher(GrepArguments args) {
		final LineMatcher matcher;
		if (args.isFixedStrings()) {
			if (args.isWholeLine()) {
				matcher = args.isIgnoreCase() ? new FixedStringMatcher.WholeLineIgnoreCase(args) : new FixedStringMatcher.WholeLine(args);
			} else {
				matcher = args.isIgnoreCase() ? new FixedStringMatcher.IgnoreCase(args) : new FixedStringMatcher.Standard(args);
			}
		} else {
			matcher = new RegexpMatcher(args);
		}
		return args.isInvertMatch() ? new InvertedMatcher(matcher) : matcher;
	}

	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output, GrepArguments args) {
		final LineMatcher matcher = getMatcher(args);
		if (args.isCount()) {
			return new CountMatchingLinesInputProcessor(this, context, output, matcher);
		} else if (args.isMatchingFiles()) {
			return new WriteFilesWithMatchingLinesProcessor(this, context, output, matcher);
		} else if (args.isLineNumber()) {
			return new WriteMatchingLinesWithLineNumberProcessor(this, context, output, matcher);
		}
		return new WriteMatchingLinesProcessor(this, context, output, matcher);
	}

	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output, GrepArguments args) {
		switch (inputs.size()) {
			case 0:
				return getInputProcessor(NullInput.INSTANCE, context, output, args);
			case 1:
				return getInputProcessor(inputs.get(0), context, output, args);
			default:
				return getMultipleFilesInputProcessor(inputs, context, output, args);
		}
	}

	private LineProcessor getInputProcessor(Input input, ExecutionContext context, LineProcessor output, GrepArguments args) {
		if (args.isCount()) {
			final LineMatcher matcher = getMatcher(args);
			final LineProcessor lineProcessor = new CountMatchingLinesProcessor(this, context, output, matcher);
			return new InputLineProcessor(input, new DefaultInputProcessor(), lineProcessor);
		} else if (args.isMatchingFiles()) {
			final LineMatcher matcher = getMatcher(args);
			final InputProcessor inputProcessor = new WriteFilesWithMatchingLinesProcessor(this, context, output, matcher);
			return new InputLineProcessor(input, inputProcessor, output);
		} else if (args.isLineNumber()) {
			final LineMatcher matcher = getMatcher(args);
			final LineProcessor lineProcessor = new WriteMatchingLinesWithLineNumberProcessor(this, context, output, matcher);
			return new InputLineProcessor(input, new DefaultInputProcessor(), lineProcessor);
		} else {
			final LineMatcher matcher = getMatcher(args);
			final LineProcessor lineProcessor = new WriteMatchingLinesProcessor(this, context, output, matcher);
			return new InputLineProcessor(input, new DefaultInputProcessor(), lineProcessor);
		}
	}

	private LineProcessor getMultipleFilesInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output, GrepArguments args) {
		if (args.isCount()) {
			final LineMatcher matcher = getMatcher(args);
			final InputProcessor inputProcessor = new CountMatchingLinesInputProcessor(this, context, output, matcher);
			return new MultipleInputLineProcessor(inputs, inputProcessor, output);
		} else if (args.isMatchingFiles()) {
			final LineMatcher matcher = getMatcher(args);
			final InputProcessor inputProcessor = new WriteFilesWithMatchingLinesProcessor(this, context, output, matcher);
			return new MultipleInputLineProcessor(inputs, inputProcessor, output);
		} else if (args.isLineNumber()) {
			final LineMatcher matcher = getMatcher(args);
			final LineProcessor inputProcessor = new WriteMatchingLinesProcessor(this, context, output, matcher);
			return new MultipleInputLineProcessor(inputs,
					new WriteMatchingLinesInputProcessor(this, context, matcher), inputProcessor);
		} else {
			final LineMatcher matcher = getMatcher(args);
			final InputProcessor inputProcessor = new WriteMatchingLinesInputProcessor(this, context, matcher);
			return new MultipleInputLineProcessor(inputs, inputProcessor, output);
		}
	}
}
