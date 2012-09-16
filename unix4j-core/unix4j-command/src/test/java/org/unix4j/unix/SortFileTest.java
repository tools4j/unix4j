package org.unix4j.unix;

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
		tester.run(Unix4j.create().sort(tester.getInputFile()));
	}

	@Test
	public void sortReverse() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).sort(Sort.OPTIONS.reverse));
		tester.run(Unix4j.create().sort(Sort.OPTIONS.reverse, tester.getInputFile()));
	}

	@Test
	public void sortMerge() {
		final FileTest tester = new FileTest(this.getClass(), 3);
		tester.run(Unix4j.create().sort(Sort.OPTIONS.merge, tester.getInputFiles()));
	}

}
