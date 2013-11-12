package org.unix4j.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ArgsUtil}. Tests all samples given in the javadoc
 * comments of {@link ArgsUtil#parseArgs(String, List, String...)}.
 */
public class ArgsUtilTest {
	
	private static final String OPTIONS = "options";
	private static final String DEFAULT = "default";
	private static final String PATTERN = "pattern";
	private static final String PATHS	= "paths";
	private static final String SIZE	= "size";

	private Map<String, List<Object>> actual;
	private Map<String, List<Object>> expected;

	@Before
	public void beforeEach() {
		actual = null;
		expected = new LinkedHashMap<String, List<Object>>();
	}

	@Test
	public void testEchoWithMessage() {
		expect("message", "hello", "world");
		actual("--message", "hello", "world");
	}

	@Test
	public void testEchoWithShortOptionAndMessage() {
		expect("message", "hello", "world");
		expect(OPTIONS, "n");
		actual("-n", "--message", "hello", "world");
	}

	@Test
	public void testEchoWithMessageAndShortOption() {
		expect("message", "hello", "world");
		expect(OPTIONS, "n");
		actual("--message", "hello", "world", "-n");
	}

	@Test
	public void testEchoWithLongOptionAndMessage() {
		expect("message", "hello", "world");
		expect(OPTIONS, "noNewline");
		actual("--noNewline", "--message", "hello", "world");
	}

	@Test
	public void testEchoWithMessageAndLongOption() {
		expect("message", "hello", "world");
		expect(OPTIONS, "noNewline");
		actual("--message", "hello", "world", "--noNewline");
	}

	@Test
	public void testEchoWithDefaultOperands() {
		expect(DEFAULT, "hello", "world");
		actual("hello", "world");
	}

	@Test
	public void testEchoWithDefaultAndNonDefaultOperandsMerged() {
		expect(DEFAULT, "hello", "world");
		actual("hello", "--" + DEFAULT, "world");
	}
	@Test
	public void testEchoWithDefaultOperandAfterShortOptionAndNonDefaultOperandMerged() {
		expect(OPTIONS, "n");
		expect(DEFAULT, "hello", "world");
		actual("hello", "-n", "world");
	}
	@Test
	public void testEchoWithNonDefaultAndDefaultOperandsMerged() {
		expect(DEFAULT, "hello", "world");
		actual("--" + DEFAULT, "hello", "--", "world");
	}
	@Test
	public void testEchoWithNonDefaultAndDefaultOperandsAfterShortOptionMerged() {
		expect(OPTIONS, "n");
		expect(DEFAULT, "hello", "world");
		actual("--" + DEFAULT, "hello", "-n", "world");
	}

	@Test
	public void testEchoWithShortOptionNameAndDefaultOperands() {
		expect(OPTIONS, "n");
		expect(DEFAULT, "hello", "world");
		actual("-n", "hello", "world");
	}

	@Test
	public void testEchoWithLongOptionNameAndDelimitedDefaultOperands() {
		expect(OPTIONS, "noNewline");
		expect(DEFAULT, "hello", "world");
		actual("--noNewline", "--", "hello", "world");
	}

	@Test
	public void testEchoWithDelimitedDefaultOperands1() {
		expect(DEFAULT, "8", "-", "7", "=", "1");
		actual("--", "8", "-", "7", "=", "1");
	}

	@Test
	public void testEchoWithDelimitedDefaultOperands2() {
		expect(DEFAULT, "8", "--", "7", "=", "15");
		actual("--", "8", "--", "7", "=", "15");
	}

	@Test
	public void testLsWithShortOptions() {
		expect(OPTIONS, "l", "a", "r", "t");
		actual("-lart");
	}

	@Test
	public void testLsWithShortOptionsAndFiles() {
		expect(OPTIONS, "l", "a", "R");
		expect("files", "*.txt", "*.log");
		actual("-laR", "--files", "*.txt", "*.log");
	}

	@Test
	public void testLsWithShortOptionsAndLongAndFiles() {
		expect(OPTIONS, "a", "longFormat");
		expect("files", "*");
		actual("-a", "--longFormat", "--files", "*");
	}

	@Test
	public void testLsWithShortOptionsAndDefaultOperands() {
		expect(OPTIONS, "l", "a", "R");
		expect(DEFAULT, "*.txt", "*.log");
		actual("-laR", "*.txt", "*.log");
	}

	@Test
	public void testLsWithLongOptionAndDelimitedDefaultOperands() {
		expect(OPTIONS, "longFormat");
		expect(DEFAULT, "-*", "--*");
		actual("--longFormat", "--", "-*", "--*");
	}

	@Test
	public void testGrepWithTwoDefaultOperands() {
		expect(PATTERN, "myword");
		expect(PATHS, "myfile.txt");
		actualGrep("myword", "myfile.txt");
	}

	@Test
	public void testGrepWithShortOptionAndTwoDefaultOperands() {
		expect(OPTIONS, "i");
		expect(PATTERN, "myword");
		expect(PATHS, "myfile.txt");
		actualGrep("-i", "myword", "myfile.txt");
	}

	@Test
	public void testGrepWithShortOptionAndThreeDefaultOperands() {
		expect(OPTIONS, "i");
		expect(PATTERN, "error");
		expect(PATHS, "*.txt", "*.log");
		actualGrep("-i", "error", "*.txt", "*.log");
	}

	@Test
	public void testGrepWithLongOptionAndTwoDelimitedDefaultOperands() {
		expect(OPTIONS, "ignoreCase");
		expect(PATTERN, "error");
		expect(PATHS, "*");
		actualGrep("--ignoreCase", "--", "error", "*");
	}

	@Test
	public void testGrepWithLongOptionOneNamedOperandAndOneDelimitedDefaultOperand() {
		expect(OPTIONS, "ignoreCase");
		expect(PATTERN, "error");
		expect(PATHS, "*");
		actualGrep("--ignoreCase", "--" + PATTERN, "error", "--", "*");
	}

	@Test
	public void testGrepWithShortOptionDefaultOperandAndNamedOperand() {
		expect(OPTIONS, "i");
		expect(PATTERN, "error");
		expect(PATHS, "*");
		actualGrep("-i", "error", "--" + PATHS, "*");
	}

	@Test
	public void testGrepWithLongOptionNamedOperandAndDefaultDelimitedOperand() {
		expect(OPTIONS, "ignoreCase");
		expect(PATTERN, "error");
		expect(PATHS, "*");
		actualGrep("--ignoreCase", "--" + PATHS, "*", "--", "error");
	}

	@Test
	public void testFindWithNegativeSizeOperand() {
		expect(SIZE, "-1000");
		actual("--size", "-1000");
	}

	@Test
	public void testEmpty() {
		expected = Collections.emptyMap();
		actual();
	}

	private void expect(String key, Object... values) {
		expected.put(key, Arrays.asList(values));

	}

	private void actual(Object... args) {
		assertArgs(OPTIONS, Arrays.asList(DEFAULT), args);
	}
	private void actualGrep(Object... args) {
		assertArgs(OPTIONS, Arrays.asList(PATTERN, PATHS), args);
	}
	private void assertArgs(String options, List<String> defaults, Object... args) {
		actual = ArgsUtil.parseArgs(options, defaults, args);
		Assert.assertEquals(expected, actual);
	}

}
