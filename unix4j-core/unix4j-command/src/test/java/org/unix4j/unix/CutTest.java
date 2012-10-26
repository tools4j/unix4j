package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.StringOutput;
import org.unix4j.util.ArrayUtil;
import org.unix4j.util.MultilineString;
import org.unix4j.util.Range;

import java.util.Arrays;

import static java.lang.String.format;
import static junit.framework.Assert.assertTrue;

public class CutTest {
	private final static MultilineString input;

	static {
		input = new MultilineString();
		input.appendLine("row1col1,row1col2,row1col3,row1col4,row1col5")
				.appendLine("row2col1,row2col2,row2col3,row2col4,row2col5")
				.appendLine("row3col1,row3col2,row3col3,row3col4,row3col5")
				.appendLine(",row4col2,row4col3,row4col4,row4col5")
				.appendLine("row5col1,row5col2,row5col3,row5col4,")
				.appendLine(",,,,")
				.appendLine("")
				.appendLine("row8col1,row8col2,row8col3,row8col4,row8col5");

	}

	@Test
	public void testFieldCut1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("row1col1")
				.appendLine("row2col1")
				.appendLine("row3col1")
				.appendLine("")
				.appendLine("row5col1")
				.appendLine("")
				.appendLine("")
				.appendLine("row8col1");
		assertFieldCut(input, expectedOutput, 1);
	}

	@Test
	public void testFieldCut2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("row1col2")
				.appendLine("row2col2")
				.appendLine("row3col2")
				.appendLine("row4col2")
				.appendLine("row5col2")
				.appendLine("")
				.appendLine("")
				.appendLine("row8col2");
		assertFieldCut(input, expectedOutput, 2);
	}

	@Test
	public void testFieldCut3() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("row1col1,row1col2")
				.appendLine("row2col1,row2col2")
				.appendLine("row3col1,row3col2")
				.appendLine(",row4col2")
				.appendLine("row5col1,row5col2")
				.appendLine(",")
				.appendLine("")
				.appendLine("row8col1,row8col2");
		assertFieldCut(input, expectedOutput, 1, 2);
	}

	@Test
	public void testFieldCut4() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("row1col1,row1col3,row1col5")
				.appendLine("row2col1,row2col3,row2col5")
				.appendLine("row3col1,row3col3,row3col5")
				.appendLine(",row4col3,row4col5")
				.appendLine("row5col1,row5col3,")
				.appendLine(",,")
				.appendLine("")
				.appendLine("row8col1,row8col3,row8col5");
		assertFieldCut(input, expectedOutput, 5, 3, 1);
	}

	@Test
	public void testFieldCutWithDefaultDelimiter() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("row1col1,row1col2,row1col3,row1col4,row1col5")
				.appendLine("row2col1,row2col2,row2col3,row2col4,row2col5")
				.appendLine("row3col1,row3col2,row3col3,row3col4,row3col5")
				.appendLine(",row4col2,row4col3,row4col4,row4col5")
				.appendLine("row5col1,row5col2\trow5col3,row5col4,")
				.appendLine(",,,,")
				.appendLine("")
				.appendLine("row8col1,row8col2,row8col3,row8col4,row8col5");

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("row1col1,row1col2,row1col3,row1col4,row1col5")
				.appendLine("row2col1,row2col2,row2col3,row2col4,row2col5")
				.appendLine("row3col1,row3col2,row3col3,row3col4,row3col5")
				.appendLine(",row4col2,row4col3,row4col4,row4col5")
				.appendLine("row5col1,row5col2")
				.appendLine(",,,,")
				.appendLine("")
				.appendLine("row8col1,row8col2,row8col3,row8col4,row8col5");

		final StringOutput actualStringOutput = new StringOutput();
		Unix4j.from(input.toInput()).cut(Cut.Options.fields, 1).toOutput(actualStringOutput);
		final MultilineString actualOutput = new MultilineString(actualStringOutput.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	private void assertFieldCut(final MultilineString input, final MultilineString expectedOutput, int... fields) {
		final StringOutput actualStringOutput = new StringOutput();
		Unix4j.from(input.toInput()).cut(Cut.Options.fields, ",", ',', fields).toOutput(actualStringOutput);
		final MultilineString actualOutput = new MultilineString(actualStringOutput.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test
	public void testRangeCut1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("r")
				.appendLine("r")
				.appendLine("r")
				.appendLine(",")
				.appendLine("r")
				.appendLine(",")
				.appendLine("")
				.appendLine("r");
		assertRangeCut(input, expectedOutput, 1, 1);
	}

	@Test
	public void testRangeCut2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("ro")
				.appendLine("ro")
				.appendLine("ro")
				.appendLine(",r")
				.appendLine("ro")
				.appendLine(",,")
				.appendLine("")
				.appendLine("ro");
		assertRangeCut(input, expectedOutput, 1, 2);
	}

	@Test
	public void testRangeCut3() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("ow1co")
				.appendLine("ow2co")
				.appendLine("ow3co")
				.appendLine("row4c")
				.appendLine("ow5co")
				.appendLine(",,,")
				.appendLine("")
				.appendLine("ow8co");
		assertRangeCut(input, expectedOutput, 2, 6);
	}

	@Test
	public void testRangeCut4() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput.
				appendLine("row1col1,row1col2,row1col3,row1col4,row1col5")
				.appendLine("row2col1,row2col2,row2col3,row2col4,row2col5")
				.appendLine("row3col1,row3col2,row3col3,row3col4,row3col5")
				.appendLine(",row4col2,row4col3,row4col4,row4col5")
				.appendLine("row5col1,row5col2,row5col3,row5col4,")
				.appendLine(",,,,")
				.appendLine("")
				.appendLine("row8col1,row8col2,row8col3,row8col4,row8col5");

		assertRangeCut(input, expectedOutput, 1, 1000);
	}

	private void assertRangeCut(final MultilineString input, final MultilineString expectedOutput, int start, int length) {
		final StringOutput actualStringOutput = new StringOutput();
		Unix4j.from(input.toInput()).cut(Cut.Options.chars, Range.between(start, length)).toOutput(actualStringOutput);
		final MultilineString actualOutput = new MultilineString(actualStringOutput.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test
	public void testCharacterCut1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("r")
				.appendLine("r")
				.appendLine("r")
				.appendLine(",")
				.appendLine("r")
				.appendLine(",")
				.appendLine("")
				.appendLine("r");
		assertCharacterCut(input, expectedOutput, 1);
	}

	@Test
	public void testCharacterCut2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("ro")
				.appendLine("ro")
				.appendLine("ro")
				.appendLine(",r")
				.appendLine("ro")
				.appendLine(",,")
				.appendLine("")
				.appendLine("ro");
		assertCharacterCut(input, expectedOutput, 1, 2);
	}

	@Test
	public void testCharacterCut3() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("ocr")
				.appendLine("ocr")
				.appendLine("ocr")
				.appendLine("r4,")
				.appendLine("ocr")
				.appendLine(",")
				.appendLine("")
				.appendLine("ocr");
		assertCharacterCut(input, expectedOutput, 2, 5, 10, 1000);
	}

	@Test
	public void testCharacterCut4() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("1c")
				.appendLine("2c")
				.appendLine("3c")
				.appendLine("w4")
				.appendLine("5c")
				.appendLine(",")
				.appendLine("")
				.appendLine("8c");
		assertCharacterCut(input, expectedOutput, 4, 5, 1000);
	}

	private void assertCharacterCut(final MultilineString input, final MultilineString expectedOutput, int... characters) {
		final StringOutput actualStringOutput = new StringOutput();
		Unix4j.from(input.toInput()).cut(Cut.Options.chars, Range.of(characters)).toOutput(actualStringOutput);
		final MultilineString actualOutput = new MultilineString(actualStringOutput.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test
	public void testSplitIncludeTrailingEmptyStrings(){
		final String[] expected1 = {"1","2","3","4"};
		assertArraysEqual(expected1, "1,2,3,4".split(",", -1));

		final String[] expected2 = {"","2","3","4"};
		assertArraysEqual(expected2, ",2,3,4".split(",", -1));

		final String[] expected3 = {"","2","3",""};
		assertArraysEqual(expected3, ",2,3,".split(",", -1));

		final String[] expected4 = {"","","",""};
		assertArraysEqual(expected4, ",,,".split(",", -1));
	}

	private void assertArraysEqual(final String[] expected, final String[] actual){
		final String error = format("Expected {%s} does not equal actual:{%s}", ArrayUtil.join(expected, ","), ArrayUtil.join(actual, ","));
		assertTrue(error, Arrays.equals(expected, actual));
	}
}
