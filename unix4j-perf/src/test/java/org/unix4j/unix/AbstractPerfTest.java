package org.unix4j.unix;

import org.junit.Before;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.util.PropertyUtil;
import org.unix4j.util.StackTraceUtil;

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
	private final static String DEFAULT_PROPERTIES_FILE = "/unix4j-perf-expectedResults-default.properties";
	private final static String FILE_1_MEG_PATH = "/1_Meg_test_file.txt";
	private final static String FILE_10_MEG_PATH = "/10_Meg_test_file.txt";
	private final static String FILE_100_MEG_PATH = "/100_Meg_test_file.txt";

	private long timeStarted;

	protected File file1Meg;
	protected File file10Meg;
	protected File file100Meg;

	public void startTimer() {
		System.out.println("Starting timer...");
		timeStarted = new Date().getTime();
	}

	protected void runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4jCommandBuilder command) {
		final StackTraceElement callerStackTraceElement = StackTraceUtil.getCurrentMethodStackTraceElement(1);
		final String callerClass  = callerStackTraceElement.getClassName();
		final String callerMethod = callerStackTraceElement.getMethodName();
		final String propertyName = callerClass + "." + callerMethod + "." + "equivalentUnixTimeMillis";
		final String propertyValue = PropertyUtil.getProperty(DEFAULT_PROPERTIES_FILE, propertyName, null);
		if (propertyValue == null) {
			System.err.println("property '" + propertyName + "' not found in " + DEFAULT_PROPERTIES_FILE);
		}
		final long equivalentUnixTimeMillis = Long.parseLong(propertyValue);
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(command, equivalentUnixTimeMillis);
	}

	private void runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4jCommandBuilder command,
			long equivalentUnixTimeMillis) {

		startTimer();
		command.toDevNull();
		final long executionTime = getRunningTime();

		System.out.println("=========================================");
		System.out.println("Command........................." + command.toString());
		System.out.println("Execution time.................." + executionTime + " millis");
		System.out.println("Equivalent Unix execution time.." + equivalentUnixTimeMillis + " millis");
		System.out.println("Unix4j to Unix Difference......." + (equivalentUnixTimeMillis - executionTime) + "millis");
		System.out.println("Unix4j to Unix %................" + ((1.0f * executionTime / equivalentUnixTimeMillis) * 100) + "%");
		System.out.println();
	}

	protected long getRunningTime() {
		return (new Date()).getTime() - timeStarted;
	}

	@Before
	public void ensurePerfTestFilesExists() throws IOException {
		// Ensure that the small test file exists
		final URL file1MUrl = AbstractPerfTest.class.getResource(FILE_1_MEG_PATH);
		final String SMALL_FILE_MUST_EXIST_MESSAGE = "To run perf tests, " + FILE_1_MEG_PATH + " must exist on the classpath.  Usually this" +
				"should exist in the unix4j-perf/src/test/resources directory.  It should then be copied" +
				"to the target/test-classes directory.  This file will be used during perf testing to" +
				"create the larger test files which are also used for perf testing...";

		assertTrue(SMALL_FILE_MUST_EXIST_MESSAGE, (file1MUrl != null));
		this.file1Meg = new File(file1MUrl.getFile());

		this.file10Meg = new File(file1Meg.getParent(), FILE_10_MEG_PATH);
		createLargeFileIfItDoesntExist(file10Meg, 10);

		this.file100Meg = new File(file1Meg.getParent(), FILE_100_MEG_PATH);
		createLargeFileIfItDoesntExist(file100Meg, 100);
	}

	private void createLargeFileIfItDoesntExist(final File destinationFile, int multiplesOfSmallFile) throws IOException {
		if (!destinationFile.exists()) {
			System.out.println("Large file does not exist, creating....");
			final String file1MegContents = readFile(file1Meg);

			final FileWriter fileWriter = new FileWriter(destinationFile);
			System.out.println("Creating large test file at: " + destinationFile.getAbsolutePath());
			for (int i = 0; i < multiplesOfSmallFile; i++) {
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
		} finally {
			stream.close();
		}
	}
}
