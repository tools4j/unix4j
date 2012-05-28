package org.unix4j;

public interface Input {
	boolean hasMoreLines();
	String readLine();
}
