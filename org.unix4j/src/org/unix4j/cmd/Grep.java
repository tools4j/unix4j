package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.Input;

public class Grep extends AbstractCommand<Grep.Option> {

	public static final String NAME = "grep";

	public static enum Option {
		i, ignoreCase, v, invert, expression, file;
	}

	public Grep() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		final String matchString = getArgs().getArgumentValue(Option.expression);
		final Input input = getInput();
		while (input.hasMoreLines()) {
			final String line = input.readLine();
			boolean matches;
			if (isOptSet(Option.i) || isOptSet(Option.ignoreCase)) {
				matches = line.toLowerCase().contains(matchString.toLowerCase());
			} else {
				matches = line.contains(matchString);
			}
			if (isOptSet(Option.v) || isOptSet(Option.invert)) {
				matches = !matches;
			}
			if (matches) {
				getOutput().writeLine(line);
			}
		}
	}

	@Override
	public Option getDefaultArgumentOption() {
		return Option.expression;
	}
}