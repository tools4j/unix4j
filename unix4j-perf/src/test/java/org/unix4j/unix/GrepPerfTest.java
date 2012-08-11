package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;

public class GrepPerfTest extends AbstractPerfTest{
	/**
	 * Equivalent unix test:
	 * time cat 10_Meg_test_file.txt | grep "test" > /dev/null
	 */
	@Test(timeout = 2000)
	public void testGrep_10Meg() {
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4j.fromFile(file10Meg).grep("test", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 100_Meg_test_file.txt | grep "test" > /dev/null
	 */
	@Test(timeout = 5000)
	public void testGrep_100Meg() {
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4j.fromFile(file100Meg).grep("test", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 10_Meg_test_file.txt | grep "test" | grep "the" > /dev/null
	 */
	@Test(timeout = 2000)
	public void testGrep_10Meg_pipedTwice() {
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4j.fromFile(file10Meg).grep("test", Grep.Option.fixedStrings).grep("the", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 100_Meg_test_file.txt | grep "test" | grep "the" > /dev/null
	 */
	@Test(timeout = 5000)
	public void testGrep_100Meg_pipedTwice() {
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4j.fromFile(file100Meg).grep("test", Grep.Option.fixedStrings).grep("the", Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 10_Meg_test_file.txt | grep -v "test" > /dev/null
	 */
	@Test(timeout = 2500)
	public void testGrep_10Meg_inverseGrep() {
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4j.fromFile(file10Meg).grep("test", Grep.Option.invert, Grep.Option.fixedStrings));
	}

	/**
	 * Equivalent unix test:
	 * time cat 100_Meg_test_file.txt | grep -v "test" > /dev/null
	 */
	@Test(timeout = 5000)
	public void testGrep_100Meg_inverseGrep() {
		runCommandAndCompareToEquivalentUnixExecutionTimeInMillis(
			Unix4j.fromFile(file100Meg).grep("test", Grep.Option.invert, Grep.Option.fixedStrings));
	}
}

