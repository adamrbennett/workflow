package com.lwolf.wf.admin.rs;

import javax.servlet.http.HttpServletRequest;

import com.lwolf.wf.dto.UserDto;

public abstract class RestResource {
	
	protected UserDto user(HttpServletRequest request) {
		UserDto user = (UserDto) request.getSession().getAttribute("UserContext");
		return user;
	}

}
