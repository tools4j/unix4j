package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

class DeleteProcessor extends AbstractRegexpProcessor {
	public DeleteProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
	}
	public DeleteProcessor(Command command, String script, SedArguments args, LineProcessor output) {
		this(command, deriveArgs(command, script, args), output);
	}

	private static SedArguments deriveArgs(Command command, String script, SedArguments args) {
		final int start = StringUtil.findStartTrimWhitespace(script);
		final int end = indexOfNextDelimiter(script, start);
		if (end < 0) {
			throw new IllegalArgumentException("invalid script for sed " + command + " command: " + script);
		}
		args = parsePatternFlags(command, args, script, end + 1);
		args.setRegexp(script.substring(start + 1, end));
		return args;
	}

	@Override
	public boolean processLine(Line line) {
		final boolean matches = regexp.matcher(line).find();
		if (!matches) {
			return output.processLine(line);
		}
		return true;
	}
}