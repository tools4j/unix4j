package org.unix4j.util;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class to assert actual files one by one, either by their
 * {@link #assertAbsolute(String) absolute} or {@link #assertRelative(File, String)
 * relative} path.
 */
class FileAsserter {
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
	 *            the expected absolute path of the directory or file
	 */
	public void assertAbsolute(final String expectedFileOrDirName) {
		assertEquals(expectedFileOrDirName, actualFiles.next().getPath());
	}

	/**
	 * Assert the absoulte path of the next actual file.
	 * 
	 * @param expectedParentDir
	 *            the parent directory of the expected file or directory
	 * @param expectedChildName
	 *            the child name of the expected file or directory
	 */
	public void assertAbsolute(final File expectedParentDir, final String expectedChildName) {
		assertAbsolute(new File(expectedParentDir, expectedChildName).getPath());
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
	public void assertRelative(final File relativeRoot, String expectedRelativePath) {
		final String actualRelativePath = FileUtil.getRelativePath(relativeRoot, actualFiles.next());
		assertEquals(expectedRelativePath, actualRelativePath);
	}
}