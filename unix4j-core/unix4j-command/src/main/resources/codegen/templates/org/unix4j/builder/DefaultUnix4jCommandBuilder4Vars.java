<#include "/include/macros.fmpp">
package org.unix4j.builder;

import org.unix4j.command.Command;
import org.unix4j.command.NoOp;
import ${varPkgName}.*;

<#foreach def in commandDefs> 
<#if def.options?size != 0>
import ${def.pkg.name}.${def.command.simpleName}Options;
</#if>
<#foreach opd in def.operands?values>
	<#if indexOfOperandByType(def.operands, opd) == opd_index><#-- otherwise it is a repeted occurance of this type -->
		<#if isCommandSpecificOperand(def, opd)>
import ${def.pkg.name}.${def.command.simpleName}4Vars.${varIfaceName(opd)};
		</#if>
	</#if>
</#foreach>
</#foreach>

/**
 * Default implementation for {@link Unix4jCommandBuilder4Vars}. Application code 
 * does usually not directly refer to this class but uses it indirectly through 
 * the static methods in {@link org.unix4j.Unix4j Unix4j}.
 */
public class DefaultUnix4jCommandBuilder4Vars extends DefaultUnix4jCommandBuilder implements Unix4jCommandBuilder4Vars {

	/**
	 * Default constructor, initialized to build a {@link NoOp} command if no 
	 * command is {@link #join(Command) joined} to this builder's command chain.
	 */
	public DefaultUnix4jCommandBuilder4Vars() {
		super();
	}

<#macro methodSignatureForVars def method isVarFlags>
<#if method.args?size != isVarFlags?length>
<@methodSignatureForVars def method isVarFlags+"T"/>
<@methodSignatureForVars def method isVarFlags+"F"/>
<#else>
<#if isVarFlags?contains("T")><#--at least one variable, otherwise it is the same as the standard method without variables -->
	@Override
	public Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${fixedOrVarType(def arg isVar(arg_index isVarFlags))} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		//FIXME join(${def.command.simpleName}.FACTORY$.${method.name}(<#foreach arg in method.args>${arg}<#if arg_has_next>, </#if></#foreach>));
		return this;
	}
</#if>
</#if>
</#macro>

<#foreach def in commandDefs>
	
	/* ------------------ ${def.commandName} ------------------ */

<#foreach method in def.methods>
<@methodSignatureForVars def method ""/>
</#foreach>
</#foreach>

	/**
	 * Simply returns this builder as it already reflects the extended support
	 * for variables.
	 *  
	 * @return this builder
	 */
	@Override
	public Unix4jCommandBuilder4Vars withVariables() {
		return this;
	}

}
