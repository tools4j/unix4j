package org.unix4j.unix;

import static java.lang.String.format;
import static junit.framework.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.util.FileUtil;
import org.unix4j.util.MultilineString;
import org.unix4j.util.StackTraceUtil;

/**
 * Unit test for simple App.
 */

public class GrepTest {
	@Test
	public void testGrep_simple1() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("This"));
		tester.run(Unix4j.create().grep("This", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple2() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("a"));
		tester.run(Unix4j.create().grep("a", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple3() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(".*"));
		tester.run(Unix4j.create().grep(".*", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple4() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(".+"));
		tester.run(Unix4j.create().grep(".+", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple5() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("\\s"));
		tester.run(Unix4j.create().grep("\\s", tester.getInputFile()));
	}

	@Test
	public void testGrep_caseIsIncorrect() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("THIS"));
		tester.run(Unix4j.create().grep("THIS", tester.getInputFile()));
	}

	@Test
	public void testGrep_ignoringCase() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.ignoreCase, "THIS"));
		tester.run(Unix4j.create().grep(Grep.OPTIONS.ignoreCase, "THIS", tester.getInputFile()));
	}

	@Test
	public void testGrep_inverseMatch() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.invertMatch, "This"));
		tester.run(Unix4j.create().grep(Grep.OPTIONS.invertMatch, "This", tester.getInputFile()));
	}

	@Test
	public void testGrep_inverseMatchIgnoreCase() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.invertMatch.ignoreCase, "t"));
		tester.run(Unix4j.create().grep(Grep.OPTIONS.invertMatch.ignoreCase, "t", tester.getInputFile()));
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscaping() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("def\\d123"));
		tester.run(Unix4j.create().grep("def\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscapingButWithFixedStringOptionSet() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.fixedStrings, "def\\d123"));
		tester.run(Unix4j.create().grep(Grep.OPTIONS.fixedStrings, "def\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_fixedStringButWithWrongCase() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.fixedStrings, "DEF\\d123"));
		tester.run(Unix4j.create().grep(Grep.OPTIONS.fixedStrings, "DEF\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_fixedStringWithWrongCaseAndIngoringCase() {
		final SimpleCommandTester tester = new SimpleCommandTester(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.fixedStrings.ignoreCase, "DEF\\d123"));
		tester.run(Unix4j.create().grep(Grep.OPTIONS.fixedStrings.ignoreCase, "DEF\\d123", tester.getInputFile()));
	}

	private class SimpleCommandTester {
		private final String expectedOutput;
		private final File testMethodInputFile;

		public SimpleCommandTester(final Class<?> testClass){
			final File testClassParentDir = FileUtil.getDirectoryOfClassFile(testClass);
			final StackTraceElement stackTraceElement = StackTraceUtil.getCurrentMethodStackTraceElement(1);
			final String testClassOutputDirPath = testClassParentDir.getPath() + "/" + testClass.getSimpleName();
			final String testMethodName = stackTraceElement.getMethodName();

			testMethodInputFile = new File(format("%s/%s.input", testClassOutputDirPath, testMethodName));
			final File testMethodExpectedOutputFile = new File(format("%s/%s.output", testClassOutputDirPath, testMethodName));
			expectedOutput = Unix4j.fromFile(testMethodExpectedOutputFile).toStringResult();
		}

		public File getInputFile(){
			return testMethodInputFile;
		}

		public void run(final Unix4jCommandBuilder command){
			final String actualOutput = command.toStringResult();

			if(!expectedOutput.equals(actualOutput)){
				printFailureCommandToStandardErr(command);
			}
			assertEquals(expectedOutput, actualOutput);
		}

		private void printFailureCommandToStandardErr(Unix4jCommandBuilder command) {
			System.err.println("===============================================================");
			System.err.println("FAILED testing command: " + command.toString());
			System.err.println("===============================================================");
			System.err.println("---------------------------------------------------------------");
			System.err.println("INPUT:");
			System.err.println("---------------------------------------------------------------");
			System.err.println(Unix4j.fromFile(testMethodInputFile).toStringResult());
			System.err.println("---------------------------------------------------------------");
			System.err.println("EXPECTED OUTPUT:");
			System.err.println("---------------------------------------------------------------");
			System.err.println(expectedOutput);
			System.err.println("---------------------------------------------------------------");
			System.err.println("ACTUAL OUTPUT:");
			System.err.println("---------------------------------------------------------------");
			System.err.println(expectedOutput);
			System.err.println("---------------------------------------------------------------");
			System.err.println();
		}
	}

	@SuppressWarnings("unused")
	private void testTestToInputAndOutputFiles(MultilineString input, MultilineString expectedOutput) {
		final File testClassParentDir = FileUtil.getDirectoryOfClassFile(this.getClass());
		final String testClassOutputDirPath = testClassParentDir.getPath() + "/" + this.getClass().getSimpleName();

		final StackTraceElement stackTraceElement = StackTraceUtil.getCurrentMethodStackTraceElement(1);
		final String testMethodName = stackTraceElement.getMethodName();

		final File testMethodInputFile = new File(format("%s/%s.input", testClassOutputDirPath, testMethodName));
		final File testMethodExpectedOutputFile = new File(format("%s/%s.output", testClassOutputDirPath, testMethodName));

		Unix4j.fromString(input.toString()).toFile(testMethodInputFile);
		Unix4j.fromString(expectedOutput.toString()).toFile(testMethodExpectedOutputFile);
	}
}
