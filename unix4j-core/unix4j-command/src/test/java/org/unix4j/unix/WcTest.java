package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.Input;
import org.unix4j.io.StringInput;
import org.unix4j.line.Line;
import org.unix4j.unix.wc.WcOptions;
import org.unix4j.util.MultilineString;

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
	public void testWcLines() {
		assertWc(input.toInput(), "5", Wc.Options.lines);
		assertWc(input.toInput(), "5", "--lines");
		assertWc(input.toInput(), "5", "-l");
	}
	@Test
	public void testWcChars() {
		assertWc(input.toInput(), "" + (41 + 5 * LEL), Wc.Options.chars);	//41 chars + 5 line endings
		assertWc(input.toInput(), "" + (41 + 5 * LEL), "--chars");	//41 chars + 5 line endings
		assertWc(input.toInput(), "" + (41 + 5 * LEL), "-m");	//41 chars + 5 line endings
	}
	@Test
	public void testWcWords() {
		assertWc(input.toInput(), "8", Wc.Options.words);
		assertWc(input.toInput(), "8", "--words");
		assertWc(input.toInput(), "8", "-w");
	}

	@Test
	public void testWcCountLinesWordsAndChars() {
//		assertEquals("   5   8  " + (41 + 5 * LEL), Unix4j.from(input.toInput()).wcCountLinesWordsAndChars().toStringResult());
		assertEquals("   5   8  " + (41 + 5 * LEL), Unix4j.from(input.toInput()).wc().toStringResult());
	}

	@Test
	public void testWcCountLines() {
//		assertEquals("5", Unix4j.from(input.toInput()).wcCountLines().toStringResult());
		assertEquals("5", Unix4j.from(input.toInput()).wc(Wc.Options.lines).toStringResult());
		assertEquals("5", Unix4j.from(input.toInput()).wc("--lines").toStringResult());
		assertEquals("5", Unix4j.from(input.toInput()).wc("-l").toStringResult());
	}

	@Test
	public void testWcCountChars() {
//		assertEquals("" + (41 + 5 * LEL), Unix4j.from(input.toInput()).wcCountChars().toStringResult());
		assertEquals("" + (41 + 5 * LEL), Unix4j.from(input.toInput()).wc(Wc.Options.chars).toStringResult());
		assertEquals("" + (41 + 5 * LEL), Unix4j.from(input.toInput()).wc("--chars").toStringResult());
		assertEquals("" + (41 + 5 * LEL), Unix4j.from(input.toInput()).wc("-m").toStringResult());
	}

	@Test
	public void testWcCountWords() {
//		assertEquals("8", Unix4j.from(input.toInput()).wcCountWords().toStringResult());
		assertEquals("8", Unix4j.from(input.toInput()).wc(Wc.Options.words).toStringResult());
		assertEquals("8", Unix4j.from(input.toInput()).wc("--words").toStringResult());
		assertEquals("8", Unix4j.from(input.toInput()).wc("-w").toStringResult());
	}


	@Test
	public void testDefaultOutputWhenNoOptionsGiven() {
		assertWc(input.toInput(), "   5   8  " + (41 + 5 * LEL));
	}

	@Test
	public void testMultipleOutputs() {
		assertWc(input.toInput(), "   5   8  " + (41 + 5 * LEL), Wc.Options.lines.words.chars);
		assertWc(input.toInput(), "  5  8", Wc.Options.lines.words);
		assertWc(input.toInput(), "  5  8", "--lines", "--words");
		assertWc(input.toInput(), "  5  8", "-lw");
		assertWc(input.toInput(), "  5  8", "-wl");
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

	private void assertWc(final Input input, final String expectedOutput, WcOptions options){
		final String actualOutput = Unix4j.from(input).wc(options).toStringResult();
		System.out.println("Asserting: expected:'" + expectedOutput + "' = actual:'" + actualOutput +"'");
	    assertEquals(expectedOutput, actualOutput);
	}
	private void assertWc(final Input input, final String expectedOutput, String... args){
		final String actualOutput = Unix4j.from(input).wc(args).toStringResult();
		System.out.println("Asserting: expected:'" + expectedOutput + "' = actual:'" + actualOutput +"'");
	    assertEquals(expectedOutput, actualOutput);
	}
}
