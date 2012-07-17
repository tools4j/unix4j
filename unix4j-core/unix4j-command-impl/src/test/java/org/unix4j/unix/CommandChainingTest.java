package org.unix4j.unix;

import org.junit.Ignore;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.MultilineString;

import static org.junit.Assert.assertEquals;

@Ignore
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
				.appendLine("def\\d123");
	}

	@Test
	public void test1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah blah blah")
				.appendLine("This is a test hasblah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertEquals("one", Unix4j.fromString(input.toString()).head(4).tail(1).executeToString(false));
	}

}
