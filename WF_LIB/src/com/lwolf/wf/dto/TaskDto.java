package com.lwolf.wf.dto;

import java.util.Date;

public class TaskDto extends DataTransferObject {
	
	public enum ActionType {
		COMPLETE("complete");
		
		private String type;
		
		private ActionType(String type) {
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
	
	private static final long serialVersionUID = 1L;
	
	public Integer processId;
	public Integer taskId;
	public String scriptId;
	public String name;
	public String role;
	public String buttons;
	public String options;
	public String decision;
	public Date dueOn;
	public Date completedOn;
	
	public String form;
	public InputMapDto inputs;
	
	public String action;
	public String handler;
	
	public TaskDto() {
		
	}
	
	public TaskDto(Integer processId, Integer taskId) {
		this.processId = processId;
		this.taskId = taskId;
		this.inputs = new InputMapDto(processId, taskId);
	}
	
	public TaskDto(Integer processId, String scriptId) {
		this.processId = processId;
		this.scriptId = scriptId;
	}
	
	public TaskDto(Integer processId, Integer taskId, String scriptId) {
		this.processId = processId;
		this.taskId = taskId;
		this.scriptId = scriptId;
		this.inputs = new InputMapDto(processId, taskId);
	}

}
