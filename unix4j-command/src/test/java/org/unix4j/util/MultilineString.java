package org.unix4j.util;

import org.unix4j.io.BufferedOutput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultilineString {
	public final static MultilineString EMPTY = new MultilineString("");

	private final BufferedOutput buffer = new BufferedOutput();

	public MultilineString() {
		super();
	}

	public MultilineString(final String content) {
		appendLine(content);
	}

	public MultilineString appendLine(final String line) {
		if (line.isEmpty()) {
			buffer.processLine(Line.EMPTY_LINE);
		} else {
			for (final Line l : StringUtil.splitLines(line)) {
				buffer.processLine(l);
			}
		}
		return this;
	}

	public MultilineString appendLines(final String... lines) {
		for (int i = 0; i < lines.length; i++) {
			appendLine(lines[i]);
		}
		return this;
	}

	public void assertMultilineStringEquals(final MultilineString expected) {
		if (equals(expected)) {
			assertEquals(expected, this);
		} else {
			final String unequalityMessage = "Expected string does not equal actual string.\nExpected: \n'" + expected + "' \nActual: '\n" + this + "'\n";
			if (buffer.size() != expected.buffer.size()) {
				String message = "Expected string has " + expected.buffer.size() + " lines, but actual string has " + buffer.size() + " lines.\n";
				fail(message + unequalityMessage);
			} else {
				final List<Line> actualLines = buffer.asList();
				final List<Line> expectedLines = expected.buffer.asList();
				for (int i = 0; i < expectedLines.size(); i++) {
					final Line expectedLine = expectedLines.get(i);
					final Line actualLine = actualLines.get(i);
					if (!expectedLine.equals(actualLine)) {
						if (!expectedLine.getContent().equals(actualLine.getContent())) {
							assertEquals("line index:" + i, expectedLine, actualLine);
						} else {
							if (i + 1 < expectedLines.size()) {
								// line endings different?
								if (expectedLine.getLineEndingLength() != 0 && actualLine.getLineEndingLength() != 0) {
									assertEquals("line index:" + i + " (line endings different) ", expectedLine, actualLine);
								}
							}
						}
					}
				}
			}
		}
	}

	public Input toInput() {
		return buffer.asInput();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof MultilineString) {
			return toString().equals(obj.toString());
		}
		return false;
	}

	/**
	 * Returns a multi-line representation of the lines in this
	 * {@code MultilineString} object. The last line is never terminated, all
	 * other lines are terminated with guarantee even if the line itself has an
	 * empty line ending string.
	 * 
	 * @return a multi-line string of the buffered lines, without line
	 *         termination for the last line
	 */
	@Override
	public String toString() {
		return buffer.toMultiLineString();
	}

}
