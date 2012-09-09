package org.unix4j.unix.grep;

import java.util.regex.Pattern;

import org.unix4j.line.Line;

/**
 * A matcher using regular expressions to match the pattern with a line. Uses
 * Java's {@Link Pattern} to do the regexp stuff.
 */
class RegexpMatcher implements LineMatcher {

	private final Pattern pattern;

	public RegexpMatcher(GrepArguments args) {
		final String regex;
		if (args.isWholeLine()) {
			regex = args.getPattern();
		} else {
			regex = ".*" + args.getPattern() + ".*";
		}
		this.pattern = Pattern.compile(regex, args.isIgnoreCase() ? Pattern.CASE_INSENSITIVE : 0);
	}

	@Override
	public boolean matches(Line line) {
		// NOTE: we use content here because . does not match line
		// ending characters, see {@link Pattern#DOTALL}
		boolean matches = pattern.matcher(line.getContent()).matches();
		return matches;
	}
}
