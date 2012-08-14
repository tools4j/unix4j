package org.unix4j.compiler;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JavaCompilerTest {

	@Test
	public void testCompile() throws ClassNotFoundException, IOException {
		final File testOutputDir = getOutputDir();
		final File testFilesDir = getInputDir();

		System.out.println("Running compiler in directory: " + testFilesDir + " to output directory:" + testOutputDir);
		new JavaCompiler().compile(testOutputDir, testFilesDir);
	}

	/**
	 * @return A directory to write files to. If using maven structure, should exist at:
	 * target/test-classes/org/unix4j/compiler/testfiles
	 */
	private File getInputDir() {
		final String thisClassPackageAndName = "/" + this.getClass().getName().replace(".","/") + ".class";
		final URL thisClassURL = this.getClass().getResource(thisClassPackageAndName);
		final File thisClassFile = new File(thisClassURL.getFile());
		return new File(thisClassFile.getParent() + "/testfiles");
	}

	/**
	 * @return A directory to write files to. If using maven structure, should exist at:
	 * target/test-classes/test-JavaCompilerTest
	 */
	private File getOutputDir() {
		final URL outputDirURL = this.getClass().getResource("/");
		final File outputDirAsFile = new File(outputDirURL.getFile());
		final File testOutputDir = new File(outputDirAsFile.getPath() + "/test-" + this.getClass().getSimpleName());
		if (!testOutputDir.exists()) {
			testOutputDir.mkdirs();
		}
		return testOutputDir;
	}
}
