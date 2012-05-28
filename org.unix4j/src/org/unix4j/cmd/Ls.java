package org.unix4j.cmd;

import java.io.File;

import org.unix4j.AbstractCommand;

public class Ls extends AbstractCommand<Ls.Option> {
	
	public static final String NAME = "ls";
	public static enum Option {
		l, a, r, t
	}
	
	public Ls() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		if (getArgCount() == 0) {
			listFiles(new File(System.getProperty("user.dir")));
		} else {
			for (final String arg : getArgs()) {
				listFiles(new File(arg));
			}
		}
	}

	private void listFiles(File dir) {
		for (File file : dir.listFiles()) {
			getOutput().writeLine(file.toString());
		}
	}
	
}
