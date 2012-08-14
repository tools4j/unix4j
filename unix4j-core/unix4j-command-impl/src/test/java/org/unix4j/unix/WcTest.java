package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.Input;
import org.unix4j.io.StringInput;
import org.unix4j.util.MultilineString;

public class WcTest {
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
		assertWc(input.toInput(), "5", Wc.Option.lines);

		assertWc(input.toInput(), "46", Wc.Option.chars);	//41 chars + 5 line endings

		assertWc(input.toInput(), "8", Wc.Option.words);
	}

	@Test
	public void testDefaultOutputWhenNoOptionsGiven() {
		assertWc(input.toInput(), "   5   8  46");
	}

	@Test
	public void testMultipleOutputs() {
		assertWc(input.toInput(), "   5   8  46", Wc.Option.lines, Wc.Option.words, Wc.Option.chars);
		assertWc(input.toInput(), "  5  8", Wc.Option.lines, Wc.Option.words);
		assertWc(input.toInput(), "   5   8  46", Wc.Option.lines, Wc.Option.words, Wc.Option.chars, Wc.Option.lines, Wc.Option.words, Wc.Option.chars);
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

	private void assertWc(final String input, final String expectedOutput, Wc.Option... options) {
		assertWc(new StringInput(input), expectedOutput, options);
	}
	
	private void assertWc(final Input input, final String expectedOutput, Wc.Option... options){
		final String actualOutput = Unix4j.from(input).wc(options).toStringResult();
		System.out.println("Asserting: expected:'" + expectedOutput + "' = actual:'" + actualOutput +"'");
	    assertEquals(expectedOutput, actualOutput);
	}
}
