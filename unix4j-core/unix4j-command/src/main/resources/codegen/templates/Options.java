<#include "include/macros.fmpp">

<@pp.dropOutputFile />
<#list optionSetDefs as def>
<#global cmdDef=def.command> 
<#global cmd=cmdDef.command>
<#global commandName=cmdDef.commandName> 
<#global options=cmdDef.options?values> 
<#global optionName=cmd.simpleName+"Option">
<#global simpleName=cmd.simpleName+"Options">
<@pp.changeOutputFile name=pp.pathTo("/"+cmdDef.pkg.path+"/"+simpleName+".java")/> 
package ${cmdDef.pkg.name};

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.unix4j.option.Option;

import ${cmd.pkg.name}.${cmd.simpleName};
import ${cmdDef.pkg.name}.${optionName};

/**
 * Options for the {@link ${cmd.simpleName} ${commandName}} command with the 
 * the following options: <#foreach opt in options>{@link ${optionName}#${opt.name} ${opt.acronym}}<#if opt_has_next>, </#if></#foreach>.
 * Note that each option has also a long name: <#foreach opt in options>{@code ${opt.acronym}:}{@link ${optionName}#${opt.name} ${opt.name}}<#if opt_has_next>, </#if></#foreach>.
 * <p>
 * This class serves as entry point to every possible set of {@code ${commandName}} options
 * defined as an enum constant. With this explicit expansion of all possible 
 * option combinations, options can be passed to the command in a very compact 
 * form, such as:
 * <pre>
<#if options?size != 0> * ${commandName}(${cmd.simpleName}.OPTIONS.${options[0].acronym}, ...);
<#if options?size != 1> * ${commandName}(${cmd.simpleName}.OPTIONS.${options[0].acronym}.${options[1].acronym}, ...);
<#if options?size != 2> * ...
<#if options?size != 2> * ${commandName}(${cmd.simpleName}.OPTIONS<#foreach o in options>.${o.acronym}</#foreach>, ...);
</#if></#if></#if></#if>
 * </pre>
 */
public final class ${simpleName} {
	/**
	 * The singleton instance.
	 */
	public static final ${simpleName} INSTANCE = new ${simpleName}();
	
	/**
	 * Constant for an empty option set.
	 */
	public static final ${cmd.simpleName}.Options NO_OPTIONS = new ${cmd.simpleName}.Options() {
		@Override
		public boolean isSet(${optionName} option) {
			return false;
		}
		/**
		 * Returns 0 as this is a set with no active options.
		 * 
		 * @return zero
		 */
		@Override
		public int size() {
			return 0;
		}
		/**
		 * Returns an immutable empty set.
		 * 
		 * @return an immutable empty set.
		 */
		@Override
		public Set<${optionName}> asSet() {
			return Collections.emptySet();
		}
		
		/**
		 * Returns an iterator returning no elements. 
		 * 
		 * @return an immutable iterator with no elements.
		 */
		@Override
		public Iterator<${optionName}> iterator() {
			return asSet().iterator();
		}
		
		/**
		 * Returns true if the {@link Option#acronym() acronym} should be used
		 * for the specified {@code option} in string representations. 
		 * <p>
		 * This method returns always true;
		 *  
		 * @param option
		 *            the option of interest
		 * @return always true
		 */
		@Override
		public boolean useAcronymFor(${optionName} option) {
			return true;
		}
	};

	<#global grp = def.initialGroup>
	<#foreach opt in grp.options?values>
	<#global optType = grp.optionToNextGroup[opt.name].groupType>
	<#global activeSetName = "Active_" + opt.acronym>
	<@optionJavadocAcronym opt false/>
	public final ${optType.simpleName} ${opt.acronym} = ${optType.simpleName}.${activeSetName};  
	<@optionJavadocLong opt false/>
	public final ${optType.simpleName} ${opt.name} = ${optType.simpleName}.${activeSetName}_long;  
	</#foreach>
	
}
</#list>