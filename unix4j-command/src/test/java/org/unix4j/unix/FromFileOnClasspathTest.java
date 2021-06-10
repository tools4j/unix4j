package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FromFileOnClasspathTest {
	@Test
	public void testFromFileOnClasspath_fileInRootPackage() {
		assertThat(Unix4j.fromResource("/commuting.txt").grep("from").head(4).tail(1).wc(Wc.Options.words).toStringResult(), is("13"));
	}

	@Test
	public void testFromFileOnClasspath_fileInRootPackage_omitLeadingSlash() {
		assertThat(Unix4j.fromResource("commuting.txt").grep("from").head(4).tail(1).wc(Wc.Options.words).toStringResult(), is("13"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromFileOnClasspath_fileNotFound() {
		Unix4j.fromResource("/asdfasfdasfd").grep("asfdasfdasfd").toStdOut();
	}

	@Test(expected = NullPointerException.class)
	public void testFromFileOnClasspath_fileParamIsNull() {
		Unix4j.fromResource(null).grep("asfdasfdasfd").toStdOut();
	}

	@Test
	public void testFromFileOnClasspath_fileInPackage() {
		assertThat(Unix4j.fromResource("/my/pkg/redneck.txt").grep("for").grep("ask").wc(Wc.Options.lines).toStringResult(), is("15"));
		assertThat(Unix4j.fromResource("/my/pkg/redneck.txt").grep("for").grep("ask").wc(Wc.Options.words).wc(Wc.Options.lines).toStringResult(), is("1"));
	}

	@Test
	public void testRelativePath() {
		assertThat(Unix4j.fromResource(FromFileOnClasspathTest.class, "redneck-unix.txt").grep("for").grep("ask").wc(Wc.Options.lines).toStringResult(), is("15"));
		assertThat(Unix4j.fromResource(FromFileOnClasspathTest.class, "redneck-unix.txt").grep("for").grep("ask").wc(Wc.Options.words).wc(Wc.Options.lines).toStringResult(), is("1"));
	}
}
