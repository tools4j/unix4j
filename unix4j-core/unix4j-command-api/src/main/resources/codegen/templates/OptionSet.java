<@pp.dropOutputFile /> 
<#list optionDefs as def> 
<#global pkgName=def.packagePath> 
<@pp.changeOutputFile name=pp.pathTo(def.packagePath+"/"+def.optionSetClassName+".java")/> 
package ${def.packageName};

import java.util.Set;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Collections;
import org.unix4j.optset.OptionSet;
import ${def.command.packageName}.${def.command.className};
import ${def.packageName}.${def.optionClassName};

/**
 * {@link ${def.optionClassName}} sets for the {@link ${def.command.className} ${def.command.name}} command.
 */
public enum ${def.optionSetClassName} implements OptionSet<${def.optionClassName}> {
	<#foreach set in def.optionSets>
	/** OptionSet with the following active options: [<#foreach opt in set.active>${opt}<#if opt_has_next>, </#if></#foreach>]*/
	${set.name}(<#foreach opt in def.options?keys><#if set.next[opt]??>${set.next[opt]}<#else>null/*already set*/</#if><#if opt_has_next || set.active?size != 0>, </#if></#foreach>
		/*active:*/<#foreach opt in set.active>${def.optionClassName}.${def.options[opt]}<#if opt_has_next>, </#if></#foreach>
	)<#if set_has_next>,<#else>;</#if>
	</#foreach>
	<#foreach opt in def.options?keys>
	/** OptionSet with the current options plus the option ${opt} (same set if option is already set)*/
	public final ${def.optionSetClassName} ${opt};
	</#foreach>
	private final Set<${def.optionClassName}> options;
	private ${def.optionSetClassName}(
		<#foreach opt in def.options?keys>${def.optionSetClassName} ${opt}, </#foreach>
		${def.optionClassName}... activeOptions
	) {
		<#foreach opt in def.options?keys>
		this.${opt} = ${opt} == null ? this : ${opt};
		</#foreach>
		final EnumSet<${def.optionClassName}> set = activeOptions.length == 0 ? EnumSet.noneOf(${def.optionClassName}.class) : EnumSet.copyOf(Arrays.asList(activeOptions));
		this.options = Collections.unmodifiableSet(set);
	}
	@Override
	public boolean isSet(${def.optionClassName} option) {
		return options.contains(option);
	}
	@Override
	public Set<${def.optionClassName}> asSet() {
		return options;
	}
}
</#list>
