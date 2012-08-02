package org.unix4j.unix;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.junit.Test;
import org.unix4j.Unix4j;

public class FromInputStream {
	@Test
	public void testFromInputStream() {
		final InputStream inputStream = getClass().getResourceAsStream("/commuting.txt");
		assertThat(Unix4j.from(inputStream).grep("from").head(4).tail(1).wcCountWords().toStringResult(), is("13"));
	}

	@Test(expected = NullPointerException.class)
	public void testFromInputStream_fileNotFound() {
		Unix4j.from((InputStream)null).grep("asfd").toStringResult();
	}
}
