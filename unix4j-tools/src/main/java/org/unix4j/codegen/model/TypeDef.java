package org.unix4j.codegen.model;


public class TypeDef extends AbstractModelElement {
	
	public TypeDef(Class<?> type) {
		this.pkg = new PackageDef(type.getPackage());
		this.simpleName = type.getSimpleName();
	}
	public TypeDef(String fullyQualifiedClassName) {
		this(getTypeName(fullyQualifiedClassName), getPackageName(fullyQualifiedClassName));
	}
	public TypeDef(String typeName, String packageName) {
		this.pkg = new PackageDef(packageName);
		this.simpleName = typeName;
	}
	private static String getTypeName(String fullyQualifiedClassName) {
		final int lastDot = fullyQualifiedClassName.lastIndexOf('.');
		return fullyQualifiedClassName.substring(lastDot + 1);//also works if there is no dot
	}
	private static String getPackageName(String fullyQualifiedClassName) {
		final int lastDot = fullyQualifiedClassName.lastIndexOf('.');
		return lastDot < 0 ? fullyQualifiedClassName : fullyQualifiedClassName.substring(0, lastDot);
	}
	public final PackageDef pkg;
	public final String simpleName;
}