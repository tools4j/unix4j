package org.unix4j;

import java.io.File;
import java.io.InputStream;

import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.builder.Unix4jCommandBuilderImpl;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.io.StreamInput;

public class Unix4j {
	
	public static Unix4jCommandBuilder builder(File file) {
		return builder(new FileInput(file));
	}
	public static Unix4jCommandBuilder builder(InputStream in) {
		return builder(new StreamInput(in));
	}
	public static Unix4jCommandBuilder builder(Input in) {
		return new Unix4jCommandBuilderImpl(in);
	}
	public static Unix4jCommandBuilder builder() {
		return new Unix4jCommandBuilderImpl();
	}
	
	//no instances
	private Unix4j() {
		super();
	}
}
