package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

class TranslateProcessor extends AbstractSedProcessor {
	private final String source;
	private final String destination;
	public TranslateProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
		this.source = args.getString1();
		this.destination = args.getString2();
	}
	public TranslateProcessor(Command command, String script, SedArguments args, LineProcessor output) {
		this(command, deriveArgs(command, script, args), output);
	}

	private static SedArguments deriveArgs(Command command, String script, SedArguments args) {
		final int start = StringUtil.findStartTrimWhitespace(script);
		final int mid = indexOfNextDelimiter(script, start);
		final int end = indexOfNextDelimiter(script, mid);
		if (mid < 0 || end < 0) {
			throw new IllegalArgumentException("invalid script for sed " + command + " command: " + script);
		}
		throw new RuntimeException("not implemented");
	}
	@Override
	public boolean processLine(Line line) {
		throw new RuntimeException("not implemented");
	}
}