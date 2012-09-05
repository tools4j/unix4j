package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.Input;
import org.unix4j.io.StringInput;
import org.unix4j.line.Line;
import org.unix4j.util.MultilineString;

import static org.junit.Assert.assertEquals;

public class WcTest {

	/** OS line ending length */
	private static final int LEL = Line.LINE_ENDING.length();

	private static MultilineString input;
	static{
		input = new MultilineString();
		input.appendLine("This is a test blah blah blah")
				.appendLine("        ")
				.appendLine("")
				.appendLine("blah")
				.appendLine("");
	}

	@Test
	public void testWcSingleFields() {
		assertWc(input.toInput(), "5", Wc.OPTIONS.lines);
		assertWc(input.toInput(), "" + (41 + 5 * LEL), Wc.OPTIONS.chars);	//41 chars + 5 line endings
		assertWc(input.toInput(), "8", Wc.OPTIONS.words);
	}

	@Test
	public void testWcCountLinesWordsAndChars() {
		assertEquals("   5   8  " + (41 + 5 * LEL), Unix4j.from(input.toInput()).wcCountLinesWordsAndChars().toStringResult());
	}

	@Test
	public void testWcCountLines() {
		assertEquals("5", Unix4j.from(input.toInput()).wcCountLines().toStringResult());
	}

	@Test
	public void testWcCountChars() {
		assertEquals("" + (41 + 5 * LEL), Unix4j.from(input.toInput()).wcCountChars().toStringResult());
	}

	@Test
	public void testWcCountWords() {
		assertEquals("8", Unix4j.from(input.toInput()).wcCountWords().toStringResult());
	}


	@Test
	public void testDefaultOutputWhenNoOptionsGiven() {
		assertWc(input.toInput(), "   5   8  " + (41 + 5 * LEL));
	}

	@Test
	public void testMultipleOutputs() {
		assertWc(input.toInput(), "   5   8  " + (41 + 5 * LEL), Wc.OPTIONS.lines.words.chars);
		assertWc(input.toInput(), "  5  8", Wc.OPTIONS.lines.words);
	}

	@Test
	public void testEmptyInput() {
		assertWc("", "  0  0  0");
	}

	@Test
	public void testSingleLine() {
		assertWc("blah", "  1  1  4");
	}

	@Test
	public void testSingleChar() {
		assertWc("d", "  1  1  1");
	}

	@Test
	public void testSingleCharWithSpacesAtEndOfLine() {
		assertWc("d ", "  1  1  2");
	}

	private void assertWc(final String input, final String expectedOutput) {
		assertWc(new StringInput(input), expectedOutput);
	}

	private void assertWc(final Input input, final String expectedOutput){
		final String actualOutput = Unix4j.from(input).wc().toStringResult();
		System.out.println("Asserting: expected:'" + expectedOutput + "' = actual:'" + actualOutput +"'");
		assertEquals(expectedOutput, actualOutput);
	}

	private void assertWc(final Input input, final String expectedOutput, Wc.Options options){
		final String actualOutput = Unix4j.from(input).wc(options).toStringResult();
		System.out.println("Asserting: expected:'" + expectedOutput + "' = actual:'" + actualOutput +"'");
	    assertEquals(expectedOutput, actualOutput);
	}
}
