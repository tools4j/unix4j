package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;

public class GrepPerfTest extends AbstractPerfTest {
	/**
	 * Equivalent unix test:
	 * time cat 10_Meg_test_file.txt | grep "test" > /dev/null
	 */
	@Test(timeout = 2000)
	public void testGrep_10Meg() {
		run(Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 100_Meg_test_file.txt | grep "test" > /dev/null
	 */
	@Test(timeout = 5000)
	public void testGrep_100Meg() {
		run(Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).grep("test", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 10_Meg_test_file.txt | grep "test" | grep "the" > /dev/null
	 */
	@Test(timeout = 2000)
	public void testGrep_10Meg_pipedTwice() {
		run(Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.fixedStrings).grep("the", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 100_Meg_test_file.txt | grep "test" | grep "the" > /dev/null
	 */
	@Test(timeout = 5000)
	public void testGrep_100Meg_pipedTwice() {
		run(Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).grep("test", Grep.Option.fixedStrings).grep("the", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 10_Meg_test_file.txt | grep -v "test" > /dev/null
	 */
	@Test(timeout = 2500)
	public void testGrep_10Meg_inverseGrep() {
		run(Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).grep("test", Grep.Option.invert, Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 100_Meg_test_file.txt | grep -v "test" > /dev/null
	 */
	@Test(timeout = 5000)
	public void testGrep_100Meg_inverseGrep() {
		run(Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).grep("test", Grep.Option.invert, Grep.Option.fixedStrings));
	}
}

