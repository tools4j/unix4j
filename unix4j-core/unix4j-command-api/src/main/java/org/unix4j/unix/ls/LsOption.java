package org.unix4j.unix.ls;

import org.unix4j.codegen.annotation.Javadoc;
import org.unix4j.optset.Option;

@Javadoc("Option flags for the {@code ls} command.")
public enum LsOption implements Option {
	@Javadoc("Lists all files in the given directory, including hidden files " +
	"(those whose names start with \".\" in Unix). By default, these files " +
	"are excluded from the list.")
	allFiles('a'),
	@Javadoc("Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)")
	humanReadable('h'),
	@Javadoc("Long format, displaying file types, permissions, number of hard " +
	"links, owner, group, size, date, and filename.")
	longFormat('l'),
	@Javadoc("Recursively lists subdirectories encountered.")
	recurseSubdirs('R'),
	@Javadoc("Reverses the order of the sort to get reverse collating sequence " +
	"or oldest first.")
	reverseOrder('r'),
	@Javadoc("Sorts with the primary key being time modified (most recently " +
	"modified first) and the secondary key being filename in the collating " +
	"sequence.")
	timeSorted('t');
	
	private final char acronym;
	private LsOption(char acronym) {
		this.acronym = acronym;
	}
	@Override
	public char acronym() {
		return acronym;
	}
}