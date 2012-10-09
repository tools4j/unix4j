package org.unix4j.builder;

import org.unix4j.builder.DefaultCommandBuilder;
import org.unix4j.command.Command;
import org.unix4j.command.NoOp;

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
	 * Default constructor, initialized to build a {@link NoOp} command if no 
	 * command is {@link #join(Command) joined} to this builder's command chain.
	 */
	public DefaultUnix4jCommandBuilder() {
		super();
	}

<#foreach def in commandDefs>

	/* ------------------ ${def.commandName} ------------------ */
<#foreach method in def.methods>
	@Override
	public Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${def.operands[arg].type} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		join(${def.command.simpleName}.FACTORY.${method.name}(<#foreach arg in method.args>${arg}<#if arg_has_next>, </#if></#foreach>));
		return this;
	}
</#foreach>
</#foreach>

	@Override
	public Unix4jCommandBuilder$ withVariables() {
		final Unix4jCommandBuilder$ builder$ = new DefaultUnix4jCommandBuilder$();
		builder$.join(build());
		return builder$;
	}

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
