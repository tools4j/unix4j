package org.unix4j.unix.ls;

import org.unix4j.annotation.Alias;
import org.unix4j.annotation.Javadoc;

@Javadoc("Option flags for the {@code ls} command.")
enum LsOption {
	@Javadoc("Lists all files in the given directory, including hidden files " +
	"(those whose names start with \".\" in Unix). By default, these files " +
	"are excluded from the list.")
	@Alias("a")
	allFiles,
	@Javadoc("Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)")
	@Alias("h")
	humanReadable,
	@Javadoc("Long format, displaying file types, permissions, number of hard " +
	"links, owner, group, size, date, and filename.")
	@Alias("l")
	longFormat,
	@Javadoc("Recursively lists subdirectories encountered.")
	@Alias("R")
	recurseSubdirs,
	@Javadoc("Reverses the order of the sort to get reverse collating sequence " +
	"or oldest first.")
	@Alias("r")
	reverseOrder,
	@Javadoc("Sorts with the primary key being time modified (most recently " +
	"modified first) and the secondary key being filename in the collating " +
	"sequence.")
	@Alias("t")
	timeSorted;
}