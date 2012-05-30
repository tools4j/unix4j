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
		runTest(new Echo().withArg(Echo.Argument.message, "hello world"));
	}
	@Test
	public void testEcho3args() {
		runTest(new Echo().withArgs(Echo.Argument.message, "hello world", "my world", "junk"));
	}
	@Test
	public void testEchoGrep() {
		runTest(new Echo().withArg(Echo.Argument.message, "hello world").join(new Grep().withArg(Grep.Argument.expression, "world")));
		runTest(new Echo().withArg(Echo.Argument.message, "hello world").join(new Grep().withArg(Grep.Argument.expression, "bla")));
	}
	@Test
	public void testEchoGrep2() {
		runTest(new Echo().withArg(Echo.Argument.message, "hello world").join(new Grep()).withArg(Grep.Argument.expression, "world"));
		runTest(new Echo().withArg(Echo.Argument.message, "hello world").join(new Grep()).withArg(Grep.Argument.expression, "bla"));
	}
	@Test
	public void testLs() {
		runTest(new Ls());
	}
	@Test
	public void testLsGrep() {
		runTest(new Ls().join(new Grep().withArg(Grep.Argument.expression, "src")));
	}
	@Test
	public void testLsGrep2() {
		runTest(new Ls().join(new Grep()).withArg(Grep.Argument.expression, "src"));
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
		runTest(new Ls().join(new Grep().withArg(Grep.Argument.expression, "SRC").withOpt(Grep.Option.ignoreCase)).join(new Sort().withOpt(Sort.Option.desc)));
	}
	@Test
	public void testLsGrepSort2() {
		runTest(new Ls().join(new Grep()).withArg(Grep.Argument.expression, "SRC").withOpt(Grep.Option.ignoreCase).join(new Sort()).withOpt(Sort.Option.desc));
	}
	@Test
	public void testSortFileLines() {
		runTest(new Sort().readFrom(new FileInput(".classpath")));
	}
	@Test
	public void testLsXargsEcho() {
		runTest(new Ls().join(new Xargs().withArg(Xargs.Argument.L, 1).withTarget(new Echo().withArg(Echo.Argument.message, "OUTPUT FILE: "), Echo.Argument.message)));
	}
	
	private void runTest(Command<?> command) {
		System.out.println("*** running: " + command);
		command.execute();
	}
}
