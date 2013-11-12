package org.unix4j.unix;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.Input;
import org.unix4j.io.StringInput;
import org.unix4j.util.StackTraceUtil;
import org.unix4j.variable.Arg;

//@Ignore
public class XargsTest {
	
	private static final String LINE_0 = "hello world";
	private static final String LINE_1 = "  line one";
	private static final String LINE_2 = "  \ttabs\t\t and\tspaces    \t";
	private static final String LINE_3 = "";
	private static final String LINE_4 = "Bla";
	private static final String LINE_5 = "A\0B\0C\0\0D";
	
	private Input input;
	private final List<String> expectedLines = new ArrayList<String>();
	
	@Before
	public void beforeEach() {
		input = new StringInput(LINE_0, LINE_1, LINE_2, LINE_3, LINE_4, LINE_5);
		expectedLines.clear();
	}
	
	@Test
	public void testXargsWithDefaultCommand() {
		expect("hello world");
		expect("line one");
		expect("tabs and spaces Bla");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs().toStringList());
	}

	@Test
	public void testXargsWithEchoAndNoVariable() {
		expect("");
		expect("");
		expect("");
		expect("");
		actual(Unix4j.from(input).cat().xargs().echo().toStringList());
	}

	@Test
	public void testXargsWithEchoAndArgsVariable() {
		expect("hello world");
		expect("line one");
		expect("tabs and spaces Bla");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.$all).toStringList());
	}
	@Test
	public void testXargsWithEchoAndArgsFrom0Variable() {
		expect("hello world");
		expect("line one");
		expect("tabs and spaces Bla");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.argsFrom(0)).toStringList());
	}

	@Test
	public void testXargsWithEchoAndArg0Variable() {
		expect("hello");
		expect("line");
		expect("tabs");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.$0).toStringList());
	}
	
	@Test
	public void testXargsWithEchoAndArg0Variable2() {
		expect("hello");
		expect("line");
		expect("tabs");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.arg(0)).toStringList());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testXargsWithEchoAndArg1Variable() {
		actual(Unix4j.from(input).cat().xargs().echo(Arg.$1).toStringList());
	}

	@Test
	public void testXargsWithEchoAndArgsFrom1Variable() {
		expect("world");
		expect("one");
		expect("and spaces Bla");
		expect("");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.argsFrom(1)).toStringList());
	}

	@Test
	public void testXargsWithEchoAndArg0ThenArgsFrom2Variable() {
		expect("hello");
		expect("line");
		expect("tabs spaces Bla");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.$0, Arg.argsFrom(2)).toStringList());
	}

	@Test
	public void testXargsWithDefaultCommandAndMaxArgs1() {
		expect("hello");
		expect("world");
		expect("line");
		expect("one");
		expect("tabs");
		expect("and");
		expect("spaces");
		expect("Bla");
		expect("A\0B\0C\0\0D");
		actual(Unix4j.from(input).cat().xargs(1).toStringList());
	}

	@Test
	public void testXargsWithEchoAndArgsVariableThenSortThenGrep() {
		expect("A\0B\0C\0\0D");
		expect("tabs and spaces Bla");
		actual(Unix4j.from(input).cat().xargs().echo(Arg.$all).sort().grep("B").toStringList());
	}
	
	//Unix4j.find(dictFolder.getAbsolutePath(), "data.*").xargs().tail(Tail.Options.s, "26", Arg.$all).toStdOut();
	@Test
	public void testXargsWithTailAndSomeOptions() {
		expect("A\0B\0C\0\0D");
		expect("tabs and spaces Bla");
		//actual(Unix4j.find(new File(System.getProperty("user.home")).getAbsolutePath()).xargs().tail(Tail.Options.s, 2, Arg.$all).toStringList());
//		actual(Unix4j.find(Find.Options.typeFile, "*.txt").xargs().tail("-s", "--count", "10", "--paths", Arg.$all).toStringList());
		System.out.println(Unix4j.find(Find.Options.typeFile, "*.txt").xargs().tail("-s", "--count", "1", "--paths", Arg.$0).toString());
		actual(Unix4j.find(Find.Options.typeFile, "*.txt").xargs(1).tail("-s", "--count", "-10000", "--paths", Arg.$0).toStringList());
//		actual(Unix4j.from(input).tail("-s", "--count", "1").toStringList());
		//actual(Unix4j.find(Find.Options.typeFile, "*.txt").xargs().tail("-s", "--count", "1", "--paths", Arg.$all).toStringList());
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
			final StackTraceElement testMethod = StackTraceUtil.getCurrentMethodStackTraceElement(1);
			System.err.println(">>> TEST: class=" + testMethod.getClassName() + ", method=" + testMethod.getMethodName() + "()");
			final int max = Math.max(expectedLines.size(), actualLines.size());
			for (int i = 0; i < max; i++) {
				final String exp = i < expectedLines.size() ? expectedLines.get(i) : "<none>"; 
				final String act = i < actualLines.size() ? actualLines.get(i) : "<none>";
				final String prefix = (exp != null && act != null && exp.equals(act)) ? "..(ok)..." : "..(ERR).."; 
				System.err.println(prefix + "EXP[" + i + "]=" + exp);
				System.err.println(prefix + "ACT[" + i + "]=" + act);
			}
			throw e;
		}
	}
}
