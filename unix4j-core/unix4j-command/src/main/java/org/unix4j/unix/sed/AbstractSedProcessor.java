package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;

abstract class AbstractSedProcessor implements LineProcessor {
	protected final SedArguments args;
	protected final LineProcessor output;

	public AbstractSedProcessor(SedArguments args, LineProcessor output) {
		this.args = args;
		this.output = output;
	}

	@Override
	public void finish() {
		output.finish();
	}
	protected static String getRegexp(SedArguments args) {
		if (args.isRegexpSet()) {
			return args.getRegexp();
		}
		if (args.isString1Set()) {
			return args.getString1();
		}
		return "";
	}
	protected static String getReplacement(SedArguments args) {
		if (args.isReplacementSet()) {
			return args.getReplacement();
		}
		if (args.isString2Set()) {
			return args.getString2();
		}
		return "";
	}
}