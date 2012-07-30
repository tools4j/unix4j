package org.unix4j.builder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.unix4j.command.Command;
import org.unix4j.command.CommandInterface;

public class GenericCommandBuilder {
	
	public static <B extends CommandBuilder> B createCommandBuilder(Class<B> commandBuilderInterface, CommandInterface<? extends Command<?>>... commandFactories) {
		return createCommandBuilder(commandBuilderInterface, new DefaultCommandBuilder(), commandFactories);
	}
	@SuppressWarnings("unchecked")
	public static <B extends CommandBuilder> B createCommandBuilder(Class<B> commandBuilderInterface, DefaultCommandBuilder defaultCommandBuilder, CommandInterface<? extends Command<?>>... commandFactories) {
		if (commandBuilderInterface.isInterface()) {
			return (B) Proxy.newProxyInstance(GenericCommandBuilder.class.getClassLoader(), new Class[] {commandBuilderInterface}, new GenericCommandHandler<B>(commandBuilderInterface, defaultCommandBuilder, commandFactories));
		}
		throw new IllegalArgumentException(commandBuilderInterface.getName() + " must be an interface");
	}

	private static class GenericCommandHandler<B extends CommandBuilder> implements InvocationHandler {
		private final DefaultCommandBuilder defaultCommandBuilder;
		private final Map<MethodSignature, Object> signatureToTarget = new HashMap<GenericCommandBuilder.MethodSignature, Object>();
		public GenericCommandHandler(Class<B> commandBuilderInterface, DefaultCommandBuilder defaultCommandBuilder, CommandInterface<? extends Command<?>>... commandFactories) {
			this.defaultCommandBuilder = defaultCommandBuilder;
			final Map<MethodSignature, Object> factoryMethods = new HashMap<GenericCommandBuilder.MethodSignature, Object>();
			addSignatures(factoryMethods, defaultCommandBuilder);
			for (int i = 0; i < commandFactories.length; i++) {
				addSignatures(factoryMethods, commandFactories[i]);
			}
			for (final Method method : defaultCommandBuilder.getClass().getMethods()) {
				final MethodSignature signature = new MethodSignature(method);
				signatureToTarget.put(signature, defaultCommandBuilder);
			}
			for (final Method method : commandBuilderInterface.getMethods()) {
				final MethodSignature signature = new MethodSignature(method);
				final Object factory = factoryMethods.get(signature);
				if (factory == null) {
					throw new IllegalArgumentException("No factory method found for method " + signature + " defined in interface " + commandBuilderInterface.getName());
				}
				final Object otherFactory = signatureToTarget.get(signature);
				if (otherFactory == null) {
					signatureToTarget.put(signature, factory);
				} else {
					if (factory != otherFactory) {
						throw new IllegalArgumentException("method " + signature + " exist in " + otherFactory.getClass().getName() + " and also in " + factory.getClass().getName());
					}
				}
			}
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final MethodSignature signature = new MethodSignature(method);
			final Object target = signatureToTarget.get(signature);
			if (target != null) {
				Object result = signature.invoke(target, args);
				if (result instanceof Command) {
					result = defaultCommandBuilder.join((Command<?>)result);
				}
				if (result == defaultCommandBuilder) {
					result = proxy;
				}
				return result;
			}
			throw new IllegalStateException("no target object found for method " + signature);
		}

	}

	private static class MethodSignature {
		private final String name;
		private final Class<?>[] parameterTypes;

		public MethodSignature(Method method) {
			this(method.getName(), method.getParameterTypes());
		}

		public MethodSignature(String name, Class<?>... parameterTypes) {
			this.name = name;
			this.parameterTypes = parameterTypes;
		}
		
		public Method findInClass(Class<?> clazz) throws NoSuchMethodException {
			return clazz.getMethod(name, parameterTypes);
		}
		
		public Object invoke(final Object instance, final Object... args) {
			try {
				final Method method = findInClass(instance.getClass());
				return method.invoke(instance, args);
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof RuntimeException) {
					throw (RuntimeException)e.getCause();
				}
				throw new RuntimeException(e.getCause());
			} catch (Exception e) {
				throw new RuntimeException("invokation failed for method " + this + " in class " + instance.getClass().getName() + ", e=" + e, e);
			}
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder(name).append('(');
			for (int i = 0; i < parameterTypes.length; i++) {
				if (i > 0) sb.append(", ");
				sb.append(parameterTypes[i].getName());
			}
			return sb.append(')').toString();
		}
		@Override
		public int hashCode() {
			return 31 * name.hashCode() ^ Arrays.hashCode(parameterTypes);
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true;
			if (obj instanceof MethodSignature) {
				final MethodSignature other = (MethodSignature)obj;
				if (!name.equals(other.name)) return false;
				return Arrays.equals(parameterTypes, other.parameterTypes);
			}
			return false;
		}
	}

	private static void addSignatures(Map<MethodSignature, Object> factoryMethods, Object factory) {
		for (final Method method : factory.getClass().getMethods()) {
			final MethodSignature signature = new MethodSignature(method);
			final Object existingFactory = factoryMethods.get(signature);
			if (existingFactory == null) {
//				System.out.println("add method: " + signature + ":\t" + factory.getClass());
				factoryMethods.put(signature, factory);
			} else {
				//NOTE:	(a) same method exists twice if return type has been overloaded
				//		(b) for object methods and more general all methods defined by DefaultCommandBuilder, we use the DefaultCommandBuilder method
				if (existingFactory != factory && !DefaultCommandBuilder.class.isInstance(existingFactory)) {
					throw new IllegalArgumentException("method " + signature + " exist in " + existingFactory.getClass().getName() + " and also in " + factory.getClass().getName());
				}
			}
		}
	}
}
