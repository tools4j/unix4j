<@pp.dropOutputFile /> 
<#list optionDefs as def> 
<#global cmdDef=def.command> 
<#global optDef=def.optionType> 
<#global setDef=def.optionSetType> 
<@pp.changeOutputFile name=pp.pathTo(setDef.pkg.path+"/"+setDef.simpleName+".java")/> 
package ${setDef.pkg.name};

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

import ${cmdDef.pkg.name}.${cmdDef.simpleName};
import ${optDef.pkg.name}.${optDef.simpleName};

/**
 * Option sets for the {@link ${cmdDef.simpleName} ${cmdDef.name}} command with 
 * the following options: <#foreach opt in def.options?keys>{@link #${opt} ${opt}}<#if opt_has_next>, </#if></#foreach>.
 * Note that each option has also a long name: <#foreach opt in def.options?keys>{@code ${opt}:}{@link #${def.options[opt]} ${def.options[opt]}}<#if opt_has_next>, </#if></#foreach>.
 * <p>
 * Every possible {@code ${cmdDef.name}} option permutation is reflected by one
 * of the {@code ${setDef.simpleName}} constants. With this explicit expansion
 * of all possible option combinations, options can be passed to the command in
 * a very compact form, such as:
 * <pre>
<#if def.options?keys?size != 0> * ${cmdDef.name}(${setDef.simpleName}.${def.options?keys[0]}, ...);
<#if def.options?keys?size != 1> * ${cmdDef.name}(${setDef.simpleName}.${def.options?keys[0]}.${def.options?keys[1]}, ...);
<#if def.options?keys?size != 2> * ...
<#if def.options?keys?size != 2> * ${cmdDef.name}(${setDef.simpleName}<#foreach o in def.options?keys>.${o}</#foreach>, ...);
</#if></#if></#if></#if>
 * </pre>
 */
public enum ${setDef.simpleName} implements OptionSet<${optDef.simpleName}> {
	<#foreach set in def.optionSets>
	/** <#if set.active?size==0>Empty option set without active options<#else>Option set with the following active options: <#foreach opt in set.active>{@link #${opt} ${opt}}<#if opt_has_next>, </#if></#foreach></#if>.*/
	${set.name}(<#foreach opt in def.options?keys><#if set.next[opt]??>${set.next[opt]}<#else>null/*already set*/</#if><#if opt_has_next || set.active?size != 0>, </#if></#foreach>
		/*active:*/<#foreach opt in set.active>${optDef.simpleName}.${def.options[opt]}<#if opt_has_next>, </#if></#foreach>
	)<#if set_has_next>,<#else>;</#if>
	</#foreach>
	private ${setDef.simpleName}(
		<#foreach opt in def.options?keys>${setDef.simpleName} ${opt}, </#foreach>
		/*TODO boolean useAcronym,*/
		${optDef.simpleName}... activeOptions
	) {
		<#foreach opt in def.options?keys>
		this.${opt} = ${opt} == null ? this : ${opt};
		this.${def.options[opt]} = this.${opt};
		</#foreach>
		this.useAcronym = false;//TODO useAcronym;
		final EnumSet<${optDef.simpleName}> set = activeOptions.length == 0 ? EnumSet.noneOf(${optDef.simpleName}.class) : EnumSet.copyOf(Arrays.asList(activeOptions));
		this.options = Collections.unmodifiableSet(set);
	}
	private final boolean useAcronym;
	<#foreach opt in def.options?keys>
	<@optionJavadoc opt def.options[opt] def.javadoc[opt]/>
	public final ${setDef.simpleName} ${opt};
	<@optionJavadoc def.options[opt] opt def.javadoc[opt]/>
	public final ${setDef.simpleName} ${def.options[opt]};
	</#foreach>
	private final Set<${optDef.simpleName}> options;
	//inherit javadoc
	@Override
	public boolean isSet(${optDef.simpleName} option) {
		return options.contains(option);
	}
	/**
	 * Returns an unmodifiable set with the active options.
	 * @return an unmodifiable set with the active options.
	 */
	@Override
	public Set<${optDef.simpleName}> asSet() {
		return options;
	}
	/**
	 * Returns true if the last option chosen TODO
	 * Returns true if the string representation of this option set should use
	 * option {@link Option#acronym() acronyms} instead of the long option
	 * {@link Option#name() names}.
	 * 
	 * @return true if option acronyms should be used for string representations
	 *         of this option set
	 */
	public boolean useAcronym() {
		return useAcronym;
	}
}
</#list>
