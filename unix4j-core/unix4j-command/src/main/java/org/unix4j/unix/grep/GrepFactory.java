package org.unix4j.unix.grep;

import org.unix4j.unix.Grep;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 4/09/12
 * Time: 6:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class GrepFactory implements Grep.Interface<GrepCommand> {
	public static final GrepFactory INSTANCE = new GrepFactory();

	private GrepFactory() {
		super();
	}

	@Override
	public GrepCommand grep(String pattern) {
		final GrepArguments args = new GrepArguments();
		args.setPattern(pattern);
		return new GrepCommand(args);
	}

	@Override
	public GrepCommand grep(GrepOptions options, String pattern) {
		final GrepArguments args = new GrepArguments(options);
		args.setPattern(pattern);
		return new GrepCommand(args);
	}
}
