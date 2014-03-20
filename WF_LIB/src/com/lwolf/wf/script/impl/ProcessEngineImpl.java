package com.lwolf.wf.script.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.lwolf.wf.exceptions.ProcessException;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.script.ProcessEngine;
import com.lwolf.wf.script.ScriptTask;
import com.lwolf.wf.utils.JSON;

public class ProcessEngineImpl implements ProcessEngine {
	
	public enum HandlerType {
		COMPLETED("completed");
		
		private String type;
		
		private HandlerType(String type) {
			this.type = type;
		}
		
		@Override
		public String toString() {
			return type;
		}
		
		public boolean is(String value) {
			return type.equalsIgnoreCase(value);
		}
	}
	
	private TaskRepository _taskRepository;
	private InputRepository _inputRepository;
	
	private Integer _processId;
	private InputStream _data;
	private String _userName;
	
	private Document _doc;
	private Scriptable _scope;
	
	public ProcessEngineImpl(TaskRepository taskRepository, InputRepository inputRepository, Integer processId, InputStream data, String userName) {
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
		
		_processId = processId;
		_data = data;
		_userName = userName;
		
		try {
			if (_data.available() <= 0) {
				_data.reset();
			}
			
			// parse the html
			_doc = Jsoup.parse(_data, "utf-8", "/");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Context ctx = Context.enter();
		try {
			_scope = ctx.initStandardObjects();
			
			// define classes
			ScriptableObject.defineClass(_scope, ScriptTaskImpl.class);
			ScriptableObject.defineClass(_scope, ScriptUserImpl.class);
			
			// add scope variables
			_scope.put("process", _scope, this);
			_scope.put("user", _scope, new ScriptUserImpl(userName));
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} finally {
			Context.exit();
		}
	}
	
	@Override
	public void invoke() throws ProcessException {
		Context ctx = Context.enter();
		try {
			// initialize scripting
			initialize(ctx);
			
			// invoke the process
			Function inv = (Function) _scope.get("invoke", _scope);
			inv.call(ctx, _scope, _scope, null);
		} catch (IOException ex) {
			throw new ProcessException(ex);
		} catch (RhinoException ex) {
			throw new ProcessException("Script error: " + ex.details() + " (" + ex.sourceName() + "#" + ex.lineNumber() + ":" + ex.columnNumber() + ")");
		} finally {
			Context.exit();
		}
	}
	/*
	@Override
	public Object call(String function, Object... args) throws ProcessException {
		Context ctx = Context.enter();
		try {
			// evaluate the process script
			ctx.evaluateString(_scope, _doc.getElementById("process-script").data(), "process", 1, null);
			
			// invoke the function
			Function f = (Function) _scope.get(function, _scope);
			return f.call(ctx, _scope, _scope, args);
		} finally {
			Context.exit();
		}
	}
	*/
	@Override
	public void completeTask(String taskId, String handler) throws ProcessException {
		Context ctx = Context.enter();
		try {
			if (handler == null || handler.equals(""))
				handler = HandlerType.COMPLETED.toString();
			
			// evaluate the task script
			evaluate(ctx, _doc.getElementById(taskId).getElementById("task-script").data(), "task.js");
			
			// complete the task
			Function comp = (Function) _scope.get(handler, _scope);
			comp.call(ctx, _scope, _scope, null);
		} catch (IOException ex) {
			throw new ProcessException(ex);
		} catch (RhinoException ex) {
			throw new ProcessException("Script error: " + ex.sourceName() + "#" + ex.lineNumber() + ":" + ex.columnNumber());
		} finally {
			Context.exit();
		}
	}

	@Override
	public ScriptTask create(String taskId) throws ProcessException {
		try {
			ScriptTask task = get(taskId);
			task.save();
			
			return task;
		} catch (RepositoryException ex) {
			throw new ProcessException(ex);
		}
	}

	@Override
	public ScriptTask get(String taskId) {
		ScriptTaskImpl task = new ScriptTaskImpl(_taskRepository, _inputRepository, _processId, taskId, _userName);
		
		try {
			task.get();
			task.form = _doc.getElementById(task.scriptId());
				
			// if the task does not exist yet, get the task from the model
			if (!task.exists()) {
				if (task.form != null) {
					
					task.name = task.form.attr("data-name");
					task.role = task.form.attr("data-role");
					task.buttons = JSON.minify(task.form.attr("data-buttons"));
					task.options = JSON.minify(task.form.attr("data-options"));
					
					Elements inputs = task.form.getElementsByTag("input");
					inputs.addAll(task.form.getElementsByTag("textarea"));
					inputs.addAll(task.form.getElementsByTag("select"));
					
					if (inputs != null) {
						for (Element el : inputs) {
							task.setInput(el.attr("name"), null);
						}
					}
				}
			}
		} catch (RepositoryException ex) {
			ex.printStackTrace();
		}
		
		return task;
	}
	
	@Override
	public boolean isComplete(String taskId) {
		ScriptTask task = get(taskId);
		return task.isComplete();
	}
	
	private void initialize(Context ctx) throws IOException {
		ctx.evaluateString(_scope, _doc.getElementById("process-script").data(), "process.js", 1, null);
		ctx.evaluateReader(_scope, new InputStreamReader(getClass().getResourceAsStream("/com/lwolf/wf/resources/script/utils.js")), "utils.js", 1, null);
	}
	
	private void evaluate(Context ctx, String source, String sourceName) throws IOException {
		initialize(ctx);
		ctx.evaluateString(_scope, source, sourceName, 1, null);
	}

}
