package com.lwolf.wf.script;

import com.lwolf.wf.exceptions.RepositoryException;

public interface ScriptTask {
	
	public void get() throws RepositoryException;
	public void save() throws RepositoryException;
	
	public boolean exists();
	public boolean isComplete();
	
	public String scriptId();
	public String formData();
	
	public void setInput(String name, String value);

}
