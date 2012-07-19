package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.MultilineString;

import static org.junit.Assert.assertEquals;

public class SortTest {
	@Test
	public void testSortSimple() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("To be or not to be")
				.appendLine("That is the question")
				.appendLine("Whether tis nobler in the mind")
				.appendLine("To suffer the")
				.appendLine("10 slings and arrows")
				.appendLine("of outrageous fortune")
				.appendLine("or to bear")
				.appendLine("arms against a sea of troubles.");

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("10 slings and arrows")
				.appendLine("arms against a sea of troubles.")
				.appendLine("of outrageous fortune")
				.appendLine("or to bear")
				.appendLine("That is the question")
				.appendLine("To be or not to be")
				.appendLine("To suffer the")
				.appendLine("Whether tis nobler in the mind");

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().executeToString());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sortAscending().executeToString());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.Option.ascending).executeToString());
	}

	@Test
	public void testSortDescendingSimple() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("To be or not to be")
				.appendLine("That is the question")
				.appendLine("Whether tis nobler in the mind")
				.appendLine("To suffer the")
				.appendLine("10 slings and arrows")
				.appendLine("of outrageous fortune")
				.appendLine("or to bear")
				.appendLine("arms against a sea of troubles.");

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("Whether tis nobler in the mind")
				.appendLine("To suffer the")
				.appendLine("To be or not to be")
				.appendLine("That is the question")
				.appendLine("or to bear")
				.appendLine("of outrageous fortune")
				.appendLine("arms against a sea of troubles.")
				.appendLine("10 slings and arrows");

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sortDescending().executeToString());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.Option.descending).executeToString());
	}

	@Test
	public void testSortAscendingThenDescending() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("To be or not to be")
				.appendLine("That is the question")
				.appendLine("Whether tis nobler in the mind")
				.appendLine("To suffer the")
				.appendLine("10 slings and arrows")
				.appendLine("of outrageous fortune")
				.appendLine("or to bear")
				.appendLine("arms against a sea of troubles.");

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("Whether tis nobler in the mind")
				.appendLine("To suffer the")
				.appendLine("To be or not to be")
				.appendLine("That is the question")
				.appendLine("or to bear")
				.appendLine("of outrageous fortune")
				.appendLine("arms against a sea of troubles.")
				.appendLine("10 slings and arrows");

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().sortDescending().executeToString());
	}

	@Test
	public void testSortOneLine() {
		assertEquals("blah", Unix4j.fromString("blah").sort().sortDescending().executeToString(false));
	}

	@Test
	public void testSortNothing() {
		assertEquals("", Unix4j.fromString("").sort().sortDescending().executeToString(false));
	}

	@Test
	public void testSortNumbers() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("1000 To be or not to be")
				.appendLine("10 That is the question")
				.appendLine("1 Whether tis nobler in the mind")
				.appendLine("999 To suffer the")
				.appendLine("8888 10 slings and arrows")
				.appendLine("7 of outrageous fortune")
				.appendLine("6 or to bear")
				.appendLine("5 arms against a sea of troubles.");

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("1000 To be or not to be")
				.appendLine("10 That is the question")
				.appendLine("1 Whether tis nobler in the mind")
				.appendLine("5 arms against a sea of troubles.")
				.appendLine("6 or to bear")
				.appendLine("7 of outrageous fortune")
				.appendLine("8888 10 slings and arrows")
				.appendLine("999 To suffer the");


		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().executeToString());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.Option.ascending).executeToString());
	}

	@Test
	public void testSortNumbersDescending() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("1000 To be or not to be")
				.appendLine("10 That is the question")
				.appendLine("1 Whether tis nobler in the mind")
				.appendLine("999 To suffer the")
				.appendLine("8888 10 slings and arrows")
				.appendLine("7 of outrageous fortune")
				.appendLine("6 or to bear")
				.appendLine("5 arms against a sea of troubles.");

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("999 To suffer the")
				.appendLine("8888 10 slings and arrows")
				.appendLine("7 of outrageous fortune")
				.appendLine("6 or to bear")
				.appendLine("5 arms against a sea of troubles.")
				.appendLine("1 Whether tis nobler in the mind")
				.appendLine("10 That is the question")
				.appendLine("1000 To be or not to be");

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sortDescending().executeToString());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.Option.descending).executeToString());
	}
}
