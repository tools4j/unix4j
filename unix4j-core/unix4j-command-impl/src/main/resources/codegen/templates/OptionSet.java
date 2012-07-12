<@pp.dropOutputFile /> 
<#list optionDefs as def> 
<#global cmdDef=def.command> 
<#global optDef=def.optionType> 
<#global setDef=def.optionSetType> 
<@pp.changeOutputFile name=pp.pathTo(setDef.packagePath+"/"+setDef.className+".java")/> 
package ${setDef.packageName};

import java.util.Set;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Collections;
import org.unix4j.optset.OptionSet;
import ${cmdDef.packageName}.${cmdDef.className};
import ${optDef.packageName}.${optDef.className};

/**
 * {@link ${optDef.className}} sets for the {@link ${cmdDef.className} ${cmdDef.name}} command.
 */
public enum ${setDef.className} implements OptionSet<${optDef.className}> {
	<#foreach set in def.optionSets>
	/** OptionSet with the following active options: [<#foreach opt in set.active>${opt}<#if opt_has_next>, </#if></#foreach>]*/
	${set.name}(<#foreach opt in def.options?keys><#if set.next[opt]??>${set.next[opt]}<#else>null/*already set*/</#if><#if opt_has_next || set.active?size != 0>, </#if></#foreach>
		/*active:*/<#foreach opt in set.active>${optDef.className}.${def.options[opt]}<#if opt_has_next>, </#if></#foreach>
	)<#if set_has_next>,<#else>;</#if>
	</#foreach>
	<#foreach opt in def.options?keys>
	/** Set with the current options plus the option "-${opt}" (aka "${def.options[opt]}"). This same set if "--${def.options[opt]}" is already set.*/
	public final ${setDef.className} ${opt};
	/** Set with the current options plus the option "--${def.options[opt]}" (aka "-${opt}"). This same set if "--${def.options[opt]}" is already set.*/
	public final ${setDef.className} ${def.options[opt]};
	</#foreach>
	private final Set<${optDef.className}> options;
	private ${setDef.className}(
		<#foreach opt in def.options?keys>${setDef.className} ${opt}, </#foreach>
		${optDef.className}... activeOptions
	) {
		<#foreach opt in def.options?keys>
		this.${opt} = ${opt} == null ? this : ${opt};
		this.${def.options[opt]} = ${opt};
		</#foreach>
		final EnumSet<${optDef.className}> set = activeOptions.length == 0 ? EnumSet.noneOf(${optDef.className}.class) : EnumSet.copyOf(Arrays.asList(activeOptions));
		this.options = Collections.unmodifiableSet(set);
	}
	@Override
	public boolean isSet(${optDef.className} option) {
		return options.contains(option);
	}
	@Override
	public Set<${optDef.className}> asSet() {
		return options;
	}
}
</#list>
