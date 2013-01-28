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
            fileWriter.write("cd " + OUTPUT_DIR); fileWriter.write('\n');
            fileWriter.write("command -v bc >/dev/null 2>&1 || { echo >&2 \"I require command bc but it's not installed.  Aborting.\"; exit 1; }"); fileWriter.write('\n');
            fileWriter.write("command -v time >/dev/null 2>&1 || { echo >&2 \"I require command time but it's not installed.  Aborting.\"; exit 1; }"); fileWriter.write('\n');
            fileWriter.write("command -v tail >/dev/null 2>&1 || { echo >&2 \"I require command tail but it's not installed.  Aborting.\"; exit 1; }"); fileWriter.write('\n');
            fileWriter.write("command -v grep >/dev/null 2>&1 || { echo >&2 \"I require command grep but it's not installed.  Aborting.\"; exit 1; }"); fileWriter.write('\n');
            fileWriter.write("command -v sed >/dev/null 2>&1 || { echo >&2 \"I require command sed but it's not installed.  Aborting.\"; exit 1; }"); fileWriter.write('\n');
			fileWriter.write("> " + TEST_RESULTS_FILENAME); fileWriter.write('\n');
            fileWriter.write("echo \"Test results will be written to: " + TEST_RESULTS_FILENAME + "\""); fileWriter.write('\n');
            fileWriter.write('\n');
			fileWriter.flush();
			fileWriter.close();

		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public void writeCommandTest(final String testName, final String linuxEquivalentCommand){
		try{
			System.out.println(format("Writing equivalent linux test to: %s.  Run this script from a shell to produce a native linux/mac/windows-cygwin baseline.", TEST_SCRIPT_FILENAME));
			final FileWriter fileWriter = new FileWriter(scriptFile ,true);
			final BufferedWriter scriptFile = new BufferedWriter(fileWriter);
            final String scriptLine = "durationInMillis=`(time " + linuxEquivalentCommand + ") 2>&1 | tail -3 | grep real | sed 's/real.*\\([0-9]\\{1,\\}\\)m\\([0-9\\.]\\{1,\\}\\)s/((\\1*60)+\\2)*1000/' | bc | sed 's/\\.000//'` && echo \"" + testName + ".executionTime=$durationInMillis\" >> " + TEST_RESULTS_FILENAME;
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
