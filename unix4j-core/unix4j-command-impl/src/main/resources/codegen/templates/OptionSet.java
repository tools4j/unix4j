<@pp.dropOutputFile /> 
<#list optionDefs as def> 
<#global cmdDef=def.command> 
<#global optDef=def.optionType> 
<#global setDef=def.optionSetType> 
<@pp.changeOutputFile name=pp.pathTo(setDef.pkg.path+"/"+setDef.simpleName+".java")/> 
package ${setDef.pkg.name};

<#macro optionJavadoc myName aliasName myPre aliasPre javadoc> 
	/**
	 * Option {@code "${myPre}${myName}"}: ${javadoc}
	 * <p>
	 * The option {@code "${myPre}${myName}"} is equivalent to the {@code "${aliasPre}}{@link #${aliasName} ${aliasName}}{@code "} option.
	 * <p>
	 * Technically speaking, this field points to a set with the options of the 
	 * current set plus the option {@code "${myPre}${myName}"}. If the option {@code "${myPre}${myName}"}
	 * is already set, the field {@code ${myName}} points to the enum constant itself
	 * as it already represents the current set of options. 
	 */
</#macro>
<#macro optionJavadocLong acronym optionName javadoc> 
	<@optionJavadoc optionName acronym "--" "-" javadoc/>
</#macro>
<#macro optionJavadocAcronym acronym optionName javadoc> 
	<@optionJavadoc acronym optionName "-" "--" javadoc/>
</#macro>
<#macro setName name useAcronym><#if 
	useAcronym>${name}<#else>${name}_long</#if
></#macro>

import java.util.EnumSet;
import java.util.Arrays;
import org.unix4j.optset.Option;
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
	<#foreach useAcronym in [true,false]>
	<#if useAcronym || set.active?size != 0><#-- no long version for empty set -->
	/** <#if set.active?size==0>Empty option set without active options<#else>Option set with the following active options: <#foreach opt in set.active>{@link #${opt} ${opt}}<#if opt_has_next>, </#if></#foreach></#if>.*/
	<@setName set.name useAcronym/>(
		<#foreach opt in def.options?keys><#if set.next[opt]??><@setName set.next[opt] true/>, <@setName set.next[opt] false/><#else>null/*already set*/, null/*already set*/</#if>, </#foreach>
		${useAcronym?string}<#if set.active?size != 0>, </#if>
		/*active:*/<#foreach opt in set.active>${optDef.simpleName}.${def.options[opt]}<#if opt_has_next>, </#if></#foreach>
	)<#if set.active?size != 0>,<#else>;</#if>
	</#if>
	</#foreach>
	</#foreach>
	private ${setDef.simpleName}(
		<#foreach opt in def.options?keys>${setDef.simpleName} ${opt}, ${setDef.simpleName} ${opt}_long, </#foreach>
		boolean useAcronym,
		${optDef.simpleName}... activeOptions
	) {
		<#foreach opt in def.options?keys>
		this.${opt} = ${opt} == null ? this : ${opt};
		this.${def.options[opt]} = ${opt}_long == null ? this : ${opt}_long;
		</#foreach>
		this.useAcronym = useAcronym;
		this.options = activeOptions.length == 0 ? EnumSet.noneOf(${optDef.simpleName}.class) : EnumSet.copyOf(Arrays.asList(activeOptions));
	}
	private final boolean useAcronym;
	<#foreach opt in def.options?keys>
	<@optionJavadocAcronym opt def.options[opt] def.javadoc[opt]/>
	public final ${setDef.simpleName} ${opt};
	<@optionJavadocLong opt def.options[opt] def.javadoc[opt]/>
	public final ${setDef.simpleName} ${def.options[opt]};
	</#foreach>
	private final EnumSet<${optDef.simpleName}> options;
	//inherit javadoc
	@Override
	public boolean isSet(${optDef.simpleName} option) {
		return options.contains(option);
	}
	/**
	 * Returns the set with the active options. The returned set a new defensive
	 * copy instance created when this method is called, modifications of this
	 * set will therefore not alter {@code this} option set.
	 * 
	 * @return a copy of the set with the active options.
	 */
	@Override
	public EnumSet<${optDef.simpleName}> asSet() {
		return EnumSet.copyOf(options);
	}
	/**
	 * Returns true if the string representation of this option set should use
	 * option {@link Option#acronym() acronyms} instead of the long option
	 * {@link Option#name() names}.
<#if def.options?keys?size != 0>
	 * <p>
	 * In particular, this option set returns true if the last option added to 
	 * this set was an acronym, and false if it was a long option name. 
	 * <p>
	 * For instance, the set defined as
	 * <pre>
	 * <#if def.options?keys?size != 1
	 	>   ${setDef.simpleName}.${def.options?values[0]}.${def.options?keys[1]};<#else
	 	>   ${setDef.simpleName}.${def.options?keys[0]};</#if>
	 * </pre>
	 * uses acronyms, that is, this method returns true for the above set. 
	 * <p>
	 * On the other hand, long option names are used and this method returns 
	 * false for the set
	 * <pre>
	 * <#if def.options?keys?size != 1
	 	>   ${setDef.simpleName}.${def.options?keys[0]}.${def.options?values[1]};<#else
	 	>   ${setDef.simpleName}.${def.options?values[0]};</#if>
	 * </pre>
	 * <p>
</#if>
	 * Note that a repeated option is <i>not</i> treated as the last set option.
	 * For instance, the first and last option of the following set are 
	 * equivalent and acronyms are used:
	 * <pre>
	 * <#if def.options?keys?size != 1
	 	>   ${setDef.simpleName}.${def.options?keys[0]}.${def.options?keys[1]}.${def.options?values[0]};<#else
	 	>   ${setDef.simpleName}.${def.options?keys[0]}.${def.options?values[0]};</#if>
	 * </pre>
	 * <p>
	 * This method returns true for the empty set with no active options.
	 *  
	 * @return true if option acronyms should be used for string representations
	 *         of this option set
	 */
	@Override
	public boolean useAcronym() {
		return useAcronym;
	}
}
</#list>
