package org.unix4j.unix.wc;

import org.unix4j.line.Line;

/**
 * Constants for different count types.
 */
public enum CounterType {
	Lines {
		@Override
		public boolean isOptionSet(WcArguments args) {
			return args.isLines();
		}

		@Override
		public int count(Line line) {
			return 1;
		}
	},
	Words {
		@Override
		public boolean isOptionSet(WcArguments args) {
			return args.isWords();
		}

		@Override
		public int count(Line line) {
			int words = 0;
			int wordlen = 0;
			final int len = line.getContentLength();
			for (int i = 0; i < len; i++) {
				final char ch = line.charAt(i);
				if (Character.isWhitespace(ch)) {
					if (wordlen > 0) {
						words++;
					}
					wordlen = 0;
				} else {
					wordlen++;
				}
			}
			return wordlen > 0 ? words + 1 : words;
		}
	},
	Chars {
		@Override
		public boolean isOptionSet(WcArguments args) {
			return args.isChars();
		}

		@Override
		public int count(Line line) {
			return line.length();
		}
	};

	/**
	 * Returns true if the option relevant for this count type is set in
	 * {@code args}.
	 * 
	 * @param args
	 *            the arguments with the count options
	 * @return true if this count type option is set
	 */
	abstract public boolean isOptionSet(WcArguments args);

	/**
	 * Returns the count by which a counter should be incremented given the
	 * specified {@code line}. For instance, the method returns 1 if this type
	 * specifies that lines are counted, and it returns the number of words or
	 * characters in the line if the count type refers to words or chars,
	 * respectively.
	 * 
	 * @param line
	 *            the line to analyse
	 * @return the increment to add to the counter for this count type and the
	 *         specified line
	 */
	abstract public int count(Line line);
}
