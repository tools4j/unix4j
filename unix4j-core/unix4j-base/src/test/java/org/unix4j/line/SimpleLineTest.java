package org.unix4j.line;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link SimpleLine}.
 */
public class SimpleLineTest {
	
	@Test
	public void testUnixLine() {
		final SimpleLine line = new SimpleLine("Hello world", "\n");
		testLine(line, "Hello world", "\n");
	}
	@Test
	public void testWindowsLine() {
		final SimpleLine line = new SimpleLine("Hello world", "\r\n");
		testLine(line, "Hello world", "\r\n");
	}
	@Test
	public void testUnterminatedLine() {
		final SimpleLine line = new SimpleLine("Hello world", "");
		testLine(line, "Hello world", "");
	}

	@Test(expected=NullPointerException.class)
	public void testContentNull() {
		new SimpleLine(null, "");
	}
	@Test(expected=NullPointerException.class)
	public void testLineEndingNull() {
		new SimpleLine("", null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testNewLineLengthAboveTwo() {
		new SimpleLine("Hello world", "\n\r\n");
	}
	
	private static void testLine(Line line, String content, String newLine) {
		Assert.assertEquals(content, line.getContent());
		Assert.assertEquals(newLine, line.getLineEnding());
		Assert.assertEquals(content.length(), line.getContentLength());
		Assert.assertEquals(newLine.length(), line.getLineEndingLength());
	}

}
