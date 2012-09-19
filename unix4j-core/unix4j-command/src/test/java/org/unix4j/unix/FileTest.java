package org.unix4j.unix;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.util.StackTraceUtil;

/**
 * Reads input and expected output for a command unit test from files. For a 
 * test class {@code org.unix4j.unix.MyTest} and a test method "testMe", the 
 * files are (in the test-class path):
 * <pre>
 * org-unix4j-unix/MyTest/default.input
 * org-unix4j-unix/MyTest/testMe.input
 * org-unix4j-unix/MyTest/testMe.output
 * </pre>
 * The file "default.input" is used if the method specific "testMe.input" file
 * does not exist.
 */
class FileTest {
	private final List<File> inputFiles;
	private final String expectedOutput;

	public FileTest(final Class<?> testClass) {
		this(testClass, 1, StackTraceUtil.getCurrentMethodStackTraceElement(1));
	}
	public FileTest(final Class<?> testClass, int inputFileCount) {
		this(testClass, inputFileCount, StackTraceUtil.getCurrentMethodStackTraceElement(1));
	}
	private FileTest(final Class<?> testClass, int inputFileCount, StackTraceElement stackTraceElement) {
		this.inputFiles = new ArrayList<File>(inputFileCount);
		final String testMethodName = stackTraceElement.getMethodName();
		final File outputFile = getTestFile(testClass, testMethodName, testMethodName + ".output");
		if (inputFileCount == 1) {
			final File inputFile = getTestFile(testClass, testMethodName, testMethodName + ".input", "default.input");
			inputFiles.add(inputFile);
		} else {
			for (int i = 0; i < inputFileCount; i++) {
				final File inputFile = getTestFile(testClass, testMethodName, testMethodName + ".input." + (i+1));
				inputFiles.add(inputFile);
			}
		}
		expectedOutput = Unix4j.fromFile(outputFile).cat().toStringResult();
	}
	
	public static final File getTestFile(Class<?> testClass, String fileName) {
		final StackTraceElement stackTraceElement = StackTraceUtil.getCurrentMethodStackTraceElement(1);
		return getTestFile(testClass, stackTraceElement.getMethodName(), fileName);
		
	}
	private static final File getTestFile(Class<?> testClass, String testMethod, String fileName) {
		return getTestFile(testClass, testMethod, fileName, null);
	}
	private static final File getTestFile(Class<?> testClass, String testMethod, String fileName, String defaultFileName) {
		final String packageDir = testClass.getPackage().getName().replace('.', '-');
		final String testDir = packageDir + "/" + testClass.getSimpleName();
		final String filePath= "/" + testDir + "/" + fileName;
		URL fileURL = testClass.getResource(filePath);
		if (fileURL == null) {
			if (defaultFileName == null) {
				throw new IllegalArgumentException("test file for " + testClass.getName() + "." + testMethod + " not found, expected file: " + filePath);
			}
			final String defaultPath= "/" + testDir + "/" + defaultFileName;
			fileURL = testClass.getResource(defaultPath);
			if (fileURL == null) {
				throw new IllegalArgumentException("test file for " + testClass.getName() + "." + testMethod + " not found, expected file: " + filePath + " or default file: " + defaultPath);
			}
		}
		return new File(fileURL.getFile());
	}

	public File getInputFile(){
		if (inputFiles.size() == 1) {
			return inputFiles.get(0);
		}
		throw new IllegalStateException("there are " + inputFiles.size() + " inpput files, use getInputFiles() instead");
	}
	public File[] getInputFiles(){
		return inputFiles.toArray(new File[inputFiles.size()]);
	}

	public void run(final Unix4jCommandBuilder command){
		final String actualOutput = command.toStringResult();

		if(!expectedOutput.equals(actualOutput)){
			printFailureCommandToStandardErr(command, actualOutput);
		}
		assertEquals(expectedOutput, actualOutput);
	}

	private void printFailureCommandToStandardErr(Unix4jCommandBuilder command, String actualOutput) {
		System.err.println("===============================================================");
		System.err.println("FAILED testing command: " + command.toString());
		System.err.println("===============================================================");
		System.err.println("---------------------------------------------------------------");
		for (final File input : inputFiles) {
			System.err.println("INPUT:");
			System.err.println("---------------------------------------------------------------");
			System.err.println(Unix4j.fromFile(input).cat().toStringResult());
			System.err.println("---------------------------------------------------------------");
		}
		System.err.println("EXPECTED OUTPUT:");
		System.err.println("---------------------------------------------------------------");
		System.err.println(expectedOutput);
		System.err.println("---------------------------------------------------------------");
		System.err.println("ACTUAL OUTPUT:");
		System.err.println("---------------------------------------------------------------");
		System.err.println(actualOutput);
		System.err.println("---------------------------------------------------------------");
		System.err.println();
	}
}