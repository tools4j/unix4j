package org.unix4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.unix4j.io.BufferedInput;
import org.unix4j.io.Input;

public class MultilineString {
	public final static String LINE_ENDING = System.getProperty("line.separator");
	public final static MultilineString EMPTY = new MultilineString("");

	private final List<String> lines = new ArrayList<String>();

	public MultilineString() {
		super();
	}

	public MultilineString(final String content) {
		int start = 0;
		int end = content.indexOf(LINE_ENDING, start);
		while (end >= 0) {
			lines.add(content.substring(start, end));
			start = end + LINE_ENDING.length();
			end = content.indexOf(LINE_ENDING, start);
		}
		if (start < content.length()) {
			lines.add(content.substring(start, end));
		}
	}

	public MultilineString appendLine(final String line){
		lines.add(line);
		return this;
	}

	public MultilineString appendLines(final String... lines){
		this.lines.addAll(Arrays.asList(lines));
		return this;
	}

	public void fromString(final String content){
		appendLines(content.split(LINE_ENDING));
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
					final String expectedLine = expected.lines.get(i);
					final String actualLine = lines.get(i);
					assertEquals("line index:" + i, expectedLine, actualLine);
				}
			}
		}
	}

	public Input toInput() {
		return new BufferedInput(new LinkedList<String>(lines));
	}
	
	@Override
	public int hashCode() {
		return lines.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof MultilineString) {
			return lines.equals(((MultilineString)obj).lines);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	public String toString(boolean lastLineWithLineEnding) {
		final StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			if (sb.length() > 0) {
				sb.append(LINE_ENDING);
			}
			sb.append(line);
		}
		if (lastLineWithLineEnding && !lines.isEmpty()) {
			sb.append(LINE_ENDING);
		}
		return sb.toString();
	}
	
}
