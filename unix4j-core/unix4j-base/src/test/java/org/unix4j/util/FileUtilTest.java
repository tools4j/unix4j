package org.unix4j.util;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;


public class FileUtilTest {

	/*
("/home/john", "/home/john") --> "."
("/home/john", "/home/john/notes.txt") --> "notes.txt"
("/home/john", "/home/john/documents/important") --> "./documents/important"
("/home/john", "/home/smith/public") --> "../smith/public"
("/home/john", "/var/tmp/test.out") --> "/var/tmp/test.out"
	 */
	@Test
	public void testRelativePath_1_equal() {
		final String actual = FileUtil.getRelativePath(new File("/home/john"), new File("/home/john"));
		Assert.assertEquals(".", actual);
	}
	@Test
	public void testRelativePath_2_directParent() {
		final String actual = FileUtil.getRelativePath(new File("/home/john"), new File("/home/john/notes.txt"));
		Assert.assertEquals("notes.txt", actual);
	}
	@Test
	public void testRelativePath_3_indirectParent() {
		final String actual = FileUtil.getRelativePath(new File("/home/john"), new File("/home/john/documents/important"));
		Assert.assertEquals("./documents/important", actual);
	}
	@Test
	public void testRelativePath_4_commonAncestor() {
		final String actual1 = FileUtil.getRelativePath(new File("/home/john"), new File("/home/smith/public/holidays.pdf"));
		Assert.assertEquals("../smith/public/holidays.pdf", actual1);
		final String actual2 = FileUtil.getRelativePath(new File("/home/john/documents"), new File("/home/smith/public/holidays.pdf"));
		Assert.assertEquals("../../smith/public/holidays.pdf", actual2);
	}
	@Test
	public void testRelativePath_5_noCommonAncestor() {
		final String actual = FileUtil.getRelativePath(new File(FileUtil.ROOT + "home/john"), new File(FileUtil.ROOT + "var/tmp/test.out"));
		Assert.assertEquals(FileUtil.ROOT.replace('\\', '/') + "var/tmp/test.out", actual);
	}
}
