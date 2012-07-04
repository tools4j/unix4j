<@pp.dropOutputFile /> 
<#list commands as cmd> 
<#global pkgName=cmd.packagePath> 
<@pp.changeOutputFile name=pp.pathTo("/main/java/"+cmd.packagePath+"/"+cmd.className+".java")/> 

package ${cmd.packageName}

${cmd.javadoc}
public final class ${cmd.className} {
	/**
	 * Interface defining all method signatures for the @{link ${cmd.className} ${cmd.name}} command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> {
	<#foreach method in cmd.methods>
		${method.javadoc}
		R ${method.name}(<#foreach param in method.params>${param.type} ${param.name}<#if param_has_next>, </#if></#foreach>);
	</#foreach>
	}
	
	/**
	 * Options for the @{link ${cmd.className} ${cmd.name}} command.
	 */
	public static final class Options {
		<#assign type=cmd.options.types[cmd.options.initial]>
		<#foreach optName in type.next?keys>
			<#assign optType=type.next[optName]>
			<#assign optJavoc=cmd.options.javadoc[optName]>
			<#if optJavoc??>${optJavoc}<#else>/*JAVADOC missing for ${optName}*/</#if>
			public static final OptionSets.${optType} ${optName} = OptionSets.${optType}.INSTANCE;
		</#foreach>
	}
	
	public static final class OptionSets {
		public static final class OptionSet extends DefaultOptionSet<${cmd.className}.Interface> {
			
		}
		<#foreach setName in cmd.options.types?keys>
		<#assign setType=cmd.options.types[setName]>
		/** OptionSet with the following active options: [<#foreach optName in setType.current>${optName}<#if optName_has_next>, </#if></#foreach>] */
		public static final class ${setName} extends OptionSet {
			private ${setName}() {
				super(<#foreach optName in setType.current>${cmd.options.enumClassName}.${optName}<#if optName_has_next>, </#if></#foreach>)
			}
			private static final ${setName} INSTANCE = new ${setName}(); 
			<#foreach optName in setType.next?keys>
			<#assign optType=setType.next[optName]>
			<#assign optJavoc=cmd.options.javadoc[optName]>
			<#if optJavoc??>${optJavoc}<#else>/*JAVADOC missing for ${optName}*/</#if>
			public static final ${optType} ${optName} = ${optType}.INSTANCE;
			</#foreach>
		}
		</#foreach>
	}
	
}
</#list>
