package org.unix4j.unix.grep;

import java.util.regex.Pattern;

import org.unix4j.line.Line;

/**
 * A matcher using regular expressions to match the pattern with a line. Uses
 * Java's {@link Pattern} to do the regexp stuff.
 */
class RegexpMatcher implements LineMatcher {

	private final Pattern pattern;

	public RegexpMatcher(GrepArguments args) {
		if(args.isPatternSet()){
			this.pattern = args.getPattern();
		} else if(args.isRegexpSet()){
			final String regex;
			if (args.isWholeLine()) {
				regex = args.getRegexp();
			} else {
				regex = ".*" + args.getRegexp() + ".*";
			}
			this.pattern = Pattern.compile(regex, args.isIgnoreCase() ? Pattern.CASE_INSENSITIVE : 0);
		} else {
			throw new IllegalArgumentException("Either pattern, or patternStr must be given");
		}
	}

	@Override
	public boolean matches(Line line) {
		// NOTE: we use content here because . does not match line
		// ending characters, see {@link Pattern#DOTALL}
		boolean matches = pattern.matcher(line.getContent()).matches();
		return matches;
	}
}
