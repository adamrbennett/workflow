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

import com.lwolf.wf.dto.ProcessDto;

public interface ProcessResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProcessDto> list(@Context HttpServletRequest request);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ProcessDto create(@Context HttpServletRequest request, ProcessDto process);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{processId}")
	public ProcessDto read(@Context HttpServletRequest request, @PathParam("processId") Integer processId);
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{processId}")
	public ProcessDto update(
		@Context HttpServletRequest request,
		@PathParam("processId") Integer processId,
		ProcessDto process
	);
	
	@DELETE
	@Path("{processId}")
	public void delete(@Context HttpServletRequest request, @PathParam("processId") Integer processId);

}
