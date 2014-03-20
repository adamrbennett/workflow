package com.lwolf.wf.admin.rs.impl;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;

import com.lwolf.wf.admin.handlers.WorkflowEntityListResponseHandler;
import com.lwolf.wf.admin.handlers.WorkflowEntityResponseHandler;
import com.lwolf.wf.admin.http.WorkflowRequest;
import com.lwolf.wf.admin.rs.ModelResource;
import com.lwolf.wf.admin.rs.RestResource;
import com.lwolf.wf.dto.ModelDto;

@Path("/model")
public class ModelResourceImpl extends RestResource implements ModelResource {
	
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	
	private static final WorkflowEntityListResponseHandler<ModelDto> listHandler = new WorkflowEntityListResponseHandler<>(ModelDto.class);
	private static final WorkflowEntityResponseHandler<ModelDto> entityHandler = new WorkflowEntityResponseHandler<>(ModelDto.class);
	
	private final String resourceUri = "/rs/model/";
	
	public List<ModelDto> list() {
		return WorkflowRequest.Get(uri()).execute(listHandler);
	}
	
	public ModelDto create(HttpServletRequest request, ModelDto model) {
		model.createdBy = user(request).fullName;
		return WorkflowRequest.Post(uri(), model).execute(entityHandler);
	}
	
	public ModelDto read(Integer modelId) {
		return WorkflowRequest.Get(uri(modelId)).execute(entityHandler);
	}
	
	public ModelDto update(HttpServletRequest request, Integer modelId, ModelDto model) {
		model.modifiedBy = user(request).fullName;
		return WorkflowRequest.Put(uri(modelId), model).execute(entityHandler);
	}
	
	public ModelDto delete(Integer modelId) {
		return WorkflowRequest.Delete(uri(modelId)).execute(entityHandler);
	}
	
	public Response getFile(Integer modelId) {
		try {
			
			StringBuilder url = new StringBuilder(resourceUri);
			url.append(modelId).append("/file");
			
			HttpResponse response = WorkflowRequest.Get(url.toString()).execute().returnResponse();
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
