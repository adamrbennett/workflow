package com.lwolf.wf.repository;

import com.lwolf.wf.dto.UserDto;
import com.lwolf.wf.exceptions.RepositoryException;

public interface UserRepository {
	
	public void get(UserDto user) throws RepositoryException;

}
