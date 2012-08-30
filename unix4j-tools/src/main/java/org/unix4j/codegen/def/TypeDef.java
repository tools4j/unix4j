package org.unix4j.codegen.def;


public class TypeDef extends AbstractElementDef {
	
	public TypeDef(Class<?> type) {
		this(type.getSimpleName(), new PackageDef(type.getPackage()));
	}
	public TypeDef(String fullyQualifiedClassName) {
		this(getTypeName(fullyQualifiedClassName), getPackageName(fullyQualifiedClassName));
	}
	public TypeDef(String typeName, String packageName) {
		this(typeName, new PackageDef(packageName));
	}
	public TypeDef(String typeName, PackageDef pkg) {
		this.pkg = pkg;
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