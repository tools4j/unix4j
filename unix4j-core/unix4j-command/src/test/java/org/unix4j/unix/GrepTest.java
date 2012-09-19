package org.unix4j.unix;

import static java.lang.String.format;

import java.io.File;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.FileUtil;
import org.unix4j.util.MultilineString;
import org.unix4j.util.StackTraceUtil;

/**
 * Unit test for simple App.
 */

public class GrepTest {
	@Test
	public void testGrep_simple1() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("This"));
		tester.run(Unix4j.builder().grep("This", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple2() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("a"));
		tester.run(Unix4j.builder().grep("a", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple3() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(".*"));
		tester.run(Unix4j.builder().grep(".*", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple4() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(".+"));
		tester.run(Unix4j.builder().grep(".+", tester.getInputFile()));
	}

	@Test
	public void testGrep_simple5() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("\\s"));
		tester.run(Unix4j.builder().grep("\\s", tester.getInputFile()));
	}

	@Test
	public void testGrep_caseIsIncorrect() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("THIS"));
		tester.run(Unix4j.builder().grep("THIS", tester.getInputFile()));
	}

	@Test
	public void testGrep_ignoringCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.ignoreCase, "THIS"));
		tester.run(Unix4j.builder().grep(Grep.OPTIONS.ignoreCase, "THIS", tester.getInputFile()));
	}

	@Test
	public void testGrep_inverseMatch() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.invertMatch, "This"));
		tester.run(Unix4j.builder().grep(Grep.OPTIONS.invertMatch, "This", tester.getInputFile()));
	}

	@Test
	public void testGrep_inverseMatchIgnoreCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.invertMatch.ignoreCase, "t"));
		tester.run(Unix4j.builder().grep(Grep.OPTIONS.invertMatch.ignoreCase, "t", tester.getInputFile()));
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscaping() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("def\\d123"));
		tester.run(Unix4j.builder().grep("def\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscapingButWithFixedStringOptionSet() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.fixedStrings, "def\\d123"));
		tester.run(Unix4j.builder().grep(Grep.OPTIONS.fixedStrings, "def\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_fixedStringButWithWrongCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.fixedStrings, "DEF\\d123"));
		tester.run(Unix4j.builder().grep(Grep.OPTIONS.fixedStrings, "DEF\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_fixedStringWithWrongCaseAndIngoringCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.OPTIONS.fixedStrings.ignoreCase, "DEF\\d123"));
		tester.run(Unix4j.builder().grep(Grep.OPTIONS.fixedStrings.ignoreCase, "DEF\\d123", tester.getInputFile()));
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
