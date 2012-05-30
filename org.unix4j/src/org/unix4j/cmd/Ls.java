package org.unix4j.cmd;

import java.io.File;

import org.unix4j.AbstractCommand;
import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.DefaultArg;

public class Ls extends AbstractCommand<Ls.E> {
	
	public static final String NAME = "ls";
	
	public static interface Argument {
		Arg<E,File> files = new DefaultArg<E,File>(E.files, File.class, 0, 1);
	}
	protected static enum E {
		l, a, r, t, files
	}
	
	public Ls() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		final ArgList<E,File> args = getArgs(Argument.files);
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
