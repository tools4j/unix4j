package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;

public class TailPerfTest extends AbstractPerfTest {

	@Test(timeout = 5000)
	public void testTail_10Meg_smallTail() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).tail(1000);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | tail -1000";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 5000)
	public void testTail_10Meg_bigTail() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).tail(1000000);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | tail -100000";
		run(command, equivalentUnixTest);
	}
}

