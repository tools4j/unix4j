package org.unix4j.unix;

import junit.framework.ComparisonFailure;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.io.Input;
import org.unix4j.io.URLInput;
import org.unix4j.util.FileTestUtils;
import org.unix4j.util.StackTraceUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

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
class CommandFileTest {
	private static final String TEST_DIR_TOKEN = "\\{\\{TEST_DIR\\}\\}";
	public enum MatchMode {
		Exact {
			@Override
			public boolean matches(String expected, String actual) {
				return expected == actual || (expected != null && expected.equals(actual));
			}
		},
		Regex {
			@Override
			public boolean matches(String expected, String actual) {
				return (expected == null && actual == null) || (expected != null && actual != null && actual.matches(expected));
			}
		};
		abstract public boolean matches(String expected, String actual);
	}

	private final Class<?> testClass;
	private final String testMethodName;
	private final List<File> inputFiles;
	private final List<String> expectedOutputLines;
	private final MatchMode matchMode;
    private final File testDir;


	public CommandFileTest(final Class<?> testClass) {
		this(testClass, 1, MatchMode.Exact, StackTraceUtil.getCurrentMethodStackTraceElement(1));
	}
	public CommandFileTest(final Class<?> testClass, MatchMode matchMode) {
		this(testClass, 1, matchMode, StackTraceUtil.getCurrentMethodStackTraceElement(1));
	}
	public CommandFileTest(final Class<?> testClass, int inputFileCount) {
		this(testClass, inputFileCount, MatchMode.Exact, StackTraceUtil.getCurrentMethodStackTraceElement(1));
	}
    public CommandFileTest(final Class<?> testClass, MatchMode matchMode, int inputFileCount) {
        this(testClass, inputFileCount, matchMode, StackTraceUtil.getCurrentMethodStackTraceElement(1));
    }
	private CommandFileTest(final Class<?> testClass, int inputFileCount, MatchMode matchMode, StackTraceElement stackTraceElement) {

        this.testDir = FileTestUtils.getTestDir(testClass);
        this.inputFiles = new ArrayList<>(inputFileCount);
        this.testClass = requireNonNull(testClass);
		this.testMethodName = stackTraceElement.getMethodName();
		final File outputFile = FileTestUtils.getTestFile(testClass, testMethodName, testMethodName + ".output");
		if (inputFileCount == 1) {
			final File inputFile = FileTestUtils.getTestFile(testClass, testMethodName, testMethodName + ".input", "default.input");
			inputFiles.add(inputFile);
		} else {
			for (int i = 0; i < inputFileCount; i++) {
				final File inputFile = FileTestUtils.getTestFile(testClass, testMethodName, testMethodName + ".input." + (i + 1));
				inputFiles.add(inputFile);
			}
		}
		this.expectedOutputLines = getExpectedOutputLines(outputFile);
		this.matchMode = matchMode;
	}

    public File getInputFile() {
		if (inputFiles.size() == 1) {
			return inputFiles.get(0);
		}
		throw new IllegalStateException("there are " + inputFiles.size() + " input files, use getInputFiles() instead");
	}
	public String getInputFileName() {
		return getInputFile().toString();
	}
	public File[] getInputFiles(){
		return inputFiles.toArray(new File[inputFiles.size()]);
	}
	public String[] getInputFileNames(){
		final String[] names = new String[inputFiles.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = inputFiles.get(i).toString();
		}
		return names;
	}
	public Input[] getInputs() {
		return inputFiles.stream()
				.map(f -> {
					try {
						return new URL("file:" + f);
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
				})
				.map(URLInput::new)
				.toArray(Input[]::new);
	}

    public File getTestDir() {
        return testDir;
    }

    private List<String> getExpectedOutputLines(final File outputFile) {
		return Unix4j.fromFile(outputFile).sed(TEST_DIR_TOKEN, getTestDir().getAbsolutePath()).toStringList();
	}

    public void runAndAssert(final Unix4jCommandBuilder command){
		runAndAssert(command, expectedOutputLines);
	}

	public void runAndAssert(final Unix4jCommandBuilder command, final String outputFilePostfix) {
		final File outputFile = FileTestUtils.getTestFile(testClass, testMethodName, testMethodName + outputFilePostfix);
		final List<String> expectedOutputLines = getExpectedOutputLines(outputFile);
		runAndAssert(command, expectedOutputLines);
	}

    private void runAndAssert(final Unix4jCommandBuilder command, final List<String> expectedOutputLines){
        final List<String> actualOutputLines = command.toStringList();
        boolean ok = expectedOutputLines.size() == actualOutputLines.size();
        for (int i = 0; ok && i < expectedOutputLines.size(); i++) {
            final String exp = expectedOutputLines.get(i);
            final String act = actualOutputLines.get(i);
            ok = matchMode.matches(exp, act);
        }
        if (!ok) {
            System.out.println("Actual:");
            for(final String line: actualOutputLines){
                System.out.println(line);
            }
            throw printFailureCommandToStandardErr(command, expectedOutputLines, actualOutputLines);
        }
    }

    public void runAndAssertIgnoringOrder(final Unix4jCommandBuilder command){
		final List<String> actualOutputLines = command.toStringList();

        List<String> actualOutputLinesSorted = new ArrayList<String>(actualOutputLines);
        Collections.sort(actualOutputLinesSorted);

        List<String> expectedOutputLinesSorted = new ArrayList<String>(expectedOutputLines);
        Collections.sort(expectedOutputLinesSorted);

        boolean ok = expectedOutputLines.size() == actualOutputLines.size();
		for (int i = 0; ok && i < expectedOutputLines.size(); i++) {
			final String exp = expectedOutputLinesSorted.get(i);
			final String act = actualOutputLinesSorted.get(i);
			ok = matchMode.matches(exp, act);
		}
		if (!ok) {
            System.out.println("Actual:");
            for(final String line: actualOutputLinesSorted){
                System.out.println(line);
            }
			throw printFailureCommandToStandardErr(command, expectedOutputLines, actualOutputLinesSorted);
		}
	}

	private AssertionError printFailureCommandToStandardErr(Unix4jCommandBuilder command,
															List<String> expectedOutputLines,
															List<String> actualOutputLines) {
		AssertionError error = null;
		System.err.println("===============================================================");
		System.err.println("FAILED testing command: " + command.toString());
		System.err.println("===============================================================");
		System.err.println("---------------------------------------------------------------");
		for (final File input : inputFiles) {
			System.err.println("INPUT:");
			System.err.println("---------------------------------------------------------------");
			System.err.println(input.isDirectory() ? ("(DIR) " + input.getAbsolutePath()) : Unix4j.fromFile(input).toStringResult());
			System.err.println("---------------------------------------------------------------");
		}
		System.err.println("MODE:" + matchMode);
		System.err.println("OUTPUT:");
		System.err.println("---------------------------------------------------------------");
		for (int i = 0; i < Math.max(expectedOutputLines.size(), actualOutputLines.size()); i++) {
			final String exp = i < expectedOutputLines.size() ? expectedOutputLines.get(i) : null;
			final String act = i < actualOutputLines.size() ? actualOutputLines.get(i) : null;
			final boolean ok = matchMode.matches(exp, act);
			if (ok) {
				System.err.println("...ok.[" + i + "].expected=" + exp);
				System.err.println("...ok.[" + i + "]...actual=" + act);
			} else {
				System.err.println("..ERR.[" + i + "].expected=" + exp);
				System.err.println("..ERR.[" + i + "]...actual=" + act);
				if (error == null) {
					error = new ComparisonFailure(command.toString() + ", line[" + i + "] does not match (mode=" + matchMode + ")", exp, act); 
				}
			}
		}
		System.err.println("---------------------------------------------------------------");
		System.err.println();
		return error == null ? new AssertionError(command.toString()) : error;
	}
}