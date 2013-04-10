package org.unix4j.unix.sed;

import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

/**
 * Constants for the sed commands with utility method to derive a constant from
 * a script (see {@link #fromScript(String)} or from the sed arguments
 * {@link #fromArgs(SedArguments)}. The constant also provides support for the
 * instantiation of a new command processor through
 * {@link #createProcessorFor(SedArguments, LineProcessor)}.
 */
public enum Command {
	print('p') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isPrint();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new PrintProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new PrintProcessor(this, script, args, output);
		}
	},
	substitute('s') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isSubstitute();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new SubstituteProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new SubstituteProcessor(this, script, args, output);
		}
	},
	append('a') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isAppend();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new AppendProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new AppendProcessor(this, script, args, output);
		}
	},
	insert('i') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isInsert();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new InsertProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new InsertProcessor(this, script, args, output);
		}
	},
	change('c') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isChange();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new ChangeProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new ChangeProcessor(this, script, args, output);
		}
	},
	delete('d') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isDelete();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new DeleteProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new DeleteProcessor(this, script, args, output);
		}
	},
	translate('y') {
		@Override
		public boolean matches(SedArguments args) {
			return args.isTranslate();
		}

		@Override
		public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output) {
			return new TranslateProcessor(this, args, output);
		}

		@Override
		public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output) {
			return new TranslateProcessor(this, script, args, output);
		}
	};
	protected final char commandChar;

	private Command(char command) {
		this.commandChar = command;
	}

	/**
	 * Returns true if the the command option for this command is set in the
	 * specified sed arguments, and false otherwise.
	 * 
	 * @param args
	 *            the sed arguments
	 * @return true if the command option for this command is set in args
	 */
	abstract public boolean matches(SedArguments args);

	/**
	 * Returns a new instance of the appropriate sed processor for this command.
	 * Note that this method does not check whether the correct command option
	 * is selected in {@code args}.
	 * 
	 * @param args
	 *            the sed arguments passed to the processor constructor
	 * @param output
	 *            the output object passed to the processor constructor
	 * @return a new sed processor instance for this command
	 */
	abstract public AbstractSedProcessor createProcessorFor(SedArguments args, LineProcessor output);

	/**
	 * Returns a new instance of the appropriate sed processor for this command.
	 * Note that this method does not check whether the script starts with the
	 * correct command character.
	 * 
	 * @param script
	 *            the sed script passed to the processor constructor
	 * @param args
	 *            the sed arguments passed to the processor constructor
	 * @param output
	 *            the output object passed to the processor constructor
	 * @return a new sed processor instance for this command
	 */
	abstract public AbstractSedProcessor createProcessorFor(String script, SedArguments args, LineProcessor output);

	/**
	 * Returns the command constant The first non-whitespace character of the
	 * script defines the command for substitute and translate; for all other
	 * commands, it is the first character after a whitespace sequence within
	 * the script (leading whitespace is ignored).
	 * 
	 * @param script
	 *            the script to analyse
	 * @return the matching command or null if not found
	 */
	public static Command fromScript(String script) {
		final int len = script.length();
		final int scriptStart = StringUtil.findStartTrimWhitespace(script);
		if (scriptStart < len) {
			final char firstChar = script.charAt(scriptStart);
			if (firstChar == substitute.commandChar) {
				return substitute;
			} else if (firstChar == translate.commandChar) {
				return translate;
			} else {
				final int scriptEnd = AbstractSedProcessor.indexOfNextDelimiter(script, scriptStart);
				if (scriptEnd < 0) {
					throw new IllegalArgumentException("sed regexp pattern is not terminated, expected a second unescaped '" + firstChar + "' character in: " + script);
				}
				final int whitespaceStart = StringUtil.findWhitespace(script, scriptEnd);

				// either
				// (a) the command is the first char after the whitespace
				// following the pattern
				// (b) the command is one of the characters in the pattern flags
				// after the second delimiter

				// (a) try to find the command char in the pattern flags
				Command command = fromCommandChars(script, scriptEnd + 1, whitespaceStart);

				// (b) ok after the whitespace then
				if (command == null && whitespaceStart < len) {
					final int commandStart = StringUtil.findStartTrimWhitespace(script, whitespaceStart);
					command = fromCommandChars(script, commandStart, commandStart + 1);
				}

				// success?
				if (command == null) {
					throw new IllegalArgumentException("command expected in sed script: " + script);
				}
				return command;
			}
		}
		return null;
	}

	/**
	 * Returns the command constant if any of the string characters between
	 * start and end matches one of the command characters. Returns null if no
	 * command character is found.
	 * 
	 * @param string
	 *            the string with the command chararcter candidates
	 * @param start
	 *            the start index in string, inclusive
	 * @param end
	 *            the end index in string, exclusive
	 * @return the matching command or null if not found
	 */
	private static Command fromCommandChars(String string, int start, int end) {
		final int s = Math.max(0, start);
		final int e = Math.min(string.length(), end);
		if (s < e) {
			for (final Command command : Command.values()) {
				for (int i = s; i < e; i++) {
					if (string.charAt(i) == command.commandChar) {
						return command;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the command constant taken from the command option set in args
	 * using the {@link #matches(SedArguments)} method Returns null if no
	 * command option is set.
	 * 
	 * @param args
	 *            the sed command arguments
	 * @return the matching command or null if not found
	 */
	public static Command fromArgs(SedArguments args) {
		for (final Command command : values()) {
			if (command.matches(args)) {
				return command;
			}
		}
		return null;
	}
}
