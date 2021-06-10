package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;

abstract class AbstractSedProcessor implements LineProcessor {
	protected final Command command;
	protected final SedArguments args;
	protected final LineProcessor output;

	public AbstractSedProcessor(Command command, SedArguments args, LineProcessor output) {
		this.command = command;
		this.args = args;
		this.output = output;
	}

	@Override
	public void finish() {
		output.finish();
	}

	/**
	 * Returns the regexp operand from args, either called "regexp" or
	 * "string1". If none of the two is set, an empty string is returned.
	 * 
	 * @param args
	 *            the args with operand values
	 * @return the regexp argument from "regexp" or "string1" or an empty string
	 *         of none of the two operands is set
	 */
	protected static String getRegexp(SedArguments args) {
		if (args.isRegexpSet()) {
			return args.getRegexp();
		}
		if (args.isString1Set()) {
			return args.getString1();
		}
		return "";
	}

	/**
	 * Returns the replacement operand from args, either called "replacement" or
	 * "string2". If none of the two is set, an empty string is returned.
	 * 
	 * @param args
	 *            the args with operand values
	 * @return the replacement argument from "replacement" or "string2" or an
	 *         empty string of none of the two operands is set
	 */
	protected static String getReplacement(SedArguments args) {
		if (args.isReplacementSet()) {
			return args.getReplacement();
		}
		if (args.isString2Set()) {
			return args.getString2();
		}
		return "";
	}

	/**
	 * Returns the index of the next delimiter in the given sed script. The
	 * character at {@code indexOfPreviousDelimiter} is taken as delimiter. The
	 * method handles escaped delimiters and returns -1 if no further delimiter
	 * is found.
	 * 
	 * @param script
	 *            the script to analyze
	 * @param indexOfPreviousDelimiter
	 *            the index of the previous delimiter
	 * @return the index of the next delimiter after
	 *         {@code indexOfPreviousDelimiter}, or -1 if no further delimiter
	 *         exists of if {@code indexOfNextDelimiter < 0}
	 */
	protected static int indexOfNextDelimiter(String script, int indexOfPreviousDelimiter) {
		if (indexOfPreviousDelimiter < 0 || script.length() <= indexOfPreviousDelimiter) {
			return -1;
		}
		final char delim = script.charAt(indexOfPreviousDelimiter);
		if (delim == '\\') {
			throw new IllegalArgumentException("invalid delimiter '\\' in sed script: " + script);
		}
		int index = indexOfPreviousDelimiter;
		do {
			index = script.indexOf(delim, index + 1);
		} while (index >= 0 && isEscaped(script, index));
		return index;
	}

	private static boolean isEscaped(String script, int index) {
		int backslashCount = 0;
		index--;
		while (index >= 0 && script.charAt(index) == '\\') {
			backslashCount++;
			index--;
		}
		// an uneven count of backslashes means that the character at position
		// index is escaped (an even count of backslashes escapes backslashes)
		return backslashCount % 2 == 1;
	}
}