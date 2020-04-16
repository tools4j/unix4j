package org.unix4j.util;

import org.unix4j.line.Line;

import java.util.List;

/**
 * Contains static utility methods related to {@link Line} objects.
 */
public class LineUtil {

	/**
	 * Returns a multi-line representation of the provided {@code lines}. The
	 * last line of the returned string is never terminated, all other lines are
	 * terminated with guarantee even if the line itself has an empty line
	 * ending string.
	 * 
	 * @return a multi-line string of the buffered lines, without line
	 *         termination for the last line
	 */
	public static String toMultiLineString(List<? extends Line> lines) {
		final StringBuilder sb = new StringBuilder();
		Line lastTerminatedLine = Line.EMPTY_LINE;
		for (int i = 0; i < lines.size(); i++) {
			final Line line = lines.get(i);
			sb.append(line.getContent());
			if (i + 1 < lines.size()) {
				if (line.getLineEndingLength() > 0) {
					sb.append(line.getLineEnding());
					lastTerminatedLine = line;
				} else {
					sb.append(lastTerminatedLine.getLineEnding());
				}
			}
		}
		return sb.toString();
	}

	// no instances
	private LineUtil() {
		super();
	}
}
