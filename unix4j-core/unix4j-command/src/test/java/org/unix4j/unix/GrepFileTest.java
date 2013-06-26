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

public class GrepFileTest {
	@Test
	public void testGrep_simple1() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("This"));
		tester.runAndAssert(Unix4j.builder().grep("This", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("This", tester.getInputFileName()));
		tester.runAndAssert(Unix4j.builder().grep("--regexp", "This", "--paths", tester.getInputFile().toString()));
	}

	@Test
	public void testGrep_simple2() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("a"));
		tester.runAndAssert(Unix4j.builder().grep("a", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("a", tester.getInputFileName()));
	}

	@Test
	public void testGrep_simple3() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(".*"));
		tester.runAndAssert(Unix4j.builder().grep(".*", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep(".*", tester.getInputFileName()));
	}

	@Test
	public void testGrep_simple4() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(".+"));
		tester.runAndAssert(Unix4j.builder().grep(".+", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep(".+", tester.getInputFileName()));
	}

	@Test
	public void testGrep_simple5() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("\\s"));
		tester.runAndAssert(Unix4j.builder().grep("\\s", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("\\s", tester.getInputFileName()));
	}

	@Test
	public void testGrep_withPatternObject() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Pattern.compile("o.e")));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("--pattern", "o.e"));
		tester.runAndAssert(Unix4j.builder().grep(Pattern.compile("o.e"), tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("--pattern", "o.e", "--paths", tester.getInputFileName()));
	}

	@Test
	public void testGrep_caseIsIncorrect() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("THIS"));
		tester.runAndAssert(Unix4j.builder().grep("THIS", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("THIS", tester.getInputFileName()));
	}

	@Test
	public void testGrep_ignoringCase() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.ignoreCase, "THIS"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("-i", "THIS"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("THIS", "--ignoreCase"));
		tester.runAndAssert(Unix4j.builder().grep(Grep.Options.ignoreCase, "THIS", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("-i", "THIS", tester.getInputFileName()));
	}

	@Test
	public void testGrep_inverseMatch() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.invertMatch, "This"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("-v", "This"));
		tester.runAndAssert(Unix4j.builder().grep(Grep.Options.invertMatch, "This", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("-v", "This", tester.getInputFileName()));
	}

	@Test
	public void testGrep_inverseMatchIgnoreCase() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.invertMatch.ignoreCase, "t"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("-vi", "t"));
		tester.runAndAssert(Unix4j.builder().grep(Grep.Options.invertMatch.ignoreCase, "t", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("-vi", "t", tester.getInputFileName()));
	}

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscaping() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("def\\d123"));
		tester.runAndAssert(Unix4j.builder().grep("def\\d123", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("def\\d123", tester.getInputFileName()));
	}

    @Test
    public void testGrep_count() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.count, "test"));
    }

	@Test
	public void testGrep_usingRegexCharactersToFindLiteralTextWithoutEscapingButWithFixedStringOptionSet() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.fixedStrings, "def\\d123"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("-F", "def\\d123"));
		tester.runAndAssert(Unix4j.builder().grep(Grep.Options.fixedStrings, "def\\d123", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("-F", "def\\d123", tester.getInputFileName()));
	}

	@Test
	public void testGrep_fixedStringButWithWrongCase() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.fixedStrings, "DEF\\d123"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("-F", "DEF\\d123"));
		tester.runAndAssert(Unix4j.builder().grep(Grep.Options.fixedStrings, "DEF\\d123", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("-F", "DEF\\d123", tester.getInputFileName()));
	}

	@Test
	public void testGrep_fixedStringWithWrongCaseAndIgnoringCase() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep(Grep.Options.fixedStrings.ignoreCase, "DEF\\d123"));
		tester.runAndAssert(Unix4j.fromFile(tester.getInputFile()).grep("-Fi", "DEF\\d123"));
		tester.runAndAssert(Unix4j.builder().grep(Grep.Options.fixedStrings.ignoreCase, "DEF\\d123", tester.getInputFile()));
		tester.runAndAssert(Unix4j.builder().grep("-Fi", "DEF\\d123", tester.getInputFileName()));
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
