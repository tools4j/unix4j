package org.unix4j.io;

import java.util.LinkedList;

import org.unix4j.line.Line;
import org.unix4j.util.StringUtil;

/**
 * Input device reading the input from a string. If the string contains
 * line-ending code (UNIX or DOS), it is split into multiple lines.
 */
public class StringInput extends BufferedInput {

	/**
	 * Constructor with lines. Each line is tested for new line characters and
	 * possibly split into multiple lines.
	 * 
	 * @param lines
	 *            the lines for this input
	 */
	public StringInput(String... lines) {
		super(toList(lines));
	}

	/**
	 * Constructor with lines. Each line is tested for new line characters and
	 * possibly split into multiple lines.
	 * 
	 * @param lines
	 *            the lines for this input
	 */
	public StringInput(Iterable<String> lines) {
		super(toList(lines));
	}

	private static LinkedList<Line> toList(String[] lines) {
		final LinkedList<Line> list = new LinkedList<Line>();
		for (int i = 0; i < lines.length; i++) {
			list.addAll(StringUtil.splitLines(lines[i]));
		}
		return list;
	}

	private static LinkedList<Line> toList(Iterable<String> lines) {
		final LinkedList<Line> list = new LinkedList<Line>();
		for (String line : lines) {
			list.addAll(StringUtil.splitLines(line));
		}
		return list;
	}
}
