package org.unix4j.io;

import org.junit.Assert;
import org.junit.Test;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;

import java.io.StringReader;

public class ReaderInputTest {
	private final String[] input = {
			"01234\n6789",	
			"01234\r\n789a", 
			"01234\n\r\r\n9abcd",
			"01234\n\r\r\n9abcd\n",
			"01234\n\r\r\n9abcd\n\n",
			"hello\nworld",	
			"hello\r\nworld", 
			"hello\n\r\r\nworld",
			"hello\n\r\r\nworld\n",
			"hello\n\r\r\nworld\n\n",
			"\nworld",	
			"\r\nworld", 
			"\n\r\r\nworld",
			"\n\r\r\nworld\n",
			"\n\r\r\nworld\n\n",
		};
	private final Line[][] expected = {
			{new SimpleLine("01234", "\n"), new SimpleLine("6789", "")},
			{new SimpleLine("01234", "\r\n"), new SimpleLine("789a", "")},
			{new SimpleLine("01234", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("9abcd", "")},
			{new SimpleLine("01234", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("9abcd", "\n")},
			{new SimpleLine("01234", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("9abcd", "\n"), new SimpleLine("", "\n")},
			{new SimpleLine("hello", "\n"), new SimpleLine("world", "")},
			{new SimpleLine("hello", "\r\n"), new SimpleLine("world", "")},
			{new SimpleLine("hello", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "")},
			{new SimpleLine("hello", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "\n")},
			{new SimpleLine("hello", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "\n"), new SimpleLine("", "\n")},
			{new SimpleLine("", "\n"), new SimpleLine("world", "")},
			{new SimpleLine("", "\r\n"), new SimpleLine("world", "")},
			{new SimpleLine("", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "")},
			{new SimpleLine("", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "\n")},
			{new SimpleLine("", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "\n"), new SimpleLine("", "\n")},
	};
	
	@Test
	public void testReadLines() {
		for (int sample = 0; sample < input.length; sample++) {
			final StringReader reader = new StringReader(input[sample]);
			final ReaderInput readerInput = new ReaderInput(reader);

			//assert
			int lineIndex = 0;
			while (readerInput.hasMoreLines()) {
				final Line line = readerInput.readLine();
				Assert.assertEquals(expected[sample][lineIndex], line);
				lineIndex++;
			}
			Assert.assertEquals(expected[sample].length, lineIndex);
		}
	}

	@Test
	public void testLongReadLines() {
		final int minLinePrefixLen = 1;
		final int maxLinePrefixLen = 4048;
		for (int linePrefixLen = minLinePrefixLen; linePrefixLen < maxLinePrefixLen; linePrefixLen++) {
			final String linePrefix = makeLinePrefix(linePrefixLen);
			for (int sample = 0; sample < input.length; sample++) {
				final StringReader reader = new StringReader(linePrefix + input[sample]);
				final ReaderInput readerInput = new ReaderInput(reader);
	
				//assert
				int lineIndex = 0;
				final String sampleInfo = "sample " + sample + " with linePrefixLen=" + linePrefixLen;
				while (readerInput.hasMoreLines()) {
					final String lineInfo = "line " + lineIndex + " of " + sampleInfo;
					final Line line = readerInput.readLine();
					if (lineIndex >= expected[sample].length) { 
						Assert.assertTrue("no more than " + expected[sample].length + " lines expected in " + sampleInfo, lineIndex < expected[sample].length);
					}
					if (lineIndex == 0) {
						//first line has line prefix now
						Assert.assertEquals(lineInfo, new SimpleLine(linePrefix + expected[sample][lineIndex].getContent(), expected[sample][lineIndex].getLineEnding()), line);
					} else {
						Assert.assertEquals(lineInfo, expected[sample][lineIndex], line);
					}
					lineIndex++;
				}
				Assert.assertEquals("expected number of lines in " + sampleInfo, expected[sample].length, lineIndex);
			}
		}
	}

	private String makeLinePrefix(int n) {
		final StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			sb.append((char)('A' + (i % 26)));
		}
		return sb.toString();
	}
	
	
}
