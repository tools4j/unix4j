package org.unix4j.line;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link SingleCharSequenceLine}.
 */
public class SingleCharSequenceLineTest {
	
	@Test
	public void testUnixLine() {
		final SingleCharSequenceLine line = new SingleCharSequenceLine("Hello world\n", 1);
		testLine(line, "Hello world", "\n");
	}
	@Test
	public void testWindowsLine() {
		final SingleCharSequenceLine line = new SingleCharSequenceLine("Hello world\r\n", 2);
		testLine(line, "Hello world", "\r\n");
	}
	@Test
	public void testUnterminatedLine() {
		final SingleCharSequenceLine line = new SingleCharSequenceLine("Hello world", 0);
		testLine(line, "Hello world", "");
	}
	@Test
	public void testUnixLineWithOffsetAndLengths() {
		final SingleCharSequenceLine line = new SingleCharSequenceLine("Hello world\nAndSomeCrapThereAfter", "Hello ".length(), "world".length(), 1);
		testLine(line, "world", "\n");
	}
	@Test
	public void testWindowsLineWithOffsetAndLengths() {
		final SingleCharSequenceLine line = new SingleCharSequenceLine("Hello world\r\nAndSomeCrapThereAfter", "Hello ".length(), "world".length(), 2);
		testLine(line, "world", "\r\n");
	}
	@Test
	public void testUnterminatedLineWithOffsetAndLengths() {
		final SingleCharSequenceLine line = new SingleCharSequenceLine("Hello worldAndSomeCrapThereAfter", "Hello ".length(), "world".length(), 0);
		testLine(line, "world", "");
	}
	
	@Test(expected=NullPointerException.class)
	public void testContentNull() {
		new SingleCharSequenceLine(null, 0);
	}
	@Test(expected=NullPointerException.class)
	public void testContentNullWithOffsetAndLEngths() {
		new SingleCharSequenceLine(null, 0, 0, 0);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testNewLineLengthNegative() {
		new SingleCharSequenceLine("Hello world\n", -1);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testOffsetNegative() {
		new SingleCharSequenceLine("Hello world\n", -1, 0, 0);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testContentLengthNegativeWithOffsetAndLengths() {
		new SingleCharSequenceLine("Hello world\n", 0, -1, -1);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testNewLineLengthNegativeWithOffsetAndLengths() {
		new SingleCharSequenceLine("Hello world\n", 0, 0, -1);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testNewLineLengthAboveTwo() {
		new SingleCharSequenceLine("Hello world\n", 0, 0, 3);
	}
	
	private static void testLine(Line line, String content, String newLine) {
		Assert.assertEquals(content, line.getContent());
		Assert.assertEquals(newLine, line.getLineEnding());
		Assert.assertEquals(content.length(), line.getContentLength());
		Assert.assertEquals(newLine.length(), line.getLineEndingLength());
	}

}
