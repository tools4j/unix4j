package org.unix4j.command.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.io.WriterOutput;

import java.io.InputStream;
import java.io.StringWriter;

public class GrepPerfTest {
	@Test
	public void testGrep_simple1() {
		final String regex = "This";
		runGrep( regex );
	}

	private void runGrep(final String expression, final Grep.Option... options){
		final InputStream testFileAsInputStream = this.getClass().getClassLoader().getResourceAsStream("1.1M_test_file.txt");
		final StringWriter actualOutputStringWriter = new StringWriter();
		Unix4j.builder(testFileAsInputStream).grep(expression, options).execute(new WriterOutput(actualOutputStringWriter));
	}
}
