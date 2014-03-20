package com.lwolf.wf.admin.rs.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import com.lwolf.wf.admin.handlers.WorkflowEntityListResponseHandler;
import com.lwolf.wf.admin.handlers.WorkflowEntityResponseHandler;
import com.lwolf.wf.admin.http.WorkflowRequest;
import com.lwolf.wf.admin.rs.ProcessResource;
import com.lwolf.wf.admin.rs.RestResource;
import com.lwolf.wf.dto.ProcessDto;

@Path("/process")
public class ProcessResourceImpl extends RestResource implements ProcessResource {

	private static final WorkflowEntityListResponseHandler<ProcessDto> listHandler = new WorkflowEntityListResponseHandler<>(ProcessDto.class);
	private static final WorkflowEntityResponseHandler<ProcessDto> entityHandler = new WorkflowEntityResponseHandler<>(ProcessDto.class);
	
	private final String resourceUri = "/rs/process/";
	
	public List<ProcessDto> list() {
		return WorkflowRequest.Get(uri()).execute(listHandler);
	}
	
	public ProcessDto create(HttpServletRequest request, ProcessDto process) {
		process.createdBy = user(request).fullName;
		return WorkflowRequest.Post(uri(), process).execute(entityHandler);
	}
	
	public ProcessDto read(Integer processId) {
		return WorkflowRequest.Get(uri(processId)).execute(entityHandler);
	}
	
	public ProcessDto update(HttpServletRequest request, Integer processId, ProcessDto process) {
		process.modifiedBy = user(request).fullName;
		return WorkflowRequest.Put(uri(processId), process).execute(entityHandler);
	}
	
	public void delete(Integer processId) {
		WorkflowRequest.Delete(uri(processId)).execute();
	}
	
	private String uri() {
		return uri(null);
	}
	
	private String uri(Integer id) {
		StringBuilder uri = new StringBuilder(resourceUri);
		if (id != null)
			uri.append(id);
		return uri.toString();
	}

}
