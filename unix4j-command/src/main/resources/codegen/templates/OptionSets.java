<#include "/include/macros.fmpp">
<#include "/include/option-javadoc.fmpp">

<@pp.dropOutputFile />
<#list optionSetDefs as setDef>
<#global def=setDef.command> 
<#if def.options?size != 0> 
<#global cmd=def.command>
<#global commandName=def.commandName> 
<#global options=def.options?values> 
<#global simpleName=cmd.simpleName+"OptionSets">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+simpleName+".java")/> 
package ${def.pkg.name};

import ${cmd.pkg.name}.${cmd.simpleName};

/**
 * Options for the {@link ${cmd.simpleName} ${commandName}} command with the 
 * the following options: 
 * <p>
<#include "/include/options-javadoc.java">
 * <p>
 * This class serves as entry point to every possible set of {@code ${commandName}} options
 * defined as an enum constant. With this explicit expansion of all possible 
 * option combinations, options can be passed to the command in a very compact 
 * form, such as:
 * <pre>
<#if options?size != 0> * ${commandName}(${cmd.simpleName}.Options.${options[0].acronym}, ...);
<#if options?size != 1> * ${commandName}(${cmd.simpleName}.Options.${options[0].acronym}.${options[1].acronym}, ...);
<#if options?size != 2> * ...
<#if options?size != 2> * ${commandName}(${cmd.simpleName}.Options<#foreach o in options>.${o.acronym}</#foreach>, ...);
</#if></#if></#if></#if>
 * </pre>
 */
public final class ${simpleName} {
	/**
	 * The singleton instance.
	 */
	public static final ${simpleName} INSTANCE = new ${simpleName}();
	
	<#global grp = setDef.initialGroup>
	<#foreach opt in grp.options?values>
	<#global optType = grp.optionToNextGroup[opt.name].groupType>
	<#global activeSetName = "Active_" + opt.acronym>
	<@optionJavadocAcronym opt false/>
	public final ${optType.simpleName} ${opt.acronym} = ${optType.simpleName}.${activeSetName};  
	<@optionJavadocLong opt false/>
	public final ${optType.simpleName} ${opt.name} = ${optType.simpleName}.${activeSetName}_long;  
	</#foreach>
	
}
</#if>
</#list>