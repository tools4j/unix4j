package org.unix4j.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Input device based on a {@link File}.
 */
public class FileInput extends ReaderInput {

	private final String fileInfo;

	public FileInput(FileReader fileReader) {
		super(fileReader);
		this.fileInfo = fileReader.toString();
	}

	public FileInput(File file) {
		super(createFileReader(file));
		this.fileInfo = file.toString();
	}

	public FileInput(FileInputStream fileStream) {
		super(new InputStreamReader(fileStream));
		this.fileInfo = fileStream.toString();
	}

	public FileInput(FileDescriptor fileDesc) {
		super(new FileReader(fileDesc));
		this.fileInfo = fileDesc.toString();
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(fileInfo=" + fileInfo + ")";
	}
}
