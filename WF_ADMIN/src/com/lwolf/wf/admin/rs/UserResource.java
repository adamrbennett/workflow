package com.lwolf.wf.admin.rs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.lwolf.wf.dto.UserDto;

public interface UserResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserDto get(@Context HttpServletRequest request);

}
