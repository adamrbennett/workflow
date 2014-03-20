package com.lwolf.wf.script;

import com.lwolf.wf.exceptions.ProcessException;

public interface ProcessEngine {
	
	public void invoke() throws ProcessException;
//	public Object call(String function, Object... args) throws ProcessException;
	public void completeTask(String taskId, String handler) throws ProcessException;
	
	public ScriptTask create(String taskId) throws ProcessException;
	public ScriptTask get(String taskId);
	
	public boolean isComplete(String taskId);

}
