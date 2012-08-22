package org.unix4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static java.lang.String.format;

/**
 * User: ben
 */
public class ResultsFile {
	private final Properties properties;
	private final String filename;
	private final static String USERNAME = System.getProperty("user.name");

	public ResultsFile()  {
		try {
			final File outputDir = getOutputDirectoryGivenClass(this.getClass());
			filename = format("%s/unix4j-perf-results-%s.properties", outputDir.getPath(), USERNAME);
			properties = new Properties();

			if((new File(filename)).exists()){
				properties.load(new FileInputStream(filename));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(final String key, final String value){
		try {
			properties.put(key, value);
			properties.store(new FileOutputStream(filename), null);
			System.out.println(format("Execution time written to: %s", filename));
			System.out.println(format("Save and check-in this file to unix4j-perf/src/test/resources/unix4j-perf-baseline-%s.properties to create a new baseline file.", USERNAME));
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}

	private File getOutputDirectoryGivenClass(final Class<?> clazz){
		final String resource = "/" + clazz.getName().replace(".", "/") + ".class";
		final URL classFileURL = clazz.getResource(resource);
		final int packageDepth = clazz.getName().split("\\.").length;
		final File classFile = new File(classFileURL.getFile());
		File parentDir = classFile.getParentFile();
		for(int i=0; i<(packageDepth-1); i++){
			parentDir = parentDir.getParentFile();
		}
		return parentDir;
	}
}
