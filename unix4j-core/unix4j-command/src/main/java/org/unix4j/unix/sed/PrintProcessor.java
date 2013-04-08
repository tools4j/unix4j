package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

class PrintProcessor extends AbstractRegexpProcessor {
	public PrintProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
	}

	public PrintProcessor(Command command, String script, SedArguments args, LineProcessor output) {
		super(command, deriveArgs(command, script, args), output);
	}

	private static SedArguments deriveArgs(Command command, String script, SedArguments args) {
		final int start = StringUtil.findStartTrimWhitespace(script);
		final int end = indexOfNextDelimiter(script, start);
		if (start < 0 || end < 0) {
			throw new IllegalArgumentException("invalid script for sed " + command + " command: " + script);
		}
		args.setRegexp(script.substring(start + 1, end));
		return args;
	}

	@Override
	public boolean processLine(Line line) {
		if (!args.isQuiet()) {
			if (!output.processLine(line)) {
				return false;
			}
		}
		final boolean matches = regexp.matcher(line).find();
		if (matches) {
			return output.processLine(line);
		}
		return true;
	}
}