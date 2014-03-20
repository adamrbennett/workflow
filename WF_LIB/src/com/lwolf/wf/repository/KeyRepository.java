package com.lwolf.wf.repository;

import com.lwolf.wf.exceptions.RepositoryException;

public interface KeyRepository {
	
	public String get(String apiKey) throws RepositoryException;

}
