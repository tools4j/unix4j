package org.unix4j;

import org.unix4j.impl.Echo;
import org.unix4j.impl.Grep;
import org.unix4j.impl.Ls;
import org.unix4j.impl.Sort;
import org.unix4j.impl.Xargs;

public interface Unix4jCommandBuilder extends CommandBuilder, Ls.Interface<Unix4jCommandBuilder>, Grep.Interface<Unix4jCommandBuilder>, Echo.Interface<Unix4jCommandBuilder>, Sort.Interface<Unix4jCommandBuilder>, Xargs.Interface<Unix4jCommandBuilder> {
}
