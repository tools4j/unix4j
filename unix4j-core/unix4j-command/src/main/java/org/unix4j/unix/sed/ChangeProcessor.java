package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;

class ChangeProcessor extends AbstractTextProcessor {
	public ChangeProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
	}

	@Override
	public boolean processLine(Line line) {
		if (line.toString().matches(regexp)) {
			return output.processLine(new SimpleLine(text, line.getLineEnding()));
		}
		if (!args.isQuiet()) {
			return output.processLine(line);
		}
		return true;
	}
}