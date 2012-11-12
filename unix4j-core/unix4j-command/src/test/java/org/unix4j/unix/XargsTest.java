package org.unix4j.unix;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.Input;
import org.unix4j.io.StringInput;
import org.unix4j.unix.xargs.Xarg;

@Ignore
public class XargsTest {
	
	private static final String LINE_0 = "hello world";
	private static final String LINE_1 = "  line one";
	private static final String LINE_2 = "  \ttabs\t\t and\tspaces    \t";
	private static final String LINE_3 = "";
	private static final String LINE_4 = "Bla";
	private static final String LINE_5 = "A\0B\0C\0\0E";
	
	private Input input;
	private final List<String> expectedLines = new ArrayList<String>();
	
	@Before
	public void beforeEach() {
		input = new StringInput(LINE_0, LINE_1, LINE_2, LINE_3, LINE_4, LINE_5);
		expectedLines.clear();
	}
	
	@Test
	public void testXargs() {
		Unix4j.ls().xargs().withVariables().echo(Xarg.$strings).toStdOut();

		expect("hello world");
		expect("line one");
		expect("tabs and spaces");
		expect("");
		expect("Bla");
		expect("A\0B\0C\0\0E");
		actual(Unix4j.from(input).cat().xargs().toStringList());
	}

	private void expect(String... expectedLines) {
		for (final String expectedLine : expectedLines) {
			this.expectedLines.add(expectedLine);
		}
	}
	private void actual(List<String> actualLines) {
		try {
			Assert.assertEquals("number of lines", expectedLines.size(), actualLines.size());
			for (int i = 0; i < expectedLines.size(); i++) {
				Assert.assertEquals("line[" + i + "]", expectedLines.get(i), actualLines.get(i));
			}
		} catch (AssertionError e) {
			final int max = Math.max(expectedLines.size(), actualLines.size());
			for (int i = 0; i < max; i++) {
				final String exp = i < expectedLines.size() ? expectedLines.get(i) : null; 
				final String act = i < actualLines.size() ? actualLines.get(i) : null;
				final String prefix = (exp != null && act != null && exp.equals(act)) ? "..(ok)..." : "..(ERR).."; 
				System.err.println(prefix + "EXP[" + i + "]=" + exp);
				System.err.println(prefix + "ACT[" + i + "]=" + act);
			}
			throw e;
		}
	}
}
