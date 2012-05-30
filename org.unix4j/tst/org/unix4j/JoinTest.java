package org.unix4j;

import org.junit.Test;
import org.unix4j.cmd.Echo;
import org.unix4j.cmd.Grep;
import org.unix4j.cmd.Ls;
import org.unix4j.cmd.Sort;
import org.unix4j.cmd.Xargs;
import org.unix4j.io.FileInput;

public class JoinTest {

	@Test
	public void testEcho() {
		runTest(new Echo().withArg("hello world"));
	}
	@Test
	public void testEcho3args() {
		runTest(new Echo().withArgs("hello world", "my world", "junk"));
	}
	@Test
	public void testEchoGrep() {
		runTest(new Echo().withArg("hello world").join(new Grep().withArg("world")));
		runTest(new Echo().withArg("hello world").join(new Grep().withArg("bla")));
	}
	@Test
	public void testEchoGrep2() {
		runTest(new Echo().withArg("hello world").join(new Grep()).withArg("world"));
		runTest(new Echo().withArg("hello world").join(new Grep()).withArg("bla"));
	}
	@Test
	public void testLs() {
		runTest(new Ls());
	}
	@Test
	public void testLsGrep() {
		runTest(new Ls().join(new Grep().withArg("src")));
	}
	@Test
	public void testLsGrep2() {
		runTest(new Ls().join(new Grep()).withArg("src"));
	}
	@Test
	public void testLsSort() {
		runTest(new Ls().join(new Sort().withOpt(Sort.Option.desc)));
	}
	@Test
	public void testLsSort2() {
		runTest(new Ls().join(new Sort()).withOpt(Sort.Option.desc));
	}
	@Test
	public void testLsGrepSort() {
		runTest(new Ls().join(new Grep().withArg("SRC").withOpt(Grep.Option.ignoreCase)).join(new Sort().withOpt(Sort.Option.desc)));
	}
	@Test
	public void testLsGrepSort2() {
		runTest(new Ls().join(new Grep()).withArg("SRC").withOpt(Grep.Option.ignoreCase).join(new Sort()).withOpt(Sort.Option.desc));
	}
	@Test
	public void testSortFileLines() {
		runTest(new Sort().readFrom(new FileInput(".classpath")));
	}
	@Test
	public void testLsXargsEcho() {
		runTest(new Ls().join(new Xargs().withArg(Xargs.Option.L, "1").withTarget(new Echo().withArg(Echo.Option.string, "OUTPUT FILE: "))));
	}

	private void runTest(Command<?> command) {
		System.out.println("*** running: " + command);
		command.execute();
	}
}
