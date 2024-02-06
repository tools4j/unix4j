package org.unix4j.unix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.FileUtil;

public class LsTest {

	private Unix4jCommandBuilder unix4j;

	@Before
	public void beforeEach() {
		unix4j = Unix4j.builder();
	}

	@After
	public void afterEach() {
		unix4j.toStdOut();
		System.out.println("*** THIS WAS COMMAND: " + unix4j);
		System.out.println();
	}

	@Test
	public void testLs() {
		unix4j.ls();
	}

	@Test
	public void testLsLong() {
		unix4j.ls(Ls.Options.l);
	}

	@Test
	public void testLsAllLong() {
		unix4j.ls(Ls.Options.longFormat.allFiles);
	}

	@Test
	public void testLsALH() {
		unix4j.ls(Ls.Options.l.a.h, FileUtil.getUserDir());
	}

	@Test
	public void testLsLARTH() {
		unix4j.ls(Ls.Options.l.a.r.t.h, FileUtil.getUserDir());
	}
}
