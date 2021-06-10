package org.unix4j.util;

import junit.framework.Assert;
import org.junit.Test;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;

import java.util.Arrays;
import java.util.List;

/**
 * Unit test for {@link StringUtil}.
 */
public class StringUtilTest {

	@Test
	public void testFixedLengthWithIntValue() {
		final boolean[] left = {true, true, false, false, false};
		final char[] filler = {' ', ' ', ' ', '0', ' '};
		final int[] s = {89, 1234, 89, 89, 1234};
		final String[][] expected = {
				{"", "", "", "", ""},
				{"8", "1", "9", "9", "4"},
				{"89", "12", "89", "89", "34"},
				{"89 ", "123", " 89", "089", "234"},
				{"89  ", "1234", "  89", "0089", "1234"}
			};
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected.length; j++) {
				final String actual = StringUtil.fixSizeString(j, left[i], filler[i], s[i]);
				Assert.assertEquals("length=" + j + " of " + s[i], expected[j][i], actual);
			}
		}
	}
	@Test
	public void testFixedLengthWithStringValue() {
		final boolean[] left = {true, true, false, false, false};
		final String[] s = {"XY", "Abcd", "XY", "12", "Abcd"};
		final String[][] expected = {
				{"", "", "", "", ""},
				{"X", "A", "Y", "2", "d"},
				{"XY", "Ab", "XY", "12", "cd"},
				{"XY ", "Abc", " XY", " 12", "bcd"},
				{"XY  ", "Abcd", "  XY", "  12", "Abcd"}
			};
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected.length; j++) {
				final String actual = StringUtil.fixSizeString(j, left[i], s[i]);
				Assert.assertEquals("length=" + j + " of " + s[i], expected[j][i], actual);
			}
		}
	}
	@Test
	public void testFixedLengthWithStringValueAndFiller() {
		final boolean[] left = {true, true, false, false, false};
		final char[] filler = {' ', ' ', ' ', '0', ' '};
		final String[] s = {"XY", "Abcd", "XY", "12", "Abcd"};
		final String[][] expected = {
				{"", "", "", "", ""},
				{"X", "A", "Y", "2", "d"},
				{"XY", "Ab", "XY", "12", "cd"},
				{"XY ", "Abc", " XY", "012", "bcd"},
				{"XY  ", "Abcd", "  XY", "0012", "Abcd"}
			};
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected.length; j++) {
				final String actual = StringUtil.fixSizeString(j, left[i], filler[i], s[i]);
				Assert.assertEquals("length=" + j + " of " + s[i], expected[j][i], actual);
			}
		}
	}
	
	@Test
	public void testSplitLines() {
		final String[] input = {
				"hello\nworld",	
				"hello\r\nworld", 
				"hello\n\r\r\nworld",
				"hello\n\r\r\nworld\n",
				"hello\n\r\r\nworld\n\n",
			};
		final Line[][] expected = {
				{new SimpleLine("hello", "\n"), new SimpleLine("world", "")},
				{new SimpleLine("hello", "\r\n"), new SimpleLine("world", "")},
				{new SimpleLine("hello", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "")},
				{new SimpleLine("hello", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "\n")},
				{new SimpleLine("hello", "\n\r"), new SimpleLine("", "\r\n"), new SimpleLine("world", "\n"), new SimpleLine("", "\n")},
			};
		for (int i = 0; i < input.length; i++) {
			final List<Line> actual = StringUtil.splitLines(input[i]);
			Assert.assertEquals(Arrays.asList(expected[i]), actual);
		}
	}
	
	@Test
	public void findStartAndEndTrimWhitespaceChars() {
		final String s = "\n\r\n\r \thello \r\r\n\n";
		Assert.assertEquals(6, StringUtil.findStartTrimWhitespace(s));
		Assert.assertEquals(s.length() - 5, StringUtil.findEndTrimWhitespace(s));
		for (int i = 0; i < s.length(); i++) {
			final int expected = i <= 6 ? 6 : i < s.length() - 5 ? i : s.length();
			Assert.assertEquals(expected, StringUtil.findStartTrimWhitespace(s, i));
		}
	}
	@Test
	public void findStartAndEndTrimNewlineChars() {
		final String s = "\n\r\n\r  hello \r\r\n\n";
		Assert.assertEquals(3, StringUtil.findStartTrimNewlineChars(s));
		Assert.assertEquals(s.length() - 3, StringUtil.findEndTrimNewlineChars(s));
		for (int i = 0; i < s.length(); i++) {
			final int expected = i <= 3 ? 3 : i < s.length() - 3 ? i : s.length();
			Assert.assertEquals(expected, StringUtil.findStartTrimNewlineChars(s, i));
		}
	}
	@Test
	public void findWhitespace() {
		final String s = "Bla\n\r\n\r \thello \r\r\n\nblabla";
		Assert.assertEquals(3, StringUtil.findWhitespace(s));
		for (int i = 0; i < s.length(); i++) {
			final int expected = i <= 3 ? 3 : i < 9 ? i : i < 14 ? 14 : i < s.length() - 6 ? i : s.length();
			Assert.assertEquals(expected, StringUtil.findWhitespace(s, i));
		}
	}

	@Test
	public void indexOfIgnoreCase() {
		String source = "abc ade";
		String target = "adE";
		Assert.assertEquals(4, StringUtil.indexOfIgnoreCase(source, target));
	}
}
