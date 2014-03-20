package com.lwolf.wf.script.impl;

import java.lang.reflect.Field;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;

import com.lwolf.wf.script.ScriptUser;

public class ScriptUserImpl extends ScriptableObject implements ScriptUser {
	
	private static final long serialVersionUID = 1L;
	
	public String name;
	
	@JSConstructor
	public ScriptUserImpl() {
		
	}
	
	public ScriptUserImpl(String name) {
		this.name = name;
	}

	@Override
	public String getClassName() {
		return "User";
	}

	@Override
	public Object get(String name, Scriptable start) {
		// look for the field on this
		Field f = null;
		try {
			f = getClass().getDeclaredField(name);
		} catch (NoSuchFieldException ex) {
			f = null;
		}
		if (f != null) {
			try {
				return f.get(this);
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		
		// if all else fails, fall back to default behavior
		return super.get(name, start);
	}

}
