package org.unix4j.variable;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unix4j.context.ExecutionContext;

/**
 * Variable resolver for values defined by the {@link ExecutionContext}.
 */
public class ExecutionContextVariableResolver implements VariableResolver {
	
	public static enum Variable {
		CurrentDirectory("$pwd") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getCurrentDirectory();
			}
		},
		User("$whoami") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getUser();
			}
		}, 
		UserHome("$home") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getUserHome();
			}
		}, 
		TempDirectory("$temp") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getTempDirectory();
			}
		},
		Locale("$locale") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getLocale();
			}
		};
		private final String unixName;
		private Variable(String unixName) {
			this.unixName = unixName;
		}
		public String getUnixName() {
			return unixName;
		}
		public static Variable getByVariableName(String variableName) {
			for (final Variable v : values()) {
				if (v.getUnixName().equals(variableName)) {
					return v;
				}
			}
			return null;
		}
		abstract public Object getValue(ExecutionContext executionContext);
	}

	private final ExecutionContext executionContext;

	/**
	 * Constructor with execution context.
	 * 
	 * @param executionContext
	 *            the execution context with values resolved by this class
	 */
	public ExecutionContextVariableResolver(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	@Override
	public Object getValue(String name) {
		final Variable variable = Variable.getByVariableName(name);
		if (variable != null) {
			return variable.getValue(executionContext);
		}
		return null;
	}

	@Override
	public String toString() {
		final Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		for (final Variable v : Variable.values()) {
			map.put(v, v.getValue(executionContext));
		}
		return map.toString();
	}

}
