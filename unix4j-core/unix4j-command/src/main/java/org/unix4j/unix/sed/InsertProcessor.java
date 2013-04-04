package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;

class InsertProcessor extends AbstractTextProcessor {
	public InsertProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
	}

	@Override
	public boolean processLine(Line line) {
		final boolean matches = line.toString().matches(regexp);
		if (matches) {
			if (!output.processLine(new SimpleLine(text, line.getLineEnding()))) {
				return false;
			}
		}
		if (matches || !args.isQuiet()) {
			return output.processLine(line);
		}
		return true;
	}
}