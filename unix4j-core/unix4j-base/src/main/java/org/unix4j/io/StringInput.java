package org.unix4j.io;

import org.unix4j.line.Line;
import org.unix4j.util.StringUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Input device reading the input from a string. If the string contains
 * line-ending code (UNIX or DOS), it is split into multiple lines.
 */
public class StringInput implements Input {

	private final Input input;

	/**
	 * Constructor with lines. Each line is tested for new line characters and
	 * possibly split into multiple lines.
	 * 
	 * @param lines
	 *            the lines for this input
	 */
	public StringInput(String... lines) {
		this(new BufferedInput(toList(lines)));
	}

	/**
	 * Constructor with lines. Each line is tested for new line characters and
	 * possibly split into multiple lines.
	 *
	 * @param lines
	 *            the lines for this input
	 */
	public StringInput(Iterable<? extends String> lines) {
		this(lines instanceof Collection ?
				new BufferedInput(toList(lines)) :
				new IteratorInput(toIterator(lines))
		);
	}

	/**
	 * Constructor with lines. Each line is tested for new line characters and
	 * possibly split into multiple lines.
	 *
	 * @param lines
	 *            the lines for this input
	 */
	public StringInput(Stream<? extends String> lines) {
		this(lines.iterator());
	}

	/**
	 * Constructor with lines. Each line is tested for new line characters and
	 * possibly split into multiple lines.
	 *
	 * @param lines
	 *            the lines for this input
	 */
	public StringInput(Iterator<? extends String> lines) {
		this(new IteratorInput(toIterator(lines)));
	}

	private StringInput(Input input) {
		this.input = input;
	}

	@Override
	public boolean hasMoreLines() {
		return input.hasMoreLines();
	}

	@Override
	public Line readLine() {
		return input.readLine();
	}

	@Override
	public Iterator<Line> iterator() {
		return input.iterator();
	}

	@Override
	public void close() {
		input.close();
	}

	@Override
	public String toString() {
		return input.toString();
	}

	public String toMultilineString() {
		if (input instanceof BufferedInput) {
			return ((BufferedInput)input).toMultiLineString();
		}
		return toString();
	}

	private static LinkedList<Line> toList(String[] lines) {
		final LinkedList<Line> list = new LinkedList<Line>();
		for (int i = 0; i < lines.length; i++) {
			list.addAll(StringUtil.splitLines(lines[i]));
		}
		return list;
	}

	private static LinkedList<Line> toList(Iterable<? extends String> lines) {
		final LinkedList<Line> list = new LinkedList<Line>();
		for (String line : lines) {
			list.addAll(StringUtil.splitLines(line));
		}
		return list;
	}

	private static Iterator<Line> toIterator(final Iterable<? extends String> lines) {
		return toIterator(lines.iterator());
	}

	private static Iterator<Line> toIterator(final Iterator<? extends String> lines) {
		return new Iterator<Line>() {
			final LinkedList<Line> next = new LinkedList<>();
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Line next() {
				if (next.isEmpty()) {
					if (lines.hasNext()) {
						next.addAll(StringUtil.splitLines(lines.next()));
					}
				}
				return next.isEmpty() ? null : next.removeFirst();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("remove is not supported");
			}
		};
	}
}
