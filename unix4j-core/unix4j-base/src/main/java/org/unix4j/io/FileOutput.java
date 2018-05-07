package org.unix4j.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Output device writing to a {@link File}.
 */
public class FileOutput extends WriterOutput {
	private final String fileInfo;

	public FileOutput(FileWriter fileWriter) {
		super(fileWriter, false);
		this.fileInfo = fileWriter.toString();
	}

	public FileOutput(File file) {
		super(createFileWriter(file), true);
		this.fileInfo = file.toString();
	}

	public FileOutput(FileOutputStream fileStream) {
		super(new OutputStreamWriter(fileStream), false);
		this.fileInfo = fileStream.toString();
	}

	public FileOutput(FileDescriptor fileDesc) {
		super(new FileWriter(fileDesc), true);
		this.fileInfo = fileDesc.toString();
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(fileInfo=" + fileInfo + ")";
	}
}
