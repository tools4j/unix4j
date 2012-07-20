package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.WriterOutput;
import org.unix4j.util.MultilineString;

import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;

public class HeadTest {
	private final static String LINE1 = "This is a test blah blah blah";
	private final static String LINE2 = "This is a test blah blah de blah";
	private final static String LINE3 = "This is a test one two three";
	private final static String LINE4 = "one";
	private final static String LINE5 = "a";
	private final static String LINE6 = "";
	private final static String LINE7 = "two";
	private final static String LINE8 = "asfd";
	private final static String LINE9 = "qwerty";
	private final static String LINE10 = "two";
	private final static String LINE11 = "";
	private final static String LINE12 = "asfd asfd asfd asfd asfd";
	private final static MultilineString input;

	static {
		input = new MultilineString();
		input.appendLine(LINE1)
				.appendLine(LINE2)
				.appendLine(LINE3)
				.appendLine(LINE4)
				.appendLine(LINE5)
				.appendLine(LINE6)
				.appendLine(LINE7)
				.appendLine(LINE8)
				.appendLine(LINE9)
				.appendLine(LINE10)
				.appendLine(LINE11)
				.appendLine(LINE12);
	}

	@Test
	public void testHead_1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		assertHead(input, 1, expectedOutput);
	}

	@Test
	public void testHead_2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1)
				.appendLine(LINE2);
		assertHead(input, 2, expectedOutput);
	}

	@Test
	public void testHead_6() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1)
				.appendLine(LINE2)
				.appendLine(LINE3)
				.appendLine(LINE4)
				.appendLine(LINE5)
				.appendLine(LINE6);
		assertHead(input, 6, expectedOutput);
	}

	@Test
	public void testHead_12() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1)
				.appendLine(LINE2)
				.appendLine(LINE3)
				.appendLine(LINE4)
				.appendLine(LINE5)
				.appendLine(LINE6)
				.appendLine(LINE7)
				.appendLine(LINE8)
				.appendLine(LINE9)
				.appendLine(LINE10)
				.appendLine(LINE11)
				.appendLine(LINE12);
		assertHead(input, 12, expectedOutput);
	}

	@Test
	public void testHead_100() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1)
				.appendLine(LINE2)
				.appendLine(LINE3)
				.appendLine(LINE4)
				.appendLine(LINE5)
				.appendLine(LINE6)
				.appendLine(LINE7)
				.appendLine(LINE8)
				.appendLine(LINE9)
				.appendLine(LINE10)
				.appendLine(LINE11)
				.appendLine(LINE12);
		assertHead(input, 100, expectedOutput);
	}

	@Test
	public void testHead_default() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1)
				.appendLine(LINE2)
				.appendLine(LINE3)
				.appendLine(LINE4)
				.appendLine(LINE5)
				.appendLine(LINE6)
				.appendLine(LINE7)
				.appendLine(LINE8)
				.appendLine(LINE9)
				.appendLine(LINE10);
		final String actualOutput = Unix4j.fromString(input.toString()).head().executeToString();
		assertEquals(expectedOutput.toString(true), actualOutput);
	}

	@Test
	public void testHead_0() {
		final MultilineString expectedOutput = new MultilineString();
		assertHead(input, 0, expectedOutput);
	}

	@Test
	public void testHead_noInput_0() {
		final MultilineString expectedOutput = new MultilineString();
		assertHead(MultilineString.EMPTY, 1, expectedOutput);
	}

	@Test
	public void testHead_noInput_1() {
		final MultilineString expectedOutput = new MultilineString();
		assertHead(MultilineString.EMPTY, 1, expectedOutput);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHead_negativeLines() {
		final MultilineString expectedOutput = new MultilineString();
		assertHead(input, -1, expectedOutput);
	}

	private void assertHead(final MultilineString input, final int lines, final MultilineString expectedOutput){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.fromInput(input.toInput()).head(lines).execute(new WriterOutput(actualOutputStringWriter));
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
}
