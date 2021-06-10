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
	
	@Test
	public void testSubLine() {
		final String CRLF = "" + Line.CR + Line.LF;  
		final Line line = new SimpleLine("0123456789", CRLF);
		//identity cut
		Assert.assertSame(line, SimpleLine.subLine(line, 0, line.length(), false));
		Assert.assertSame(line, SimpleLine.subLine(line, 0, line.length(), true));
		//0 to content length now
		Assert.assertEquals(new SimpleLine("", ""), SimpleLine.subLine(line, 0, 0, false));
		Assert.assertEquals(new SimpleLine("", CRLF), SimpleLine.subLine(line, 0, 0, true));
		Assert.assertEquals(new SimpleLine("0", ""), SimpleLine.subLine(line, 0, 1, false));
		Assert.assertEquals(new SimpleLine("0", CRLF), SimpleLine.subLine(line, 0, 1, true));
		Assert.assertEquals(new SimpleLine("1", ""), SimpleLine.subLine(line, 1, 2, false));
		Assert.assertEquals(new SimpleLine("1", CRLF), SimpleLine.subLine(line, 1, 2, true));
		Assert.assertEquals(new SimpleLine("9", ""), SimpleLine.subLine(line, 9, 10, false));
		Assert.assertEquals(new SimpleLine("9", CRLF), SimpleLine.subLine(line, 9, 10, true));
		Assert.assertEquals(new SimpleLine("9", "" + Line.CR), SimpleLine.subLine(line, 9, 11, false));
		Assert.assertEquals(new SimpleLine("9", CRLF), SimpleLine.subLine(line, 9, 12, false));
		Assert.assertEquals(new SimpleLine("", "" + Line.CR), SimpleLine.subLine(line, 10, 11, false));
		Assert.assertEquals(new SimpleLine("", CRLF), SimpleLine.subLine(line, 10, 12, false));
		Assert.assertEquals(new SimpleLine("", "" + Line.LF), SimpleLine.subLine(line, 11, 12, false));

		//expecte exceptions for
		final int[] start= {-1,  0, 1, 0, 0};
		final int[] end  = { 0, -1, 0, line.getContentLength()+1, line.length()+1};
		final boolean[] preserveEnding= { true, true, true, true, false};
		for (int i = 0; i < start.length; i++) {
			try {
				SimpleLine.subLine(line, start[i], end[i], preserveEnding[i]);
				Assert.fail("expected exception for illegal subLine input arguments: line.subLine(" + start[i] + ", " + end[i] + ", " + preserveEnding[i] + ") for line " + line);
			} catch (IllegalArgumentException e) {
				//ok, that's what we would expect
			}
		}
	}
	
	@Test
	public void testSubSequence() {
		final String CRLF = "" + Line.CR + Line.LF;  
		final Line line = new SimpleLine("0123456789", CRLF);
		//identity cut
		Assert.assertSame(line, SimpleLine.subLine(line, 0, line.length(), false));
		Assert.assertSame(line, SimpleLine.subLine(line, 0, line.length(), true));
		//0 to content length now
		Assert.assertEquals("", 					line.subSequence(0, 0));
		Assert.assertEquals("0", 					line.subSequence(0, 1));
		Assert.assertEquals("01",					line.subSequence(0, 2));
		Assert.assertEquals("012",					line.subSequence(0, 3));
		Assert.assertEquals("0123",					line.subSequence(0, 4));
		Assert.assertEquals("01234",				line.subSequence(0, 5));
		Assert.assertEquals("012345",				line.subSequence(0, 6));
		Assert.assertEquals("0123456",				line.subSequence(0, 7));
		Assert.assertEquals("01234567",				line.subSequence(0, 8));
		Assert.assertEquals("012345678",			line.subSequence(0, 9));
		Assert.assertEquals("0123456789",			line.subSequence(0, 10));
		Assert.assertEquals("0123456789" + Line.CR,	line.subSequence(0, 11));
		Assert.assertEquals("0123456789" + CRLF,	line.subSequence(0, 12));
		Assert.assertEquals("123456789" + CRLF,		line.subSequence(1, 12));
		Assert.assertEquals("23456789" + CRLF,		line.subSequence(2, 12));
		Assert.assertEquals("3456789" + CRLF,		line.subSequence(3, 12));
		Assert.assertEquals("456789" + CRLF,		line.subSequence(4, 12));
		Assert.assertEquals("56789" + CRLF,			line.subSequence(5, 12));
		Assert.assertEquals("6789" + CRLF,			line.subSequence(6, 12));
		Assert.assertEquals("789" + CRLF,			line.subSequence(7, 12));
		Assert.assertEquals("89" + CRLF,			line.subSequence(8, 12));
		Assert.assertEquals("9" + CRLF,				line.subSequence(9, 12));
		Assert.assertEquals("" + CRLF,				line.subSequence(10, 12));
		Assert.assertEquals("" + Line.LF,			line.subSequence(11, 12));
		Assert.assertEquals("",						line.subSequence(12, 12));
		Assert.assertEquals("1", 			line.subSequence(1, 2));
		Assert.assertEquals("9",			line.subSequence(9, 10));
		Assert.assertEquals("9" + Line.CR,	line.subSequence(9, 11));
		Assert.assertEquals("9" + CRLF,		line.subSequence(9, 12));
		Assert.assertEquals("" + Line.CR,	line.subSequence(10, 11));
		Assert.assertEquals(CRLF,			line.subSequence(10, 12));
		Assert.assertEquals("" + Line.LF,	line.subSequence(11, 12));

		//expecte exceptions for
		final int[] start= {-1,  0, 1, 0, 0};
		final int[] end  = { 0, -1, 0, line.getContentLength()+1, line.length()+1};
		final boolean[] preserveEnding= { true, true, true, true, false};
		for (int i = 0; i < start.length; i++) {
			try {
				SimpleLine.subLine(line, start[i], end[i], preserveEnding[i]);
				Assert.fail("expected exception for illegal subLine input arguments: line.subLine(" + start[i] + ", " + end[i] + ", " + preserveEnding[i] + ") for line " + line);
			} catch (IllegalArgumentException e) {
				//ok, that's what we would expect
			}
		}
	}

	private static void testLine(Line line, String content, String newLine) {
		Assert.assertEquals(content, line.getContent());
		Assert.assertEquals(newLine, line.getLineEnding());
		Assert.assertEquals(content.length(), line.getContentLength());
		Assert.assertEquals(newLine.length(), line.getLineEndingLength());
	}

}
