package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.AbstractPerfTest;
import org.unix4j.LargeTestFiles;
import org.unix4j.Unix4j;

public class CutPerfTest extends AbstractPerfTest {

	@Test(timeout = 5000)
	public void testCut_10Meg_fields() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).cut(Cut.Options.fields, " ", 1,2,3,4);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | cut -d ' ' -f 1,2,3,4";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 5000)
	public void testCut_10Meg_fieldRange() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).cut(Cut.Options.fields, " ", 1, 10);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | cut -d ' ' -f 1-10";
		run(command, equivalentUnixTest);
	}

	@Test(timeout = 5000)
	public void testCut_10Meg_charRange() {
		final Unix4jCommandBuilder command = Unix4j.fromFile(LargeTestFiles.FILE_10_MEG.getFile()).cut(Cut.Options.chars, 1,5,10,15,20);
		final String equivalentUnixTest = "cat 10_Meg_test_file.txt | cut -c 1,5,10,15,20";
		run(command, equivalentUnixTest);
	}
}

