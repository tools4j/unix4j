package org.unix4j.cmd;

import java.io.File;

import org.unix4j.AbstractCommand;

public class Ls extends AbstractCommand<Ls.Option> {

	public static final String NAME = "ls";

	public enum Option{
		l, a, r, t, file;
	}

	public Ls() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		if (!getArgs().isOptSet(Option.file)) {
			listFiles(new File(System.getProperty("user.dir")));
		} else {
			for (final String arg : getArgs().getArgumentOfOption(Option.file).getValues()) {
				listFiles(new File(arg));
			}
		}
	}

	@Override
	public Option getDefaultArgumentOption() {
		return Option.file;
	}

	private void listFiles(File dir) {
		for (File file : dir.listFiles()) {
			getOutput().writeLine(file.toString());
		}
	}

}