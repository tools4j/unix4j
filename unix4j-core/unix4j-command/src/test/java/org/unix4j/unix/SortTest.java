package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.util.MultilineString;

public class SortTest {

	static {
		Locale.setDefault(Locale.US);//make sure that sort is always the same, uses Collator.getInstance() with default locale
	}

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

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.unique).toStringResult());
	}

	@Test
	public void testSortReverse() {
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

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.reverse).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.r).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.reverse.unique).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.u.r).toStringResult());
	}

	@Test
	public void testSortWithDuplicates() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("To be or not to BE")
				.appendLine("That is the question")
				.appendLine("To be or not to be")
				.appendLine("To suffer the")
				.appendLine("10 slings and arrows")
				.appendLine("10 slings and ARROWS")
				.appendLine("or to bear")
				.appendLine("10 slings and arrows")
		;

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("10 slings and arrows")
				.appendLine("10 slings and arrows")
				.appendLine("10 slings and ARROWS")
				.appendLine("or to bear")
				.appendLine("That is the question")
				.appendLine("To be or not to be")
				.appendLine("To be or not to BE")
				.appendLine("To suffer the")
		;

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().toStringResult());
	}

	@Test
	public void testSortWithDuplicatesIgnoreCase() {
		final MultilineString input = new MultilineString();
		input
			.appendLine("To be or not to BE")
			.appendLine("That is the question")
			.appendLine("To be or not to be")
			.appendLine("To suffer the")
			.appendLine("10 slings and arrows")
			.appendLine("10 slings and ARROWS")
			.appendLine("or to bear")
			.appendLine("10 slings and arrows")
		;

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("10 slings and arrows")
				.appendLine("10 slings and ARROWS")
				.appendLine("10 slings and arrows")
				.appendLine("or to bear")
				.appendLine("That is the question")
				.appendLine("To be or not to BE")
				.appendLine("To be or not to be")
				.appendLine("To suffer the")
		;

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.ignoreCase).toStringResult());
	}

	@Test
	public void testSortWithDuplicateUniques() {
		final MultilineString input = new MultilineString();
		input
				.appendLine("To be or not to BE")
				.appendLine("That is the question")
				.appendLine("To be or not to be")
				.appendLine("To suffer the")
				.appendLine("10 slings and arrows")
				.appendLine("10 slings and ARROWS")
				.appendLine("or to bear")
				.appendLine("10 slings and arrows")
		;

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("10 slings and arrows")
				.appendLine("10 slings and ARROWS")
				.appendLine("or to bear")
				.appendLine("That is the question")
				.appendLine("To be or not to be")
				.appendLine("To be or not to BE")
				.appendLine("To suffer the")
		;

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.unique).toStringResult());
	}

	@Test
	public void testSortWithDuplicatesIgnoreCaseUnique() {
		final MultilineString input = new MultilineString();
		input
			.appendLine("To be or not to BE")
			.appendLine("That is the question")
			.appendLine("To be or not to be")
			.appendLine("To suffer the")
			.appendLine("10 slings and arrows")
			.appendLine("10 slings and ARROWS")
			.appendLine("or to bear")
			.appendLine("10 slings and arrows")
		;

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("10 slings and arrows")
				.appendLine("or to bear")
				.appendLine("That is the question")
				.appendLine("To be or not to BE")
				.appendLine("To suffer the")
		;

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.ignoreCase.unique).toStringResult());
	}

	@Test
	public void testSortIgnoreBlanks() {
		final MultilineString input = new MultilineString();
		input
			.appendLine("   Abc")
			.appendLine("  Abc")
			.appendLine(" Abc")
			.appendLine("Abc")
			.appendLine("AAA")
			.appendLine(" AAb")
			.appendLine("  Abz")
			.appendLine("   ZZZ")
			.appendLine("\t\tZZZ")
		;

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
			.appendLine("AAA")
			.appendLine(" AAb")
			.appendLine("   Abc")
			.appendLine("  Abc")
			.appendLine(" Abc")
			.appendLine("Abc")
			.appendLine("  Abz")
			.appendLine("   ZZZ")
			.appendLine("\t\tZZZ")
		;
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.ignoreLeadingBlanks).toStringResult());
	}

	@Test
	public void testSortUniqueIgnoreBlanks() {
		final MultilineString input = new MultilineString();
		input
			.appendLine("   Abc")
			.appendLine("  Abc")
			.appendLine(" Abc")
			.appendLine("Abc")
			.appendLine("AAA")
			.appendLine(" AAb")
			.appendLine("  Abz")
			.appendLine("   ZZZ")
			.appendLine("\t\tZZZ")
		;

		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
			.appendLine("AAA")
			.appendLine(" AAb")
			.appendLine("   Abc")
			.appendLine("  Abz")
			.appendLine("   ZZZ")
		;
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.unique.ignoreLeadingBlanks).toStringResult());
	}

	@Test
	public void testSortThenReverse() {
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

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().sort(Sort.OPTIONS.reverse).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.unique).sort(Sort.OPTIONS.reverse.unique).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().sort(Sort.OPTIONS.reverse.unique).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.unique).sort(Sort.OPTIONS.reverse).toStringResult());
	}

	@Test
	public void testSortOneLine() {
		assertEquals("blah", Unix4j.fromString("blah").sort().toStringResult());
		assertEquals("blah", Unix4j.fromString("blah").sort(Sort.OPTIONS.reverse).toStringResult());
		assertEquals("blah", Unix4j.fromString("blah").sort(Sort.OPTIONS.unique).toStringResult());
		assertEquals("blah", Unix4j.fromString("blah").sort(Sort.OPTIONS.unique.reverse).toStringResult());
	}

	@Test
	public void testSortNothing() {
		assertEquals("", Unix4j.fromString("").sort().toStringResult());
		assertEquals("", Unix4j.fromString("").sort(Sort.OPTIONS.reverse).toStringResult());
		assertEquals("", Unix4j.fromString("").sort(Sort.OPTIONS.unique).toStringResult());
		assertEquals("", Unix4j.fromString("").sort(Sort.OPTIONS.unique.reverse).toStringResult());
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


		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort().toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.unique).toStringResult());
	}

	@Test
	public void testSortNumbersReverse() {
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

		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.reverse).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.r).toStringResult());
		assertEquals(expectedOutput.toString(), Unix4j.fromString(input.toString()).sort(Sort.OPTIONS.r.u).toStringResult());
	}
}
