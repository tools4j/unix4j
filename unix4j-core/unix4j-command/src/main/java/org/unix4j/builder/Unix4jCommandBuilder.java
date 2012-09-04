package org.unix4j.builder;

import org.unix4j.redirect.From;
import org.unix4j.unix.*;

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
