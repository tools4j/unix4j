package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.MultilineString;

public class CommandChainingTest {
	private final static MultilineString input;
	static {
		input = new MultilineString();
		input
				.appendLine("This is a test blah blah blah")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def123");
	}

	@Test
	public void test_headTail_1() {
		assertEquals("one", Unix4j.fromString(input.toString()).head(4).tail(1).toStringResult());
	}

	@Test
	public void test_headTail_2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("one")
				.appendLine("a");

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).head(5).tail(2).toStringResult());
	}

	@Test
	public void test_headTail_3() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah blah blah")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def123");

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).head(10).tail(10).toStringResult());
	}

	@Test
	public void test_headTail_4() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine("This is a test blah blah blah");
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).head(1).tail(1).toStringResult());
	}

	@Test
	public void test_headTail_5() {
		assertEquals("", Unix4j.fromString(input.toString()).head(0).tail(0).toStringResult());
	}

	@Test
	public void test_headTailHead() {
		assertEquals("a", Unix4j.fromString(input.toString()).head(10).tail(4).head(1).toStringResult());
	}

	@Test
	public void test_headSortSedTail() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("a")
				.appendLine("def123")
				.appendLine("one")
				.appendLine("Dude is a test blah blah")
				.appendLine("Dude is a test blah blah blah")
				.appendLine("Dude is a test one two three")
				.appendLine("two");
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).head(10).sort().tail(7).sed("s/This/Dude/").toStringResult());
	}

	@Test
	public void test_headSortSedTailGrep() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("Dude is a test blah blah")
				.appendLine("Dude is a test blah blah blah")
				.appendLine("Dude is a test one two three");
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).head(10).sort().tail(7).sed("s/This/Dude/").grep("Dude").toStringResult());
	}

	@Test
	public void test_headSortSedTailGrepHeadWc() {
		System.out.println(
				Unix4j.fromString(input.toString())
						.head(10)
						.sort()
						.tail(7)
						.sed("s/This/Dude/")
						.grep("Dude")
						.head(1)
						.wc(Wc.Options.words)
						.toStringResult());
		assertEquals("6", /* 6 words */
				Unix4j.fromString(input.toString())
						.head(10)
						.sort()
						.tail(7)
						.sed("s/This/Dude/")
						.grep("Dude")
						.head(1)
						.wc(Wc.Options.words)
						.toStringResult());
	}
}
