package org.unix4j.compiler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavaCompiler {
	
	private final List<? extends File> classPath;

	public JavaCompiler() {
		this(emptyFileList());
	}
	public JavaCompiler(String classPath) {
		this(toFileList(classPath));
	}
	private static List<File> toFileList(String classPath) {
		final String[] files = classPath.split(File.pathSeparator);
		final List<File> list = new ArrayList<File>(files.length);
		for (final String file : files) {
			list.add(new File(file));
		}
		return list;
	}
	public JavaCompiler(File... classPath) {
		this(Arrays.asList(classPath));
	}
	private static final List<File> emptyFileList() {
		return Collections.emptyList();
	}
	public JavaCompiler(List<? extends File> classPath) {
		this.classPath = classPath;
	}

	public List<Class<?>> compile(File classOutputFolder, File... sourceFolders) throws IOException, ClassNotFoundException {
		final javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		fileManager.setLocation(StandardLocation.CLASS_PATH, classPath);
		fileManager.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(sourceFolders));
		fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(classOutputFolder));
		final List<JavaFile> sourceFiles = JavaFile.findRecursive(Kind.SOURCE, sourceFolders);
		
		if (sourceFiles.isEmpty()) {
			System.err.println(getClass().getSimpleName() + ": no source files found in " + toAbsoluteString(sourceFolders));
		} else {
			System.out.println(getClass().getSimpleName() + " running with the following options:");
			System.out.println("...classpath     = " + classPath);
			System.out.println("...sourceFolders = " + Arrays.toString(sourceFolders));
			System.out.println("...sourceFiles   = " + sourceFiles);
			System.out.println("...classOutput   = " + classOutputFolder);
		}
		
		final CompilationTask task = compiler.getTask(null, fileManager, null, null, null, sourceFiles);
		if (task.call().booleanValue()) {
			return loadClasses(fileManager, classOutputFolder, JavaFile.findRecursive(Kind.CLASS, classOutputFolder));
		} else {
			final String msg = "COMPILATION FAILED";
			System.err.println(msg);
			throw new IOException(msg);
		}
	}
	private List<Class<?>> loadClasses(StandardJavaFileManager fileManager, File classOutputFolder, List<JavaFile> classFiles) throws ClassNotFoundException, MalformedURLException {
		final ClassLoader loader = new URLClassLoader(new URL[] {classOutputFolder.toURI().toURL()}, fileManager.getClassLoader(StandardLocation.CLASS_PATH));
		final List<Class<?>> classes = new ArrayList<Class<?>>(classFiles.size());
		for (final JavaFile classFile : classFiles) {
			final Class<?> clazz = loader.loadClass(classFile.getClassName());
			classes.add(clazz);
		}
		return classes;
	}
	
	private static String toAbsoluteString(File... files) {
		final StringBuilder sb = new StringBuilder();
		for (final File file : files) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(file.getAbsolutePath());
		}
		return sb.toString();
	}
	
}
