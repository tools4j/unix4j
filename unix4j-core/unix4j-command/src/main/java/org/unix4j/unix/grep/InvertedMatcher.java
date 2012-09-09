package org.unix4j.unix.grep;

import org.unix4j.line.Line;

/**
 * Inverts the result of another matcher that is passed to the constructor.
 */
public class InvertedMatcher implements LineMatcher {
	
	private final LineMatcher matcher;
	
	/**
	 * Constructor with matcher to invert.
	 * @param matcher the matcher to invert
	 */
	public InvertedMatcher(LineMatcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean matches(Line line) {
		return !matcher.matches(line);
	}

}
