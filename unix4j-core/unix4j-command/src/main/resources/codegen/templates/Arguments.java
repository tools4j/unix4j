<#include "/include/macros.fmpp">

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

import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.unix4j.command.Arguments;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.VariableContext;
import org.unix4j.convert.ValueConverter;
<#if def.options?size != 0>
import org.unix4j.option.DefaultOptionSet;
</#if>
import org.unix4j.util.ArgsUtil;
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
	
	<#if def.options?size != 0>
	private final ${optionsName} options;
	</#if>

	<#if def.options?size != 0 || def.operands?size != 0>
	// string arguments encoding options and operands
	private String[] args;
	private boolean argsIsSet = false;
	</#if>
	<#foreach operand in def.operands?values>
	<#if !isOptions(operand) && operand.redirection?length == 0>
	
	// operand: <${operand.name}>
	private ${normalizeVarArgType(operand.type, false)} ${operand.name};
	private boolean ${operand.name}IsSet = false;
	</#if>
	</#foreach>
	
	<#if def.options?size != 0>
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
	 * @throws NullPointerException if the argument is null
	 */
	public ${argumentsName}(${optionsName} options) {
		if (options == null) {
			throw new NullPointerException("options argument cannot be null");
		}
		this.options = options;
	}
	
	/**
	 * Returns the options set containing the selected command options. Returns
	 * an empty options set if no option has been selected.
	 * 
	 * @return set with the selected options
	 */
	public ${optionsName} getOptions() {
		return options;
	}
	<#else>
	/**
	 * Default constructor, no argument values are set initially.
	 */
	public ${argumentsName}() {
		super();
	}
	</#if>
	<#if def.options?size != 0 || def.operands?size != 0>

	/**
	 * Constructor string arguments encoding options and arguments, possibly
	 * also containing variable expressions. 
	 * 
	 * @param args string arguments for the command
	 * @throws NullPointerException if args is null
	 */
	public ${argumentsName}(String... args) {
		this();
		if (args == null) {
			throw new NullPointerException("optionsVariable argument cannot be null");
		}
		this.args = args;
		this.argsIsSet = true;
	}

	private String resolveVariable(VariableContext context, String variable) {
		final Object value = context.getValue(variable);
		return value == null ? "" : value.toString();
	}
	private String[] resolveVariables(VariableContext context, String... unresolved) {
		final String[] resolved = new String[unresolved.length];
		for (int i = 0; i < resolved.length; i++) {
			final String expression = unresolved[i];
			if (expression.startsWith("$")) {
				resolved[i] = resolveVariable(context, expression);
			} else {
				resolved[i] = expression;
			}
		}
		return resolved;
	}
	private <V> V convert(ExecutionContext context, String operandName, Class<V> operandType, Object value) {
		if (operandType.isInstance(value)) {
			return operandType.cast(value);
		}
		final ValueConverter<V> converter = context.getValueConverterFor(operandType);
		if (converter != null) {
			return converter.convert(value);
		}
		throw new IllegalArgumentException("cannot convert --" + operandName + 
				" value '" + value + "' into the type " + operandType.getName() + 
				" for ${commandName} command");
	}
		
	private <V> V convertList(ExecutionContext context, String operandName, Class<V> operandType, List<String> values) {
		if (values.size() == 1) {
			final String value = values.get(0);
			return convert(context, operandName, operandType, value);
		}
		return convert(context, operandName, operandType, values);
	}
	
	</#if>
	
	@Override
	public ${argumentsName} getForContext(ExecutionContext context) {
		if (context == null) {
			throw new NullPointerException("context cannot be null");
		}
		if (!argsIsSet) {
			return this;
		}
		//check if there is at least one variable
		boolean hasVariable = false;
		for (final String arg : args) {
			if (arg != null && arg.startsWith("$")) {
				hasVariable = true;
				break;
			}
		}
		
		//resolve variables
		final String[] resolvedArgs = hasVariable ? resolveVariables(context.getVariableContext(), this.args) : this.args;
		
		//convert now
		final Map<String, List<String>> map = ArgsUtil.parseArgs(resolvedArgs);
		<#if def.options?size != 0>
		final ${optionsName}.Default options = new ${optionsName}.Default();
		final ${argumentsName} argsForContext = new ${argumentsName}(options);
		<#else>
		final ${argumentsName} argsForContext = new ${argumentsName}();
		</#if>
		for (final Map.Entry<String, List<String>> e : map.entrySet()) {
			<#foreach operand in def.operands?values>
			<#if operand_index != 0>} else </#if>if ("${operand.name}".equals(e.getKey())) {
				<#if isGenericType(operand.type)>@SuppressWarnings("unchecked")</#if>
				final ${normalizeVarArgType(operand.type, false)} value = convertList(context, "${operand.name}", ${typeClass(operand.type, true)}, e.getValue());  
			<#if isOptions(operand)>
				options.setAll(value);
			<#elseif operand.redirection?length == 0>
				argsForContext.${setter(operand)}(value);
			<#else>
				argsForContext.${operand.redirection?replace(r"${value}","value")};
			</#if>
			</#foreach>
			} else {
				throw new IllegalStateException("invalid operand '" + e.getKey() + "' in ${commandName} command args: " + Arrays.toString(args));
			}
		}
		return argsForContext; 
	}
	
	<#foreach operand in def.operands?values>
	<#if !isOptions(operand) && operand.redirection?length == 0>
	/**
	 * Returns {@code <${operand.name}>}: ${operand.desc}
	 * 
	 * @return the {@code <${operand.name}>} operand value
	 * @throws IllegalStateException if this operand has never been set
	 */
	public ${normalizeVarArgType(operand.type, false)} ${getter(operand)}() {
		if (${operand.name}IsSet) {
			return ${operand.name};
		}
		throw new IllegalStateException("operand has not been set: " + ${operand.name});
	}

	/**
	 * Returns true if the {@code <${operand.name}>} operand has been set. 
	 * <p>
	 * Note that this method returns true if {@link #${setter(operand)}(${normalizeVarArgType(rawType(operand.type), false)})}
	 * also if null was passed to the method. 
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
		return getOptions().isSet(${optionName}.${opt.name});
	}
	</#foreach>

	@Override
	public String toString() {
		// ok, we have options or arguments or both
		final StringBuilder sb = new StringBuilder();

		<#if def.options?size != 0 || def.operands?size != 0>
		if (argsIsSet) {
			for (String arg : args) {
				sb.append(arg);
			}
		} else {
			<#if def.options?size != 0>
			// first the options
			if (options.size() > 0) {
				sb.append(DefaultOptionSet.toString(options));
			}
			</#if>
			<#foreach operand in def.operands?values>
			<#if !isOptions(operand) && operand.redirection?length == 0>
			// operand: <${operand.name}>
			if (${operand.name}IsSet) {
				if (sb.length() > 0) sb.append(' ');
				sb.append("--").append("${operand.name}");
				sb.append(" ").append(toString(${getter(operand)}()));
			}
			</#if>
			</#foreach>
		}
		</#if>
		
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
