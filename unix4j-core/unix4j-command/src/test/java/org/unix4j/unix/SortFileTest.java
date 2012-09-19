package org.unix4j.unix;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.unix4j.Unix4j;

/**
 * Unit test for {@code Sort} reading the input from test files.
 */
public class SortFileTest {

	@Test
	public void sort() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).sort());
		tester.run(Unix4j.builder().sort(tester.getInputFile()));
	}

	@Test
	public void sortReverse() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).sort(Sort.OPTIONS.reverse));
		tester.run(Unix4j.builder().sort(Sort.OPTIONS.reverse, tester.getInputFile()));
	}

//	@Test
//	public void sortNumeric() {
//		final FileTest tester = new FileTest(this.getClass());
//		tester.run(Unix4j.create().sort(Sort.OPTIONS.numericSort, tester.getInputFiles()));
//	}
	
	@Test
	public void sortMerge() {
		final FileTest tester = new FileTest(this.getClass(), 3);
		tester.run(Unix4j.builder().sort(Sort.OPTIONS.merge, tester.getInputFiles()));
	}
	
	@Test
	public void sortCheck() {
		final FileTest tester = new FileTest(this.getClass(), 2);
		tester.run(Unix4j.builder().sort(Sort.OPTIONS.check, tester.getInputFiles()));
	}

	@Test
	public void sortCheckExitValueOK() {
		final File file = FileTest.getTestFile(getClass(), "sortCheckSucceed.input");
		final int exitVal = Unix4j.builder().sort(Sort.OPTIONS.check, file).toExitValue();
		Assert.assertEquals(0, exitVal);
	}

	@Test
	public void sortCheckExitValueFAILURE() {
		final File file = FileTest.getTestFile(getClass(), "sortCheckFail.input");
		final int exitVal = Unix4j.builder().sort(Sort.OPTIONS.check, file).toExitValue();
		Assert.assertEquals(1, exitVal);
	}
}
