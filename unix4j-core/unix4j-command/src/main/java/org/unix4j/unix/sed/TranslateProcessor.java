package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

class TranslateProcessor extends AbstractSedProcessor {
	private final String source;
	private final String destination;
	public TranslateProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
		this.source = args.getString1();
		this.destination = args.getString2();
	}

	@Override
	public boolean processLine(Line line) {
		throw new RuntimeException("not implemented");
	}
}