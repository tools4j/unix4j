package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;

public class SortPerfTest extends AbstractPerfTest {

	@Test(timeout = 5000)
	public void testSort_1Meg_ascending() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_1_MEG.getFile()).sort();
		final String equivalentUnixTest = "cat 1_Meg_test_file.txt | sort";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 5000)
	public void testSort_1Meg_descending() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_1_MEG.getFile()).sort(Sort.Options.reverse);
		final String equivalentUnixTest = "cat 1_Meg_test_file.txt | sort -r";
		run(command, equivalentUnixTest);
	}
}

