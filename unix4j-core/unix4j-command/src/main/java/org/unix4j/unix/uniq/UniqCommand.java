package org.unix4j.unix.uniq;


import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.command.InputArgumentLineProcessor;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.line.SimpleLine;
import org.unix4j.unix.Ls;

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
		final LineProcessor processor; 
		if (args.isGlobal()) {
			if (args.isUniqueOnly()) {
				processor = new GlobalProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				processor = new GlobalProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				processor = new GlobalProcessor.Count(this, context, output);
			} else {
				processor = new GlobalProcessor.Normal(this, context, output);
			}
		} else {
			if (args.isUniqueOnly()) {
				processor = new AdjacentProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				processor = new AdjacentProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				processor = new AdjacentProcessor.Count(this, context, output);
			} else {
				processor = new AdjacentProcessor.Normal(this, context, output);
			}
		}
		
		//input from file?
		if (args.isFileSet()) {
			final Input input = new FileInput(args.getFile());
			return new InputArgumentLineProcessor(input, processor);
		} else if (args.isPathSet()) {
			final Input input = new FileInput(args.getFile());
			return new InputArgumentLineProcessor(input, processor);
		}
		
		//no, from standard input
		return processor;
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