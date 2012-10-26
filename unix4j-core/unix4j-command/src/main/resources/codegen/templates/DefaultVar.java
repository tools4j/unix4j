<#include "/include/macros.fmpp">

<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#foreach opd in def.operands?values>
<#if !isCommandSpecificOperand(def, opd)>
<#global simpleName=varTypeName(opd)>
<#global ifaceName=varIfaceName(opd)>
<#global valueType=normalizeVarArgType(opd.type, true)>
<@pp.changeOutputFile name=pp.pathTo("/"+varPkgPath+"/"+simpleName+".java")/> 
package ${varPkgName};

import org.unix4j.variable.DefaultVariable;

/**
 * Variable holding a value of the type {@code ${valueType}}.
 */
public class ${simpleName} extends DefaultVariable<${valueType}> implements ${ifaceName} {
	/**
	 * Constructor with variable name initializing the variable with a null
	 * value.
	 * 
	 * @param name
	 *            the variable name
	 */
	public ${simpleName}(String name) {
		super(name);
	}

	/**
	 * Constructor with name and initial value for the variable.
	 * 
	 * @param name
	 *            the variable name
	 * @param value
	 *            the initial value for the variable
	 */
	public ${simpleName}(String name, ${opd.type} value) {
		super(name, value);
	}
	<#if isPrimitive(opd.type)>

	/**
	 * Constructor with name and initial value for the variable.
	 * 
	 * @param name
	 *            the variable name
	 * @param value
	 *            the initial value for the variable
	 */
	public ${simpleName}(String name, ${valueType} value) {
		super(name, value);
	}
	</#if>
}
</#if>
</#foreach>
</#list>
