package org.unix4j.unix.sed;

import java.util.regex.Pattern;

import org.unix4j.processor.LineProcessor;

abstract class AbstractRegexpProcessor extends AbstractSedProcessor {
	
	protected final Pattern regexp;

	public AbstractRegexpProcessor(Command command, SedArguments args, LineProcessor output) {
		super(command, args, output);
		this.regexp = Pattern.compile(getRegexp(args));
	}
}