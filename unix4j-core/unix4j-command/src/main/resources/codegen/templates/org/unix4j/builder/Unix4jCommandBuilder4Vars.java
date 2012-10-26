<#include "/include/macros.fmpp">
<#include "/include/builder-method-javadoc.fmpp">
package org.unix4j.builder;

import ${varPkgName}.*;

<#foreach def in commandDefs> 
import ${def.command.pkg.name}.${def.command.simpleName};
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
 * Extension of {@link Unix4jCommandBuilder} adding methods for commands when
 * variables are used as command arguments. Application code does usually not 
 * directly refer to this class but uses it indirectly through the static 
 * methods in {@link org.unix4j.Unix4j Unix4j}.
 */
public interface Unix4jCommandBuilder4Vars extends Unix4jCommandBuilder,
<#foreach def in commandDefs>
		${def.command.simpleName}.Interface4Vars<Unix4jCommandBuilder><#if def_has_next>,<#else> {</#if>
</#foreach>

<#macro methodSignatureForVars def method isVarFlags>
<#if method.args?size != isVarFlags?length>
<@methodSignatureForVars def method isVarFlags+"T"/>
<@methodSignatureForVars def method isVarFlags+"F"/>
<#else>
<#if isVarFlags?contains("T")><#--at least one variable, otherwise it is the same as the standard method without variables -->
<@builderMethodJavadoc def method/>
	@Override
	Unix4jCommandBuilder ${method.name}(<#foreach arg in method.args>${fixedOrVarType(def arg isVar(arg_index isVarFlags))} ${arg}<#if arg_has_next>, </#if></#foreach>);
</#if>
</#if>
</#macro>

<#foreach def in commandDefs>
	
	/* ------------------ ${def.commandName} ------------------ */

<#foreach method in def.methods>
<@methodSignatureForVars def method ""/>
</#foreach>
</#foreach>
}
