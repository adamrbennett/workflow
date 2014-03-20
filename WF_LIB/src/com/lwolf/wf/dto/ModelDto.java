package com.lwolf.wf.dto;

import java.io.BufferedInputStream;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"fileData"})
public class ModelDto extends DataTransferObject {
	
	private static final long serialVersionUID = 1L;
	
	public Integer modelId;
	public String name;
	public Integer versionNumber;
	public String fileName;
	public String fileType;
	public BufferedInputStream fileData;
	public String tempName;
	
	public ModelDto() {
		
	}
	
	public ModelDto(Integer modelId) {
		this.modelId = modelId;
	}

}
