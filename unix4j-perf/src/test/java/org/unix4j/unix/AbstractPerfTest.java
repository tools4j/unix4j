package org.unix4j.unix;

import org.junit.Before;
import org.unix4j.builder.Unix4jCommandBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * User: ben
 */
public class AbstractPerfTest {
	private final static String FILE_1_MEG_PATH = "/1_Meg_test_file.txt";
	private final static String FILE_10_MEG_PATH = "/10_Meg_test_file.txt";

	private long timeStarted;

	protected File file1Meg;
	protected File file10Meg;

	public void startTimer(){
		System.out.println("Starting timer...");
		timeStarted = new Date().getTime();
	}

	protected void runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4jCommandBuilder command,
			int equivalentUnixTime) {

		startTimer();
		command.toDevNull();
		final long executionTime = getRunningTime();

		System.out.println("=========================================");
		System.out.println("Command........................." + command.toString());
		System.out.println("Execution time.................." + executionTime + " millis");
		System.out.println("Equivalent Linux execution time." + equivalentUnixTime + " millis");
		System.out.println("Unix4j to Linux Difference......" + (equivalentUnixTime - executionTime) + "millis");
		System.out.println("Unix4j to Linux %..............." + ((executionTime/equivalentUnixTime)*100) + "%");
		System.out.println();
	}

	protected long getRunningTime(){
		return (new Date()).getTime() - timeStarted;
	}

	@Before
	public void checkIfLargeFileExists() throws IOException {
		//Ensure that the small test file exists
		final URL file1MUrl = AbstractPerfTest.class.getResource(FILE_1_MEG_PATH);
		final String SMALL_FILE_MUST_EXIST_MESSAGE = "To run perf tests, " + FILE_1_MEG_PATH + " must exist on the classpath.  Usually this" +
				"should exist in the unix4j-perf/src/test/resources directory.  It should then be copied" +
				"to the target/test-classes directory.  This file will be used during perf testing to" +
				"create the 1 Gigabyte test file which is also used for perf testing...";

		assertTrue(SMALL_FILE_MUST_EXIST_MESSAGE, (file1MUrl != null));
		this.file1Meg = new File(file1MUrl.getFile());

		this.file10Meg = new File(file1Meg.getParent(), FILE_10_MEG_PATH);
		createLargeFileIfItDoesntExist(file10Meg, 10);
	}

	private void createLargeFileIfItDoesntExist(final File destinationFile, int multiplesOfSmallFile) throws IOException {
		if(!destinationFile.exists()){
			System.out.println("Large file does not exist, creating....");
			final String file1MegContents = readFile(file1Meg);

			final FileWriter fileWriter = new FileWriter(destinationFile);
			System.out.println("Creating large test file at: " + destinationFile.getAbsolutePath());
			for(int i=0; i<multiplesOfSmallFile; i++){
				fileWriter.write(file1MegContents);
				System.out.print(".");
			}
			fileWriter.flush();
			fileWriter.close();
		} else {
			System.out.println("File at " + destinationFile + " already exists...");
		}
	}

	private static String readFile(final File file) throws IOException {
		final FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
	}
}
