package org.unix4j.unix.uniq;


import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
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
		if (args.isGlobal()) {
			if (args.isUniqueOnly()) {
				return new GlobalProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				return new GlobalProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				return new GlobalProcessor.Count(this, context, output);
			}
			return new GlobalProcessor.Normal(this, context, output);
		} else {
			if (args.isUniqueOnly()) {
				return new AdjacentProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				return new AdjacentProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				return new AdjacentProcessor.Count(this, context, output);
			}
			return new AdjacentProcessor.Normal(this, context, output);
		}
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