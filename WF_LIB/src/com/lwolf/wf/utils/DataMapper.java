package com.lwolf.wf.utils;

import java.lang.reflect.Field;

public class DataMapper {
	
	public static void map(Object to, String toFieldName, Object from, String... fromChain) throws NoSuchFieldException, IllegalAccessException {
		Field toField = to.getClass().getDeclaredField(toFieldName);
		toField.setAccessible(true);
		
		Object value = null;
		Object target = from;
		Field targetField = null;
		for (String field : fromChain) {
			if (target != null) {
				targetField = target.getClass().getDeclaredField(field);
				targetField.setAccessible(true);
				
				target = targetField.get(target);
				value = target;
			}
		}
		toField.set(to, value);
	}
	
	public static void map(Object to, String toFieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
		Field toField = to.getClass().getDeclaredField(toFieldName);
		toField.setAccessible(true);
		toField.set(to, value);
	}
	
	public static void map(Object to, String toFieldName, Object from, String fromFieldName) throws NoSuchFieldException, IllegalAccessException {
		Field toField = to.getClass().getDeclaredField(toFieldName);
		toField.setAccessible(true);
		
		Field fromField = from.getClass().getDeclaredField(fromFieldName);
		fromField.setAccessible(true);
		
		toField.set(to, fromField.get(from));
	}

}
