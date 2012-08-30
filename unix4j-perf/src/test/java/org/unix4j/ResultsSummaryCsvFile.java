package org.unix4j;

import org.unix4j.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static java.lang.String.format;

/**
 * User: ben
 */
public class ResultsSummaryCsvFile {
	private final File scriptFile;
	private final static String USERNAME = System.getProperty("user.name");

	public ResultsSummaryCsvFile(){
		final File outputDir = FileUtil.getOutputDirectoryGivenClass(this.getClass());
		final String filename = format("%s/perf-results-summary-%s.csv", outputDir.getPath(), USERNAME);
		scriptFile = new File(filename);

		try{
			final FileWriter fileWriter = new FileWriter(scriptFile ,false);
			fileWriter.write("TEST_NAME,LINUX_BASELINE_MILLIS,UNIX4J_BASELINE_MILLIS,CURRENT_MILLIS");
			fileWriter.write('\n');
			fileWriter.flush();
			fileWriter.close();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public void write(final String testName,
					  final long linuxBaselineExecutionTimeInMillis,
					  final long unix4jBaselineExecutionTimeInMillis,
					  final long currentExecutionTimeInMillis){
		try{
			final FileWriter fileWriter = new FileWriter(scriptFile ,true);
			final BufferedWriter scriptFile = new BufferedWriter(fileWriter);
			final String line = format("%s,%s,%s,%s", testName, linuxBaselineExecutionTimeInMillis, unix4jBaselineExecutionTimeInMillis, currentExecutionTimeInMillis);
			scriptFile.write(line);
			scriptFile.write('\n');
			scriptFile.flush();
			scriptFile.close();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
