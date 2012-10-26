package org.unix4j.unix;

import java.io.StringWriter;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.unix.cat.CatOptions;
import org.unix4j.util.MultilineString;
import org.unix4j.util.StringUtil;

public class CatTest {
	private final static String LINE1 = "This is a test blah blah blah";
	private final static String LINE2 = "This is a test blah blah de blah";
	private final static String LINE3 = "This is a test one two three";
	private final static String LINE4 = "";
	private final static String LINE5 = "";
	private final static String LINE6 = "";
	private final static String LINE7 = "two";
	private final static String LINE8 = "three";
	private final static String LINE9 = "four";
	private final static String LINE10 = "five";
	private final static String LINE11 = "";
	private final static String LINE12 = "six";
	private final static String LINE13 = "seven";
	private final static String LINE14 = "";
	private final static String LINE15 = "";
	private final static MultilineString input;

	static {
		input = new MultilineString()
					.appendLine(LINE1)
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
		;
	}

	@Test
	public void testCat() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		expectedOutput.appendLine(LINE2);
		expectedOutput.appendLine(LINE3);
		expectedOutput.appendLine(LINE4);
		expectedOutput.appendLine(LINE5);
		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(LINE7);
		expectedOutput.appendLine(LINE8);
		expectedOutput.appendLine(LINE9);
		expectedOutput.appendLine(LINE10);
		expectedOutput.appendLine(LINE11);
		expectedOutput.appendLine(LINE12);
		expectedOutput.appendLine(LINE13);
		expectedOutput.appendLine(LINE14);
		expectedOutput.appendLine(LINE15);
		assertCat(input, CatOptions.EMPTY, expectedOutput);
	}

	@Test
	public void testCat_numberLines() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(makeCountLine(1, LINE1));
		expectedOutput.appendLine(makeCountLine(2, LINE2));
		expectedOutput.appendLine(makeCountLine(3, LINE3));
		expectedOutput.appendLine(makeCountLine(4, LINE4));
		expectedOutput.appendLine(makeCountLine(5, LINE5));
		expectedOutput.appendLine(makeCountLine(6, LINE6));
		expectedOutput.appendLine(makeCountLine(7, LINE7));
		expectedOutput.appendLine(makeCountLine(8, LINE8));
		expectedOutput.appendLine(makeCountLine(9, LINE9));
		expectedOutput.appendLine(makeCountLine(10, LINE10));
		expectedOutput.appendLine(makeCountLine(11, LINE11));
		expectedOutput.appendLine(makeCountLine(12, LINE12));
		expectedOutput.appendLine(makeCountLine(13, LINE13));
		expectedOutput.appendLine(makeCountLine(14, LINE14));
		expectedOutput.appendLine(makeCountLine(15, LINE15));
		assertCat(input, Cat.Options.numberLines, expectedOutput);
	}

	@Test
	public void testCat_numberNonBlankLines() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(makeCountLine(1, LINE1));
		expectedOutput.appendLine(makeCountLine(2, LINE2));
		expectedOutput.appendLine(makeCountLine(3, LINE3));
		expectedOutput.appendLine(LINE4);
		expectedOutput.appendLine(LINE5);
		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(makeCountLine(4, LINE7));
		expectedOutput.appendLine(makeCountLine(5, LINE8));
		expectedOutput.appendLine(makeCountLine(6, LINE9));
		expectedOutput.appendLine(makeCountLine(7, LINE10));
		expectedOutput.appendLine(LINE11);
		expectedOutput.appendLine(makeCountLine(8, LINE12));
		expectedOutput.appendLine(makeCountLine(9, LINE13));
		expectedOutput.appendLine(LINE14);
		expectedOutput.appendLine(LINE15);
		assertCat(input, Cat.Options.numberNonBlankLines, expectedOutput);
	}

	@Test
	public void testCat_squeezeEmptyLines() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(LINE1);
		expectedOutput.appendLine(LINE2);
		expectedOutput.appendLine(LINE3);
		expectedOutput.appendLine(LINE4);
//		expectedOutput.appendLine(LINE5);
//		expectedOutput.appendLine(LINE6);
		expectedOutput.appendLine(LINE7);
		expectedOutput.appendLine(LINE8);
		expectedOutput.appendLine(LINE9);
		expectedOutput.appendLine(LINE10);
		expectedOutput.appendLine(LINE11);
		expectedOutput.appendLine(LINE12);
		expectedOutput.appendLine(LINE13);
		expectedOutput.appendLine(LINE14);
//		expectedOutput.appendLine(LINE15);
		assertCat(input, Cat.Options.squeezeEmptyLines, expectedOutput);
	}

	@Test
	public void testCat_squeezeEmptyLines_numberLines() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(makeCountLine(1, LINE1));
		expectedOutput.appendLine(makeCountLine(2, LINE2));
		expectedOutput.appendLine(makeCountLine(3, LINE3));
		expectedOutput.appendLine(makeCountLine(4, LINE4));
//		expectedOutput.appendLine(makeCountLine(-, LINE5));
//		expectedOutput.appendLine(makeCountLine(-, LINE6));
		expectedOutput.appendLine(makeCountLine(5, LINE7));
		expectedOutput.appendLine(makeCountLine(6, LINE8));
		expectedOutput.appendLine(makeCountLine(7, LINE9));
		expectedOutput.appendLine(makeCountLine(8, LINE10));
		expectedOutput.appendLine(makeCountLine(9, LINE11));
		expectedOutput.appendLine(makeCountLine(10, LINE12));
		expectedOutput.appendLine(makeCountLine(11, LINE13));
		expectedOutput.appendLine(makeCountLine(12, LINE14));
//		expectedOutput.appendLine(makeCountLine(LINE15));
		assertCat(input, Cat.Options.squeezeEmptyLines.numberLines, expectedOutput);
	}

	@Test
	public void testCat_squeezeEmptyLines_numberNonBlankLines() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.appendLine(makeCountLine(1, LINE1));
		expectedOutput.appendLine(makeCountLine(2, LINE2));
		expectedOutput.appendLine(makeCountLine(3, LINE3));
		expectedOutput.appendLine(LINE4);
//		expectedOutput.appendLine(makeCountLine(-, LINE5));
//		expectedOutput.appendLine(makeCountLine(-, LINE6));
		expectedOutput.appendLine(makeCountLine(4, LINE7));
		expectedOutput.appendLine(makeCountLine(5, LINE8));
		expectedOutput.appendLine(makeCountLine(6, LINE9));
		expectedOutput.appendLine(makeCountLine(7, LINE10));
		expectedOutput.appendLine(LINE11);
		expectedOutput.appendLine(makeCountLine(8, LINE12));
		expectedOutput.appendLine(makeCountLine(9, LINE13));
		expectedOutput.appendLine(LINE14);
//		expectedOutput.appendLine(makeCountLine(LINE15));
		assertCat(input, Cat.Options.squeezeEmptyLines.numberNonBlankLines, expectedOutput);
	}

	private String makeCountLine(int count, String line) {
		return StringUtil.fixSizeString(5, false, ' ', count) + "  " + line;
	}

	private void assertCat(final MultilineString input, CatOptions options, final MultilineString expectedOutput){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).cat(options).toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
}
