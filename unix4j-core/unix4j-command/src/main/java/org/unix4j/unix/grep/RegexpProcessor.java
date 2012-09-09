package org.unix4j.unix.grep;

import java.util.regex.Pattern;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

final class RegexpProcessor extends AbstractGrepProcessor {
	
	private final Pattern pattern;

	public RegexpProcessor(GrepCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		final GrepArguments args = getArguments();
		final String regex;
		if (args.isWholeLine()) {
			regex = args.getPattern();
		} else {
			regex = ".*" + args.getPattern() + ".*";
		}
		this.pattern = Pattern.compile(regex, args.isIgnoreCase() ? Pattern.CASE_INSENSITIVE : 0);
	}

	@Override
	public boolean matches(Line line) {
		// NOTE: we use content here because . does not match line
		// ending characters, see {@link Pattern#DOTALL}
		boolean matches = pattern.matcher(line.getContent()).matches();
		return matches;
	}
}