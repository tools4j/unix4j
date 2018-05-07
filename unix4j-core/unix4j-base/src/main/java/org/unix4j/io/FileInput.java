package org.unix4j.io;

import org.unix4j.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Input device reading from a {@link File}.
 */
public class FileInput extends ReaderInput {

	private final String fileInfo;

	public FileInput(FileReader fileReader) {
		super(fileReader, false);
		this.fileInfo = fileReader.toString();
	}

	public FileInput(File currentDirectory, File file) {
		super(createFileReader(currentDirectory, file), true);
		this.fileInfo = file.toString();
	}

	public FileInput(File file) {
		super(createFileReader(file), true);
		this.fileInfo = file.toString();
	}

	public FileInput(FileInputStream fileStream) {
		super(new InputStreamReader(fileStream), false);
		this.fileInfo = fileStream.toString();
	}

	public FileInput(FileDescriptor fileDesc) {
		super(new FileReader(fileDesc), true);
		this.fileInfo = fileDesc.toString();
	}

	public FileInput(String file) {
		this(new File(file));
	}
	public FileInput(File currentDirectory, String file) {
		this(currentDirectory, new File(file));
	}

	/**
	 * Creates a new {@link FileInput} object for each of the specified files
	 * and resturns a list with all input objects.
	 * 
	 * @param files
	 *            the files for whose to create a {@code FileInput} object
	 */
	public static List<FileInput> multiple(File... files) {
		final List<FileInput> inputs = new ArrayList<FileInput>(files.length);
		for (int i = 0; i < files.length; i++) {
			inputs.add(new FileInput(files[i]));
		}
		return inputs;
	}

	/**
	 * Creates a new {@link FileInput} object for each of the specified files
	 * and resturns a list with all input objects.
	 * 
	 * @param files
	 *            the files for whose to create a {@code FileInput} object
	 */
	public static List<FileInput> multiple(List<File> files) {
		final List<FileInput> inputs = new ArrayList<FileInput>(files.size());
		for (int i = 0; i < files.size(); i++) {
			inputs.add(new FileInput(files.get(i)));
		}
		return inputs;
	}

	/**
	 * Creates and returns an input composed from the specified files
	 * altogether. The resulting input object returns the lines lines of the
	 * first file first, then the lines of the second file etc.
	 * 
	 * @param files
	 *            the files to combine into a single input object
	 */
	public static Input composite(File... files) {
		if (files.length == 0)
			return NullInput.INSTANCE;
		if (files.length == 1)
			return new FileInput(files[0]);
		return new CompositeInput(multiple(files));
	}

	/**
	 * Creates and returns an input composed from the specified files
	 * altogether. The resulting input object returns the lines lines of the
	 * first file first, then the lines of the second file etc.
	 * 
	 * @param files
	 *            the files to combine into a single input object
	 */
	public static Input composite(List<File> files) {
		if (files.size() == 0)
			return NullInput.INSTANCE;
		if (files.size() == 1)
			return new FileInput(files.get(0));
		return new CompositeInput(multiple(files));
	}

	private static FileReader createFileReader(File file) {
		try {
			return new FileReader(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static FileReader createFileReader(File currentDirectory, File path) {
		try {
			final File file = FileUtil.toAbsoluteFile(currentDirectory, path);
			return new FileReader(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the file info string, for instance a file path or file name.
	 * 
	 * @return the file info string, usually the file name or path.
	 */
	public String getFileInfo() {
		return fileInfo;
	}

	/**
	 * Returns the file info string relative to the given root directory. If a
	 * relative path cannot be evaluated, the method defaults to
	 * {@link #getFileInfo()}.
	 * 
	 * @param relativeRoot
	 *            the relative root for the returned path, may be ignored if a
	 *            relative path cannot be evaluated for the underlying source
	 *            object
	 * @return the file info string, usually the file name or path relative to
	 *         the given root directory
	 */
	public String getFileInfo(File relativeRoot) {
		try {
			return FileUtil.getRelativePath(relativeRoot, new File(fileInfo));
		} catch (Exception e) {
			return getFileInfo();
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(fileInfo=" + getFileInfo() + ")";
	}
}
