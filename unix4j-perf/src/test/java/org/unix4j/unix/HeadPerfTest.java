package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;

public class HeadPerfTest extends AbstractPerfTest {

	@Test(timeout = 4000)
	public void testHead_100Meg_smallHead() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).head(1000);
		final String equivalentUnixTest = "cat 100_Meg_test_file.txt | head -10000";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 4000)
	public void testHead_100Meg_bigHead() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_100_MEG.getFile()).head(1000000);
		final String equivalentUnixTest = "cat 100_Meg_test_file.txt | head -1000000";
		run(command, equivalentUnixTest);
	}
}

