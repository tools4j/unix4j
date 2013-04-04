package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;

abstract class AbstractRegexpProcessor extends AbstractSedProcessor {
	protected final String regexp;

	public AbstractRegexpProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
		this.regexp = args.getRegexp();
	}
}