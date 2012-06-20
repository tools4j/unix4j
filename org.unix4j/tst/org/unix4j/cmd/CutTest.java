package org.unix4j.cmd;

import org.junit.Test;
import org.unix4j.io.ReaderInput;
import org.unix4j.io.WriterOutput;

import java.io.StringReader;
import java.io.StringWriter;

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
				.appendLine(",")
				.appendLine("row8col1,row8col2");
		assertFieldCut(input, expectedOutput, 1, 2);
	}

	@Test
	public void testFieldCut4() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("row1col5,row1col3,row1col1")
				.appendLine("row2col5,row2col3,row2col1")
				.appendLine("row3col5,row3col3,row3col1")
				.appendLine("row4col5,row4col3,")
				.appendLine(",row5col3,row5col1")
				.appendLine(",,")
				.appendLine(",,")
				.appendLine("row8col5,row8col3,row8col1");
		assertFieldCut(input, expectedOutput, 5, 3, 1);
	}

	@Test
	public void testFieldCut5() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine(",,")
				.appendLine(",,")
				.appendLine(",,")
				.appendLine(",,")
				.appendLine(",,")
				.appendLine(",,")
				.appendLine(",,")
				.appendLine(",,");
		assertFieldCut(input, expectedOutput, -1, 0, 100);
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

		final StringWriter actualOutputStringWriter = new StringWriter();
		final Cut cut = new Cut();
		cut.readFrom(new ReaderInput(new StringReader(input.toString())));
		cut.writeTo(new WriterOutput(actualOutputStringWriter));
		cut.withArgVals(Cut.Argument.field(1));
		cut.execute();
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);

	}

	private void assertFieldCut(final MultilineString input, final MultilineString expectedOutput, Integer... fields){
		final StringWriter actualOutputStringWriter = new StringWriter();
		final Cut cut = new Cut();
		cut.readFrom(new ReaderInput(new StringReader(input.toString())));
		cut.writeTo(new WriterOutput(actualOutputStringWriter));
		cut.withArgVals(Cut.Argument.field(fields));
		cut.withArgVals(Cut.Argument.delimiter(","));
		cut.execute();
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
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
		assertCharacterCut(input, expectedOutput, -2, 4, 5, 1000);
	}

	private void assertCharacterCut(final MultilineString input, final MultilineString expectedOutput, Integer... characters){
		final StringWriter actualOutputStringWriter = new StringWriter();
		final Cut cut = new Cut();
		cut.readFrom(new ReaderInput(new StringReader(input.toString())));
		cut.writeTo(new WriterOutput(actualOutputStringWriter));
		cut.withArgVals(Cut.Argument.character(characters));
		cut.execute();
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBothFieldAndCharacterArgumentsGiven(){
		final StringWriter actualOutputStringWriter = new StringWriter();
		final Cut cut = new Cut();
		cut.readFrom(new ReaderInput(new StringReader(input.toString())));
		cut.writeTo(new WriterOutput(actualOutputStringWriter));
		cut.withArgVals(Cut.Argument.character(1));
		cut.withArgVals(Cut.Argument.field(1));
		cut.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelimiterArgumentGivenWithCharacterArgument(){
		final StringWriter actualOutputStringWriter = new StringWriter();
		final Cut cut = new Cut();
		cut.readFrom(new ReaderInput(new StringReader(input.toString())));
		cut.writeTo(new WriterOutput(actualOutputStringWriter));
		cut.withArgVals(Cut.Argument.character(1));
		cut.withArgVals(Cut.Argument.delimiter(","));
		cut.execute();
	}
}
