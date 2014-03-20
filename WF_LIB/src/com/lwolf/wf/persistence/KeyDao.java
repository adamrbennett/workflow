package com.lwolf.wf.persistence;

import com.lwolf.scud.exceptions.DataException;

public interface KeyDao {
	
	public String getSecretKey(String apiKey) throws DataException;

}
