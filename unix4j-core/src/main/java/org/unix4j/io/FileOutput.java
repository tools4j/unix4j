package org.unix4j.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileOutput extends WriterOutput {
	public FileOutput(FileWriter fileWriter) {
		super(fileWriter);
	}
	public FileOutput(File file) {
		super(createFileWriter(file));
	}
	public FileOutput(FileOutputStream fileStream) {
		super(new OutputStreamWriter(fileStream));
	}
	public FileOutput(FileDescriptor fileDesc) {
		super(new FileWriter(fileDesc));
	}
	public FileOutput(String file) {
		this(new File(file));
	}
	
	private static FileWriter createFileWriter(File file) {
		try {
			return new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
