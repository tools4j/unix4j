package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.MultilineString;

public class SedTest {
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
	public void testSed_searchAndReplaceSimple() {
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

		final String script = "s/blah/hasblah/";
		assertSed( input, script, expectedOutput );
	}

	@Test
	public void testSed_searchAndReplaceGlobal() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah hasblah hasblah")
				.appendLine("This is a test hasblah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		final String script = "s/blah/hasblah/g";
		assertSed(input, script, expectedOutput);
	}

	@Test
	public void testSed_searchAndReplaceUsingEscapedForwardSlashes() {
		final String input = "to be/not to be that is the question";
		final String script = "s/be\\/not/be or not/g";
		final String expectedOutput = "to be or not to be that is the question";

		final String output = Unix4j.fromString(input).sed(script).toStringResult();
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testSed_searchAndReplaceWithWhitespaceChar() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This_is_a_test_blah_blah_blah")
				.appendLine("This_is_a_test_blah_blah")
				.appendLine("This_is_a_test_one_two_three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		final String script = "s/\\s/_/g";
		assertSed(input, script, expectedOutput);
	}

	@Test
	public void testSed_searchAndReplaceWithGroups() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test a is This blah blah blah")
				.appendLine("test a is This blah blah")
				.appendLine("test a is This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		final String script = "s/(This) (is) (a) (test)/$4 $3 $2 $1/";
		assertSed( input, script, expectedOutput );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSed_searchAndReplaceWithIllegalGroupReference() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test a is This blah blah blah")
				.appendLine("test a is This blah blah")
				.appendLine("test a is This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		final String script = "s/(This) (is) (a) (test)/$4 $3 $ $2 $1/";
		assertSed( input, script, expectedOutput );
	}

	@Test
	public void testSed_searchAndReplaceWithEscapedGroupSymbol() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test a $ is This blah blah blah")
				.appendLine("test a $ is This blah blah")
				.appendLine("test a $ is This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		final String script = "s/(This) (is) (a) (test)/$4 $3 \\$ $2 $1/";
		assertSed( input, script, expectedOutput );
	}

	@Test
	public void testSed_searchAndReplaceUsingSubstituteMethodSimple() {
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

		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.fromString(input.toString()).sedSubstituteFirst("blah", "hasblah").toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test
	public void testSed_searchAndReplaceUsingSubstituteMethodGlobal() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah hasblah hasblah")
				.appendLine("This is a test hasblah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.fromString(input.toString()).sedSubstitute("blah", "hasblah").toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test(expected = NullPointerException.class)
	public void testSed_substituteNullSearchParam() {
		Unix4j.fromString("blah").sedSubstitute(null, "hasblah").toStringResult();
	}

	@Test(expected = NullPointerException.class)
	public void testSed_substituteNullReplaceParam() {
		Unix4j.fromString("blah").sedSubstitute("blah", null).toStringResult();
	}

	private void assertSed(final MultilineString input, final String script, final MultilineString expectedOutput){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).sed(script).toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test(expected = NullPointerException.class)
	public void testSed_nullScript() {
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).sed(null).toWriter(actualOutputStringWriter);
	}
}
