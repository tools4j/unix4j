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
 * comments of {@link ArgsUtil#parseArgs(String...)}.
 */
public class ArgsUtilTest {

	private Map<String, List<String>> actual;
	private Map<String, List<String>> expected;

	@Before
	public void beforeEach() {
		actual = null;
		expected = new LinkedHashMap<String, List<String>>();
	}

	@Test
	public void testEchoWithMessage() {
		expect("message", "hello", "world");
		actual("--message", "hello", "world");
	}

	@Test
	public void testEchoWithShortOptionAndMessage() {
		expect("message", "hello", "world");
		expect(ArgsUtil.KEY_OPTIONS, "n");
		actual("-n", "--message", "hello", "world");
	}

	@Test
	public void testEchoWithMessageAndShortOption() {
		expect("message", "hello", "world");
		expect(ArgsUtil.KEY_OPTIONS, "n");
		actual("--message", "hello", "world", "-n");
	}

	@Test
	public void testEchoWithLongOptionAndMessage() {
		expect("message", "hello", "world");
		expect(ArgsUtil.KEY_OPTIONS, "noNewline");
		actual("--noNewline", "--message", "hello", "world");
	}

	@Test
	public void testEchoWithMessageAndLongOption() {
		expect("message", "hello", "world");
		expect(ArgsUtil.KEY_OPTIONS, "noNewline");
		actual("--message", "hello", "world", "--noNewline");
	}

	@Test
	public void testEchoWithDefaultOperands() {
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "hello", "world");
		actual("hello", "world");
	}

	@Test
	public void testEchoWithShortOptionNameAndDefaultOperands() {
		expect(ArgsUtil.KEY_OPTIONS, "n");
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "hello", "world");
		actual("-n", "hello", "world");
	}

	@Test
	public void testEchoWithLongOptionNameAndDelimitedDefaultOperands() {
		expect(ArgsUtil.KEY_OPTIONS, "noNewline");
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "hello", "world");
		actual("--noNewline", "--", "hello", "world");
	}

	@Test
	public void testEchoWithDelimitedDefaultOperands1() {
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "8", "-", "7", "=", "1");
		actual("--", "8", "-", "7", "=", "1");
	}

	@Test
	public void testEchoWithDelimitedDefaultOperands2() {
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "8", "--", "7", "=", "15");
		actual("--", "8", "--", "7", "=", "15");
	}

	@Test
	public void testLsWithShortOptions() {
		expect(ArgsUtil.KEY_OPTIONS, "l", "a", "r", "t");
		actual("-lart");
	}

	@Test
	public void testLsWithShortOptionsAndFiles() {
		expect(ArgsUtil.KEY_OPTIONS, "l", "a", "R");
		expect("files", "*.txt", "*.log");
		actual("-laR", "--files", "*.txt", "*.log");
	}

	@Test
	public void testLsWithShortOptionsAndLongAndFiles() {
		expect(ArgsUtil.KEY_OPTIONS, "a", "longFormat");
		expect("files", "*");
		actual("-a", "--longFormat", "--files", "*");
	}

	@Test
	public void testLsWithShortOptionsAndDefaultOperands() {
		expect(ArgsUtil.KEY_OPTIONS, "l", "a", "R");
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "*.txt", "*.log");
		actual("-laR", "*.txt", "*.log");
	}

	@Test
	public void testLsWithLongOptionAndDelimitedDefaultOperands() {
		expect(ArgsUtil.KEY_OPTIONS, "longFormat");
		expect(ArgsUtil.KEY_DEFAULT_OPERAND, "-*", "--*");
		actual("--longFormat", "--", "-*", "--*");
	}

	@Test
	public void testEmpty() {
		expected = Collections.emptyMap();
		actual();
	}

	private void expect(String key, String... values) {
		expected.put(key, Arrays.asList(values));

	}

	private void actual(String... args) {
		actual = ArgsUtil.parseArgs(args);
		Assert.assertEquals(expected, actual);
	}

}
