package org.unix4j.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultilineString {
	public final static MultilineString EMPTY = new MultilineString("");
	private final static String LINE_ENDING = "\n";
	private String content = "";
	private List<String> lines = new ArrayList<String>();

	public MultilineString(){}

	public MultilineString(final String content){
		fromString(content);
	}

	public MultilineString appendLine(final String line){
		lines.add(line);
		content += line + LINE_ENDING;
		return this;
	}

	public void fromString(final String content){
		String[] lines = content.split(LINE_ENDING);
		this.lines = Arrays.asList(lines);
		this.content = content;
	}

	public void assertMultilineStringEquals(final MultilineString expected){
		if( content.equals(expected.content)){
			assertEquals(expected.content, content);
		} else {
			final String unequalityMessage = "Expected string does not equal actual string.\nExpected: \n" + expected.content + "' \nActual: \n" + content + "\n";
			if(lines.size() != expected.lines.size()){
				String message = "Expected string has a different number of lines to actual string.\n";
				fail(message + unequalityMessage);
			} else {
				for(int i=0; i<expected.lines.size(); i++){
					final String expectedLine = expected.lines.get(i);
					final String actualLine = lines.get(i);
					assertEquals(expectedLine, actualLine);
				}
			}
		}
	}

	@Override
	public String toString(){
		return content;
	}
}
