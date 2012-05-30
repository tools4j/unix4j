package org.unix4j.arg;

import java.io.File;
import java.util.List;

public class Grep implements Command<Grep.Key> {

	public static final String NAME = "grep";

	public static interface Option {
		Opt i = new Opt(Key.ignoreCase);
		Opt ignoreCase = new Opt(Key.ignoreCase);
		Opt v = new Opt(Key.invert);
		Opt invert = new Opt(Key.invert);
	}
	public static interface Argument {
		Arg<String> expression = new Arg<String>(Key.expression, String.class, 1, 1);
		Arg<File> file = new Arg<File>(Key.file, File.class, 0, Integer.MAX_VALUE);
	}

	protected static enum Key {
		ignoreCase, invert, expression, file;
	}
	private static final class Arg<V> extends DefaultArg<Key, V> {
		private Arg(Key key, Class<V> valueType, int minOccurance, int maxOccurance) {
			super(key, valueType, minOccurance, maxOccurance);
		}
	}
	private static final class Opt extends DefaultOpt<Key> {
		private Opt(Key key) {
			super(key);
		}
	}

	@Override
	public <V> Command<Key> setArg(org.unix4j.arg.Arg<Key, V> arg, V value) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public <V> Command<Key> setArgs(org.unix4j.arg.Arg<Key, V> arg, V... values) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public <V> Command<Key> setArgs(org.unix4j.arg.Arg<Key, V> arg, List<? extends V> values) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public <V> V getArg(org.unix4j.arg.Arg<Key, V> arg) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public <V> List<V> getArgs(org.unix4j.arg.Arg<Key, V> arg) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Command<Key> setOpt(org.unix4j.arg.Opt<Key> opt) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean isOptSet(org.unix4j.arg.Opt<Key> opt) {
		// TODO Auto-generated method stub
		return false;
	}
}
