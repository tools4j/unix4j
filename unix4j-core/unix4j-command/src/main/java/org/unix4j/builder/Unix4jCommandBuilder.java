package org.unix4j.builder;

import org.unix4j.redirect.From;
import org.unix4j.unix.Cut;
import org.unix4j.unix.Echo;
import org.unix4j.unix.Grep;
import org.unix4j.unix.Head;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Sed;
import org.unix4j.unix.Sort;
import org.unix4j.unix.Tail;
import org.unix4j.unix.Uniq;
import org.unix4j.unix.Wc;
import org.unix4j.unix.Xargs;

public interface Unix4jCommandBuilder
		extends CommandBuilder,
		From.Interface<Unix4jCommandBuilder>,
		Ls.Interface<Unix4jCommandBuilder>,
		Grep.Interface<Unix4jCommandBuilder>,
		Echo.Interface<Unix4jCommandBuilder>,
		Sort.Interface<Unix4jCommandBuilder>,
		Cut.Interface<Unix4jCommandBuilder>,
		Sed.Interface<Unix4jCommandBuilder>,
		Wc.Interface<Unix4jCommandBuilder>,
		Head.Interface<Unix4jCommandBuilder>,
		Tail.Interface<Unix4jCommandBuilder>,
		Uniq.Interface<Unix4jCommandBuilder>,
		Xargs.Interface<Unix4jCommandBuilder>{
}
