package org.unix4j.util;

import java.io.File;
import java.util.List;

/**
 * This class represents a base directory for relative paths. The
 * {@link #getRelativePathFor(File)} method returns a path string relative to
 * the base directory passed to the constructor of this class.
 */
public class RelativePathBase {

	/**
	 * Settings used to construct relative paths.
	 */
	public static interface Settings {
		/**
		 * Appends the path for a file that equals the base directory. Some
		 * settings may append "." or "./" and others the base directory name.
		 * 
		 * @param result
		 *            the string builder with the result path
		 * @param baseDir
		 *            the base directory, here also the relative path target
		 * @return the {@code result} instance for chained invocations
		 */
		StringBuilder appendPathForBaseDir(StringBuilder result, File baseDir);

		/**
		 * Appends the path prefix for children of the base directory. Most
		 * settings add "./" as prefix, others maybe nothing and some add
		 * nothing for direct children but "./" for all other child paths.
		 * 
		 * @param result
		 *            the string builder with the result path
		 * @param baseDir
		 *            the base directory, here a parent directory of the
		 *            relative path target
		 * @return the {@code result} instance for chained invocations
		 */
		StringBuilder appendPrefixForChildren(StringBuilder result, File baseDir, boolean directChildOfBase);

		/**
		 * Appends the path prefix from the base directory to the common
		 * ancestor of base directory and target file. Most settings add one
		 * "../" element for every directory up to the common ancestor.
		 * 
		 * @param result
		 *            the string builder with the result path
		 * @param baseDir
		 *            the base directory, here a directory which has a common
		 *            ancestor with the relative path target
		 * @return the {@code result} instance for chained invocations
		 */
		StringBuilder appendPrefixToCommonAncestor(StringBuilder result, File baseDir, int levelsToCommonAncestor);
	}

	/**
	 * Default settings to construct relative paths: "." represents the base
	 * directory, "./" is used as prefix of child paths (but "" for direct
	 * children) and "../" for elements up to the common ancestor.
	 */
	public static final Settings DEFAULT = new Settings() {
		/**
		 * Appends "." to {@code result} representing the base directory.
		 */
		@Override
		public StringBuilder appendPathForBaseDir(StringBuilder result, File baseDir) {
			return result.append('.');
		}

		/**
		 * Appends "./" to {@code result} as prefix for child directories, but
		 * only if {@code directChildOfBase==false} (no prefix for direct
		 * children of the base directory).
		 */
		@Override
		public StringBuilder appendPrefixForChildren(StringBuilder result, File baseDir, boolean directChildOfBase) {
			if (directChildOfBase) {
				return result;
			}
			return result.append("./");
		}

		/**
		 * Appends one "../" element for every directory up to the common
		 * ancestor ({@code n} elements where {@code n=levelsToCommonAncestor}).
		 */
		@Override
		public StringBuilder appendPrefixToCommonAncestor(StringBuilder result, File baseDir, int levelsToCommonAncestor) {
			for (int i = 0; i < levelsToCommonAncestor; i++) {
				result.append("../");
			}
			return result;
		}
	};
	/**
	 * Alternative settings to construct relative paths similar to
	 * {@link #DEFAULT}: "." represents the base directory, "./" is used as
	 * prefix of child paths and "../" for elements up to the common ancestor.
	 * As opposed to the DEFAULT settings, the "./" prefix is used for all child
	 * paths including direct children.
	 */
	public static final Settings ALL_CHILDREN_WITH_DOT_PREFIX = new Settings() {
		/**
		 * Appends "." to {@code result} representing the base directory.
		 */
		@Override
		public StringBuilder appendPathForBaseDir(StringBuilder result, File baseDir) {
			return result.append('.');
		}

		/**
		 * Appends "./" to {@code result} as prefix for child directories, but
		 * only if {@code directChildOfBase==false} (no prefix for direct
		 * children of the base directory).
		 */
		@Override
		public StringBuilder appendPrefixForChildren(StringBuilder result, File baseDir, boolean directChildOfBase) {
			return result.append("./");
		}

		/**
		 * Appends one "../" element for every directory up to the common
		 * ancestor ({@code n} elements where {@code n=levelsToCommonAncestor}).
		 */
		@Override
		public StringBuilder appendPrefixToCommonAncestor(StringBuilder result, File baseDir, int levelsToCommonAncestor) {
			for (int i = 0; i < levelsToCommonAncestor; i++) {
				result.append("../");
			}
			return result;
		}
	};
	/**
	 * Alternative settings to construct relative paths without prefixes for
	 * children: "." represents the base directory, no prefix for child paths
	 * and "../" for elements up to the common ancestor.
	 */
	public static final Settings CHILDREN_WITHOUT_PREFIX = new Settings() {
		/**
		 * Appends "." to {@code result} representing the base directory.
		 */
		@Override
		public StringBuilder appendPathForBaseDir(StringBuilder result, File baseDir) {
			return result.append('.');
		}

		/**
		 * Appends "./" to {@code result} as prefix for child directories, but
		 * only if {@code directChildOfBase==false} (no prefix for direct
		 * children of the base directory).
		 */
		@Override
		public StringBuilder appendPrefixForChildren(StringBuilder result, File baseDir, boolean directChildOfBase) {
			return result;
		}

		/**
		 * Appends one "../" element for every directory up to the common
		 * ancestor ({@code n} elements where {@code n=levelsToCommonAncestor}).
		 */
		@Override
		public StringBuilder appendPrefixToCommonAncestor(StringBuilder result, File baseDir, int levelsToCommonAncestor) {
			for (int i = 0; i < levelsToCommonAncestor; i++) {
				result.append("../");
			}
			return result;
		}
	};

	private final File base;
	private final Settings settings;

	/**
	 * Constructor with base directory for relative paths using {@link #DEFAULT}
	 * settings to construct relative paths.
	 * 
	 * @param base
	 *            the basis for relative paths returned by
	 *            {@link #getRelativePathFor(File)}
	 */
	public RelativePathBase(String base) {
		this(new File(base));
	}

	/**
	 * Constructor with base directory for relative paths using {@link #DEFAULT}
	 * settings to construct relative paths.
	 * 
	 * @param base
	 *            the basis for relative paths returned by
	 *            {@link #getRelativePathFor(File)}
	 */
	public RelativePathBase(File base) {
		this(base, DEFAULT);
	}

	/**
	 * Constructor with base directory for relative paths using the specified
	 * settings to construct relative paths.
	 * 
	 * @param base
	 *            the basis for relative paths returned by
	 *            {@link #getRelativePathFor(File)}
	 * @param settings
	 *            the settings used to construct relative paths
	 */
	public RelativePathBase(String base, Settings settings) {
		this(new File(base), settings);
	}

	/**
	 * Constructor with base directory for relative paths using the specified
	 * settings to construct relative paths.
	 * 
	 * @param base
	 *            the basis for relative paths returned by
	 *            {@link #getRelativePathFor(File)}
	 * @param settings
	 *            the settings used to construct relative paths
	 */
	public RelativePathBase(File base, Settings settings) {
		this.base = base;
		this.settings = settings;
	}

	/**
	 * Returns the base directory for relative paths, the directory that was
	 * passed to the constructor.
	 */
	public File getBase() {
		return base;
	}

	/**
	 * Returns the settings used to construct relative paths. Has been passed to
	 * the constructor unless {@link #DEFAULT} settings are used.
	 * 
	 * @return the settings used to construct relative paths
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Returns the path of the given {@code file} relative to the
	 * {@link #getBase() base} directory.
	 * <p>
	 * The relative path is evaluated as follows:
	 * <ol>
	 * <li>If {@code base} is the same as {@code file}, "." is returned</li>
	 * <li>If {@code base} is the direct parent directory of {@code file}, the
	 * simple file name is returned</li>
	 * <li>If {@code base} is a non-direct parent directory of {@code file}, the
	 * relative path from {@code base} to {@code file} is returned</li>
	 * <li>If {@code base} is not parent directory of {@code file}, the relative
	 * path is the path from {@code base} to the common ancestor and then to
	 * {@code file}</li>
	 * <li>If {@code base} is not parent directory of {@code file} and the only
	 * common ancestor of the two is the base directory, the absolute path of
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
	 * @param file
	 *            the target file whose relative path is returned
	 * @return the path of {@code file} relative to the {@link #getBase() base}
	 *         directory
	 */
	public String getRelativePathFor(File file) {
		final StringBuilder path = new StringBuilder();
		if (base.equals(file)) {
			return settings.appendPathForBaseDir(path, file).toString();
		}
		if (base.equals(file.getParentFile())) {
			return settings.appendPrefixForChildren(path, base, true).append(file.getName()).toString();
		}
		final List<String> baseParts = FileUtil.getPathElements(base);
		final List<String> fileParts = FileUtil.getPathElements(file);

		final int len = Math.min(baseParts.size(), fileParts.size());
		int common = 0;
		while (common < len && baseParts.get(common).equals(fileParts.get(common))) {
			common++;
		}
		if (common == 0) {
			return file.getAbsolutePath().replace('\\', '/');
		}
		if (common < baseParts.size()) {
			settings.appendPrefixToCommonAncestor(path, base, baseParts.size() - common);
		} else {
			settings.appendPrefixForChildren(path, base, false);
		}
		for (int i = common; i < fileParts.size(); i++) {
			path.append(fileParts.get(i)).append('/');
		}
		// cut off last '/' and return string
		return path.deleteCharAt(path.length() - 1).toString();
	}

	/**
	 * Returns the path of the given {@code file} relative to the
	 * {@link #getBase() base} directory.
	 * <p>
	 * The relative path is evaluated as follows:
	 * <ol>
	 * <li>If {@code base} is the same as {@code file}, "." is returned</li>
	 * <li>If {@code base} is the direct parent directory of {@code file}, the
	 * simple file name is returned</li>
	 * <li>If {@code base} is a non-direct parent directory of {@code file}, the
	 * relative path from {@code base} to {@code file} is returned</li>
	 * <li>If {@code base} is not parent directory of {@code file}, the relative
	 * path is the path from {@code base} to the common ancestor and then to
	 * {@code file}</li>
	 * <li>If {@code base} is not parent directory of {@code file} and the only
	 * common ancestor of the two is the base directory, the absolute path of
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
	 * @param file
	 *            the target file whose relative path is returned
	 * @return the path of {@code file} relative to the {@link #getBase() base}
	 *         directory
	 */
	public String getRelativePathFor(String file) {
		return getRelativePathFor(new File(file));
	}
}
