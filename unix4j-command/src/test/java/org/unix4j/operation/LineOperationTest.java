package org.unix4j.operation;

import org.junit.Assert;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.Input;
import org.unix4j.io.StringInput;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

import java.util.List;

/**
 * Unit tests for {@link LineOperation} and {@link AdHocCommand}.
 */
public class LineOperationTest {
	
	private static final String[] LINES = new String[] {
		"This is an initial line",
		"This is another line",
		"And yet another one",
		"More lines",
		"More and more",
		"and more and more",
		"And a final line",
	};
	
	@Test
	public void testPassThrough() {
		final LineOperation op = new LineOperation() {
			@Override
			public void operate(ExecutionContext context, Line input, LineProcessor output) {
				output.processLine(input);
			}
			@Override
			public String toString() {
				return "passthrough";
			}
		};
		test(op, 0, 1, 2, 3, 4, 5, 6);
		Assert.assertEquals("adhoc --operation passthrough", Unix4j.builder().apply(op).build().toString());
	}

	@Test
	public void testEveryLineTwice() {
		final LineOperation op = new LineOperation() {
			@Override
			public void operate(ExecutionContext context, Line input, LineProcessor output) {
				output.processLine(input);
				output.processLine(input);
			}
		};
		test(op, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6);
	}

	@Test
	public void testHead3() {
		final LineOperation op = new LineOperation() {
			int count = 0;
			@Override
			public void operate(ExecutionContext context, Line input, LineProcessor output) {
				output.processLine(input);
				count++;
				if (count == 3) {
					output.finish();
				}
			}
		};
		test(op, 0, 1, 2);
	}

	@Test
	public void testOnlyEvenLines() {
		final LineOperation op = new LineOperation() {
			int count = 0;
			@Override
			public void operate(ExecutionContext context, Line input, LineProcessor output) {
				if (count % 2 == 0) {
					output.processLine(input);
				}
				count++;
			}
		};
		test(op, 0, 2, 4, 6);
	}

	@Test
	public void testNoOutputAfterFinish() {
		final LineOperation op = new LineOperation() {
			@Override
			public void operate(ExecutionContext context, Line input, LineProcessor output) {
				output.processLine(input);
				output.processLine(input);
				output.finish();
				output.processLine(input);//should not be written to output
				output.processLine(input);//should not be written to output
				output.processLine(input);//should not be written to output
			}
		};
		test(op, 0, 0);
	}

	private void test(LineOperation op, int... expectedLines) {
		final Input input = new StringInput(LINES);
		final List<String> actual = Unix4j.from(input).apply(op).toStringList();
		Assert.assertEquals("number of lines in output", expectedLines.length, actual.size());
		for (int i = 0; i < expectedLines.length; i++) {
			Assert.assertEquals("line[" + i + "] = input[" + expectedLines[i] + "]", LINES[expectedLines[i]], actual.get(i));
		}
	}

}
