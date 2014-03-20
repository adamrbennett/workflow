package com.lwolf.wf.admin.rs.impl;

import java.io.InputStream;
import java.text.MessageFormat;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;

import com.lwolf.wf.admin.http.WorkflowRequest;
import com.lwolf.wf.admin.rs.InputResource;

@Path("/process/{processId}/task/{taskId}/input")
public class InputResourceImpl implements InputResource {
	
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	
	public Response getFile(
		Integer processId,
		Integer taskId,
		Integer inputId,
		Integer fileId
	) {
		try {
			
			String url = MessageFormat.format("/rs/process/{0,number,#}/task/{1,number,#}/input/{2,number,#}/file/{3,number,#}", processId, taskId, inputId, fileId);
			
			HttpResponse response = WorkflowRequest.Get(url).execute().returnResponse();
			InputStream content = response.getEntity().getContent();
			
			String contentDisposition = response.getFirstHeader(CONTENT_DISPOSITION).getValue();
			String contentType = response.getFirstHeader(CONTENT_TYPE).getValue();
			
			return Response
					.ok(content)
					.header(CONTENT_DISPOSITION, contentDisposition)
					.header(CONTENT_TYPE, contentType)
					.build();
		} catch (Throwable th) {
			throw new WebApplicationException(th, Status.INTERNAL_SERVER_ERROR);
		}
	}
}
