package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

class TranslateProcessor extends AbstractSedProcessor {
	private final CharMap charMap;
	public TranslateProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
		this.charMap = new CharMap(args.getString1(), args.getString2());
	}
	public TranslateProcessor(Command command, String script, SedArguments args, LineProcessor output) {
		this(command, deriveArgs(command, script, args), output);
	}

	private static SedArguments deriveArgs(Command command, String script, SedArguments args) {
		final int start = StringUtil.findStartTrimWhitespace(script) + 1;
		final int mid = indexOfNextDelimiter(script, start);
		final int end = indexOfNextDelimiter(script, mid);
		if (mid < 0 || end < 0) {
			throw new IllegalArgumentException("invalid script for sed " + command + " command: " + script);
		}
		final int scriptEnd = StringUtil.findEndTrimWhitespace(script);
		if (end + 1 < scriptEnd) {
			throw new IllegalArgumentException("non-whitespace characters found after " + command + " command in sed script: " + script);
		}
		args.setString1(script.substring(start + 1, mid));
		args.setString2(script.substring(mid + 1, end));
		return args;
	}
	@Override
	public boolean processLine(Line line) {
		char[] changed = null;
		final int len = line.getContentLength();//or whole line length?
		for (int i = 0; i < len; i++) {
			final char src = line.charAt(i);
			final char dst = charMap.map(src);
			if (dst != 0) {
				if (changed == null) {
					changed = new char[len];
					for (int j = 0; j < i; j++) {
						changed[j] = line.charAt(j);
					}
				}
				changed[i] = dst;
			} else {
				if (changed != null) {
					changed[i] = src;
				}
			}
		}
		if (changed != null) {
			final Line changedLine = new SimpleLine(String.valueOf(changed), line.getLineEnding());
			return output.processLine(changedLine);
		} else {
			return output.processLine(line);
		}
	}
}