package com.lwolf.wf.admin.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public interface InputResource {
	
	@GET
	@Path("{inputId}/file/{fileId}")
	public Response getFile(
		@PathParam("processId") Integer processId,
		@PathParam("taskId") Integer taskId,
		@PathParam("inputId") Integer inputId,
		@PathParam("fileId") Integer fileId
	);

}
