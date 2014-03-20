package com.lwolf.wf.dto;

import java.io.BufferedInputStream;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"data"})
public class ProcessDto extends DataTransferObject {
	
	private static final long serialVersionUID = 1L;
	
	public Integer processId;
	public Integer modelId;
	public String name;
	public BufferedInputStream data;
	public TaskListDto tasks;
	
	public ProcessDto() {
		
	}
	
	public ProcessDto(Integer processId) {
		this.processId = processId;
		this.tasks = new TaskListDto(processId);
	}

}
