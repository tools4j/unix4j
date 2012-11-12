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

import java.util.Map;
import java.util.HashMap;

import org.unix4j.command.Arguments;
import org.unix4j.variable.TypedVariable;
import org.unix4j.variable.Variable;
<#if def.options?size != 0 || def.operands?size != 0>
import org.unix4j.variable.DefaultVariable;
</#if>
import org.unix4j.variable.VariableContext;
<#if def.options?size != 0>
import org.unix4j.option.DefaultOptionSet;
</#if>
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
	
	private enum OperandState {
		NotSet, SetAsValue, SetAsVariable;
		boolean isSet() {return this != NotSet;}
		boolean isVariable() {return this == SetAsVariable;}
	}
	
	<#if def.options?size != 0>
	private final ${optionsName} options;
	private final OperandState optionsOperandState;
	</#if>
	private final Map<String,Variable> operandToVariable = new HashMap<String, Variable>();
	<#foreach operand in def.operands?values>
	<#if !isOptions(operand) && operand.redirection?length == 0>
	
	// operand: <${operand.name}>
	private OperandState ${operand.name}OperandState = OperandState.NotSet;
	private ${normalizeVarArgType(operand.type, false)} ${operand.name};
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
		this.optionsOperandState = OperandState.SetAsValue;
	}
	
	/**
	 * Constructor with variable name for the options.
	 * 
	 * @param optionsVariable the variable that defines the selected options
	 * @throws NullPointerException if the argument is null
	 */
	public ${argumentsName}(Variable optionsVariable) {
		if (optionsVariable == null) {
			throw new NullPointerException("optionsVariable argument cannot be null");
		}
		this.options = null;
		this.operandToVariable.put("options", optionsVariable);
		this.optionsOperandState = OperandState.SetAsVariable;
	}
	
	/**
	 * Returns the options set containing the selected command options. Returns
	 * an empty options set if no option has been selected.
	 * 
	 * @return set with the selected options
	 * @throws IllegalStateException if options are defined as a variable which 
	 * 		has not been resolved through {@link #getForContext(VariableContext)}.
	 */
	public ${optionsName} getOptions() {
		if (optionsOperandState.isVariable()) {
			throw createUnresolvedOperandVariableException("options");
		}
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
	 * Returns an exception stating that the specified operand is defined as 
	 * unresolved variable.
	 * 
	 * @param operandName the name of the operand
	 * @return An IllegalStateException stating that the specified operand is 
	 * 		defined as unresolved variable.  
	 */
	private IllegalStateException createUnresolvedOperandVariableException(String operandName) {
		final Variable variable = operandToVariable.get(operandName);
		return new IllegalStateException("unresolved variable " + variable.getName() + " for the " + operandName + 
				" operand of the ${commandName} command [hint: it can be resolved through getForContext(..)]");
	}
	/**
	 * Returns an exception stating that the specified operand is defined as 
	 * typed variable and the conversion into the variable type failed. 
	 * 
	 * @param operandName the name of the operand
	 * @return An IllegalStateException stating that the specified operand is 
	 * 		defined as typed variable and the type conversion failed.  
	 */
	private IllegalStateException createOperandVariableConversionFailedException(String operandName, Class<?> operandType, Object value, Exception cause) {
		final Variable variable = operandToVariable.get(operandName);
		final String msg = "unsupported type conversion for variable " + variable.getName() + "=" + value + 
				" for " + operandName + "operand of the ${commandName} command, operand type=" + operandType.getSimpleName() + 
				" [hint: use typed variables with customized value converters]";
		return cause == null ? new IllegalStateException(msg) : new IllegalStateException(msg, cause);
	}
	private <V> V resolveVariable(VariableContext context, String operandName, Variable variable, Class<V> operandType) {
		final Object value;
		if (variable instanceof TypedVariable) {
			try {
				value = context.getValue(variable.getName(), ((TypedVariable<?>)variable).getValueConverter());
			} catch (IllegalArgumentException e) {
				final Object unconverted = context.getValue(variable.getName());
				throw createOperandVariableConversionFailedException(operandName, operandType, unconverted, e);
			}
		} else {
			value = context.getValue(variable.getName());
		}
		if (value == null) {
			throw createUnresolvedOperandVariableException(operandName);
		}
		if (operandType.isInstance(value)) {
			return operandType.cast(value);
		}
		return null;
	}
	
	</#if>
	
	@Override
	public ${argumentsName} getForContext(VariableContext context) {
		if (context == null) {
			throw new NullPointerException("variable context cannot be null");
		}
		if (operandToVariable.isEmpty()) {
			return this;
		}
		
		<#if def.options?size != 0>
		final ${argumentsName} argsForContext;
		if (optionsOperandState.isVariable()) {
			final Variable variable = operandToVariable.get("options");
			final ${optionsName} options = resolveVariable(context, "options", variable, ${optionsName}.class);
			argsForContext = new ${argumentsName}(options);
		} else {
			argsForContext = options != null ? new ${argumentsName}(options) : new ${argumentsName}(new DefaultVariable("options")); 
		}
		<#else>
		final ${argumentsName} argsForContext = new ${argumentsName}();
		</#if>
		
		<#foreach operand in def.operands?values>
		<#if !isOptions(operand) && operand.redirection?length == 0>
		if (${operand.name}OperandState.isSet()) {
			if (${operand.name}OperandState.isVariable()) {
				final Variable variable = operandToVariable.get("${operand.name}");
				<#if isGenericType(operand.type)>@SuppressWarnings("unchecked")</#if>
				final ${normalizeVarArgType(operand.type, true)} value = resolveVariable(context, "${operand.name}", variable, ${typeClass(operand.type, true)});
				if (value != null) {
					argsForContext.${setter(operand)}(value);//resolved now
				} else {
					argsForContext.${setter(operand)}(variable);//still unresolved, exception only when value is accessed
				}
			} else {
				argsForContext.${setter(operand)}(${operand.name});//a fixed value
			}
		}
		</#if>
		</#foreach>
		return argsForContext; 
	}
	
	<#foreach operand in def.operands?values>
	<#if !isOptions(operand) && operand.redirection?length == 0>
	/**
	 * Returns {@code <${operand.name}>}: ${operand.desc}
	 * 
	 * @return the {@code <${operand.name}>} operand value
	 * @throws IllegalStateException if this argument has never been set
	 */
	public ${normalizeVarArgType(operand.type, false)} ${getter(operand)}() {
		switch (${operand.name}OperandState) {
		case SetAsValue:
			return ${operand.name};
		case SetAsVariable:
			throw createUnresolvedOperandVariableException("${operand.name}");
		case NotSet:
			throw new IllegalStateException("argument has not been set: " + ${operand.name});
		default:
			//should never happen
			throw new IllegalStateException("unknown operand state " + ${operand.name}OperandState + " for operand " + ${operand.name});
		}
	}

	/**
	 * Returns true if the {@code <${operand.name}>} operand has been set. 
	 * <p>
	 * The method returns true if the {@code <${operand.name}>} operand has been 
	 * set to a fixed value or if it has been associated with a variable. In the
	 * latter case, the {@link #${getter(operand)}()} method throws an exception
	 * if the variable has not been resolved through 
	 * {@link #getForContext(VariableContext)}.
	 * <p>
	 * Note that this method returns true if {@link #${setter(operand)}(${normalizeVarArgType(rawType(operand.type), false)})}
	 * (also if null was passed to the method) or if the operand has been 
	 * associated with a variable through {@link #${setter(operand)}(Variable)}. 
	 * 
	 * @return	true if the setter for the {@code <${operand.name}>} operand has 
	 * 			been called at least once
	 */
	public boolean ${isset(operand)}() {
		return ${operand.name}OperandState.isSet();
	}
	/**
	 * Sets {@code <${operand.name}>}: ${operand.desc}
	 * 
	 * @param ${operand.name} the value for the {@code <${operand.name}>} operand
	 */
	public void ${setter(operand)}(${operand.type} ${operand.name}) {
		this.${operand.name} = ${operand.name};
		this.${operand.name}OperandState = OperandState.SetAsValue;
	}
	/**
	 * Associates {@code <${operand.name}>} with a variable: ${operand.desc}
	 * 
	 * @param ${operand.name}Variable the variable to be associated with the 
	 * 		{@code <${operand.name}>} operand
	 */
	public void ${setter(operand)}(Variable ${operand.name}Variable) {
		this.operandToVariable.put("${operand.name}", ${operand.name}Variable);
		this.${operand.name}OperandState = OperandState.SetAsVariable;
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
		
		<#if def.options?size != 0>
		// first the options
		if (optionsOperandState.isVariable()) {
			final Variable variable = operandToVariable.get("options");
			sb.append('{').append(variable.getName()).append('}');
		} else {
			if (options.size() > 0) {
				sb.append(DefaultOptionSet.toString(options));
			}
		}
		</#if>
		<#foreach operand in def.operands?values>
		<#if !isOptions(operand) && operand.redirection?length == 0>
		// operand: <${operand.name}>
		switch (${operand.name}OperandState) {
		case SetAsValue: {
			if (sb.length() > 0) sb.append(' ');
			sb.append("--").append("${operand.name}");
			sb.append(" ").append(toString(${getter(operand)}()));
			break;
		}
		case SetAsVariable: {
			final Variable variable = operandToVariable.get("options");
			if (sb.length() > 0) sb.append(' ');
			sb.append("--").append("${operand.name}");
			sb.append(" {").append(variable.getName()).append('}');
			break;
		}
		default:
			//not set, nothing to do
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
