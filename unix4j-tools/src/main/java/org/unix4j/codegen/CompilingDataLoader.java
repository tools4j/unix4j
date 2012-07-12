package org.unix4j.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject.Kind;

import org.unix4j.compiler.JavaCompiler;
import org.unix4j.compiler.JavaFile;

import fmpp.Engine;
import fmpp.tdd.DataLoader;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;

/**
 * A {@link DataLoader} for the fmpp/freemarker codegenerator compiling Java
 * source files and passing the compiled and loaded classes to a
 * {@link ClassBasedDataLoader} for further analysis.
 */
public class CompilingDataLoader implements DataLoader {
	
	enum Arg {
		JavaSourceDir("java-source-dir"),
		ClassOutputDir("class-output-dir"),
		Classpath("classpath");
		private Arg(String fileName) {
			this.fileName = fileName;
		}
		private final String fileName;
		public int getArgIndex() {
			return ordinal();
		}
		public String getFileName() {
			return fileName;
		}
		public File getFile(File basedir) {
			return new File(basedir, CompilingDataLoader.class.getSimpleName() + "/" + getFileName());
		}
	}
	
	private final ClassBasedDataLoader templateLoader;
	
	public CompilingDataLoader(ClassBasedDataLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleSequence load(Engine engine, List args) throws Exception {
		return compileAndLoad(engine, args);
	}
	private SimpleSequence compileAndLoad(Engine engine, List<?> args) throws Exception {
		final List<Class<?>> classes = compileAndLoadClasses(engine, args);
		final List<TemplateModel> templateModels = new ArrayList<TemplateModel>();
		for (final Class<?> clazz : classes) {
			final TemplateModel templateModel = templateLoader.load(clazz);
			if (templateModel != null) {
				templateModels.add(templateModel);
			}
		}
		return new SimpleSequence(templateModels);
	}
	
	private List<Class<?>> compileAndLoadClasses(Engine engine, List<?> args) throws ClassNotFoundException, IOException {
		final String classPath = getArgValue(engine, args, Arg.Classpath);
		final File javaSourceDir = getArgFile(engine, args, Arg.JavaSourceDir);
		final File classOutputDir = getArgFile(engine, args, Arg.ClassOutputDir);
		if (!classOutputDir.exists()) {
			if (!classOutputDir.mkdirs()) {
				throw new IllegalArgumentException("mkdirs failed for " + Arg.ClassOutputDir + ": " + classOutputDir.getAbsolutePath());
			}
		}
		final JavaCompiler compiler = classPath == null ? new JavaCompiler() : new JavaCompiler(classPath);
		return compiler.compile(classOutputDir, javaSourceDir);
	}

	private File getArgFile(Engine engine, List<?> args, Arg arg) {
		final String path = getArgValue(engine, args, arg);
		if (path != null) {
			return new File(path);
		}
		throw new NullPointerException("missing argument " + arg + ". Hint: provide through loader arg [" + arg.getArgIndex() + "] or via text file " + arg.getFile(engine.getOutputRoot()));
	}
	private String getArgValue(Engine engine, List<?> args, Arg arg) {
		if (arg.getArgIndex() < args.size()) {
			final String value = String.valueOf(args.get(arg.getArgIndex()));
			if (!value.equals("null") && value.length() != 0) {
				return value;
			}
		}
		final File argFile = arg.getFile(engine.getOutputRoot());
		if (argFile.canRead()) {
			try {
				return new JavaFile(null, argFile, Kind.OTHER).getCharContent(true).toString();
			} catch (IOException e) {
				throw new RuntimeException("cannot read arg file " + argFile.getAbsolutePath() + ", e=" + e, e);
			}
		}
		return null;
	}

}
