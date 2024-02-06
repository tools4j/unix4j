<#include "/include/macros.fmpp">
package org.unix4j;

import org.unix4j.unix.Unix4jCommandBuilder;
import org.unix4j.unix.DefaultUnix4jCommandBuilder;
import org.unix4j.command.NoOp;
import org.unix4j.context.ExecutionContextFactory;

<#foreach def in commandDefs>
<#if countUsesStandardInput(def, false) != 0 && def.options?size != 0>
import ${def.pkg.name}.${def.command.simpleName}Options;
</#if>
</#foreach>

/**
 * Utility class with static methods serving as starting point to create a
 * command or build a command chain joining several commands.
 * <p> 
 * Every method returns a new builder instance. For more information and a 
 * detailed description of command building and chaining, see 
 * {@link Unix4jCommandBuilder}.
 */
public final class Unix4j {

	/**
	 * Returns a builder to create a command or command chain. The builder is 
	 * initialized with a {@link NoOp} command which will be replaced by the 
	 * first command joined to this builder's command chain.
	 * 
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder builder() {
		return new DefaultUnix4jCommandBuilder();
	}

	/**
	 * Returns a builder that uses the specified factory to create contexts for 
	 * command execution. The builder is initialized with a {@link NoOp} command 
	 * which will be replaced by the first command joined to this builder's 
	 * command chain.
	 * 
	 * @param contextFactory
	 *            the factory used to create execution contexts that are passed
	 *            to the execute method when a command is executed
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder use(ExecutionContextFactory contextFactory) {
		return new DefaultUnix4jCommandBuilder(contextFactory);
	}
	
<#foreach def in commandDefs>
<#foreach method in def.methods>
<#if !method.usesStandardInput>
	/**
	 * ${method.desc}
	 * <p>
	 * Note that the method returns the command builder to allow for command 
	 * chaining. The command itself is returned by the {@link Unix4jCommandBuilder#build() build()} method
	 * of the returned builder (see {@link Unix4jCommandBuilder} for more information).
	 *
<#foreach arg in method.args>
	 * @param ${arg} ${def.operands[arg].desc}
</#foreach>
	 * @return	the command builder to allow for method chaining. Method
	 * 			chaining is used here to create command chains. Adding a command 
	 * 			to the chain usually means that the previous command <i>pipes</i> 
	 * 			its output to the added command (the pipe symbol in unix)
	 */
	public static Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${def.operands[arg].type} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		return builder().${method.name}(<#foreach arg in method.args>${arg}<#if arg_has_next>, </#if></#foreach>);
	}
</#if>
</#foreach>
</#foreach>
	
	// no instances
	private Unix4j() {
		super();
	}
}
