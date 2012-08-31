<#include "include/macros.fmpp">

<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=def.commandName>
<#global optionsName=cmd.simpleName+"Options">
<#global varName=cmd.simpleName+"Var">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+varName+".java")/> 
package ${def.pkg.name};

import org.unix4j.command.CommandInterface;
import org.unix4j.variable.Literal;
import ${cmd.pkg.name}.${cmd.simpleName};

<#function varType operand>
	<#if operand.type?ends_with("...")>
		<#local name=operand.type?substring(0, operand.type?length-3)>
	<#elseif operand.type?ends_with("[]")>
		<#local name=operand.type?substring(0, operand.type?length-2)>
	<#else>
		<#local name=operand.type>
	</#if>
	<#local lastDot=name?last_index_of(".")>
	<#return name?substring(lastDot+1)?cap_first + "$">
</#function>
<#function isVar index isVarFlags>
	<#return isVarFlags?substring(index, index+1)=="T">
</#function>
<#function fixedOrVarType def arg isVar>
	<#if isVar>
		<#return varType(def.operands[arg])>
	<#else>
		<#return def.operands[arg].type>
	</#if>
</#function>
/**
 * Non-instantiable module with inner types defining variables applicable to the
 * <b>${commandName}</b> command methods; {@link Interface} defines the signature 
 * methods for the <b>${commandName}</b> to apply those variables.
 */
public class ${varName} {
<#foreach opd in def.operands?values>
	/**
	 * Literal for {@code <${opd.name}>} operand, for instance representing a 
	 * variable or named constant holding a value of the type {@code ${normalizeVarArgType(opd.type)}}.
	 */
	public interface ${varType(opd)} extends Literal<${normalizeVarArgType(opd.type)}>{}
</#foreach>
	
	/**
	 * Very similar to {@link ${cmd.simpleName}.Interface} but defines all method signatures for 
	 * the "${commandName}" command when variables are used in form of a
	 * {@link Literal}.
	 * 
	 * @param <R>
	 *            the generic return type for all command signature methods
	 *            to support different implementor types; the command
	 *            {@link ${cmd.simpleName}#FACTORY FACTORY} for instance returns a
	 *            new command instance; command builders can also implement this
	 *            interface and return an instance to itself allowing for
	 *            chained method invocations to create joined commands.
	 */
	public interface Interface<R> extends CommandInterface<R> {
<#macro methodSignatureForVars def method isVarFlags>
<#if method.args?size != isVarFlags?length>
<@methodSignatureForVars def method isVarFlags+"T"/>
<@methodSignatureForVars def method isVarFlags+"F"/>
<#else>
<#if isVarFlags?contains("T")><#--at least one variable, otherwise it is the same as the standard method without variables -->
		/**
		 * ${method.desc}
		 *
		<#foreach arg in method.args>
		 * @param ${arg} ${def.operands[arg].desc}
		</#foreach>
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to the standard output. This supports different
		 *         implementor types, like the command {@link ${cmd.simpleName}#FACTORY FACTORY} 
		 *         which returns a new command instance. Command builders can 
		 *         also implement this interface and return an instance to 
		 *         itself allowing for chained method invocations to create 
		 *         joined commands.
		 */
		R ${method.name}$(<#foreach arg in method.args>${fixedOrVarType(def arg isVar(arg_index isVarFlags))} ${arg}<#if arg_has_next>, </#if></#foreach>);
</#if>
</#if>
</#macro>
<#foreach method in def.methods>
<@methodSignatureForVars def method ""/>
</#foreach>
	}
}
</#list>
