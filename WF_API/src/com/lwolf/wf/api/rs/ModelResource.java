package com.lwolf.wf.api.rs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lwolf.wf.dto.ModelDto;

public interface ModelResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModelDto> list(@Context HttpServletRequest request);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ModelDto create(@Context HttpServletRequest request, ModelDto model);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{modelId}")
	public ModelDto read(@Context HttpServletRequest request, @PathParam("modelId") Integer modelId);
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{modelId}")
	public ModelDto update(
		@Context HttpServletRequest request,
		@PathParam("modelId") Integer modelId,
		ModelDto model
	);
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{modelId}")
	public ModelDto delete(@Context HttpServletRequest request, @PathParam("modelId") Integer modelId);
	
	@GET
	@Path("{modelId}/file")
	public Response getFile(@Context HttpServletRequest request, @PathParam("modelId") Integer modelId);

}
