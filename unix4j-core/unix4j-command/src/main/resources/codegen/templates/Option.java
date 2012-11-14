<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#if def.options?size != 0> 
<#global cmd=def.command>
<#global simpleName=cmd.simpleName+"Option">
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+simpleName+".java")/> 
package ${def.pkg.name};

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;

import org.unix4j.option.Option;
import ${cmd.pkg.name}.${cmd.simpleName};

/**
 * Options for the {@link ${cmd.simpleName} ${def.commandName}} command.
 * <p>
 * For most applications, it may be more convenient to use {@link ${cmd.simpleName}#Options} 
 * instead of the option constants defined here.
 * <p>
<#include "/include/options-javadoc.java">
 */
public enum ${simpleName} implements Option, ${cmd.simpleName}Options {
	<#foreach opt in def.options?values>
	/**
	 * Option <b>{@code --${opt.name}}</b>, <b>{@code -${opt.acronym}}</b>: 
	 * ${opt.desc}
	 */
	${opt.name}('${opt.acronym}')<#if opt_has_next>,<#else>;</#if>
	</#foreach>
	
	private final char acronym;
	private ${simpleName}(char acronym) {
		this.acronym = acronym;
	}
	@Override
	public Class<${simpleName}> optionType() {
		return ${simpleName}.class;
	}
	/**
	 * Returns the option with the given {@code acronym}, or {@code null} if no
	 * such option is found.
	 * 
	 * @param acronym the option {@link #acronym() acronym}
	 * @return	the option with the given {@code acronym} or {@code null} if it
	 * 			is not found
	 */
	public static ${simpleName} findByAcronym(char acronym) {
		for (final ${simpleName} opt : values()) {
			if (opt.acronym() == acronym) return opt;
		}
		return null;
	}
	@Override
	public char acronym() {
		return acronym;
	}
	@Override
	public boolean isSet(${simpleName} option) {
		return equals(option);
	}
	/**
	 * Returns a new set with {@code this} active option.
	 * 
	 * @return a new set containing this option
	 */
	@Override
	public EnumSet<${simpleName}> asSet() {
		return EnumSet.of(this);
	}
	
	/**
	 * Returns an immutable iterator returning o single element: {@code this} 
	 * option.
	 * 
	 * @return an immutable iterator with {@code this} active option.
	 */
	@Override
	public Iterator<${simpleName}> iterator() {
		return Collections.singleton(this).iterator();
	}
	
	/**
	 * Returns 1 as this is a set with a single element: {@code this} option
	 * 
	 * @return one
	 */
	@Override
	public int size() {
		return 1;
	}

	/**
	 * Returns true if the {@link Option#acronym() acronym} should be used for
	 * the specified {@code option} in string representations. 
	 * <p>
	 * This method returns always true for all options.
	 *  
	 * @param option
	 *            the option of interest
	 * @return always true indicating that option acronyms should be used in
	 * 			string representations for all options
	 */
	@Override
	public boolean useAcronymFor(${simpleName} option) {
		return true;
	}
}
</#if>
</#list>
