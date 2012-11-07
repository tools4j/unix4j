<#include "/include/macros.fmpp">

<@pp.dropOutputFile/> 
<#list commandDefs as def> 
<#foreach opd in def.operands?values>
<#if !isCommandSpecificOperand(def, opd)>
<#global implName=varImplName(opd)>
<#global simpleName=varTypedName(opd)>
<#global valueType=normalizeVarArgType(opd.type, true)>
<@pp.changeOutputFile name=pp.pathTo("/"+varPkgPath+"/"+simpleName+".java")/> 
<@pp.restartOutputFile/> 
package ${varPkgName};

import org.unix4j.convert.ValueConverter;
import org.unix4j.variable.TypedVariable;

/**
 * Implementation for a typed variable associated with a value of the type 
 * {@code ${normalizeVarArgType(opd.type, false)}}. A typed variable is able to
 * convert values into the variable's type using a {@link ValueConverter}. The
 * converter is passed to the constructor of a variable.
 */
public class ${simpleName} extends ${implName} implements TypedVariable<${normalizeVarArgType(opd.type, true)}> {
	
	private final ValueConverter<${normalizeVarArgType(opd.type, true)}> converter;

	/**
	 * Constructor with variable name and converter.
	 * 
	 * @param name
	 *            the variable name
	 * @param converter
	 *            the converter used to convert a value of an arbitrary type
	 *            into a value of type {@code ${normalizeVarArgType(opd.type, false)}}
	 */
	public ${simpleName}(String name, ValueConverter<${normalizeVarArgType(opd.type, true)}> converter) {
		super(name);
		if (converter == null) {
			throw new NullPointerException("converter cannot be null");
		}
		this.converter = converter;
	}
	
	@Override
	public ValueConverter<${normalizeVarArgType(opd.type, true)}> getValueConverter() {
		return converter;
	}
}
</#if>
</#foreach>
</#list>
