package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

class PrintProcessor extends AbstractSedProcessor {
	private final String regexp;
	public PrintProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
		this.regexp = getRegexp(args);
	}

	@Override
	public boolean processLine(Line line) {
		if (!args.isQuiet()) {
			if (!output.processLine(line)) {
				return false;
			}
		}
		if (line.toString().matches(regexp)) {
			return output.processLine(line);
		}
		return true;
	}
}