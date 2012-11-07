<#include "/include/macros.fmpp">

<@pp.dropOutputFile/> 
<#list commandDefs as def> 
<#foreach opd in def.operands?values>
<#if !isCommandSpecificOperand(def, opd)>
<#global simpleName=varImplName(opd)>
<#global ifaceName=varIfaceName(opd)>
<#global valueType=normalizeVarArgType(opd.type, true)>
<@pp.changeOutputFile name=pp.pathTo("/"+varPkgPath+"/"+simpleName+".java")/> 
<@pp.restartOutputFile/> 
package ${varPkgName};

import org.unix4j.variable.DefaultVariable;

/**
 * Implementation for a variable associated with a value of the type {@code ${normalizeVarArgType(opd.type, false)}}.
 */
public class ${simpleName} extends DefaultVariable implements ${ifaceName} {
	
	/**
	 * Constructor with variable name.
	 * 
	 * @param name
	 *            the variable name
	 */
	public ${simpleName}(String name) {
		super(name);
	}
}
</#if>
</#foreach>
</#list>
