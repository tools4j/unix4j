package org.unix4j.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.unix.Unix4jCommandBuilder;
import org.unix4j.io.Output;
import org.unix4j.io.StreamOutput;
import org.unix4j.unix.Cut;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Sort;
import org.unix4j.unix.grep.GrepOption;
import org.unix4j.util.Range;
import org.unix4j.variable.Arg;

public class ExampleTest {

	private Unix4jCommandBuilder unix4j;
	private Output output;

	@Before
	public void beforeEach() {
		unix4j = Unix4j.builder();
		output = null;
	}

	@After
	public void afterEach() {
		System.out.println(">>> " + unix4j);
		if (output == null) {
			unix4j.toStdOut();
		} else {
			unix4j.toOutput(output);
		}
	}

	@Test
	public void testLs() {
		unix4j.ls();
	}

	@Test
	public void testLsStar() {
		unix4j.ls("*");
	}

	@Test
	public void testLsStarStarStarJava() {
		unix4j.ls("src/*/*/*/*/*.java", "src/*/*/*/*/*/*.java", "src/*/*/*/*/*/*/*.java");
	}

	@Test
	public void testLsStarStarStarJavaLA() {
		unix4j.ls(Ls.Options.l.a, "src/*/*/*/*/*.java", "src/*/*/*/*/*/*.java", "src/*/*/*/*/*/*/*.java");
	}

	@Test
	public void testLsFile() {
		unix4j.ls("src");
	}

	@Test
	public void testCut_4_5_6_7_8() {
		unix4j.fromString("some string").cut(Cut.Options.c, 4, 5, 6, 7, 8);
	}

	@Test
	public void testCut_4_to_8() {
		unix4j.fromString("some string").cut(Cut.Options.c, Range.between(4, 8));
	}

	@Test
	public void testCut_4_5_6_to_8_string_args() {
		unix4j.fromString("some string").cut("-c", "4,5,6-8");
	}

	@Test
	public void testCut_1_string_args() {
		unix4j.fromString("some string").cut("-c", "1");
	}

	@Test
	public void testCut_to_8_string_args() {
		unix4j.fromString("some string").cut("-c", "-8");
	}

	@Test
	public void testCut_4_to_8_string_args() {
		unix4j.fromString("some string").cut("-c", "4-8");
	}

	@Test
	public void testCut_4_to_string_args() {
		unix4j.fromString("some string").cut("-c", "4-");
	}

	@Test
	public void testLsSort() {
		unix4j.ls().sort();
	}

	@Test
	public void testLsSortReverse() {
		unix4j.ls().sort(Sort.Options.reverse);
	}

	@Test
	public void testEcho() {
		unix4j.echo("Hello world");
	}

	@Test
	public void testEcho2() {
		unix4j.echo("Hello", "world");
	}

	@Test
	public void testEchoGrepMatch() {
		unix4j.echo("Hello world").grep("world");
	}

	@Test
	public void testEchoGrepNoMatch() {
		unix4j.echo("Hello WORLD").grep("world");
	}

	@Test
	public void testEchoGrepMatchIgnoreCase() {
		unix4j.echo("Hello WORLD").grep(GrepOption.ignoreCase, "world");
	}

	@Test
	public void testLsXargsEcho() {
		unix4j.ls().xargs().echo("XARGS OUTPUT:", Arg.$0);
	}

	@Test
	public void testToSystemError() {
		output = new StreamOutput(System.err);
		unix4j.echo("Hello ERROR").grep(GrepOption.ignoreCase, "error");
	}
}
