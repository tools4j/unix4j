package org.unix4j.unix;

import org.junit.Ignore;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.IoTestStub;

/**
 * Not the most elegant way of asserting order.  Probably better to
 * use a mock framework.  Couldn't get it working quickly so wrote a manual
 * stubbing tool.
 */
public class LineByLineTest {

	@Test
	public void testLineByLine_singleHead() throws Exception {

		final IoTestStub input = new IoTestStub("input");
		final IoTestStub output = new IoTestStub("output");

		input.addLine("1").addLine("2").addLine("3").addLine("4").addLine("5");
		IoTestStub.startRecording();

		Unix4j.fromInput(input).head(5).toOutput(output);

		IoTestStub.stopRecordingAndVerify()
				.expect(input,  IoTestStub.ActionType.READ,  "1")
				.expect(output, IoTestStub.ActionType.WRITE, "1")
				.expect(input,  IoTestStub.ActionType.READ,  "2")
				.expect(output, IoTestStub.ActionType.WRITE, "2")
				.expect(input,  IoTestStub.ActionType.READ,  "3")
				.expect(output, IoTestStub.ActionType.WRITE, "3")
				.expect(input,  IoTestStub.ActionType.READ,  "4")
				.expect(output, IoTestStub.ActionType.WRITE, "4")
				.expect(input,  IoTestStub.ActionType.READ,  "5")
				.expect(output, IoTestStub.ActionType.WRITE, "5")
				.finish();
	}

	@Ignore
	@Test
	public void testLineByLine_headPipedIntoHead() throws Exception {

		final IoTestStub input = new IoTestStub("input");
		final IoTestStub output = new IoTestStub("output");

		input.addLine("1").addLine("2").addLine("3").addLine("4").addLine("5");
		IoTestStub.startRecording();

		Unix4j.fromInput(input).head(5).head(5).toOutput(output);

		IoTestStub.stopRecordingAndVerify()
				.expect(input,  IoTestStub.ActionType.READ,  "1")
				.expect(output, IoTestStub.ActionType.WRITE, "1")
				.expect(input,  IoTestStub.ActionType.READ,  "2")
				.expect(output, IoTestStub.ActionType.WRITE, "2")
				.expect(input,  IoTestStub.ActionType.READ,  "3")
				.expect(output, IoTestStub.ActionType.WRITE, "3")
				.expect(input,  IoTestStub.ActionType.READ,  "4")
				.expect(output, IoTestStub.ActionType.WRITE, "4")
				.expect(input,  IoTestStub.ActionType.READ,  "5")
				.expect(output, IoTestStub.ActionType.WRITE, "5")
				.finish();
	}

	@Test
	public void testLineByLine_headPipedIntoGrep() throws Exception {

		final IoTestStub input = new IoTestStub("input");
		final IoTestStub output = new IoTestStub("output");

		input.addLine("1").addLine("2").addLine("3").addLine("4").addLine("5");
		IoTestStub.startRecording();

		Unix4j.fromInput(input).head(5).grep("blah", Grep.Option.invert).toOutput(output);

		IoTestStub.stopRecordingAndVerify()
				.expect(input,  IoTestStub.ActionType.READ,  "1")
				.expect(output, IoTestStub.ActionType.WRITE, "1")
				.expect(input,  IoTestStub.ActionType.READ,  "2")
				.expect(output, IoTestStub.ActionType.WRITE, "2")
				.expect(input,  IoTestStub.ActionType.READ,  "3")
				.expect(output, IoTestStub.ActionType.WRITE, "3")
				.expect(input,  IoTestStub.ActionType.READ,  "4")
				.expect(output, IoTestStub.ActionType.WRITE, "4")
				.expect(input,  IoTestStub.ActionType.READ,  "5")
				.expect(output, IoTestStub.ActionType.WRITE, "5")
				.finish();
	}

	@Ignore
	@Test
	public void testLineByLine_grepPipedIntoHead() throws Exception {

		final IoTestStub input = new IoTestStub("input");
		final IoTestStub output = new IoTestStub("output");

		input.addLine("1").addLine("2").addLine("3").addLine("4").addLine("5");
		IoTestStub.startRecording();

		Unix4j.fromInput(input).grep("blah", Grep.Option.invert).head(5).toOutput(output);

		IoTestStub.stopRecordingAndVerify()
				.expect(input,  IoTestStub.ActionType.READ,  "1")
				.expect(output, IoTestStub.ActionType.WRITE, "1")
				.expect(input,  IoTestStub.ActionType.READ,  "2")
				.expect(output, IoTestStub.ActionType.WRITE, "2")
				.expect(input,  IoTestStub.ActionType.READ,  "3")
				.expect(output, IoTestStub.ActionType.WRITE, "3")
				.expect(input,  IoTestStub.ActionType.READ,  "4")
				.expect(output, IoTestStub.ActionType.WRITE, "4")
				.expect(input,  IoTestStub.ActionType.READ,  "5")
				.expect(output, IoTestStub.ActionType.WRITE, "5")
				.finish();
	}

	@Test
	public void testLineByLine_sortPipedIntoHead() throws Exception {

		/*
		Because sort needs the whole input before sorting can begin
		all of the input will be read before anything is written.
		 */

		final IoTestStub input = new IoTestStub("input");
		final IoTestStub output = new IoTestStub("output");

		input.addLine("1").addLine("2").addLine("3").addLine("4").addLine("5");
		IoTestStub.startRecording();

		Unix4j.fromInput(input).sort().toOutput(output);

		IoTestStub.stopRecordingAndVerify()
				.expect(input,  IoTestStub.ActionType.READ,  "1")
				.expect(input,  IoTestStub.ActionType.READ,  "2")
				.expect(input,  IoTestStub.ActionType.READ,  "3")
				.expect(input,  IoTestStub.ActionType.READ,  "4")
				.expect(input,  IoTestStub.ActionType.READ,  "5")
				.expect(output, IoTestStub.ActionType.WRITE, "1")
				.expect(output, IoTestStub.ActionType.WRITE, "2")
				.expect(output, IoTestStub.ActionType.WRITE, "3")
				.expect(output, IoTestStub.ActionType.WRITE, "4")
				.expect(output, IoTestStub.ActionType.WRITE, "5")
				.finish();
	}

	@Test
	public void testLineByLine_grepPipedIntoWc() throws Exception {

		/*
		Because wc needs the whole input before sorting can begin
		all of the input will be read before anything is written.
		What is written is the number of lines. "5"
		 */

		final IoTestStub input = new IoTestStub("input");
		final IoTestStub output = new IoTestStub("output");

		input.addLine("1").addLine("2").addLine("3").addLine("4").addLine("5");
		IoTestStub.startRecording();

		Unix4j.fromInput(input).grep("\\d").wcCountLines().toOutput(output);

		IoTestStub.stopRecordingAndVerify()
				.expect(input,  IoTestStub.ActionType.READ,  "1")
				.expect(input,  IoTestStub.ActionType.READ,  "2")
				.expect(input,  IoTestStub.ActionType.READ,  "3")
				.expect(input,  IoTestStub.ActionType.READ,  "4")
				.expect(input,  IoTestStub.ActionType.READ,  "5")
				.expect(output, IoTestStub.ActionType.WRITE, "5")
				.finish();
	}
}
