package org.unix4j.codegen.model;


public class TypeDef extends AbstractModelElement {
	
	public TypeDef(Class<?> type) {
		this(type.getSimpleName(), type.getPackage() == null ? "" : type.getPackage().getName());
	}
	public TypeDef(String typeName, String packageName) {
		this.className = typeName;
		this.packageName = packageName;
		this.packagePath = packageName.replace('.', '/');
	}
	public final String className;
	public final String packageName;
	public final String packagePath;
}