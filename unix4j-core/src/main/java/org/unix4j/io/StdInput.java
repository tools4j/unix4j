package org.unix4j.io;

public class StdInput extends StreamInput {
	public StdInput() {
		super(System.in);
	}
}
