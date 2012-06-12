package org.unix4j;

import org.unix4j.impl.Echo;
import org.unix4j.impl.Grep;
import org.unix4j.impl.Ls;
import org.unix4j.impl.Sort;
import org.unix4j.impl.Xargs;

public interface CommandBuilder extends Ls.Interface<CommandBuilder>, Grep.Interface<CommandBuilder>, Echo.Interface<CommandBuilder>, Sort.Interface<CommandBuilder>, Xargs.Interface<CommandBuilder> {
	Command<?, Void> build();
	void execute(Input input, Output output);
}
