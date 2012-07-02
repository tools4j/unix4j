package org.unix4j.unix.ls;

import java.io.File;

import org.unix4j.unix.Ls.Interface;
import org.unix4j.unix.Ls.OptionSet;

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
	public LsCommand ls(File... files) {
		return new LsCommand(new LsArgs(files));
	}

	@Override
	public LsCommand ls(String... files) {
		return new LsCommand(new LsArgs(files));
	}

	@Override
	public LsCommand ls(OptionSet options) {
		final LsArgs args = new LsArgs();
		args.setOpts(options);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(OptionSet options, File... files) {
		final LsArgs args = new LsArgs(files);
		args.setOpts(options);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(OptionSet options, String... files) {
		final LsArgs args = new LsArgs(files);
		args.setOpts(options);
		return new LsCommand(args);
	}

}