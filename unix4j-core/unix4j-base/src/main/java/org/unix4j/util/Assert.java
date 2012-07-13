package org.unix4j.util;


public class Assert {
	public static void assertArgNotNull(final String message, final Object obj){
		if(obj == null){
			throw new NullPointerException(message);
		}
	}

	public static void assertArgNotNull(final String message, final Object ... objects){
		for(Object obj: objects){
			assertArgNotNull(message, obj);
		}
	}

	public static void assertArgTrue(final String message, boolean expression){
		if(!expression){
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgGreaterThan(final String message, int argValue, int num){
		if(argValue <= num){
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgGreaterThanOrEqualTo(final String message, int argValue, int num){
		if(argValue < num){
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgLessThan(final String message, int argValue, int num){
		if(argValue >= num){
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgLessThanOrEqualTo(final String message, int argValue, int num){
		if(argValue > num){
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgFalse(final String message, boolean expression){
		if(expression){
			throw new IllegalArgumentException(message);
		}
	}
}
