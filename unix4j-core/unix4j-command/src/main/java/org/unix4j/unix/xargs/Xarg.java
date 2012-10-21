package org.unix4j.unix.xargs;

import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.unix.Ls;
import org.unix4j.unix.xargs.Commands.Command;
import org.unix4j.unix.xargs.Commands.Commander;
import org.unix4j.unix.xargs.Commands.Executable;
import org.unix4j.vars.DefaultFilesVar;
import org.unix4j.vars.DefaultStringsVar;
import org.unix4j.vars.FileVar;
import org.unix4j.vars.FilesVar;
import org.unix4j.vars.StringVar;
import org.unix4j.vars.StringsVar;

public class Xarg implements Commander {
	
	public static final FilesVar files = asFiles(1);
	public static final FilesVar asFiles(int index) {
		return new DefaultFilesVar("$" + index);
	}
	public static final StringsVar args = asStrings(1);
	public static final StringsVar asStrings(int index) {
		return new DefaultStringsVar("$" + index);
	}
	public static final StringsVar $1 = asStrings(1);
	public static final StringsVar $2 = asStrings(2);
	
	public static final FilesVar files$1 = asFiles(1);
	public static final FilesVar files$2 = asFiles(2);

	public static final StringVar $1() {
		return null;
	}
	public static final FileVar $2() {
		return null;
	}
	
	public <E extends Executable<Unix4jCommandBuilder>> E xargs(Command<E, Unix4jCommandBuilder> command) {
		return exec(command);
	}
	@Override
	public <E extends Executable<Unix4jCommandBuilder>> E exec(Command<E, Unix4jCommandBuilder> command) {
		return command.create(this, Unix4j.builder());
	}
	
	public Commander xargs() {
		return this;
	}
	
	public Commander find(String name) {
		return this;
	}
	
	{
		Unix4j.builder().withVariables().ls(Ls.OPTIONS.l.a, new DefaultFilesVar("$1")).toStdOut();
		Unix4j.builder().withVariables().ls(Ls.OPTIONS.l.a, Xarg.asFiles(1)).toStdOut();
		Unix4j.builder().withVariables().ls(Ls.OPTIONS.l.a, Xarg.asStrings(1)).toStdOut();
		
		xargs().exec(Commands.ls).ls("/usr/home").grep("*").toStdOut();
		xargs(Commands.ls).ls("/usr/home").grep("*").toStdOut();
		find(".").exec(Commands.cat).cat().grep("*").toStdOut();

		xargs().exec(Commands.ls).with("/usr/home").grep("*").toStdOut();
		xargs(Commands.ls).with("/usr/home").grep("*").toStdOut();
		find(".").exec(Commands.cat).with("{}").grep("*").toStdOut();
		
		xargs(Commands.ls).ls(Xarg.files).grep("*").toStdOut();
		find(".").exec(Commands.ls).ls(Find.file).grep("*").toStdOut();
		
		xargs(Commands.ls).with(Xarg.args).grep("*").toStdOut();
		xargs(Commands.ls).with(Xarg.files).grep("*").toStdOut();
		xargs(Commands.ls).with(Xarg.$1).grep("*").toStdOut();
		xargs(Commands.ls).with(Xarg.files$1).grep("*").toStdOut();
		find(".").exec(Commands.ls).with(Find.file).grep("*").toStdOut();
		
		//		xargs().ls("/usr/home").grep("*").toStdOut();
//		find(".").exec().cat().grep("*").toStdOut();
	}
	
}
