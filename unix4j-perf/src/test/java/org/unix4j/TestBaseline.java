package org.unix4j;

import org.unix4j.util.PropertyUtil;

import static org.junit.Assert.fail;

/**
 * User: ben
 */
public class TestBaseline {
	public enum Factory{
		UNIX4J("/perf-baseline-unix4j-default.properties"),
		LINUX("/perf-baseline-linux-default.properties");

		private String defaultFilename;

		public TestBaseline create(final String testName){
			return new TestBaseline(testName, defaultFilename);
		}

		private Factory(final String defaultFilename) {
			this.defaultFilename = defaultFilename;
		}
	}

	private final long executionTime;

	private TestBaseline(final String testName, final String defaultPropertiesFile) {
		final String propertyName = testName + "." + "executionTime";
		final String value = PropertyUtil.getProperty(defaultPropertiesFile, propertyName, null);
		if (value == null) {
			final String message = "property '" + propertyName + "' not found in " + defaultPropertiesFile;
			System.err.println(message);
			System.err.println("this usually means that a new perf test has been created, but a new baseline");
			System.err.println("has not been recorded.  In order to provide a meaningful test, baseline numbers");
			System.err.println("MUST be provided.  Please add an entry to this file.");
			fail(message);
		}
		executionTime = Long.parseLong(value);
	}

	public long getExecutionTime() {
		return executionTime;
	}
}
