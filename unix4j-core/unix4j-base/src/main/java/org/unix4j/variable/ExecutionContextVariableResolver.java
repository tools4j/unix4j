package org.unix4j.variable;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unix4j.context.ExecutionContext;

/**
 * Variable resolver for values defined by the {@link ExecutionContext}. The
 * {@link Variable} constants define variable names for execution context
 * values.
 */
public class ExecutionContextVariableResolver implements VariableResolver {
	/**
	 * Variable name constants for {@link ExecutionContext} values.
	 */
	public static enum Variable {
		/**
		 * Variable {@code $pwd} to access
		 * {@link ExecutionContext#getCurrentDirectory()}.
		 */
		CurrentDirectory("$pwd") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getCurrentDirectory();
			}
		},
		/**
		 * Variable {@code $whoami} to access {@link ExecutionContext#getUser()}
		 * .
		 */
		User("$whoami") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getUser();
			}
		},
		/**
		 * Variable {@code $home} to access
		 * {@link ExecutionContext#getUserHome()}.
		 */
		UserHome("$home") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getUserHome();
			}
		},
		/**
		 * Variable {@code $temp} to access
		 * {@link ExecutionContext#getTempDirectory()}.
		 */
		TempDirectory("$temp") {
			@Override
			public Object getValue(ExecutionContext executionContext) {
				return executionContext.getTempDirectory();
			}
		},
		/**
		 * Variable {@code $locale} to access
		 * {@link ExecutionContext#getLocale()}.
		 */
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
