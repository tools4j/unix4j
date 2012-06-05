package org.unix4j;

import org.unix4j.arg.ArgVals;
import org.unix4j.cmd.Echo;
import org.unix4j.cmd.Grep;
import org.unix4j.cmd.Ls;
import org.unix4j.cmd.NoOp;
import org.unix4j.cmd.Sort;
import org.unix4j.cmd.Xargs;

public class CommandBuilder {
	private Command<?> command = new NoOp();
	
	public CommandBuilder() {
		super();
	}
	
	public CommandBuilder join(Command<?> command) {
		this.command = this.command.join(command);
		return this;
	}
	
	public CommandBuilder readFrom(Input input) {
		command = command.readFrom(input);
		return this;
	}
	
	public CommandBuilder writeTo(Output output) {
		command = command.writeTo(output);
		return this;
	}
	
	public CommandBuilder clear() {
		command = new NoOp();
		return this;
	}

	public Command<?> build() {
		return command.clone();
	}
	
	public void execute() {
		build().execute();
	}

	//echo
	public CommandBuilder echo(ArgVals<Echo.ArgName,?> argVals) {
		return join(new Echo().withArgVals(argVals));
	}
	public CommandBuilder echo(ArgVals<Echo.ArgName,?> argVals1, ArgVals<Echo.ArgName,?> argVals2) {
		return join(new Echo().withArgVals(argVals1, argVals2));
	}
	public CommandBuilder echo(ArgVals<Echo.ArgName,?> argVals1, ArgVals<Echo.ArgName,?> argVals2, ArgVals<Echo.ArgName,?> argVals3) {
		return join(new Echo().withArgVals(argVals1, argVals2, argVals3));
	}
	public CommandBuilder echo(ArgVals<Echo.ArgName,?> argVals1, ArgVals<Echo.ArgName,?> argVals2, ArgVals<Echo.ArgName,?> argVals3, ArgVals<Echo.ArgName,?> argVals4) {
		return join(new Echo().withArgVals(argVals1, argVals2, argVals3, argVals4));
	}
	public CommandBuilder echo(ArgVals<Echo.ArgName,?> argVals1, ArgVals<Echo.ArgName,?> argVals2, ArgVals<Echo.ArgName,?> argVals3, ArgVals<Echo.ArgName,?> argVals4, ArgVals<Echo.ArgName,?> argVals5) {
		return join(new Echo().withArgVals(argVals1, argVals2, argVals3, argVals4, argVals5));
	}
	public CommandBuilder echo(ArgVals<Echo.ArgName,?>... argVals) {
		return join(new Echo().withArgVals(argVals));
	}

	//echo
	public CommandBuilder grep(ArgVals<Grep.ArgName,?> argVals) {
		return join(new Grep().withArgVals(argVals));
	}
	public CommandBuilder grep(ArgVals<Grep.ArgName,?> argVals1, ArgVals<Grep.ArgName,?> argVals2) {
		return join(new Grep().withArgVals(argVals1, argVals2));
	}
	public CommandBuilder grep(ArgVals<Grep.ArgName,?> argVals1, ArgVals<Grep.ArgName,?> argVals2, ArgVals<Grep.ArgName,?> argVals3) {
		return join(new Grep().withArgVals(argVals1, argVals2, argVals3));
	}
	public CommandBuilder grep(ArgVals<Grep.ArgName,?> argVals1, ArgVals<Grep.ArgName,?> argVals2, ArgVals<Grep.ArgName,?> argVals3, ArgVals<Grep.ArgName,?> argVals4) {
		return join(new Grep().withArgVals(argVals1, argVals2, argVals3, argVals4));
	}
	public CommandBuilder grep(ArgVals<Grep.ArgName,?> argVals1, ArgVals<Grep.ArgName,?> argVals2, ArgVals<Grep.ArgName,?> argVals3, ArgVals<Grep.ArgName,?> argVals4, ArgVals<Grep.ArgName,?> argVals5) {
		return join(new Grep().withArgVals(argVals1, argVals2, argVals3, argVals4, argVals5));
	}
	public CommandBuilder grep(ArgVals<Grep.ArgName,?>... argVals) {
		return join(new Grep().withArgVals(argVals));
	}
	
	//ls
	public CommandBuilder ls() {
		return join(new Ls());
	}
	public CommandBuilder ls(ArgVals<Ls.ArgName,?> argVals) {
		return join(new Ls().withArgVals(argVals));
	}
	public CommandBuilder ls(ArgVals<Ls.ArgName,?> argVals1, ArgVals<Ls.ArgName,?> argVals2) {
		return join(new Ls().withArgVals(argVals1, argVals2));
	}
	public CommandBuilder ls(ArgVals<Ls.ArgName,?> argVals1, ArgVals<Ls.ArgName,?> argVals2, ArgVals<Ls.ArgName,?> argVals3) {
		return join(new Ls().withArgVals(argVals1, argVals2, argVals3));
	}
	public CommandBuilder ls(ArgVals<Ls.ArgName,?> argVals1, ArgVals<Ls.ArgName,?> argVals2, ArgVals<Ls.ArgName,?> argVals3, ArgVals<Ls.ArgName,?> argVals4) {
		return join(new Ls().withArgVals(argVals1, argVals2, argVals3, argVals4));
	}
	public CommandBuilder ls(ArgVals<Ls.ArgName,?> argVals1, ArgVals<Ls.ArgName,?> argVals2, ArgVals<Ls.ArgName,?> argVals3, ArgVals<Ls.ArgName,?> argVals4, ArgVals<Ls.ArgName,?> argVals5) {
		return join(new Ls().withArgVals(argVals1, argVals2, argVals3, argVals4, argVals5));
	}
	public CommandBuilder ls(ArgVals<Ls.ArgName,?>... argVals) {
		return join(new Ls().withArgVals(argVals));
	}
	
	//sort
	public CommandBuilder sort() {
		return join(new Sort());
	}
	public CommandBuilder sort(ArgVals<Sort.ArgName,?> argVals) {
		return join(new Sort().withArgVals(argVals));
	}
	public CommandBuilder sort(ArgVals<Sort.ArgName,?> argVals1, ArgVals<Sort.ArgName,?> argVals2) {
		return join(new Sort().withArgVals(argVals1, argVals2));
	}
	public CommandBuilder sort(ArgVals<Sort.ArgName,?> argVals1, ArgVals<Sort.ArgName,?> argVals2, ArgVals<Sort.ArgName,?> argVals3) {
		return join(new Sort().withArgVals(argVals1, argVals2, argVals3));
	}
	public CommandBuilder sort(ArgVals<Sort.ArgName,?> argVals1, ArgVals<Sort.ArgName,?> argVals2, ArgVals<Sort.ArgName,?> argVals3, ArgVals<Sort.ArgName,?> argVals4) {
		return join(new Sort().withArgVals(argVals1, argVals2, argVals3, argVals4));
	}
	public CommandBuilder sort(ArgVals<Sort.ArgName,?> argVals1, ArgVals<Sort.ArgName,?> argVals2, ArgVals<Sort.ArgName,?> argVals3, ArgVals<Sort.ArgName,?> argVals4, ArgVals<Sort.ArgName,?> argVals5) {
		return join(new Sort().withArgVals(argVals1, argVals2, argVals3, argVals4, argVals5));
	}
	public CommandBuilder sort(ArgVals<Sort.ArgName,?>... argVals) {
		return join(new Sort().withArgVals(argVals));
	}
	
	//xargs
	public CommandBuilder xargs(ArgVals<Xargs.ArgName,?> argVals) {
		return join(new Xargs().withArgVals(argVals));
	}
	public CommandBuilder xargs(ArgVals<Xargs.ArgName,?> argVals1, ArgVals<Xargs.ArgName,?> argVals2) {
		return join(new Xargs().withArgVals(argVals1, argVals2));
	}
	public CommandBuilder xargs(ArgVals<Xargs.ArgName,?> argVals1, ArgVals<Xargs.ArgName,?> argVals2, ArgVals<Xargs.ArgName,?> argVals3) {
		return join(new Xargs().withArgVals(argVals1, argVals2, argVals3));
	}
	public CommandBuilder xargs(ArgVals<Xargs.ArgName,?> argVals1, ArgVals<Xargs.ArgName,?> argVals2, ArgVals<Xargs.ArgName,?> argVals3, ArgVals<Xargs.ArgName,?> argVals4) {
		return join(new Xargs().withArgVals(argVals1, argVals2, argVals3, argVals4));
	}
	public CommandBuilder xargs(ArgVals<Xargs.ArgName,?> argVals1, ArgVals<Xargs.ArgName,?> argVals2, ArgVals<Xargs.ArgName,?> argVals3, ArgVals<Xargs.ArgName,?> argVals4, ArgVals<Xargs.ArgName,?> argVals5) {
		return join(new Xargs().withArgVals(argVals1, argVals2, argVals3, argVals4, argVals5));
	}
	public CommandBuilder xargs(ArgVals<Xargs.ArgName,?>... argVals) {
		return join(new Xargs().withArgVals(argVals));
	}
	
}
