package org.unix4j;

import org.unix4j.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
			final File outputDir = FileUtil.getOutputDirectoryGivenClass(this.getClass());
			filename = format("%s/perf-results-unix4j-%s.properties", outputDir.getPath(), USERNAME);
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
			System.out.println(format("Save and check-in this file to unix4j-perf/src/test/resources%s to create a new baseline file.", TestBaseline.Factory.UNIX4J.getUserFilename()));
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}
}
