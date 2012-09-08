package org.unix4j.unix.uniq;


import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Ls;
import org.unix4j.util.FileUtil;

/**
 * Uniq command implementation.
 */
class UniqCommand extends AbstractCommand<UniqArguments> {
	public UniqCommand(UniqArguments arguments) {
		super(Ls.NAME, arguments);
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		final UniqArguments args = getArguments();
		final LineProcessor standardInputProcessor; 
		if (args.isGlobal()) {
			if (args.isUniqueOnly()) {
				standardInputProcessor = new GlobalProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				standardInputProcessor = new GlobalProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				standardInputProcessor = new GlobalProcessor.Count(this, context, output);
			} else {
				standardInputProcessor = new GlobalProcessor.Normal(this, context, output);
			}
		} else {
			if (args.isUniqueOnly()) {
				standardInputProcessor = new AdjacentProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				standardInputProcessor = new AdjacentProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				standardInputProcessor = new AdjacentProcessor.Count(this, context, output);
			} else {
				standardInputProcessor = new AdjacentProcessor.Normal(this, context, output);
			}
		}
		
		//input from file?
		if (args.isFileSet()) {
			final Input input = new FileInput(args.getFile());
			return new RedirectInputLineProcessor(input, standardInputProcessor);
		} else if (args.isPathSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPath());
			final List<FileInput> inputs = FileInput.multiple(files);
			return new RedirectInputLineProcessor(inputs, standardInputProcessor);
		}
		
		//no, from standard input
		return standardInputProcessor;
	}

	protected static String formatCount(long count, int maxDigitsForCount) {
		final StringBuilder sb = new StringBuilder(maxDigitsForCount);
		sb.append(count);
		while (sb.length() < maxDigitsForCount) {
			sb.insert(0, ' ');
		}
		return sb.toString();
	}

	protected static int writeCountLine(Line line, long count, int maxDigitsForCount, LineProcessor output) {
		final String countString = UniqCommand.formatCount(count, maxDigitsForCount);
		final Line outputLine = new SimpleLine(" " + countString + " " + line.getContent(), line.getLineEnding());
		output.processLine(outputLine);
		return countString.length();
	}
}