<@pp.dropOutputFile /> 
<#list optionDefs as def> 
<#global cmdDef=def.command> 
<#global optDef=def.optionType> 
<#global setDef=def.optionSetType> 
<@pp.changeOutputFile name=pp.pathTo(setDef.packagePath+"/"+setDef.className+".java")/> 
package ${setDef.packageName};

<#macro optionJavadoc opt alias javadoc> 
	/**
	 * Option {@code "-${opt}"}: ${javadoc}
	 * <p>
	 * The option {@code "-${opt}"} is equivalent to the {@code "--}{@link #${alias} ${alias}}{@code "} option.
	 * <p>
	 * Technically speaking, this field points to a set with the options of the 
	 * current set plus the option {@code "-${opt}"}. If the option {@code "-${opt}"}
	 * is already set, the field {@code ${opt}} points to the enum constant itself
	 * as it already represents the current set of options. 
	 */
</#macro>

import java.util.Set;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Collections;
import org.unix4j.optset.OptionSet;
import ${cmdDef.packageName}.${cmdDef.className};
import ${optDef.packageName}.${optDef.className};

/**
 * Option sets for the {@link ${cmdDef.className} ${cmdDef.name}} command with 
 * the following options: <#foreach opt in def.options?keys>{@link #${opt} ${opt}}<#if opt_has_next>, </#if></#foreach>.
 * Note that each option has also a long name: <#foreach opt in def.options?keys>{@code ${opt}:}{@link #${def.options[opt]} ${def.options[opt]}}<#if opt_has_next>, </#if></#foreach>.
 */
public enum ${setDef.className} implements OptionSet<${optDef.className}> {
	<#foreach set in def.optionSets>
	/** OptionSet with the following active options: <#foreach opt in set.active>{@link #${opt} ${opt}}<#if opt_has_next>, </#if></#foreach>*/
	${set.name}(<#foreach opt in def.options?keys><#if set.next[opt]??>${set.next[opt]}<#else>null/*already set*/</#if><#if opt_has_next || set.active?size != 0>, </#if></#foreach>
		/*active:*/<#foreach opt in set.active>${optDef.className}.${def.options[opt]}<#if opt_has_next>, </#if></#foreach>
	)<#if set_has_next>,<#else>;</#if>
	</#foreach>
	private ${setDef.className}(
		<#foreach opt in def.options?keys>${setDef.className} ${opt}, </#foreach>
		${optDef.className}... activeOptions
	) {
		<#foreach opt in def.options?keys>
		this.${opt} = ${opt} == null ? this : ${opt};
		this.${def.options[opt]} = this.${opt};
		</#foreach>
		final EnumSet<${optDef.className}> set = activeOptions.length == 0 ? EnumSet.noneOf(${optDef.className}.class) : EnumSet.copyOf(Arrays.asList(activeOptions));
		this.options = Collections.unmodifiableSet(set);
	}
	<#foreach opt in def.options?keys>
	<@optionJavadoc opt def.options[opt] def.javadoc[opt]/>
	public final ${setDef.className} ${opt};
	<@optionJavadoc def.options[opt] opt def.javadoc[opt]/>
	public final ${setDef.className} ${def.options[opt]};
	</#foreach>
	private final Set<${optDef.className}> options;
	//inherit javadoc
	@Override
	public boolean isSet(${optDef.className} option) {
		return options.contains(option);
	}
	/**
	 * Returns an unmodifiable set with the active options.
	 * @return an unmodifiable set with the active options.
	 */
	@Override
	public Set<${optDef.className}> asSet() {
		return options;
	}
}
</#list>
