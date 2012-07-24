package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;

import java.io.File;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FromFileTest {
	@Test
	public void testFromFile() {
		URL url = this.getClass().getResource("/commuting.txt");
		File testFile = new File(url.getFile());
		assertThat(Unix4j.fromFile(testFile).grep("from").head(4).tail(1).wcCountWords().executeToString(false), is("13"));
	}

	@Test(expected = RuntimeException.class)
	public void testFromFile_fileNotFound() {
		File testFile = new File("asfd");
		Unix4j.fromFile(testFile).execute();
	}

	@Test(expected = NullPointerException.class)
	public void testFromFile_fileParamIsNull() {
		Unix4j.fromFile(null).execute();
	}
}
