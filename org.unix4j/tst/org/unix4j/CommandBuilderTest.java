package org.unix4j;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unix4j.impl.Grep;
import org.unix4j.impl.Sort;
import org.unix4j.impl.Xargs;
import org.unix4j.io.NullInput;
import org.unix4j.io.StdOutput;

public class CommandBuilderTest {

	private Unix4jCommandBuilder builder;
	
	@Before
	public void beforeEach() {
		builder = new CommandBuilderImpl();
	}
	
	@After
	public void afterEach() {
		Command<?> cmd = builder.build();
		System.out.println(">>> " + cmd);
		cmd.execute(new NullInput(), new StdOutput());
	}
	
	@Test
	public void testLs() {
		builder.ls();
	}
	@Test
	public void testLsFile() {
		builder.ls(new File("src"));
	}
	@Test
	public void testLsSort() {
		builder.ls().sort();
	}
	@Test
	public void testLsSortDesc() {
		builder.ls().sort(Sort.Option.descending);
	}
	@Test
	public void testEcho() {
		builder.echo("Hello world");
	}
	@Test
	public void testEcho2() {
		builder.echo("Hello", "world");
	}
	@Test
	public void testEchoGrepMatch() {
		builder.echo("Hello world").grep("world");
	}
	@Test
	public void testEchoGrepNoMatch() {
		builder.echo("Hello WORLD").grep("world");
	}
	@Test
	public void testEchoGrepMatchIgnoreCase() {
		builder.echo("Hello WORLD").grep("world", Grep.Option.ignoreCase);
	}
	@Test
	public void testLsXargsEcho() {
		builder.ls().xargs().echo("XARGS OUTPUT:", Xargs.XARG);
	}
}
