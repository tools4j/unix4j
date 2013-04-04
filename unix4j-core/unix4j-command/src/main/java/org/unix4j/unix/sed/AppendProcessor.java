package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;

class AppendProcessor extends AbstractTextProcessor {
	public AppendProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
	}

	@Override
	public boolean processLine(Line line) {
		final boolean matches = line.toString().matches(regexp);
		if (matches || !args.isQuiet()) {
			if (!output.processLine(line)) {
				return false;
			}
		}
		if (matches) {
			return output.processLine(new SimpleLine(text, line.getLineEnding()));
		}
		return true;
	}
}