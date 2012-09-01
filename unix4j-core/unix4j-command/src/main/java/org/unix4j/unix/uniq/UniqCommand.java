package org.unix4j.unix.uniq;


import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Ls;

/**
 * Uniq command implementation.
 */
class UniqCommand extends AbstractCommand<UniqArguments> {
	public UniqCommand(UniqArguments arguments) {
		super(Ls.NAME, arguments);
	}

	@Override
	public UniqCommand withArgs(UniqArguments arguments) {
		return new UniqCommand(arguments);
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		final UniqArguments args = getArguments();
		if (args.isGlobal()) {
			if (args.isUniqueOnly()) {
				return new GlobalLineProcessor.UniqueOnly(args, context, output);
			} else if (args.isDuplicatedOnly()) {
				return new GlobalLineProcessor.DuplicateOnly(args, context, output);
			} else if (args.isCount()) {
				return new GlobalLineProcessor.Count(args, context, output);
			}
			return new GlobalLineProcessor.Normal(args, context, output);
		} else {
			if (args.isUniqueOnly()) {
				return new AdjacentLineProcessor.UniqueOnly(args, context, output);
			} else if (args.isDuplicatedOnly()) {
				return new AdjacentLineProcessor.DuplicateOnly(args, context, output);
			} else if (args.isCount()) {
				return new AdjacentLineProcessor.Count(args, context, output);
			}
			return new AdjacentLineProcessor.Normal(args, context, output);
		}
	}

	static String formatCount(long count, int maxDigitsForCount) {
		final StringBuilder sb = new StringBuilder(maxDigitsForCount);
		sb.append(count);
		while (sb.length() < maxDigitsForCount) {
			sb.insert(0, ' ');
		}
		return sb.toString();
	}

}