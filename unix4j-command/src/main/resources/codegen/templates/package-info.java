<@pp.dropOutputFile />
<#list commandDefs as def> 
<#global cmd=def.command>
<#global commandName=def.commandName>
<@pp.changeOutputFile name=pp.pathTo("/"+def.pkg.path+"/"+"package-info.java")/> 
/**
 * Contains private classes used by the {@link ${cmd.pkg.name}.${cmd.simpleName} ${commandName}} command. 
 * Most of the classes have package visibility and do not need to be accessed by 
 * application code directly. 
 */
package ${def.pkg.name};
</#list>