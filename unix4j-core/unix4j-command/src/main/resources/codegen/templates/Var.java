<#include "/include/macros.fmpp">

<@pp.dropOutputFile /> 
<#list commandDefs as def> 
<#foreach opd in def.operands?values>
<#if !isCommandSpecificOperand(def, opd)>
<#global simpleName=varIfaceName(opd)>
<@pp.changeOutputFile name=pp.pathTo("/"+varPkgPath+"/"+simpleName+".java")/> 
package ${varPkgName};

import org.unix4j.variable.Variable;

/**
 * Variable associated with a value of the type {@code ${normalizeVarArgType(opd.type, false)}}.
 */
public interface ${simpleName} extends Variable{}
</#if>
</#foreach>
</#list>
