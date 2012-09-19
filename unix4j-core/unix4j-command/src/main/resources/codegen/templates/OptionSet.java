<#include "include/macros.fmpp">

<@pp.dropOutputFile /> 
<#list optionSetDefs as def>
<#global cmdDef=def.command> 
<#if cmdDef.options?size != 0> 
<#global cmd=cmdDef.command> 
<#global commandName=cmdDef.commandName> 
<#global optDef=def.optionType> 
<#global optionsName=cmd.simpleName+"Options">
<#list def.groups as grp> 
<#global grpDef=grp.groupType> 
<#global options=grp.options> 
<@pp.changeOutputFile name=pp.pathTo("/"+grpDef.pkg.path+"/"+grpDef.simpleName+".java")/> 
package ${grpDef.pkg.name};

<#function activeSetName activeSet useAcronym>
	<#if useAcronym>
		<#return activeSet.name>
	<#else>
		<#return activeSet.name + "_long">
	</#if>
</#function>
<#function activeSetRef grp opt activeSet useAcronym>
	<#local optGroup = grp.optionToNextGroup[opt.name]>
	<#if grp.groupType.simpleName = optGroup.groupType.simpleName>
		<#return activeSetName(activeSet.next[opt.name], useAcronym)>
	<#else>
		<#return optGroup.groupType.simpleName + "." + activeSetName(activeSet.next[opt.name], useAcronym)>
	</#if>
</#function>
<#function groupForOption grp opt>
	<#if grp.optionToNextGroup[opt.name]??>
		<#return grp.optionToNextGroup[opt.name]>
	<#else>
		<#return grp>
	</#if>
</#function>
<#function nextSetNotNull grp optGroup opt varName>
	<#if grp.groupType.simpleName = optGroup.groupType.simpleName>
		<#return varName + " == null ? this : " + varName>
	<#else>
		<#return "notNull(" + varName + ")">
	</#if>
</#function>
<#function hasRefToOtherGroup grp>
	<#foreach otherGroup in grp.optionToNextGroup?values>
		<#if grp.groupType.simpleName != otherGroup.groupType.simpleName>
			<#return true>
		</#if>
	</#foreach>
	<#return false>
</#function>
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import org.unix4j.option.Option;

import ${cmd.pkg.name}.${cmd.simpleName};

/**
 * Option sets for the {@link ${cmd.simpleName} ${commandName}} command with 
 * the following options: <#foreach opt in options?values>{@link #${opt.acronym} ${opt.acronym}}<#if opt_has_next>, </#if></#foreach>.
 * <p>
 * Application code does normally not directly refer to this class;
 * {@link ${cmd.simpleName}#OPTIONS} should be used instead to specify command 
 * options. See also {@link ${cmdDef.pkg.name}.${optionsName}} for more information.
 */
public enum ${grpDef.simpleName} implements ${cmd.simpleName}Options {
	<#foreach levelSets in grp.levelActiveSets?reverse>
	<#foreach activeSet in levelSets?values>
	<#foreach useAcronym in [true,false]>
	/** <#if activeSet.active?size==0>Empty option set without active options<#else>Option set with the following active options: <#foreach opt in activeSet.active>{@link #${opt} ${options[opt].acronym}}<#if opt_has_next>, </#if></#foreach></#if>.*/
	${activeSetName(activeSet, useAcronym)}(
		<#foreach opt in options?values><#if activeSet.next[opt.name]??>/*${opt.acronym}:*/${activeSetRef(grp, opt, activeSet, true)}, /*${opt.name}:*/${activeSetRef(grp, opt, activeSet, false)}, <#else>/*${opt.acronym}:*/null /*already set*/, /*${opt.name}:*/null /*already set*/, </#if></#foreach>
		${useAcronym?string}<#if activeSet.active?size != 0>, </#if>
		/*active:*/<#foreach opt in activeSet.active>${optDef.simpleName}.${options[opt].name}<#if opt_has_next>, </#if></#foreach>
	)<#if useAcronym_has_next || activeSet_has_next || levelSets_has_next>,<#else>;</#if>
	</#foreach>
	</#foreach>
	</#foreach>
	private ${grpDef.simpleName}(
		<#foreach opt in options?values><#global optGroup = groupForOption(grp, opt)>${optGroup.groupType.simpleName} ${opt.acronym}, ${optGroup.groupType.simpleName} ${opt.name}, </#foreach>
		boolean useAcronym,
		${optDef.simpleName}... activeOptions
	) {
		<#foreach opt in options?values>
		<#global optGroup = groupForOption(grp, opt)>
		this.${opt.acronym} = ${nextSetNotNull(grp, optGroup, opt, opt.acronym)};
		this.${opt.name} = ${nextSetNotNull(grp, optGroup, opt, opt.name)};
		</#foreach>
		this.useAcronym = useAcronym;
		this.options = activeOptions.length == 0 ? EnumSet.noneOf(${optDef.simpleName}.class) : EnumSet.copyOf(Arrays.asList(activeOptions));
	}
	private final boolean useAcronym;
	<#foreach opt in options?values>
	<@optionJavadocAcronym opt true/>
	<#global optGroup = groupForOption(grp, opt)>
	public final ${optGroup.groupType.simpleName} ${opt.acronym};
	<@optionJavadocLong opt true/>
	public final ${optGroup.groupType.simpleName} ${opt.name};
	</#foreach>
	private final EnumSet<${optDef.simpleName}> options;
	//inherit javadoc
	@Override
	public boolean isSet(${optDef.simpleName} option) {
		return options.contains(option);
	}
	//inherit javadoc
	@Override
	public int size() {
		return options.size();
	}
<#if hasRefToOtherGroup(grp)>
	/** 
	 * Checks that the given {@code value} is not null and throws an exception 
	 * otherwise.
	 * 
	 * @param the value to check
	 * @return the given {@code value} if it is not null
	 * @throws NullPointerException if {@code value==null} 
	 */
	private static <T> T notNull(T value) {
		if (value != null) return value;
		throw new NullPointerException();
	}
</#if>
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
	 * Returns an immutable iterator with the active options of this option set.
	 * 
	 * @return an immutable iterator for over the active options
	 */
	@Override
	public Iterator<${optDef.simpleName}> iterator() {
		return Collections.unmodifiableSet(options).iterator();
	}
	/**
	 * Returns true if the {@link Option#acronym() acronym} should be used in
	 * for the specified {@code option} string representations. 
<#if options?size != 0>
	 * <p>
	 * In particular and independent from the {@code option} argument, this 
	 * option set returns true if the last option added to this set was an 
	 * acronym, and false if it was a long option name. 
	 * <p>
	 * For instance, the set defined as
	 * <pre>
	 * <#if options?size != 1
	 	>   ${grpDef.simpleName}.${options?values[0].name}.${options?values[1].acronym};<#else
	 	>   ${grpDef.simpleName}.${options?values[0].acronym};</#if>
	 * </pre>
	 * uses acronyms, that is, this method always returns true for the above 
	 * set. 
	 * <p>
	 * On the other hand, long option names are used and this method always 
	 * returns false for the set
	 * <pre>
	 * <#if options?size != 1
	 	>   ${grpDef.simpleName}.${options?values[0].acronym}.${options?values[1].name};<#else
	 	>   ${grpDef.simpleName}.${options?values[0].name};</#if>
	 * </pre>
	 * <p>
</#if>
	 * Note that a repeated option is <i>not</i> treated as the last set option.
	 * For instance, the first and last option of the following set are 
	 * equivalent and acronyms are used:
	 * <pre>
	 * <#if options?size != 1
	 	>   ${grpDef.simpleName}.${options?values[0].acronym}.${options?values[1].acronym}.${options?values[0].name};<#else
	 	>   ${grpDef.simpleName}.${options?values[0].acronym}.${options?values[0].name};</#if>
	 * </pre>
	 * <p>
	 * This method always returns true for the empty set with no active options.
	 *  
	 * @param option
	 *            the option of interest, has no impact on the result returned
	 *            by this method
	 * @return true if option acronyms should be used for string representations
	 *         of any option of this option set
	 */
	@Override
	public boolean useAcronymFor(${optDef.simpleName} option) {
		return useAcronym;
	}
}
</#list>
</#if>
</#list>