package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.unix.sed.SedOption;
import org.unix4j.unix.sed.SedOptions;
import org.unix4j.util.MultilineString;

public class SedTest {
	private final static MultilineString input;
	static {
		input = new MultilineString();
		input
				.appendLine("This is a test blah blah blah")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");
	}

	@Test
	public void testSed_searchAndReplaceSimple() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah blah blah")
				.appendLine("This is a test hasblah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput);
		assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah");
		assertStringArgs(input, expectedOutput, "--string1", "blah", "--string2", "hasblah");

		assertScript( input, "s/blah/hasblah/", expectedOutput );
		assertStringArgs(input, expectedOutput, "s/blah/hasblah/");
		assertStringArgs(input, expectedOutput, "--script", "s/blah/hasblah/");
	}

	@Test
	public void testSed_searchAndReplaceGlobal() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah hasblah hasblah")
				.appendLine("This is a test hasblah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, Sed.Options.g);
		assertStringArgs(input, expectedOutput, "-g", "--regexp", "blah", "--replacement", "hasblah");
		assertStringArgs(input, expectedOutput, "--global", "--string1", "blah", "--string2", "hasblah");

		assertScript(input, "s/blah/hasblah/g", expectedOutput);
		assertStringArgs(input, expectedOutput, "s/blah/hasblah/g");
		assertStringArgs(input, expectedOutput, "--script", "s/blah/hasblah/g");
	}

	@Test
	public void testSed_searchAndReplaceIgnoreCase() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah blah blah")
				.appendLine("This is a test hasblah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "Blah", "hasblah", expectedOutput, Sed.Options.ignoreCase);
		assertStringArgs(input, expectedOutput, "--ignoreCase", "--regexp", "Blah", "--replacement", "hasblah");
		assertStringArgs(input, expectedOutput, "--ignoreCase", "--string1", "Blah", "--string2", "hasblah");

		assertScript( input, "s/Blah/hasblah/I", expectedOutput );
		assertStringArgs(input, expectedOutput, "s/Blah/hasblah/I");
		assertStringArgs(input, expectedOutput, "--script", "s/Blah/hasblah/I");
	}

	@Test
	public void testSed_searchAndReplaceGlobalIgnoreCase() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah hasblah hasblah")
				.appendLine("This is a test hasblah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "bLaH", "hasblah", expectedOutput, Sed.Options.g.I);
		assertStringArgs(input, expectedOutput, "--ignoreCase", "--global", "--regexp", "bLaH", "--replacement", "hasblah");
		assertStringArgs(input, expectedOutput, "-gI", "--string1", "bLaH", "--string2", "hasblah");

		assertScript( input, "s/bLaH/hasblah/gI", expectedOutput );
		assertStringArgs(input, expectedOutput, "s/bLaH/hasblah/Ig");
		assertStringArgs(input, expectedOutput, "--script", "s/bLaH/hasblah/gI");
	}

	@Test
	public void testSed_searchAndReplaceOccurrence1() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah blah blah")
				.appendLine("This is a test hasblah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, 1);
		//TODO add converter for int[]
		//assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah", "--occurrence", "1");

		assertScript(input, "s/blah/hasblah/1", expectedOutput);
		assertStringArgs(input, expectedOutput, "s/blah/hasblah/1");
	}

	@Test
	public void testSed_searchAndReplaceOccurrence2() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah hasblah blah")
				.appendLine("This is a test blah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, 2);
		//TODO add converter for int[]
		//assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah", "--occurrence", "2");

		assertScript(input, "s/blah/hasblah/2", expectedOutput);
		assertStringArgs(input, expectedOutput, "s/blah/hasblah/2");
	}

	@Test
	public void testSed_searchAndReplaceOccurrence3() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah blah hasblah")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, 3);
		//TODO add converter for int[]
		//assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah", "--occurrence", "3");

		assertScript(input, "s/blah/hasblah/3", expectedOutput);
		assertStringArgs(input, expectedOutput, "s/blah/hasblah/3");
	}

	@Test
	public void testSed_searchAndReplaceOccurrence12() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah hasblah blah")
				.appendLine("This is a test hasblah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, 1, 2);
		//TODO add converter for int[]
		//assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah", "--occurrence", "1", "2");
	}


	@Test
	public void testSed_searchAndReplaceOccurrence23() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah hasblah hasblah")
				.appendLine("This is a test blah hasblah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, 2, 3);
		assertSubstitute(input, "blah", "hasblah", expectedOutput, Sed.Options.g, 2);
		//TODO add converter for int[]
		//assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah", "--occurrence", "2", "3");

		assertScript(input, "s/blah/hasblah/2g", expectedOutput);
		assertStringArgs(input, expectedOutput, "s/blah/hasblah/2g");
	}

	@Test
	public void testSed_searchAndReplaceOccurrence13() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test hasblah blah hasblah")
				.appendLine("This is a test hasblah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "blah", "hasblah", expectedOutput, 1, 3);
		//TODO add converter for int[]
		//assertStringArgs(input, expectedOutput, "--regexp", "blah", "--replacement", "hasblah", "--occurrence", "1", "3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSed_searchAndReplaceOccurrence0() {
		final MultilineString expectedOutput = new MultilineString();//not important, exception expected
		assertSubstitute(input, "blah", "hasblah", expectedOutput, 0);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testSed_searchAndReplaceOccurrence0script() {
		final MultilineString expectedOutput = new MultilineString();//not important, exception expected
		assertScript(input, "s/blah/hasblah/0", expectedOutput);
	}

	@Test
	public void testSed_searchAndReplaceUsingEscapedForwardSlashes() {
		final String input = "to be/not to be that is the question";
		final String script = "s/be\\/not/be or not/g";
		final String expectedOutput = "to be or not to be that is the question";

		final String output = Unix4j.fromString(input).sed(script).toStringResult();
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testSed_searchAndReplaceUsingPipeDelimiter() {
		final String input = "to be/not to be that is the question";
		final String script = "s|be/not|be or not|g";
		final String expectedOutput = "to be or not to be that is the question";

		final String output = Unix4j.fromString(input).sed(script).toStringResult();
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testSed_searchAndReplaceUsingPipeDelimiterAndEscapedPipe() {
		final String input = "to be|not to be that is the question";
		final String script = "s|be\\|not|be or not|g";
		final String expectedOutput = "to be or not to be that is the question";

		final String output = Unix4j.fromString(input).sed(script).toStringResult();
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testSed_searchAndReplaceWithWhitespaceChar() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This_is_a_test_blah_blah_blah")
				.appendLine("This_is_a_test_blah_blah")
				.appendLine("This_is_a_test_one_two_three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "\\s", "_", expectedOutput, Sed.Options.global);
		assertStringArgs(input, expectedOutput, "--global", "--regexp", "\\s", "--replacement", "_");

		assertScript(input, "s/\\s/_/g", expectedOutput);
		assertStringArgs(input, expectedOutput, "s/\\s/_/g");
	}

	@Test
	public void testSed_searchAndReplaceWithGroups() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test a is This blah blah blah")
				.appendLine("test a is This blah blah")
				.appendLine("test a is This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "(This) (is) (a) (test)", "$4 $3 $2 $1", expectedOutput);
		//TODO add support for excaped dollars in string args
		//assertStringArgs(input, expectedOutput, "--regexp", "(This) (is) (a) (test)", "--replacement", "$4 $3 $2 $1");
		
		assertScript( input, "s/(This) (is) (a) (test)/$4 $3 $2 $1/", expectedOutput );
		//TODO add support for excaped dollars in string args
		//assertStringArgs(input, expectedOutput, "s/(This) (is) (a) (test)/$4 $3 $2 $1/");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSed_searchAndReplaceWithIllegalGroupReference() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test a is This blah blah blah")
				.appendLine("test a is This blah blah")
				.appendLine("test a is This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "(This) (is) (a) (test)", "$4 $3 $ $2 $1", expectedOutput);
		assertScript( input, "s/(This) (is) (a) (test)/$4 $3 $ $2 $1/", expectedOutput );
	}

	@Test
	public void testSed_searchAndReplaceWithEscapedGroupSymbol() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test a $ is This blah blah blah")
				.appendLine("test a $ is This blah blah")
				.appendLine("test a $ is This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "(This) (is) (a) (test)", "$4 $3 \\$ $2 $1", expectedOutput);
		assertScript( input, "s/(This) (is) (a) (test)/$4 $3 \\$ $2 $1/", expectedOutput );
	}

	@Test
	public void testSed_print() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah blah blah")
				.appendLine("This is a test blah blah blah")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSed(input, "blah", expectedOutput);//should be the default command here
		assertSed(input, "blah", expectedOutput, Sed.Options.p);
		assertSed(input, "blah", expectedOutput, Sed.Options.print);
		assertStringArgs(input, expectedOutput, "--regexp", "blah");
		assertStringArgs(input, expectedOutput, "--print", "--regexp", "blah");
		assertStringArgs(input, expectedOutput, "-p", "--regexp", "blah");
		
		assertScript( input, "/blah/ p", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/ p");

		//print also without space... yea I hate sed!
		assertScript( input, "/blah/p", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/p");
	}

	@Test
	public void testSed_printQuiet() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah blah blah")
				.appendLine("This is a test blah blah");

		assertSed(input, "blah", expectedOutput, Sed.Options.quiet);//should be the default command here
		assertSed(input, "blah", expectedOutput, Sed.Options.n.p);
		assertSed(input, "blah", expectedOutput, Sed.Options.print.quiet);
		assertStringArgs(input, expectedOutput, "--quiet", "--regexp", "blah");
		assertStringArgs(input, expectedOutput, "--quiet", "--print", "--regexp", "blah");
		assertStringArgs(input, expectedOutput, "-n", "-p", "--regexp", "blah");

		assertStringArgs( input, expectedOutput, "-n", "/blah/ p");
		assertStringArgs( input, expectedOutput, "/blah/ p", "--quiet");
	}

	@Test
	public void testSed_printQuietCaseDoesNotMatch() {
		final MultilineString expectedOutput = new MultilineString();

		assertSed(input, "Blah", expectedOutput, Sed.Options.quiet);//should be the default command here
		assertSed(input, "Blah", expectedOutput, Sed.Options.n.p);
		assertSed(input, "Blah", expectedOutput, Sed.Options.print.quiet);
		assertStringArgs(input, expectedOutput, "--quiet", "--regexp", "Blah");
		assertStringArgs(input, expectedOutput, "--quiet", "--print", "--regexp", "Blah");
		assertStringArgs(input, expectedOutput, "-n", "-p", "--regexp", "Blah");

		assertStringArgs( input, expectedOutput, "-n", "/Blah/ p");
		assertStringArgs( input, expectedOutput, "/Blah/ p", "--quiet");

		assertStringArgs( input, expectedOutput, "-n", "/Blah/p");
		assertStringArgs( input, expectedOutput, "/Blah/p", "--quiet");
	}

	@Test
	public void testSed_printQuietCaseInsensitive() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
			.appendLine("This is a test blah blah blah")
			.appendLine("This is a test blah blah");

		assertSed(input, "Blah", expectedOutput, Sed.Options.quiet.ignoreCase);//should be the default command here
		assertSed(input, "Blah", expectedOutput, Sed.Options.n.p.I);
		assertSed(input, "Blah", expectedOutput, Sed.Options.print.quiet.ignoreCase);
		assertStringArgs(input, expectedOutput, "--quiet", "--ignoreCase", "--regexp", "Blah");
		assertStringArgs(input, expectedOutput, "--quiet", "--print", "--ignoreCase", "--regexp", "Blah");
		assertStringArgs(input, expectedOutput, "-n", "-p", "-I", "--regexp", "Blah");
		assertStringArgs(input, expectedOutput, "-npI", "--regexp", "Blah");

		assertStringArgs( input, expectedOutput, "-n", "/Blah/I p");
		assertStringArgs( input, expectedOutput, "/Blah/I p", "--quiet");

		assertStringArgs( input, expectedOutput, "-n", "/Blah/Ip");
		assertStringArgs( input, expectedOutput, "-n", "/Blah/pI");
		assertStringArgs( input, expectedOutput, "/Blah/Ip", "--quiet");
	}
	
	@Test
	public void testSed_translate() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a tEst Blah Blah Blah")
				.appendLine("This is a tEst Blah Blah")
				.appendLine("This is a tEst onE two thrEE")
				.appendLine("onE")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("dEf\\d456");

		assertSed(input, "be123", "BE456", expectedOutput, Sed.Options.translate);
		assertSed(input, "be123", "BE456", expectedOutput, Sed.Options.y);
		assertSed(input, "be123", "BE456", expectedOutput, Sed.Options.translate);
		assertStringArgs(input, expectedOutput, "-y", "--string1", "be123", "--string2", "BE456");
		
		assertScript( input, "y/be123/BE456/", expectedOutput );
		assertStringArgs(input, expectedOutput, "y/be123/BE456/");
	}

	@Test
	public void testSed_translateExotics() {
		final StringBuilder input = new StringBuilder();
		final StringBuilder output = new StringBuilder();
		final StringBuilder src = new StringBuilder();
		final StringBuilder dst = new StringBuilder();
		for (int i = 0; i < 256; i++) {
			input.append((char)i).append((char)(i + 256)).append((char)(i + 512));
			output.append((char)i).append("_").append(":");
			src.append((char)(i + 256)).append((char)(i + 512));
			dst.append("_").append(":");
		}

		final String actual = Unix4j.fromString(input.toString()).sed(Sed.Options.translate, src.toString(), dst.toString()).toStringResult();
		assertEquals(output.toString(), actual);
	}

	private void assertSed(final MultilineString input, final String regexp, final MultilineString expectedOutput){
		assertSed(input, regexp, expectedOutput, SedOption.EMPTY);
	}
	private void assertSed(final MultilineString input, final String regexp, final MultilineString expectedOutput, SedOptions options){
		final StringWriter actualOutputStringWriter = new StringWriter();
		if (options != null) {
			Unix4j.from(input.toInput()).sed(options, regexp).toWriter(actualOutputStringWriter);
		} else {
			Unix4j.from(input.toInput()).sed(regexp).toWriter(actualOutputStringWriter);
		}
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
	private void assertSed(final MultilineString input, final String string1, String string2, final MultilineString expectedOutput, SedOptions options){
		final StringWriter actualOutputStringWriter = new StringWriter();
		if (options != null) {
			Unix4j.from(input.toInput()).sed(options, string1, string2).toWriter(actualOutputStringWriter);
		} else {
			Unix4j.from(input.toInput()).sed(string1, string2).toWriter(actualOutputStringWriter);
		}
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
	private void assertSubstitute(final MultilineString input, final String regexp, String replacement, final MultilineString expectedOutput, int... occurrences){
		assertSubstitute(input, regexp, replacement, expectedOutput, null, occurrences);
	}
	private void assertSubstitute(final MultilineString input, final String regexp, String replacement, final MultilineString expectedOutput, SedOptions options, int... occurrences){
		final StringWriter actualOutputStringWriter = new StringWriter();
		if (options != null) {
			if (occurrences != null && occurrences.length > 0) {
				Unix4j.from(input.toInput()).sed(options, regexp, replacement, occurrences).toWriter(actualOutputStringWriter);
			} else {
				Unix4j.from(input.toInput()).sed(options, regexp, replacement).toWriter(actualOutputStringWriter);
			}
		} else {
			if (occurrences != null && occurrences.length > 0) {
				Unix4j.from(input.toInput()).sed(regexp, replacement, occurrences).toWriter(actualOutputStringWriter);
			} else {
				Unix4j.from(input.toInput()).sed(regexp, replacement).toWriter(actualOutputStringWriter);
			}
		}
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
	private void assertScript(final MultilineString input, final String script, final MultilineString expectedOutput){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).sed(script).toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}
	private void assertStringArgs(final MultilineString input, final MultilineString expectedOutput, String... args){
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).sed(args).toWriter(actualOutputStringWriter);
		final MultilineString actualOutput = new MultilineString(actualOutputStringWriter.toString());
		actualOutput.assertMultilineStringEquals(expectedOutput);
	}

	@Test(expected = NullPointerException.class)
	public void testSed_nullScript() {
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).sed((String)null).toWriter(actualOutputStringWriter);
	}
}
