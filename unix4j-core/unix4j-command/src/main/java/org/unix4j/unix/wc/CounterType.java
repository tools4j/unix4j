package org.unix4j.unix.wc;

import org.unix4j.line.Line;



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
	
	abstract public boolean isOptionSet(WcArguments args);
	abstract public int count(Line line);
}
