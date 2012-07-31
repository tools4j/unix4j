package org.unix4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.unix4j.io.BufferedInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;

public class MultilineString {
	public final static MultilineString EMPTY = new MultilineString("");

	private final List<Line> lines;

	public MultilineString() {
		this.lines = new ArrayList<Line>();
	}

	public MultilineString(final String content) {
		this();
		appendLine(content);
	}

	public MultilineString appendLine(final String line){
		if (line.isEmpty()) {
			lines.add(new SimpleLine(""));
		} else {
			lines.addAll(StringUtil.splitLines(line));
		}
		return this;
	}

	public MultilineString appendLines(final String... lines){
		for (int i = 0; i < lines.length; i++) {
			appendLine(lines[i]);
		}
		return this;
	}

	public void assertMultilineStringEquals(final MultilineString expected) {
		if (lines.equals(expected.lines)) {
			assertEquals(expected.lines, lines);
		} else {
			final String unequalityMessage = "Expected string does not equal actual string.\nExpected: \n'" + expected.toString() + "' \nActual: '\n" + toString() + "'\n";
			if(lines.size() != expected.lines.size()){
				String message = "Expected string has " + expected.lines.size() + " lines, but actual string has " + lines.size() + " lines.\n";
				fail(message + unequalityMessage);
			} else {
				for(int i=0; i<expected.lines.size(); i++){
					final Line expectedLine = expected.lines.get(i);
					final Line actualLine = lines.get(i);
					assertEquals("line index:" + i, expectedLine, actualLine);
				}
			}
		}
	}

	public Input toInput() {
		return new BufferedInput(new LinkedList<Line>(lines));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof MultilineString) {
			return toString().equals(obj.toString());
		}
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final Iterator<Line> it = lines.iterator();
		while (it.hasNext()) {
			final Line line = it.next();
			sb.append(line.getContent());
			if (it.hasNext()) {
				sb.append(line.getLineEndingLength() == 0 ? StringUtil.LINE_ENDING : line.getLineEnding());
			}
			//no line ending for last line
		}
		return sb.toString();
	}

}
