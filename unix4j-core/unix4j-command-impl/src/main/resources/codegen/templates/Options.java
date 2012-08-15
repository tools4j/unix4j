<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#global cmd=def.command>
<#global optionName=cmd.simpleName+"Option">
<#global simpleName=cmd.simpleName+"Options">
<@pp.changeOutputFile name=pp.pathTo(def.pkg.path+"/"+simpleName+".java")/> 
package ${def.pkg.name};

import org.unix4j.optset.OptionSet;

/**
 * Option set for the {@link ${cmd.pkg.name}.${cmd.simpleName} ${def.commandName}} command.
 * <p>
 * This interface serves as an alias for the extended interface to simplify the
 * command signature methods by avoiding generic parameters.
 */
public interface ${simpleName} extends OptionSet<${optionName}> {
	//no methods as it this interface is an alias only
}
</#list>