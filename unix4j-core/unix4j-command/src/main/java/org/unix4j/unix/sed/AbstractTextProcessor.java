package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

abstract class AbstractTextProcessor extends AbstractRegexpProcessor {
	
	protected final String text;

	public AbstractTextProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
		this.text = args.getString2();
	}
	
	public AbstractTextProcessor(Command command, String script, SedArguments args, LineProcessor output) {
		this(command, deriveArgs(command, script, args), output);
	}

	private static SedArguments deriveArgs(Command command, String script, SedArguments args) {
		final int start = StringUtil.findStartTrimWhitespace(script);
		final int end = indexOfNextDelimiter(script, start);
		if (end < 0) {
			throw new IllegalArgumentException("invalid script for sed " + command + " command: " + script);
		}
		args.setString1(script.substring(start + 1, end));
		final int whitespaceStart = StringUtil.findWhitespace(script, end + 1);
		final int commandStart = StringUtil.findStartTrimWhitespace(script, whitespaceStart);
		final int backslashStart = StringUtil.findStartTrimWhitespace(script, commandStart + 1);
		if (backslashStart < script.length() && script.charAt(commandStart) == command.command) {
			final int textStart = StringUtil.findStartTrimWhitespace(script, backslashStart + 1);
			final int textEnd = StringUtil.findEndTrimWhitespace(script);
			args.setString2(script.substring(textStart, textEnd));
		} else {
			throw new IllegalArgumentException("invalid script (missing \\ before text?) for sed " + command + " command: " + script);
		}
		return args;
	}
	
}