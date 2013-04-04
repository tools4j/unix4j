package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;

abstract class AbstractTextProcessor extends AbstractSedProcessor {
	protected final String regexp;
	protected final String text;

	public AbstractTextProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
		this.regexp = args.getString1();
		this.text = args.getString2();
	}
}