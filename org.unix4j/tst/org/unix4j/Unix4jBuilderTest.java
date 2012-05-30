package org.unix4j;

import org.junit.Test;
import org.unix4j.cmd.Grep;
import org.unix4j.cmd.Sort;

public class Unix4jBuilderTest {

	@Test
	public void testEcho() {
		runTest(Unix4jBuilder.echo("hello world"));
	}

	@Test
	public void testEchoGrep() {
		runTest(Unix4jBuilder.echo("hello world").grep("world"));
		runTest(Unix4jBuilder.echo("hello world").grep("bla"));
	}

	@Test
	public void testLs() {
		runTest(Unix4jBuilder.ls());
	}

	@Test
	public void testLsGrep() {
		runTest(Unix4jBuilder.ls().grep("src"));
	}

	@Test
	public void testLsSort() {
		runTest(Unix4jBuilder.ls().sort(Sort.Option.desc));
	}

	@Test
	public void testLsGrepSort() {
		runTest(Unix4jBuilder.ls().grep("SRC", Grep.Option.ignoreCase).sort(Sort.Option.desc));
	}

	@Test
	public void testLsXargsEcho() {
		runTest(Unix4jBuilder.ls().xargs(1).echo("OUTPUT FILE: "));
	}


	private void runTest(Unix4jBuilder unix4jBuilder) {
		System.out.println("*** running: " + unix4jBuilder);
		unix4jBuilder.execute();
	}
}