package org.unix4j.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class to assert actual files one by one, either by their
 * {@link #assertAbsolute(String) absolute} or {@link #assertRelative(File, String)
 * relative} path.  Assertions do not need to be performed in the order that the
 * files appear in the actualFiles list.
 */
class FileAsserter {
	private final List<File> actualFiles;

	/**
	 * Constructor with actual files to assert.
	 * 
	 * @param actualFiles
	 *            the actual files to assert.
	 */
	public FileAsserter(List<File> actualFiles) {
		this.actualFiles = actualFiles;
	}

	/**
	 * Assert the absolute path against files which have yet to be asserted.
	 * 
	 * @param expectedFileOrDirName
	 *            the expected absolute path of the directory or file
	 */
	public void assertAbsolute(final String expectedFileOrDirName) {
        File fileFound = null;
        for(final File file: actualFiles){
		    if(expectedFileOrDirName.equals(file.getPath())){
                fileFound = file;
            }
        }
        if(fileFound == null){
            String message = "Could not find file matching path: " + expectedFileOrDirName
                           + "Amongst remaining files which have yet to be asserted:\n";
            for(final File file: actualFiles){
                message += "    [" + file.getPath() + "]\n";
            }
            fail(message);
        } else {
            actualFiles.remove(fileFound);
        }
	}

	/**
	 * Assert the absoulte path against files which have yet to be asserted.
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
	 * Assert the relative path against files which have yet to be asserted.
	 * 
	 * @param relativeRoot
	 *            the root directory for the relative path
	 * @param expectedRelativePath
	 *            the expected path relative to the given
	 *            {@code relativeRoot}
	 * @see FileUtil#getRelativePath(File, File)
	 */
    public void assertRelative(final File relativeRoot, String expectedRelativePath) {
        File fileFound = null;
        for(final File file: actualFiles){
            if(expectedRelativePath.equals(FileUtil.getRelativePath(relativeRoot, file))){
                fileFound = file;
            }
        }
        if(fileFound == null){
            String message = "Could not find file matching relative path: " + expectedRelativePath + " with relative root: " + relativeRoot.getPath()
                           + "Amongst remaining files which have yet to be asserted:\n";
            for(final File file: actualFiles){
                message += "    [" + FileUtil.getRelativePath(relativeRoot, file) + "]\n";
            }
            fail(message);
        } else {
            actualFiles.remove(fileFound);
        }
    }
}