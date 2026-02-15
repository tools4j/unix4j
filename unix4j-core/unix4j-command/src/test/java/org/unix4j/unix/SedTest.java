package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.unix.sed.SedOption;
import org.unix4j.unix.sed.SedOptions;
import org.unix4j.util.MultilineString;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
	public void testSed_searchAndReplaceWithZeroGroupAndEscapedGroupSymbol() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("test $3 $2 This is a test This blah blah blah")
				.appendLine("test $3 $2 This is a test This blah blah")
				.appendLine("test $3 $2 This is a test This one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");

		assertSubstitute(input, "(This) (is) (a) (test)", "$4 \\$3 \\$2 $0 $1", expectedOutput);
		assertScript( input, "s/(This) (is) (a) (test)/$4 \\$3 \\$2 $0 $1/", expectedOutput );
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

		//also without space... yea I hate sed!
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
		assertStringArgs(input, expectedOutput, "-np", "--regexp", "blah");

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
	public void testSed_append() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test blah blah blah")
				.appendLine(" This is an appended line\t")
				.appendLine("This is a test blah blah")
				.appendLine(" This is an appended line\t")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");
		
		assertSed(input, "blah", " This is an appended line\t", expectedOutput, Sed.Options.append);
		assertSed(input, "blah", " This is an appended line\t", expectedOutput, Sed.Options.a);
		assertStringArgs(input, expectedOutput, "-a", "--string1", "blah", "--string2", " This is an appended line\t");
		assertStringArgs(input, expectedOutput, "--append", "--string1", "blah", "--string2", " This is an appended line\t");
		
		assertScript(input, "/blah/ a\\ This is an appended line\t", expectedOutput );
		assertScript(input, "/blah/ a \\\n This is an appended line\t\n", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/ a \\ This is an appended line\t");

		//also without space... yea I hate sed!
		assertScript(input, "/blah/a \\ This is an appended line\t", expectedOutput );
		assertScript(input, "/blah/a \\\n This is an appended line\t\n", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/a \\ This is an appended line\t");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSed_appendMissingBackslash1() {
		assertScript(input, "/blah/ a", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_appendMissingBackslash2() {
		assertScript(input, "/blah/a", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_appendMissingBackslash3() {
		assertScript(input, "/blah/a   ", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_appendMissingBackslash4() {
		assertScript(input, "/blah/a This is an appended line", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_appendMissingBackslash5() {
		assertScript(input, "/blah/ a This is an appended line", new MultilineString());
	}

	@Test
	public void testSed_insert() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine(" This is an inserted line\t")
				.appendLine("This is a test blah blah blah")
				.appendLine(" This is an inserted line\t")
				.appendLine("This is a test blah blah")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");
		
		assertSed(input, "blah", " This is an inserted line\t", expectedOutput, Sed.Options.insert);
		assertSed(input, "blah", " This is an inserted line\t", expectedOutput, Sed.Options.i);
		assertStringArgs(input, expectedOutput, "-i", "--string1", "blah", "--string2", " This is an inserted line\t");
		assertStringArgs(input, expectedOutput, "--insert", "--string1", "blah", "--string2", " This is an inserted line\t");
		
		assertScript(input, "/blah/ i \\ This is an inserted line\t", expectedOutput );
		assertScript(input, "/blah/ i\\\n This is an inserted line\t\n", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/ i \\ This is an inserted line\t");

		//also without space... yea I hate sed!
		assertScript(input, "/blah/i \\ This is an inserted line\t", expectedOutput );
		assertScript(input, "/blah/i \\\n This is an inserted line\t\n", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/i \\ This is an inserted line\t");
	}
	
	@Test
	public void testSed_change() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine(" This is an changed line\t")
				.appendLine(" This is an changed line\t")
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");
		
		assertSed(input, "blah", " This is an changed line\t", expectedOutput, Sed.Options.change);
		assertSed(input, "blah", " This is an changed line\t", expectedOutput, Sed.Options.c);
		assertStringArgs(input, expectedOutput, "-c", "--string1", "blah", "--string2", " This is an changed line\t");
		assertStringArgs(input, expectedOutput, "--change", "--string1", "blah", "--string2", " This is an changed line\t");
		
		assertScript(input, "/blah/ c \\ This is an changed line\t", expectedOutput );
		assertScript(input, "/blah/ c\\\n This is an changed line\t\n", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/ c \\ This is an changed line\t");

		//also without space... yea I hate sed!
		assertScript(input, "/blah/c \\ This is an changed line\t", expectedOutput );
		assertScript(input, "/blah/c \\\n This is an changed line\t\n", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/c \\ This is an changed line\t");
	}

	@Test
	public void testSed_delete() {
		final MultilineString expectedOutput = new MultilineString();
		expectedOutput
				.appendLine("This is a test one two three")
				.appendLine("one")
				.appendLine("a")
				.appendLine("")
				.appendLine("two")
				.appendLine("def\\d123");
		
		assertSed(input, "blah", expectedOutput, Sed.Options.delete);
		assertSed(input, "blah", expectedOutput, Sed.Options.d);
		assertSed(input, "BLAH", expectedOutput, Sed.Options.delete.ignoreCase);
		assertStringArgs(input, expectedOutput, "-dI", "--regexp", "bLaH");
		assertStringArgs(input, expectedOutput, "--delete", "--regexp", "blah");
		
		assertScript(input, "/blah/ d", expectedOutput );
		assertScript(input, "/blah/d", expectedOutput );
		assertScript(input, "/BLAH/dI", expectedOutput );
		assertStringArgs(input, expectedOutput, "/blah/ d");
		assertStringArgs(input, expectedOutput, "/bLaH/Id");
		assertStringArgs(input, expectedOutput, "/bLaH/dI");
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

	/**
	 * See issue #71: How to replace with Regex that needs multiple lines
	 */
	@Test
	public void testSed_regexWithMultipleLines() throws IOException {
		//prepare input file
		final String inputFile = System.getProperty("java.io.tmpdir") + "/sed-issue.txt";
		Unix4j.fromStrings(
				"0001 this is a test",
				"0002 another test",
				"with a new line",
				"and maybe another",
				"0003 another test").toFile(inputFile);

		//step 1: replace new line chars
		final String singleLine = Unix4j.fromFile(inputFile).toStringResult().replace("\n", "<NL>").replace("\r", "");

		//step 2: perform actual sed
		final List<String> result = Unix4j.fromString(singleLine)
				.sed("s/<NL>(\\d\\d\\d\\d)/\n$1/g")
				.sed("s/<NL>/ /g")
				.toStringList();

		//assert result
		final String expected = "" +
				"0001 this is a test\n" +
				"0002 another test with a new line and maybe another\n" +
				"0003 another test";

		assertEquals(expected, String.join("\n", result));

		//alternative without unix4j:
		final String outputFile = System.getProperty("java.io.tmpdir") + "/sed-issue-result.txt";
		final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		final PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
		final StringBuilder lineBuffer = new StringBuilder(256);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.matches("^\\d\\d\\d\\d.*")) {
				if (lineBuffer.length() > 0) {
					writer.println(lineBuffer);
					lineBuffer.setLength(0);
				}
			}
			lineBuffer.append(lineBuffer.length() > 0 ? " " : "").append(line);
		}
		if (lineBuffer.length() > 0) {
			writer.println(lineBuffer);
		}
		writer.flush();
		System.out.println("written: " + outputFile);
		assertEquals(expected, String.join("\n", Unix4j.fromFile(outputFile).toStringList()));
	}

    /**
     * Unit test replicating the example given in Issue #82
     * @see <a href="https://github.com/tools4j/unix4j/issues/82">Issue #82</a>
     */
    @Test
    public void testStandAloneWord() {
        final String programName = "cdoc_bound_script";
        final String devProgramName = "cudev_cdoc_bound_script";
        final String result = Unix4j.fromStrings(
                "drop program cdoc_bound_script:dba go",
                "create program cdoc_bound_script:dba",
                "/**",
                " This is a script that is bound to a transaction.",
                " @boundTransaction 12349876",
                " */")
                .sed("s/(\\W)" + programName + "(\\W|$)/$1" + devProgramName + "$2/gI")
                .sed("s/:dba//g")
                .toStringResult();

        //assert result
        final String expected = "" +
                "drop program cudev_cdoc_bound_script go\n" +
                "create program cudev_cdoc_bound_script\n" +
                "/**\n" +
                " This is a script that is bound to a transaction.\n" +
                " @boundTransaction 12349876\n" +
                " */";

        assertEquals(expected, String.join("\n", result));
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

	@Test(expected=IllegalArgumentException.class)
	public void testSed_unterminatedRegexp() {
		assertScript(input, "/blah", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_unterminatedRegexpForSubstitute() {
		assertScript(input, "s/blah", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_unterminatedReplacementForSubstitute() {
		assertScript(input, "s/blah/blo", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_unterminatedSourceForTranslate() {
		assertScript(input, "y/blah", new MultilineString());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSed_unterminatedDestinationForTranslate() {
		assertScript(input, "y/blah/BLAH", new MultilineString());
	}

	@Test(expected = NullPointerException.class)
	public void testSed_nullScript() {
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.from(input.toInput()).sed((String)null).toWriter(actualOutputStringWriter);
	}
}
