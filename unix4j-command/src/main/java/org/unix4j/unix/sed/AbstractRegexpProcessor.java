package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

import java.util.regex.Pattern;

abstract class AbstractRegexpProcessor extends AbstractSedProcessor {
	
	protected final Pattern regexp;

	public AbstractRegexpProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
		if (args.isIgnoreCase()) {
			this.regexp = Pattern.compile(getRegexp(args), Pattern.CASE_INSENSITIVE);
		} else {
			this.regexp = Pattern.compile(getRegexp(args));
		}
	}

	protected static SedArguments parsePatternFlags(Command command, SedArguments args, String script, int start) {
		final int end = StringUtil.findWhitespace(script, start);
		for (int i = start; i < end; i++) {
			final char flag = script.charAt(i);
			if (flag == 'I' && !args.isIgnoreCase()) {
				final SedOptions.Default options = new SedOptions.Default(args.getOptions());
				options.set(SedOption.ignoreCase);
				args = new SedArguments(options);
			} else if (flag == command.commandChar) {
				//ignore
			} else {
				throw new IllegalArgumentException("invalid pattern flags in sed script: " + script);
			}
		}
		return args;
	}
}