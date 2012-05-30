package org.unix4j.cmd;

import java.io.File;

import org.unix4j.AbstractCommand;
import org.unix4j.Input;
import org.unix4j.arg.Arg;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.DefaultOpt;
import org.unix4j.arg.Opt;

public class Grep extends AbstractCommand<Grep.E> {
	
	public static final String NAME = "grep";
	
	public static interface Option {
		Opt<E> i = new DefaultOpt<E>(E.ignoreCase);
		Opt<E> ignoreCase = new DefaultOpt<E>(E.ignoreCase);
		Opt<E> v = new DefaultOpt<E>(E.invert);
		Opt<E> invert = new DefaultOpt<E>(E.invert);
	}
	public static interface Argument {
		Arg<E,String> expression = new DefaultArg<E,String>(E.expression, String.class, 1, 1);
		Arg<E,File> file = new DefaultArg<E,File>(E.file, File.class, 0, Integer.MAX_VALUE);
	}

	protected static enum E {
		ignoreCase, invert, expression, file;
	}
	
	public Grep() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		final String matchString = getArgs(Argument.expression).getFirst();
		final Input input = getInput();
		while (input.hasMoreLines()) {
			final String line = input.readLine();
			boolean matches;
			if (isOptSet(Option.i) || isOptSet(Option.ignoreCase)) {
				matches = line.toLowerCase().contains(matchString.toLowerCase());
			} else {
				matches = line.contains(matchString);
			}
			if (isOptSet(Option.v) || isOptSet(Option.invert)) {
				matches = !matches;
			}
			if (matches) {
				getOutput().writeLine(line);
			}
		}
	}

}
