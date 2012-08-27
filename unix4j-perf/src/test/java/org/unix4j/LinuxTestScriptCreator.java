package org.unix4j;

import org.unix4j.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static java.lang.String.format;

/**
 * User: ben
 */
public class LinuxTestScriptCreator {
	private final static String USERNAME = System.getProperty("user.name");
	private final static String OUTPUT_DIR = FileUtil.getOutputDirectoryGivenClass(LinuxTestScriptCreator.class).getPath();
	private final static String TEST_SCRIPT_FILENAME = format("%s/perf-linux-test-script.sh", OUTPUT_DIR);
	private final static String TEST_RESULTS_FILENAME = format("%s/perf-results-linux-%s.properties", OUTPUT_DIR, USERNAME);
	private final File scriptFile;

	public LinuxTestScriptCreator(){
		System.out.println("Creating unix test script:" + TEST_SCRIPT_FILENAME);
		scriptFile = new File(TEST_SCRIPT_FILENAME);

		try{
			final FileWriter fileWriter = new FileWriter(scriptFile ,false);
			fileWriter.write("> " + TEST_RESULTS_FILENAME);
			fileWriter.write('\n');
			fileWriter.write('\n');
			fileWriter.flush();
			fileWriter.close();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public void writeCommandTest(final String testName, final String linuxEquivalentCommand){
		try{
			System.out.println(format("Writing equivalent linux test to: %s", TEST_SCRIPT_FILENAME));
			System.out.println();
			final FileWriter fileWriter = new FileWriter(scriptFile ,true);
			final BufferedWriter scriptFile = new BufferedWriter(fileWriter);
			final String scriptLine = "startTime=\"$(date +%s%N)\"; " + linuxEquivalentCommand + " > /dev/null; duration=\"$(($(date +%s%N)-startTime))\"; durationInMillis=\"$((duration/1000000))\"; echo \"" + testName + ".executionTime=$durationInMillis\" >> " + TEST_RESULTS_FILENAME;
			System.out.println(scriptLine);
			System.out.println();
			scriptFile.write(format("echo 'Testing command: %s'", testName));
			scriptFile.write('\n');
			scriptFile.write(scriptLine);
			scriptFile.write('\n');
			scriptFile.write('\n');
			scriptFile.flush();
			scriptFile.close();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
