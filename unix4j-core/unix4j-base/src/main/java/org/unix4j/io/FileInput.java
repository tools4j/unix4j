package org.unix4j.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class FileInput extends ReaderInput {

	public FileInput(FileReader fileReader) {
		super(fileReader);
	}
	public FileInput(File file) {
		super(createFileReader(file));
	}
	public FileInput(FileInputStream fileStream) {
		super(new InputStreamReader(fileStream));
	}
	public FileInput(FileDescriptor fileDesc) {
		super(new FileReader(fileDesc));
	}
	public FileInput(String file) {
		this(new File(file));
	}
	private static FileReader createFileReader(File file) {
		try {
			return new FileReader(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
