package org.unix4j.unix.ls;

import org.unix4j.codegen.annotation.Acronym;
import org.unix4j.codegen.annotation.Javadoc;

@Javadoc("Option flags for the {@code ls} command.")
public enum LsOption {
	@Javadoc("Lists all files in the given directory, including hidden files " +
	"(those whose names start with \".\" in Unix). By default, these files " +
	"are excluded from the list.")
	@Acronym('a')
	allFiles,
	@Javadoc("Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)")
	@Acronym('h')
	humanReadable,
	@Javadoc("Long format, displaying file types, permissions, number of hard " +
	"links, owner, group, size, date, and filename.")
	@Acronym('l')
	longFormat,
	@Javadoc("Recursively lists subdirectories encountered.")
	@Acronym('R')
	recurseSubdirs,
	@Javadoc("Reverses the order of the sort to get reverse collating sequence " +
	"or oldest first.")
	@Acronym('r')
	reverseOrder,
	@Javadoc("Sorts with the primary key being time modified (most recently " +
	"modified first) and the secondary key being filename in the collating " +
	"sequence.")
	@Acronym('t')
	timeSorted;
}