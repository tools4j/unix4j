<#include "/include/macros.fmpp">

<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=def.commandName>
<#global optionsName=cmd.simpleName+"Options">
<#global varName=cmd.simpleName+"Var">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+varName+".java")/> 
package ${def.pkg.name};

import org.unix4j.command.CommandInterface;
import org.unix4j.variable.NamedValue;
<#foreach opd in def.operands?values>
	<#if !isCommandSpecificOperand(def, opd)>
		<#if indexOfOperandByType(def.operands, opd) == opd_index><#-- otherwise it is a repeted occurance of this type -->
import ${varPkgName}.${varIfaceName(opd)};
		</#if>
	</#if>
</#foreach>
import ${cmd.pkg.name}.${cmd.simpleName};

/**
 * Non-instantiable module with inner types defining variables applicable to the
 * <b>${commandName}</b> command methods; {@link Interface} defines the signature 
 * methods for the <b>${commandName}</b> to apply those variables.
 */
public class ${varName} {
<#foreach opd in def.operands?values>
<#if isCommandSpecificOperand(def, opd)>
<#if indexOfOperandByType(def.operands, opd) == opd_index><#-- otherwise it is a repeted occurance of this type -->
	/**
	 * Interface for a variable or a constant holding a value of the type {@code ${normalizeVarArgType(opd.type, false)}}.
	 * Such variables can for instance be passed to the {@code <${opd.name}>} 
	 * operand.
	 */
	public interface ${varIfaceName(opd)} extends NamedValue<${normalizeVarArgType(opd.type, true)}>{}
</#if>
</#if>
</#foreach>
	
	/**
	 * Very similar to {@link ${cmd.simpleName}.Interface} but defines all method signatures for 
	 * the "${commandName}" command when variables are used in form of a
	 * {@link NamedValue}.
	 * 
<#include "/include/returntype-class-javadoc.java">
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
<#include "/include/returntype-method-javadoc.java">
		 */
		R ${method.name}(<#foreach arg in method.args>${fixedOrVarType(def arg isVar(arg_index isVarFlags))} ${arg}<#if arg_has_next>, </#if></#foreach>);
</#if>
</#if>
</#macro>
<#foreach method in def.methods>
<@methodSignatureForVars def method ""/>
</#foreach>
	}
}
</#list>
