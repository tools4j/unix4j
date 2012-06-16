package org.unix4j.builder;

import org.unix4j.command.impl.Echo;
import org.unix4j.command.impl.Grep;
import org.unix4j.command.impl.Ls;
import org.unix4j.command.impl.Sort;
import org.unix4j.command.impl.Xargs;

public interface Unix4jCommandBuilder extends CommandBuilder, Ls.Interface<Unix4jCommandBuilder>, Grep.Interface<Unix4jCommandBuilder>, Echo.Interface<Unix4jCommandBuilder>, Sort.Interface<Unix4jCommandBuilder>, Xargs.Interface<Unix4jCommandBuilder> {
}
