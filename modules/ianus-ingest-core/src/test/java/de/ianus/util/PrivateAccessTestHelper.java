package de.ianus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrivateAccessTestHelper {
	
	
	
	
	public static Object getReflection(Class<?> c) {
		try {
			return c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static Method methodSetAccessible(Class<?> c, Object reflection, String name, Class[] argClasses) {
		Method method = null;
		
		try {
			method = c.getDeclaredMethod(name, argClasses);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		method.setAccessible(true);
		return method;
	}
	
	
	
	public static Object invokeMethod(Class<?> c, Object reflection, String name, Class[] argClasses,  Object[] argObjects) {
		Method method = methodSetAccessible(c, reflection, name, argClasses);
		try {
			return method.invoke(reflection, argObjects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public static boolean setField(Class<?> c, Object reflection, String name, Object value) {
		Field field = null;
		
		try {
			field = c.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		field.setAccessible(true);
		
		try {
			field.set(reflection, value);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	public static Object getField(Class<?> c, Object reflection, String name) {
		Field field = null;
		
		try {
			field = c.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		field.setAccessible(true);
		
		try {
			return field.get(reflection);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
}
