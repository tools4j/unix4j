package org.unix4j.unix.sed;

import java.util.Arrays;
import java.util.regex.Matcher;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

final class SubstituteProcessor extends AbstractRegexpProcessor {
	
	private static final int[] EMPTY_OCCURRENCE = new int[0];

	private final String replacement;
	private final int[] occurrences;

	public SubstituteProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
		this.replacement = getReplacement(args);
		this.occurrences = args.isOccurrenceSet() ? args.getOccurrence() : EMPTY_OCCURRENCE;
		for (int i = 0; i < occurrences.length; i++) {
			if (occurrences[i] <= 0) {
				throw new IllegalArgumentException("invalid occurrence index " + occurrences[i] + " in sed " + command + " command");
			}
		}
		Arrays.sort(occurrences);
	}
	public SubstituteProcessor(Command command, String script, SedArguments args, LineProcessor output) {
		this(command, deriveArgs(command, script, args), output);
	}

	private static SedArguments deriveArgs(Command command, String script, SedArguments args) {
		final int start = StringUtil.findStartTrimWhitespace(script) + 1;
		final int mid = indexOfNextDelimiter(script, start);
		final int end = indexOfNextDelimiter(script, mid);
		if (mid < 0 || end < 0) {
			throw new IllegalArgumentException("invalid script for sed " + command + " command: " + script);
		}
		args = parseFlags(command, args, script, end);
		args.setRegexp(script.substring(start + 1, mid));
		args.setReplacement(script.substring(mid + 1, end));
		return args;
	}

	private static SedArguments parseFlags(Command command, SedArguments args, String script, int end) {
		if (end + 1 < script.length()) {
			final SedOptions.Default options = new SedOptions.Default(args.getOptions());
			int last = script.length() - 1;
			//skip trailing whitespace
			while (last > end && Character.isWhitespace(script.charAt(last))) {
				last--;
			}
			//g and p flags
			while (last > end) {
				final char flag = script.charAt(last);
				if (flag == 'g') {
					options.set(SedOption.global);
				} else if (flag == 'p') {
					options.set(SedOption.print);
				} else {
					break;
				}
				last--;
			}
			args = new SedArguments(options);
			//occurrence index
			if (last > end) {
				final String occurrenceStr = script.substring(end + 1, last + 1);
				final int occurrence;
				try {
					occurrence = Integer.parseInt(occurrenceStr);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("invalid substitute flags in sed script: " + script, e);
				}
				if (occurrence <= 0) {
					throw new IllegalArgumentException("invalid occurrence index " + occurrence + " in sed script: " + script);
				}
				args.setOccurrence(occurrence);
			}
		}
		return args;
	}
	@Override
	public boolean processLine(Line line) {
		final Matcher matcher = regexp.matcher(line.getContent());
		if (matcher.find()) {
			boolean matches = true;
			final StringBuffer changed = new StringBuffer();//cannot use StringBuilder here since matcher does not support it
			if (occurrences.length > 0) {
				int current = 1;
				for (int i = 0; i < occurrences.length; i++) {
					final int occurrence = occurrences[i];
					while (matches && current < occurrence) {
						 matches = matcher.find();
						 current++;//one too much after unsuccessful find, but does not matter anymore
					}
					if (matches) {
				        matcher.appendReplacement(changed, replacement);
					} else {
						break;
					}
				}
				if (matches && occurrences.length == 1 && args.isGlobal()) {
					//replace all subsequent occurrences in this case
					matches = matcher.find();
					while (matches) {
				        matcher.appendReplacement(changed, replacement);
				        matches = matcher.find();
					}
				}
			} else {
				while (matches) {
			        matcher.appendReplacement(changed, replacement);
			        matches = args.isGlobal() && matcher.find();
				}
			}
	        matcher.appendTail(changed);
			return output.processLine(new SimpleLine(changed, line.getLineEnding()));
		} else {
			if (args.isQuiet()) {
				return true;
			}
			return output.processLine(line);
		}
	}
}