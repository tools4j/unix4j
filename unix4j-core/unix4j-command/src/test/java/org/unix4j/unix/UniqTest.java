package org.unix4j.unix;

import java.io.StringWriter;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.unix.uniq.UniqOptions;
import org.unix4j.util.MultilineString;

public class UniqTest {
	private final static String LINE1 = "This is a test blah blah blah";
	private final static String LINE2 = "This is a test blah blah de blah";
	private final static String LINE3 = "This is a test one two three";
	private final static String LINE4 = "one";
	private final static String LINE5 = "one";
	private final static String LINE6 = "";
	private final static String LINE7 = "two";
	private final static String LINE8 = "two";
	private final static String LINE9 = "two";
	private final static String LINE10 = "three";
	private final static String LINE11 = "three";
	private final static String LINE12 = "one";
	private final static String LINE13 = "two";
	private final static String LINE14 = "three";
	private final static String LINE15 = "This is a test blah blah blah";
	private final static String LINE16 = "Last line is different, has no line ending";
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
				.appendLine(LINE12)
				.appendLine(LINE13)
				.appendLine(LINE14)
				.appendLine(LINE15)
				.appendLine(LINE16);
	}

	@Test
	public void testUniq() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		expectedOutput.appendLine(LINE2);
		expectedOutput.appendLine(LINE3);
		expectedOutput.appendLine(LINE4);
		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(LINE7);
		expectedOutput.appendLine(LINE10);
		expectedOutput.appendLine(LINE12);
		expectedOutput.appendLine(LINE13);
		expectedOutput.appendLine(LINE14);
		expectedOutput.appendLine(LINE15);
		expectedOutput.appendLine(LINE16);
		assertUniq(input, UniqOptions.EMPTY, expectedOutput);
	}

	@Test
	public void testUniq_uniqueOnly() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		expectedOutput.appendLine(LINE2);
		expectedOutput.appendLine(LINE3);
		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(LINE12);
		expectedOutput.appendLine(LINE13);
		expectedOutput.appendLine(LINE14);
		expectedOutput.appendLine(LINE15);
		expectedOutput.appendLine(LINE16);
		assertUniq(input, Uniq.OPTIONS.uniqueOnly, expectedOutput);
	}

	@Test
	public void testUniq_duplicatesOnly() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE4);
		expectedOutput.appendLine(LINE7);
		expectedOutput.appendLine(LINE10);
		assertUniq(input, Uniq.OPTIONS.duplicatedOnly, expectedOutput);
	}

	@Test
	public void testUniq_count() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(makeCountLine(1, LINE1));
		expectedOutput.appendLine(makeCountLine(1, LINE2));
		expectedOutput.appendLine(makeCountLine(1, LINE3));
		expectedOutput.appendLine(makeCountLine(2, LINE4));
		expectedOutput.appendLine(makeCountLine(1, LINE6));
		expectedOutput.appendLine(makeCountLine(3, LINE7));
		expectedOutput.appendLine(makeCountLine(2, LINE10));
		expectedOutput.appendLine(makeCountLine(1, LINE12));
		expectedOutput.appendLine(makeCountLine(1, LINE13));
		expectedOutput.appendLine(makeCountLine(1, LINE14));
		expectedOutput.appendLine(makeCountLine(1, LINE15));
		expectedOutput.appendLine(makeCountLine(1, LINE16));
		assertUniq(input, Uniq.OPTIONS.count, expectedOutput);
	}

	@Test
	public void testUniq_Global() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		expectedOutput.appendLine(LINE2);
		expectedOutput.appendLine(LINE3);
		expectedOutput.appendLine(LINE4);
		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(LINE7);
		expectedOutput.appendLine(LINE10);
		expectedOutput.appendLine(LINE16);
		assertUniq(input, Uniq.OPTIONS.global, expectedOutput);
	}

	@Test
	public void testUniq_uniqueOnlyGlobal() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE2);
		expectedOutput.appendLine(LINE3);
		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(LINE16);
		assertUniq(input, Uniq.OPTIONS.uniqueOnly.global, expectedOutput);
	}

	@Test
	public void testUniq_duplicatesOnlyGlobal() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		expectedOutput.appendLine(LINE4);
		expectedOutput.appendLine(LINE7);
		expectedOutput.appendLine(LINE10);
		assertUniq(input, Uniq.OPTIONS.duplicatedOnly.g, expectedOutput);
	}

	@Test
	public void testUniq_countGlobal() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(makeCountLine(2, LINE1));
		expectedOutput.appendLine(makeCountLine(1, LINE2));
		expectedOutput.appendLine(makeCountLine(1, LINE3));
		expectedOutput.appendLine(makeCountLine(3, LINE4));
		expectedOutput.appendLine(makeCountLine(1, LINE6));
		expectedOutput.appendLine(makeCountLine(4, LINE7));
		expectedOutput.appendLine(makeCountLine(3, LINE10));
		expectedOutput.appendLine(makeCountLine(1, LINE16));
		assertUniq(input, Uniq.OPTIONS.count.global, expectedOutput);
	}

	private String makeCountLine(int count, String line) {
		return "   " + count + " " + line;
	}

	private void assertUniq(final MultilineString input, UniqOptions options, final MultilineString expectedOutput){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).uniq(options).toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
}
