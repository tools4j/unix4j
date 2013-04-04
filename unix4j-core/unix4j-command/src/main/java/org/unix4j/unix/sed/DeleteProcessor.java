package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

class DeleteProcessor extends AbstractRegexpProcessor {
	public DeleteProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
	}

	@Override
	public boolean processLine(Line line) {
		if (!line.toString().matches(regexp)) {
			return output.processLine(line);
		}
		return true;
	}
}