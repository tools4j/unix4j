package org.unix4j.util;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.List;

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


    @Test
    public void testExpandFiles_absolutePaths(){
        final File testDir = FileTestUtils.getTestFile(this.getClass(), "testExpandFiles");

        List<File> actualFiles = FileUtil.expandFiles(testDir.getPath() + "/*");
        FileAsserter asserter = new FileAsserter(actualFiles);
        asserter.assertAbsolute(testDir, "blah");
        asserter.assertAbsolute(testDir, "dir1");
        asserter.assertAbsolute(testDir, "dir2");
        asserter.assertAbsolute(testDir, "dir3");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertAbsolute(testDir.getPath());

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah.txt");
        asserter = new FileAsserter(actualFiles);
        asserter.assertAbsolute(testDir, "blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertAbsolute(testDir, "blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/dir*/test*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertAbsolute(testDir, "dir1/test-file-1.txt");
        asserter.assertAbsolute(testDir, "dir1/test-file-1a.txt");
        asserter.assertAbsolute(testDir, "dir2/test-file-2.txt");
        asserter.assertAbsolute(testDir, "dir3/test-file-3.txt");
    }

    @Test
    public void testExpandFiles_relativePaths(){
        final File testDir = FileTestUtils.getTestFile(this.getClass(), "testExpandFiles");

        List<File> actualFiles = FileUtil.expandFiles(testDir.getPath() + "/*");
        FileAsserter asserter = new FileAsserter(actualFiles);
        asserter.assertRelative(testDir, "blah");
        asserter.assertRelative(testDir, "dir1");
        asserter.assertRelative(testDir, "dir2");
        asserter.assertRelative(testDir, "dir3");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertRelative(testDir, ".");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah.txt");
        asserter = new FileAsserter(actualFiles);
        asserter.assertRelative(testDir, "./blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertRelative(testDir, "./blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/dir*/test*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertRelative(testDir, "./dir1/test-file-1.txt");
        asserter.assertRelative(testDir, "./dir1/test-file-1a.txt");
        asserter.assertRelative(testDir, "./dir2/test-file-2.txt");
        asserter.assertRelative(testDir, "./dir3/test-file-3.txt");
    }


	@Test
    public void testFiles(){
        final File testDir =  FileTestUtils.getTestFile(this.getClass(), "testFiles");
        assertTrue(new File(testDir, "blah/blah.txt").exists());
        assertTrue(new File(testDir, "./blah/blah.txt").exists());
    }
}
