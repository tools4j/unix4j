package org.unix4j.unix.ls;

import org.unix4j.optset.DefaultOptionSet;
import org.unix4j.unix.Ls;

public class LsOptionSet extends DefaultOptionSet<Ls.Option> implements Ls.OptionSet {

	public LsOptionSet() {
		super(Ls.Option.class);
	}

	public LsOptionSet(Ls.Option option) {
		super(option);
	}

	public LsOptionSet(Ls.Option first, Ls.Option... rest) {
		super(first, rest);
	}

	@Override
	public Ls.OptionSet allFiles() {
		set(Ls.Option.allFiles);
		return this;
	}

	@Override
	public Ls.OptionSet a() {
		set(Ls.Option.a);
		return this;
	}

	@Override
	public Ls.OptionSet humanReadable() {
		set(Ls.Option.humanReadable);
		return this;
	}

	@Override
	public Ls.OptionSet h() {
		set(Ls.Option.h);
		return this;
	}

	@Override
	public Ls.OptionSet longFormat() {
		set(Ls.Option.longFormat);
		return this;
	}

	@Override
	public Ls.OptionSet l() {
		set(Ls.Option.l);
		return this;
	}

	@Override
	public Ls.OptionSet reverseOrder() {
		set(Ls.Option.reverseOrder);
		return this;
	}

	@Override
	public Ls.OptionSet r() {
		set(Ls.Option.r);
		return this;
	}

	@Override
	public Ls.OptionSet recurseSubdirs() {
		set(Ls.Option.recurseSubdirs);
		return this;
	}

	@Override
	public Ls.OptionSet R() {
		set(Ls.Option.R);
		return this;
	}

	@Override
	public Ls.OptionSet timeSorted() {
		set(Ls.Option.timeSorted);
		return this;
	}

	@Override
	public Ls.OptionSet t() {
		set(Ls.Option.t);
		return this;
	}
	
	@Override
	public LsOptionSet clone() {
		return (LsOptionSet)super.clone();
	}
	
	@Override
	public LsOptionSet copy() {
		return clone();
	}
}
