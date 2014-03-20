package com.lwolf.wf.dto;

import java.util.List;

public class InputDto extends DataTransferObject {
	
	private static final long serialVersionUID = 1L;
	
	public Integer processId;
	public Integer taskId;
	public Integer inputId;
	public String name;
	public String value;
	public List<InputFileDto> files;
	
	public InputDto() {
		
	}
	
	public InputDto(Integer processId, Integer taskId, String name, String value) {
		this.processId = processId;
		this.taskId = taskId;
		this.name = name;
		this.value = value;
	}

}
