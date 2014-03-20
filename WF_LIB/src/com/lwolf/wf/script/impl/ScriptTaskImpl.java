package com.lwolf.wf.script.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;

import com.lwolf.wf.dto.InputDto;
import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.dto.InputMapDto;
import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.script.ScriptTask;

public class ScriptTaskImpl extends ScriptableObject implements ScriptTask {

	private static final long serialVersionUID = 1L;
	
	private TaskRepository _taskRepository;
	private InputRepository _inputRepository;
	
	private Integer _processId;
	private Integer _taskId;
	private String _scriptId;
	private String _userName;
	private Map<String, InputDto> _inputs = new HashMap<String, InputDto>();
	
	public Date createdOn;
	public String createdBy;
	public Date modifiedOn;
	public String modifiedBy;
	public String name;
	public String role;
	public String buttons;
	public String options;
	public String decision;
	public Date dueOn;
	public Date completedOn;
	public Element form;

	@JSConstructor
	public ScriptTaskImpl() {
	}
	
	public ScriptTaskImpl(TaskRepository taskRepository, InputRepository inputRepository, String scriptId, String userName) {
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
		_scriptId = scriptId;
		_userName = userName;
	}
	
	public ScriptTaskImpl(TaskRepository taskRepository, InputRepository inputRepository, Integer processId, String scriptId, String userName) {
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
		_processId = processId;
		_scriptId = scriptId;
		_userName = userName;
	}

	@Override
	public String getClassName() {
		return "Task";
	}

	@Override
	public Object get(String name, Scriptable start) {
		// look for the property in the inputs map
		if (_inputs.containsKey(name)) {
			InputDto input = _inputs.get(name);
			if (input.files != null)
				return input.files;
			else
				return input.value;
		} else {
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

	@SuppressWarnings("unchecked")
	@Override
	public void put(String name, Scriptable start, Object value) {
		try {
			// look for the property in the inputs map
			if (_inputs != null && _inputs.containsKey(name)) {
				InputDto input = _inputs.get(name);
				
				// inputs can only hold lists of files,
				// and no other types of lists ...
				// therefore, if the passed value is a list
				// then it *should* be a list of files ...
				// in any case, the only place we can store a list
				// on an input is in the files member
				if (value instanceof List<?>) {
					input.files = (List<InputFileDto>) value;
					
					// clear any fileIds and get fileDatas
					if (input.files != null) {
						for (InputFileDto file : input.files) {
							_inputRepository.get(file);
							file.fileId = null;
						}
					}
				} else {
					// set the input value
					String val = null;
					if (value != null)
						val = value.toString();
					
					input.value = val;
				}
				
				save();
			} else {
				// look for the field on this
				Field f = null;
				try {
					f = getClass().getDeclaredField(name);
				} catch (NoSuchFieldException ex) {
					f = null;
				}
				if (f != null) {
					try {
						
						if (f.getType() == Date.class) {
							value = (Date) Context.jsToJava(value, Date.class);
						}
						
						f.set(this, value);
						save();
						return;
					} catch (IllegalAccessException ex) {
						ex.printStackTrace();
					}
				}
				
				// if all else fails, fall back to default behavior
				super.put(name, start, value);
			}
		} catch (RepositoryException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void get() throws RepositoryException {
		TaskDto task = new TaskDto(_processId, _scriptId);
		_taskRepository.get(task);
		
		_taskId = task.taskId;
		createdOn = task.createdOn;
		createdBy = task.createdBy;
		modifiedOn = task.modifiedOn;
		modifiedBy = task.modifiedBy;
		name = task.name;
		role = task.role;
		buttons = task.buttons;
		options = task.options;
		decision = task.decision;
		dueOn = task.dueOn;
		completedOn = task.completedOn;
		
		if (task.taskId != null) {
			task.inputs = new InputMapDto(task.processId, task.taskId);
			_inputRepository.get(task.inputs);
			
			_inputs.clear();
			if (task.inputs != null && !task.inputs.isEmpty()) {
				for (InputDto input : task.inputs.values()) {
					_inputs.put(input.name, input);
				}
			}
		}
	}

	@Override
	public void save() throws RepositoryException {
		TaskDto task = new TaskDto(_processId, _taskId, _scriptId);
		task.createdOn = createdOn;
		task.createdBy = createdBy;
		task.modifiedOn = modifiedOn;
		task.modifiedBy = modifiedBy;
		task.name = name;
		task.role = role;
		task.buttons = buttons;
		task.options = options;
		task.decision = decision;
		task.dueOn = dueOn;
		task.completedOn = completedOn;
		
		if (!_inputs.isEmpty()) {
			task.inputs = new InputMapDto(task.processId, task.taskId);
			for (Map.Entry<String, InputDto> entry : _inputs.entrySet()) {
				InputDto input = entry.getValue();
				task.inputs.put(input.name, input);
			}
		}
		
		if (task.taskId != null) {
			task.modifiedOn = new Date();
			task.modifiedBy = _userName;
			
			_taskRepository.update(task);
			_inputRepository.update(task.inputs);
		} else {
			task.createdOn = new Date();
			task.createdBy = _userName;
			
			_taskRepository.add(task);
			
			task.inputs.taskId = task.taskId;
			_inputRepository.add(task.inputs);
			
			get();
		}
		
		_taskId = task.taskId;
	}

	@Override
	public boolean exists() {
		return _taskId != null;
	}
	
	@Override
	public boolean isComplete() {
		return completedOn != null;
	}

	@Override
	public String scriptId() {
		return _scriptId;
	}
	
	@Override
	public String formData() {
		if (form != null)
			return form.html();
		else
			return "";
	}

	@Override
	public void setInput(String name, String value) {
		if (_inputs.containsKey(name)) {
			_inputs.get(name).value = value;
		} else {
			_inputs.put(name, new InputDto(_processId, _taskId, name, value));
		}
	}

}
