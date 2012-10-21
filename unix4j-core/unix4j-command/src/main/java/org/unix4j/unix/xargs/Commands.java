package org.unix4j.unix.xargs;

import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.vars.FileVar;
import org.unix4j.vars.FilesVar;
import org.unix4j.vars.StringsVar;

public class Commands {

	public static final Command<Ls<Unix4jCommandBuilder>, Unix4jCommandBuilder> ls = new Command<Ls<Unix4jCommandBuilder>, Unix4jCommandBuilder>() {
		@Override
		public Ls<Unix4jCommandBuilder> create(Commander commander, Unix4jCommandBuilder returnValue) {
			return new Ls<Unix4jCommandBuilder>(returnValue);
		}
	};
	public static final Command<Cat<Unix4jCommandBuilder>, Unix4jCommandBuilder> cat = new Command<Cat<Unix4jCommandBuilder>, Unix4jCommandBuilder>() {
		@Override
		public Cat<Unix4jCommandBuilder> create(Commander commander, Unix4jCommandBuilder returnValue) {
			return new Cat<Unix4jCommandBuilder>(returnValue);
		}
	};
	
	public static interface Commander {
		<E extends Executable<Unix4jCommandBuilder>> E exec(Command<E, Unix4jCommandBuilder> command);
	}
	
	public static interface Executable<R> {}
	public static interface Command<E extends Executable<R>, R> {
		E create(Commander commander, R returnValue); 
	}
	
	public static class Ls<R> implements Executable<R> {
		private final R returnValue;
		public Ls(R returnValue) {
			this.returnValue = returnValue;
		}
		public R ls(String file) {
			//bla
			return returnValue;
		}
		public R with(String file) {
			//bla
			return returnValue;
		}
		public R ls(FilesVar files) {
			//bla
			return returnValue;
		}
		public R with(FilesVar files) {
			//bla
			return returnValue;
		}
		public R ls(FileVar files) {
			//bla
			return returnValue;
		}
		public R with(FileVar files) {
			//bla
			return returnValue;
		}
		public R with(StringsVar paths) {
			//bla
			return returnValue;
		}
	}
	public static class Cat<R> implements Executable<R> {
		private final R returnValue;
		public Cat(R returnValue) {
			this.returnValue = returnValue;
		}
		public R cat() {
			//bla
			return returnValue;
		}
		public R with(String value) {
			//bla
			return returnValue;
		}
	}
}
