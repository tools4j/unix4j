package org.unix4j.compiler;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class JavaCompilerTest {

	@Test
	public void testCompile() throws ClassNotFoundException, IOException {
		final File outputFolder = new File("target/test-" + getClass().getSimpleName());
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		new JavaCompiler().compile(outputFolder, new File("src/test/java/org/unix4j/compiler/testfiles"));
	}
}
