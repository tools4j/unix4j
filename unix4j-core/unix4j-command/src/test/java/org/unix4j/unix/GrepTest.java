package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.FileUtil;
import org.unix4j.util.MultilineString;
import org.unix4j.util.StackTraceUtil;

import java.io.File;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Unit test for simple App.
 */

public class GrepTest {
	@Test
	public void testGrep_simple1() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep("This"));
//		tester.run(Unix4j.builder().grep("This", tester.getInputFile()));
//		tester.run(Unix4j.builder().grep("--pattern", "This", "--file", tester.getInputFile().toString()));
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
	public void testGrep_withPatternObject() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Pattern.compile("o.e")));
		tester.run(Unix4j.builder().grep(Pattern.compile("o.e"), tester.getInputFile()));
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
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.ignoreCase, "THIS"));
		tester.run(Unix4j.builder().grep(Grep.Options.ignoreCase, "THIS", tester.getInputFile()));
	}

	@Test
	public void testGrep_inverseMatch() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.invertMatch, "This"));
		tester.run(Unix4j.builder().grep(Grep.Options.invertMatch, "This", tester.getInputFile()));
	}

	@Test
	public void testGrep_inverseMatchIgnoreCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.invertMatch.ignoreCase, "t"));
		tester.run(Unix4j.builder().grep(Grep.Options.invertMatch.ignoreCase, "t", tester.getInputFile()));
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
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.fixedStrings, "def\\d123"));
		tester.run(Unix4j.builder().grep(Grep.Options.fixedStrings, "def\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_fixedStringButWithWrongCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.fixedStrings, "DEF\\d123"));
		tester.run(Unix4j.builder().grep(Grep.Options.fixedStrings, "DEF\\d123", tester.getInputFile()));
	}

	@Test
	public void testGrep_fixedStringWithWrongCaseAndIgnoringCase() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.fixedStrings.ignoreCase, "DEF\\d123"));
		tester.run(Unix4j.builder().grep(Grep.Options.fixedStrings.ignoreCase, "DEF\\d123", tester.getInputFile()));
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
