package org.unix4j.builder;

import org.unix4j.builder.DefaultCommandBuilder;
import org.unix4j.command.Command;
import org.unix4j.command.NoOp;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

<#foreach def in commandDefs> 
import ${def.command.pkg.name}.${def.command.simpleName};
<#if def.options?size != 0>
import ${def.pkg.name}.${def.command.simpleName}Options;
</#if>
</#foreach>

/**
 * Default implementation for {@link Unix4jCommandBuilder}. Application code 
 * does usually not directly refer to this class but uses it indirectly through 
 * the static methods in {@link org.unix4j.Unix4j Unix4j}.
 */
public class DefaultUnix4jCommandBuilder extends DefaultCommandBuilder implements Unix4jCommandBuilder {

	/**
	 * Default constructor initialized with a {@link NoOp} command which will be 
	 * replaced by the first command joined to this builder's command chain. 
	 * Uses a {@link DefaultExecutionContext} to execute commands.
	 */
	public DefaultUnix4jCommandBuilder() {
		super();
	}

	/**
	 * Constructor using the specified factory to create contexts for command
	 * execution. The builder is initialized with a {@link NoOp} command which
	 * will be replaced by the first command joined to this builder's command 
	 * chain.
	 * 
	 * @param contextFactory
	 *            the factory used to create execution contexts that are passed
	 *            to the execute method when a command is executed
	 */
	public DefaultUnix4jCommandBuilder(ExecutionContextFactory contextFactory) {
		super(contextFactory);
	}

<#foreach def in commandDefs>

	/* ------------------ ${def.commandName} ------------------ */
<#foreach method in def.methods>
	@Override
	public Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${def.operands[arg].type} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		join(${def.command.simpleName}.Factory.${method.name}(<#foreach arg in method.args>${arg}<#if arg_has_next>, </#if></#foreach>));
		return this;
	}
</#foreach>
</#foreach>

	@Override
	public Unix4jCommandBuilder join(Command<?> command) {
		super.join(command);
		return this;
	}
	
	@Override
	public Unix4jCommandBuilder reset() {
		super.reset();
		return this;
	}

}
