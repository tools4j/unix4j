<#include "include/macros.fmpp">

<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=def.commandName>
<#global optionName=cmd.simpleName+"Option">
<#global optionsName=cmd.simpleName+"Options">
<#global argumentsName=cmd.simpleName+"Arguments">
<#global hasTrueOperand=def.operands?size != 0 && (def.operands?size != 1 || !isOptions(def.operands?values[0]))>
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+argumentsName+".java")/> 
package ${def.pkg.name};

import org.unix4j.command.Arguments;
import org.unix4j.option.DefaultOptionSet;
<#if hasTrueOperand>
import org.unix4j.util.ArrayUtil;
</#if>

import ${cmd.pkg.name}.${cmd.simpleName};

<#function getter operand>
	<#return "get" + operand.name?cap_first>
</#function>
<#function setter operand>
	<#return "set" + operand.name?cap_first>
</#function>
<#function isset operand>
	<#return "is" + operand.name?cap_first + "Set">
</#function>
<#function isOptionSet option>
	<#return "is" + option.name?cap_first>
</#function>
<#function isOptions operand>
	<#return operand.type == cmd.simpleName + "Options">
</#function>
/**
 * Arguments and options for the {@link ${cmd.simpleName} ${commandName}} command.
 */
public final class ${argumentsName} implements Arguments<${argumentsName}> {
	
	private final ${optionsName} options;
	<#foreach operand in def.operands?values>
	<#if !isOptions(operand)>
	
	// operand: <${operand.name}>
	private boolean ${operand.name}IsSet;
	private ${normalizeVarArgType(operand.type)} ${operand.name};
	</#if>
	</#foreach>
	
	/**
	 * Constructor to use if no options are specified.
	 */
	public ${argumentsName}() {
		this(${optionsName}.EMPTY);
	}

	/**
	 * Constructor with option set containing the selected command options.
	 * 
	 * @param options the selected options
	 */
	public ${argumentsName}(${optionsName} options) {
		this.options = options;
	}
	
	/**
	 * Returns the options set containing the selected command options.
	 * 
	 * @return set with the selected options
	 */
	public ${optionsName} getOptions() {
		return options;
	}
	
	<#foreach operand in def.operands?values>
	<#if !isOptions(operand)>
	/**
	 * Returns {@code <${operand.name}>}: ${operand.desc}
	 * 
	 * @return the {@code <${operand.name}>} operand value
	 */
	public ${normalizeVarArgType(operand.type)} ${getter(operand)}() {
		return ${operand.name};
	}
	/**
	 * Returns true if the {@code <${operand.name}>} operand has been set. 
	 * <p>
	 * Note that this method returns true if {@link #${setter(operand)}(${normalizeVarArgType(operand.type)})}
	 * has been called at least once even if {@link #${getter(operand)}()} returns
	 * {@code null}. 
	 * 
	 * @return	true if the setter for the {@code <${operand.name}>} operand has 
	 * 			been called at least once
	 */
	public boolean ${isset(operand)}() {
		return ${operand.name}IsSet;
	}
	/**
	 * Sets {@code <${operand.name}>}: ${operand.desc}
	 * 
	 * @param ${operand.name} the value for the {@code <${operand.name}>} operand
	 */
	public void ${setter(operand)}(${operand.type} ${operand.name}) {
		this.${operand.name} = ${operand.name};
		this.${operand.name}IsSet = true;
	}
	</#if>
	</#foreach>
	
	<#foreach opt in def.options?values>
	/**
	 * Returns true if the {@code --}{@link ${optionName}#${opt.name} ${opt.name}} option
	 * is set. The option is also known as {@code -}${opt.acronym} option.
	 * <p>
	 * Description: ${opt.desc}
	 * 
	 * @return true if the {@code --${opt.name}} or {@code -${opt.acronym}} option is set
	 */
	public boolean ${isOptionSet(opt)}() {
		return options.isSet(${optionName}.${opt.name});
	}
	</#foreach>

	@Override
	public String toString() {
		// check first whether there is any option or argument
		boolean isEmpty = options.size() == 0;
		<#foreach operand in def.operands?values>
		<#if !isOptions(operand)>
		isEmpty &= !${operand.name}IsSet;
		</#if>
		</#foreach>
		if (isEmpty) {
			return "";
		}

		// ok, we have options or arguments or both
		final StringBuilder sb = new StringBuilder();
		
		// first the options
		if (options.size() > 0) {
			sb.append(DefaultOptionSet.toString(options));
		}
		
		<#foreach operand in def.operands?values>
		<#if !isOptions(operand)>
		// operand: <${operand.name}>
		if (${operand.name}IsSet) {
			if (sb.length() > 0) sb.append(' ');
			sb.append("--").append("${operand.name}");
			sb.append(" ").append(toString(${getter(operand)}()));
		}
		</#if>
		</#foreach>
		return sb.toString();
	}
	<#if hasTrueOperand>
	private static String toString(Object value) {
		if (value != null && value.getClass().isArray()) {
			return ArrayUtil.toString(value);
		}
		return String.valueOf(value);
	}
	</#if>
}
</#list>
