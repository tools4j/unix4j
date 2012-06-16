package org.unix4j;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.command.impl.Grep;
import org.unix4j.command.impl.Sort;
import org.unix4j.command.impl.Xargs;
import org.unix4j.io.Output;
import org.unix4j.io.StreamOutput;

public class CommandBuilderTest {
	
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
	@Test
	public void testFromFile() {
		unix4j = Unix4j.builder(new File(".classpath")).sort();
	}
	@Test
	public void testToSystemError() {
		output = new StreamOutput(System.err);
		unix4j.echo("Hello ERROR").grep("error", Grep.Option.ignoreCase);
	}
}
