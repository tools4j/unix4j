package org.unix4j.unix.grep;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

final class FixedStringsProcessor extends AbstractGrepProcessor {
	
	private final String pattern;

	public FixedStringsProcessor(GrepCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		final GrepArguments args = getArguments();
		if (args.isIgnoreCase() && !args.isWholeLine()) {
			this.pattern = args.getPattern().toLowerCase();
		} else {
			this.pattern = args.getPattern();
		}
	}

	@Override
	public boolean matches(Line line) {
		final GrepArguments args = getArguments();
		final String content = line.getContent();
		if (args.isWholeLine()) {
			if (args.isIgnoreCase()) {
				return content.equalsIgnoreCase(pattern);
			} else {
				return content.equals(pattern);
			}
		} else {
			if (args.isIgnoreCase()) {
				return content.toLowerCase().contains(pattern);
			} else {
				return content.contains(pattern);
			}
		}
	}
}