package org.unix4j.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


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
        asserter.assertNext(testDir.getPath() + "/blah");
        asserter.assertNext(testDir.getPath() + "/dir1");
        asserter.assertNext(testDir.getPath() + "/dir2");
        asserter.assertNext(testDir.getPath() + "/dir3");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir.getPath());

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah.txt");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir.getPath() + "/blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir.getPath() + "/blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/dir*/test*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir.getPath() + "/dir1/test-file-1.txt");
        asserter.assertNext(testDir.getPath() + "/dir1/test-file-1a.txt");
        asserter.assertNext(testDir.getPath() + "/dir2/test-file-2.txt");
        asserter.assertNext(testDir.getPath() + "/dir3/test-file-3.txt");
    }

    @Ignore //doesn't work
    @Test
    public void testExpandFiles_relativePaths(){
        final File testDir = FileTestUtils.getTestFile(this.getClass(), "testExpandFiles");

        List<File> actualFiles = FileUtil.expandFiles(testDir.getPath(), "*");
        FileAsserter asserter = new FileAsserter(actualFiles);
        asserter.assertNext("blah");
        asserter.assertNext("dir1");
        asserter.assertNext("dir2");
        asserter.assertNext("dir3");

        actualFiles = FileUtil.expandFiles(testDir.getPath(), "*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir.getPath());

        actualFiles = FileUtil.expandFiles(testDir.getPath(), "blah/blah.txt");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext("blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath(), "blah/blah*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext("blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath(), "dir*/test*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext("dir1/test-file-1.txt");
        asserter.assertNext("dir1/test-file-1a.txt");
        asserter.assertNext("dir2/test-file-2.txt");
        asserter.assertNext("dir3/test-file-3.txt");
    }


    @Test
    public void testFiles(){
        final File testDir =  FileTestUtils.getTestFile(this.getClass(), "testFiles");
        assertTrue(new File(testDir, "blah/blah.txt").exists());
        assertTrue(new File(testDir, "./blah/blah.txt").exists());
    }

    private static class FileAsserter{
        private final Iterator<File> actualFiles;


        private FileAsserter(List<File> actualFiles) {
            this.actualFiles = actualFiles.iterator();
        }

        private void assertNext(final String expectedFileOrDirName){
            assertEquals(expectedFileOrDirName, actualFiles.next().getPath());
        }
    }
}
