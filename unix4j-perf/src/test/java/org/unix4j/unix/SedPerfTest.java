package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;

public class SedPerfTest extends AbstractPerfTest {

	@Test(timeout = 2000)
	public void testSed_substituteFirst_fixedString() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).sed("s/the/blah/");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | sed 's/the/blah/'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testSed_substituteFirst_regex() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).sed("s/to(.*)the(.*)a/abc$1def$2ghi/");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | sed 's/to\\(.*\\)the\\(.*\\)a/abc\\1def\\2ghi/'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testSed_substituteAll_fixedString() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).sed("s/a/b/g");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | sed 's/a/b/g'";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 2000)
	public void testSed_substituteAll_regex() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).sed("s/a(.*)t/t$1a/g");
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | sed 's/a\\(.*\\)t/t$1a/g'";
		run(command, equivalentUnixTest);
	}
}

