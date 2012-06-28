package org.unix4j.unix.ls;

import java.io.File;
import java.util.List;

import org.unix4j.unix.Ls.Interface;
import org.unix4j.unix.Ls.Option;

/**
 * Factory class returning a new {@link LsCommand} instance from every signature
 * method.
 */
public final class LsFactory implements Interface<LsCommand> {
	
	/**
	 * The singleton instance of this factory.
	 */
	public static final LsFactory INSTANCE = new LsFactory();
	
	/**
	 * Private, only used to create singleton instance.
	 */
	private LsFactory() {
		super();
	}

	@Override
	public LsCommand ls() {
		return new LsCommand(new LsArgs());
	}

	@Override
	public LsCommand ls(String... files) {
		return new LsCommand(new LsArgs(files));
	}

	@Override
	public LsCommand ls(Option... options) {
		final LsArgs args = new LsArgs();
		args.setOpts(options);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(File file, Option... options) {
		final LsArgs args = new LsArgs(file);
		args.setOpts(options);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(List<File> files, Option... options) {
		final LsArgs args = new LsArgs(files);
		args.setOpts(options);
		return new LsCommand(args);
	}
}