package org.unix4j.builder.gen;//FIXME remove gen package when ready to replace the builder in org.unix4j.builder

import org.unix4j.builder.DefaultCommandBuilder;
import org.unix4j.command.Command;
import org.unix4j.io.Input;
import org.unix4j.unix.Xargs;

<#foreach def in commandDefs> 
import ${def.command.pkg.name}.${def.command.simpleName};
</#foreach>

/**
 * Builder for a <b>unix4j</b> command or a chain of joined commands. 
 * Application code does usually not directly refer to this class but uses it 
 * indirectly through the static methods in {@link org.unix4j.Unix4j Unix4j}.
 * <p>
 * Note that the command creation methods do not return a new command instance.
 * Instead, the builder stores the created commands and only returns a 
 * {@link Command} object when the {@link #build()} method is invoked. 
 * <p>
 * The {@link Command} object returned by the {@link #build()} method can
 * represent a single command or a chain of commands. In a command chain, the
 * previous command usually pipes its output as standard input into the
 * next command (the pipe symbol between two commands in unix). For come 
 * commands, however, chaining has a different interpretation. An example is the
 * {@link Xargs xargs} command: here, the next command after {@code xargs} 
 * receives <i>arguments</i> from {@code xargs} instead of (standard) input.
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

	/**
	 * Constructor for a builder with the specified {@code input} passed to the 
	 * the first command as standard input.
	 * 
	 * @param input passed to the first command as standard input
	 */
	public Unix4jCommandBuilder(Input input) {
		super(input);
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
}
