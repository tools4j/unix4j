package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.MultilineString;

import static org.junit.Assert.assertEquals;

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
		assertWc(input.toString(), "5", Wc.Option.l);
		assertWc(input.toString(), "5", Wc.Option.lines);

		assertWc(input.toString(), "41", Wc.Option.m);
		assertWc(input.toString(), "41", Wc.Option.chars);

		assertWc(input.toString(), "8", Wc.Option.w);
		assertWc(input.toString(), "8", Wc.Option.words);
	}

	@Test
	public void testDefaultOutputWhenNoOptionsGiven() {
		assertWc(input.toString(), "   5   8  41");
	}

	@Test
	public void testMultipleOutputs() {
		assertWc(input.toString(), "   5   8  41", Wc.Option.l, Wc.Option.w, Wc.Option.m);
		assertWc(input.toString(), "  5  8", Wc.Option.l, Wc.Option.w);
		assertWc(input.toString(), "   5   8  41", Wc.Option.l, Wc.Option.w, Wc.Option.m, Wc.Option.l, Wc.Option.w, Wc.Option.m);
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

	private void assertWc(final String input, final String expectedOutput, Wc.Option... options){
		final String actualOutput = Unix4j.fromString(input).wc(options).toStringResult();
		System.out.println("Asserting: expected:'" + expectedOutput + "' = actual:'" + actualOutput +"'");
	    assertEquals(expectedOutput, actualOutput);
	}
}
