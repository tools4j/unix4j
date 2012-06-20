package org.unix4j.cmd;

import org.unix4j.AbstractCommand;
import org.unix4j.Input;
import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgVals;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.DefaultArgVals;

import java.util.Iterator;


public class Cut extends AbstractCommand<Cut.ArgName> {

	public static final String NAME = "cut";

	public static interface Option {
	}
	public static class Argument {
		public static final Arg<ArgName,Integer> field = new DefaultArg<ArgName,Integer>(ArgName.field, Integer.class, 0, 100);
		public static ArgVals<ArgName, Integer> field(Integer field) {
			return new DefaultArgVals<Cut.ArgName, Integer>(Argument.field, field);
		}
		public static ArgVals<ArgName, Integer> field(Integer ... fields) {
			return new DefaultArgVals<Cut.ArgName, Integer>(Argument.field, fields);
		}
		public static final Arg<ArgName,String> delimiter = new DefaultArg<ArgName,String>(ArgName.field, String.class, 0, 1);
		public static ArgVals<ArgName, String> delimiter(String delimiter) {
			return new DefaultArgVals<Cut.ArgName, String>(Argument.delimiter, delimiter);
		}
		public static final Arg<ArgName,Integer> character = new DefaultArg<ArgName,Integer>(ArgName.character, Integer.class, 0, 100);
		public static ArgVals<ArgName, Integer> character(Integer characters) {
			return new DefaultArgVals<Cut.ArgName, Integer>(Argument.character, characters);
		}
		public static ArgVals<ArgName, Integer> character(Integer ... characters) {
			return new DefaultArgVals<Cut.ArgName, Integer>(Argument.character, characters);
		}
	}

	protected static enum ArgName {
		delimiter, field, character;
	}

	public Cut() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		if(!getArgs(Argument.field).isEmpty() && !getArgs(Argument.character).isEmpty()){
			throw new IllegalArgumentException("Cannot specify field/f argument(s) as well as character/c argument(s).");
		}
		if(!getArgs(Argument.field).isEmpty()){
			cutByField();
		} else if(!getArgs(Argument.character).isEmpty()){
			if(!getArgs(Argument.delimiter).isEmpty()){
				throw new IllegalArgumentException("Delimiter argument can only be specified with the field argument.");
			}
			cutByCharacter();
		} else {
			throw new IllegalArgumentException("Either field or character argument(s) must be specified.");
		}
	}

	private void cutByCharacter() {
		final Input input = getInput();

		while (input.hasMoreLines()) {
			final String line = input.readLine();
			final StringBuilder sb = new StringBuilder();
			final Iterator<Integer> charNumbers = getArgs(Argument.character).getAll().iterator();

			while(charNumbers.hasNext()){
				final int charNum = charNumbers.next();
				if( charNum > 0 && charNum <= line.length()){
					sb.append(line.charAt(charNum - 1));
				}
			}
			getOutput().writeLine(sb.toString());
		}
	}

	private void cutByField() {
		final Input input = getInput();
		while (input.hasMoreLines()) {
			final String line = input.readLine();

			String delimiter = "\t";
			if( super.isArgSet(Argument.delimiter)){
				delimiter = getArgs(Argument.delimiter).getFirst();
			}
			final String outputDelimiter=delimiter;

			final String[] splitLine = line.split(delimiter);

			final StringBuilder sb = new StringBuilder();
			final Iterator<Integer> fieldNumbers = getArgs(Argument.field).iterator();
			while(fieldNumbers.hasNext()){
				final int fieldNumber = fieldNumbers.next();
				if(splitLine.length >= fieldNumber && fieldNumber > 0){
					sb.append(splitLine[fieldNumber-1]);
				}
				if(fieldNumbers.hasNext()){
					sb.append(outputDelimiter);
				}
			}
			getOutput().writeLine(sb.toString());
		}
	}
}
