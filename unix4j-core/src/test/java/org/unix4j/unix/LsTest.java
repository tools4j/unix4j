package org.unix4j.unix;

import java.io.File;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.unix.Ls;

public class LsTest {
	private Unix4jCommandBuilder unix4j;
	
	@Before
	public void beforeEach() {
		unix4j = Unix4j.builder();
	}
	@After
	public void afterEach() {
		unix4j.execute();
	}
	
	@Test
	public void testLs() {
		unix4j.ls();
	}
	@Test
	public void testLsLong() {
		unix4j.ls(Ls.Option.longFormat);
	}
	@Test
	public void testLsAllLong() {
		unix4j.ls(Ls.Option.longFormat, Ls.Option.allFiles);
	}
	@Test
	public void testLsAL() {
		unix4j.ls(Collections.singletonList(new File(System.getProperty("user.home"))), Ls.Option.l, Ls.Option.a, Ls.Option.h);
	}
}
