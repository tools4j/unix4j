package org.unix4j.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unix4j.variable.Arg;

/**
 * Utility class with static methods involving files.
 */
public class FileUtil {

	public static final String ROOT_UNIX = "/";

	// absolute prefix also: \\\\ i.e. actually \\ for network drive
	public static final String ROOT_WINDOWS = "C:\\";
	public static final String ROOT_WINDOWS_NETWORK = "\\\\";

	public static final String ROOT = isWindows() ? ROOT_WINDOWS : ROOT_UNIX;

	private static boolean isWindows() {
		return OS.Windows.equals(OS.current());
	}

	/**
	 * Returns the user's current working directory taken from the system
	 * property "user.dir".
	 * 
	 * @return the user's current working directory
	 * @see System#getProperties()
	 */
	public static File getUserDir() {
		return new File(System.getProperty("user.dir"));
	}

	/**
	 * Returns the specified files in a new mutable array list. This is similar
	 * to {@link Arrays#asList(Object...)} but the returned list is expandable.
	 * 
	 * @param files
	 *            the files to add to a new list
	 * @return a new array list containing the specified files
	 */
	public static List<File> toList(File... files) {
		final List<File> list = new ArrayList<File>(files.length + 2);
		for (int i = 0; i < files.length; i++) {
			list.add(files[i]);
		}
		return list;
	}

	/**
	 * Returns the path of the given {@code file} relative to the given
	 * {@code root}.
	 * <p>
	 * The relative path is evaluated as follows:
	 * <ol>
	 * <li>If {@code root} is the same as {@code file}, "." is returned</li>
	 * <li>If {@code root} is the direct parent directory of {@code file}, the
	 * simple file name is returned</li>
	 * <li>If {@code root} is a non-direct parent directory of {@code file}, the
	 * relative path from {@code root} to {@code file} is returned</li>
	 * <li>If {@code root} is not parent directory of {@code file}, the relative
	 * path is the path from {@code root} to the common ancestor and then to
	 * {@code file}</li>
	 * <li>If {@code root} is not parent directory of {@code file} and the only
	 * common ancestor of the two is the root directory, the absolute path of
	 * {@code file} is returned</li>
	 * </ol>
	 * Examples (same order as above):
	 * <ol>
	 * <li>("/home/john", "/home/john") &rarr; "."</li>
	 * <li>("/home/john", "/home/john/notes.txt") &rarr; "notes.txt"</li>
	 * <li>("/home/john", "/home/john/documents/important") &rarr;
	 * "./documents/important"</li>
	 * <li>("/home/john", "/home/smith/public/holidays.pdf") &rarr;
	 * "../smith/public/holidays.pdf"</li>
	 * <li>("/home/john", "/var/tmp/test.out") &rarr; "/var/tmp/test.out"</li>
	 * </ol>
	 * 
	 * @param root
	 *            the root directory for the relative path
	 * @param file
	 *            the file whose path should be returned
	 * @return the path of {@code file} relative to {@code root}
	 */
	public static String getRelativePath(File root, File file) {
		return new RelativePathBase(root).getRelativePathFor(file);
	}

	/**
	 * Returns an absolute file for the given input file, resolving relative
	 * paths on the basis of the given {@code currentDirectory}. If the given
	 * {@code file} represents an absolute path or a variable, it is returned
	 * unchanged. If {@code currentDirectory==null},
	 * {@link File#getAbsoluteFile()} is returned.
	 * 
	 * @param currentDirectory
	 *            the current directory
	 * @param file
	 *            the file to be returned as absolute file
	 * @return the absolute path version of the given file with
	 *         {@code currentDirectory} as basis for relative paths
	 * @see File#isAbsolute()
	 * @see Arg#isVariable(String)
	 * @see File#getAbsoluteFile()
	 */
	public static File toAbsoluteFile(File currentDirectory, File file) {
		if (file.isAbsolute() || Arg.isVariable(file.getPath())) {
			return file;
		}
		if (currentDirectory == null) {
			return file.getAbsoluteFile();
		}
		return new File(currentDirectory, file.getPath()).getAbsoluteFile();
	}

	/**
	 * Returns all path elements of the given {@code file}. The absolute path of
	 * the file is used to evaluate the path elements.
	 * <p>
	 * For instance, a list with the 3 elements "var", "tmp", "out.txt" is
	 * returned for an input file "/var/tmp/out.txt".
	 * 
	 * @param file
	 *            the file whose path elements should be returned
	 * @return the path elements of {@code file}
	 */
	public static List<String> getPathElements(File file) {
		file = file.getAbsoluteFile();
		final List<String> elements = new LinkedList<String>();
		do {
			elements.add(0, file.getName());
			file = file.getParentFile();
		} while (file != null);
		elements.remove(0);
		return elements;
	}

	/**
	 * Expands files if necessary, meaning that input files with wildcards are
	 * expanded. If the specified {@code files} list contains no wildcard, the
	 * files are simply returned; all wildcard files are expanded.
	 * 
	 * @param paths
	 *            the file paths, possibly containing wildcard parts
	 * @return the expanded files resolving wildcards
	 */
	public static List<File> expandFiles(String... paths) {
		return expandFiles(FileUtil.getUserDir(), paths);
	}

	/**
	 * Expands files if necessary, meaning that input files with wildcards are
	 * expanded. If the specified {@code files} list contains no wildcard, the
	 * files are simply returned; all wildcard files are expanded. The given
	 * current directory serves as basis for all relative paths.
	 * 
	 * @param currentDirectory
	 *            the basis for all relative paths
	 * @param paths
	 *            the file paths, possibly containing wildcard parts
	 * @return the expanded files resolving wildcards
	 */
	public static List<File> expandFiles(File currentDirectory, String... paths) {
		final List<File> expanded = new ArrayList<File>(paths.length);
		for (final String path : paths) {
			addFileExpanded(currentDirectory, new File(path), expanded);
		}
		return expanded;
	}

	private static void addFileExpanded(File currentDirectory, File file, List<File> expandedFiles) {
		if (!file.isAbsolute()) {
			file = new File(currentDirectory, file.getPath());
		}
		if (isWildcardFileName(file.getPath())) {
			final List<String> parts = new LinkedList<String>();
			File f = file;
			File p;
			do {
				parts.add(0, f.getName());
				p = f;
				f = f.getParentFile();
			} while (f != null);
			if (p.isDirectory()) {
				//we pass p (the first directory) as starting directory
				parts.remove(0);
			} else if (p.getPath().endsWith("\\")) {
				//must be a drive, such as \\mydrive\bla with parts {"", "mydrive", "bla"}
				parts.remove(0);
			}
			
			//descend again until first wildcard part is found
			while (!parts.isEmpty() && !isWildcardFileName(parts.get(0))) {
				p = new File(p, parts.remove(0));
			}
			if (!p.isDirectory()) {
				// what now? throw exception? trace error?
				throw new IllegalArgumentException("file not found: " + file + " [root=" + p + ", currentDirectory=" + currentDirectory + "]");
			}
			listFiles(p, parts, expandedFiles);
		} else {
			if (file.exists()) {
				expandedFiles.add(file);
			} else {
				// try file as relative path
				final File relFile = new File(currentDirectory, file.getPath());
				if (relFile.exists()) {
					expandedFiles.add(relFile);
				} else {
					// what now? throw exception? trace error?
					throw new IllegalArgumentException("file not found: " + file + " [currentDirectory=" + currentDirectory + "]");
				}
			}
		}
	}

	private static void listFiles(File file, List<String> parts, List<File> dest) {
		final String part = parts.remove(0);
		final FilenameFilter filter = getFileNameFilter(part);
		for (final File f : file.listFiles(filter)) {
			if (parts.isEmpty()) {
				dest.add(f);
			} else {
				if (f.isDirectory()) {
					listFiles(f, parts, dest);
				}
			}
		}
		parts.add(0, part);
	}

	/**
	 * Returns a file name filter for the specified name. The name should be
	 * either a simple file name (not a path) or a wildcard expression such as
	 * "*" or "*.java".
	 * <p>
	 * The wildcards "*" and "?" are supported. "*" stands for any character
	 * repeated 0 to many times, "?" for exactly one arbitrary character. Both
	 * characters can be escaped with a preceding backslash character \ (Unix 
	 * and MAC) or % character (Windows).
	 * 
	 * @param name
	 *            the name or pattern without path
	 * @return a file name filter matching either the name or the pattern
	 */
	public static FilenameFilter getFileNameFilter(String name) {
		if (isWildcardFileName(name)) {
			if (isWindows()) {
				//escape char is %
				name = name.replace("%%", "%_");
				name = name.replace("%.", "%/").replace(".", "%.").replace("%/", "%.");
				name = name.replace("%*", "%/").replace("*", ".*").replace("%/", "%*");
				name = name.replace("%?", "%/").replace("?", ".").replace("%/", "%?");
				name = name.replace("%_", "%%");
			} else {
				//escape char is \
				name = name.replace("\\\\", "\\_");
				name = name.replace("\\.", "\\/").replace(".", "\\.").replace("\\/", "\\.");
				name = name.replace("\\*", "\\/").replace("*", ".*").replace("\\/", "\\*");
				name = name.replace("\\?", "\\/").replace("?", ".").replace("\\/", "\\?");
				name = name.replace("\\_", "\\\\");
			}
			final Pattern pattern = Pattern.compile(name);
			return new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return pattern.matcher(name).matches();
				}
			};
		} else {
			final String fileName = name;
			return new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return fileName.equals(name);
				}
			};
		}
	}

	/**
	 * Returns true if the given name or path contains unescaped wildcard
	 * characters. The characters "*" and "?" are considered wildcard chars if
	 * they are not escaped with a preceding backslash character \ (Unix and 
	 * MAC) or % character (Windows).
	 * 
	 * @param name
	 *            the name or path
	 * @return true if the name contains unescaped wildcard characters
	 */
	public static boolean isWildcardFileName(String name) {
		if (name.contains("*") || name.contains("?")) {
			final String unescaped;
			if (isWindows()) {
				unescaped = name.replace("%%", "%_").replace("%*", "%_").replace("%?", "%_");
			} else {
				unescaped = name.replace("\\\\", "\\_").replace("\\*", "\\_").replace("\\?", "\\_");				
			}
			return unescaped.contains("*") || unescaped.contains("?");
		}
		return false;
	}

	/**
	 * This method returns the output directory of a given class.
	 * 
	 * Example: Given a class: com.abc.def.MyClass Which is outputted to a
	 * directory: /home/ben/myproject/target/com/abc/dev/MyClass.class This
	 * method will return: /home/ben/myproject/target
	 * 
	 * @param clazz
	 * @return A file representing the output directory containing the class and
	 *         parent packages
	 */

	public static File getOutputDirectoryGivenClass(final Class<?> clazz) {
		File parentDir = getDirectoryOfClassFile(clazz);
		final int packageDepth = clazz.getName().split("\\.").length;
		for (int i = 0; i < (packageDepth - 1); i++) {
			parentDir = parentDir.getParentFile();
		}
		return parentDir;
	}

	/**
	 * This method returns the parent directory of a given class.
	 * 
	 * Example: Given a class: com.abc.def.MyClass Which is outputted to a
	 * directory: /home/ben/myproject/target/com/abc/def/MyClass.class This
	 * method will return: /home/ben/myproject/target/com/abc/def
	 * 
	 * @param clazz
	 * @return A file representing the output directory containing the class and
	 *         parent packages
	 */
	public static File getDirectoryOfClassFile(Class<?> clazz) {
		final String resource = "/" + clazz.getName().replace(".", "/") + ".class";
		final URL classFileURL = clazz.getResource(resource);
		final File classFile = new File(classFileURL.getFile());
		return classFile.getParentFile();
	}

	/**
	 * This method returns the parent directory of a given class.
	 * 
	 * Example: Given a class: com.abc.def.MyClass Which is outputted to a
	 * directory: /home/ben/myproject/target/com/abc/def/MyClass.class This
	 * method will return: /home/ben/myproject/target/com/abc/def
	 * 
	 * @param className
	 *            fully qualified class name
	 * @return A file representing the output directory containing the class and
	 *         parent packages
	 */
	public static File getDirectoryOfClassFile(final String className) {
		final String resource = "/" + className.replace(".", "/") + ".class";
		final URL classFileURL = FileUtil.class.getResource(resource);
		final File classFile = new File(classFileURL.getFile());
		return classFile.getParentFile();
	}

	// no instances
	private FileUtil() {
		super();
	}
}
