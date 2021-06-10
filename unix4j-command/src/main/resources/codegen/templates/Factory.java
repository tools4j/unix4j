<#include "/include/macros.fmpp">
<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=cmd.simpleName + "Command">
<#global optionsName=cmd.simpleName+"Options">
<#global argumentsName=cmd.simpleName+"Arguments">
<#global simpleName=cmd.simpleName+"Factory">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+simpleName+".java")/> 
package ${def.pkg.name};

import ${cmd.pkg.name}.${cmd.simpleName};

/**
 * Factory for the {@link ${cmd.simpleName} ${def.commandName}} command returning 
 * a new command instance from every signature method.
 */
public final class ${simpleName} implements ${cmd.simpleName}.Interface<${commandName}> {
	
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
<#foreach method in def.methods>

	@Override
	public ${commandName} ${method.name}(<#foreach arg in method.args>${def.operands[arg].type} ${arg}<#if arg_has_next>, </#if></#foreach>) {
		<#if method.args?size==1 && isArgsOperandName(method.args[0])>
		final ${argumentsName} ${def.commandName}Args = new ${argumentsName}(${method.args[0]});
		<#else>
		final ${argumentsName} ${def.commandName}Args = new ${argumentsName}(${getOptionsArgIfAny(def, method.args)});
		<#foreach arg in method.args>
		<#if !isOptionsArg(def, arg)>
		<#global operand = def.operands[arg]>
		<#if operand.redirection?length == 0>
		${def.commandName}Args.${setter(operand)}(${arg});
		<#else>
		${def.commandName}Args.${operand.redirection?replace(r"${value}",arg)};
		</#if>
		</#if>
		</#foreach>
		</#if>
		return new ${commandName}(${def.commandName}Args);
	}
</#foreach>
}
</#list>
