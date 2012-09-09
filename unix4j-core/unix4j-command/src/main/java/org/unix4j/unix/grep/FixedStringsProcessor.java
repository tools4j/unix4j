package org.unix4j.unix.grep;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

final class FixedStringsProcessor extends AbstractGrepProcessor {
	
	private final String pattern;

	public FixedStringsProcessor(GrepCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		final GrepArguments args = getArguments();
		if (args.isIgnoreCase()) {
			this.pattern = args.getPattern().toLowerCase();
		} else {
			this.pattern = args.getPattern();
		}
	}

	@Override
	public boolean matches(Line line) {
		if (getArguments().isIgnoreCase()) {
			return line.toString().toLowerCase().contains(pattern);
		} else {
			return line.toString().contains(pattern);
		}
	}
}