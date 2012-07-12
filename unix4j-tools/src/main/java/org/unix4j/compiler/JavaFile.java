package org.unix4j.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.SimpleJavaFileObject;

public class JavaFile extends SimpleJavaFileObject {

	private final File folder;
	private final File file;

	public JavaFile(File folder, File file, Kind kind) {
		super(file.toURI(), kind);
		this.folder = folder;
		this.file = file;
	}
	
	public String getClassName() {
		final String folderPath = folder.getAbsolutePath();
		final String filePath = file.getAbsolutePath();
		final String relativePath = filePath.substring(folderPath.length() + 1);
		return relativePath.substring(0, relativePath.length() - kind.extension.length()).replace('/', '.');
	}

	@Override
	public FileInputStream openInputStream() throws IOException {
		return new FileInputStream(file);
	}
	
	@Override
	public FileReader openReader(boolean ignoreEncodingErrors) throws IOException {
		return new FileReader(file);
	}
	
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		final Reader reader = openReader(ignoreEncodingErrors);
		final StringBuilder sb = new StringBuilder();
		final char[] buffer = new char[1024];
		int len;
		while (0 < (len = reader.read(buffer))) {
			sb.append(buffer, 0, len);
		}
		return sb;
	}

	public static List<JavaFile> from(Kind kind, File folder, File... files) {
		final List<JavaFile> list = new ArrayList<JavaFile>(files.length);
		for (final File file : files) {
			list.add(new JavaFile(folder, file, kind));
		}
		return list;
	}

	public static List<JavaFile> findRecursive(Kind kind, File... folders) {
		final List<JavaFile> files = new ArrayList<JavaFile>();
		for (final File folder : folders) {
			addJavaFiles(kind, folder, folder, files);
		}
		return files;
	}

	private static void addJavaFiles(Kind kind, File root, File folder, List<JavaFile> files) {
		final File[] list = folder.listFiles();
		for (final File file : list == null ? new File[0] : list) {
			if (file.isFile() && file.getName().endsWith(kind.extension)) {
				files.add(new JavaFile(root, file, kind));
			} else if (file.isDirectory()) {
				addJavaFiles(kind, root, file, files);
			}
		}
	}

}
