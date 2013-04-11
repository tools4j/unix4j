package org.unix4j.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
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

    @Test
    public void testExpandFiles_relativePaths(){
        final File testDir = FileTestUtils.getTestFile(this.getClass(), "testExpandFiles");

        List<File> actualFiles = FileUtil.expandFiles(testDir.getPath() + "/*");
        FileAsserter asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir, "blah");
        asserter.assertNext(testDir, "dir1");
        asserter.assertNext(testDir, "dir2");
        asserter.assertNext(testDir, "dir3");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir, ".");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah.txt");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir, "./blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/blah/blah*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir, "./blah/blah.txt");

        actualFiles = FileUtil.expandFiles(testDir.getPath() + "/dir*/test*");
        asserter = new FileAsserter(actualFiles);
        asserter.assertNext(testDir, "./dir1/test-file-1.txt");
        asserter.assertNext(testDir, "./dir1/test-file-1a.txt");
        asserter.assertNext(testDir, "./dir2/test-file-2.txt");
        asserter.assertNext(testDir, "./dir3/test-file-3.txt");
    }


	@Test
    public void testFiles(){
        final File testDir =  FileTestUtils.getTestFile(this.getClass(), "testFiles");
        assertTrue(new File(testDir, "blah/blah.txt").exists());
        assertTrue(new File(testDir, "./blah/blah.txt").exists());
    }

	/**
	 * Helper class to assert actual files one by one, either by their
	 * {@link #assertNext(String) absolute} or {@link #assertNext(File, String)
	 * relative} path.
	 */
	private static class FileAsserter {
		private final Iterator<File> actualFiles;

		/**
		 * Constructor with actual files to assert.
		 * 
		 * @param actualFiles
		 *            the actual files to assert.
		 */
		public FileAsserter(List<File> actualFiles) {
			this.actualFiles = actualFiles.iterator();
		}

		/**
		 * Assert the absoulte path of the next actual file.
		 * 
		 * @param expectedFileOrDirName
		 *            the expected absolute path of the direcctory or file
		 */
		public void assertNext(final String expectedFileOrDirName) {
			assertEquals(expectedFileOrDirName, actualFiles.next().getPath());
		}

		/**
		 * Assert the relative path of the next actual file.
		 * 
		 * @param relativeRoot
		 *            the root directory for the relative path
		 * @param expectedRelativePath
		 *            the expected path relative to the given
		 *            {@code relativeRoot}
		 * @see FileUtil#getRelativePath(File, File)
		 */
		public void assertNext(final File relativeRoot, String expectedRelativePath) {
			final String actualRelativePath = FileUtil.getRelativePath(relativeRoot, actualFiles.next());
			assertEquals(expectedRelativePath, actualRelativePath);
		}
	}
}
