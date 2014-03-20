package com.lwolf.scud.locators;

import com.lwolf.scud.exceptions.ResourceException;

public interface IdLocator {
	
	public Integer id(String tableName) throws ResourceException;

}
