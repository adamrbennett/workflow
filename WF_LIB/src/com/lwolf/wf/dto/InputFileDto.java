package com.lwolf.wf.dto;

import java.io.BufferedInputStream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"fileData"})
public class InputFileDto extends DataTransferObject {
	
	private static final long serialVersionUID = 1L;
	
	public Integer processId;
	public Integer taskId;
	public Integer inputId;
	public Integer fileId;
	public String fileName;
	public String fileType;
	public BufferedInputStream fileData;
	public String tempName;
	
	public InputFileDto() {
		
	}
	
	public InputFileDto(Integer processId, Integer taskId, Integer inputId, Integer fileId) {
		this.processId = processId;
		this.taskId = taskId;
		this.inputId = inputId;
		this.fileId = fileId;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().
			append(processId).
			append(taskId).
			append(inputId).
			append(fileId).
			append(fileName).
			append(fileType).
			append(fileData).
			append(tempName).
			toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;
		
		InputFileDto that = (InputFileDto) obj;
		return new EqualsBuilder().
			append(processId, that.processId).
			append(taskId, that.taskId).
			append(inputId, that.inputId).
			append(fileId, that.fileId).
			append(fileName, that.fileName).
			append(fileType, that.fileType).
			append(fileData, that.fileData).
			append(tempName, that.tempName).
			isEquals();
	}

}
