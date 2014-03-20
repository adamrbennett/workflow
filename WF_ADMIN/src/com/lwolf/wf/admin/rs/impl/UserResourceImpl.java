package com.lwolf.wf.admin.rs.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import com.lwolf.wf.admin.rs.RestResource;
import com.lwolf.wf.admin.rs.UserResource;
import com.lwolf.wf.dto.UserDto;

@Path("/user")
public class UserResourceImpl extends RestResource implements UserResource {
	
	public UserDto get(HttpServletRequest request) {
		return user(request);
	}
}
