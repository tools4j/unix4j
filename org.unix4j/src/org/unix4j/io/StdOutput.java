package org.unix4j.io;

public class StdOutput extends StreamOutput {
	public StdOutput() {
		super(System.out);
	}
}
