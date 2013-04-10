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
		args = parsePatternFlags(command, args, script, end + 1);
		args.setString1(script.substring(start + 1, end));
		final int commandStart = findCommand(command, script, end + 1);
		final int backslashStart = findBackslash(command, script, commandStart + 1);
		if (backslashStart < script.length() && script.charAt(commandStart) == command.commandChar) {
			final int textStart = StringUtil.findStartTrimNewlineChars(script, backslashStart + 1);
			final int textEnd = StringUtil.findEndTrimNewlineChars(script);
			args.setString2(script.substring(textStart, textEnd));
		} else {
			throw new IllegalArgumentException("invalid script (missing \\ before text?) for sed " + command + " command: " + script);
		}
		return args;
	}

	private static int findCommand(Command command, String script, int start) {
		final char cmd = command.commandChar;
		final int len = script.length();
		boolean isPatternFlags = true;
		for (int i = start; i < len; i++) {
			final char ch = script.charAt(i);
			if (cmd == ch) {
				return i;
			}
			if (Character.isWhitespace(ch)) {
				isPatternFlags = false;
			} else {
				if (!isPatternFlags) {
					//should not get there as the script syntax has been checked before
					throw new IllegalArgumentException("illegal character '" + ch + "' found instead of command character '" + cmd + "' in sed script: " + script);
				}
			}
		}
		//should not get there as the script syntax has been checked before
		throw new IllegalArgumentException("command character '" + cmd + "' expected in sed script: " + script);
	}
	
	private static int findBackslash(Command command, String script, int start) {
		final int index = StringUtil.findStartTrimWhitespace(script, start);
		if (index < script.length()) {
			final int ch = script.charAt(index); 
			if (ch == '\\') {
				return index;
			}
			throw new IllegalArgumentException("illegal character '" + ch + "' found instead of backslash character '\\' in sed script: " + script);
		}
		throw new IllegalArgumentException("backslash character '\\' expected in sed script: " + script);
	}

}