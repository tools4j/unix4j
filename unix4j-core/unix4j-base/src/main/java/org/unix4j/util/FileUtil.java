package org.unix4j.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class with static methods involving files.
 */
public class FileUtil {

	public static final String ROOT_UNIX = "/";
	public static final String ROOT_WINDOWS = "C:\\";//absolute prefix also: \\\\ i.e. actually \\ for network drive
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
		if (root.equals(file)) {
			return ".";
		}
		if (root.equals(file.getParentFile())) {
			return file.getName();
		}
		final List<String> rootParts = getPathElements(root);
		final List<String> fileParts = getPathElements(file);

		final int len = Math.min(rootParts.size(), fileParts.size());
		int common = 0;
		while (common < len && rootParts.get(common).equals(fileParts.get(common))) {
			common++;
		}
		if (common == 0) {
			return file.getAbsolutePath().replace('\\', '/');
		}
		final StringBuilder sb = new StringBuilder();
		if (common < rootParts.size()) {
			for (int i = common; i < rootParts.size(); i++) {
				sb.append("../");
			}
		} else {
			sb.append("./");
		}
		for (int i = common; i < fileParts.size(); i++) {
			sb.append(fileParts.get(i)).append('/');

		}
		// cut off last '/' and return string
		return sb.deleteCharAt(sb.length() - 1).toString();
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
	 * expanded. If the input {@code files} list contains no wildcard, the files
	 * are simply returned; all wildcard files are expanded.
	 *
	 * @param files
	 *            the files, possibly containing wildcard parts
	 * @return the expanded files resolving wildcards
	 */
	public static List<File> expandFiles(List<File> files) {
		final List<File> expanded = new ArrayList<File>(files.size());
		for (final File file : files) {
			addFileExpanded(file, expanded);
		}
		return expanded;
	}

	private static void addFileExpanded(File file, List<File> expandedFiles) {
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
				parts.remove(0);
			} else {
				p = FileUtil.getUserDir();
			}
			listFiles(p, parts, expandedFiles);
		} else {
			expandedFiles.add(file);
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
	 * characters can be escaped with a preceding
	 * "\" character (also escaped, that is, "\\").
	 *
	 * @param name
	 *            the name or pattern without path
	 * @return a file name filter matching either the name or the pattern
	 */
	public static FilenameFilter getFileNameFilter(String name) {
		if (isWildcardFileName(name)) {
			name = name.replace(".", "/");
			name = name.replace("\\*", "\\/").replace("*", ".*").replace("\\/", "\\*");
			name = name.replace("\\?", "\\/").replace("?", ".").replace("\\/", "\\?");
			name = name.replace("/", "\\.");
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
	 * they are not escaped with a preceding
	 * "\" character (also escaped, that is, "\\").
	 *
	 * @param name
	 *            the name or path
	 * @return true if the name contains unescaped wildcard characters
	 */
	public static boolean isWildcardFileName(String name) {
		if (name.contains("*") || name.contains("?")) {
			// quick match
			if (name.replace("\\*", "").contains("*") || name.replace("\\?", "").contains("?")) {
				// non-escaped match
				return true;
			}
		}
		return false;
	}

	/**
	 * This method returns the output directory of a given class.
	 *
	 * Example:
	 * Given a class:						com.abc.def.MyClass
	 * Which is outputted to a directory:	/home/ben/myproject/target/com/abc/dev/MyClass.class
	 * This method will return:				/home/ben/myproject/target
	 *
	 * @param clazz
	 * @return A file representing the output directory containing the class and parent packages
	 */

	public static File getOutputDirectoryGivenClass(final Class<?> clazz){
		final String resource = "/" + clazz.getName().replace(".", "/") + ".class";
		final URL classFileURL = clazz.getResource(resource);
		final int packageDepth = clazz.getName().split("\\.").length;
		final File classFile = new File(classFileURL.getFile());
		File parentDir = classFile.getParentFile();
		for(int i=0; i<(packageDepth-1); i++){
			parentDir = parentDir.getParentFile();
		}
		return parentDir;
	}

	// no instances
	private FileUtil() {
		super();
	}
}
