<#include "/include/builder-method-javadoc.fmpp">
package org.unix4j.builder;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.Command;

<#foreach def in commandDefs> 
import ${def.command.pkg.name}.${def.command.simpleName};
<#if def.options?size != 0>
import ${def.pkg.name}.${def.command.simpleName}Options;
</#if>
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
public interface Unix4jCommandBuilder extends CommandBuilder,
<#foreach def in commandDefs>
		${def.command.simpleName}.Interface<Unix4jCommandBuilder><#if def_has_next>,<#else> {</#if>
</#foreach>

<#foreach def in commandDefs>

	/* ------------------ ${def.commandName} ------------------ */
<#foreach method in def.methods>
<@builderMethodJavadoc def method/>
	@Override
	Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${def.operands[arg].type} ${arg}<#if arg_has_next>, </#if></#foreach>);
</#foreach>
</#foreach>

	/**
	 * Returns a builder with extended support for use of variables when 
	 * constructing commands. The returned builder inherits the command chain
	 * from this builder. 
	 * 
	 * @return 	a builder with extended support for variable use when 
	 * 			constructing commands
	 */
	Unix4jCommandBuilder4Vars withVariables();

	//override with specialized return type
	@Override
	Unix4jCommandBuilder join(Command<?> command);
	
	//override with specialized return type
	@Override
	Unix4jCommandBuilder reset();

}
