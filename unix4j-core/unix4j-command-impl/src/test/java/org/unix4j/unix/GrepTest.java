package org.unix4j.unix;

import java.io.StringWriter;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.WriterOutput;
import org.unix4j.util.MultilineString;

/**
 * Unit test for simple App.
 */

public class GrepTest {
	private final static String THIS_IS_A_TEST_BLAH_BLAH_BLAH = "This is a test blah blah blah";
	private final static String THIS_IS_A_TEST_BLAH_BLAH = "This is a test blah blah";
	private final static String THIS_IS_A_TEST_ONE_TWO_THREE = "This is a test one two three";
	private final static String ONE = "one";
	private final static String A = "a";
	private final static String EMPTY_LINE = "";
	private final static String TWO = "two";
	private final static String COUNT = "def\\d123";
	private final static MultilineString input;

	static {
		input = new MultilineString();
		input.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE)
				.appendLine(ONE)
				.appendLine(A)
				.appendLine(EMPTY_LINE)
				.appendLine(TWO)
				.appendLine(COUNT);
	}

	@Test
	public void testGrep_simple1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				// .appendLine(ONE)
				// .appendLine(A)
				// .appendLine(EMPTY_LINE)
				// .appendLine(TWO);
		final String regex = "This";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_simple2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE)
				.appendLine(A);
				// .appendLine(EMPTY_LINE)
				// .appendLine(TWO);
		final String regex = "a";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_simple3() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE)
				.appendLine(ONE)
				.appendLine(A)
				.appendLine(EMPTY_LINE)
				.appendLine(TWO)
				.appendLine(COUNT);
		final String regex = ".*";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_simple4() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE)
				.appendLine(ONE)
				.appendLine(A)
				// .appendLine(EMPTY_LINE)
				.appendLine(TWO)
				.appendLine(COUNT);
		final String regex = ".+";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_simple5() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				// .appendLine(ONE)
				// .appendLine(A)
				// .appendLine(EMPTY_LINE)
				// .appendLine(TWO);
		final String regex = "\\s";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_caseIsIncorrect() {
		final MultilineString expectedOutput = new MultilineString();
				// expectedOutput
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				// .appendLine(ONE)
				// .appendLine(A)
				// .appendLine(EMPTY_LINE)
				// .appendLine(TWO);
		final String regex = "THIS";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_ignoringCase() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				.appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				// .appendLine(ONE)
				// .appendLine(A)
				// .appendLine(EMPTY_LINE)
				// .appendLine(TWO);
		final String regex = "THIS";
		assertGrep( input, regex, expectedOutput, Grep.Option.ignoreCase );
	}

	@Test
	public void testGrep_inverseMatch() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				.appendLine(ONE)
				.appendLine(A)
				.appendLine(EMPTY_LINE
				).appendLine(TWO)
				.appendLine(COUNT);
		final String regex = "This";
		assertGrep( input, regex, expectedOutput, Grep.Option.invert );
	}

	@Test
	public void testGrep_inverseMatchIgnoreCase() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				.appendLine(ONE)
				.appendLine(A)
				.appendLine(EMPTY_LINE)
				// .appendLine(TWO);
				.appendLine(COUNT);
		final String regex = "t";
		assertGrep( input, regex, expectedOutput, Grep.Option.invert );
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscaping() {
		final MultilineString expectedOutput = new MultilineString();
			// expectedOutput
			// .appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
			// .appendLine(THIS_IS_A_TEST_BLAH_BLAH)
			// .appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
			// .appendLine(ONE)
			// .appendLine(A)
			// .appendLine(EMPTY_LINE)
			// .appendLine(TWO);
			// .appendLine(COUNT);
		final String regex = "def\\d123";
		assertGrep( input, regex, expectedOutput );
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscapingButWithFixedStringOptionSet() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				// .appendLine(ONE)
				// .appendLine(A)
				// .appendLine(EMPTY_LINE)
				// .appendLine(TWO);
				.appendLine(COUNT);
		final String regex = "def\\d123";
		assertGrep( input, regex, expectedOutput, Grep.Option.fixedStrings );
	}

	@Test
	public void testGrep_fixedStringButWithWrongCase() {
		final MultilineString expectedOutput = MultilineString.EMPTY;
		final String regex = "DEF\\d123";
		assertGrep( input, regex, expectedOutput, Grep.Option.fixedStrings );
	}

	@Test
	public void testGrep_fixedStringWithWrongCaseAndIngoringCase() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_BLAH_BLAH)
				// .appendLine(THIS_IS_A_TEST_ONE_TWO_THREE);
				// .appendLine(ONE)
				// .appendLine(A)`
				// .appendLine(TWO);
				.appendLine(COUNT);
		final String regex = "DEF\\d123";
		assertGrep( input, regex, expectedOutput, Grep.Option.fixedStrings, Grep.Option.ignoreCase );
	}

	private void assertGrep(final MultilineString input, final String expression, final MultilineString expectedOutput, Grep.Option... options){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.fromInput(input.toInput()).grep(expression, options).toOutput(new WriterOutput(actualOutputStringWriter));
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
}
