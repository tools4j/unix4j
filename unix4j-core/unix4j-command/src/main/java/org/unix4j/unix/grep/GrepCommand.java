package org.unix4j.unix.grep;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Grep;

import java.util.regex.Pattern;

/**
 * User: ben
 */
public class GrepCommand extends AbstractCommand<GrepArguments> {
	public GrepCommand(GrepArguments arguments) {
		super(Grep.NAME, arguments);
	}

	@Override
	public GrepCommand withArgs(GrepArguments arguments) {
		return new GrepCommand(arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final GrepArguments args = getArguments();
		if (getArguments().isFixedStrings()) {
			return new FixedStringsProcessor(args, output);
		} else {
			return new RegexpProcessor(args, output);
		}
	}

	private static abstract class AbstractProcessor implements LineProcessor {
		protected final boolean isIgnoreCase;
		protected final boolean isInvert;
		protected final LineProcessor output;

		public AbstractProcessor(GrepArguments args, LineProcessor output) {
			this.isIgnoreCase = args.isIgnoreCase();
			this.isInvert = args.isInvert();
			this.output = output;
		}

		@Override
		public boolean processLine(Line line) {
			final boolean matches = matches(line);
			if (isInvert ^ matches) {
				return output.processLine(line);
			}
			return true;
		}

		@Override
		public void finish() {
			output.finish();
		}

		abstract protected boolean matches(Line line);
	}

	private static final class RegexpProcessor extends AbstractProcessor {
		private final Pattern pattern;

		public RegexpProcessor(GrepArguments args, LineProcessor output) {
			super(args, output);
			final String regex = ".*" + args.getPattern() + ".*";
			this.pattern = Pattern.compile(regex, isIgnoreCase ? Pattern.CASE_INSENSITIVE : 0);
		}

		@Override
		public boolean matches(Line line) {
			// NOTE: we use content here because . does not match line
			// ending characters, see {@link Pattern#DOTALL}
			boolean matches = pattern.matcher(line.getContent()).matches();
			return matches;
		}
	}

	private static final class FixedStringsProcessor extends AbstractProcessor {
		private final String pattern;

		public FixedStringsProcessor(GrepArguments args, LineProcessor output) {
			super(args, output);
			if (isIgnoreCase) {
				this.pattern = args.getPattern().toLowerCase();
			} else {
				this.pattern = args.getPattern();
			}
		}

		@Override
		public boolean matches(Line line) {
			if (isIgnoreCase) {
				return line.toString().toLowerCase().contains(pattern);
			} else {
				return line.toString().contains(pattern);
			}
		}
	}
}
