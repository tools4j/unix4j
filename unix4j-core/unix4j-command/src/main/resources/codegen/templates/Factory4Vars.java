<#include "/include/macros.fmpp">
<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=cmd.simpleName + "Command">
<#global optionsName=cmd.simpleName+"Options">
<#global argumentsName=cmd.simpleName+"Arguments">
<#global factoryName=cmd.simpleName+"Factory">
<#global simpleName=factoryName+"4Vars">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+simpleName+".java")/> 
package ${def.pkg.name};

import ${varPkgName}.*;

import org.unix4j.variable.NamedValue;
import ${cmd.pkg.name}.${cmd.simpleName};
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

/**
 * Factory for the {@link ${cmd.simpleName} ${def.commandName}} command returning 
 * a new command instance from every signature method --- very similar to
 * {@link ${factoryName}} but for construction of a command when variables are 
 * used in form of a {@link NamedValue}.
 */
public final class ${simpleName} implements ${cmd.simpleName}4Vars.Interface<${commandName}> {
	
	/**
	 * The singleton instance of this factory.
	 */
	public static final ${simpleName} INSTANCE = new ${simpleName}();

	/**
	 * Private, only used to create singleton instance.
	 */
	private ${simpleName}() {
		super();
	}
<#macro methodSignatureForVars def method isVarFlags>
<#if method.args?size != isVarFlags?length>
<@methodSignatureForVars def method isVarFlags+"T"/>
<@methodSignatureForVars def method isVarFlags+"F"/>
<#else>
<#if isVarFlags?contains("T")><#--at least one variable, otherwise it is the same as the standard method without variables -->

	@Override
	public ${commandName} ${method.name}(<#foreach arg in method.args>${fixedOrVarType(def arg isVar(arg_index isVarFlags))} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		final ${argumentsName} args = new ${argumentsName}(${getOptionsArgIfAny(def, method.args)});
		<#foreach arg in method.args>
		<#if !isOptionsArg(def, arg)>
		<#global operand = def.operands[arg]>
		<#if operand.redirection?length == 0>
		args.${setter(operand)}(${arg});
		<#else>
		args.${operand.redirection?replace(r"${value}",arg+".getValue()")};
		</#if>
		</#if>
		</#foreach>
		return new ${commandName}(args);
	}
</#if>
</#if>
</#macro>

<#foreach method in def.methods>
<@methodSignatureForVars def method ""/>
</#foreach>
}
</#list>
