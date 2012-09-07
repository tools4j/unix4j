package org.unix4j.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Input device reading from a {@link File}.
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
	
	/**
	 * Creates and returns an input composed from the specified files 
	 * altogether. The resulting input object returns the lines lines of the 
	 * first file first, then the lines of the second file etc.
	 * 
	 * @param files the files to combine into a single input object
	 */
	public static Input composite(File... files) {
		if (files.length == 0) return NullInput.INSTANCE;
		if (files.length == 1) return new FileInput(files[0]);
		final List<FileInput> inputs = new ArrayList<FileInput>(files.length);
		for (int i = 0; i < files.length; i++) {
			inputs.add(new FileInput(files[i]));
		}
		return new CompositeInput(inputs);
	}
	/**
	 * Creates and returns an input composed from the specified files 
	 * altogether. The resulting input object returns the lines lines of the 
	 * first file first, then the lines of the second file etc.
	 * 
	 * @param files the files to combine into a single input object
	 */
	public static Input composite(String... files) {
		if (files.length == 0) return NullInput.INSTANCE;
		if (files.length == 1) return new FileInput(files[0]);
		final List<FileInput> inputs = new ArrayList<FileInput>(files.length);
		for (int i = 0; i < files.length; i++) {
			inputs.add(new FileInput(files[i]));
		}
		return new CompositeInput(inputs);
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
