<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=cmd.simpleName + "Command">
<#global optionsName=cmd.simpleName+"Options">
<#global argumentsName=cmd.simpleName+"Arguments">
<#global varName=cmd.simpleName+"Var">
<#global simpleName=cmd.simpleName+"Factory">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+simpleName+".java")/> 
package ${def.pkg.name};

<#function setter operand>
	<#return "set" + operand.name?cap_first>
</#function>
<#function getOptionsArgIfAny def args>
	<#foreach arg in args>
		<#if isOptionsArg(def, arg)>
			<#return arg>
		</#if>
	</#foreach>
	<#return "">
</#function>
<#function hasOptionsArg def args>
	<#return getOptionsArgIfAny(def, args)?length != 0>
</#function>
<#function isOptionsArg def arg>
	<#return def.operands[arg].type == optionsName>
</#function>

import ${cmd.pkg.name}.${cmd.simpleName};

/**
 * Factory for the {@link ${cmd.simpleName} ${def.commandName}} returning a new 
 * command instance from every signature method.
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
		final ${argumentsName} args = new ${argumentsName}(${getOptionsArgIfAny(def, method.args)});
		<#foreach arg in method.args>
		<#if !isOptionsArg(def, arg)>
		<#global operand = def.operands[arg]>
		<#if operand.redirection?length == 0>
		args.${setter(operand)}(${arg});
		<#else>
		args.${operand.redirection};
		</#if>
		</#if>
		</#foreach>
		return new ${commandName}(args);
	}
</#foreach>
}
</#list>
