package org.unix4j.codegen.model;

public class PackageDef extends AbstractModelElement {
	
	public PackageDef(Package pkg) {
		this(pkg.getName());
	}
	public PackageDef(String packageName) {
		this.name = packageName;
		this.path = packageName.replace('.', '/');
	}
	public final String name;
	public final String path;
}