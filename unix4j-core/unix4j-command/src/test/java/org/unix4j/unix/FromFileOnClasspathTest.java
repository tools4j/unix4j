package org.unix4j.unix;

import org.junit.Ignore;
import org.junit.Test;
import org.unix4j.Unix4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FromFileOnClasspathTest {
	@Test
	public void testFromFileOnClasspath_fileInRootPackage() {
		assertThat(Unix4j.fromResource("/commuting.txt").grep("from").head(4).tail(1).wcCountWords().toStringResult(), is("13"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromFileOnClasspath_fileNotFound() {
		Unix4j.fromResource("/asdfasfdasfd").grep("asfdasfdasfd").toStdOut();
	}

	@Test(expected = NullPointerException.class)
	public void testFromFileOnClasspath_fileParamIsNull() {
		Unix4j.fromResource(null).grep("asfdasfdasfd").toStdOut();
	}

	@Ignore
	@Test
	public void testFromFileOnClasspath_fileInPackage() {
		/*
		Hey marco, try this line.  Should return the number of lines, but executeBatch
		is called for each line for some reason.
		System.out.println(Unix4j.fromFileOnClasspath("/my/package/redneck.txt").grep("for").grep("ask").wcCountLines().toStringResult());
		 */
		assertThat(Unix4j.fromResource("/my/package/redneck.txt").grep("for").grep("ask").wcCountLines().wcCountLines().toStringResult(), is("1"));
	}
}
