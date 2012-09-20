package org.unix4j.builder;

import org.unix4j.builder.DefaultCommandBuilder;
import org.unix4j.command.Command;

<#foreach def in commandDefs> 
import ${def.command.pkg.name}.${def.command.simpleName};
import ${def.pkg.name}.*;
</#foreach>

/**
 * Builder for a <b>unix4j</b> command or a chain of joined commands. 
 * Application code does usually not directly refer to this class but uses it 
 * indirectly through the static methods in {@link org.unix4j.Unix4j Unix4j}.
 * <p>
 * Note that the command creation methods do not return a new command instance.
 * Instead, the builder stores the created commands and only returns a 
 * {@link Command} object when the {@link #build()} method is invoked. Most
 * applications, however, need not to call {@code build()} explicitly. The
 * command can be built and executed in a single step by calling one of the 
 * {@code toXXX(..)} methods, such as {@link #toStdOut()}, 
 * {@link #toFile(String)} or {@link #toStringResult()}. 
 * <p>
 * The {@link Command} object returned by the {@link #build()} method can
 * represent a single command or a chain of commands. In a command chain, the
 * previous command usually pipes its output as standard input into the
 * next command (the pipe symbol between two commands in unix). For come 
 * commands, however, chaining has a different interpretation. An example is the
 * {@code xargs} command: here, the next command after {@code xargs} receives 
 * <i>arguments</i> from {@code xargs} instead of (standard) input.
 */
public class Unix4jCommandBuilder extends DefaultCommandBuilder
	implements 
<#foreach def in commandDefs>
		${def.command.simpleName}.Interface<Unix4jCommandBuilder><#if def_has_next>,<#else> {</#if>
</#foreach>

	/**
	 * Constructor for a builder where the standard input is used as input for
	 * the first command.
	 */
	public Unix4jCommandBuilder() {
		super();
	}

<#foreach def in commandDefs>

	/* ------------------ ${def.commandName} ------------------ */
<#foreach method in def.methods>
	/**
	 * ${method.desc}
	 * <p>
	 * Note that the method returns {@code this} builder to allow for command 
	 * chaining. The command itself is returned by the {@link #build()} method. 
	 * See {@link Unix4jCommandBuilder class comments} for more information.
	 *
<#foreach arg in method.args>
	 * @param ${arg} ${def.operands[arg].desc}
</#foreach>
	 * @return	{@code this} builder to allow for method chaining. Method
	 * 			chaining is used here to create command chains. Adding a command 
	 * 			to the chain usually means that the previous command <i>pipes</i> 
	 * 			its output to the added command (the pipe symbol in unix)
	 */
	@Override
	public Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${def.operands[arg].type} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		join(${def.command.simpleName}.FACTORY.${method.name}(<#foreach arg in method.args>${arg}<#if arg_has_next>, </#if></#foreach>));
		return this;
	}
</#foreach>
</#foreach>

	@Override
	public Unix4jCommandBuilder join(Command<?> command) {
		super.join(command);
		return this;
	}

}