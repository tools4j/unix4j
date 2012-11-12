package org.unix4j.unix.xargs;


public class Xarg {
	public static final String $0 = "$0";
	public static final String $1 = "$1";
	public static final String $2 = "$2";
	public static final String $3 = "$3";
	public static final String $4 = "$4";
	public static final String $5 = "$5";
	public static final String $6 = "$6";
	public static final String $7 = "$7";
	public static final String $8 = "$8";
	public static final String $9 = "$9";
	public static final String $args = "$*";
	public static final String[] $argsarray = {$args};
	public static final String args() {
		return "$*";
	}
	public static final String arg(int index) {
		return "$" + index;
	}
}
