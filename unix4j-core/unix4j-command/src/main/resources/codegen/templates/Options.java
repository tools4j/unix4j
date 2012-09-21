<#include "/include/macros.fmpp">

<@pp.dropOutputFile />
<#list optionSetDefs as def>
<#global cmdDef=def.command> 
<#if cmdDef.options?size != 0> 
<#global cmd=cmdDef.command>
<#global commandName=cmdDef.commandName> 
<#global optionName=cmd.simpleName+"Option">
<#global simpleName=cmd.simpleName+"Options">
<@pp.changeOutputFile name=pp.pathTo("/"+cmdDef.pkg.path+"/"+simpleName+".java")/> 
package ${cmdDef.pkg.name};

import java.util.Collections;
import java.util.Iterator;

import org.unix4j.option.Option;
import org.unix4j.option.OptionSet;

import ${cmd.pkg.name}.${cmd.simpleName};
import ${cmdDef.pkg.name}.${optionName};

/**
 * Interface implemented by all option sets for the {@link ${cmd.simpleName} ${commandName}} command.
 * <p>
 * This interface serves as an alias for the extended interface to simplify the
 * command signature methods by avoiding generic parameters.
 */
public interface ${simpleName} extends OptionSet<${optionName}> {
	/**
	 * Constant for an empty option set.
	 */
	${simpleName} EMPTY = new ${simpleName}() {
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
		public java.util.Set<${optionName}> asSet() {
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
}
</#if>
</#list>