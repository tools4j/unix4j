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
		tester.run(Unix4j.builder().sort("" + tester.getInputFile()));
		//FIXME make this work (String converted into [File]
//		tester.run(Unix4j.builder().sort("--files", tester.getInputFileName()));
	}

	@Test
	public void sortReverse() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.fromFile(tester.getInputFile()).sort(Sort.Options.reverse));
		tester.run(Unix4j.builder().sort(Sort.Options.reverse, tester.getInputFile()));
		tester.run(Unix4j.builder().sort("-r", tester.getInputFileName()));
		tester.run(Unix4j.builder().sort("--reverse", "--", tester.getInputFileName()));
		tester.run(Unix4j.builder().sort(tester.getInputFileName(), "--reverse"));
	}

	@Test
	public void sortNumeric() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.builder().sort(Sort.Options.numericSort, tester.getInputFiles()));
		tester.run(Unix4j.builder().sort(combine(arr("-n"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(arr("--numericSort", "--"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(arr("--numericSort", "--paths"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(tester.getInputFileNames(), "--numericSort")));
	}

	@Test
	public void sortGeneralNumeric() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.builder().sort(Sort.Options.generalNumericSort, tester.getInputFiles()));
		tester.run(Unix4j.builder().sort(combine(arr("-g"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(arr("--generalNumericSort", "--paths"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(arr("--generalNumericSort", "--"), tester.getInputFileNames())));
	}

	@Test
	public void sortNumericIntegers() {
		final FileTest tester = new FileTest(this.getClass());
		tester.run(Unix4j.builder().sort(Sort.Options.numericSort, tester.getInputFiles()));
		tester.run(Unix4j.builder().sort(combine(tester.getInputFileNames(), "-n")));
		tester.run(Unix4j.builder().sort(combine(arr("--numericSort", "--paths"), tester.getInputFileNames())));
	}
	
	@Test
	public void sortMerge() {
		final FileTest tester = new FileTest(this.getClass(), 3);
		tester.run(Unix4j.builder().sort(Sort.Options.merge, tester.getInputFiles()));
		tester.run(Unix4j.builder().sort(combine(arr("--merge", "--paths"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(tester.getInputFileNames(), "-m")));
	}
	
	@Test
	public void sortCheck() {
		final FileTest tester = new FileTest(this.getClass(), 2);
		tester.run(Unix4j.builder().sort(Sort.Options.check, tester.getInputFiles()));
		tester.run(Unix4j.builder().sort(combine(arr("--check", "--paths"), tester.getInputFileNames())));
		tester.run(Unix4j.builder().sort(combine(tester.getInputFileNames(), "-c")));
	}

	@Test
	public void sortCheckExitValueOK() {
		final File file = FileTest.getTestFile(getClass(), "sortCheckSucceed.input");
		int exitVal;
		
		exitVal = Unix4j.builder().sort(Sort.Options.check, file).toExitValue();
		Assert.assertEquals(0, exitVal);
		exitVal = Unix4j.builder().sort("-c", file.toString()).toExitValue();
		Assert.assertEquals(0, exitVal);
		exitVal = Unix4j.builder().sort("--check", "--paths", file.toString()).toExitValue();
		Assert.assertEquals(0, exitVal);
	}

	@Test
	public void sortCheckExitValueFAILURE() {
		final File file = FileTest.getTestFile(getClass(), "sortCheckFail.input");
		int exitVal;
		
		exitVal = Unix4j.builder().sort(Sort.Options.check, file).toExitValue();
		Assert.assertEquals(1, exitVal);
		exitVal = Unix4j.builder().sort("-c", file.toString()).toExitValue();
		Assert.assertEquals(1, exitVal);
		exitVal = Unix4j.builder().sort(file.toString(), "--check").toExitValue();
		Assert.assertEquals(1, exitVal);
	}
	
	private static String[] arr(String... s) {
		return s;
	}
	private static String[] combine(String[] a, String... b) {
		final String[] merged = new String[a.length + b.length];
		System.arraycopy(a, 0, merged, 0, a.length);
		System.arraycopy(b, 0, merged, a.length, b.length);
		return merged;
	}
}
