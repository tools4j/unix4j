package org.unix4j.unix.ls;

import org.unix4j.annotation.Alias;


/**
 * Option flags for the ls command.
 */
enum LsOption {
	/**
	 * Lists all files in the given directory, including hidden files (those
	 * whose names start with "." in Unix). By default, these files are
	 * excluded from the list.
	 */
	@Alias("a")
	allFiles,
	/**
	 * Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)
	 */
	@Alias("h")
	humanReadable,
	/**
	 * Long format, displaying file types, permissions, number of hard
	 * links, owner, group, size, date, and filename.
	 */
	@Alias("l")
	longFormat,
	/**
	 * Recursively lists subdirectories encountered.
	 */
	@Alias("R")
	recurseSubdirs,
	/**
	 * Reverses the order of the sort to get reverse collating sequence or
	 * oldest first.
	 */
	@Alias("r")
	reverseOrder,
	/**
	 * Sorts with the primary key being time modified (most recently
	 * modified first) and the secondary key being filename in the collating
	 * sequence.
	 */
	@Alias("t")
	timeSorted;
}