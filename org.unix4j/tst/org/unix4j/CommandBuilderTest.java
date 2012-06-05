package org.unix4j;

import org.junit.Before;
import org.junit.Test;
import org.unix4j.cmd.Echo;
import org.unix4j.cmd.Grep;
import org.unix4j.cmd.Sort;
import org.unix4j.cmd.Xargs;
import org.unix4j.io.FileInput;

public class CommandBuilderTest {
	
	private CommandBuilder builder;
	
	@Before
	public void beforeEach() {
		builder = new CommandBuilder();
	}
	
	@Test
	public void testEcho() {
		builder.echo(Echo.Argument.message("hello world"));
		runTest();
	}
	@Test
	public void testEcho3args() {
		builder.echo(Echo.Argument.message("hello world", "hello world", "my world", "junk"));
		runTest();
	}
	@Test
	public void testEchoGrep() {
		builder.echo(Echo.Argument.message("hello world")).grep(Grep.Argument.expression("world"));
		runTest();
		builder.clear();
		builder.echo(Echo.Argument.message("hello world")).grep(Grep.Argument.expression("bla"));
		runTest();
	}
	@Test
	public void testLs() {
		builder.ls();
		runTest();
	}
	@Test
	public void testLsGrep() {
		builder.ls().grep(Grep.Argument.expression("src"));
		runTest();
	}
	@Test
	public void testLsSort() {
		builder.ls().sort(Sort.Option.desc);
		runTest();
	}
	@Test
	public void testLsGrepSort() {
		builder.ls().grep(Grep.Option.ignoreCase, Grep.Argument.expression("SRC")).sort(Sort.Option.desc);
		runTest();
	}
	@Test
	public void testSortFileLines() {
		builder.sort().readFrom(new FileInput(".classpath"));
		runTest();
	}
	@Test
	public void testLsXargsEcho() {
		builder.ls().xargs(Xargs.Argument.L(1), Xargs.Argument.target(new Echo().withArg(Echo.Argument.message, "OUTPUT FILE: "), Echo.Argument.message));
		runTest();
	}
	
	private void runTest() {
		final Command<?> command = builder.build();
		System.out.println("*** running: " + command);
		command.execute();
	}
}
