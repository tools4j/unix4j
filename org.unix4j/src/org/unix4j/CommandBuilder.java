package org.unix4j;

import org.unix4j.impl.Echo;
import org.unix4j.impl.Grep;
import org.unix4j.impl.Ls;
import org.unix4j.impl.Sort;
import org.unix4j.impl.Xargs;

public interface CommandBuilder extends Ls<CommandBuilder>, Grep<CommandBuilder>, Echo<CommandBuilder>, Sort<CommandBuilder>, Xargs<CommandBuilder> {
	org.unix4j.Command<?, Void> build();
	void execute(Input input, Output output);
}
