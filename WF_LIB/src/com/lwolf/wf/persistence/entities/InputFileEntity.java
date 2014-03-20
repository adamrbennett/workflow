package com.lwolf.wf.persistence.entities;

import java.io.BufferedInputStream;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.lwolf.scud.persistence.Entity;

@Table(name="input_file")
public class InputFileEntity implements Entity {
	
	@Id
	@Column(name="process_id")
	public Integer processId;
	
	@Id
	@Column(name="task_id")
	public Integer taskId;
	
	@Id
	@Column(name="input_id")
	public Integer inputId;
	
	@Id
	@GeneratedValue
	@Column(name="file_id")
	public Integer fileId;

	@Column(name="file_name")
	public String fileName;

	@Column(name="file_type")
	public String fileType;
	
	@Lob
	@Column(name="file_data")
	public BufferedInputStream fileData;
	
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
		
		InputFileEntity that = (InputFileEntity) obj;
		return new EqualsBuilder().
			append(processId, that.processId).
			append(taskId, that.taskId).
			append(inputId, that.inputId).
			append(fileId, that.fileId).
			append(fileName, that.fileName).
			append(fileType, that.fileType).
			append(fileData, that.fileData).
			isEquals();
	}

}
