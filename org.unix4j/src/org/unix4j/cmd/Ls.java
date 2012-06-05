package org.unix4j.cmd;

import java.io.File;

import org.unix4j.AbstractCommand;
import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.ArgVals;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.DefaultArgVals;

public class Ls extends AbstractCommand<Ls.ArgName> {
	
	public static final String NAME = "ls";
	
	public static class Argument {
		public static final Arg<ArgName,File> files = new DefaultArg<ArgName,File>(ArgName.files, File.class, 1, Integer.MAX_VALUE);
		public static ArgVals<ArgName, File> files(File... files) {
			return new DefaultArgVals<Ls.ArgName, File>(Argument.files, files);
		}
	}
	public static enum ArgName {
		l, a, r, t, files
	}
	
	public Ls() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		final ArgList<ArgName,File> args = getArgs(Argument.files);
		if (args.isEmpty()) {
			listFiles(new File(System.getProperty("user.dir")));
		} else {
			for (final File arg : args) {
				listFiles(arg);
			}
		}
	}

	private void listFiles(File dir) {
		for (File file : dir.listFiles()) {
			getOutput().writeLine(file.toString());
		}
	}
	
}
