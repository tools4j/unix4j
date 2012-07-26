package org.unix4j.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

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
				"hello\n\r\r\nworld\n",
			};
		final String[][] expected = {
				{"hello", "world"},
				{"hello", "world"},
				{"hello", "", "world"},
			};
		for (int i = 0; i < input.length; i++) {
			final List<String> actual = StringUtil.splitLines(input[i]);
			Assert.assertEquals(Arrays.asList(expected[i]), actual);
		}
	}
}
