package com.lwolf.wf.admin.rs.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import com.lwolf.wf.admin.handlers.WorkflowEntityListResponseHandler;
import com.lwolf.wf.admin.handlers.WorkflowEntityResponseHandler;
import com.lwolf.wf.admin.http.WorkflowRequest;
import com.lwolf.wf.admin.rs.RestResource;
import com.lwolf.wf.admin.rs.TaskResource;
import com.lwolf.wf.dto.TaskDto;

@Path("/process/{processId}/task")
public class TaskResourceImpl extends RestResource implements TaskResource {
	
	private static final WorkflowEntityListResponseHandler<TaskDto> listHandler = new WorkflowEntityListResponseHandler<>(TaskDto.class);
	private static final WorkflowEntityResponseHandler<TaskDto> entityHandler = new WorkflowEntityResponseHandler<>(TaskDto.class);
	
	private final String resourceRoot = "/rs/process/";
	
	public List<TaskDto> listByProcess(Integer processId) {
		return WorkflowRequest.Get(uri(processId)).execute(listHandler);
	}
	
	public TaskDto read(Integer processId, Integer taskId) {
		return WorkflowRequest.Get(uri(processId, taskId)).execute(entityHandler);
	}
	
	public TaskDto update(HttpServletRequest request, Integer processId, Integer taskId, TaskDto task) {
		task.modifiedBy = user(request).fullName;
		return WorkflowRequest.Put(uri(processId, taskId), task).execute(entityHandler);
	}
	
	public void delete(Integer processId, Integer taskId) {
		WorkflowRequest.Delete(uri(processId, taskId)).execute();
	}
	
	private String uri(Integer processId) {
		return uri(processId, null);
	}
	
	private String uri(Integer processId, Integer taskId) {
		StringBuilder uri = new StringBuilder(resourceRoot).append(processId).append("/task/");
		if (taskId != null)
			uri.append(taskId);
		
		return uri.toString();
	}

}
