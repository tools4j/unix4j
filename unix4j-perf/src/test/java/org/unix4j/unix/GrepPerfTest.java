package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;

public class GrepPerfTest extends AbstractPerfTest {

	@Test(timeout = 2000)
	public void testGrep_10Meg_fixed() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep(Grep.Options.fixedStrings, "test");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -F 'test'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testGrep_10Meg_fixed_pipedTwice() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep(Grep.Options.fixedStrings, "test").grep(Grep.Options.fixedStrings, "the");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -F 'test' | grep -F 'the'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2500)
	public void testGrep_10Meg_fixed_inverseGrep() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep(Grep.Options.invertMatch.fixedStrings, "test");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -v -F 'test'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testGrep_10Meg() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test.*g");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep 'test.*g'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testGrep_10Meg_pipedTwice() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test").grep("the");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep 'test' | grep 'the'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2500)
	public void testGrep_10Meg_inverseGrep() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep(Grep.Options.invertMatch, "test");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -v 'test'";
		run(command, equivalentUnixTest);
	}
}

