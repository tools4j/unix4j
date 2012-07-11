package org.unix4j.builder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.io.StreamOutput;
import org.unix4j.unix.*;

import java.io.File;

public class GenericCommandBuilderTest {

	private Unix4jCommandBuilder unix4j;
	private Output output;

	@SuppressWarnings("unchecked")
	private static Unix4jCommandBuilder createCommandBuilder(Input input) {
		final DefaultCommandBuilder defaultCommandBuilder = input == null ? new DefaultCommandBuilder() : new DefaultCommandBuilder(input);
		return GenericCommandBuilder.createCommandBuilder(Unix4jCommandBuilder.class, defaultCommandBuilder, Ls.FACTORY, Grep.FACTORY, Echo.FACTORY, Sort.FACTORY, Cut.FACTORY, Sed.FACTORY, Wc.FACTORY, Head.FACTORY, Tail.FACTORY, Xargs.FACTORY);
	}
	@Before
	public void beforeEach() {
		unix4j = createCommandBuilder(null);
		output = null;
	}

	@After
	public void afterEach() {
		System.out.println(">>> " + unix4j);
		if (output == null) {
			unix4j.execute();
		} else {
			unix4j.execute(output);
		}
	}

	@Test
	public void testLs() {
		unix4j.ls();
	}
	@Test
	public void testLsFile() {
		unix4j.ls(new File("src"));
	}
	@Test
	public void testLsSort() {
		unix4j.ls().sort();
	}
	@Test
	public void testLsSortDesc() {
		unix4j.ls().sort(Sort.Option.descending);
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
		unix4j.echo("Hello WORLD").grep("world", Grep.Option.ignoreCase);
	}
	@Test
	public void testLsXargsEcho() {
		unix4j.ls().xargs().echo("XARGS OUTPUT:", Xargs.XARG);
	}
//TODO Need to load a file from the resources folder
//	@Test
//	public void testFromFile() {
//		unix4j = createCommandBuilder(new FileInput(new File(".classpath"))).sort();
//	}
	@Test
	public void testToSystemError() {
		output = new StreamOutput(System.err);
		unix4j.echo("Hello ERROR").grep("error", Grep.Option.ignoreCase);
	}
}
