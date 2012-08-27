package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;

public class GrepPerfTest extends AbstractPerfTest {

	@Test(timeout = 2000)
	public void testGrep_10Meg_fixed() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.fixedStrings);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -F 'test'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 5000)
	public void testGrep_100Meg_fixed() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).grep("test", Grep.Option.fixedStrings);
		final String equivalentUnixTest = "cat 100_Meg_test_file.txt | grep -F 'test'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testGrep_10Meg_fixed_pipedTwice() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.fixedStrings).grep("the", Grep.Option.fixedStrings);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -F 'test' | grep -F 'the'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2500)
	public void testGrep_10Meg_fixed_inverseGrep() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.invert, Grep.Option.fixedStrings);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -v -F 'test'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 5000)
	public void testGrep_100Meg_fixed_inverseGrep() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).grep("test", Grep.Option.invert, Grep.Option.fixedStrings);
		final String equivalentUnixTest = "cat 100_Meg_test_file.txt | grep -v -F 'test'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testGrep_10Meg() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test.*g");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep 'test.*g'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 15000)
	public void testGrep_100Meg() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).grep("test.*g");
		final String equivalentUnixTest = "cat 100_Meg_test_file.txt | grep 'test.*g'";
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
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.invert);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | grep -v 'test'";
		run(command, equivalentUnixTest);
	}
}

