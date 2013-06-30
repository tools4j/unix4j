package org.unix4j.unix.grep;

import org.unix4j.line.Line;
import org.unix4j.util.StringUtil;

/**
 * Performs fixed string comparisons to decide whether the pattern matches a
 * line. Member classes exist for different grep options.
 */
abstract class FixedStringMatcher implements LineMatcher {

	protected final String pattern;

	public FixedStringMatcher(String pattern) {
		this.pattern = pattern;
	}

	public static class Standard extends FixedStringMatcher {
		public Standard(GrepArguments args) {
			super(args.getRegexp());
		}
		@Override
		public boolean matches(Line line) {
			return line.getContent().contains(pattern);
		}
	}
	public static class IgnoreCase extends FixedStringMatcher {
		public IgnoreCase(GrepArguments args) {
			super(args.getRegexp());
		}
		@Override
		public boolean matches(Line line) {
			return StringUtil.containsIgnoreCase(line.getContent(), pattern);
		}
	}
	public static class WholeLine extends FixedStringMatcher {
		public WholeLine(GrepArguments args) {
			super(args.getRegexp());
		}
		@Override
		public boolean matches(Line line) {
			return line.getContent().equals(pattern);
		}
	}
	public static class WholeLineIgnoreCase extends FixedStringMatcher {
		public WholeLineIgnoreCase(GrepArguments args) {
			super(args.getRegexp());
		}
		@Override
		public boolean matches(Line line) {
			return line.getContent().equalsIgnoreCase(pattern);
		}
	}
	
}
